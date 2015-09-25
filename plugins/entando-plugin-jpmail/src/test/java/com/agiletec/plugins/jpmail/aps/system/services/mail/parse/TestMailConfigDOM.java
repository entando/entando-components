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
package com.agiletec.plugins.jpmail.aps.system.services.mail.parse;


import com.agiletec.plugins.jpmail.aps.system.services.mail.AbstractMailConfigTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.MailConfig;
import com.agiletec.plugins.jpmail.aps.services.mail.parse.MailConfigDOM;

/**
 * Testing class for XML configuration DOM.
 * @version 1.0
 * @author E.Mezzano
 *
 */
public class TestMailConfigDOM extends AbstractMailConfigTestCase {
	
	/**
	 * Tests the extraction of the configuration from the XML.
	 * @throws Throwable
	 */
	public void testExtractConfig() throws Throwable {
		String xml = this._configManager.getConfigItem(JpmailSystemConstants.MAIL_CONFIG_ITEM);
		MailConfig mailConfig = new MailConfigDOM().extractConfig(xml);
		this.checkOriginaryConfig(mailConfig);
	}
	
	/**
	 * Tests the updating of the configuration.
	 * @throws Throwable
	 */
	public void testUpdateConfig() throws Throwable {
		MailConfigDOM mailConfigDom = new MailConfigDOM();
		String xml = this._configManager.getConfigItem(JpmailSystemConstants.MAIL_CONFIG_ITEM);
		MailConfig originaryConfig = mailConfigDom.extractConfig(xml);
		try {
			MailConfig config = this.createMailConfig();
			xml = mailConfigDom.createConfigXml(config);
			MailConfig updatedConfig = mailConfigDom.extractConfig(xml);
			this.compareConfigs(config, updatedConfig);
		} catch (Throwable t) {
			throw t;
		} finally {
			xml = mailConfigDom.createConfigXml(originaryConfig);
			MailConfig updatedConfig = mailConfigDom.extractConfig(xml);
			this.compareConfigs(originaryConfig, updatedConfig);
		}
	}
	
	@Override
	protected void init() throws Exception {
		super.init();
		try {
			this._configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		} catch (Exception e) {
			throw e;
		}
	}
	
	private ConfigInterface _configManager;
	
}