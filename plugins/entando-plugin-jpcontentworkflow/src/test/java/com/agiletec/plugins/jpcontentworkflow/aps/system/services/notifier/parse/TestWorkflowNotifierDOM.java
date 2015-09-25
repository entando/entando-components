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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.parse;

import java.util.Date;

import com.agiletec.plugins.jpcontentworkflow.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.model.NotifierConfig;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.parse.WorkflowNotifierDOM;

/**
 * @author E.Santoboni
 */
public class TestWorkflowNotifierDOM extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testExtractConfig() throws Throwable {
		WorkflowNotifierDOM configDOM = new WorkflowNotifierDOM();
		String xml = this._configManager.getConfigItem(JpcontentworkflowSystemConstants.WORKFLOW_NOTIFIER_CONFIG_ITEM);
		NotifierConfig notifierConfig = configDOM.extractConfig(xml);
		assertFalse(notifierConfig.isActive());
		assertEquals(24, notifierConfig.getHoursDelay());
		assertTrue(notifierConfig.getStartScheduler().getTime()<(new Date()).getTime());
		assertEquals("CODE1", notifierConfig.getSenderCode());
		//assertEquals("email", notifierConfig.getMailAttrName());
		assertFalse(notifierConfig.isHtml());
                assertEquals("[My Own Portal]: A content changed", notifierConfig.getSubject());
                assertEquals("Hi {user},<br />these contents require your attention<br /><br />", notifierConfig.getHeader());
                assertEquals("<br />Content {type} - {descr} - Status {status}<br />", notifierConfig.getTemplate());
                assertEquals("<br />End (footer)", notifierConfig.getFooter());
	}
	
	public void testSetNotifierConfig() throws Throwable {
		WorkflowNotifierDOM configDOM = new WorkflowNotifierDOM();
		
		NotifierConfig newConfig = this.prepareNotifierConfig();
		String xml = configDOM.createConfigXml(newConfig);
		
		NotifierConfig extractedConfig = configDOM.extractConfig(xml);
		this.compareNotifiers(newConfig, extractedConfig);
	}
	
	private NotifierConfig prepareNotifierConfig() {
		NotifierConfig notifierConfig = new NotifierConfig();
		
		notifierConfig.setActive(true);
		notifierConfig.setHoursDelay(23);
		notifierConfig.setStartScheduler(new Date());
		
		notifierConfig.setSenderCode("CODE2");
		//notifierConfig.setMailAttrName("eMail");
		notifierConfig.setHtml(true);
		notifierConfig.setSubject("Oggetto");
		notifierConfig.setHeader("header");
		notifierConfig.setTemplate("template");
		notifierConfig.setFooter("footer");
		
		return notifierConfig;
	}
	
	private void compareNotifiers(NotifierConfig nc1, NotifierConfig nc2) {
		assertEquals(nc1.isActive(), nc2.isActive());
		assertEquals(nc1.getHoursDelay(), nc2.getHoursDelay());
		long time1 = nc1.getStartScheduler().getTime()/60000;
		long time2 = nc2.getStartScheduler().getTime()/60000;
		assertEquals(time1, time2);
		assertEquals(nc1.getSenderCode(), nc2.getSenderCode());
		//assertEquals(nc1.getMailAttrName(), nc2.getMailAttrName());
		assertEquals(nc1.isHtml(), nc2.isHtml());
		assertEquals(nc1.getSubject(), nc2.getSubject());
		assertEquals(nc1.getHeader(), nc2.getHeader());
		assertEquals(nc1.getTemplate(), nc2.getTemplate());
		assertEquals(nc1.getFooter(), nc2.getFooter());
	}
	
	private void init() {
		this._configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
	}
	
	private ConfigInterface _configManager;
	
}
