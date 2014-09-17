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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpmyportalplus.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.PageUserConfigBean;

/**
 * @author E.Santoboni
 */
public class TestMyPortalConfigManager extends ApsPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testGetUserConfig() throws Throwable {
		UserDetails user = this.getUser("editorCustomers");
		PageUserConfigBean config = this._pageUserConfigManager.getUserConfig(user);
		assertNotNull(config);
		assertEquals(1, config.getConfig().size());
		CustomPageConfig pageConfig = config.getConfig().get("jpmyportalplus_testpage");
		assertNotNull(pageConfig);
		Widget[] showlets = pageConfig.getConfig();
		Integer[] status = pageConfig.getStatus();
		assertEquals(8, status.length);
		assertEquals(showlets.length, status.length);

		assertNull(status[0]);
		assertEquals(new Integer(0), status[1]);
		assertEquals(new Integer(1), status[2]);
		assertEquals(new Integer(0), status[3]);
		assertNull(status[4]);
		assertNull(status[5]);
		assertEquals(new Integer(1), status[6]);
		assertNull(status[7]);

		assertNull(showlets[0]);
		assertEquals("jpmyportalplus_void", showlets[1].getType().getCode());
		assertEquals("jpmyportalplus_test_widget_1", showlets[2].getType().getCode());
		assertEquals("jpmyportalplus_sample_widget", showlets[3].getType().getCode());
		assertNull(showlets[4]);
		assertNull(showlets[5]);
		assertEquals("jpmyportalplus_test_widget_3", showlets[6].getType().getCode());
		assertNull(showlets[7]);
	}

	public void testGetWidgetsToRender() throws Throwable {
		UserDetails user = this.getUser("editorCustomers");
		PageUserConfigBean config = this._pageUserConfigManager.getUserConfig(user);
		IPage page = this._pageManager.getPage("jpmyportalplus_testpage");
		CustomPageConfig pageConfig = config.getConfig().get("jpmyportalplus_testpage");
		Widget[] widgets = this._pageUserConfigManager.getWidgetsToRender(page, pageConfig.getConfig());

		assertEquals(8, widgets.length);

		assertEquals("login_form", widgets[0].getType().getCode());
		assertEquals("jpmyportalplus_void", widgets[1].getType().getCode());
		assertEquals("jpmyportalplus_test_widget_1", widgets[2].getType().getCode());
		assertEquals("jpmyportalplus_sample_widget", widgets[3].getType().getCode());
		assertEquals("jpmyportalplus_test_widget_3", widgets[4].getType().getCode());
		assertNull(widgets[5]);
		assertEquals("jpmyportalplus_test_widget_3", widgets[6].getType().getCode());
		assertNull(widgets[7]);
	}

	private void init() throws Exception {
		try {
			this._pageUserConfigManager = (IPageUserConfigManager) this.getService(JpmyportalplusSystemConstants.PAGE_USER_CONFIG_MANAGER);
			this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	protected IPageManager _pageManager;
	protected IPageUserConfigManager _pageUserConfigManager;

}
