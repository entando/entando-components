/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpsurvey.aps.system.services.collect;

import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.SingleQuestionResponse;

public interface IResponseDAO {

	/**
	 * Save in the database a single response 'object'
	 * @param response the object to save
	 */
	public void submitResponse(SingleQuestionResponse response);
	
	public void submitResponses(List<SingleQuestionResponse> responses);

	/**
	 * Search among the responses those matching the given critera
	 * @param voterId the id of the voter
	 * @param questionId the id of the question being answered
	 * @param choiceId the option choosen
	 * @param freetext the (optional) string submitted by the voter
	 * @return the list of object of type 'response'
	 */
	public List<SingleQuestionResponse> aggregateResponseByIds(Integer voterId,
			Integer questionId, Integer choiceId, String freetext);
	
	public Map<Integer, Integer> loadQuestionStatistics(Integer questionId);
	
	/**
	 * Delete the given response object from the database (mostly for testing purposes)
	 * @param response the object to delete.
	 */
	public void deleteResponse(SingleQuestionResponse response);
	
	/**
	 * Delete all those record sharing the same question ID
	 * @param questionId the question ID of the records to delete
	 */
	public void deleteResponseByQuestionId(int questionId);
	
	/**
	 * Delete all those record sharing the same choice ID
	 * @param choiceId the choice ID of the record to delete 
	 */
	public void deleteResponseByChoiceId(int choiceId);
	
}