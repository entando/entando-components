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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig;

import java.util.List;

import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.PageUserConfigBean;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.WidgetUpdateInfoBean;

/**
 * @author E.Santoboni
 */
public interface IPageUserConfigDAO {

	/**
	 * Return a bean containing the customized configuration of the user. If the user did not
	 * customize any frame it returns null.
	 * @param username
	 * @return bean The bean containing the myportal customization of the current user, null otherwise.
	 */
	public PageUserConfigBean getUserConfig(String username);
	
	@Deprecated
	public List<WidgetType> buildCustomizableShowletsList(MyPortalConfig config);
	
	/**
	 * Return from the configuration the list of the widgets currently configurable.
	 * @param config class that maps the jpmyportal configuration
	 * @return the list of widgets, null otherwise
	 */
	public List<WidgetType> buildCustomizableWidgetsList(MyPortalConfig config);
	
	public void syncCustomization(List<WidgetType> configurableWidgets, String voidWidgetCode);

	public void updateUserPageConfig(String username, IPage page, WidgetUpdateInfoBean[] updateInfos);

	public void removeUserPageConfig(String username, String pageCode, Integer framePosition);
	
	@Deprecated
	public void removeUnauthorizedShowlet(String username, String widgetCode);
	
	public void removeUnauthorizedWidget(String username, String showletCode);
	
}
