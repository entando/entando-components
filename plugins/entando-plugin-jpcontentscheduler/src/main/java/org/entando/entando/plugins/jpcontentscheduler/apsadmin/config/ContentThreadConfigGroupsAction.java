/*
 * Copyright 2017-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpcontentscheduler.apsadmin.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.IContentSchedulerManager;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentThreadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class ContentThreadConfigGroupsAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentThreadConfigGroupsAction.class);

	//private static final String THREAD_CONFIG_SESSION_PARAM = "threadConfig";
	private static final String THREAD_CONFIG_SESSION_PARAM_GROUPS = "threadConfigGroups";

	private static final String ALL_TYPES = "*";

	/**
	 * entrypoint
	 * 
	 * @return
	 */
	public String viewGroups() {
		try {
			this.setConfigItemOnSession();

		} catch (Throwable t) {
			_logger.error("Error in viewGroups", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String addContentType() {
		try {

			Map<String, List<String>> config = this.getGroupsContentType();
			boolean hasErrors = this.validateAdd();
			if (this.hasErrors()) {
				return INPUT;
			}
			if (!hasErrors) {

				if (!config.containsKey(this.getGroupName())) {
					config.put(this.getGroupName(), new ArrayList<String>());
				}
				if (!config.get(this.getGroupName()).contains(this.getContentType())) {
					if (this.getContentType().equals(ALL_TYPES)) {
						config.get(this.getGroupName()).clear();
					}

					config.get(this.getGroupName()).add(this.getContentType());
				}
				this.setConfigItemOnSession(config);
			}

		} catch (Throwable t) {
			_logger.error("Error in viewUsers", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String removeContentType() {
		try {
			Map<String, List<String>> config = this.getGroupsContentType();
			boolean isValidInput = this.validateAdd();
			if (this.hasErrors()) {
				return INPUT;
			}
			if (isValidInput) {

				if (!config.containsKey(this.getGroupName())) {
					return Action.SUCCESS;
				}
				if (config.get(this.getGroupName()).contains(this.getContentType())) {
					config.get(this.getGroupName()).remove(this.getContentType());
				}
				this.setConfigItemOnSession(config);
			}

		} catch (Throwable t) {
			_logger.error("Error in removeContentType", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String removeGroup() {
		try {
			Map<String, List<String>> config = this.getGroupsContentType();
			boolean isValidInput = this.validateAdd();
			if (this.hasErrors()) {
				return INPUT;
			}
			if (isValidInput) {

				if (!config.containsKey(this.getGroupName())) {
					return Action.SUCCESS;
				}
				if (config.get(this.getGroupName()).contains(this.getContentType())) {
					config.get(this.getGroupName()).remove(this.getContentType());
				}
				this.setConfigItemOnSession(config);
			}

		} catch (Throwable t) {
			_logger.error("Error in removeContentType", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	private boolean validateAdd() {
		if (StringUtils.isBlank(this.getGroupName())) {
			return false;
		}

		if (StringUtils.isBlank(this.getContentType())) {
			return false;
		}

		return true;
	}

	public String saveGroupsItem() {
		try {
			ContentThreadConfig config = this.getContentSchedulerManager().getConfig();
			config.setGroupsContentType(this.getGroupsContentType());

			this.getContentSchedulerManager().updateConfig(config);

			this.addActionMessage(this.getText("jpcontentscheduler.saveItem.success"));
		} catch (Throwable t) {
			_logger.error("Error saving item", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	private Map<String, List<String>> setConfigItemOnSession() {
		Map<String, List<String>> groupsContentType = this.getContentSchedulerManager().getConfig().getGroupsContentType();
		this.getRequest().getSession().setAttribute(THREAD_CONFIG_SESSION_PARAM_GROUPS, groupsContentType);
		return groupsContentType;
	}

	public List<Group> getGroups() {
		List<Group> groups = this.getGroupManager().getGroups();
		return groups;
	}

	public List<SmallEntityType> getContentTypes() {
		List<SmallEntityType> smallContentTypes = this.getContentManager().getSmallEntityTypes();
		return smallContentTypes;
	}

	public Map<String, List<String>> getGroupsContentType() {
		return (Map<String, List<String>>) this.getRequest().getSession().getAttribute(THREAD_CONFIG_SESSION_PARAM_GROUPS);
	}

	private void setConfigItemOnSession(Map<String, List<String>> config) {
		this.getRequest().getSession().setAttribute(THREAD_CONFIG_SESSION_PARAM_GROUPS, config);
	}

	public void setBaseConfigManager(ConfigInterface baseConfigManager) {
		this._baseConfigManager = baseConfigManager;
	}

	public ConfigInterface getBaseConfigManager() {
		return _baseConfigManager;
	}

	public IContentSchedulerManager getContentSchedulerManager() {
		return _contentSchedulerManager;
	}

	public void setContentSchedulerManager(IContentSchedulerManager contentSchedulerManager) {
		this._contentSchedulerManager = contentSchedulerManager;
	}

	public IContentManager getContentManager() {
		return contentManager;
	}

	public void setContentManager(IContentManager contentManager) {
		this.contentManager = contentManager;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public IGroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(IGroupManager groupManager) {
		this.groupManager = groupManager;
	}

	private ConfigInterface _baseConfigManager;
	private IContentSchedulerManager _contentSchedulerManager;
	private IContentManager contentManager;
	private IGroupManager groupManager;
	private String groupName;
	private String contentType;

}
