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
package com.agiletec.plugins.jpmail.apsadmin.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;

import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailConfig;
import com.agiletec.plugins.jpmail.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpmail.util.JpmailTestHelper;

import com.opensymphony.xwork2.Action;

public class TestSmtpConfigAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	/**
	 * Tests the 'edit' action.
	 * @throws Throwable
	 */
	public void testEdit() throws Throwable {
		MailConfig config = this._mailManager.getMailConfig();
		
		this.setUserOnSession("admin");
		this.initAction("/do/jpmail/MailConfig", "editSmtp");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		SmtpConfigAction action = (SmtpConfigAction) this.getAction();
		assertEquals(config.isDebug(), action.isDebug());
		assertEquals(config.getSmtpHost(), action.getSmtpHost());
		assertEquals(config.getSmtpUserName(), action.getSmtpUserName());
		assertEquals(config.getSmtpPassword(), action.getSmtpPassword());
	}
	
	public void testSaveFailure_1() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		String result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		assertEquals(2, this.getAction().getFieldErrors().size());
		assertEquals(0, this.getAction().getActionErrors().size());
		
		params.put("debug", "true");
		params.put("smtpUserName", "username");
		params.put("smtpPassword", "password");
		result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(2, fieldErrors.size());
		assertEquals(1, fieldErrors.get("smtpHost").size());
		assertEquals(1, fieldErrors.get("smtpProtocol").size());
	}

	public void testSaveFailure_2() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("smtpProtocol", "invalidNumber");
		params.put("smtpPort", "invalidNumber");
		String result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(3, fieldErrors.size());
		assertEquals(1, fieldErrors.get("smtpHost").size());
		assertEquals(2, fieldErrors.get("smtpProtocol").size());
		assertEquals(1, fieldErrors.get("smtpPort").size());
	}

	public void testSaveFailure_3() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("debug", "true");
		params.put("smtpProtocol", "1");
		params.put("smtpPort", "invalidNumber");
		String result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(3, fieldErrors.size());
		assertEquals(1, fieldErrors.get("smtpHost").size());
		assertEquals(1, fieldErrors.get("smtpPort").size());
		assertEquals(1, fieldErrors.get("smtpUserName").size());
		
		params.put("smtpProtocol", "0");
		params.put("smtpPort", "invalidNumber");
		result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		
		System.out.println(this.getAction().getFieldErrors());
		
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(2, fieldErrors.size());
		assertEquals(1, fieldErrors.get("smtpHost").size());
		assertEquals(1, fieldErrors.get("smtpPort").size());
		
		params.put("smtpHost", "localhost");
		params.put("smtpPort", "25");
		params.put("smtpProtocol", "1");
		params.put("smtpUserName", "");
		params.put("smtpPassword", "");
		result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("smtpUserName").size());
	}

	public void testSaveFailure_4() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("smtpHost", "localhost");
		params.put("debug", "true");
		params.put("smtpProtocol", "6");
		params.put("smtpPort", "25");
		String result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("smtpProtocol").size());
	}
	
	/**
	 * Tests the 'save' action with successful result.
	 * @throws Throwable
	 */
	public void testSaveSuccessful() throws Throwable {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("smtpHost", "host");
			params.put("smtpProtocol", "0");
			params.put("smtpUserName", "username");
			params.put("smtpPassword", "password");
			String result = this.executeSave("admin", params);
			assertEquals(Action.SUCCESS, result);
			
			MailConfig config = this._mailManager.getMailConfig();
			assertEquals("host", config.getSmtpHost());
			assertNull(config.getSmtpPort());
			assertNull(config.getSmtpTimeout());
			assertEquals("username", config.getSmtpUserName());
			assertEquals("password", config.getSmtpPassword());
			
			params.put("smtpPort", "2525");
			params.put("smtpTimeout", "2000");
			result = this.executeSave("admin", params);
			assertEquals(Action.SUCCESS, result);
			config = this._mailManager.getMailConfig();
			assertEquals("host", config.getSmtpHost());
			assertEquals(new Integer(2525), config.getSmtpPort());
			assertEquals(new Integer(2000), config.getSmtpTimeout());
			assertEquals("username", config.getSmtpUserName());
			assertEquals("password", config.getSmtpPassword());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.resetConfig();
		}
	}
	
	protected String executeSave(String currentUser, Map<String, String> params) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/jpmail/MailConfig", "saveSmtp");
		this.addParameters(params);
		return this.executeAction();
	}
	
    protected void init() throws Exception {
    	try {
    		ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
    		this._mailManager = (IMailManager) this.getService(JpmailSystemConstants.MAIL_MANAGER);
    		this._helper = new JpmailTestHelper(configManager, this._mailManager);
		} catch (Exception e) {
			throw e;
		}
    }
	
	protected JpmailTestHelper _helper;
	protected IMailManager _mailManager;
	
}