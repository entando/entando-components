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
