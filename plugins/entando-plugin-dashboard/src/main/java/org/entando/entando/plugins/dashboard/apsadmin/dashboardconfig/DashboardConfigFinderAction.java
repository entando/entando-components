/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.apsadmin.dashboardconfig;

import java.util.List;
import org.apache.commons.lang.StringUtils;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigManager;
import com.agiletec.apsadmin.system.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardConfigFinderAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(DashboardConfigFinderAction.class);

	public List<Integer> getDashboardConfigsId() {
		try {
			FieldSearchFilter[] filters = new FieldSearchFilter[0];
			if (null != this.getId()) {
				//TODO add a constant into your IDashboardConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("id"), this.getId(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getServerDescription())) {
				//TODO add a constant into your IDashboardConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("serverDescription"), this.getServerDescription(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getServerURI())) {
				//TODO add a constant into your IDashboardConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("serverURI"), this.getServerURI(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getUsername())) {
				//TODO add a constant into your IDashboardConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("username"), this.getUsername(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getPassword())) {
				//TODO add a constant into your IDashboardConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("password"), this.getPassword(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getToken())) {
				//TODO add a constant into your IDashboardConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("token"), this.getToken(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getTimeConnection()) {
				//TODO add a constant into your IDashboardConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("timeConnection"), this.getTimeConnection(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getActive()) {
				//TODO add a constant into your IDashboardConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("active"), this.getActive(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getDebug()) {
				//TODO add a constant into your IDashboardConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("debug"), this.getDebug(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			List<Integer> dashboardConfigs = this.getDashboardConfigManager().searchDashboardConfigs(filters);
			return dashboardConfigs;
		} catch (Throwable t) {
			_logger.error("Error getting dashboardConfigs list", t);
			throw new RuntimeException("Error getting dashboardConfigs list", t);
		}
	}

	protected FieldSearchFilter[] addFilter(FieldSearchFilter[] filters, FieldSearchFilter filterToAdd) {
		int len = filters.length;
		FieldSearchFilter[] newFilters = new FieldSearchFilter[len + 1];
		for(int i=0; i < len; i++){
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		return newFilters;
	}

	public DashboardConfig getDashboardConfig(int id) {
		DashboardConfig dashboardConfig = null;
		try {
			dashboardConfig = this.getDashboardConfigManager().getDashboardConfig(id);
		} catch (Throwable t) {
			_logger.error("Error getting dashboardConfig with id {}", id, t);
			throw new RuntimeException("Error getting dashboardConfig with id " + id, t);
		}
		return dashboardConfig;
	}


	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
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


	public Integer getTimeConnection() {
		return _timeConnection;
	}
	public void setTimeConnection(Integer timeConnection) {
		this._timeConnection = timeConnection;
	}


	public Integer getActive() {
		return _active;
	}
	public void setActive(Integer active) {
		this._active = active;
	}


	public Integer getDebug() {
		return _debug;
	}
	public void setDebug(Integer debug) {
		this._debug = debug;
	}


	protected IDashboardConfigManager getDashboardConfigManager() {
		return _dashboardConfigManager;
	}
	public void setDashboardConfigManager(IDashboardConfigManager dashboardConfigManager) {
		this._dashboardConfigManager = dashboardConfigManager;
	}
	
	private Integer _id;
	private String _serverDescription;
	private String _serverURI;
	private String _username;
	private String _password;
	private String _token;
	private Integer _timeConnection;
	private Integer _active;
	private Integer _debug;
	private IDashboardConfigManager _dashboardConfigManager;
}