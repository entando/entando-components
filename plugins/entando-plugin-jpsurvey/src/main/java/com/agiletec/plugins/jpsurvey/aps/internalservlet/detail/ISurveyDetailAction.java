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
package com.agiletec.plugins.jpsurvey.aps.internalservlet.detail;

import java.util.Map;

import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

/**
 * @author E.Santoboni
 */
public interface ISurveyDetailAction {
	
	/**
	 * Load an entire survey object with all the related questions and choices
	 * @return
	 */
	public Survey getSurvey();
	
	/**
	 * Get the number of the voters which answered to given survey
	 * @param surveyId the unique ID of the survey
	 * @return the number of voters for the current survey
	 */
	public Integer getTotalVoters(Integer surveyId);
	
	/**
	 * Load the statistics of the given question
	 * @param questionId the identifier of the survey 
	 * @return a map whose key is the choice ID, with the number of times it was answered as value
	 */
	public Map<Integer, Integer> getQuestionStatistics(Integer questionId);
	
	/**
	 * Get the percentage of the preferences the given choice collected
	 * @param questionStats
	 * @param choiceId
	 * @return
	 */
	public double getChoicePercentage(Map<Integer, Integer> questionStats, Integer choiceId);
	
}
