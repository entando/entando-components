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

import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;

import java.util.List;

public interface IChoiceDAO {

	/**
	 * Load the choice of a survey
	 * @param id the id of the question to load
	 * @return the choice requested, null otherwise
	 */
	public Choice loadChoice(int id);
	
	/**
	 * Save the given choice in the database
	 * @param choice
	 */
	public void saveChoice(Choice choice);
	
	/**
	 * Save the choice in the database selecting automatically the position (thus ignoring the one defined
	 * for the given choice)
	 * @param choice
	 */
	public void saveChoiceInSortedPosition(Choice choice);
	
	/**
	 * Delete a choice
	 * @param id The id of the survey to delete
	 */
	public void deleteChoice(int id);
	
	/**
	 * Updates a single choice in the database. The choice to update is located through its ID
	 * @param choice
	 */
	public void updateChoice(Choice choice);
	
	/**
	 * Delete multiple choice from the database given the ID of the question they belong to 
	 * @param id The id of the question of the choices to delete
	 */
	public void deleteChoiceByQuestionId(int id);
	
	public void swapChoicePosition(Choice choiceToSwap, List<Choice> choices, boolean up);
	
}
