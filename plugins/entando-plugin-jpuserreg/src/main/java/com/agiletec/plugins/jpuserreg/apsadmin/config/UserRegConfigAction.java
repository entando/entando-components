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
package com.agiletec.plugins.jpuserreg.apsadmin.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.authorizator.IApsAuthorityManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.IUserRegManager;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model.IUserRegConfig;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model.Template;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model.UserRegConfig;

public class UserRegConfigAction extends BaseAction implements IUserRegConfigAction {

	@Override
	public void validate() {
		super.validate();
		try {
			IUserRegConfig config = this.getConfig();
			this.checkTemplates(config.getActivationTemplates(), "config.activationTemplates");
			this.checkTemplates(config.getReactivationTemplates(), "config.reactivationTemplates");
			this.checkAuthorities(config.getRoles(), (IApsAuthorityManager) this.getRoleManager(), "config.roles");
			this.checkAuthorities(config.getGroups(), (IApsAuthorityManager) this.getGroupManager(), "config.groups");
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "validate", "Error during validation");
			this.addActionError(this.getText("jpuserreg.errors.genericError"));
		}
	}

	private void checkTemplates(Map<String, Template> templates, String fieldName) {
		Iterator<Lang> langs = this.getLangManager().getLangs().iterator();
		while (langs.hasNext()) {
			Lang lang = langs.next();
			Template template = templates.get(lang.getCode());
			if (template==null) {
				this.addFieldError(fieldName, this.getText("jpuserreg.errors.templates.notValued", new String[] { this.getText(fieldName), lang.getDescr() }));
			} else {
				String subject = template.getSubject();
				if (subject==null || subject.trim().length()==0) {
					this.addFieldError(fieldName, this.getText("jpuserreg.errors.templates.subject.notValued", new String[] { this.getText(fieldName), lang.getDescr() }));
				}
				String body = template.getBody();
				if (body==null || body.trim().length()==0) {
					this.addFieldError(fieldName, this.getText("jpuserreg.errors.templates.body.notValued", new String[] { this.getText(fieldName), lang.getDescr() }));
				}
			}
		}
	}

	private void checkAuthorities(Collection<String> authorities, IApsAuthorityManager authorityManager, String fieldName) {
		if (authorities!=null) {
			Iterator<String> authIter = authorities.iterator();
			while (authIter.hasNext()) {
				String authName = authIter.next();
				if (authorityManager.getAuthority(authName)==null) {
					this.addFieldError(fieldName, this.getText("jpuserreg.errors.authority.notFound", new String[] { this.getText(fieldName), authName }));
				}
			}
		}
	}

	/*
	 * Used in validation xml
	 */
	public boolean checkPage(String pageCode) {
		return this.getPageManager().getPage(pageCode)!=null;
	}

	/*
	 * Used in validation xml
	 */
	public boolean checkSenderCode() {
		boolean existsSender = false;
		try {
			String senderCode = this.getConfig().getEMailSenderCode();
			existsSender = null!=this.getMailManager().getMailConfig().getSender(senderCode);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "Error loading mail configuration");
		}
		return existsSender;
	}

	@Override
	public String edit() {
		try {
			IUserRegConfig config = this.getUserRegManager().getUserRegConfig();
			this.setConfig(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String save() {
		try {
			this.getUserRegManager().saveUserRegConfig(this.getConfig());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String addGroup() {
		try {
			String groupName = this.getGroupName();
			Collection<String> groups = this.getConfig().getGroups();
			if (null!=groupName && !groups.contains(groupName) && null!=this.getGroupManager().getGroup(groupName)) {
				groups.add(groupName);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addGroup");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String removeGroup() {
		try {
			this.getConfig().getGroups().remove(this.getGroupName());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeGroup");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String addRole() {
		try {
			String roleName = this.getRoleName();
			Collection<String> roles = this.getConfig().getRoles();
			if (null!=roleName && !roles.contains(roleName) && null!=this.getRoleManager().getRole(roleName)) {
				roles.add(roleName);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addRole");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String removeRole() {
		try {
			this.getConfig().getRoles().remove(this.getRoleName());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeRoles");
			return FAILURE;
		}
		return SUCCESS;
	}

	public List<SelectItem> getMailSenders() {
		try {
			Map<String, String> senders = this.getMailManager().getMailConfig().getSenders();
			List<SelectItem> items = new ArrayList<SelectItem>(senders.size());
			Iterator<String> iter = senders.keySet().iterator();
			while (iter.hasNext()) {
				String string = (String) iter.next();
				SelectItem item = new SelectItem(string, senders.get(string));
				items.add(item);
			}
			return items;
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getMailSenders");
			throw new RuntimeException(t);
		}
	}

	public List<Role> getSystemRoles() {
		return this.getRoleManager().getRoles();
	}

	public List<Group> getSystemGroups() {
		return this.getGroupManager().getGroups();
	}

	public List<IPage> getPages() {
		if (this._pages==null) {
			this._pages = new ArrayList<IPage>();
			IPage root = this.getPageManager().getRoot();
			this.addPages(root, this._pages);
		}
		return this._pages;
	}

	protected void addPages(IPage page, List<IPage> pages) {
		pages.add(page);
		IPage[] children = page.getChildren();
		for (int i=0; i<children.length; i++) {
			this.addPages(children[i], pages);
		}
	}
	
	public List<IPage> getActivationPages() throws ApsSystemException {
		return this.getSystemPages(this.getActivationWidgetCode());
	}
	
	public List<IPage> getReactivationPages() throws ApsSystemException {
		return this.getSystemPages(this.getReactivationWidgetCode());
	}
	
	protected List<IPage> getSystemPages(String widgetCode) throws ApsSystemException {
		List<IPage> pages = null;
		try {
			pages = this.getPageManager().getWidgetUtilizers(widgetCode);
		} catch (Exception e) {
			ApsSystemUtils.logThrowable(e, this, "getSystemPages");
			pages = new ArrayList<IPage>();
		}
		return pages;
	}

	public List<Lang> getLangs() {
		return this.getLangManager().getLangs();
	}
	
	protected String getActivationWidgetCode() {
		return _activationWidgetCode;
	}
	public void setActivationWidgetCode(String activationWidgetCode) {
		this._activationWidgetCode = activationWidgetCode;
	}
	
	protected String getReactivationWidgetCode() {
		return _reactivationWidgetCode;
	}
	public void setReactivationWidgetCode(String reactivationWidgetCode) {
		this._reactivationWidgetCode = reactivationWidgetCode;
	}
	
	public IUserRegConfig getConfig() {
		return _config;
	}
	public void setConfig(IUserRegConfig config) {
		this._config = config;
	}

	public String getGroupName() {
		return _groupName;
	}
	public void setGroupName(String groupName) {
		this._groupName = groupName;
	}

	public String getRoleName() {
		return _roleName;
	}
	public void setRoleName(String roleName) {
		this._roleName = roleName;
	}

	protected IUserRegManager getUserRegManager() {
		return _userRegManager;
	}
	public void setUserRegManager(IUserRegManager userRegManager) {
		this._userRegManager = userRegManager;
	}

	protected IMailManager getMailManager() {
		return _mailManager;
	}
	public void setMailManager(IMailManager mailManager) {
		this._mailManager = mailManager;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}

	protected IRoleManager getRoleManager() {
		return _roleManager;
	}
	public void setRoleManager(IRoleManager roleManager) {
		this._roleManager = roleManager;
	}
	
	private String _activationWidgetCode = "jpuserreg_Activation";
	private String _reactivationWidgetCode = "jpuserreg_Reactivation";
	
	private IUserRegConfig _config = new UserRegConfig();
	private String _groupName;
	private String _roleName;

	private List<IPage> _pages;

	private IUserRegManager _userRegManager;
	private IMailManager _mailManager;
	private IPageManager _pageManager;
	private IGroupManager _groupManager;
	private IRoleManager _roleManager;

}