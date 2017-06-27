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
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class ContentThreadConfigUsersAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentThreadConfigUsersAction.class);

	//private static final String THREAD_CONFIG_SESSION_PARAM = "threadConfig";
	private static final String THREAD_CONFIG_SESSION_PARAM_USERS_CONTENT_TYPE = "threadConfigUsersContentType";

	private static final String ALL_TYPES = "*";

	/**
	 * entrypoint
	 * 
	 * @return
	 */
	public String viewUsers() {
		try {
			this.setConfigItemOnSession();

		} catch (Throwable t) {
			_logger.error("Error in viewUsers", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String entry() {

		String selectedUser = this.getRequest().getParameter("user");
		if (StringUtils.isNotBlank(selectedUser)) {
			this.setUsername(selectedUser);
		}
		return Action.SUCCESS;
	}

	public String addContentType() {
		try {

			Map<String, List<String>> config = this.getUsersContentType();
			boolean isValidInput = this.validateAdd();
			if (this.hasErrors()) {
				return INPUT;
			}
			if (isValidInput) {

				if (!config.containsKey(this.getUsername())) {
					config.put(this.getUsername(), new ArrayList<String>());
				}
				if (!config.get(this.getUsername()).contains(this.getContentType())) {
					if (this.getContentType().equals(ALL_TYPES)) {
						config.get(this.getUsername()).clear();
					}

					config.get(this.getUsername()).add(this.getContentType());
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
			Map<String, List<String>> config = this.getUsersContentType();
			boolean isValidInput = this.validateRemoveContentType();
			if (this.hasErrors()) {
				return INPUT;
			}
			if (isValidInput) {

				if (!config.containsKey(this.getUsername())) {
					return Action.SUCCESS;
				}
				if (config.get(this.getUsername()).contains(this.getContentType())) {
					config.get(this.getUsername()).remove(this.getContentType());
				}
				this.setConfigItemOnSession(config);
			}

		} catch (Throwable t) {
			_logger.error("Error in removeContentType", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String removeUser() {
		try {
			Map<String, List<String>> config = this.getUsersContentType();
			String selectedUser = this.getRequest().getParameter("user");
			this.setUsername(selectedUser);
			boolean isValidInput = this.validateRemoveUser();
			if (this.hasErrors()) {
				return INPUT;
			}
			if (isValidInput) {

				if (!config.containsKey(this.getUsername())) {
					return Action.SUCCESS;
				}
				config.remove(this.getUsername());
				this.setConfigItemOnSession(config);
			}

		} catch (Throwable t) {
			_logger.error("Error in removeContentType", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	private boolean validateAdd() throws ApsSystemException {
		if (StringUtils.isBlank(this.getUsername())) {
			this.addFieldError("username", this.getText("requiredstringByArg", this.getText("username")));
			return false;
		}
		if (null == this.getUserManager().getUser(this.getUsername())) {
			this.addFieldError("username", this.getText("username.notFound"));
			return false;
		}

		if (StringUtils.isBlank(this.getContentType())) {
			this.addFieldError("contentType", this.getText("requiredstringByArg", this.getText("contentType")));
			return false;
		}

		return true;
	}

	private boolean validateRemoveContentType() throws ApsSystemException {
		if (StringUtils.isBlank(this.getUsername())) {
			this.addFieldError("username", this.getText("requiredstringByArg", this.getText("username")));
			return false;
		}
		if (StringUtils.isBlank(this.getContentType())) {
			this.addFieldError("contentType", this.getText("requiredstringByArg", this.getText("contentType")));
			return false;
		}

		return true;
	}

	private boolean validateRemoveUser() throws ApsSystemException {
		if (StringUtils.isBlank(this.getUsername())) {
			this.addFieldError("username", this.getText("requiredstringByArg", this.getText("username")));
			return false;
		}

		return true;
	}

	public String saveUsersItem() {
		try {
			ContentThreadConfig config = this.getContentSchedulerManager().getConfig();
			config.setUsersContentType(this.getUsersContentType());

			this.getContentSchedulerManager().updateConfig(config);

			this.addActionMessage(this.getText("jpcontentscheduler.saveItem.success"));
		} catch (Throwable t) {
			_logger.error("Error saving item", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	private Map<String, List<String>> setConfigItemOnSession() {
		Map<String, List<String>> usersContentType = this.getContentSchedulerManager().getConfig().getUsersContentType();
		this.getRequest().getSession().setAttribute(THREAD_CONFIG_SESSION_PARAM_USERS_CONTENT_TYPE, usersContentType);
		return usersContentType;
	}

	private void setConfigItemOnSession(Map<String, List<String>> config) {
		this.getRequest().getSession().setAttribute(THREAD_CONFIG_SESSION_PARAM_USERS_CONTENT_TYPE, config);
	}

	public Map<String, List<String>> getUsersContentType() {
		return (Map<String, List<String>>) this.getRequest().getSession().getAttribute(THREAD_CONFIG_SESSION_PARAM_USERS_CONTENT_TYPE);
	}

	public List<SmallEntityType> getContentTypes() {
		List<SmallEntityType> smallContentTypes = this.getContentManager().getSmallEntityTypes();
		return smallContentTypes;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public IUserManager getUserManager() {
		return _userManager;
	}

	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}

	private ConfigInterface _baseConfigManager;
	private IContentSchedulerManager _contentSchedulerManager;
	private IUserManager _userManager;
	private IContentManager contentManager;
	private String username;
	private String contentType;

}
