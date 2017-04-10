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

import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.apsadmin.system.BaseAction;
import com.opensymphony.xwork2.Action;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.ContentThreadConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ConfigAction extends BaseAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(ConfigAction.class);
	
	public String viewItem() {
		try {
			String config = this.getBaseConfigManager().getConfigItem(this.getItem());
			this.setConfig(config);
		} catch (Throwable t) {
			_logger.error("Error in viewItem", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}
	
	public String saveItem() {
		try {
			this.getBaseConfigManager().updateConfigItem(this.getItem(), this.getConfig());
			this.addActionMessage(this.getText("jpcontentscheduler.saveItem.success"));
		} catch (Throwable t) {
			_logger.error("Error saving item", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}
	

	public void setItem(String item) {
		this._item = item;
	}
	public String getItem() {
		return _item;
	}

	public void setConfig(String config) {
		this._config = config;
	}
	public String getConfig() {
		return _config;
	}
	
	public void setBaseConfigManager(ConfigInterface baseConfigManager) {
		this._baseConfigManager = baseConfigManager;
	}
	public ConfigInterface getBaseConfigManager() {
		return _baseConfigManager;
	}
	
	private ConfigInterface _baseConfigManager;
	
	private String _item = ContentThreadConstants.CONTENTTHREAD_CONFIG_ITEM;
	private String _config;
	
}
