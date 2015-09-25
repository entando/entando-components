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
