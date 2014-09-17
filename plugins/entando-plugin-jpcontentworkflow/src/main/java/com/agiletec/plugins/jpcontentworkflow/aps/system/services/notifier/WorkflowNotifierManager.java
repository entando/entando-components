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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.authorization.authorizator.IApsAuthorityManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.model.ContentStatusChangedEventInfo;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.model.NotifierConfig;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.parse.WorkflowNotifierDOM;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.scheduler.MailSenderTask;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.scheduler.Scheduler;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.scheduler.Task;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.IContentWorkflowManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Step;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Workflow;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;


@Aspect
public class WorkflowNotifierManager extends AbstractService implements IWorkflowNotifierManager {

	private static final Logger _logger = LoggerFactory.getLogger(WorkflowNotifierManager.class);

	@Override
	public void init() throws Exception {
		this.loadConfigs();
		this.openScheduler();
		_logger.debug("{}: ready", this.getClass().getName());
	}
	
	@Override
	public void release() {
		this.closeScheduler();
	}
	
	@Override
	public void destroy() {
		this.release();
		super.destroy();
	}

	protected void loadConfigs() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpcontentworkflowSystemConstants.WORKFLOW_NOTIFIER_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + JpcontentworkflowSystemConstants.WORKFLOW_NOTIFIER_CONFIG_ITEM);
			}
			WorkflowNotifierDOM configDOM = new WorkflowNotifierDOM();
			this.setNotifierConfig(configDOM.extractConfig(xml));
		} catch (Throwable t) {
			_logger.error("Error loading configs", t);
			throw new ApsSystemException("Error loading configs", t);
		}
	}

	@Before("execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.saveContent(..)) && args(content,..)")
	public void listenContentSaving(Object content) {
		try {
			boolean notify = true;
			Content currentContent = (Content) content;
			String contentId = currentContent.getId();
			if (null != contentId) {
				Content previousContent = this.getContentManager().loadContent(contentId, false);
				if (previousContent.getStatus().equals(currentContent.getStatus())) {
					notify = false;
				}
			}
			if (notify) {
				this.saveContentStatusChanged(currentContent);
			}
		} catch (Throwable t) {
			_logger.error("Error notifying content status change", t);
		}
	}

	@Override
	public NotifierConfig getNotifierConfig() {
		return _notifierConfig.clone();
	}
	protected NotifierConfig getWorkflowNotifierConfig() {
		return _notifierConfig;
	}
	protected void setNotifierConfig(NotifierConfig notifierConfig) {
		this._notifierConfig = notifierConfig;
	}

	@Override
	public void saveNotifierConfig(NotifierConfig notifierConfig) throws ApsSystemException {
		try {
			WorkflowNotifierDOM configDOM = new WorkflowNotifierDOM();
			String xml = configDOM.createConfigXml(notifierConfig);
			this.getConfigManager().updateConfigItem(JpcontentworkflowSystemConstants.WORKFLOW_NOTIFIER_CONFIG_ITEM, xml);
			this.setNotifierConfig(notifierConfig);
			this.closeScheduler();
			this.openScheduler();
		} catch (Throwable t) {
			_logger.error("Error saving configs", t);
			throw new ApsSystemException("Error saving configs", t);
		}
	}

	protected void saveContentStatusChanged(Content content) {
		if (this.getWorkflowNotifierConfig().isActive()) {
			try {
				ContentStatusChangedEventInfo statusChangedInfo = new ContentStatusChangedEventInfo();
				statusChangedInfo.setContentId(content.getId());
				statusChangedInfo.setContentTypeCode(content.getTypeCode());
				statusChangedInfo.setContentDescr(content.getDescr());
				statusChangedInfo.setMainGroup(content.getMainGroup());
				statusChangedInfo.setDate(new Date());
				statusChangedInfo.setStatus(content.getStatus());
				this.getNotifierDAO().saveContentEvent(statusChangedInfo);
			} catch (Throwable t) {
				_logger.error("Error saving content status changed event for content {}", content.getId(), t);
			}
		}
	}

	/**
	 * Apre lo scheduler istanziando il task relativo 
	 * alla spedizione degli sms con i rilevamenti meteo.
	 */
	protected void openScheduler() {
		this.closeScheduler();
		NotifierConfig notifierConfig = this.getWorkflowNotifierConfig();
		if (notifierConfig.isActive()) {
			long milliSecondsDelay = notifierConfig.getHoursDelay() * 3600000l; // x minuti secondi millisecondi
			Date startTime = notifierConfig.getStartScheduler();

			startTime = this.calculateStartTime(startTime, milliSecondsDelay);
			Task task = new MailSenderTask(this);
			this._mailSenderScheduler = new Scheduler(task, startTime, milliSecondsDelay);
		}
	}

	private Date calculateStartTime(Date startTime, long delay) {
		Date current = new Date();
		long waitTime = current.getTime() - startTime.getTime();
		if (waitTime > 0) {
			startTime = new Date((current.getTime() + delay) - (waitTime % delay));
		}
		return startTime;
	}

	protected void closeScheduler() {
		if (this._mailSenderScheduler != null) this._mailSenderScheduler.cancel();
		this._mailSenderScheduler = null;
	}

	@Override
	public void sendMails() throws ApsSystemException {
		try {
			Map<String, List<ContentStatusChangedEventInfo>> statusChangedInfos = this.getNotifierDAO().getEventsToNotify();
			_logger.trace("Found {} events to notify", statusChangedInfos.size());
			if (statusChangedInfos.size()>0) {
				Map<String, List<ContentStatusChangedEventInfo>> contentsForUsers = this.prepareContentsForUsers(statusChangedInfos);
				Iterator<String> iter = contentsForUsers.keySet().iterator();
				while (iter.hasNext()) {
					String username = iter.next();
					List<ContentStatusChangedEventInfo> contentInfos = contentsForUsers.get(username);
					this.sendMail(username, contentInfos);
				}
				List<ContentStatusChangedEventInfo> notifiedContents = new ArrayList<ContentStatusChangedEventInfo>();
				for (List<ContentStatusChangedEventInfo> contentsForType : statusChangedInfos.values()) {
					notifiedContents.addAll(contentsForType);
				}
				_logger.trace("Notified {} events", notifiedContents.size());
				this.getNotifierDAO().signNotifiedEvents(notifiedContents);
			}
		} catch (Throwable t) {
			_logger.error("error sending emails ", t);
			throw new ApsSystemException("error sending emails", t);
		}
	}

	protected void sendMail(String username, List<ContentStatusChangedEventInfo> contentInfos) {
		try {
			NotifierConfig notifierConfig = this.getWorkflowNotifierConfig();
			String mailAddress = this.getMailAddress(username);
			if (null == mailAddress) {
				return;
			} 
			String[] mailAddresses = {mailAddress};
			Map<String, String> params = this.prepareParams(username);
			String subject = this.replaceParams(notifierConfig.getSubject(), params);
			String text = this.prepareMailText(params, contentInfos);
			String senderCode = notifierConfig.getSenderCode();
			String contentType = (notifierConfig.isHtml()) ? IMailManager.CONTENTTYPE_TEXT_HTML : IMailManager.CONTENTTYPE_TEXT_PLAIN;
			this.getMailManager().sendMail(text, subject, mailAddresses, null, null, senderCode, contentType);
		} catch(Throwable t) {
			_logger.error("Error sending mail to user {}", username, t);
		}
	}
	
	protected String getMailAddress(String username) throws Throwable {
		String email = null;
		IUserProfileManager profileManager = (IUserProfileManager) super.getBeanFactory().getBean(SystemConstants.USER_PROFILE_MANAGER);
		IUserProfile profile = profileManager.getProfile(username);
		if (null != profile) {
			ITextAttribute mailAttribute = (ITextAttribute) profile.getAttributeByRole(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_MAIL);
			if (null != mailAttribute && mailAttribute.getText().trim().length() > 0) {
				email = mailAttribute.getText();
			}
		}
		return email;
	}
	
	protected Map<String, String> prepareParams(String username) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("user", username);
		return params;
	}
	
	protected void addContentParams(Map<String, String> params, ContentStatusChangedEventInfo contentInfo) {
		params.put("type", contentInfo.getContentTypeCode());
		params.put("contentId", contentInfo.getContentId());
		params.put("descr", contentInfo.getContentDescr());
		params.put("group", contentInfo.getMainGroup());
		params.put("status", contentInfo.getStatus());
		params.put("data", DateConverter.getFormattedDate(contentInfo.getDate(), "dd MMMM yyyy"));
	}

	protected String prepareMailText(Map<String, String> params, List<ContentStatusChangedEventInfo> contentInfos) {
		NotifierConfig notifierConfig = this.getWorkflowNotifierConfig();
		String header = this.replaceParams(notifierConfig.getHeader(), params);
		String footer = this.replaceParams(notifierConfig.getFooter(), params);
		String body = notifierConfig.getTemplate();
		StringBuilder text = new StringBuilder(header);
		for (ContentStatusChangedEventInfo contentInfo : contentInfos) {
			this.addContentParams(params, contentInfo);
			text.append(this.replaceParams(body, params));
		}
		text.append(footer);
		return text.toString();
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
		Iterator<Entry<String, String>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pairs = it.next();
			String field = "\\{" + pairs.getKey() + "\\}";
			body = body.replaceAll(field, pairs.getValue());
		}
		return body;
	}
	
	protected Map<String, List<ContentStatusChangedEventInfo>> prepareContentsForUsers (
			Map<String, List<ContentStatusChangedEventInfo>> statusChangedInfos) throws ApsSystemException {
		Map<String, List<ContentStatusChangedEventInfo>> contentsForUsers = new HashMap<String, List<ContentStatusChangedEventInfo>>();
		List<String> editors = new ArrayList<String>();
		List<String> supervisors = new ArrayList<String>();
		this.findContentOperators(editors, supervisors);
		if (null != statusChangedInfos) {
			Iterator<String> iter = statusChangedInfos.keySet().iterator();
			while (iter.hasNext()) {
				String typeCode = iter.next();
				List<ContentStatusChangedEventInfo> infosForContentType = statusChangedInfos.get(typeCode);
				this.prepareUsersForContentType(contentsForUsers, typeCode, infosForContentType, editors, supervisors);
			}
		}
		return contentsForUsers;
	}
	
	protected void findContentOperators(List<String> editors, List<String> supervisors) throws ApsSystemException {
		List<Role> rolesWithSupervisor = ((IRoleManager) this.getRoleManager()).getRolesWithPermission(Permission.SUPERVISOR);
		List<String> roleNamesWithSupervisor = this.getRolesNames(rolesWithSupervisor);
		List<Role> rolesWithEditors = ((IRoleManager) this.getRoleManager()).getRolesWithPermission("editContents");
		List<String> roleNamesWithEditor = this.getRolesNames(rolesWithEditors);
		IUserProfileManager profileManager = (IUserProfileManager) super.getBeanFactory().getBean(SystemConstants.USER_PROFILE_MANAGER);
		List<String> usernames = profileManager.searchId(null);
		for (int i = 0; i < usernames.size(); i++) {
			String extractedUsername = usernames.get(i);
			List<IApsAuthority> userRoles = this.getRoleManager().getAuthorizationsByUser(extractedUsername);
			if (null == userRoles) {
				continue;
			}
			for (int j = 0; j < userRoles.size(); j++) {
				IApsAuthority role = userRoles.get(j);
				if (null == role) {
					continue;
				}
				if (roleNamesWithSupervisor.contains(role.getAuthority())) {
					supervisors.add(extractedUsername);
				}
				if (roleNamesWithEditor.contains(role.getAuthority())) {
					editors.add(extractedUsername);
				}
			}
		}
	}
	
	private List<String> getRolesNames(List<Role> roles) {
		List<String> names = new ArrayList<String>();
		if (null == roles) {
			return names;
		}
		for (int i = 0; i < roles.size(); i++) {
			IApsAuthority role = roles.get(i);
			if (null == role) {
				continue;
			}
			names.add(role.getAuthority());
		}
		return names;
	}
	
	protected void prepareUsersForContentType(Map<String, List<ContentStatusChangedEventInfo>> contentsForUsers, 
			String typeCode, List<ContentStatusChangedEventInfo> infosForContentType, List<String> editors, List<String> supervisors) throws ApsSystemException {
		Workflow workflow = this.getWorkflowManager().getWorkflow(typeCode);
		String contentTypeRole = workflow.getRole();
		if (contentTypeRole == null || contentTypeRole.length() == 0) {
			editors = this.filterUsersForRole(contentTypeRole, editors);
			supervisors = this.filterUsersForRole(contentTypeRole, supervisors);
		}
		List<String> usersForStep = null;
		String previousStepRole = null;
		for (ContentStatusChangedEventInfo contentInfo : infosForContentType) {
			String currentStep = contentInfo.getStatus();
			Step step = workflow.getStep(currentStep);
			String currentStepRole = step!=null ? step.getRole() : null;
			boolean needsSupervisor = Content.STATUS_READY.equals(currentStep);
			if (previousStepRole==null || !previousStepRole.equals(currentStepRole)) {
				previousStepRole = currentStepRole;
				if (needsSupervisor) {
					usersForStep = supervisors;
				} else {
					usersForStep = this.filterUsersForRole(currentStepRole, editors);
				}
			}
			String mainGroup = contentInfo.getMainGroup();
			List<String> allowedUsers = this.filterUsersForGroup(mainGroup, usersForStep);
			this.addContentForUsers(contentInfo, allowedUsers, contentsForUsers);
		}
	}

	protected List<String> filterUsersForRole(String roleName, List<String> users) throws ApsSystemException {
		List<String> usersForContentType = null;
		try {
			if (users.isEmpty() || roleName == null || roleName.length()==0) {
				usersForContentType = users;
			} else {
				usersForContentType = new ArrayList<String>();
				IApsAuthorityManager roleManager = this.getRoleManager();
				IApsAuthority authority = roleManager.getAuthority(roleName);
				List<String> usernamesWithAuth = this.getRoleManager().getUsernamesByAuthority(authority);
				for (int i = 0; i < users.size(); i++) {
					String username = users.get(i);
					if (null == username) {
						continue;
					}
					for (int j = 0; j < usernamesWithAuth.size(); j++) {
						String userWithAuth = usernamesWithAuth.get(j);
						if (null == userWithAuth) {
							continue;
						}
						if (username.equals(userWithAuth)) {
							usersForContentType.add(username);
						}
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error extracting users by role {}", roleName, t);
			throw new ApsSystemException("error extracting users by  role", t);
		}
		return usersForContentType;
	}
	
	protected List<String> filterUsersForGroup(String groupName, List<String> usernames) throws ApsSystemException {
		List<String> usersForContentType = null;
		if (usernames.isEmpty() || groupName == null || groupName.length()==0) {
			usersForContentType = usernames;
		} else {
			usersForContentType = new ArrayList<String>();
			IApsAuthorityManager groupManager = (IApsAuthorityManager) super.getBeanFactory().getBean(SystemConstants.GROUP_MANAGER);
			IApsAuthority authority = groupManager.getAuthority(groupName);
			List<String> usersWithAuth = groupManager.getUsernamesByAuthority(authority);
			for (int i = 0; i < usernames.size(); i++) {
				String username = usernames.get(i);
				if (null == username) {
					continue;
				}
				for (int j = 0; j < usersWithAuth.size(); j++) {
					String userWithAuth = usersWithAuth.get(j);
					if (null == userWithAuth) {
						continue;
					}
					if (username.equals(userWithAuth)) {
						usersForContentType.add(username);
					}
				}
			}
		}
		return usersForContentType;
	}
	
	protected void addContentForUsers(ContentStatusChangedEventInfo contentInfo, List<String> allowedUsers, 
			Map<String, List<ContentStatusChangedEventInfo>> contentsForUsers) {
		if (null == contentsForUsers || contentsForUsers.isEmpty()) {
			return;
		}
		for (int i = 0; i < allowedUsers.size(); i++) {
			String username = allowedUsers.get(i);
			List<ContentStatusChangedEventInfo> infos = contentsForUsers.get(username);
			if (infos == null) {
				infos = new ArrayList<ContentStatusChangedEventInfo>();
				contentsForUsers.put(username, infos);
			}
			infos.add(contentInfo);
		}
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	protected IAuthenticationProviderManager getAuthProvider() {
		return _authProvider;
	}
	public void setAuthProvider(IAuthenticationProviderManager authProvider) {
		this._authProvider = authProvider;
	}
	
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	
	protected IApsAuthorityManager getRoleManager() {
		return _roleManager;
	}
	public void setRoleManager(IApsAuthorityManager roleManager) {
		this._roleManager = roleManager;
	}
	
	protected IContentWorkflowManager getWorkflowManager() {
		return _workflowManager;
	}
	public void setWorkflowManager(IContentWorkflowManager workflowManager) {
		this._workflowManager = workflowManager;
	}
	
	protected IMailManager getMailManager() {
		return _mailManager;
	}
	public void setMailManager(IMailManager mailManager) {
		this._mailManager = mailManager;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IWorkflowNotifierDAO getNotifierDAO() {
		return _notifierDAO;
	}
	public void setNotifierDAO(IWorkflowNotifierDAO notifierDAO) {
		this._notifierDAO = notifierDAO;
	}
	
	private NotifierConfig _notifierConfig;
	protected Scheduler _mailSenderScheduler;

	private ConfigInterface _configManager;
	private IUserManager _userManager;
	private IAuthenticationProviderManager _authProvider;
	private IAuthorizationManager _authorizationManager;
	private IApsAuthorityManager _roleManager;
	private IContentWorkflowManager _workflowManager;
	private IMailManager _mailManager;
	private IContentManager _contentManager;
	private IWorkflowNotifierDAO _notifierDAO;

}