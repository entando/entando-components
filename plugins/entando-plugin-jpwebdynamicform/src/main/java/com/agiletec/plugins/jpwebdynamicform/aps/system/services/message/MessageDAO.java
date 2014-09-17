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
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
	public void deleteEntity(String entityId) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteMessageAnswers(entityId, conn);
			this.deleteEntitySearchRecord(entityId, conn);
			stat = conn.prepareStatement(this.getDeleteEntityRecordQuery());
			stat.setString(1, entityId);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			processDaoException(t, "Errore on delete entity by id", "deleteEntity");
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
			this.deleteUserMessagesAnswers(username, conn);
			this.deleteUserMessagesSearchRecord(username, conn);
			stat = conn.prepareStatement(DELETE_USERMESSAGES);
			stat.setString(1, username);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error removing messages for user " + username, "deleteUserMessages");
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
			processDaoException(t, "Error adding message answer", "addAnswer");
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
			processDaoException(t, "Error reading answers to message " + messageId, "loadAnswers");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return answers;
	}

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
		"DELETE FROM jpwebdynamicform_search WHERE messageid IN ( SELECT messageid FROM jpwebdynamicform_messages WHERE username = ? ) ";
	
	private final String DELETE_USERMESSAGES_ANSWERS =
		"DELETE FROM jpwebdynamicform_answers WHERE messageid IN ( SELECT messageid FROM jpwebdynamicform_messages WHERE username = ? ) ";
	
	private final String ADD_ATTRIBUTE_ROLE_RECORD =
		"INSERT INTO jpwebdynamicform_attroles (messageid, attrname, rolename) VALUES ( ? , ? , ? )";
	
	private final String DELETE_ATTRIBUTE_ROLE_RECORD =
		"DELETE FROM jpwebdynamicform_attroles WHERE messageid = ? ";
	
}