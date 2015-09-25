/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.agiletec.plugins.jpldap.aps.system.services.user;

import java.io.IOException;
import java.net.ConnectException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.PartialResultException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.StartTlsRequest;
import javax.naming.ldap.StartTlsResponse;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpldap.aps.system.LdapSystemConstants;
import com.agiletec.plugins.jpldap.aps.system.services.user.tls.MyTLSHostnameVerifier;
import com.agiletec.plugins.jpldap.aps.system.services.user.tls.MyX509TrustManager;

/**
 * The Data Access Object for LdapUser.
 * @author E.Santoboni
 */
public class LdapUserDAO implements ILdapUserDAO {

	private static final Logger _logger = LoggerFactory.getLogger(LdapUserDAO.class);
	
    @Override
	public void addUser(UserDetails user) {
		this.checkDn();
		DirContext dirCtx = null;
		try {
			dirCtx = this.getDirContext();
			Attributes attrs = this.createNewEntry(user);
			dirCtx.createSubcontext(this.getEntryName(user), attrs);
		} catch (ConnectException e) {
			_logger.error("Error while adding user '{}' : Directory not available", user.getUsername(), e);
        } catch (CommunicationException e) {
         	_logger.error("Error while adding user '{}' : Directory not available", user.getUsername(), e);
        } catch (PartialResultException e) {
        	_logger.error("Error while adding user '{}' : Directory not available", user.getUsername(), e);
        } catch (Throwable t) {
            throw new RuntimeException("Error while adding user '" + user.getUsername() + "'", t);
        } finally {
			this.closeDirContext(dirCtx);
		}
	}
	
	private void checkDn() {
        DirContext dirCtx = null;
		Context result = null;
		try {
			String filterExpr = "(&(" + this.getBaseDn() + "))";
            SearchResult sr = this.search(filterExpr);
            if (sr != null) {
                return;
            }
            dirCtx = this.getDirContext();
			Attributes attrs = new BasicAttributes(true); // case-ignore
			Attribute objclass = new BasicAttribute("objectClass");
			String[] classes = this.getOuObjectClasses();
			//if (null == classes) {
			//	classes = new String[]{"organizationalUnit", "top"};
			//}
			for (int i = 0; i < classes.length; i++) {
				objclass.add(classes[i]);
			}
			attrs.put(objclass);
			result = dirCtx.createSubcontext(this.getBaseDn(), attrs);
		} catch (ConnectException e) {
			_logger.error("Error while adding new OU : Directory not available", e);
        } catch (CommunicationException e) {
        	_logger.error("Error while adding new OU : Directory not available", e);
        } catch (PartialResultException e) {
        	_logger.error("Error while adding new OU : Directory not available", e);
        } catch (Throwable t) {
        	_logger.error("Error while adding new OU.", t);
            throw new RuntimeException("Error while adding new OU ", t);
        } finally {
			if (null != result) {
				try {
					result.close();
				} catch (NamingException ex) {}
			}
			this.closeDirContext(dirCtx);
		}
    }
	
	private Attributes createNewEntry(UserDetails user) {
		Attributes attrs = new BasicAttributes(true);
		Attribute oc = new BasicAttribute("objectClass");
		String[] classes = this.getUserObjectClasses();
		//if (null == classes) {
		//	classes = new String[]{this.getUserObjectClass(), "top"};
		//}
		for (int i = 0; i < classes.length; i++) {
			oc.add(classes[i]);
		}
        attrs.put(oc);
		Attribute cn = new BasicAttribute(this.getUserRealAttributeName(), user.getUsername());
		String sEncrypted = this.encryptLdapPassword(user.getPassword());
        Attribute password = new BasicAttribute(this.getUserPasswordAttributeName(), sEncrypted);
        Attribute uid = new BasicAttribute(this.getUserIdAttributeName(), user.getUsername());
        attrs.put(cn);
        attrs.put(password);
        attrs.put(uid);
        return attrs;
	}
	
