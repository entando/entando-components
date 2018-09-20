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