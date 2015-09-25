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
package com.agiletec.plugins.jpcrowdsourcing.apsadmin.ideainstance;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.Idea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IIdeaInstanceManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IdeaInstance;
import com.agiletec.plugins.jpcrowdsourcing.apsadmin.portal.specialwidget.IdeaInstanceWidgetAction;


public class IdeaInstanceAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaInstanceAction.class);

	@Override
	public void validate() {
		super.validate();
		if (null == this.getGroups() || this.getGroups().isEmpty()) {
			this.addActionError(this.getText("jpcrowdsourcing.error.ideaInstance.group.required"));
		}
		if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
			IdeaInstance clone = this.getIdeaInstance(this.getCode());
			if (null != clone) {
				this.addActionError(this.getText("jpcrowdsourcing.error.ideaInstance.code.duplicated"));
			}
		}
	}

	public String newIdeaInstance() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
		} catch (Throwable t) {
			_logger.error("error in newIdeaInstance", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String edit() {
		try {
			IdeaInstance ideaInstance = this.getIdeaInstanceManager().getIdeaInstance(this.getCode());
			if (null == ideaInstance) {
				this.addActionError(this.getText("jpcrowdsourcing.error.ideaInstance.null"));
				return INPUT;
			}
			this.populateForm(ideaInstance);
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			IdeaInstance ideaInstance = this.createIdeaInstance();
			int strutsAction = this.getStrutsAction();
			if (ApsAdminSystemConstants.ADD == strutsAction) {
				this.getIdeaInstanceManager().addIdeaInstance(ideaInstance);
			} else if (ApsAdminSystemConstants.EDIT == strutsAction) {
				this.getIdeaInstanceManager().updateIdeaInstance(ideaInstance);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String trash() {
		try {
			IdeaInstance ideaInstance = this.getIdeaInstanceManager().getIdeaInstance(this.getCode());
			if (null == ideaInstance) {
				this.addActionError(this.getText("jpcrowdsourcing.error.ideaInstance.null"));
				return INPUT;
			}
			Boolean b = this.isPublished();
			if (null == b || b.booleanValue()) {
				return INPUT;
			}

			this.populateForm(ideaInstance);
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	protected Boolean isPublished() {
		Boolean published = false;
		try {
			List<IPage> pages = this.getPageManager().getWidgetUtilizers(IdeaInstanceWidgetAction.WIDGET_CODE);
			if (null != pages && !pages.isEmpty()) {
				Iterator<IPage> it = pages.iterator();
				while (it.hasNext()) {
					IPage currentPage = it.next();
					Widget[] widgets = currentPage.getWidgets();
					for (int i = 0; i < widgets.length; i++) {
						Widget currentWidget = widgets[i];
						if (null != currentWidget && currentWidget.getType().getCode().equals(IdeaInstanceWidgetAction.WIDGET_CODE)) {
							ApsProperties config = currentWidget.getConfig();
							if (null != config) {
								String value = config.getProperty(IdeaInstanceWidgetAction.WIDGET_PARAM_IDEA_INSTANCE);
								if (StringUtils.isNotBlank(value) && value.equals(this.getCode())) {
									List<String> args = new ArrayList<String>();
									args.add(currentPage.getCode());
									args.add(Integer.valueOf(i).toString());
									this.addActionError(this.getText("jpcrowdsourcing.error.ideaInstance.online", args));
									published = true;
								}
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error checking if idea instance is published", t);
			throw new RuntimeException("Error checking if idea instance is published" );
		}
		return published;
	}

	public String delete() {
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getIdeaInstanceManager().deleteIdeaInstance(this.getCode());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String view() {
		try {
			IdeaInstance ideaInstance = this.getIdeaInstanceManager().getIdeaInstance(this.getCode());
			if (null == ideaInstance) {
				this.addActionError(this.getText("jpcrowdsourcing.error.ideaInstance.null"));
				return INPUT;
			}
			this.populateForm(ideaInstance);
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String joinGroup() {
		try {
			if (null == this.getGroups()) {
				this.setGroups(new ArrayList<String>());
			}
			String group = this.getGroupName();
			if (!this.getGroups().contains(group)) {
				this.getGroups().add(group);
			}
		} catch (Throwable t) {
			_logger.error("error in joinGroup", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String removeGroup() {
		try {
			if (null == this.getGroups()) {
				return SUCCESS;
			}
			this.getGroups().remove(this.getGroupName());
		} catch (Throwable t) {
			_logger.error("error in removeGroup", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public List<Group> getSystemGroups() {
		List<Group> groups = null;
		try {
			groups = this.getGroupManager().getGroups();
		} catch (Throwable t) {
			_logger.error("Error loading system groups", t);
			throw new RuntimeException("Error loading system groups");
		}
		return groups;
	}

	public Group getSystemGroup(String groupName) {
		Group group = null;
		try {
			group = this.getGroupManager().getGroup(groupName);
		} catch (Throwable t) {
			_logger.error("Error loading system group {}", groupName, t);
			throw new RuntimeException("Error loading system group " + groupName);
		}
		return group;
	}


	public IdeaInstance getIdeaInstance(String code) {
		IdeaInstance ideaInstance = null;
		try {
			ideaInstance = this.getIdeaInstanceManager().getIdeaInstance(code);
		} catch (Throwable t) {
			_logger.error("Error loading instance {}", code, t);
			throw new RuntimeException("Error loading getIdeaInstance" + code);
		}
		return ideaInstance;
	}

	public Idea getIdea(String code) {
		Idea idea = null;
		try {
			idea = (Idea) this.getIdeaManager().getIdea(code);
		} catch (Throwable t) {
			_logger.error("Error loading idea {}", code, t);
			throw new RuntimeException("Error loading getIdea" + code);
		}
		return idea;
	}

	private void populateForm(IdeaInstance ideaInstance) throws Throwable {
		this.setCode(ideaInstance.getCode());
		this.setCreatedat(ideaInstance.getCreatedat());
		this.setGroups(ideaInstance.getGroups());
	}

	private IdeaInstance createIdeaInstance() {
		IdeaInstance ideaInstance = new IdeaInstance();
		ideaInstance.setCode(this.getCode());
		ideaInstance.setCreatedat(this.getCreatedat());
		ideaInstance.setGroups(this.getGroups());
		return ideaInstance;
	}

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}

	public Date getCreatedat() {
		return _createdat;
	}
	public void setCreatedat(Date createdat) {
		this._createdat = createdat;
	}


	protected IIdeaInstanceManager getIdeaInstanceManager() {
		return _ideaInstanceManager;
	}
	public void setIdeaInstanceManager(IIdeaInstanceManager ideaInstanceManager) {
		this._ideaInstanceManager = ideaInstanceManager;
	}

	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}

	public List<String> getGroups() {
		return _groups;
	}
	public void setGroups(List<String> groups) {
		this._groups = groups;
	}

	public String getGroupName() {
		return _groupName;
	}
	public void setGroupName(String groupName) {
		this._groupName = groupName;
	}

	protected IIdeaManager getIdeaManager() {
		return _ideaManager;
	}
	public void setIdeaManager(IIdeaManager ideaManager) {
		this._ideaManager = ideaManager;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	private int _strutsAction;
	private String _groupName;

	private String _code;
	private Date _createdat;
	private List<String> _groups;

	private IIdeaInstanceManager _ideaInstanceManager;
	private IIdeaManager _ideaManager;
	private IGroupManager _groupManager;
	private IPageManager _pageManager;

}