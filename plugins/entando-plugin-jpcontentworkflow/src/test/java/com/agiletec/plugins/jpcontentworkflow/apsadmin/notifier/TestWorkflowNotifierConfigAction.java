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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.notifier;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpcontentworkflow.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.IWorkflowNotifierManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.model.NotifierConfig;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.notifier.WorkflowNotifierConfigAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestWorkflowNotifierConfigAction extends ApsAdminPluginBaseTestCase {
	
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testConfig() throws Throwable {
		String result = this.executeConfig("supervisorCoach");
		assertEquals("userNotAllowed", result);
		
		result = this.executeConfig("admin");
		assertEquals(Action.SUCCESS, result);
		WorkflowNotifierConfigAction action = (WorkflowNotifierConfigAction) this.getAction();
		NotifierConfig config = action.getConfig();
		assertFalse(config.isActive());
		assertEquals(24, config.getHoursDelay());
		assertEquals("CODE1", config.getSenderCode());
		//assertEquals("email", config.getMailAttrName());
		assertEquals("04/12/2008", action.getStartDate());
		assertEquals(16, action.getHour());
		assertEquals(8, action.getMinute());
		
		assertFalse(config.isHtml());
                assertEquals("[My Own Portal]: A content changed", config.getSubject());
                assertEquals("Hi {user},<br />these contents require your attention<br /><br />", config.getHeader());
                assertEquals("<br />Content {type} - {descr} - Status {status}<br />", config.getTemplate());
                assertEquals("<br />End (footer)", config.getFooter());
	}
	
	public void testSaveFailure() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		String result = this.executeSave("supervisorCoach", params);
		assertEquals("userNotAllowed", result);
		
		result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(4, fieldErrors.size());
		
		params.put("hour", "24");
		params.put("minute", "60");
		result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(6, fieldErrors.size());
		
		params.put("config.hoursDelay", "24");
		params.put("hour", "23");
		params.put("minute", "59");
		result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(3, fieldErrors.size());
		
		params.put("config.subject", "");
		params.put("config.template", "");
		result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(3, fieldErrors.size());
		
		params.put("config.subject", "subject");
		params.put("config.template", "template");
		result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertNotNull(fieldErrors.get("startDate"));
		
		params.put("startDate", "05/12/2008");
		params.put("hour", "24");
		result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertNull(fieldErrors.get("startDate"));
		assertNotNull(fieldErrors.get("hour"));
	}
	
	public void testSave() throws Throwable {
		String config = this._configManager.getConfigItem(JpcontentworkflowSystemConstants.WORKFLOW_NOTIFIER_CONFIG_ITEM);
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("config.active", "true");
			params.put("config.senderCode", "CODE1");
			params.put("config.mailAttrName", "email");
			params.put("config.hoursDelay", "10");
			params.put("startDate", "19/11/2009");
			params.put("hour", "20");
			params.put("minute", "40");
			
			params.put("config.html", "true");
			params.put("config.subject", "subject");
			params.put("config.header", "header");
			params.put("config.template", "template");
			params.put("config.footer", "footer");
			
			String result = this.executeSave("admin", params);
			assertEquals(Action.SUCCESS, result);
			NotifierConfig updatedNotifierConfig = this._notifierManager.getNotifierConfig();
			
			assertTrue(updatedNotifierConfig.isActive());
			assertEquals(10, updatedNotifierConfig.getHoursDelay());
			String startDate = DateFormatter.format(updatedNotifierConfig.getStartScheduler());
			assertEquals("19/11/2009 20:40", startDate);
			assertEquals("CODE1", updatedNotifierConfig.getSenderCode());
			//assertEquals("email", updatedNotifierConfig.getMailAttrName());
			assertTrue(updatedNotifierConfig.isHtml());
			assertEquals("subject", updatedNotifierConfig.getSubject());
			assertEquals("header", updatedNotifierConfig.getHeader());
			assertEquals("template", updatedNotifierConfig.getTemplate());
			assertEquals("footer", updatedNotifierConfig.getFooter());
		} catch(Throwable t) {
			throw t;
		} finally {
			this._configManager.updateConfigItem(JpcontentworkflowSystemConstants.WORKFLOW_NOTIFIER_CONFIG_ITEM, config);
		}
	}
	
	protected String executeConfig(String currentUserName) throws Throwable {
		this.setUserOnSession(currentUserName);
		this.initAction("/do/jpcontentworkflow/Notifier", "config");
		String result = this.executeAction();
		return result;
	}
	
	protected String executeSave(String currentUserName, Map<String, String> params) throws Throwable {
		this.setUserOnSession(currentUserName);
		this.initAction("/do/jpcontentworkflow/Notifier", "save");
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}
	
	private void init() {
		this._configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		this._notifierManager = (IWorkflowNotifierManager) this.getService(JpcontentworkflowSystemConstants.WORKFLOW_NOTIFIER_MANAGER);
	}
	
	private ConfigInterface _configManager;
	private IWorkflowNotifierManager _notifierManager;
	private static DateFormat DateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
}
