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
