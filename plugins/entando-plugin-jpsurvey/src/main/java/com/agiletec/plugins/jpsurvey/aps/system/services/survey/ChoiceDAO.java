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
package com.agiletec.plugins.jpsurvey.aps.system.services.survey;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpsurvey.aps.system.services.AbstractSurveyDAO;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;

public class ChoiceDAO extends AbstractSurveyDAO implements IChoiceDAO {

	private static final Logger _logger = LoggerFactory.getLogger(ChoiceDAO.class);

	@Override
	public Choice loadChoice(int id) {
		Choice choice = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_CHOICE_BY_ID);
			stat.setInt(1, id);
			res = stat.executeQuery();
			if (res.next()) {
				choice = this.buildChoiceRecordFromResultSet(res, 1);
				// process get extra info
				int surveyId = res.getInt(6);
				boolean questionnaire = res.getInt(7) == 1 ? true:false;
				ApsProperties surveyTitle = new ApsProperties();
				surveyTitle.loadFromXml(res.getString(8));
				ApsProperties questionTitle = new ApsProperties();
				questionTitle.loadFromXml(res.getString(9));
				choice.setExtraInfo(surveyId, questionnaire, surveyTitle, questionTitle);
			}
		} catch (Throwable t) {
			_logger.error("Error while loading the choice of ID {}", id,  t);
			throw new RuntimeException("Error while loading the choice of ID", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return choice;
	}
	
	@Override
	public void saveChoice(Choice choice) {
		Connection conn = null;
		if (null == choice) return;
		try {
			conn = this.getConnection();
			saveChoice(conn, choice);
		} catch (Throwable t) {
			_logger.error("Error while saving the 'choice' ID {}", choice.getId(),  t);
			throw new RuntimeException("Error while saving the 'choice'", t);
		} finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public void saveChoiceInSortedPosition(Choice choice) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(GET_CHOICE_GREATER_POS);
			stat.setInt(1, choice.getQuestionId());
			res = stat.executeQuery();
			if (res.next()) {
				int lastPosition = res.getInt(1);
				choice.setPos(++lastPosition);
			} else {
				choice.setPos(0);
			}
			this.saveChoice(conn, choice);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while saving the choice ID  {} in the proper position", choice.getId(), choice.getPos(), t);
			throw new RuntimeException("Error while saving", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
	}
	
	@Override
	public void deleteChoice(int id) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			this.deleteChoice(conn,id);
		} catch (Throwable t) {
			_logger.error("Error while deleting the 'choice' ID {}", id,  t);
			throw new RuntimeException("Error while deleting a 'choice'", t);
		} finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public void deleteChoiceByQuestionId(int id) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			this.deleteChoiceByQuestionId(conn, id);
		} catch (Throwable t) {
			_logger.error("Error while deleting choices by question ID",  t);
			throw new RuntimeException("Error while deleting choices by question ID", t);
		} finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public void updateChoice(Choice choice) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			this.updateChoice(conn, choice);
		} catch (Throwable t) {
			_logger.error("Error while updating a 'choice'",  t);
			throw new RuntimeException("Error while updating a 'choice'", t);
		} finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public void swapChoicePosition(Choice choiceToSwap, List<Choice> choices, boolean up) {
		Connection conn = null;
		Choice nearChoiceToSwap = null;
		try {
			for (int i = 0; i < choices.size(); i++) {
				Choice choice = choices.get(i);
				if (choice.getId() == choiceToSwap.getId()) {
					if (up && i>0) {
						nearChoiceToSwap = choices.get(i-1);
					} else if (!up && i<(choices.size()-1)) {
						nearChoiceToSwap = choices.get(i+1);
					}
					break;
				}
			}
			if (null == nearChoiceToSwap) {
				return;
			}
			conn = this.getConnection();
			conn.setAutoCommit(false);
			int initPos = choiceToSwap.getPos();
			choiceToSwap.setPos(nearChoiceToSwap.getPos());
			nearChoiceToSwap.setPos(initPos);
			this.updateChoicePosition(conn, nearChoiceToSwap);
			this.updateChoicePosition(conn, choiceToSwap);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore swapping position of two 'choice' objects",  t);
			throw new RuntimeException("Errore swapping position of two 'choice' objects", t);
		} finally {
			closeConnection(conn);
		}
	}
	
	private void updateChoicePosition(Connection conn, Choice choiceToMove) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(MOVE_CHOICE);
			stat.setInt(1, choiceToMove.getPos());
			stat.setInt(2, choiceToMove.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating the position of question",  t);
			throw new RuntimeException("Error while updating the position of question", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	private static final String GET_CHOICE_BY_ID = // THE SMART WAY
		"SELECT " +
		// choices 1 - 5
			"jpsurvey_choices.id, questionid, choice, jpsurvey_choices.pos, freetext, " +
		// extra info 6 - 9
			"jpsurvey.id, jpsurvey.questionnaire, jpsurvey.title, jpsurvey_questions.question "+
		"FROM jpsurvey " +
			"LEFT JOIN jpsurvey_questions ON jpsurvey.id = jpsurvey_questions.surveyid "+
			"LEFT JOIN jpsurvey_choices ON jpsurvey_questions.id = jpsurvey_choices.questionid "+
		"WHERE jpsurvey_choices.id = ? ";
	
	private static final String GET_CHOICE_GREATER_POS =
		"SELECT pos FROM jpsurvey_choices WHERE questionid = ? ORDER BY pos DESC";
	
	private static final String GET_CHOICE_LESSER_THAN =
		"SELECT id, questionid, choice, pos, freetext FROM jpsurvey_choices WHERE questionid = ? AND pos < ? ORDER BY pos DESC";

	private static final String GET_CHOICE_GREATER_THAN =
		"SELECT id, questionid, choice, pos, freetext FROM jpsurvey_choices WHERE questionid = ? AND pos > ? ORDER BY pos ASC";
	
	private static final String MOVE_CHOICE = 
		"UPDATE jpsurvey_choices SET pos = ? WHERE id = ? ";
	
}
