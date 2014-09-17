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