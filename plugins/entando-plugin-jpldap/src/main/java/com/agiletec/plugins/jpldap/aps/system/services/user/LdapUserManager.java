/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserDAO;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.system.services.user.UserManager;
import com.agiletec.plugins.jpldap.aps.system.LdapSystemConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Manager for LdapUser.
 * @author E.Santoboni
 */
public class LdapUserManager extends UserManager implements ILdapUserManager {

	private static final Logger _logger = LoggerFactory.getLogger(LdapUserManager.class);
	
    @Override
    public UserDetails getUser(String username, String password) throws ApsSystemException {
        if (!isActive()) {
			return super.getUser(username, password);
		}
        UserDetails user = null;
        try {
            user = this.getLdapUserDAO().loadUser(username, password);
        } catch (Throwable t) {
        	_logger.error("error in getUser: {}", username, t);
        }
        if (null != user) {
            return user;
        } else {
            return super.getUser(username, password);
        }
    }
    
    @Override
    public UserDetails getUser(String username) throws ApsSystemException {
        if (!isActive()) {
			return super.getUser(username);
		}
        UserDetails user = null;
        try {
            user = this.getLdapUserDAO().loadUser(username);
        } catch (Throwable t) {
        	_logger.error("error in getUser: {}", username, t);
        }
        if (null != user) {
            return user;
        } else {
            return super.getUser(username);
        }
    }
    
    @Override
    public List<UserDetails> getUsers() throws ApsSystemException {
        return this.searchUsers(null);
    }
    
    @Override
    public List<UserDetails> searchUsers(String text) throws ApsSystemException {
        List<UserDetails> users = super.searchUsers(text);
		if (!isActive()) {
			return super.searchUsers(text);
		}
        try {
            users.addAll(this.getLdapUserDAO().searchUsers(text));
        } catch (Throwable t) {
        	_logger.error("error searching users by text '{}'", text , t);
        }
        if (null != users) {
            BeanComparator comparator = new BeanComparator("username");
            Collections.sort(users, comparator);
        }
        return users;
    }
    
    @Override
    public List<String> getUsernames() throws ApsSystemException {
        return this.searchUsernames(null);
    }
    
    @Override
    public List<String> searchUsernames(String text) throws ApsSystemException {
        List<String> usernames = super.searchUsernames(text);
		if (!isActive()) {
			return usernames;
		}
        try {
            usernames.addAll(this.getLdapUserDAO().searchUsernames(text));
        } catch (Throwable t) {
        	_logger.error("error searching usernames by text '{}'", text , t);
        }
        if (null != usernames) {
            Collections.sort(usernames);
        }
        return usernames;
    }
    
    @Override
    public List<UserDetails> searchUsers(String text, Boolean entandoUser) throws ApsSystemException {
        if (!isActive()) {
			if (null == entandoUser || !entandoUser) {
				return new ArrayList<UserDetails>();
			}
			return super.searchUsers(text);
		}
        try {
            if (entandoUser == null) {
                return this.searchUsers(text);
            }
            IUserDAO userDAO = (entandoUser) ? super.getUserDAO() : this.getLdapUserDAO();
            if (text == null || text.trim().length() == 0) {
                return userDAO.loadUsers();
            }
            return userDAO.searchUsers(text);
        } catch (Throwable t) {
        	_logger.error("error in searchUsers", t);
            throw new ApsSystemException("Error loading users", t);
        }
    }
	
	@Override
	public void addUser(UserDetails user) throws ApsSystemException {
		if (!isActive() || !isWriteUserEnable()) {
			super.addUser(user);
			return;
		}
		try {
			this.getLdapUserDAO().addUser(user);
		} catch (Throwable t) {
			_logger.error("Error adding LDAP User {}",  user.getUsername(), t);
			throw new ApsSystemException("Error adding LDAP User" + user.getUsername(), t);
		}
	}
	
	@Override
	public void changePassword(String username, String password) throws ApsSystemException {
		if (!isActive() || !isWriteUserEnable()) {
			super.changePassword(username, password);
			return;
		}
		try {
			this.getLdapUserDAO().changePassword(username, password);
		} catch (Throwable t) {
			_logger.error("Error updating the password of the LDAP User for {}", username, t);
			throw new ApsSystemException("Error updating the password of the LDAP User" + username, t);
		}
	}
	
	@Override
	public void removeUser(UserDetails user) throws ApsSystemException {
		if (!isActive() || !isWriteUserEnable()) {
			super.removeUser(user);
		}
		try {
			this.getLdapUserDAO().deleteUser(user);
		} catch (Throwable t) {
			_logger.error("Error deleting a ldap user", t);
			throw new ApsSystemException("Error deleting a ldap user", t);
		}
	}
	
	@Override
	public void removeUser(String username) throws ApsSystemException {
		if (!isActive() || !isWriteUserEnable()) {
			super.removeUser(username);
		}
		try {
			this.getLdapUserDAO().deleteUser(username);
		} catch (Throwable t) {
			_logger.error("Error deleting a ldap user {}", username, t);
			throw new ApsSystemException("Error deleting a ldap user", t);
		}
	}
	
	@Override
	public void updateUser(UserDetails user) throws ApsSystemException {
		if (!isActive() || !isWriteUserEnable()) {
			super.updateUser(user);
			return;
		}
		try {
			this.getLdapUserDAO().updateUser(user);
		} catch (Throwable t) {
			_logger.error("Error updating a ldap user", t);
			throw new ApsSystemException("Error updating a ldap user", t);
		}
	}
	
    private boolean isActive() {
        String activeString = this.getConfigManager().getParam(LdapSystemConstants.ACTIVE_PARAM_NAME);
        Boolean active = Boolean.parseBoolean(activeString);
        return active.booleanValue();
    }
	
	@Override
	public boolean isWriteUserEnable() {
		return this.getLdapUserDAO().isWriteUserEnable();
	}
    
    protected ILdapUserDAO getLdapUserDAO() {
        return _ldapUserDAO;
    }
    public void setLdapUserDAO(ILdapUserDAO userLdapDAO) {
        this._ldapUserDAO = userLdapDAO;
    }
    
    public ILdapUserDAO _ldapUserDAO;
    
}