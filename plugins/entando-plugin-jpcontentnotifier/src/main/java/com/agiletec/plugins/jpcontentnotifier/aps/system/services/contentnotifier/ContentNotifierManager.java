/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.authorization.authorizator.IApsAuthorityManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.AbstractUser;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedObserver;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcontentnotifier.aps.system.JpcontentnotifierSystemConstants;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.model.ContentMailInfo;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.model.NotifierConfig;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.parse.ContentNotifierConfigDOM;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.scheduler.MailSenderTask;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.scheduler.Scheduler;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.scheduler.Task;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * Service to notify to users the content creation or update,
 * the communication happens by email .
 * @author E.Santoboni
 */
public class ContentNotifierManager extends AbstractService implements PublicContentChangedObserver, IContentNotifierManager {

	@Override
	public void init() throws ApsSystemException {
		this.loadConfigs();
		this.openScheduler();
		ApsSystemUtils.getLogger().debug(this.getName() + ": service for notifications on contents changes initialized");
	}

	@Override
	public void release() {
		this.closeScheduler();
	}

	@Override
	public void destroy() {
		this.closeScheduler();
	}

	@Override
	public void updateFromPublicContentChanged(PublicContentChangedEvent event) {
		NotifierConfig config = this.getConfig();
		if (config.isActive()) {
			if ((config.isNotifyRemove() || PublicContentChangedEvent.REMOVE_OPERATION_CODE!=event.getOperationCode())) {
				try {
					this.getContentNotifierDao().saveEvent(event);
					ApsSystemUtils.getLogger().trace("Traced operation " + event.getOperationCode() + " on Content " + event.getContent().getId());
				} catch (ApsSystemException e) {
					ApsSystemUtils.logThrowable(e, this, "updateFromPublicContentChanged");
					throw new RuntimeException("error in updateFromPublicContentChanged", e);
				}
			}
		}
	}

