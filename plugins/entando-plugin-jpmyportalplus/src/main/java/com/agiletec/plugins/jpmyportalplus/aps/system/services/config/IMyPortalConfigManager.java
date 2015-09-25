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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;

/**
 * Interface for the service of MyPortal that handles the configuration.
 * @author E.Santoboni
 */
public interface IMyPortalConfigManager {
	
	public MyPortalConfig getConfig();
	
	public void saveConfig(MyPortalConfig config) throws ApsSystemException;
	
	@Deprecated
	public List<WidgetType> getCustomizableShowlets();
	
	/**
	 * Get the list of the widget that cannot be handled by the jpmyportal plugin
	 * @return list of the widget
	 */
	public List<WidgetType> getCustomizableWidgets();
	
	@Deprecated
	public String getVoidShowletCode();
	
	public String getVoidWidgetCode();
	
}