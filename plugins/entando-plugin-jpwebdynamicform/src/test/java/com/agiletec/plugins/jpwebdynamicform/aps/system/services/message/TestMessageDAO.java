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

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.agiletec.plugins.jpwebdynamicform.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageDAO;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.MessageDAO;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.MessageRecordVO;

public class TestMessageDAO extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testAddLoadMessage() throws Throwable {
		Message message = this._messageManager.createMessageType("PER");
		message.setId("TEST");
		message.setCreationDate(new Date());
		message.setUsername("admin");
		message.setLangCode("it");
		this._messageDao.addEntity(message);
		MessageRecordVO messageRecord = (MessageRecordVO) this._messageDao.loadEntityRecord(message.getId());
		assertEquals(message.getId(), messageRecord.getId());
		assertEquals(message.getUsername(), messageRecord.getUsername());
		assertEquals(message.getTypeCode(), messageRecord.getTypeCode());
		assertEquals(DateConverter.getFormattedDate(message.getCreationDate(), "dd/MM/yyyy hh:mm:ss"), 
				DateConverter.getFormattedDate(messageRecord.getCreationDate(), "dd/MM/yyyy hh:mm:ss"));
		assertEquals(message.getXML(), messageRecord.getXml());
	}
	
	public void testDeleteMessage() throws Throwable {
		Message message = this._helper.createMessage(null, null, "it", new Date(), "company", "address", "indirizzoemail@dominioinesistente.it", "note");
		this._helper.addMessage(message);
		String messageId = message.getId();
		assertNotNull(this._messageDao.loadEntityRecord(messageId));
		
		Answer answer1 = this._helper.createAnswer(null, messageId, "admin", new Date(), "text1");
		this._helper.addAnswer(answer1);
		Answer answer2 = this._helper.createAnswer(null, messageId, "admin", new Date(), "text2");
		this._helper.addAnswer(answer2);
		List<Answer> answers = this._messageDao.loadAnswers(messageId);
		assertEquals(2, answers.size());
		
		this._messageDao.deleteEntity(messageId);
		assertNull(this._messageDao.loadEntityRecord(messageId));
		answers = this._messageDao.loadAnswers(messageId);
		assertEquals(0, answers.size());
	}
	
	public void testDeleteUserMessages() throws Throwable {
		Message message1 = this._helper.createMessage(null, "admin", "it", new Date(), "company1", "address1", "email1@miodominio.it", "note1");
		this._helper.addMessage(message1);
		String messageId1 = message1.getId();
		assertNotNull(this._messageDao.loadEntityRecord(messageId1));
		Answer answer1 = this._helper.createAnswer(null, messageId1, "admin", new Date(), "text1");
		this._helper.addAnswer(answer1);
		Answer answer2 = this._helper.createAnswer(null, messageId1, "admin", new Date(), "text2");
		this._helper.addAnswer(answer2);
		List<Answer> answers = this._messageDao.loadAnswers(messageId1);
		assertEquals(2, answers.size());
		
		Message message2 = this._helper.createMessage(null, "mainEditor", "it", new Date(), "company2", "address2", "email2@miodominio.it", "note2");
		this._helper.addMessage(message2);
		String messageId2 = message2.getId();
		assertNotNull(this._messageDao.loadEntityRecord(messageId2));
		Answer answer3 = this._helper.createAnswer(null, messageId2, "admin", new Date(), "text3");
		this._helper.addAnswer(answer3);
		answers = this._messageDao.loadAnswers(messageId2);
		assertEquals(1, answers.size());
		
		this._messageDao.deleteUserMessages("admin");
		assertNull(this._messageDao.loadEntityRecord(messageId1));
		answers = this._messageDao.loadAnswers(messageId1);
		assertEquals(0, answers.size());
		
		this._messageDao.deleteUserMessages("mainEditor");
		assertNull(this._messageDao.loadEntityRecord(messageId2));
		answers = this._messageDao.loadAnswers(messageId2);
		assertEquals(0, answers.size());
	}
	
	public void testAddloadAnswer() throws Throwable {
		Message message = this._helper.createMessage(null, "admin", "it", new Date(), "company", "address", "indirizzoemail@dominioinesistente.it", "note");
		this._helper.addMessage(message);
		String messageId = message.getId();
		assertNotNull(this._messageDao.loadEntityRecord(messageId));

		Answer answer1 = this._helper.createAnswer("1", messageId, "mainEditor", new Date(), "text1");
		this._messageDao.addAnswer(answer1);
		Answer answer2 = this._helper.createAnswer("2", messageId, "mainEditor", new Date(), "text2");
		this._messageDao.addAnswer(answer2);
		
		List<Answer> answers = this._messageDao.loadAnswers(messageId);
		assertEquals(2, answers.size());
		
		for (Answer answer : answers) {
			if (answer.getAnswerId().equals(answer1.getAnswerId())) {
				this.compareAnswer(answer1, answer);
			} else {
				this.compareAnswer(answer2, answer);
			}
		}
	}
	
	private void compareAnswer(Answer expected, Answer received) {
		assertEquals(expected.getAnswerId(), received.getAnswerId());
		assertEquals(expected.getMessageId(), received.getMessageId());
		assertEquals(expected.getOperator(), received.getOperator());
		assertEquals(DateConverter.getFormattedDate(expected.getSendDate(), "dd/MM/yyyy hh:mm:ss"), 
				DateConverter.getFormattedDate(received.getSendDate(), "dd/MM/yyyy hh:mm:ss"));
		assertEquals(expected.getText(), received.getText());
	}
	
    private void init() throws Exception {
		try {
			MessageDAO messageDAO = new MessageDAO();
			messageDAO.setDataSource((DataSource) this.getApplicationContext().getBean("servDataSource"));
			messageDAO.setLangManager((ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER));
			this._messageDao = messageDAO;
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
    
    private IMessageDAO _messageDao;
    
}