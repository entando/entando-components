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

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.SingleQuestionResponse;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.Voter;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

public class ResponseManager extends AbstractService implements IResponseManager {

	private static final Logger _logger = LoggerFactory.getLogger(ResponseManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseManager#saveResponse(com.agiletec.plugins.jpsurvey.aps.system.services.collect.Response)
	 */
	public void submitResponse(SingleQuestionResponse response) throws ApsSystemException {
		try {
			this.getResponseDAO().submitResponse(response);
		} catch (Throwable t ) {
			_logger.error("Error while recording the response of a survey", t);
			throw new ApsSystemException("Error while recording the response of a survey", t);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseManager#aggregateResponseByIds(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public List<SingleQuestionResponse> aggregateResponseByIds(Integer voterId,
			Integer questionId, Integer choiceId, String freetext) throws ApsSystemException {
		List<SingleQuestionResponse> list = null;
		try {
			list = this.getResponseDAO().aggregateResponseByIds(voterId, questionId, choiceId, freetext);
		} catch (Throwable t) {
			_logger.error("Error while grouping responses", t);
			throw new ApsSystemException("Error while grouping responses", t);
		}
		return list;
	}
	
	@Override
	public Map<Integer, Integer> loadQuestionStatistics(Integer questionId) throws ApsSystemException {
		Map<Integer, Integer> list = null;
		try {
			list = this.getResponseDAO().loadQuestionStatistics(questionId);
		} catch (Throwable t) {
			_logger.error("Errore in caricamento statistiche question ", questionId, t);
			throw new ApsSystemException("Errore in caricamento statistiche question " + questionId, t);
		}
		return list;
	}
	
	public void deleteResponse(SingleQuestionResponse response) throws ApsSystemException {
		try {
			this.getResponseDAO().deleteResponse(response);
		} catch (Throwable t) {
			_logger.error("Error while deleting a result from the database", t);
			throw new ApsSystemException("Error while deleting a result from the database", t);
		}
	}
	
	public void deleteResponseByQuestionId(int id) throws ApsSystemException {
		try {
			this.getResponseDAO().deleteResponseByQuestionId(id);
		} catch (Throwable t) {
			_logger.error("Error while deleting responses by the question ID {}", id, t);
			throw new ApsSystemException("Error while deleting responses by the question ID "+id);
		}
	}
	
	public void deleteResponseByChoiceId(int id) throws ApsSystemException {
		try {
			this.getResponseDAO().deleteResponseByChoiceId(id);
		} catch (Throwable t) {
			_logger.error("Error while deleting responses by choice ID {}", id, t);
			throw new ApsSystemException("Error while deleting responses by choice ID "+id);
		}
	}
	
	public void deleteResponseBySurvey(Survey survey) throws ApsSystemException {
		try {
			if (null != survey && null != survey.getQuestions()) {
				for (Question question: survey.getQuestions()) {
					this.deleteResponseByQuestionId(question.getId());
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while deleting all the responses of the given survey", t);
			throw new ApsSystemException("Error while deleting all the responses of the given survey");
		}
	}
	
	@Override
	public void saveVoterResponse(VoterResponse voterResponse) throws ApsSystemException {
		Voter voter = voterResponse.getVoter();
		try {
			this.getVoterManager().saveVoter(voter);
			int voterId = voter.getId();
			voterResponse.setVoterOnResponses(voterId);
			this.getResponseDAO().submitResponses(voterResponse.getResponses());
		} catch (Throwable t) {
			if (voter.getId() > 0) {
				this.getVoterManager().deleteVoterById(voter.getId());
			}
			_logger.error("Error saving a vote", t);
			throw new ApsSystemException("Error saving a vote", t);
		}
	}
	
	protected IVoterManager getVoterManager() {
		return _voterManager;
	}
	public void setVoterManager(IVoterManager voterManager) {
		this._voterManager = voterManager;
	}
	
	protected IResponseDAO getResponseDAO() {
		return _responseDAO;
	}
	public void setResponseDAO(IResponseDAO responseDAO) {
		this._responseDAO = responseDAO;
	}
	
	private IVoterManager _voterManager;
	private IResponseDAO _responseDAO;

}
