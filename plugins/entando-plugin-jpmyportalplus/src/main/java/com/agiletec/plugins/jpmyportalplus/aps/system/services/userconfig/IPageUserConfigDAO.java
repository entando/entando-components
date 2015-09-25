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
