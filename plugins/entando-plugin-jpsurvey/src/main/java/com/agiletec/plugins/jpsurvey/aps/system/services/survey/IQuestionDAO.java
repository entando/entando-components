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
package com.agiletec.plugins.jpsurvey.aps.system.services.survey;

import java.util.List;

import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;

public interface IQuestionDAO {

	/**
	 * Load the question and the related choices
	 * @param id The id of the question to load
	 * @return the requested 'question' object, null otherwise
	 */
	public Question loadQuestion(int id);
	
	/**
	 * Return the list of the choices sorted by position, belonging to the same question
	 * @param id The id of the question containing the requested choices
	 * @return The list of the choices related to the given question, or an empty list
	 */
	public List<Choice> getQuestionChoices(int id);

	/**
	 * Save a question an the relative choice(s), if any, in the database tables
	 * @param question The question to save
	 */
	public void saveQuestion(Question question);
	
	/**
	 * Delete the given question and the relative choice(s) from the database tables.
	 * @param id The id of the question to delete
	 */
	public void deleteQuestion(int id);
	
	/**
	 * This updates the question and the related choices.
	 * @param question The question to update
	 */
	public void updateQuestion(Question question);
	
	/**
	 * Delete all the questions -and related choices, if any- belonging to the given survey 
	 * @param id The ID of the survey whose questions are to be deleted
	 */
	public void deleteQuestionBySurveyId(int id);
	
	/**
	 * This saves the given question in the most desirable position so to avoid having questions sharing the same position in list.
	 * @param question the question to save
	 */
	public void saveQuestionInSortedPosition(Question question);
	
	public void swapQuestionPosition(Question questionToSwap, List<Question> questions, boolean up);
	
}
