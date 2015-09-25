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

import com.agiletec.plugins.jpsurvey.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpsurvey.aps.system.services.SurveySystemConstants;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.ISurveyManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;
import com.agiletec.plugins.jpsurvey.apsadmin.survey.QuestionAction;
import com.opensymphony.xwork2.Action;

public class TestQuestionAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	private void init() {
		this.setResponseManager((IResponseManager) this.getService(SurveySystemConstants.SURVEY_RESPONSE_MANAGER));
		this.setSurveyManager((ISurveyManager) this.getService(SurveySystemConstants.SURVEY_MANAGER));
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testEditSingleQuestion() throws Throwable {
		String result = null;
		try {
			this.setUserOnSession("admin");
			
			// edit with no parameters
			this.initAction("/do/jpsurvey/Survey", "editSingleQuestion");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			
			// edit unknown ID
			this.initAction("/do/jpsurvey/Survey", "editSingleQuestion");
			this.addParameter("questionId", -1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			
			// edit known ID
			this.initAction("/do/jpsurvey/Survey", "editSingleQuestion");
			this.addParameter("questionId", 1);
			result = this.executeAction();
			assertEquals(BaseAction.SUCCESS, result);
		} catch (Throwable t) {
			throw t;
		} 
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testSaveQuestion() throws Throwable {
		Survey survey = this.getFakeActiveSurvey();
		String result = null;
		try {
			this.setUserOnSession("admin");
			survey.getQuestions().clear();
			survey.setQuestionnaire(false);
			survey.setActive(true);
			this.getSurveyManager().saveSurvey(survey);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertTrue(survey.getQuestions().isEmpty());
			
			// invoke without parameters
			this.initAction("/do/jpsurvey/Survey", "saveQuestion");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			
			// add a new question to the current survey
			this.initAction("/do/jpsurvey/Survey", "saveQuestion");
			this.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
			this.addParameter("questionnaire", true);
			this.addParameter("surveyId", survey.getId());
			this.addParameter("question-it", "La risposta a tutto è: ....");
			this.addParameter("questionId", -1); // NOT NEEDED
			result = this.executeAction();
			assertNotNull(result);
			assertEquals("editSingleQuestion", result);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertFalse(survey.getQuestions().isEmpty());
			assertEquals("La risposta a tutto è: ....", survey.getQuestions().get(0).getQuestions().get("it"));
			
			// modify an existing question 
			this.initAction("/do/jpsurvey/Survey", "saveQuestion");
			this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
			this.addParameter("questionnaire", true);
			this.addParameter("surveyId", survey.getId());
			this.addParameter("question-it", "42");
			this.addParameter("singleChoice", 1);
			this.addParameter("questionId", survey.getQuestions().get(0).getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertFalse(survey.getQuestions().isEmpty());
			assertEquals("42", survey.getQuestions().get(0).getQuestions().get("it"));
		} catch (Throwable t) {
			throw t;
		} finally {
			if (null != survey) {
				this.getSurveyManager().deleteSurvey(survey.getId());
			}
		}
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testDeleteQuestion() throws Throwable {
		Survey survey = this.createFakeSurveyForTest(false, true, false);
		Question question = null;
		String result = null;
		try {
			this.setUserOnSession("admin");
			// save the survey
			this.getSurveyManager().saveSurvey(survey);
			// update internal id's by reloading the object
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertFalse(survey.getQuestions().isEmpty()); // FIXME delete this
			// delete known ID
			this.initAction("/do/jpsurvey/Survey", "deleteQuestion");
			this.addParameter("questionId", survey.getQuestions().get(0).getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			question = this.getSurveyManager().loadQuestion(survey.getQuestions().get(0).getId());
			assertNull(question);
			
			// delete unknown ID
			this.initAction("/do/jpsurvey/Survey", "deleteQuestion");
			this.addParameter("questionId", -1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			
			// invoke with no params
			this.initAction("/do/jpsurvey/Survey", "deleteQuestion");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testMoveQuestionUp() throws Throwable {
		Survey survey = this.createFakeSurveyForTest(false, true, false);
		Survey expected = null;
		String result = null;
		try {
			this.setUserOnSession("admin");
			// save the survey
			this.getSurveyManager().saveSurvey(survey);
			// update internal id's by reloading the object
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertEquals(2, survey.getQuestions().size());
			// load a copy 
			expected = this.getSurveyManager().loadSurvey(survey.getId());
			// invoke without arguments
			this.initAction("/do/jpsurvey/Survey", "moveQuestionUp");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			// swap unknown id
			this.initAction("/do/jpsurvey/Survey", "moveQuestionUp");
			this.addParameter("questionId", -1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertEquals(expected.getQuestions().get(0).getId(), survey.getQuestions().get(0).getId());
			assertEquals(expected.getQuestions().get(1).getId(), survey.getQuestions().get(1).getId());
			// swap known id
			this.initAction("/do/jpsurvey/Survey", "moveQuestionUp");
			this.addParameter("questionId", survey.getQuestions().get(1).getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertEquals(expected.getQuestions().get(0).getId(), survey.getQuestions().get(1).getId());
			assertEquals(expected.getQuestions().get(1).getId(), survey.getQuestions().get(0).getId());
			this.initAction("/do/jpsurvey/Survey", "moveQuestionUp");
			this.addParameter("questionId", survey.getQuestions().get(1).getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			survey=getSurveyManager().loadSurvey(survey.getId());
			assertEquals(expected.getQuestions().get(0).getId(), survey.getQuestions().get(0).getId());			
		} catch (Throwable t) {
			throw t;
		} finally {
			getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testMoveQuestionDown() throws Throwable {
		Survey survey = this.createFakeSurveyForTest(false, true, false);
		Survey expected = null;
		String result = null;
		try {
			this.setUserOnSession("admin");
			// save the survey
			this.getSurveyManager().saveSurvey(survey);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertEquals(2, survey.getQuestions().size());
			expected = this.getSurveyManager().loadSurvey(survey.getId());
			// invoke without arguments
			this.initAction("/do/jpsurvey/Survey", "moveQuestionDown");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			// swap unknown id
			this.initAction("/do/jpsurvey/Survey", "moveQuestionDown");
			this.addParameter("questionId", -1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertEquals(expected.getQuestions().get(0).getId(), survey.getQuestions().get(0).getId());
			assertEquals(expected.getQuestions().get(1).getId(), survey.getQuestions().get(1).getId());
			expected = survey;
			this.initAction("/do/jpsurvey/Survey", "moveQuestionDown");
			this.addParameter("questionId", survey.getQuestions().get(0).getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertEquals(expected.getQuestions().get(0).getId(), survey.getQuestions().get(1).getId());
			assertEquals(expected.getQuestions().get(1).getId(), survey.getQuestions().get(0).getId());
			this.initAction("/do/jpsurvey/Survey", "moveQuestionDown");
			this.addParameter("questionId", survey.getQuestions().get(0).getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertEquals(expected.getQuestions().get(1).getId(), survey.getQuestions().get(1).getId());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	// NOTE: this will return SUCCESS only when you are allowed to add a new text
	public void testAddFreeText() throws Throwable {
		Survey survey = this.createFakeSurveyForTest(false, true, false);
		Survey free = this.createFakeSurveyForTest(false, true, true);
		String result = null;
		try {
			this.setUserOnSession("admin");
			// save the survey
			this.getSurveyManager().saveSurvey(survey);
			// update internal id's by reloading the object
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			this.getSurveyManager().saveSurvey(free);
			free = this.getSurveyManager().loadSurvey(free.getId());
			// invoke without arguments
			this.initAction("/do/jpsurvey/Survey", "addFreeText");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			
			// add choice to unknown question
			this.initAction("/do/jpsurvey/Survey", "addFreeText");
			this.addParameter("questionId", -1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			
			// request to add new question with valid data
			this.initAction("/do/jpsurvey/Survey", "addFreeText");
			this.addParameter("questionId", survey.getQuestions().get(0).getId());
			this.addParameter("questionnaire", true);
			this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
			this.addParameter("singleChoice", true);
			this.addParameter("question-it", "TEST");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			
			// request to add new question with valid data
			this.initAction("/do/jpsurvey/Survey", "addFreeText");
			this.addParameter("questionId", free.getQuestions().get(0).getId());
			this.addParameter("questionnaire", true);
			this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
			this.addParameter("singleChoice", true);
			this.addParameter("question-it", "TEST");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals("noMoreFreeText", result);
		} catch (Throwable t) {
			throw t;
		} finally {
			getSurveyManager().deleteSurvey(survey.getId());
			getSurveyManager().deleteSurvey(free.getId());
		}
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testAddChoice() throws Throwable {
		Survey survey = this.createFakeSurveyForTest(false, true, true);
		String result = null;
		try {
			this.setUserOnSession("admin");
			// save the survey
			this.getSurveyManager().saveSurvey(survey);
			// update internal id's by reloading the object
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			// invoke without arguments
			this.initAction("/do/jpsurvey/Survey", "addChoice");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			
			// invoke without arguments
			this.initAction("/do/jpsurvey/Survey", "addChoice");
			this.addParameter("questionId", survey.getQuestions().get(0).getId());
			this.addParameter("questionnaire", true);
			this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
			this.addParameter("singleChoice", true);
			this.addParameter("question-it", "TEST");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	public void testTrashQuestion() throws Throwable {
		String result = null;
		QuestionAction action = null;
		try {
			this.setUserOnSession("admin");
			
			// test with no question ID
			this.initAction("/do/jpsurvey/Survey", "trashQuestion");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(Action.INPUT, result);
			
			// test with no question ID
			this.initAction("/do/jpsurvey/Survey", "trashQuestion");
			this.addParameter("QuestionId", -1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(Action.SUCCESS, result);
			action = (QuestionAction) this.getAction();
			assertNotNull(action.getQuestions());
			assertTrue(action.getQuestions().isEmpty());
			
			// test with valid question ID
			this.initAction("/do/jpsurvey/Survey", "trashQuestion");
			this.addParameter("QuestionId", 1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(Action.SUCCESS, result);
			action = (QuestionAction) this.getAction();
			assertNotNull(action.getQuestion());
			assertEquals(3, action.getQuestion().getChoices().size());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public IResponseManager getResponseManager() {
		return _responseManager;
	}
	public void setResponseManager(IResponseManager responseManager) {
		this._responseManager = responseManager;
	}
	
	public void setSurveyManager(ISurveyManager surveyManager) {
		this._surveyManager = surveyManager;
	}
	public ISurveyManager getSurveyManager() {
		return _surveyManager;
	}
	
	private IResponseManager _responseManager;
	private ISurveyManager _surveyManager;
	
}
