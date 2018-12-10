/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.apsadmin.iotconfig;

import java.util.List;
import org.apache.commons.lang.StringUtils;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.IotConfig;
import org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.IIotConfigManager;
import com.agiletec.apsadmin.system.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotConfigFinderAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(IotConfigFinderAction.class);

	public List<Integer> getIotConfigsId() {
		try {
			FieldSearchFilter[] filters = new FieldSearchFilter[0];
			if (null != this.getId()) {
				//TODO add a constant into your IIotConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("id"), this.getId(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getName())) {
				//TODO add a constant into your IIotConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("name"), this.getName(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getHostname())) {
				//TODO add a constant into your IIotConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("hostname"), this.getHostname(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getPort()) {
				//TODO add a constant into your IIotConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("port"), this.getPort(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getWebapp())) {
				//TODO add a constant into your IIotConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("webapp"), this.getWebapp(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getUsername())) {
				//TODO add a constant into your IIotConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("username"), this.getUsername(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getPassword())) {
				//TODO add a constant into your IIotConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("password"), this.getPassword(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getToken())) {
				//TODO add a constant into your IIotConfigManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("token"), this.getToken(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			List<Integer> iotConfigs = this.getIotConfigManager().searchIotConfigs(filters);
			return iotConfigs;
		} catch (Throwable t) {
			_logger.error("Error getting iotConfigs list", t);
			throw new RuntimeException("Error getting iotConfigs list", t);
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

	public IotConfig getIotConfig(int id) {
		IotConfig iotConfig = null;
		try {
			iotConfig = this.getIotConfigManager().getIotConfig(id);
		} catch (Throwable t) {
			_logger.error("Error getting iotConfig with id {}", id, t);
			throw new RuntimeException("Error getting iotConfig with id " + id, t);
		}
		return iotConfig;
	}


	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
		this._id = id;
	}


	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}


	public String getHostname() {
		return _hostname;
	}
	public void setHostname(String hostname) {
		this._hostname = hostname;
	}


	public Integer getPort() {
		return _port;
	}
	public void setPort(Integer port) {
		this._port = port;
	}


	public String getWebapp() {
		return _webapp;
	}
	public void setWebapp(String webapp) {
		this._webapp = webapp;
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


	protected IIotConfigManager getIotConfigManager() {
		return _iotConfigManager;
	}
	public void setIotConfigManager(IIotConfigManager iotConfigManager) {
		this._iotConfigManager = iotConfigManager;
	}
	
	private Integer _id;
	private String _name;
	private String _hostname;
	private Integer _port;
	private String _webapp;
	private String _username;
	private String _password;
	private String _token;
	private IIotConfigManager _iotConfigManager;
}