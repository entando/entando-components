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

import java.util.Collection;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

public interface ISurveyManager {

	/**
	 * This method loads a complete survey. 'Complete' here means all the elements found in the database
	 * for the survey with the given ID, so check your assumptions because 'complete' is neither 'correct'
	 * nor 'logically complete'.
	 * @param id the ID of the survey to load
	 * @return the complete survey, null otherwise
	 * @throws ApsSystemException in case of error
	 */
	public Survey loadSurvey(int id) throws ApsSystemException;
	
	/**
	 * Load a question and the related choices, if any.
	 * @param id The id if the question to load.
	 * @return the requested question, null otherwise
	 * @throws ApsSystemException in case of error
	 */
	public Question loadQuestion(int id) throws ApsSystemException;
	
	/**
	 * Load a choice from the database
	 * @param id The id of the choice to load
	 * @return The requested choice, null otherwise 
	 * @throws ApsSystemException in case of error
	 */
	public Choice loadChoice(int id) throws ApsSystemException;
	
	/**
	 * Save the given choice in the database 
	 * @param choice The choice to save in the database
	 * @throws ApsSystemException in case of error
	 */
	public void saveChoice(Choice choice) throws ApsSystemException;

	/**
	 * Save the given choice in the database with the higher position (thus ignoring the one defined).
	 * @param choice The choice to save in the database
	 * @throws ApsSystemException in case of error
	 */
	public void saveChoiceInSortedPosition(Choice choice) throws ApsSystemException;
	
	/**
	 * Delete a choice
	 * @param id The id of the survey to delete
	 * @throws ApsSystemException in case of error
	 */
	public void deleteChoice(int id) throws ApsSystemException;
	
	/**
	 * Return the list of the choices belonging to the given question
	 * @param id The id of the question that contains the requested choices
	 * @return The list of the choices of the given question, null otherwise
	 * @throws ApsSystemException in case of error
	 */
	public List<Choice> getQuestionChoices(int id) throws ApsSystemException;
	
	/**
	 * Return the list of the questions belonging to the given survey
	 * @param id The id of the question that contains the requested choices
	 * @return The list of the questions of the given survey, null otherwise
	 * @throws ApsSystemException in case of error
	 */
	public List<Question> getSurveyQuestions(int id) throws ApsSystemException;
	
	/**
	 * Store the given question and the related choice(s) in the database tables
	 * @param question The question to record
	 * @throws ApsSystemException in case of error
	 */
	public void saveQuestion(Question question) throws ApsSystemException;
	
	/**
	 * Delete the question and the choices from the database tables
	 * @param id the id of the question to delete
	 * @throws ApsSystemException in case of error
	 */
	public void deleteQuestion(int id) throws ApsSystemException;
	
	/**
	 * Save a complete survey in the database. If the survey contains questions (and, conversely, choices)
	 * they will be saved as well.
	 * @param survey
	 * @throws ApsSystemException in case of error
	 */
	public void saveSurvey(Survey survey) throws ApsSystemException;
	
	/**
	 * Delete the survey and, cascading, all the related elements, when present.
	 * @param id The id of the survey to delete
	 * @throws ApsSystemException in case of error
	 */
	public void deleteSurvey(int id) throws ApsSystemException;
	
	/**
	 * Delete multiple choice from the database given the ID of the question they belong to 
	 * @param id The id of the question of the choices to delete
	 * @throws ApsSystemException in case of error
	 */
	public void deleteChoiceByQuestionId(int id) throws ApsSystemException;
	
	/**
	 * Updates a single choice in the database. The choice to update is located through its ID
	 * @param choice The choice to update in the database
	 * @throws ApsSystemException in case of error
	 */
	public void updateChoice(Choice choice) throws ApsSystemException;
	
	/**
	 * This updates the question and the related choices.
	 * @param question The question to update
	 * @throws ApsSystemException in case of error
	 */
	public void updateQuestion(Question question) throws ApsSystemException;
	
	/**
	 * Delete all the questions -and related choices, if any- belonging to the given survey 
	 * @param id The ID of the survey whose questions are to be deleted
	 * @throws ApsSystemException in case of error
	 */
	public void deleteQuestionBySurveyId(int id) throws ApsSystemException;
	
	/**
	 * Update a survey and its related elements in the database.
	 * @param survey The survey to update
	 * @throws ApsSystemException in case of error
	 */
	public void updateSurvey(Survey survey) throws ApsSystemException;
	
	/**
	 * Swaps the position of the given question with the one closer by position. We can choose to swap the given question with
	 * the following or preceding one
	 * @param id the ID of the question to shift
	 * @param isUp when true the position of the given question will be swapped with the one preceding in the list
	 * @throws ApsSystemException in case of error
	 */
	public void swapQuestionPosition(int id, boolean isUp)  throws ApsSystemException;
	
	/**
	 * Swaps the position of the given choice with the one closer by position. We can choose to swap the
	 * given choice with the following or preceding one
	 * @param choiceId the ID of the choice to move
	 * @param isUp when true the position of the given choice will be swapped with the one preceding in the
	 * list, with the following otherwise
	 * @throws ApsSystemException in case of error
	 */
	public void swapChoicePosition(int id, boolean isUp) throws ApsSystemException;
	
	/**
	 * This saves the given question in the most desirable position so to avoid having questions sharing the same position in list.
	 * @param question the question to save
	 * @throws ApsSystemException in case of error
	 */
	public void saveQuestionInSortedPosition(Question question) throws ApsSystemException;
	
	/**
	 * Search for surveys matching the given criteria. Please note that at the moment questions and choices
	 * are excluded from the search
	 * @param id The ID of the survey to look for
	 * @param description A string eventually contained in the description
	 * @param groups The belonging group
	 * @param isActive Search for active surveys
	 * @param isQuestionnaire Search for questionnaires
	 * @param title A string eventually contained in the title
	 * @param isPublic Search for public surveys
	 * @return The ID list of the surveys found, if any
	 * @throws ApsSystemException in case of error
	 */
	public List<Integer> searchSurvey(Integer id, String description, Collection<String> groups, Boolean isActive, Boolean isQuestionnaire, String title, Boolean isPublic) throws ApsSystemException;
	
	/** 
	 * This will get the active surveys belonging to the same group(s) of the given user
	 * @param userdetails 
	 * @param ignoreDate when true the time interval specified for each survey will be ignored
	 * @return a list containing the ID of the published surveys which can be answered by the given user
	 * @throws ApsSystemException
	 */
	public List<Integer> getActiveSurveyByUser(UserDetails userdetails, Boolean isQuestionnaire, Boolean archive) throws ApsSystemException;
	
	/**
	 * Get the list of the active surveys
	 * @return The list of ID of the active surveys
	 * @throws ApsSystemException in case of error
	 */
	public List<Integer> getActiveSurveyList() throws ApsSystemException;
	
	/**
	 * Get the list of all surveys present in the system
	 * @return The list of ID of the system surveys
	 * @throws ApsSystemException in case of error
	 */
	public List<Integer> getSurveyList() throws ApsSystemException;
}
