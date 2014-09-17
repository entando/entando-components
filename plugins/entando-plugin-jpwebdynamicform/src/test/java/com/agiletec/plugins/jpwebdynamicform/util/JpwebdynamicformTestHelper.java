/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebdynamicform.util;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;

import javax.sql.DataSource;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageDAO;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.MessageDAO;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;

public class JpwebdynamicformTestHelper extends AbstractDAO {

	public JpwebdynamicformTestHelper(DataSource dataSource, ILangManager langManager, IMessageManager messageManager) {
		MessageDAO messageDAO = new MessageDAO();
		messageDAO.setDataSource(dataSource);
		messageDAO.setLangManager(langManager);
		this._messageDAO = messageDAO;
		this.setDataSource(dataSource);
		this._messageManager = messageManager;
	}

	public void addMessage(Message message) throws Throwable {
		if (null==message.getId()) {
			message.setId(String.valueOf(_currentId++));
		}
		this._messageDAO.addEntity(message);
	}

	public void addAnswer(Answer answer) throws Throwable {
		if (null==answer.getAnswerId()) {
			answer.setAnswerId(String.valueOf(_currentId++));
		}
		this._messageDAO.addAnswer(answer);
	}

	public void cleanMessages() {
		Connection conn = null;
		try {
			conn = this.getConnection();
			this.executeQuery(DELETE_MESSAGE_ANSWERS, conn);
			this.executeQuery(DELETE_MESSAGE_SEARCH_RECORDS, conn);
			this.executeQuery(DELETE_MESSAGES, conn);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			closeConnection(conn);
		}
	}

	private void executeQuery(String query, Connection conn) {
		Statement stat = null;
		try {
			stat = conn.createStatement();
			stat.executeUpdate(query);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			closeDaoResources(null, stat);
		}
	}

    public Message createMessage(String id, String username, String langCode, Date creationDate, String name, String surname, String address, String email, String note) {
		Message message = this._messageManager.createMessageType("PER");
		message.setId(id);
		message.setUsername(username);
		message.setCreationDate(creationDate);
		message.setLangCode(langCode);
		((ITextAttribute) message.getAttribute("Name")).setText(name, null);
		((ITextAttribute) message.getAttribute("Surname")).setText(surname, null);
		((ITextAttribute) message.getAttribute("Address")).setText(address, null);
		((ITextAttribute) message.getAttribute("eMail")).setText(email, null);
		((ITextAttribute) message.getAttribute("Note")).setText(note, null);
		return message;
    }

    public Message createMessage(String id, String username, String langCode, Date creationDate, String company, String address, String email, String note) {
    	Message message = this._messageManager.createMessageType("COM");
		message.setId(id);
		message.setUsername(username);
		message.setCreationDate(creationDate);
		message.setLangCode(langCode);
		((ITextAttribute) message.getAttribute("Company")).setText(company, null);
		((ITextAttribute) message.getAttribute("Address")).setText(address, null);
		((ITextAttribute) message.getAttribute("eMail")).setText(email, null);
		((ITextAttribute) message.getAttribute("Note")).setText(note, null);
		return message;
    }

    public Answer createAnswer(String answerId, String messageId, String operator, Date sendDate, String text) {
    	Answer answer = new Answer();
    	answer.setAnswerId(answerId);
    	answer.setMessageId(messageId);
    	answer.setOperator(operator);
    	answer.setSendDate(sendDate);
    	answer.setText(text);
		return answer;
    }

    private IMessageManager _messageManager;
    private IMessageDAO _messageDAO;

    private int _currentId = 1;

	private static final String DELETE_MESSAGES =
		"DELETE FROM jpwebdynamicform_messages";

	private static final String DELETE_MESSAGE_SEARCH_RECORDS =
		"DELETE FROM jpwebdynamicform_search";

	private static final String DELETE_MESSAGE_ANSWERS =
		"DELETE FROM jpwebdynamicform_answers";

	public static final String EMAIL = "indirizzo@email.inesistente";

}