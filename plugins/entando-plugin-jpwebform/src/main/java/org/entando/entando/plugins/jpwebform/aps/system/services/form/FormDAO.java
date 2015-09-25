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
package org.entando.entando.plugins.jpwebform.aps.system.services.form;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Answer;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Message;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.MessageRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.AbstractEntityDAO;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Implementation of a Data Access Object of Form Message Object and versioned types.
 * @author E.Santoboni
 */
public class FormDAO extends AbstractEntityDAO implements IFormDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(FormDAO.class);

	@Override
	protected ApsEntityRecord createEntityRecord(ResultSet res) throws Throwable {
		MessageRecordVO messageRecord = new MessageRecordVO();
		messageRecord.setId(res.getString(1));
		messageRecord.setUsername(res.getString(2));
		messageRecord.setLangCode(res.getString(3));
		messageRecord.setTypeCode(res.getString(4));
		messageRecord.setCreationDate(res.getTimestamp(5));
		messageRecord.setSendDate(res.getTimestamp(6));
		messageRecord.setVersionType(res.getInt(7));
		messageRecord.setXml(res.getString(8));
		messageRecord.setCompleted(res.getBoolean(9));
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
		stat.setTimestamp(6, new Timestamp(message.getSendDate().getTime()));
		stat.setInt(7, message.getVersionType());
		stat.setString(8, message.getXML());
		if (message.isCompleted()) {
			stat.setInt(9, 1);
		} else {
			stat.setInt(9, 0);
		}
	}
	
	@Override
	protected String getUpdateEntityRecordQuery() {
		return UPDATE_MESSAGE;
	}
	
	@Override
	protected void buildUpdateEntityStatement(IApsEntity entity, PreparedStatement stat) throws Throwable {
		Message message = (Message) entity;
		stat.setString(1, message.getUsername());
		stat.setString(2, message.getLangCode());
		stat.setString(3, message.getTypeCode());
		stat.setTimestamp(4, new Timestamp(message.getSendDate().getTime()));
		stat.setInt(5, message.getVersionType());
		stat.setString(6, message.getXML());
		if (message.isCompleted()) {
			stat.setInt(7, 1);
		} else {
			stat.setInt(7, 0);
		}
		stat.setString(8, message.getId());
	}
	
	@Override
	protected String getDeleteEntityRecordQuery() {
		return DELETE_MESSAGE;
	}
	
	@Override
	public void deleteEntity(String entityId) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			//this.deleteMessageAnswers(entityId, conn);
			this.delete(entityId, DELETE_MESSAGE_ANSWERS, conn);
			this.deleteEntitySearchRecord(entityId, conn);
			stat = conn.prepareStatement(this.getDeleteEntityRecordQuery());
			stat.setString(1, entityId);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			_logger.error("Error on delete entity by id {}", entityId,  t);
			throw new RuntimeException("Error on delete entity by id", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void deleteUserMessages(String username) throws ApsSystemException {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.delete(username, DELETE_USERMESSAGES_ANSWERS, conn);
			//this.deleteUserMessagesAnswers(username, conn);
			this.delete(username, DELETE_USERMESSAGES_SEARCH_RECORD, conn);
			//this.deleteUserMessagesSearchRecord(username, conn);
			stat = conn.prepareStatement(DELETE_USERMESSAGES);
			stat.setString(1, username);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error removing messages for user {}", username,  t);
			throw new RuntimeException("Error removing messages for user " + username, t);
		} finally {
			closeDaoResources(null, stat, conn);
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
	/*
	private void deleteUserMessagesSearchRecord(String username, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_USERMESSAGES_SEARCH_RECORD);
			stat.setString(1, username);
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Error removing messages search records for user " + username, "deleteUserMessagesSearchRecord");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	private void deleteUserMessagesAnswers(String username, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_USERMESSAGES_ANSWERS);
			stat.setString(1, username);
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Error removing answers to messages of for user " + username, "deleteUserMessagesAnswers");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	private void deleteMessageAnswers(String messageId, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_MESSAGE_ANSWERS);
			stat.setString(1, messageId);
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Error removing answers to message " + messageId, "deleteMessageAnswers");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	*/
	private void delete(String key, String query, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			stat.setString(1, key);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error removing record '{}' - query '{}'", key, query, t);
			throw new RuntimeException("Error removing record '" + key + "' - query '" + query + "'", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	/*
	@Override
	public String loadTypeVersion(String typeCode, Integer versionId) {
		String xml = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_VERSION);
			stat.setString(1, typeCode);
			stat.setInt(2, versionId);
			res = stat.executeQuery();
			if (res.next()) {
				xml = res.getString(1);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error loading versioned type - "
					+ "typeCode '" + typeCode + "' version '" + versionId + "'", "loadTypeVersion");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return xml;
	}
	
	@Override
	public void saveTypeVersion(String typeCode, Integer versionId, String modelXml, String stepXml) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			//typecode, versionid, versionedxml
			stat = conn.prepareStatement(ADD_VERSION);
			stat.setString(1, typeCode);
			stat.setInt(2, versionId);
			stat.setString(3, modelXml);
			stat.setString(4, stepXml);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			this.processDaoException(t, "Error while saving Type Version", "saveTypeVersion");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	*/
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
		"INSERT INTO jpwebform_messages (messageid, username, langcode, messagetype, creationdate, senddate, versiontype, messagexml, done ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
	
	private final String UPDATE_MESSAGE =
		"UPDATE jpwebform_messages SET username = ? , langcode = ? , " +
		" messagetype = ? , senddate = ? , versiontype = ? , messagexml = ? , done = ? WHERE messageid = ?";
	
	private final String ADD_MESSAGE_SEARCH_RECORD =
		"INSERT INTO jpwebform_messagesearch ( messageid, attrname, textvalue, datevalue, numvalue, langcode ) " +
		"VALUES ( ? , ? , ? , ? , ? , ? )";

	private final String ADD_MESSAGE_ANSWER =
		"INSERT INTO jpwebform_messageanswers ( answerid, messageid, operator, senddate, text ) values ( ?, ? , ? , ?, ? ) ";
	
	private final String LOAD_ALL_MESSAGES_ID =
		"SELECT messageid FROM jpwebform_messages";
	
	private final String LOAD_MESSAGE_VO =
		"SELECT messageid, username, langcode, messagetype, creationdate, senddate, versiontype, messagexml, done FROM jpwebform_messages WHERE messageid = ? ";
	
	private final String GET_MESSAGE_ANSWERS =
		"SELECT answerid, messageid, operator, senddate, text FROM jpwebform_messageanswers WHERE messageid = ? ORDER BY senddate DESC ";

	private final String DELETE_MESSAGE =
		"DELETE FROM jpwebform_messages WHERE messageid = ? ";

	private final String DELETE_MESSAGE_SEARCH_RECORD =
		"DELETE FROM jpwebform_messagesearch WHERE messageid = ? ";

	private final String DELETE_MESSAGE_ANSWERS =
		"DELETE FROM jpwebform_messageanswers WHERE messageid = ? ";

	private final String DELETE_USERMESSAGES =
		"DELETE FROM jpwebform_messages WHERE username = ? ";

	private final String DELETE_USERMESSAGES_SEARCH_RECORD =
		"DELETE FROM jpwebform_messagesearch WHERE messageid IN ( SELECT messageid FROM jpwebform_messages WHERE username = ? ) ";

	private final String DELETE_USERMESSAGES_ANSWERS =
		"DELETE FROM jpwebform_messageanswers WHERE messageid IN ( SELECT messageid FROM jpwebform_messages WHERE username = ? ) ";
	
	private final String ADD_ATTRIBUTE_ROLE_RECORD =
		"INSERT INTO jpwebform_attributeroles (messageid, attrname, rolename) VALUES ( ? , ? , ? )";
	
	private final String DELETE_ATTRIBUTE_ROLE_RECORD =
		"DELETE FROM jpwebform_attributeroles WHERE messageid = ? ";
	
}
