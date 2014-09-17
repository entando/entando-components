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
package com.agiletec.plugins.jpcasclient.aps.system.services.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpcasclient.CasClientPluginSystemCostants;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.parse.ConfigDOM;

public class CasClientConfigManager extends AbstractService implements ICasClientConfigManager {

	private static final Logger _logger =  LoggerFactory.getLogger(CasClientConfigManager.class);
	
	@Override
	public void init() throws Exception {
		String configItem = this.getConfigManager().getConfigItem(CasClientPluginSystemCostants.JPCASCLIENT_CONFIG_ITEM);
		ConfigDOM configDOM = new ConfigDOM();
		CasClientConfig casClientConfig = configDOM.extractConfig(configItem);
		this.setClientConfig(casClientConfig);
	}
	
	@Override
	protected void release() {
		this.setClientConfig(null);
	}
	
	@Override
	public void updateConfig(CasClientConfig config) throws ApsSystemException {
		ConfigDOM configDOM = new ConfigDOM();
		String configurationItem;
		try {
			configurationItem = configDOM.createConfigXml(config);
			this.getConfigManager().updateConfigItem(CasClientPluginSystemCostants.JPCASCLIENT_CONFIG_ITEM, configurationItem);
			this.setClientConfig(config);
		} catch (ApsSystemException t) {
			_logger.error("Error updating config", t);
			throw new ApsSystemException("Error updating config", t);
		}
	}

	public ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	@Override
	public CasClientConfig getClientConfig() {
		return _clientConfig;
	}
	private void setClientConfig(CasClientConfig clientConfig) {
		this._clientConfig = clientConfig;
	}

	private ConfigInterface _configManager;
	private CasClientConfig _clientConfig;
	
}
