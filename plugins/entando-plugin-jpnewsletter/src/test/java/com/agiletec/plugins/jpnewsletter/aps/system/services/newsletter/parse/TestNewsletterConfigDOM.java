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