/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpsharedocs.aps.system.services.sharedocs;

import org.entando.entando.plugins.jpsharedocs.aps.system.JpSharedocsSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;

public class SharedocsManager extends AbstractService implements ISharedocsManager{
	
	private static final Logger _logger = LoggerFactory.getLogger(SharedocsManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
		loadConfigs();
	}
	
	/**
	 * Load the configuration
	 * @throws ApsSystemException
	 */
	private void loadConfigs() throws ApsSystemException {
		try {
			String xml = this.getConfigManager().getConfigItem(JpSharedocsSystemConstants.SHAREDOC_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Missing configuration item: " + JpSharedocsSystemConstants.SHAREDOC_CONFIG_ITEM);
			}
			SharedocsConfig config = new SharedocsConfig(xml);
			setConfig(config);
		} catch (Throwable t) {
			_logger.error("Error loading config", t);
			throw new ApsSystemException("Error loading configuration", t);
		}
	}
	
	@Override
	public void updateConfiguration(SharedocsConfig config) throws Throwable {
		String xml =  null;
		
		try {
			if (null != config) {
				xml = config.toXML();
				this.getConfigManager().updateConfigItem(JpSharedocsSystemConstants.SHAREDOC_CONFIG_ITEM, xml);
				setConfig(config);
			}
		} catch (Throwable t) {
			throw new ApsSystemException("Error updating configuration", t);
		}
	}
	
	@Override
	public SharedocsConfig getConfiguration() {
		if (null != _config) {
			return _config.clone();
		}
		return null;
	}
	
	public ConfigInterface getConfigManager() {
		return _configManager;
	}
	
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	protected SharedocsConfig getConfig() {
		return _config;
	}

	protected void setConfig(SharedocsConfig config) {
		this._config = config;
	}

	private ConfigInterface _configManager;
	private SharedocsConfig _config;
}
