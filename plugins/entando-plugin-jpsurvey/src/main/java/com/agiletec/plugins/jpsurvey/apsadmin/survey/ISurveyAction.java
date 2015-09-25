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
package com.agiletec.plugins.jpsurvey.apsadmin.survey;

public interface ISurveyAction {
	
	/**
	 * Add a new survey to the current survey
	 * @return
	 */
	public String addQuestion();

	/**
	 * Create a new survey
	 * @return
	 */
	public String newSurvey();

	/**
	 * This will edit the current survey
	 * @return
	 */
	public String editSurvey();

	/**
	 * Save the modification / save the new survey
	 * @return
	 */
	public String saveSurvey();

	/**
	 * Delete a survey
	 * @return
	 */
	public String deleteSurvey();

	/**
	 * This will displey the warning about the disposal of the current survey
	 * @return
	 */
	public String trashSurvey();

	/**
	 * This will edit the selected question
	 * @return
	 */
	public String editQuestion();
	
	/**
	 * Determine if the survey can be modified: this happens if the survey has not been voted AND is not active. 
	 * 
	 * @param surveyId the ID of the current survey
	 * @return
	 */
	public boolean isEditable(Integer surveyId);
	
	
	/**
	 * Count the number of times the given question has been answered 
	 * @param questionId the ID of the question to count occurences for
	 */
	public int getResponseOccurences(Integer questionId);
	
	/**
	 * Count the voters which answered the given survey
	 * @param surveyId
	 * @return
	 */
	public int getVotersNumber(Integer surveyId);
	
	/**
	 * Invoked to retire an active survey from publication, making it editable again while changing all IDs
	 * @return
	 */
	public String retireSurvey();
	
	public String publishSurvey();
	
	/**
	 * Used in oreder to associate an image to the current editing survey
	 * @return
	 */
	public String associateSurveyImageEntry();

}