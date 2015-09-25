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
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpwebdynamicform.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpwebdynamicform.util.JpwebdynamicformTestHelper;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.SmallMessageType;

public class TestMessageManager extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.activeMailManager(false);
	}
	
	public void testCreateMessageType() {
		Message message = this._messageManager.createMessageType("PER");
		Collection<String> attributes = message.getAttributeMap().keySet();
		assertEquals(5, attributes.size());
		assertTrue(attributes.contains("Name"));
		assertTrue(attributes.contains("Surname"));
		assertTrue(attributes.contains("Address"));
		assertTrue(attributes.contains("eMail"));
		assertTrue(attributes.contains("Note"));
		
		message = this._messageManager.createMessageType("COM");
		attributes = message.getAttributeMap().keySet();
		assertEquals(4, attributes.size());
		assertTrue(attributes.contains("Company"));
		assertTrue(attributes.contains("Address"));
		assertTrue(attributes.contains("eMail"));
		assertTrue(attributes.contains("Note"));
		
		message = this._messageManager.createMessageType("ART");
		assertNull(message);
	}
	
	public void testGetSmallMessageTypes() {
		List<SmallMessageType> smallMessageTypes = this._messageManager.getSmallMessageTypes();
		assertEquals(2, smallMessageTypes.size());
		for (SmallMessageType smallMessageType : smallMessageTypes) {
			String typeCode = smallMessageType.getCode();
			assertTrue(typeCode.equals("PER") || typeCode.equals("COM"));
		}
	}
	
	public void testGetSmallMessageTypesMap() {
		Map<String, SmallMessageType> smallMessageTypes = this._messageManager.getSmallMessageTypesMap();
		assertEquals(2, smallMessageTypes.size());
		assertTrue(smallMessageTypes.containsKey("PER"));
		assertTrue(smallMessageTypes.containsKey("COM"));
	}
	
	public void testLoadMessagesId() throws Throwable {
		List<String> messageIds = this._messageManager.loadMessagesId(null);
		assertEquals(0, messageIds.size());
		
		Date currentDate = new Date();
		Message message1 = this._helper.createMessage(null, "admin", "it", currentDate, "MyName", "MySurname", "MyAddress", JpwebdynamicformTestHelper.EMAIL, "MyNotes");
		this._helper.addMessage(message1);
		messageIds = this._messageManager.loadMessagesId(null);
		assertEquals(1, messageIds.size());
		
		Message message2 = this._helper.createMessage(null, "admin", "it", currentDate, "MyCompany", "MyAddress", JpwebdynamicformTestHelper.EMAIL, "MyNotes");
		this._helper.addMessage(message2);
		messageIds = this._messageManager.loadMessagesId(null);
		assertEquals(2, messageIds.size());
		
		EntitySearchFilter filter = new EntitySearchFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "PER", true);
		messageIds = this._messageManager.loadMessagesId(new EntitySearchFilter[] { filter });
		assertEquals(1, messageIds.size());
		assertTrue(messageIds.contains(message1.getId()));
	}
	
	public void testAddGetMessage() throws Throwable {
		Date currentDate = new Date();
		Message message1 = this._helper.createMessage(null, "admin", "it", currentDate, "MyName", "MySurname", "MyAddress", JpwebdynamicformTestHelper.EMAIL, "MyNotes");
		this._messageManager.addMessage(message1);
		Message addedMessage = this._messageManager.getMessage(message1.getId());
		this.compareMessages(message1, addedMessage);
		
		// store = false
		Message message2 = this._helper.createMessage("id", "admin", "it", currentDate, "MyCompany", "MyAddress", JpwebdynamicformTestHelper.EMAIL, "MyNotes");
		this._messageManager.addMessage(message2);
		assertNull(this._messageManager.getMessage(message2.getId()));
		assertEquals(1, this._messageManager.loadMessagesId(null).size());
	}
	
	public void testDeleteMessage() throws Throwable {
		Message message = this._helper.createMessage(null, null, "it", new Date(), "company", "address", JpwebdynamicformTestHelper.EMAIL, "note");
		this._helper.addMessage(message);
		String messageId = message.getId();
		assertNotNull(this._messageManager.getMessage(messageId));
		
		Answer answer1 = this._helper.createAnswer(null, messageId, "admin", new Date(), "text1");
		this._helper.addAnswer(answer1);
		Answer answer2 = this._helper.createAnswer(null, messageId, "admin", new Date(), "text2");
		this._helper.addAnswer(answer2);
		List<Answer> answers = this._messageManager.getAnswers(messageId);
		assertEquals(2, answers.size());
		
		this._messageManager.deleteMessage(messageId);
		assertNull(this._messageManager.getMessage(messageId));
		answers = this._messageManager.getAnswers(messageId);
		assertEquals(0, answers.size());
	}
	
	public void testAddGetAnswer() throws Throwable {
		Message message = this._helper.createMessage(null, "admin", "it", new Date(), "company", "address", "indirizzoemail@dominioinesistente.it", "note");
		this._helper.addMessage(message);
		String messageId = message.getId();
		assertNotNull(this._messageManager.getMessage(messageId));
		
		Answer answer1 = this._helper.createAnswer("1", messageId, "mainEditor", new Date(), "text1");
		this._messageManager.sendAnswer(answer1);
		Answer answer2 = this._helper.createAnswer("2", messageId, "mainEditor", new Date(), "text2");
		this._messageManager.sendAnswer(answer2);
		
		List<Answer> answers = this._messageManager.getAnswers(messageId);
		assertEquals(2, answers.size());
		
		for (Answer answer : answers) {
			if (answer.getAnswerId().equals(answer1.getAnswerId())) {
				this.compareAnswer(answer1, answer);
			} else {
				this.compareAnswer(answer2, answer);
			}
		}
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