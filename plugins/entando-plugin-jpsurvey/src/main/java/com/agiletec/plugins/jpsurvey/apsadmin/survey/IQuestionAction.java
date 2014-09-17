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

public interface IQuestionAction {

	/**
	 * Add a new question to the current survey or poll
	 * @return
	 */
	public String addNewQuestion();

	/**
	 * Load the current 'question' object to edit
	 * @return
	 */
	public String editSingleQuestion();

	/**
	 * Save the a new record or update an existing one, depending of the current action
	 * @return
	 */
	public String saveQuestion();

	/**
	 * This will delete the given question and the related choices 
	 * @return
	 */
	public String deleteQuestion();

	/**
	 * Used to change the position of the given question with the one preceding in the list
	 * @return
	 */
	public String moveQuestionUp();

	/**
	 * Used to change the position of the given question with the one following in the list
	 * @return
	 */
	public String moveQuestionDown();

	/**
	 * This will result in the warning message printed befor deletion
	 * @return
	 */
	public String trashQuestion();

	/**
	 * Trampoline to choice actions 
	 * @return
	 */
	public String addFreeText();

	/**
	 * Trampoline to choice actions 
	 * @return
	 */
	public String addChoice();

	/**
	 * Trampoline to choice actions
	 * @return
	 */
	public String editChoice();
	
	/**
	 * Determine if the fields can be modified
	 * @param surveyId the ID of the current survey
	 * @return
	 */
	public boolean isEditable(Integer surveyId);	
	
	/**
	 * Count the number of times the given question has been answered 
	 * @param questionId the ID of the choice to count occurences for
	 */
	public int getResponseOccurences(Integer choiceId);
	
	/**
	 * Trampoline to the list of the submitted free text options
	 * @return SUCCESS
	 */
	public String freeTextListEntry();
	
	public static final int SINGLE_CHOICE_ID = 1;
	public static final int MULTIPLE_CHOICE_ID = 0;
	
}