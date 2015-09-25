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