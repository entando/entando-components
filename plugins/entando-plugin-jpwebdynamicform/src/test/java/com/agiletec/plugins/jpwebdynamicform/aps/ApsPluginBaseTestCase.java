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
package com.agiletec.plugins.jpwebdynamicform.aps;

import javax.sql.DataSource;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.plugins.jpwebdynamicform.PluginConfigTestUtils;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.JpwebdynamicformSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpwebdynamicform.util.JpwebdynamicformTestHelper;

public class ApsPluginBaseTestCase extends BaseTestCase {
	
	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }	
	
    @Override
	protected void tearDown() throws Exception {
    	this._helper.cleanMessages();
		super.tearDown();
	}

	private void init() throws Exception {
		try {
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
			ILangManager langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
			this._messageManager = (IMessageManager) this.getService(JpwebdynamicformSystemConstants.MESSAGE_MANAGER);
			this._helper = new JpwebdynamicformTestHelper(dataSource, langManager, this._messageManager);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	protected JpwebdynamicformTestHelper _helper;
	protected IMessageManager _messageManager;	
}
