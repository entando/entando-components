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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.PageUserConfigBean;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.WidgetUpdateInfoBean;

/**
 * Interface for the service of MyPortal that handles the configuration of the page models. MyPortal enables
 * users to customize the page models of the portal they are visiting.
 * @author E.Santoboni
 */
public interface IPageUserConfigManager {
	
	@Deprecated
	public PageUserConfigBean getUserConfig(String username) throws ApsSystemException;
	
	public PageUserConfigBean getUserConfig(UserDetails user) throws ApsSystemException;
	
	@Deprecated
	public List<WidgetType> getCustomizableShowlets(UserDetails user) throws ApsSystemException;

	public List<WidgetType> getCustomizableWidgets(UserDetails user) throws ApsSystemException;

	public CustomPageConfig getGuestPageConfig(IPage page, HttpServletRequest request) throws ApsSystemException;
	
	@Deprecated
	public Widget[] getShowletsToRender(IPage page, Widget[] customShowlets) throws ApsSystemException;
	
	public Widget[] getWidgetsToRender(IPage page, Widget[] customWidgets) throws ApsSystemException;

	public void updateUserPageConfig(String username, IPage page, WidgetUpdateInfoBean[] updateInfo) throws ApsSystemException;

	public void updateGuestPageConfig(IPage page, WidgetUpdateInfoBean[] updateInfos, HttpServletRequest request, HttpServletResponse response) throws ApsSystemException;

	public void removeUserPageConfig(String username, IPage page) throws ApsSystemException;

	public void removeGuestPageConfig(IPage page, HttpServletRequest request, HttpServletResponse response) throws ApsSystemException;
	
	@Deprecated
	public WidgetType getVoidShowlet();
	
	public WidgetType getVoidWidget();
	
	public static final int STATUS_OPEN = 0;//DEFAULT STATUS
	public static final int STATUS_CLOSE = 1;

}