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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.config;

import java.util.List;

import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.events.WidgetTypeChangedEvent;
import org.entando.entando.aps.system.services.widgettype.events.WidgetTypeChangedObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.parse.MyPortalPlusConfigDOM;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigDAO;
import java.util.Iterator;

/**
 * @author E.Santoboni
 */
public class MyPortalConfigManager extends AbstractService implements IMyPortalConfigManager, WidgetTypeChangedObserver {

	private static final Logger _logger = LoggerFactory.getLogger(MyPortalConfigManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadConfig();
		this.buildCustomizableWidgetsSet();
		this.syncPageModelUserDatabase();
		_logger.debug("{} ready. the code of the 'void' widget is '{}'", this.getClass().getName(), this.getVoidWidgetCode());
	}
	
	@Override
	public void updateFromShowletTypeChanged(WidgetTypeChangedEvent event) {
		if (null == event) {
			return;
		}
		try {
			if (event.getOperationCode() == WidgetTypeChangedEvent.REMOVE_OPERATION_CODE) {
				MyPortalConfig config = this.getConfig();
				if (null == config) {
					return;
				}
				Iterator<String> iter = config.getAllowedWidgets().iterator();
				while (iter.hasNext()) {
					String widgetTypeCode = iter.next();
					if (widgetTypeCode.equals(event.getWidgetTypeCode())) {
						config.getAllowedWidgets().remove(widgetTypeCode);
						this.saveConfig(config);
						return;
					}
				}
			} else {
				this.init();
			}
		} catch (Throwable t) {
			_logger.error("Error on init after WidgetTypeChangedEvent", t);
		}
	}
	
	private void loadConfig() throws ApsSystemException {
		try {
			String xml = this.getConfigManager().getConfigItem(JpmyportalplusSystemConstants.MYPORTALPLUS_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Missing configuration item: " + JpmyportalplusSystemConstants.MYPORTALPLUS_CONFIG_ITEM);
			}
			MyPortalPlusConfigDOM configDom = new MyPortalPlusConfigDOM();
			this.setConfig(configDom.extractConfig(xml));
		} catch (Throwable t) {
			_logger.error("error loading config", t);
			throw new ApsSystemException("error loading config", t);
		}
	}
	
	/**
	 * Get from the configuration the list of the widgets that can be customized in a page model.
	 */
	private void buildCustomizableWidgetsSet() {
		List<WidgetType> widgets = this.getPageUserConfigDAO().buildCustomizableWidgetsList(this.getConfig());
		this.setCustomizableWidgets(widgets);
	}
	
	private void syncPageModelUserDatabase() {
		this.getPageUserConfigDAO().syncCustomization(this.getCustomizableWidgets(), this.getVoidWidgetCode());
	}
	
	@Override
	public MyPortalConfig getConfig() {
		return _config;
	}
	protected void setConfig(MyPortalConfig config) {
		this._config = config;
	}
	
	@Override
	public void saveConfig(MyPortalConfig config) throws ApsSystemException {
		try {
			MyPortalPlusConfigDOM configDom = new MyPortalPlusConfigDOM();
			String xml = configDom.createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpmyportalplusSystemConstants.MYPORTALPLUS_CONFIG_ITEM, xml);
			this.setConfig(config);
			this.buildCustomizableWidgetsSet();
			this.syncPageModelUserDatabase();
		} catch (Throwable t) {
			_logger.error("Error saving myportal configuration", t);
			throw new ApsSystemException("Error saving myportal configuration", t);
		}
	}
	
	@Override
	@Deprecated
	public List<WidgetType> getCustomizableShowlets() {
		return this.getCustomizableWidgets();
	}
	@Deprecated
	public void setCustomizableShowlets(List<WidgetType> customizableShowlets) {
		this.setCustomizableWidgets(customizableShowlets);
	}
	
	@Override
	public List<WidgetType> getCustomizableWidgets() {
		return _customizableWidgets;
	}
	public void setCustomizableWidgets(List<WidgetType> customizableWidgets) {
		this._customizableWidgets = customizableWidgets;
	}
	
	@Override
	@Deprecated
	public String getVoidShowletCode() {
		return this.getVoidWidgetCode();
	}
	@Deprecated
	public void setVoidShowletCode(String voidShowletCode) {
		this.setVoidWidgetCode(voidShowletCode);
	}
	
	@Override
	public String getVoidWidgetCode() {
		return _voidWidgetCode;
	}
	public void setVoidWidgetCode(String voidWidgetCode) {
		this._voidWidgetCode = voidWidgetCode;
	}
	
	protected IPageUserConfigDAO getPageUserConfigDAO() {
		return _pageUserConfigDAO;
	}
	public void setPageUserConfigDAO(IPageUserConfigDAO pageModelUserConfigDAO) {
		this._pageUserConfigDAO = pageModelUserConfigDAO;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	private List<WidgetType> _customizableWidgets;
	
	private String _voidWidgetCode;
	private ConfigInterface _configManager;
	private IPageUserConfigDAO _pageUserConfigDAO;
	
	private MyPortalConfig _config;
	
}