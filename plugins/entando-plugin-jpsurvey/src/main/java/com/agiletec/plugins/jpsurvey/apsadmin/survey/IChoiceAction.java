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

import java.util.Map;

public interface IChoiceAction {

	/**
	 * Edit the 'choice' given its ID 
	 * @return
	 */
	public String editSingleChoice();

	/**
	 * Save a new 'choice' element or update an existing one
	 * @return
	 */
	public String saveChoice();

	/**
	 * This adds a new 'choice' of type freeText. If nextPos is undefined we insert at position 0.
	 * @return
	 */
	public String addNewFreeText();

	/**
	 * Add a new answer option to the current question
	 * @return
	 */
	public String addNewChoice();

	/**
	 * This will delete the given choice from the options list
	 * @return
	 */
	public String deleteChoice();

	/**
	 * Used to change the position of the given choice with the one preceding in the list
	 * @return
	 */
	public String moveChoiceUp();

	/**
	 * Used to change the position of the given choice with the one following in the list
	 * @return
	 */
	public String moveChoiceDown();
	
	/**
	 * Get all the free text answers submitted in the free text gruping them by occurrence
	 */
	public String freeTextList();
	
}