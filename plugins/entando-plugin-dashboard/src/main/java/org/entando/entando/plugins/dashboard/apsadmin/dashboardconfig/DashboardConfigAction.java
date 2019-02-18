
/*
 *
 *  * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *  *
 *  * This library is free software; you can redistribute it and/or modify it under
 *  * the terms of the GNU Lesser General Public License as published by the Free
 *  * Software Foundation; either version 2.1 of the License, or (at your option)
 *  * any later version.
 *  *
 *  * This library is distributed in the hope that it will be useful, but WITHOUT
 *  * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 *  * details.
 *
 */

package org.entando.entando.plugins.dashboard.apsadmin.dashboardconfig;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigManager;



import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardConfigAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(DashboardConfigAction.class);

	public String newDashboardConfig() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
		} catch (Throwable t) {
			_logger.error("error in newDashboardConfig", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String edit() {
		try {
			DashboardConfig dashboardConfig = this.getDashboardConfigManager().getDashboardConfig(this.getId());
			if (null == dashboardConfig) {
				this.addActionError(this.getText("error.dashboardConfig.null"));
				return INPUT;
			}
			this.populateForm(dashboardConfig);
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			DashboardConfig dashboardConfig = this.createDashboardConfig();
			int strutsAction = this.getStrutsAction();
			if (ApsAdminSystemConstants.ADD == strutsAction) {
				this.getDashboardConfigManager().addDashboardConfig(dashboardConfig);
			} else if (ApsAdminSystemConstants.EDIT == strutsAction) {
				this.getDashboardConfigManager().updateDashboardConfig(dashboardConfig);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trash() {
		try {
			DashboardConfig dashboardConfig = this.getDashboardConfigManager().getDashboardConfig(this.getId());
			if (null == dashboardConfig) {
				this.addActionError(this.getText("error.dashboardConfig.null"));
				return INPUT;
			}
			this.populateForm(dashboardConfig);
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String delete() {
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getDashboardConfigManager().deleteDashboardConfig(this.getId());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String view() {
		try {
			DashboardConfig dashboardConfig = this.getDashboardConfigManager().getDashboardConfig(this.getId());
			if (null == dashboardConfig) {
				this.addActionError(this.getText("error.dashboardConfig.null"));
				return INPUT;
			}
			this.populateForm(dashboardConfig);
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void populateForm(DashboardConfig dashboardConfig) throws Throwable {
		this.setId(dashboardConfig.getId());
		this.setServerDescription(dashboardConfig.getServerDescription());
		this.setServerURI(dashboardConfig.getServerURI());
		this.setUsername(dashboardConfig.getUsername());
		this.setPassword(dashboardConfig.getPassword());
		this.setToken(dashboardConfig.getToken());
		this.setTimeConnection(dashboardConfig.getTimeConnection());
		this.setActive(dashboardConfig.getActive());
		this.setDebug(dashboardConfig.getDebug());
	}
	
	private DashboardConfig createDashboardConfig() {
		DashboardConfig dashboardConfig = new DashboardConfig();
		dashboardConfig.setId(this.getId());
		dashboardConfig.setServerDescription(this.getServerDescription());
		dashboardConfig.setServerURI(this.getServerURI());
		dashboardConfig.setUsername(this.getUsername());
		dashboardConfig.setPassword(this.getPassword());
		dashboardConfig.setToken(this.getToken());
		dashboardConfig.setTimeConnection(this.getTimeConnection());
		dashboardConfig.setActive(this.getActive());
		dashboardConfig.setDebug(this.getDebug());
		return dashboardConfig;
	}
	

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getServerDescription() {
		return _serverDescription;
	}
	public void setServerDescription(String serverDescription) {
		this._serverDescription = serverDescription;
	}

	public String getServerURI() {
		return _serverURI;
	}
	public void setServerURI(String serverURI) {
		this._serverURI = serverURI;
	}

	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}

	public String getPassword() {
		return _password;
	}
	public void setPassword(String password) {
		this._password = password;
	}

	public String getToken() {
		return _token;
	}
	public void setToken(String token) {
		this._token = token;
	}

	public int getTimeConnection() {
		return _timeConnection;
	}
	public void setTimeConnection(int timeConnection) {
		this._timeConnection = timeConnection;
	}

	public boolean getActive() {
		return _active;
	}
	public void setActive(boolean active) {
		this._active = active;
	}

	public boolean getDebug() {
		return _debug;
	}
	public void setDebug(boolean debug) {
		this._debug = debug;
	}

	
	protected IDashboardConfigManager getDashboardConfigManager() {
		return _dashboardConfigManager;
	}
	public void setDashboardConfigManager(IDashboardConfigManager dashboardConfigManager) {
		this._dashboardConfigManager = dashboardConfigManager;
	}
	
	private int _strutsAction;
	private int _id;
	private String _serverDescription;
	private String _serverURI;
	private String _username;
	private String _password;
	private String _token;
	private int _timeConnection;
	private boolean _active;
	private boolean _debug;
	
	private IDashboardConfigManager _dashboardConfigManager;
	
}