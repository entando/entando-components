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
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpsurvey.aps.system.services.AbstractSurveyDAO;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;

/**
 * This DAO class allow to select individual 'question's of the survey
 * @author M.E. Minnai
 */
public class QuestionDAO extends AbstractSurveyDAO implements IQuestionDAO {

	private static final Logger _logger = LoggerFactory.getLogger(QuestionDAO.class);

	@Override
	public Question loadQuestion(int id) {
		Question question = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_COMPLETE_QUESTION_BY_ID);
			stat.setInt(1, id);
			res = stat.executeQuery();
			while (res.next()) {
				if (null == question) {
					question = this.buildQuestionRecordFromResultSet(res, 1);
				}
				// get extra info: questions need the survey type
				int questionnaireValue = res.getInt(13);
				boolean questionnaire = questionnaireValue == 1 ? true:false;
				ApsProperties titles = new ApsProperties();
				titles.loadFromXml(res.getString(14));
				question.setExtraInfo(questionnaire, titles);
				Choice choice = this.buildChoiceRecordFromResultSet(res, 8);
				if (null == choice) continue;
				choice.setExtraInfo(question.getSurveyId(), question.isQuestionnaire(), question.getSurveyTitles(), question.getQuestions());
				if (null == question.getChoice(choice.getId())) {
					question.getChoices().add(choice);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while loading the question of ID {}",  t);
			throw new RuntimeException("Error while loading a question", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return question;
	}

	@Override
	public List<Choice> getQuestionChoices(int id) {
		List<Choice> list = new ArrayList<Choice>();
		try {
			Question question = this.loadQuestion(id);
			if (null != question) {
				list = question.getChoices();
			}
		} catch (Throwable t) {
			_logger.error("Error while loading the choices belonging to the question of ID {}", id,  t);
			throw new RuntimeException("Error while loading the choices belonging a question", t);
		}
		return list;
	}
	
	@Override
	public void saveQuestion(Question question) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.saveQuestion(conn, question);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while saving the question",  t);
			throw new RuntimeException("Error while saving the question", t);
		} finally {
			this.refreshExtraInfo(question);
			closeConnection(conn);
		}
	}
	
	@Override
	public void deleteQuestion(int id) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteQuestion(conn, id);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while deleting the question {}", id,  t);
			throw new RuntimeException("Error while deleting the question", t);
		} finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public void updateQuestion(Question question) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateQuestion(conn, question);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating a 'question'",  t);
			throw new RuntimeException("Error while updating a 'question'", t);
		} finally {
			this.refreshExtraInfo(question);
			closeConnection(conn);
		}
	}
	
	@Override
	public void deleteQuestionBySurveyId(int id) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteQuestionBySurveyId(conn, id);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating a question in the database",  t);
			throw new RuntimeException("Error while updating a question in the database", t);
		} finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public void swapQuestionPosition(Question questionToSwap, List<Question> questions, boolean up) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Question nearQuestionToSwap = null;
		try {
			for (int i = 0; i < questions.size(); i++) {
				Question question = questions.get(i);
				if (question.getId() == questionToSwap.getId()) {
					if (up && i>0) {
						nearQuestionToSwap = questions.get(i-1);
					} else if (!up && i<(questions.size()-1)) {
						nearQuestionToSwap = questions.get(i+1);
					}
					break;
				}
			}
			if (null == nearQuestionToSwap) {
				return;
			}
			conn = this.getConnection();
			conn.setAutoCommit(false);
			int initPos = questionToSwap.getPos();
			questionToSwap.setPos(nearQuestionToSwap.getPos());
			nearQuestionToSwap.setPos(initPos);
			this.updateQuestionPosition(conn, nearQuestionToSwap);
			this.updateQuestionPosition(conn, questionToSwap);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore swapping position of two 'choice' objects",  t);
			throw new RuntimeException("Errore swapping position of two 'choice' objects", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
	}
	
	private void updateQuestionPosition(Connection conn, Question questionToMove) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(MOVE_QUESTION);
			stat.setInt(1, questionToMove.getPos());
			stat.setInt(2, questionToMove.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating the position of question",  t);
			throw new RuntimeException("Error while updating the position of question", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	@Override
	public void saveQuestionInSortedPosition(Question question) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(GET_QUESTION_GREATER_POS);
			stat.setInt(1, question.getSurveyId());
			res = stat.executeQuery();
			if (res.next()) {
				int lastPosition = res.getInt(1);
				question.setPos(++lastPosition);
			} else {
				question.setPos(0);
			}
			this.saveQuestion(conn, question);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while saving a question in a sorted position",  t);
			throw new RuntimeException("Error while saving a question in a sorted position", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
	}
	
	private static final String GET_COMPLETE_QUESTION_BY_ID =
		"SELECT " +
		// questions 1 - 7
			"jpsurvey_questions.id, surveyid, question, jpsurvey_questions.pos, singlechoice, minresponsenumber, maxresponsenumber, "+
		// choices 8 - 12
			"jpsurvey_choices.id, questionid, choice, jpsurvey_choices.pos, freetext, " +
		// extra info 13 - 14
			"jpsurvey.questionnaire, jpsurvey.title " +
		" FROM jpsurvey " +
			" LEFT JOIN jpsurvey_questions ON jpsurvey.id = jpsurvey_questions.surveyid " +
			" LEFT JOIN jpsurvey_choices ON jpsurvey_questions.id = jpsurvey_choices.questionid " +
		"WHERE jpsurvey_questions.id= ? ORDER BY jpsurvey_questions.pos, jpsurvey_choices.pos ";
	
	private static final String MOVE_QUESTION = 
		"UPDATE jpsurvey_questions SET pos = ? WHERE id = ? ";
	
	private static final String GET_QUESTION_GREATER_POS =
		"SELECT pos FROM jpsurvey_questions WHERE surveyid = ? ORDER BY pos DESC";

	
}
