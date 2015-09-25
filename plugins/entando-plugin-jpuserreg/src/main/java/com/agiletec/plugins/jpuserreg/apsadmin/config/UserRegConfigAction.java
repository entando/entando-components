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
package com.agiletec.plugins.jpuserreg.apsadmin.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegConfigAction extends BaseAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(UserRegConfigAction.class);
	
	@Override
	public void validate() {
		super.validate();
		try {
			IUserRegConfig config = this.getConfig();
			this.checkTemplates(config.getActivationTemplates(), "config.activationTemplates");
			this.checkTemplates(config.getReactivationTemplates(), "config.reactivationTemplates");
			this.checkAuthorities();
		} catch (Throwable t) {
			_logger.error("Error during validation", t);
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
				if (body == null || body.trim().length()==0) {
					this.addFieldError(fieldName, this.getText("jpuserreg.errors.templates.body.notValued", new String[] { this.getText(fieldName), lang.getDescr() }));
				}
			}
		}
	}
	
	private void checkAuthorities() {
		Collection<String> csvs = this.getConfig().getDefaultCsvAuthorizations();
		if (csvs != null) {
			Iterator<String> authsIter = csvs.iterator();
			while (authsIter.hasNext()) {
				String csv = authsIter.next();
				String[] params = csv.split(",");
				String groupName = (params.length > 0) ? params[0] : null;
				String roleName = (params.length > 1) ? params[1] : null;
				if (null == groupName 
						|| null == getGroupManager().getGroup(groupName) 
						|| (roleName != null && null == this.getRoleManager().getRole(roleName))) {
					this.addFieldError("config.defaultCsvAuthorizations", this.getText("jpuserreg.errors.authority.invalid", new String[] { groupName, roleName }));
				}
			}
		}
	}
	
	public boolean checkPage(String pageCode) {
		return this.getPageManager().getPage(pageCode)!=null;
	}
	
	public boolean checkSenderCode() {
		boolean existsSender = false;
		try {
			String senderCode = this.getConfig().getEMailSenderCode();
			existsSender = null!=this.getMailManager().getMailConfig().getSender(senderCode);
		} catch (Throwable t) {
			_logger.error("Error loading mail configuration", t);
		}
		return existsSender;
	}
	
	public String edit() {
		try {
			IUserRegConfig config = this.getUserRegManager().getUserRegConfig();
			this.setConfig(config);
		} catch (Throwable t) {
			_logger.error("Error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String save() {
		try {
			this.getUserRegManager().saveUserRegConfig(this.getConfig());
		} catch (Throwable t) {
			_logger.error("Error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String addAuthorization() {
		try {
			String groupName = this.getGroupName();
			Collection<String> csvs = this.getConfig().getDefaultCsvAuthorizations();
			if (null != groupName && null != this.getGroupManager().getGroup(groupName)) {
				String csv = groupName + ",";
				String roleName = this.getRoleName();
				if (null != roleName && null != this.getRoleManager().getRole(roleName)) {
					csv += roleName;
				}
				csvs.add(csv);
			}
		} catch (Throwable t) {
			_logger.error("Error in addAuthorization", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String removeAuthorization() {
		try {
			Collection<String> csvs = this.getConfig().getDefaultCsvAuthorizations();
			if (null != csvs && null != this.getCsvAuthorization()) {
				this.getConfig().getDefaultCsvAuthorizations().remove(this.getCsvAuthorization());
			}
		} catch (Throwable t) {
			_logger.error("Error in removeAuthorization", t);
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
			_logger.error("Error in getMailSenders", t);
			throw new RuntimeException("Error in getMailSenders", t);
		}
	}
	
	public List<Role> getSystemRoles() {
		return this.getRoleManager().getRoles();
	}
	public Role getRole(String roleName) {
		return this.getRoleManager().getRole(roleName);
	}
	
	public List<Group> getSystemGroups() {
		return this.getGroupManager().getGroups();
	}
	public Group getGroup(String groupName) {
		return this.getGroupManager().getGroup(groupName);
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
			_logger.error("Error in getSystemPages", e);
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
	
	public String getCsvAuthorization() {
		return _csvAuthorization;
	}
	public void setCsvAuthorization(String csvAuthorization) {
		this._csvAuthorization = csvAuthorization;
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
	private String _csvAuthorization;

	private List<IPage> _pages;

	private IUserRegManager _userRegManager;
	private IMailManager _mailManager;
	private IPageManager _pageManager;
	private IGroupManager _groupManager;
	private IRoleManager _roleManager;

}