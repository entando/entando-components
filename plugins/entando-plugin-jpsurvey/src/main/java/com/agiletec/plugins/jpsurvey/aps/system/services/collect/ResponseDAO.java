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
package com.agiletec.plugins.jpsurvey.aps.system.services.collect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.plugins.jpsurvey.aps.system.services.AbstractSurveyDAO;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.SingleQuestionResponse;

public class ResponseDAO extends AbstractSurveyDAO implements IResponseDAO {

	private static final Logger _logger = LoggerFactory.getLogger(ResponseDAO.class);
	
	@Override
	public void submitResponses(List<SingleQuestionResponse> responses) {
		Connection conn = null;
		PreparedStatement stat= null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(SAVE_RESPONSE);
			for (int i=0; i<responses.size(); i++) {
				SingleQuestionResponse result = responses.get(i);
				stat.setInt(1, result.getVoterId()); // 1
				stat.setInt(2, result.getQuestionId()); // 2
				stat.setInt(3, result.getChoiceId()); // 3
				if (null!=result.getFreeText()) {
					stat.setString(4, result.getFreeText()); // 4
				} else {
					stat.setNull(4, java.sql.Types.VARCHAR); // 4
				}
				stat.addBatch();
				stat.clearParameters();
			}			
			stat.executeBatch();
			conn.commit();
		} catch (Throwable t) {
			_logger.error("Error saving answers",  t);
			throw new RuntimeException("Error saving answers", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public Map<Integer, Integer> loadQuestionStatistics(Integer questionId) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_QUESTION_STATS);
			stat.setInt(1, questionId);
			res = stat.executeQuery();
			while (res.next()) {
				int choiceId = res.getInt(1);
				int occurrences = res.getInt(2);
				map.put(choiceId, occurrences);
			}
		} catch (Throwable t) {
			_logger.error("error loadQuestionStatistics id by question {}", questionId,  t);
			throw new RuntimeException("error loadQuestionStatistics id by question", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return map;
	}
	
	private static final String LOAD_QUESTION_STATS = 
		"SELECT jpsurvey_responses.choiceid, COUNT(jpsurvey_responses.choiceid) " +
		"FROM jpsurvey_responses WHERE jpsurvey_responses.questionid = ? GROUP BY jpsurvey_responses.choiceid";
	
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseDAO#saveResponse(com.agiletec.plugins.jpsurvey.aps.system.services.collect.Response)
	 */
	public void submitResponse(SingleQuestionResponse result) {
		Connection conn = null;
		PreparedStatement stat=null;
		try {
			conn = this.getConnection();
			stat=conn.prepareStatement(SAVE_RESPONSE);
			stat.setInt(1, result.getVoterId()); // 1
			stat.setInt(2, result.getQuestionId()); // 2
			stat.setInt(3, result.getChoiceId()); // 3
			if (null!=result.getFreeText()) {
				stat.setString(4, result.getFreeText()); // 4
			}
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error saving vote", t);
			throw new RuntimeException("Error saving vote", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseDAO#aggregateResponseByIds(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public List<SingleQuestionResponse> aggregateResponseByIds(Integer voterId, Integer questionId, Integer choiceId, String freetext) {
		List<SingleQuestionResponse> list = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		int idx = 1;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(prepareAggregationStatment(voterId,questionId,choiceId,freetext));
			if (null!=voterId) {
				stat.setInt(idx++, voterId);
			}
			if (null!=questionId) {
				stat.setInt(idx++, questionId);
			}
			if (null!=choiceId) {
				stat.setInt(idx++, choiceId);
			}
			if (null!=freetext) {
				stat.setString(idx++, "%"+freetext+"%");
			}
			res = stat.executeQuery();
			list = createListFromResultSet(res);
		} catch (Throwable t) {
			_logger.error("Errore searching responseId", t);
			throw new RuntimeException("Errore searching responseId", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return list;
	}

	private String prepareAggregationStatment(Integer voterId,
			Integer questionId, Integer choiceId, String freetext) {
		StringBuffer query=new StringBuffer(AGGREGATE_RESPONSE);
		boolean isWhereInserted=false;
		if (null!=voterId) {
			if (!isWhereInserted) {
				query.append(" WHERE ");
				isWhereInserted=true;
			}
			query.append(" voterid = ? ");
		}
		if (null!=questionId) {
			if (!isWhereInserted) {
				query.append(" WHERE ");
				isWhereInserted=true;
			} else {
				query.append(" AND ");
			}
			query.append(" questionid = ? ");
		}
		if (null!=choiceId) {
			if (!isWhereInserted) {
				query.append(" WHERE ");
				isWhereInserted=true;
			} else {
				query.append(" AND ");
			}
			query.append(" choiceid = ? ");
		}
		if (null!=freetext) {
			if (!isWhereInserted) {
				query.append(" WHERE ");
				isWhereInserted=true;
			} else {
				query.append(" AND ");
			}
			query.append(" LOWER(freetext ) LIKE LOWER(?) ");
		}
		query.append("ORDER BY voterid ");
		return query.toString();
	}
	
	private List<SingleQuestionResponse> createListFromResultSet(ResultSet res) {
		List<SingleQuestionResponse> list=new ArrayList<SingleQuestionResponse>();
		try {
			while (res.next()) {
				SingleQuestionResponse response=this.createRecordFromResultSet(res);
				list.add(response);
			}
		}
		catch (Throwable t) {
			_logger.error("Error building the vote result list",  t);
			throw new RuntimeException("Error building the vote result list", t);
		}
		if (list.isEmpty()) return null;
		return list;
	}
	
	private SingleQuestionResponse createRecordFromResultSet(ResultSet res) {
		SingleQuestionResponse response = new SingleQuestionResponse();
		try {
			response.setVoterId(res.getInt(1)); // 1
			response.setQuestionId(res.getInt(2)); // 2
			response.setChoiceId(res.getInt(3)); // 3
			response.setFreeText(res.getString(4)); // 4
		} catch (Throwable t) {
			_logger.error("Error creating the 'response' onject form resulset",  t);
			throw new RuntimeException("Error creating the 'response' onject form resulset", t);
			//this.processDaoException(t, "Error creating the 'response' onject form resulset", "createRecordFromResultSet");
		}
		return response;
	}

	public void deleteResponse(SingleQuestionResponse response) {
		Connection conn=null;
		PreparedStatement stat = null;
		try {			
			conn=this.getConnection();
			if (null!=response.getFreeText() && response.getFreeText().length()>0) {
				stat=conn.prepareStatement(DELETE_RESPONSE_FREETEXT);
			} else {
				stat=conn.prepareStatement(DELETE_RESPONSE_NO_FREETEXT);
			}
			stat.setInt(1, response.getVoterId()); // 1
			stat.setInt(2, response.getQuestionId()); // 2
			stat.setInt(3, response.getChoiceId()); // 3
			if (null!=response.getFreeText() && response.getFreeText().length()>0) {
				stat.setString(4, response.getFreeText()); // 4
			}
			stat.execute();
		} catch (Throwable t) {
			_logger.error("Error deleting the object response",  t);
			throw new RuntimeException("Error deleting the object response", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	public void deleteResponseByQuestionId(int questionId) {
		Connection conn=null;
		PreparedStatement stat = null;
		try {
			conn=this.getConnection();
			stat=conn.prepareStatement(DELETE_RESPONSE_BY_QUESTION_ID);
			stat.setInt(1, questionId); // 1
			stat.execute();
		} catch (Throwable t) {
			_logger.error("Error deleting responses by question ID {}",questionId, t);
			throw new RuntimeException("Error deleting responses by question ID", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	public void deleteResponseByChoiceId(int choiceId) {
		Connection conn=null;
		PreparedStatement stat = null;
		try {
			conn=this.getConnection();
			stat=conn.prepareStatement(DELETE_RESPONSE_BY_CHOICE_ID);
			stat.setInt(1, choiceId); // 1
			stat.execute();
		} catch (Throwable t) {
			_logger.error("Error deleting responses by choice ID {}", choiceId,  t);
			throw new RuntimeException("Error deleting responses by choice ID", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	final String SAVE_RESPONSE =
		"INSERT INTO jpsurvey_responses (voterid,questionid,choiceid,freetext) VALUES (?,?,?,?)";
	
	final String AGGREGATE_RESPONSE =
		"SELECT voterid,questionid,choiceid,freetext FROM jpsurvey_responses ";
	
	final String DELETE_RESPONSE_FREETEXT =
		"DELETE FROM jpsurvey_responses WHERE voterid = ? AND questionid = ? AND choiceid = ? AND freetext = ? ";
	
	final String DELETE_RESPONSE_NO_FREETEXT =
		"DELETE FROM jpsurvey_responses WHERE voterid = ? AND questionid = ? AND choiceid = ? AND freetext IS NULL ";
	
	final String DELETE_RESPONSE_BY_QUESTION_ID = "DELETE FROM jpsurvey_responses WHERE questionid = ? ";
	
	final String DELETE_RESPONSE_BY_CHOICE_ID = "DELETE FROM jpsurvey_responses WHERE choiceid = ? ";
}
