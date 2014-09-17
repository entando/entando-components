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
