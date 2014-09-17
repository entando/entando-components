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