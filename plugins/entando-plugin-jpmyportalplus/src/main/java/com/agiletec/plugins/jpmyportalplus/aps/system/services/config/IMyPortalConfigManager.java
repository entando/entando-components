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