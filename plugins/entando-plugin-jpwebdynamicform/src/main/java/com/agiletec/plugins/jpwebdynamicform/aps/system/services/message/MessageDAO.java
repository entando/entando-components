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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.AbstractEntityDAO;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.MessageRecordVO;

/**
 * Implementation of a Data Access Object of Message Object.
 * @author E.Mezzano
 */
public class MessageDAO extends AbstractEntityDAO implements IMessageDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(MessageDAO.class);
	
	@Override
	protected ApsEntityRecord createEntityRecord(ResultSet res) throws Throwable {
		MessageRecordVO messageRecord = new MessageRecordVO();
		messageRecord.setId(res.getString(1));
		messageRecord.setUsername(res.getString(2));
		messageRecord.setLangCode(res.getString(3));
		messageRecord.setTypeCode(res.getString(4));
		messageRecord.setCreationDate(res.getTimestamp(5));
		messageRecord.setXml(res.getString(6));
		return messageRecord;
	}

	@Override
	protected String getLoadEntityRecordQuery() {
		return LOAD_MESSAGE_VO;
	}

	@Override
	protected String getAddEntityRecordQuery() {
		return ADD_MESSAGE;
	}

	@Override
	protected void buildAddEntityStatement(IApsEntity entity, PreparedStatement stat) throws Throwable {
		Message message = (Message) entity;
		stat.setString(1, message.getId());
		stat.setString(2, message.getUsername());
		stat.setString(3, message.getLangCode());
		stat.setString(4, message.getTypeCode());
		stat.setTimestamp(5, new Timestamp(message.getCreationDate().getTime()));
		stat.setString(6, message.getXML());
	}

	/**
	 * Update function not supported
	 */
	@Override
	protected String getUpdateEntityRecordQuery() {
		return null;
	}
	/**
	 * Update function not supported
	 */
	@Override
	protected void buildUpdateEntityStatement(IApsEntity entity, PreparedStatement stat) throws Throwable {
		// nothing to do
	}

	@Override
	protected String getDeleteEntityRecordQuery() {
		return DELETE_MESSAGE;
	}
	
	@Override
	protected void executeDeleteEntity(String entityId, Connection conn) throws Throwable {
		super.executeQueryWithoutResultset(conn, DELETE_MESSAGE_ANSWERS, entityId);
		super.executeDeleteEntity(entityId, conn);
	}

	@Override
	public void deleteUserMessages(String username) throws ApsSystemException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			super.executeQueryWithoutResultset(conn, DELETE_USERMESSAGES_ANSWERS, username);
			super.executeQueryWithoutResultset(conn, DELETE_USERMESSAGES_SEARCH_RECORD, username);
			super.executeQueryWithoutResultset(conn, DELETE_USERMESSAGES_ROLES, username);
			super.executeQueryWithoutResultset(conn, DELETE_USERMESSAGES, username);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error removing messages for user {}", username,  t);
			throw new RuntimeException("Error removing messages for user " + username, t);
		} finally {
			this.closeConnection(conn);
		}
	}

	@Override
	public void addAnswer(Answer answer) throws ApsSystemException {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_MESSAGE_ANSWER);
			stat.setString(1, answer.getAnswerId());
			stat.setString(2, answer.getMessageId());
			stat.setString(3, answer.getOperator());
			stat.setTimestamp(4, new Timestamp(answer.getSendDate().getTime()));
			stat.setString(5, answer.getText());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error adding message answer",  t);
			throw new RuntimeException("Error adding message answer", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public List<Answer> loadAnswers(String messageId) throws ApsSystemException {
		List<Answer> answers = new ArrayList<Answer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_MESSAGE_ANSWERS);
			stat.setString(1, messageId);
			res = stat.executeQuery();
			while (res.next()) {
				Answer answer = new Answer();
				answer.setAnswerId(res.getString(1));
				answer.setMessageId(res.getString(2));
				answer.setOperator(res.getString(3));
				answer.setSendDate(res.getTimestamp(4));
				answer.setText(res.getString(5));
				answers.add(answer);
			}
		} catch (Throwable t) {
			_logger.error("Error reading answers to message {}", messageId, t);
			throw new RuntimeException("Error reading answers to message " + messageId, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return answers;
	}
	
	@Override
	protected String getAddingSearchRecordQuery() {
		return ADD_MESSAGE_SEARCH_RECORD;
	}

	@Override
	protected String getExtractingAllEntityIdQuery() {
		return LOAD_ALL_MESSAGES_ID;
	}

	@Override
	protected String getRemovingSearchRecordQuery() {
		return DELETE_MESSAGE_SEARCH_RECORD;
	}
	
	@Override
	protected String getAddingAttributeRoleRecordQuery() {
		return ADD_ATTRIBUTE_ROLE_RECORD;
	}
	
	@Override
	protected String getRemovingAttributeRoleRecordQuery() {
		return DELETE_ATTRIBUTE_ROLE_RECORD;
	}
	
	private final String ADD_MESSAGE =
		"INSERT INTO jpwebdynamicform_messages ( messageid, username, langcode, messagetype, creationdate, messagexml ) values ( ?, ?, ? , ? , ?, ? ) ";

	private final String ADD_MESSAGE_SEARCH_RECORD =
		"INSERT INTO jpwebdynamicform_search ( messageid, attrname, textvalue, datevalue, numvalue, langcode ) " +
		"VALUES ( ? , ? , ? , ? , ? , ? )";

	private final String ADD_MESSAGE_ANSWER =
		"INSERT INTO jpwebdynamicform_answers ( answerid, messageid, operator, senddate, text ) values ( ?, ? , ? , ?, ? ) ";

	private final String LOAD_ALL_MESSAGES_ID =
		"SELECT messageid FROM jpwebdynamicform_messages";
	
	private final String LOAD_MESSAGES_BY_USERNAME =
		LOAD_ALL_MESSAGES_ID + "  WHERE username = ?";

	private final String LOAD_MESSAGE_VO =
		"SELECT messageid, username, langcode, messagetype, creationdate, messagexml FROM jpwebdynamicform_messages WHERE messageid = ? ";

	private final String GET_MESSAGE_ANSWERS =
		"SELECT answerid, messageid, operator, senddate, text FROM jpwebdynamicform_answers WHERE messageid = ? ORDER BY senddate DESC ";

	private final String DELETE_MESSAGE =
		"DELETE FROM jpwebdynamicform_messages WHERE messageid = ? ";

	private final String DELETE_MESSAGE_SEARCH_RECORD =
		"DELETE FROM jpwebdynamicform_search WHERE messageid = ? ";

	private final String DELETE_MESSAGE_ANSWERS =
		"DELETE FROM jpwebdynamicform_answers WHERE messageid = ? ";

	private final String DELETE_USERMESSAGES =
		"DELETE FROM jpwebdynamicform_messages WHERE username = ? ";
	
	private final String DELETE_USERMESSAGES_SEARCH_RECORD =
		"DELETE FROM jpwebdynamicform_search WHERE messageid IN ( " + LOAD_MESSAGES_BY_USERNAME + " ) ";
	
	private final String DELETE_USERMESSAGES_ANSWERS =
		"DELETE FROM jpwebdynamicform_answers WHERE messageid IN ( " + LOAD_MESSAGES_BY_USERNAME + " ) ";
	
	private final String DELETE_USERMESSAGES_ROLES =
		"DELETE FROM jpwebdynamicform_attroles WHERE messageid IN ( " + LOAD_MESSAGES_BY_USERNAME + " ) ";
	
	private final String ADD_ATTRIBUTE_ROLE_RECORD =
		"INSERT INTO jpwebdynamicform_attroles (messageid, attrname, rolename) VALUES ( ? , ? , ? )";
	
	private final String DELETE_ATTRIBUTE_ROLE_RECORD =
		"DELETE FROM jpwebdynamicform_attroles WHERE messageid = ? ";
	
}