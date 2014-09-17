/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel;

import com.agiletec.plugins.jpmyportalplus.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;

import java.util.Map;

/**
 * @author E.Santoboni
 */
public class TestMyPortalPageModelManager extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testPageModelConfig_1() throws Throwable {
		Map<Integer, MyPortalFrameConfig> config = this._myPortalpageModelManager.getPageModelConfig("jpmyportalplus_pagemodel");
		assertNotNull(config);
		
		MyPortalFrameConfig config0 = config.get(0);
		assertTrue(config0.isLocked());
		assertNull(config0.getColumn());
		
		MyPortalFrameConfig config1 = config.get(1);
		assertTrue(!config1.isLocked());
		assertEquals(1, config1.getColumn().intValue());
		
		MyPortalFrameConfig config2 = config.get(2);
		assertTrue(!config2.isLocked());
		assertEquals(1, config2.getColumn().intValue());
		
		MyPortalFrameConfig config3 = config.get(3);
		assertTrue(!config3.isLocked());
		assertEquals(2, config3.getColumn().intValue());
		
		MyPortalFrameConfig config4 = config.get(4);
		assertTrue(!config4.isLocked());
		assertEquals(2, config4.getColumn().intValue());
		
		MyPortalFrameConfig config5 = config.get(5);
		assertTrue(!config5.isLocked());
		assertEquals(3, config5.getColumn().intValue());
		
		MyPortalFrameConfig config6 = config.get(6);
		assertTrue(!config6.isLocked());
		assertEquals(3, config6.getColumn().intValue());
		
		MyPortalFrameConfig config7 = config.get(7);
		assertTrue(config7.isLocked());
		assertNull(config7.getColumn());
		
	}
	
	private void init() throws Exception {
		try {
			this._myPortalpageModelManager = (IMyPortalPageModelManager) this.getService(JpmyportalplusSystemConstants.MYPORTAL_MODEL_CONFIG_MANAGER);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
	
	protected IMyPortalPageModelManager _myPortalpageModelManager;
	
}
