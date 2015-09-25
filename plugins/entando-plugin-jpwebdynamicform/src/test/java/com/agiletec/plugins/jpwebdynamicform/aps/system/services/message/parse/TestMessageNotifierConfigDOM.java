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
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.parse;

import java.util.Map;

import com.agiletec.plugins.jpwebdynamicform.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.JpwebdynamicformSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.MessageModel;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.MessageTypeNotifierConfig;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.parse.MessageNotifierConfigDOM;

public class TestMessageNotifierConfigDOM extends ApsPluginBaseTestCase {
	
	public void testExtractCreateConfig() throws Throwable {
		ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		String xml = configManager.getConfigItem(JpwebdynamicformSystemConstants.MESSAGE_NOTIFIER_CONFIG_ITEM);
		
		MessageNotifierConfigDOM configDOM = new MessageNotifierConfigDOM();
		Map<String, MessageTypeNotifierConfig> config = configDOM.extractConfig(xml);
		this.checkOriginaryConfig(config);
		
		String newXml = configDOM.createConfigXml(config);
		Map<String, MessageTypeNotifierConfig> newConfig = configDOM.extractConfig(newXml);
		this.checkOriginaryConfig(newConfig);
	}
	
	private void checkOriginaryConfig(Map<String, MessageTypeNotifierConfig> config) {
		assertEquals(2, config.size());
		MessageTypeNotifierConfig config1 = config.get("PER");
		assertEquals("PER", config1.getTypeCode());
		assertTrue(config1.isStore());
		assertEquals("CODE1", config1.getSenderCode());
		assertEquals("eMail", config1.getMailAttrName());
		assertEquals(2, config1.getRecipientsTo().length);
		assertEquals(1, config1.getRecipientsCc().length);
		assertEquals(1, config1.getRecipientsBcc().length);
		
		assertNotNull(config1.getMessageModel());
		
		MessageTypeNotifierConfig config2 = config.get("COM");
		assertEquals("COM", config2.getTypeCode());
		assertFalse(config2.isStore());
		assertEquals("CODE2", config2.getSenderCode());
		assertEquals("eMail", config2.getMailAttrName());
		assertEquals(3, config2.getRecipientsTo().length);
		assertEquals(2, config2.getRecipientsCc().length);
		assertEquals(1, config2.getRecipientsBcc().length);
		
		MessageModel messageModelIt = config2.getMessageModel();
		assertEquals("Corpo della mail COM", messageModelIt.getBodyModel());
		assertEquals("Oggetto della mail COM", messageModelIt.getSubjectModel());
	}
	
}