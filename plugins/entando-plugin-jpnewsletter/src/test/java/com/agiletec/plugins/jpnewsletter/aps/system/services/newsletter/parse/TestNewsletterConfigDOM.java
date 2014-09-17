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
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.parse;

import com.agiletec.plugins.jpnewsletter.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpnewsletter.aps.system.JpnewsletterSystemConstants;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.parse.NewsletterConfigDOM;

public class TestNewsletterConfigDOM extends ApsPluginBaseTestCase {
	
	public void testExtractConfig() throws Throwable {
		String xml = this._configManager.getConfigItem(JpnewsletterSystemConstants.NEWSLETTER_CONFIG_ITEM);
		NewsletterConfig config = new NewsletterConfigDOM().extractConfig(xml);
		this.checkOriginaryConfig(config);
	}
	
	public void testUpdateConfig() throws Throwable {
		NewsletterConfigDOM newsletterConfigDOM = new NewsletterConfigDOM();
		NewsletterConfig config = this.createNewsletterConfig();
		String xml = newsletterConfigDOM.createConfigXml(config);
		NewsletterConfig updatedConfig = newsletterConfigDOM.extractConfig(xml);
		this.compareConfigs(config, updatedConfig);
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