	private String encryptLdapPassword(String password) {
		String sEncrypted = password;
		if ((password != null) && (password.length() > 0)) {
			String algorithm = this.getUserPasswordAlgorithm().toUpperCase();
			if (algorithm.equalsIgnoreCase(LdapSystemConstants.USER_PASSWORD_ALGORITHM_MD5) 
					|| algorithm.equalsIgnoreCase(LdapSystemConstants.USER_PASSWORD_ALGORITHM_SHA)) {
				try {
					MessageDigest md = MessageDigest.getInstance(algorithm);
					md.update(password.getBytes("UTF-8"));
					sEncrypted = "{" + algorithm + "}" + (new BASE64Encoder()).encode(md.digest());
				} catch (Exception e) {
					sEncrypted = password;
					_logger.error("Error while encrypting Ldap Password", e);
				}
			}
		}
		return sEncrypted;
	}
	
	private String getEntryName(UserDetails user) {
		StringBuilder entryName = new StringBuilder();
		entryName.append(this.getUserIdAttributeName()).append("=").append(user.getUsername());
		String baseDN = this.getBaseDn();
		if (null != baseDN && baseDN.trim().length() > 0) {
			entryName.append(",").append(baseDN);
		}
		return entryName.toString();
	}
    
    @Override
    public void deleteUser(String username) {
		SearchResult res = this.searchUserByFilterExpr(username);
		if (res != null) {
			this.removeEntry(res.getName());
		}
    }
    
    @Override
    public void deleteUser(UserDetails user) {
        this.deleteUser(user.getUsername());
    }
	
	private void removeEntry(String uid) {
		DirContext dirCtx = null;
		try {
			dirCtx = this.getDirContext();
			dirCtx.destroySubcontext(uid);
		} catch (ConnectException e) {
            _logger.error("Error while deleting user '{}' : Directory not available", uid, e);
        } catch (CommunicationException e) {
        	_logger.error("Error while deleting user '{}' : Directory not available", uid, e);
        } catch (PartialResultException e) {
        	_logger.error("Error while deleting user '{}' : Directory not available", uid, e);
        } catch (Throwable t) {
        	_logger.error("Error while deleting user '{}'", t);
            throw new RuntimeException("Error while deleting user '" + uid + "'", t);
        } finally {
            closeDirContext(dirCtx);
        }
	}
    
    @Override
    public void updateUser(UserDetails user) {
        this.updateUser(user.getUsername(), user.getPassword());
    }
    
    @Override
    public void changePassword(String username, String password) {
        this.updateUser(username, password);
    }
	
