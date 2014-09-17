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
package com.agiletec.plugins.jpcasclient.apsadmin.config;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.CasClientConfig;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.ICasClientConfigManager;

public class ConfigAction extends BaseAction implements IConfigAction {
	
	@Override
	public String edit() {
		try {
			CasClientConfig config = this.getCasClientConfigManager().getClientConfig();
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
			this.getCasClientConfigManager().updateConfig(this.getConfig());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	

	public CasClientConfig getConfig() {
		return _config;
	}
	public void setConfig(CasClientConfig config) {
		this._config = config;
	}

	public ICasClientConfigManager getCasClientConfigManager() {
		return _casClientConfigManager;
	}
	public void setCasClientConfigManager(ICasClientConfigManager casClientConfigManager) {
		this._casClientConfigManager = casClientConfigManager;
	}

	private CasClientConfig _config;
	private ICasClientConfigManager _casClientConfigManager;

}