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

import java.util.HashSet;
import java.util.Set;

import com.agiletec.plugins.jpmyportalplus.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.IMyPortalConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;

/**
 * @author E.Santoboni
 */
public class TestPageUserConfigManager extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testGetConfig() throws Throwable {
		MyPortalConfig config = this._myPortalConfigManager.getConfig();
		assertNotNull(config);
		assertEquals(3, config.getAllowedShowlets().size());
		assertTrue(config.getAllowedShowlets().contains("jpmyportalplus_sample_widget"));
		assertTrue(config.getAllowedShowlets().contains("jpmyportalplus_test_widget_1"));
	}
	
	public void testUpdateConfig() throws Throwable {
		MyPortalConfig config = this._myPortalConfigManager.getConfig();
		assertEquals(3, config.getAllowedShowlets().size());
		try {
			MyPortalConfig newConfig = new MyPortalConfig();
			assertNull(newConfig.getAllowedShowlets());
			Set<String> allowedShowlets = new HashSet<String>();
			allowedShowlets.addAll(config.getAllowedShowlets());
			allowedShowlets.add("jpmyportalplus_test_widget_2");
			newConfig.setAllowedShowlets(allowedShowlets);
			this._myPortalConfigManager.saveConfig(newConfig);
			MyPortalConfig extractedNewConfig = this._myPortalConfigManager.getConfig();
			assertEquals(4, extractedNewConfig.getAllowedShowlets().size());
		} catch (Exception e) {
			throw e;
		} finally {
			this._myPortalConfigManager.saveConfig(config);
			MyPortalConfig extractedConfig = this._myPortalConfigManager.getConfig();
			assertEquals(3, extractedConfig.getAllowedShowlets().size());
		}
	}
	
	private void init() throws Exception {
		try {
			this._myPortalConfigManager = (IMyPortalConfigManager) this.getService(JpmyportalplusSystemConstants.MYPORTAL_CONFIG_MANAGER);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
	
	protected IMyPortalConfigManager _myPortalConfigManager;
	
}
