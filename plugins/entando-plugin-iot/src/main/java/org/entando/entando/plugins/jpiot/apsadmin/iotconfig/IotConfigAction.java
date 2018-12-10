/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.apsadmin.iotconfig;

import org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.IotConfig;
import org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.IIotConfigManager;



import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotConfigAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(IotConfigAction.class);

	public String newIotConfig() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
		} catch (Throwable t) {
			_logger.error("error in newIotConfig", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String edit() {
		try {
			IotConfig iotConfig = this.getIotConfigManager().getIotConfig(this.getId());
			if (null == iotConfig) {
				this.addActionError(this.getText("error.iotConfig.null"));
				return INPUT;
			}
			this.populateForm(iotConfig);
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			IotConfig iotConfig = this.createIotConfig();
			int strutsAction = this.getStrutsAction();
			if (ApsAdminSystemConstants.ADD == strutsAction) {
				this.getIotConfigManager().addIotConfig(iotConfig);
			} else if (ApsAdminSystemConstants.EDIT == strutsAction) {
				this.getIotConfigManager().updateIotConfig(iotConfig);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trash() {
		try {
			IotConfig iotConfig = this.getIotConfigManager().getIotConfig(this.getId());
			if (null == iotConfig) {
				this.addActionError(this.getText("error.iotConfig.null"));
				return INPUT;
			}
			this.populateForm(iotConfig);
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
				this.getIotConfigManager().deleteIotConfig(this.getId());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String view() {
		try {
			IotConfig iotConfig = this.getIotConfigManager().getIotConfig(this.getId());
			if (null == iotConfig) {
				this.addActionError(this.getText("error.iotConfig.null"));
				return INPUT;
			}
			this.populateForm(iotConfig);
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void populateForm(IotConfig iotConfig) throws Throwable {
		this.setId(iotConfig.getId());
		this.setName(iotConfig.getName());
		this.setHostname(iotConfig.getHostname());
		this.setPort(iotConfig.getPort());
		this.setWebapp(iotConfig.getWebapp());
		this.setUsername(iotConfig.getUsername());
		this.setPassword(iotConfig.getPassword());
		this.setToken(iotConfig.getToken());
	}
	
	private IotConfig createIotConfig() {
		IotConfig iotConfig = new IotConfig();
		iotConfig.setId(this.getId());
		iotConfig.setName(this.getName());
		iotConfig.setHostname(this.getHostname());
		iotConfig.setPort(this.getPort());
		iotConfig.setWebapp(this.getWebapp());
		iotConfig.setUsername(this.getUsername());
		iotConfig.setPassword(this.getPassword());
		iotConfig.setToken(this.getToken());
		return iotConfig;
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

	public int getPort() {
		return _port;
	}
	public void setPort(int port) {
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
	
	private int _strutsAction;
	private int _id;
	private String _name;
	private String _hostname;
	private int _port;
	private String _webapp;
	private String _username;
	private String _password;
	private String _token;
	
	private IIotConfigManager _iotConfigManager;
	
}