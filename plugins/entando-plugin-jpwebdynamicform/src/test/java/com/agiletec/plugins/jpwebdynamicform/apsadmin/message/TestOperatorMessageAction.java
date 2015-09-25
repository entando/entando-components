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
package com.agiletec.plugins.jpwebdynamicform.apsadmin.message;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpwebdynamicform.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpwebdynamicform.util.JpwebdynamicformTestHelper;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.common.AbstractMessageAction;
import com.opensymphony.xwork2.Action;

public class TestOperatorMessageAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.activeMailManager(false);
	}
	
	public void testView() throws Throwable {
		String result = this.executeView("admin", "PER", "1");
		assertEquals("messageNotFound", result);

		Message message = this._helper.createMessage(null, "mainEditor", "it", new Date(), "MyName", "MySurname", "MyAddress", JpwebdynamicformTestHelper.EMAIL, "MyNotes");
		this._helper.addMessage(message);
		String messageId = message.getId();
		Answer answer1 = this._helper.createAnswer("1", messageId, "mainEditor", new Date(), "text1");
		this._helper.addAnswer(answer1);
		Answer answer2 = this._helper.createAnswer("2", messageId, "mainEditor", new Date(), "text2");
		this._helper.addAnswer(answer2);
		result = this.executeView("admin", "PER", messageId);
		assertEquals(Action.SUCCESS, result);
		AbstractMessageAction action = (AbstractMessageAction) this.getAction();
		Message receivedMessage = action.getMessage();
		this.compareMessages(message, receivedMessage);
		List<Answer> answers = action.getAnswers();
		assertEquals(2, answers.size());

		for (Answer answer : answers) {
			if (answer.getAnswerId().equals(answer1.getAnswerId())) {
				this.compareAnswer(answer1, answer);
			} else {
				this.compareAnswer(answer2, answer);
			}
		}
	}

	public void testNewAnswer() throws Throwable {
		String result = this.executeNewAnswer("admin", "PER", "1");
		assertEquals("messageNotFound", result);

		Message message1 = this._helper.createMessage(null, "mainEditor", "it", new Date(), "MyName1", "MySurname1", "MyAddress1", "", "MyNotes1");
		this._helper.addMessage(message1);
		Message message2 = this._helper.createMessage(null, "mainEditor", "it", new Date(), "MyName2", "MySurname2", "MyAddress2", JpwebdynamicformTestHelper.EMAIL, "MyNotes2");
		this._helper.addMessage(message2);

		String messageId = message1.getId();
		result = this.executeNewAnswer("admin", "PER", messageId);
		assertEquals(Action.INPUT, result);
		Collection<String> actionErrors = this.getAction().getActionErrors();
		assertEquals(1, actionErrors.size());
		assertTrue(((String) actionErrors.toArray()[0]).contains(this.getAction().getText("Message.eMailAddress.notFound")));

		messageId = message2.getId();
		result = this.executeNewAnswer("admin", "PER", messageId);
		assertEquals(Action.SUCCESS, result);
	}

	public void testSendAnswerFailure() throws Throwable {
		Message message1 = this._helper.createMessage(null, "mainEditor", "it", new Date(), "MyName1", "MySurname1", "MyAddress1", "", "MyNotes1");
		this._helper.addMessage(message1);
		Message message2 = this._helper.createMessage(null, "mainEditor", "it", new Date(), "MyName2", "MySurname2", "MyAddress2", JpwebdynamicformTestHelper.EMAIL, "MyNotes2");
		this._helper.addMessage(message2);

		// Message without eMail address.
		String messageId = message1.getId();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", messageId);
		params.put("text", "Text of answer");
		String result = this.executeSendAnswer("admin", params);
		assertEquals("messageNotFound", result);

		// Incomplete fields
		messageId = message2.getId(); // Message with eMail address.
		params.put("id", messageId);
		params.put("text", "");
		result = this.executeSendAnswer("admin", params);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		List<String> currentFielderrors = fieldErrors.get("text");
		assertEquals(1, currentFielderrors.size());
		assertTrue(((String) currentFielderrors.get(0)).contains(this.getAction().getText("requiredstring")));
	}

	public void testSendAnswer() throws Throwable {
		Message message1 = this._helper.createMessage(null, "mainEditor", "it", new Date(), "MyName1", "MySurname1", "MyAddress1", "", "MyNotes1");
		this._helper.addMessage(message1);
		Message message2 = this._helper.createMessage(null, "mainEditor", "it", new Date(), "MyName2", "MySurname2", "MyAddress2", JpwebdynamicformTestHelper.EMAIL, "MyNotes2");
		this._helper.addMessage(message2);

		String messageId = message2.getId(); // Message with eMail address.
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", messageId);
		params.put("text", "Text of answer");
		String result = this.executeSendAnswer("admin", params);
		assertEquals(Action.SUCCESS, result);
		List<Answer> addedAnswers = this._messageManager.getAnswers(messageId);
		assertEquals(1, addedAnswers.size());
		Answer answer = addedAnswers.get(0);
		assertEquals(messageId, answer.getMessageId());
		assertEquals("admin", answer.getOperator());
		assertEquals("Text of answer", answer.getText());
		assertNotNull(answer.getAnswerId());
		assertNotNull(answer.getSendDate());
	}

	public void testTrash() throws Throwable {
		Message message = this._helper.createMessage(null, "mainEditor", "it", new Date(), "MyName", "MySurname", "MyAddress", JpwebdynamicformTestHelper.EMAIL, "MyNotes");
		this._helper.addMessage(message);
		String messageId = message.getId();
		assertNotNull(this._messageManager.getMessage(messageId));
		Answer answer1 = this._helper.createAnswer("1", messageId, "mainEditor", new Date(), "text1");
		this._helper.addAnswer(answer1);

		String result = this.executeTrash("admin", messageId+"unknow");
		assertEquals("messageNotFound", result);

		result = this.executeTrash("admin", messageId);
		assertEquals(Action.SUCCESS, result);
		assertNotNull(this._messageManager.getMessage(messageId));
	}

	public void testDelete() throws Throwable {
		Message message = this._helper.createMessage(null, "mainEditor", "it", new Date(), "MyName", "MySurname", "MyAddress", JpwebdynamicformTestHelper.EMAIL, "MyNotes");
		this._helper.addMessage(message);
		String messageId = message.getId();
		assertNotNull(this._messageManager.getMessage(messageId));
		Answer answer1 = this._helper.createAnswer("1", messageId, "mainEditor", new Date(), "text1");
		this._helper.addAnswer(answer1);

		String result = this.executeDelete("admin", messageId+"unknow");
		assertEquals("messageNotFound", result);

		result = this.executeDelete("admin", messageId);
		assertEquals(Action.SUCCESS, result);
		assertNull(this._messageManager.getMessage(messageId));
	}

	private void compareMessages(Message expected, Message received) {
		assertEquals(expected.getId(), received.getId());
		assertEquals(expected.getDescr(), received.getDescr());
		assertEquals(expected.getTypeCode(), received.getTypeCode());
		assertEquals(expected.getTypeDescr(), received.getTypeDescr());
		assertEquals(expected.getXML(), received.getXML());
		assertEquals(expected.getUsername(), received.getUsername());
		assertEquals(DateConverter.getFormattedDate(expected.getCreationDate(), "dd/MM/yyyy hh:mm:ss"), 
				DateConverter.getFormattedDate(received.getCreationDate(), "dd/MM/yyyy hh:mm:ss"));
	}

	private void compareAnswer(Answer expected, Answer received) {
		assertEquals(expected.getAnswerId(), received.getAnswerId());
		assertEquals(expected.getMessageId(), received.getMessageId());
		assertEquals(expected.getOperator(), received.getOperator());
		assertEquals(DateConverter.getFormattedDate(expected.getSendDate(), "dd/MM/yyyy hh:mm:ss"), 
				DateConverter.getFormattedDate(received.getSendDate(), "dd/MM/yyyy hh:mm:ss"));
		assertEquals(expected.getText(), received.getText());
	}

	private String executeView(String username, String typeCode, String id) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/Operator", "view");
		this.addParameter("typeCode", typeCode);
		this.addParameter("id", id);
		return this.executeAction();
	}

	private String executeNewAnswer(String username, String typeCode, String id) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/Operator", "newAnswer");
		this.addParameter("typeCode", typeCode);
		this.addParameter("id", id);
		return this.executeAction();
	}

	private String executeTrash(String username, String id) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/Operator", "trash");
		this.addParameter("id", id);
		return this.executeAction();
	}

	private String executeDelete(String username, String id) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/Operator", "delete");
		this.addParameter("id", id);
		return this.executeAction();
	}

	private String executeSendAnswer(String username, Map<String, String> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/Operator", "sendAnswer");
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