	private void updateUser(String username, String password) {
		SearchResult res = this.searchUserByFilterExpr(username);
		if (res != null) {
			ModificationItem[] mods = new ModificationItem[1];
			String sEncrypted = this.encryptLdapPassword(password);
			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute(this.getUserPasswordAttributeName(), sEncrypted));
			this.editEntry(res.getName(), mods);
		}
	}
	
	protected void editEntry(String uid, ModificationItem[] mods) {
		DirContext dirCtx = null;
		try {
			dirCtx = this.getDirContext();
			dirCtx.modifyAttributes(uid, mods);
		} catch (ConnectException e) {
			_logger.error("Error while editing user '{}' : Directory not available", uid, e);
        } catch (CommunicationException e) {
        	_logger.error("Error while editing user '{}' : Directory not available", uid, e);
        } catch (PartialResultException e) {
        	_logger.error("Error while editing user '{}' : Directory not available", uid, e);
        } catch (Throwable t) {
            throw new RuntimeException("Error while editing user '" + uid + "'", t);
        } finally {
            closeDirContext(dirCtx);
        }
	}
    
    @Override
    public void updateLastAccess(String username) {
        ApsSystemUtils.getLogger().error("Method not supported");
    }
    
    @Override
    public UserDetails loadUser(String username, String password) {
        boolean isAuth = this.isAuth(username, password);
        if (isAuth) {
            LdapUser user = (LdapUser) this.loadUser(username);
            user.setPassword(password);
            return user;
        }
        return null;
    }
    
    @Override
    public UserDetails loadUser(String username) {
        LdapUser user = null;
        try {
            SearchResult sr = this.searchUserByFilterExpr(username);
            if (sr == null) {
                return null;
            }
            Attributes attrs = sr.getAttributes();
            user = this.createUserFromAttributes(attrs);
        } catch (Throwable t) {
        	_logger.error("Error loading user {}", username, t);
            throw new RuntimeException("Error loading user " + username, t);
        }
        return user;
    }
    
    protected SearchResult searchUserByFilterExpr(String uid) {
        String filterExpr = "(&(objectClass=" + this.getUserObjectClass() + ")(" + this.getUserIdAttributeName() + "=" + uid + ")" + this.getFilterGroupBlock() + ")";
        return search(filterExpr);
    }
    
    private SearchResult search(String filterExpr) {
        SearchResult res = null;
        DirContext dirCtx = null;
        try {
            dirCtx = getDirContext();
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> answer = dirCtx.search("", filterExpr, ctls);
            if (answer.hasMore()) {
                res = answer.next();
            }
        } catch (ConnectException e) {
        	_logger.error("Extracting SearchResult : Directory not available", e);
        } catch (CommunicationException e) {
        	_logger.error("Extracting SearchResult : Directory not available", e);
        } catch (PartialResultException e) {
        	_logger.error("Extracting SearchResult : Directory not available", e);
        } catch (Throwable t) {
            throw new RuntimeException("Error extracting serach result ", t);
        } finally {
            closeDirContext(dirCtx);
        }
        return res;
    }
    
    protected boolean isAuth(String username, String password) {
        Logger log = ApsSystemUtils.getLogger();
        boolean isAuth = false;
        DirContext dirCtx = null;
        try {
            dirCtx = this.getDirContext();
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> answer = dirCtx.search("", "(&(objectClass=" + this.getUserObjectClass() + ")(" + this.getUserIdAttributeName() + "=" + username + ")" + this.getFilterGroupBlock() + ")", ctls);
            if (answer.hasMoreElements()) {
                dirCtx.close();
                try {
                    SearchResult searchResult = answer.nextElement();
                    String userdn = searchResult.getNameInNamespace();
                    answer.close();
                    Hashtable<String, String> clonedParams = new Hashtable<String, String>();
                    clonedParams.putAll(this.getParams(false));
                    clonedParams.put(Context.SECURITY_PRINCIPAL, userdn);
                    clonedParams.put(Context.SECURITY_CREDENTIALS, password);
                    dirCtx = new InitialDirContext(clonedParams);
                    answer = dirCtx.search("", "(&(objectClass=" + this.getUserObjectClass() + ")(" + this.getUserIdAttributeName() + "=" + username + ")" + this.getFilterGroupBlock() + ")", ctls);
                    isAuth = answer.hasMoreElements();
                    answer.close();
                    if (isAuth) {
                        log.info("Found User '" + username + "'");
                    }
                } catch (Throwable t) {
                    log.info("Bad Login for User '" + username + "'");
                }
            } else {
                log.info("Not found User '" + username + "'");
            }
            answer.close();
        } catch (ConnectException e) {
        	_logger.error("Directory not available", e);
        } catch (CommunicationException e) {
        	_logger.error("Directory not available", e);
        } catch (NamingException e) {
        	_logger.error("user authentication '{}' : Directory not available", username, e);
        } catch (Throwable t) {
        	_logger.error("Error verifying user authentication {}", username, t);
            throw new RuntimeException("Error verifying user authentication " + username, t);
        } finally {
            this.closeDirContext(dirCtx);
        }
        return isAuth;
    }
    
    @Override
    public List<UserDetails> loadUsers() {
        String filterExpr = "(&(objectClass=" + this.getUserObjectClass() + ")(" + this.getUserIdAttributeName() + "=*)" + this.getFilterGroupBlock() + ")";
        return this.searchUsersByFilterExpr(filterExpr);
    }
    
    @Override
    public List<String> loadUsernames() {
        String filterExpr = "(&(objectClass=" + this.getUserObjectClass() + ")(" + this.getUserIdAttributeName() + "=*)" + this.getFilterGroupBlock() + ")";
        return this.searchUsernamesByFilterExpr(filterExpr);
    }
    
    protected LdapUser createUserFromAttributes(Attributes attrs) throws NamingException {
        LdapUser user = new LdapUser();
        String username = (String) attrs.get(this.getUserIdAttributeName()).get(0);
        user.setUsername(username);
        if (null != attrs.get(this.getUserPasswordAttributeName())) {
            byte[] bytesPwd = (byte[]) attrs.get(this.getUserPasswordAttributeName()).get(0);
            String userPassword = new String(bytesPwd);
            user.setPassword(userPassword);
        }
        user.setAttributes(attrs);
        return user;
    }
    
    @Override
    public List<UserDetails> searchUsers(String text) {
		if (null == text || text.trim().length() == 0) {
			return loadUsers();
		}
        String filterExpr = "(&(objectClass=" + this.getUserObjectClass() + ")" + this.getFilterGroupBlock() + "(|(" + this.getUserIdAttributeName() + "=*" + text + "*)(cn=*" + text + "*)))";
        return this.searchUsersByFilterExpr(filterExpr);
    }
    
    @Override
    public List<String> searchUsernames(String text) {
        if (null == text || text.trim().length() == 0) {
			return loadUsernames();
		}
        String filterExpr = "(&(objectClass=" + this.getUserObjectClass() + ")" + this.getFilterGroupBlock() + "(|(" + this.getUserIdAttributeName() + "=*" + text + "*)(cn=*" + text + "*)))";
        return this.searchUsernamesByFilterExpr(filterExpr);
    }
    
    protected String getFilterGroupBlock() {
        String block = "";
        String filterGroup = this.getFilterGroup();
        if (null != filterGroup && filterGroup.trim().length() > 0) {
            block = "(" + this.getFilterGroupAttributeName() + "=" + filterGroup + ")";
        }
        return block;
    }
    
    protected List<String> searchUsernamesByFilterExpr(String filterExpr) {
        List<String> usernames = new ArrayList<String>();
        DirContext dirCtx = null;
        try {
            dirCtx = this.getDirContext();
            SearchControls ctls = new SearchControls();
            String[] attrIDs = {this.getUserIdAttributeName()};
            ctls.setReturningAttributes(attrIDs);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            if (this.getSearchResultMaxSize() > 0) {
                ctls.setCountLimit(this.getSearchResultMaxSize());
            }
            NamingEnumeration<SearchResult> answer = dirCtx.search("", filterExpr, ctls);
            while (answer.hasMore()) {
                SearchResult res = answer.next();
                Attributes attrs = res.getAttributes();
				usernames.add((String) attrs.get(this.getUserIdAttributeName()).get(0));
            }
        } catch (ConnectException e) {
        	_logger.error("Error loading users : Directory not available", e);
        } catch (CommunicationException e) {
        	_logger.error("Error loading users : Directory not available", e);
        } catch (NamingException e) {
        	_logger.error("Error loading users : Directory not available", e);
        } catch (Throwable t) {
        	_logger.error("Error loading users", t);
            throw new RuntimeException("Error loading users", t);
        } finally {
            this.closeDirContext(dirCtx);
        }
        return usernames;
    }
    
    protected List<UserDetails> searchUsersByFilterExpr(String filterExpr) {
        List<UserDetails> users = new ArrayList<UserDetails>();
        DirContext dirCtx = null;
        try {
            dirCtx = this.getDirContext();
            SearchControls ctls = new SearchControls();
            String[] attrIDs = {this.getUserIdAttributeName(), this.getUserPasswordAttributeName(), "cn", "sn"};
            ctls.setReturningAttributes(attrIDs);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            if (this.getSearchResultMaxSize() > 0) {
                ctls.setCountLimit(this.getSearchResultMaxSize());
            }
            NamingEnumeration<SearchResult> answer = dirCtx.search("", filterExpr, ctls);
            while (answer.hasMore()) {
                SearchResult res = answer.next();
                Attributes attrs = res.getAttributes();
                LdapUser user = this.createUserFromAttributes(attrs);
                users.add(user);
            }
        } catch (ConnectException e) {
        	_logger.error("Error loading users : Directory not available", e);
        } catch (CommunicationException e) {
        	_logger.error("Error loading users : Directory not available", e);
        } catch (NamingException e) {
        	_logger.error("Error loading users : Directory not available", e);
        } catch (Throwable t) {
        	_logger.error("Error loading users ", t);
            throw new RuntimeException("Error loading users", t);
        } finally {
            this.closeDirContext(dirCtx);
        }
        return users;
    }
    
    protected DirContext getDirContext() throws NamingException, CommunicationException, ConnectException {
        DirContext dirCtx = null;
        try {
			if (this.isTlsSecurityConnection()) {
				dirCtx = new InitialLdapContext(this.getParams(true), null);
				StartTlsResponse tls = (StartTlsResponse) ((InitialLdapContext) dirCtx).extendedOperation(new StartTlsRequest());
				if (this.isTlsFreeSecurityConnection()) {
					// Set the (our) HostVerifier
					tls.setHostnameVerifier(new MyTLSHostnameVerifier());
					SSLSocketFactory sslsf = null;
					try {
						TrustManager[] tm = new TrustManager [] {new MyX509TrustManager()};
						SSLContext sslC = SSLContext.getInstance("TLS");
						sslC.init(null, tm, null);
						sslsf = sslC.getSocketFactory();
					} catch(NoSuchAlgorithmException nSAE) {
						_logger.error("error Hier: {}", nSAE.getMessage(), nSAE);
					} catch(KeyManagementException kME) {
						_logger.error("error Hier: {}", kME.getMessage(), kME);
					}
					tls.negotiate(sslsf);
				} else {
					tls.negotiate();
				}
				if (null != this.getSecurityPrincipal() && null != this.getSecurityCredentials()) {
					dirCtx.addToEnvironment(Context.SECURITY_PRINCIPAL, this.getSecurityPrincipal());
					dirCtx.addToEnvironment(Context.SECURITY_CREDENTIALS, this.getSecurityCredentials());
					dirCtx.addToEnvironment(Context.SECURITY_AUTHENTICATION, "simple");
				}
			} else {
				dirCtx = new InitialDirContext(this.getParams(false));
			}
        } catch (IOException ex) {
        	_logger.error("error in getDirContext", ex);
		} catch (NamingException e) {
            throw e;
        }
        return dirCtx;
    }
    
    protected void closeDirContext(DirContext dirCtx) {
        if (null == dirCtx) return;
		try {
			if (dirCtx instanceof InitialLdapContext) {
				((StartTlsResponse) ((InitialLdapContext) dirCtx).getExtendedResponse()).close();
			}
            dirCtx.close();
        } catch (IOException ex) {
        	_logger.error("Error closing DirContext", ex);
		} catch (NamingException e) {
			_logger.error("Error closing DirContext", e);
            throw new RuntimeException("Error closing DirContext", e);
		}
    }
    
    protected Hashtable<String, String> getParams(boolean isTls) {
        Hashtable<String, String> params = new Hashtable<String, String>(11);
        params.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        params.put(Context.PROVIDER_URL, this.getProviderUrl());
        if (null != this.getSecurityPrincipal() && null != this.getSecurityCredentials()) {
			if (!isTls) {
				params.put(Context.SECURITY_PRINCIPAL, this.getSecurityPrincipal());
				params.put(Context.SECURITY_CREDENTIALS, this.getSecurityCredentials());
				params.put(Context.SECURITY_AUTHENTICATION, "simple");
			}
			params.put(Context.REFERRAL, "ignore");
        }
		if (this.isSslSecurityConnection()) {
			params.put(Context.SECURITY_PROTOCOL, "ssl");
		}
        return params;
    }
    
    protected String getProviderUrl() {
        return this.getConfigParam(LdapSystemConstants.PROVIDER_URL_PARAM_NAME);
    }
	
	private boolean isSslSecurityConnection() {
		return this.getSecurityConnectionType().equalsIgnoreCase(LdapSystemConstants.SECURITY_CONNECTION_TYPE_SSL);
	}
    
	private boolean isTlsSecurityConnection() {
		return (this.isTlsFreeSecurityConnection() || 
				this.getSecurityConnectionType().equalsIgnoreCase(LdapSystemConstants.SECURITY_CONNECTION_TYPE_TLS));
	}
    
	private boolean isTlsFreeSecurityConnection() {
		return this.getSecurityConnectionType().equalsIgnoreCase(LdapSystemConstants.SECURITY_CONNECTION_TYPE_TLS_FREE);
	}
    
    protected String getSecurityConnectionType() {
        String type = this.getConfigParam(LdapSystemConstants.SECURITY_CONNECTION_TYPE_PARAM_NAME);
		if (null != type) {
			if (type.equalsIgnoreCase(LdapSystemConstants.SECURITY_CONNECTION_TYPE_SSL)) {
				return LdapSystemConstants.SECURITY_CONNECTION_TYPE_SSL;
			} else if (type.equalsIgnoreCase(LdapSystemConstants.SECURITY_CONNECTION_TYPE_TLS)) {
				return LdapSystemConstants.SECURITY_CONNECTION_TYPE_TLS;
			}
		}
		return LdapSystemConstants.SECURITY_CONNECTION_TYPE_NONE;
    }
    
    protected String getSecurityPrincipal() {
        return this.getConfigParam(LdapSystemConstants.SECURITY_PRINCIPAL_PARAM_NAME);
    }
    
    protected String getSecurityCredentials() {
        return this.getConfigParam(LdapSystemConstants.SECURITY_CREDENTIALS_PARAM_NAME);
    }
    
    protected String getUserObjectClass() {
        String objectClass = this.getConfigParam(LdapSystemConstants.USER_OBJECT_CLASS_PARAM_NAME);
        if (null == objectClass) {
            objectClass = "posixAccount";
        }
        return objectClass;
    }
    
    protected String getUserIdAttributeName() {
        String attributeName = this.getConfigParam(LdapSystemConstants.USER_ID_ATTRIBUTE_NAME_PARAM_NAME);
		if (null == attributeName) {
            attributeName = "uid";
        }
        return attributeName;
    }
    
    protected String getFilterGroup() {
        return this.getConfigParam(LdapSystemConstants.FILTER_GROUP_PARAM_NAME);
    }
    
    protected String getFilterGroupAttributeName() {
        return this.getConfigParam(LdapSystemConstants.FILTER_GROUP_ATTRIBUTE_NAME_PARAM_NAME);
    }
    
    protected int getSearchResultMaxSize() {
        String maxSizeString = this.getConfigParam(LdapSystemConstants.SEARCH_RESULT_MAX_SIZE_PARAM_NAME);
        int maxSize = -1;
        try {
            maxSize = Integer.parseInt(maxSizeString);
        } catch (Exception e) {
            maxSize = -1;
        }
        return maxSize;
    }
	
	@Override
	public boolean isWriteUserEnable() {
		String activeString = this.getConfigManager().getParam(LdapSystemConstants.USER_EDITING_ACTIVE_PARAM_NAME);
        Boolean active = Boolean.parseBoolean(activeString);
        return active.booleanValue();
	}
	
	protected String getBaseDn() {
        return this.getConfigParam(LdapSystemConstants.USER_BASE_DN_PARAM_NAME);
    }
	
	protected String getUserRealAttributeName() {
        String attributeName = this.getConfigParam(LdapSystemConstants.USER_REAL_ATTRIBUTE_NAME_PARAM_NAME);
		if (null == attributeName) {
            attributeName = "cn";
        }
        return attributeName;
    }
	
	protected String getUserPasswordAttributeName() {
        String attributeName = this.getConfigParam(LdapSystemConstants.USER_PASSWORD_ATTRIBUTE_NAME_PARAM_NAME);
		if (null == attributeName) {
            attributeName = "userPassword";
        }
        return attributeName;
    }
	
	protected String getUserPasswordAlgorithm() {
        String algorithm = this.getConfigParam(LdapSystemConstants.USER_PASSWORD_ALGORITHM_PARAM_NAME);
		if (null == algorithm) {
            algorithm = LdapSystemConstants.USER_PASSWORD_ALGORITHM_NONE;
        }
        return algorithm;
    }
	
	protected String[] getUserObjectClasses() {
		String csv = this.getConfigParam(LdapSystemConstants.USER_OBJECT_CLASSES_CSV_PARAM_NAME);
		if (null == csv || csv.trim().length() == 0) {
			csv = this.getUserObjectClass() + ",top";
		}
		return csv.split(",");
    }
	
	protected String[] getOuObjectClasses() {
		String csv = this.getConfigParam(LdapSystemConstants.OU_OBJECT_CLASSES_CSV_PARAM_NAME);
		if (null == csv || csv.trim().length() == 0) {
			csv = "top,organizationalUnit";
		}
		return csv.split(",");
    }
	
    private String getConfigParam(String paramName) {
        String paramValue = this.getConfigManager().getParam(paramName);
        if (null == paramValue || paramValue.trim().length() == 0) return null;
        return paramValue.trim();
    }
    
    protected ConfigInterface getConfigManager() {
        return _configManager;
    }
    public void setConfigManager(ConfigInterface configManager) {
        this._configManager = configManager;
    }
    
    private ConfigInterface _configManager;
    
}