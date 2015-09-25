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

import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

public interface ISurveyDAO {
	
	/**
	 * This method loads a complete survey. 'Complete' here means all the elements found in the database
	 * for the survey with the given ID, so check your assumptions because 'complete' is neither 'correct'
	 * nor 'logically complete'. The survey is build sorting the position of the question and then the position
	 * of the choices.
	 * @param id the ID of the survey to load
	 * @return the complete survey, null otherwise 
	 */
	public Survey loadSurvey(int id);
	
	/**
	 * Save a complete survey in the database. If the survey contains questions (and, conversely, choices)
	 * they will be saved as well.
	 * @param survey
	 */
	public void saveSurvey(Survey survey);
	
	/**
	 * Delete a survey and, cascading, the question and choices (if present)
	 * @param id The ID of the survey to delete
	 */
	public void deleteSurvey(int id);
	
	/**
	 * Update a survey as well its related elements in the database.
	 * @param survey The survey to update
	 */
	public void updateSurvey(Survey survey);
	
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
	 */
	public List<Integer> searchSurvey(Integer id, String description, Collection<String> groups, Boolean isActive, Boolean isQuestionnaire, String title, Boolean isPublic);
	
	/**
	 * Return the list of the questions belonging to the given survey
	 * @param id The id of the question that contains the requested choices
	 * @return The list of the questions of the given survey, or an empty list
	 */
	public List<Question> getSurveyQuestions(int id);
	
	public List<Integer> loadResourceUtilizers(String resourceId);
	
}
