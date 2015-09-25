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
package com.agiletec.plugins.jpuserreg.aps.system.services.userreg;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.user.AbstractUser;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpuserreg.aps.JpUserRegSystemConstants;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model.IUserRegConfig;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model.Template;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.parse.UserRegConfigDOM;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.util.ShaEncoder;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager for operations of user account registration.
 * @author S.Puddu
 * @author E.Mezzano
 * @author G.Cocco
 */
@Aspect
public class UserRegManager extends AbstractService implements IUserRegManager {

	private static final Logger _logger = LoggerFactory.getLogger(UserRegManager.class);
	
	@Override
	public void init() throws Exception {
		try {
			this.loadConfigs();
			_logger.debug("{} ready", this.getClass().getName());
		} catch (Throwable t) {
			_logger.error("{} Manager: Error on initialization", this.getClass().getName(), t);
		}
	}

	private void loadConfigs() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpUserRegSystemConstants.USER_REG_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration Item not found: " + JpUserRegSystemConstants.USER_REG_CONFIG_ITEM);
			}
			UserRegConfigDOM userRegConfigDom = new UserRegConfigDOM();
			IUserRegConfig config = userRegConfigDom.extractConfig(xml);
			this.setUserRegConfig(config);
		} catch (Throwable t) {
			_logger.error("Error in init", t);
			throw new ApsSystemException("Error in init", t);
		}
	}

	@Override
	public IUserRegConfig getUserRegConfig() {
		return this.getConfig().clone();
	}
	@Override
	public void saveUserRegConfig(IUserRegConfig config) throws ApsSystemException {
		try {
			String xml = new UserRegConfigDOM().createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpUserRegSystemConstants.USER_REG_CONFIG_ITEM, xml);
			this.setUserRegConfig(config);
		} catch (Throwable t) {
			_logger.error("Errore in fase di aggiornamento configurazione user reg", t);
			throw new ApsSystemException("Errore in fase di aggiornamento configurazione user reg", t);
		}
	}
	protected IUserRegConfig getConfig() {
		return _userRegConfig;
	}
	protected void setUserRegConfig(IUserRegConfig regProfileConfig) {
		this._userRegConfig = regProfileConfig;
		this.setTokenValidityInMillis(regProfileConfig.getTokenValidityMinutes()*60000L);
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.user.IUserManager.removeUser(..)) && args(key)")
	public void deleteUser(Object key) {
		String username = null;
		if (key instanceof String) {
			username = key.toString();
		} else if (key instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) key;
			username = userDetails.getUsername();
		}
		if (username != null) {
			try {
				this.getUserRegDAO().clearTokenByUsername(username);
			} catch (Throwable t) {
				_logger.error("Error removing username {}", username, t);
			}
		}
	}
	
	@Override
	public void regAccount(IUserProfile userProfile) throws ApsSystemException {
		try {
			User user = new User();
			user.setDisabled(true);
			user.setUsername(userProfile.getUsername());
			user.setProfile(userProfile);
			user.setPassword("changeit");
			String token = this.createToken(userProfile.getUsername());
			this.sendAlertRegProfile((IUserProfile) user.getProfile(), token);
			this.getUserManager().addUser(user);
			this.getUserRegDAO().addActivationToken(userProfile.getUsername(), token, new Date(), IUserRegDAO.ACTIVATION_TOKEN_TYPE);
		} catch (Throwable t) {
			_logger.error("Error in Account registration", t);
			throw new ApsSystemException("Error in Account registration", t);
		}
	}

	@Override
	public void reactivationByEmail(String email) throws ApsSystemException {
		try {
			Collection<String> usernames = this.getUsernamesByEmail(email);
			Iterator<String> usernamesIter = usernames.iterator();
			while (usernamesIter.hasNext()) {
				String userName = (String) usernamesIter.next();
				this.reactivationByUserName(userName);
			}
		} catch (Throwable t) {
			_logger.error("Error in request for Account Reactivation", t);
			throw new ApsSystemException("Error in request for Account Reactivation", t);
		}
	}

	@Override
	public void activateUser(String username, String password, String token) throws ApsSystemException {
		try {
			IUserManager userManager = this.getUserManager();
			this.clearOldAccountRequests();
			AbstractUser user = (AbstractUser) userManager.getUser(username);
			if(user instanceof User){
 				((User) user).setLastPasswordChange(new Date());
				((User) user).setDisabled(false);
			}
			this.addDefaultAuthorities(user);
			user.setPassword(password);
			userManager.updateUser(user);
			if(user instanceof User){
				userManager.changePassword(username, password);// Per salvare password non in chiaro
			}
			this.getUserRegDAO().removeConsumedToken(token);
		} catch (Throwable t) {
			_logger.error("Error in Account activation", t);
			throw new ApsSystemException("Error in Account activation", t);
		}
	}

	@Override
	public void reactivateUser(String username, String password, String token) throws ApsSystemException {
		try {
			IUserManager userManager = this.getUserManager();
			this.clearOldAccountRequests();
			User user = (User) userManager.getUser(username);
			user.setLastPasswordChange(new Date());
			user.setPassword(password);
			user.setDisabled(false);
			userManager.updateUser(user);
			userManager.changePassword(username, password);// Per salvare password non in chiaro
			this.getUserRegDAO().removeConsumedToken(token);
		} catch (Throwable t) {
			_logger.error("Error in Account activation", t);
			throw new ApsSystemException("Error in Account activation", t);
		}
	}

	@Override
	public void clearOldAccountRequests() throws ApsSystemException {
		long time = new Date().getTime()-this.getTokenValidityInMillis();
		Date expiration = new Date(time);
		this.getUserRegDAO().clearOldTokens(expiration);
		List<String> usernames = this.getUserRegDAO().oldAccountsNotActivated(expiration);
		//Ragionarci
		Iterator<String> it = usernames.iterator();
		while (it.hasNext()) {
			String current = (String) it.next();
			this.getUserManager().removeUser(current);
			this.getUserRegDAO().clearTokenByUsername(current);
		}
	}

	@Override
	public void deactivateUser(UserDetails user) throws ApsSystemException {
		try {
			if (user instanceof User) {
				((User)user).setDisabled(true);
				this.getUserManager().updateUser(user);
			}
		} catch (Throwable t) {
			_logger.error("Error in Account deactivation", t);
			throw new ApsSystemException("Error in Account deactivation", t);
		}
	}

	@Override
	public String getUsernameFromToken(String token){
		return this.getUserRegDAO().getUsernameFromToken(token);
	}

	@Override
	public Collection<String> getUsernamesByEmail(String email) throws ApsSystemException {
		try {
			Collection<String> usernames = new HashSet<String>();
			IUserProfileManager profileManager = this.getUserProfileManager();
			Iterator<IApsEntity> prototypes = profileManager.getEntityPrototypes().values().iterator();
			while (prototypes.hasNext()) {
				IApsEntity prototype = prototypes.next();
				AttributeInterface eMailAttr = prototype.getAttributeByRole(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_MAIL);
				if (eMailAttr!=null) {
					EntitySearchFilter[] filters = {
							new EntitySearchFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, false, prototype.getTypeCode(), false),
							new EntitySearchFilter(eMailAttr.getName(), true, email, false) };
					usernames.addAll(profileManager.searchId(filters));
				}
			}
			return usernames;
		} catch (Throwable t) {
			_logger.error("Error searching users with email {}", email, t);
			throw new ApsSystemException("Error searching users with email " + email, t);
		}
	}

	@Override
	public void reactivationByUserName(String username) throws ApsSystemException {
		try {
			IUserProfile profile = this.getUserProfileManager().getProfile(username);
			if (null != profile) {
				String token = this.createToken(username);
				this.getUserRegDAO().clearTokenByUsername(username);
				this.getUserRegDAO().addActivationToken(username, token, new Date(), IUserRegDAO.REACTIVATION_RECOVER_TOKEN_TYPE);
				this.sendAlertReactivateUser(profile, token);
			}
		} catch (Throwable t) {
			_logger.error("Error in request for Account Reactivation. username: {}", username, t);
			throw new ApsSystemException("Error in request for Account Reactivation", t);
		}
	}
	
	protected String createLink(String pageCode, String userName, String token, String langcode) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		Lang lang = this.getLangManager().getLang(langcode);
		IPage page = this.getPageManager().getPage(pageCode);
		if (null == page) {
			page = this.getPageManager().getRoot();
		}
		return this.getUrlManager().createUrl(page, lang, params);
	}
	
	protected String replaceParams(String defaultText, Map<String, String> params) {
		String body = defaultText;
		StringBuffer strBuff = null;
		Iterator<Entry<String, String>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pairs = it.next();
			String field = "{" + pairs.getKey() + "}";
			int start = body.indexOf(field);
			if (start > 0) {
				int end = start + field.length();
				strBuff = new StringBuffer();
				strBuff.append(body.substring(0, start));
				strBuff.append(pairs.getValue());
				strBuff.append(body.substring(end));
				body = strBuff.toString();
			}
		}
		return body;
	}
	
	protected String createToken(String userName) throws NoSuchAlgorithmException {
		Random random = new Random();
		StringBuilder salt = new StringBuilder();
		long rndLong = random.nextLong();
		salt.append(rndLong);
		String date = DateConverter.getFormattedDate(new Date(), "SSSmmyyyy-SSS-MM:ssddmmHHmmEEE");
		salt.append(date);
		rndLong = random.nextLong();
		salt.append(rndLong);
		// genero il token in base a username e salt
		String token = ShaEncoder.encodePassword(userName, salt.toString());
		return token;
	}
	
	protected Map<String, String> prepareMailParams(IUserProfile profile, String token, String pageCode) {
		Map<String, String> params = new HashMap<String, String>();
		
		String name = this.getFieldValue(profile, SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_FIRST_NAME);
		params.put("name", name!=null ? name : "");
		String surname = this.getFieldValue(profile, SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_SURNAME);
		params.put("surname", surname!=null ? surname : "");
		
		String fullname = this.getFieldValue(profile, SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_FULL_NAME);
		params.put("fullname", fullname!=null ? fullname : "");
		
		String username = profile.getUsername();
		params.put("userName", username);
		params.put("username", username);

		String profileLangAttr = this.getLangForLinkCreation(profile);
		String link = this.createLink(pageCode, username, token, profileLangAttr);
		params.put("link", link);

		return params;
	}
	
	private String getLangForLinkCreation(IUserProfile profile) {
		ILangManager langManager = this.getLangManager();
		String langCode = this.getFieldValue(profile, JpUserRegSystemConstants.ATTRIBUTE_ROLE_LANGUAGE);
		if (null == langCode || langCode.length() == 0 || langManager.getLang(langCode)==null) {
			langCode = langManager.getDefaultLang().getCode();
		}
		return langCode;
	}

	private String getFieldValue(IApsEntity entity, String roleName) {
		String value = null;
		AttributeInterface attribute = entity.getAttributeByRole(roleName);
		if (attribute!=null) {
			if (attribute instanceof MonoTextAttribute) {
				value = ((MonoTextAttribute) attribute).getText();
			} else if (attribute instanceof TextAttribute) {
				value = ((TextAttribute) attribute).getTextForLang(this.getLangManager().getDefaultLang().getCode());
			}
		}
		return value;
	}

	private void sendAlertRegProfile(IUserProfile profile, String token) throws ApsSystemException {
		IUserRegConfig config = this.getConfig();
		String activationPageCode = config.getActivationPageCode();
		Map<String, String> params = this.prepareMailParams(profile, token, activationPageCode);
		this.sendAlert(config.getActivationTemplates(), params, profile);
	}

	private void sendAlertReactivateUser(IUserProfile profile, String token) throws ApsSystemException {
		IUserRegConfig config = this.getConfig();
		String reactivationPageCode = config.getReactivationPageCode();
		Map<String, String> params = this.prepareMailParams(profile, token, reactivationPageCode);
		this.sendAlert(config.getReactivationTemplates(), params, profile);
	}

	protected void sendAlert(Map<String, Template> templates, Map<String, String> params, IUserProfile profile) throws ApsSystemException {
		IUserRegConfig config = this.getConfig();
		
		String[] eMail = { this.getFieldValue(profile, SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_MAIL) };
		Template template = null;
		String langCode = this.getFieldValue(profile, JpUserRegSystemConstants.ATTRIBUTE_ROLE_LANGUAGE);
		if (langCode!=null) {
			template = templates.get(langCode);
		}
		if (template == null) {
			template = templates.get(this.getLangManager().getDefaultLang().getCode());
		}
		String subject = template.getSubject();
		String text = this.replaceParams(template.getBody(), params);
		this.getMailManager().sendMail(text, subject, eMail, null, null, config.getEMailSenderCode());
	}
	
	private void addDefaultAuthorities(AbstractUser user) throws ApsSystemException {
		Set<String> csvs = this.getConfig().getDefaultCsvAuthorizations();
		if (null != csvs) {
			Iterator<String> iter = csvs.iterator();
			while (iter.hasNext()) {
				String csv = iter.next();
				String[] params = csv.split(",");
				String groupName = (params.length > 0) ? params[0] : null;
				String roleName = (params.length > 1) ? params[1] : null;
				this.getAuthorizationManager().addUserAuthorization(user.getUsername(), groupName, roleName);
			}
		}
	}
	
	protected void setTokenValidityInMillis(long tokenValidityInMillis) {
		this._tokenValidityInMillis = tokenValidityInMillis;
	}
	protected long getTokenValidityInMillis() {
		return _tokenValidityInMillis;
	}
	
	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	protected IURLManager getUrlManager() {
		return _urlManager;
	}
	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface baseConfigManager) {
		this._configManager = baseConfigManager;
	}
	
	protected ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
	protected IMailManager getMailManager() {
		return _mailManager;
	}
	public void setMailManager(IMailManager mailManager) {
		this._mailManager = mailManager;
	}
	
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	
	protected IUserRegDAO getUserRegDAO() {
		return _userRegDAO;
	}
	public void setUserRegDAO(IUserRegDAO activationTocketDAO) {
		this._userRegDAO = activationTocketDAO;
	}

	private long _tokenValidityInMillis;
	private IUserRegConfig _userRegConfig;

	private IMailManager _mailManager;
	private ILangManager _langManager;
	private IUserManager _userManager;
	private IURLManager _urlManager;
	private IPageManager _pageManager;
	private IUserProfileManager _userProfileManager;
	private ConfigInterface _configManager;
	private IAuthorizationManager _authorizationManager;
	private IUserRegDAO _userRegDAO;
	
}