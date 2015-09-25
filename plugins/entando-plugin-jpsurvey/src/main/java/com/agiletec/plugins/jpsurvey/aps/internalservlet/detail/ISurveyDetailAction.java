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
