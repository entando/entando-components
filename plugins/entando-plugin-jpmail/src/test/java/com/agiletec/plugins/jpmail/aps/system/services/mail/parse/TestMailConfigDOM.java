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