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