	@Override
	public void updateNotifierConfig(NotifierConfig config) throws ApsSystemException {
		try {
			String xml = new ContentNotifierConfigDOM().createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpcontentnotifierSystemConstants.CONTENT_NOTIFIER_CONFIG_ITEM, xml);
			this.setSchedulerConfig(config);
			this.openScheduler();
			ApsSystemUtils.getLogger().trace("Updated Content Notifier Configuration");
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateNotifierConfig");
			throw new ApsSystemException("Errore in aggiornamento configurazione ContentNotifier", t);
		}
	}

	public List<ContentMailInfo> getContentsToNotify() throws ApsSystemException {
		return this.getContentNotifierDao().getContentsToNotify();
	}

	/**
	 * Notifica via eMail la modifica dei contenuti.
	 */
	@Override
	public void sendEMails() throws ApsSystemException {
		if (!this.getConfig().isActive()) {
			ApsSystemUtils.getLogger().info("Content Notifier not active");
			return;
		}
		try {
			List<ContentMailInfo> contentsToNotify = this.getContentsToNotify();
			ApsSystemUtils.getLogger().info("Starting to notify " + contentsToNotify.size() + " Contents");
			processContents(contentsToNotify);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "sendEMails");
			throw new ApsSystemException("Error sending emails", t);
		}
	}

	public List<ContentMailInfo> getContentsToNotifyToUser(UserDetails user, List<ContentMailInfo> contentsToNotify) {
		IAuthorizationManager authManager = this.getAuthorizationManager();
		if (authManager.isAuthOnGroup(user, Group.ADMINS_GROUP_NAME)) {
			return contentsToNotify;
		} else {
			List<ContentMailInfo> contentsToNotifyToUser = new ArrayList<ContentMailInfo>();
			boolean onlyOwner = this.getConfig().isOnlyOwner();
			for (ContentMailInfo info : contentsToNotify) {
				String mainGroup = info.getMainGroup();
				boolean allowedContent = authManager.isAuthOnGroup(user, mainGroup);
				if (!allowedContent && !onlyOwner && info.getGroups()!=null) {
					for (String group : info.getGroups()) {
						if (authManager.isAuthOnGroup(user, group)) {
							allowedContent = true;
							break;
						}
					}
				}
				if (allowedContent) {
					contentsToNotifyToUser.add(info);
				}
			}
			return contentsToNotifyToUser;
		}
	}

	protected void loadConfigs() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpcontentnotifierSystemConstants.CONTENT_NOTIFIER_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Missing content item: " + JpcontentnotifierSystemConstants.CONTENT_NOTIFIER_CONFIG_ITEM);
			}
			ApsSystemUtils.getLogger().trace(JpcontentnotifierSystemConstants.CONTENT_NOTIFIER_CONFIG_ITEM + ": " + xml);
			ContentNotifierConfigDOM configDOM = new ContentNotifierConfigDOM();
			this.setSchedulerConfig(configDOM.extractConfig(xml));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadConfigs");
			throw new ApsSystemException("Error loading config", t);
		}
	}

	/**
	 * Apre lo scheduler istanziando il task relativo
	 * alla spedizione degli sms con i rilevamenti meteo.
	 */
	protected void openScheduler() {
		this.closeScheduler();
		NotifierConfig config = this.getConfig();
		if (config.isActive()) {
			Date startTime = config.getStartScheduler();
			long milliSecondsDelay = config.getHoursDelay() * 3600000l; // x minuti secondi millisecondi
			startTime = this.calculateStartTime(startTime, milliSecondsDelay);
			Task task = new MailSenderTask(this);
			this._mailSenderScheduler = new Scheduler(task, startTime, milliSecondsDelay);
		}
	}

	protected void closeScheduler() {
		if (this._mailSenderScheduler != null) {
			this._mailSenderScheduler.cancel();
			this._mailSenderScheduler = null;
		}
	}

	protected List<UserDetails> findContentOperators() throws ApsSystemException {
		IAuthorizationManager authManager = this.getAuthorizationManager();
		IUserManager userManager = this.getUserManager();
		IUserProfileManager profileManager = this.getProfileManager();
		List<UserDetails> systemUsers = userManager.getUsers();
		List<UserDetails> allowedUsers = new ArrayList<UserDetails>();
		for (UserDetails user : systemUsers){
			user.addAutorities(this.getRoleManager().getAuthorizationsByUser(user));
			if (authManager.isAuthOnPermission(user, Permission.SUPERVISOR) || authManager.isAuthOnPermission(user, "editContents")) {
				try {
					AbstractUser userDetails = (AbstractUser) user;
					IUserProfile profile = profileManager.getProfile(userDetails.getUsername());
					userDetails.setProfile(profile);
				} catch (Throwable t) {
					ApsSystemUtils.logThrowable(t, this, "findContentOperators", "Error searching profile for user " + user.getUsername());
				}
				user.addAutorities(this.getGroupManager().getAuthorizationsByUser(user));
				allowedUsers.add(user);
			}
		}
		return allowedUsers;
	}

	protected boolean sendEMailToUser(UserDetails user, List<ContentMailInfo> contentsToNotify, Map<String, SmallContentType> smallContentTypes) throws ApsSystemException {
		boolean sent = false;
		NotifierConfig config = this.getConfig();
		IUserProfile profile = (IUserProfile) user.getProfile();
		if (profile!=null) {
			String emailAttributeName = config.getMailAttrName();
			Object eMailValue = profile.getValue(emailAttributeName);
			String eMail = eMailValue!=null ? eMailValue.toString() : null;
			if (eMail != null && eMail.length() > 0) {
				List<ContentMailInfo> contentsToNotifyToUser = this.getContentsToNotifyToUser(user, contentsToNotify);
				if (!contentsToNotifyToUser.isEmpty()) {
					String[] eMailAddresses = {eMail};
					String mailBody = this.createBody(user, contentsToNotifyToUser, smallContentTypes);
					String contentType = config.isHtml() ? IMailManager.CONTENTTYPE_TEXT_HTML : IMailManager.CONTENTTYPE_TEXT_PLAIN;
					sent = this.getMailManager().sendMail(mailBody, config.getSubject(),
							eMailAddresses, null, null, config.getSenderCode(), contentType);
				}
			}
		}
		return sent;
	}

	protected void signNotifiedContents(List<ContentMailInfo> contentsNotified) throws ApsSystemException, SQLException {
		try {
			this.getContentNotifierDao().signNotifiedContents(contentsNotified);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "signNotifiedContents");
			throw new ApsSystemException ("Error sign Notified Contents ", t);
		}
	}

	protected String createLink(ContentMailInfo info, String langCode) {
		StringBuilder link = new StringBuilder();
		String applicationBaseUrl = this.getConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL);
		link.append(applicationBaseUrl);
		link.append(langCode);
		link.append("/");
		link.append(this.getContentManager().getViewPage(info.getContentId()));
		link.append(".page?contentId=");
		link.append(info.getContentId());
		return link.toString();
	}

	/**
	 * @param defaultText Il testo di partenza, contenente le stringhe da rimpiazzare secondo la sintassi {chiaveStringa}.
	 * @param params La mappa dei parametri da rimpiazzare (solo il nome, esluse le { })<br />
	 * ATTENZIONE: le chiavi non devono contenere caratteri speciali per le regular expressions.<br />
	 * In tal caso vanno utilizzati i caratteri di escape.
	 * @return Il testo con tutte le occorrenze delle parole chiave sostituite.
	 */
	protected String replaceParams(String defaultText, Map<String, String> params) {
		String body = defaultText;
		for (Entry<String, String> pairs : params.entrySet()) {
			String field = "\\{" + pairs.getKey() + "\\}";
			body = body.replaceAll(field, (String) pairs.getValue());
		}
		return body;
	}

	protected Map<String, String> prepareContentParams(ContentMailInfo info, SmallContentType smallContentType, String link) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("link", link);
		params.put("type", smallContentType.getDescr());
		params.put("descr", info.getContentDescr());
		params.put("date", DateConverter.getFormattedDate(info.getDate(), "dd MMMM yyyy"));
		params.put("time", DateConverter.getFormattedDate(info.getDate(), "HH:mm"));
		return params;
	}

	private void processContents(List<ContentMailInfo> contentsToNotify) throws ApsSystemException, SQLException {
		if (contentsToNotify.isEmpty()) return;
		List<UserDetails> allowedUsers = this.findContentOperators();
		ApsSystemUtils.getLogger().info("Starting to notify contents to " + allowedUsers.size() + " Operators");
		Map<String, SmallContentType> smallContentTypes = this.getContentManager().getSmallContentTypesMap();
		int notified = 0;
		for (UserDetails user : allowedUsers) {
			boolean sent = this.sendEMailToUser(user, contentsToNotify, smallContentTypes);
			if (sent) notified++;
		}
		ApsSystemUtils.getLogger().info("Notified " + contentsToNotify.size() + " Contents to " + notified + " Operators");
		if (notified > 0) {
			this.signNotifiedContents(contentsToNotify);
		}
	}

	private String createBody(UserDetails user, List<ContentMailInfo> contentsToNotifyToUser, Map<String, SmallContentType> smallContentTypes) {
		String defaultLangCode = this.getLangManager().getDefaultLang().getCode();
		NotifierConfig config = this.getConfig();
		StringBuffer body = new StringBuffer(config.getHeader());
		for (ContentMailInfo info : contentsToNotifyToUser) {
			SmallContentType smallContentType = (SmallContentType) smallContentTypes.get(info.getContentTypeCode());
			String link = this.createLink(info, defaultLangCode);

			Map<String, String> params = this.prepareContentParams(info, smallContentType, link);
			switch (info.getOperationCode()) {
			case PublicContentChangedEvent.INSERT_OPERATION_CODE:
				body.append(this.replaceParams(config.getTemplateInsert(), params));
				break;
			case PublicContentChangedEvent.UPDATE_OPERATION_CODE:
				body.append(this.replaceParams(config.getTemplateUpdate(), params));
				break;
			case PublicContentChangedEvent.REMOVE_OPERATION_CODE:
				body.append(this.replaceParams(config.getTemplateRemove(), params));
				break;
			}
		}
		body.append(config.getFooter());
		return body.toString();
	}

	private Date calculateStartTime(Date startTime, long delay) {
		Date current = new Date();

		long waitTime = current.getTime() - startTime.getTime();
		if (waitTime > 0) {
			startTime = new Date((current.getTime() + delay) - (waitTime % delay));
		}
		return startTime;
	}

	@Override
	public NotifierConfig getConfig() {
		return this._schedulerConfig;
	}
	public void setSchedulerConfig(NotifierConfig schedulerConfig) {
		this._schedulerConfig = schedulerConfig;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}

	protected ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}

	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
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

	public IUserProfileManager getProfileManager() {
		return _profileManager;
	}
	public void setProfileManager(IUserProfileManager profileManager) {
		this._profileManager = profileManager;
	}

	public IApsAuthorityManager getRoleManager() {
		return _roleManager;
	}
	public void setRoleManager(IApsAuthorityManager roleManager) {
		this._roleManager = roleManager;
	}

	public IApsAuthorityManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IApsAuthorityManager groupManager) {
		this._groupManager = groupManager;
	}

	protected IContentNotifierDAO getContentNotifierDao() {
		return _contentNotifierDao;
	}
	public void setContentNotifierDao(IContentNotifierDAO contentNotifierDao) {
		this._contentNotifierDao = contentNotifierDao;
	}

	protected Scheduler _mailSenderScheduler;

	private NotifierConfig _schedulerConfig;

	private ConfigInterface _configManager;
	private IContentManager _contentManager;
	private ILangManager _langManager;
	private IUserManager _userManager;
	private IMailManager _mailManager;
	private IAuthorizationManager _authorizationManager;
	private IUserProfileManager _profileManager;
	private IApsAuthorityManager _roleManager;
	private IApsAuthorityManager _groupManager;

	private IContentNotifierDAO _contentNotifierDao;

}