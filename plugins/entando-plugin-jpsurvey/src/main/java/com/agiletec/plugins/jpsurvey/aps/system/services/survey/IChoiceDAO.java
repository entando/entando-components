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
