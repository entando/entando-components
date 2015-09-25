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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.SingleQuestionResponse;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

public interface IResponseManager {

	/**
	 * Save in the database a single response.
	 * @param response the object to save
	 * @throws ApsSystemException in case of error 
	 */
	public void submitResponse(SingleQuestionResponse response) throws ApsSystemException;

	/**
	 * Search among the responses those matching the given criteria
	 * @param voterId the id of the voter
	 * @param questionId the id of the question being answered
	 * @param choiceId the option chosen
	 * @param freetext the (optional) string submitted by the voter
	 * @return the list of object of type 'response'
	 * @throws ApsSystemException in case of error 
	 */
	public List<SingleQuestionResponse> aggregateResponseByIds(Integer voterId,
			Integer questionId, Integer choiceId, String freetext)
			throws ApsSystemException;
	
	public Map<Integer, Integer> loadQuestionStatistics(Integer questionId) throws ApsSystemException;
	
	/**
	 * Delete the given response object from the database (mostly for testing purposes)
	 * @param response the object to delete.
	 * @throws ApsSystemException in case of error 
	 */
	public void deleteResponse(SingleQuestionResponse response) throws ApsSystemException;
	
	
	public void saveVoterResponse(VoterResponse voterResponse) throws ApsSystemException;
	
	/**
	 * Delete all the record sharing the same question ID
	 * @param id The ID of the question whose answers must be deleted
	 * @throws ApsSystemException in case of error
	 */
	public void deleteResponseByQuestionId(int id) throws ApsSystemException;
	
	/**
	 * Delete all the record sharing the same choice ID
	 * @param id The ID of the choice whose answer must be deleted
	 * @throws ApsSystemException in case of error
	 */
	public void deleteResponseByChoiceId(int id) throws ApsSystemException;
	
	/**
	 * Delete all the responses given for the survey passed. NOTE: the survey itself it NOT affected
	 * by this operation.
	 * @param survey The survey whose responses must be deleted.
	 * @throws ApsSystemException in case of error
	 */
	public void deleteResponseBySurvey(Survey survey) throws ApsSystemException;
	
}