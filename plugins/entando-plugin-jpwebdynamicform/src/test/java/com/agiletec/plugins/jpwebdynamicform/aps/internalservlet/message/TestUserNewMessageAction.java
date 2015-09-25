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
package com.agiletec.plugins.jpwebdynamicform.aps.internalservlet.message;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpwebdynamicform.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailManager;
import com.agiletec.plugins.jpwebdynamicform.aps.internalservlet.message.UserNewMessageAction;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageSearcherDAO;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;
import com.opensymphony.xwork2.Action;

public class TestUserNewMessageAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.activeMailManager(false);
	}
	
	public void testCreateNew() throws Throwable {
		String result = this.executeNew("mainEditor", "");
		assertEquals("voidTypeCode", result);
		result = this.executeNew("mainEditor", "PER");
		assertEquals(Action.SUCCESS, result);
		UserNewMessageAction action = (UserNewMessageAction) this.getAction();
		Message message = action.getMessage();
		assertEquals("PER", message.getTypeCode());
	}
	
	public void testSend() throws Throwable {
		String result = this.executeNew(SystemConstants.GUEST_USER_NAME, "PER");
		assertEquals(Action.SUCCESS, result);
		Map<String, String> params = new HashMap<String, String>();
		result = this.executeSend(SystemConstants.GUEST_USER_NAME, params);
		assertEquals("expiredMessage", result);
		
		params.put("typeCode", "PER");
		result = this.executeSend(SystemConstants.GUEST_USER_NAME, params);
		assertEquals(Action.INPUT, result);
		UserNewMessageAction action = (UserNewMessageAction) this.getAction();
		Collection<String> fieldErrors = action.getFieldErrors().keySet();
		assertEquals(4, fieldErrors.size());
		assertTrue(fieldErrors.contains("Monotext:Name"));
		assertTrue(fieldErrors.contains("Monotext:Surname"));
		assertTrue(fieldErrors.contains("Monotext:eMail"));
		assertTrue(fieldErrors.contains("Monotext:Note"));
		
		params.put("Monotext:Name", "MyName");
		params.put("Monotext:Surname", "MySurname");
		params.put("Monotext:Note", "MyNote");
		params.put("Monotext:eMail", "MyEmail@inesistente.itte");
		result = this.executeSend(SystemConstants.GUEST_USER_NAME, params);
		assertEquals(Action.SUCCESS, result);
		List<String> messagesId = this._messageManager.loadMessagesId(null);
		assertEquals(1, messagesId.size());
		Message message = this._messageManager.getMessage(messagesId.get(0));
		assertEquals(SystemConstants.GUEST_USER_NAME, message.getUsername());
		
		params.put("Monotext:eMail", "MyEmail");
		result = this.executeNew("mainEditor", "PER");
		assertEquals(Action.SUCCESS, result);
		result = this.executeSend(SystemConstants.GUEST_USER_NAME, params);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors().keySet();
		assertEquals(1, fieldErrors.size());
		assertTrue(fieldErrors.contains("Monotext:eMail"));
		messagesId = this._messageManager.loadMessagesId(null);
		assertEquals(1, messagesId.size());
		
		result = this.executeNew("mainEditor", "PER");
		assertEquals(Action.SUCCESS, result);
		params.put("Monotext:eMail", "MyEmail@inesistente.itte");
		result = this.executeSend("mainEditor", params);
		assertEquals(Action.SUCCESS, result);
		
		EntitySearchFilter[] filters = new EntitySearchFilter[] { new EntitySearchFilter(IMessageSearcherDAO.USERNAME_FILTER_KEY, false, "mainEditor", false)};
		messagesId = this._messageManager.loadMessagesId(filters);
		assertEquals(1, messagesId.size());
		message = this._messageManager.getMessage(messagesId.get(0));
		assertEquals("mainEditor", message.getUsername());
	}
	
	private String executeNew(String username, String typeCode) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/User", "new");
		this.addParameter("typeCode", typeCode);
		return this.executeAction();
	}
	
	private String executeSend(String username, Map<String, String> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/User", "send");
		this.addParameters(params);
		return this.executeAction();
	}
	
	@Override
	protected void tearDown() throws Exception {
		this.activeMailManager(true);
		super.tearDown();
	}
	
	private void activeMailManager(boolean active) {
		IMailManager mailManager = (IMailManager) this.getService(JpmailSystemConstants.MAIL_MANAGER);
		if (mailManager instanceof MailManager) {
			((MailManager) mailManager).setActive(active);
		}
	}
	
}