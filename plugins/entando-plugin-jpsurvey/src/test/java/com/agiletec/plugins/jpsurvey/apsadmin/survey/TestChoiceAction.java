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
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

public class TestChoiceAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	private void init() {
		this.setSurveyManager((ISurveyManager) this.getService(SurveySystemConstants.SURVEY_MANAGER));
		this.setResponseManager((IResponseManager) this.getService(SurveySystemConstants.SURVEY_RESPONSE_MANAGER));
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testEditSingleChoice() throws Throwable {
		String result = null;
		try {
			this.setUserOnSession("admin");
			
			// invoke without arguments
			this.initAction("/do/jpsurvey/Survey", "editSingleChoice");
			result = this.executeAction();
			assertEquals(BaseAction.INPUT, result);
			
			// load unknown choice
			this.initAction("/do/jpsurvey/Survey", "editSingleChoice");
			result = this.executeAction();
			this.addParameter("choiceId", -1);
			assertEquals(BaseAction.INPUT, result);
			
			// load valid choice
			this.initAction("/do/jpsurvey/Survey", "editSingleChoice");
			this.addParameter("choiceId", 1);
			result = this.executeAction();
			assertEquals(BaseAction.SUCCESS, result);
		} catch (Throwable t) {
			throw t;
		}
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testSaveChoice() throws Throwable {
		Survey survey = this.createFakeSurveyForTest(true, true, false);
		Survey verify = null;
		String result = null;
		try {
			this.setUserOnSession("admin");
			// save the survey
			this.getSurveyManager().saveSurvey(survey);
			// update internal id's by reloading the object
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(survey);
			// invoke without arguments
			this.initAction("/do/jpsurvey/Survey", "saveChoice");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			
			// save new choice
			this.initAction("/do/jpsurvey/Survey", "saveChoice");
			this.addParameter("choiceId", -1);
			this.addParameter("surveyId", survey.getId());
			this.addParameter("questionId", survey.getQuestions().get(0).getId());
			this.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
			this.addParameter("choice-it", "forza Cagliari");
			this.addParameter("isQuestionnaire", "true");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			verify = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(verify.getQuestions().get(0).getChoices());
			assertEquals(3, verify.getQuestions().get(0).getChoices().size());
			assertEquals("forza Cagliari", verify.getQuestions().get(0).getChoices().get(2).getAnswer("it"));
			
			// save edited choice
			survey = getSurveyManager().loadSurvey(survey.getId());
			this.initAction("/do/jpsurvey/Survey", "saveChoice");
			this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
			this.addParameter("questionId", survey.getQuestions().get(0).getId());
			this.addParameter("choiceId", survey.getQuestions().get(0).getChoices().get(2).getId());
			this.addParameter("surveyId", survey.getId());
			this.addParameter("choice-it", "forza Milan");
			this.addParameter("isQuestionnaire", "true");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			verify = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(verify.getQuestions().get(0).getChoices());
			assertEquals(3, verify.getQuestions().get(0).getChoices().size());
			assertEquals("forza Milan", verify.getQuestions().get(0).getChoices().get(2).getAnswer("it"));
		} catch (Throwable t) {
			throw t;
		} finally {
			getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testAddFreeText() throws Throwable {
		Survey survey = this.createFakeSurveyForTest(true, true, false);
		String result = null;
		try {
			this.setUserOnSession("admin");
			// save the survey
			this.getSurveyManager().saveSurvey(survey);
			// update internal id's by reloading the object
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			// invoke without arguments
			this.initAction("/do/jpsurvey/Survey", "addNewFreeText");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			
			// invoke with invalid question id, we expect a SQL exception
			this.initAction("/do/jpsurvey/Survey", "addNewFreeText");
			this.addParameter("questionId", "-1");
			this.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.FAILURE, result);
			
			// normal operation
			this.initAction("/do/jpsurvey/Survey", "addNewFreeText");
			this.addParameter("questionId", survey.getQuestions().get(0).getId());
			this.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(survey);
			assertNotNull(survey.getQuestions().get(0).getChoices());
			assertEquals(3, survey.getQuestions().get(0).getChoices().size());
			assertTrue(survey.getQuestions().get(0).getChoices().get(2).isFreeText());
		} catch (Throwable t) {
			throw t;
		} finally {
			getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testDeleteChoice() throws Throwable {
		Survey survey = this.createFakeSurveyForTest(true, true, false);
		String result = null;
		try {
			this.setUserOnSession("admin");
			// save the survey
			this.getSurveyManager().saveSurvey(survey);
			// update internal id's by reloading the object
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			// invoke without parameters
			this.initAction("/do/jpsurvey/Survey", "deleteChoice");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			
			// invoke with wrong parameters
			this.initAction("/do/jpsurvey/Survey", "deleteChoice");
			this.addParameter("choiceId", -1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			
			// invoke with correct parameters
			this.initAction("/do/jpsurvey/Survey", "deleteChoice");
			this.addParameter("choiceId", survey.getQuestions().get(0).getChoices().get(0).getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(survey);
			assertNotNull(survey.getQuestions());
			assertNotNull(survey.getQuestions().get(0).getChoices());
			assertEquals(1, survey.getQuestions().get(0).getChoices().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testMoveChoiceDown() throws Throwable {
		Survey survey = this.createFakeSurveyForTest(true, true, false);
		Survey test = null;
		String result = null;
		try {
			this.setUserOnSession("admin");
			// save the survey
			this.getSurveyManager().saveSurvey(survey);
			// update internal id's by reloading the object
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			// invoke without parameters
			this.initAction("/do/jpsurvey/Survey", "moveChoiceDown");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			
			// invoke with invalid id
			this.initAction("/do/jpsurvey/Survey", "moveChoiceDown");
			this.addParameter("choiceId", -1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			
			// move down the last has no effects
			test = this.getSurveyManager().loadSurvey(survey.getId());
			this.initAction("/do/jpsurvey/Survey", "moveChoiceDown");
			this.addParameter("choiceId", survey.getQuestions().get(0).getChoices().get(1).getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(survey);
			assertEquals(survey.getQuestions().get(0).getChoices().get(1).getId(), test.getQuestions().get(0).getChoices().get(1).getId());
			
			// normal operations
			test=getSurveyManager().loadSurvey(survey.getId());
			this.initAction("/do/jpsurvey/Survey", "moveChoiceDown");
			this.addParameter("choiceId", survey.getQuestions().get(0).getChoices().get(0).getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(survey);
			assertEquals(survey.getQuestions().get(0).getChoices().get(1).getId(), test.getQuestions().get(0).getChoices().get(0).getId());
		} catch (Throwable t) {
			throw t;
		} finally {
			getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testMoveChoiceUp() throws Throwable {
		Survey survey = this.createFakeSurveyForTest(true, true, false);
		Survey test = null;
		String result = null;
		try {
			this.setUserOnSession("admin");
			// save the survey
			this.getSurveyManager().saveSurvey(survey);
			// update internal id's by reloading the object
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			// invoke without parameters
			this.initAction("/do/jpsurvey/Survey", "moveChoiceUp");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			
			// invoke with invalid id
			this.initAction("/do/jpsurvey/Survey", "moveChoiceUp");
			this.addParameter("choiceId", -1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			
			// move up the first
			test=getSurveyManager().loadSurvey(survey.getId());
			this.initAction("/do/jpsurvey/Survey", "moveChoiceUp");
			this.addParameter("choiceId", survey.getQuestions().get(0).getChoices().get(0).getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(survey);
			assertEquals(survey.getQuestions().get(0).getChoices().get(1).getId(), test.getQuestions().get(0).getChoices().get(1).getId());
			
			// normal operations
			test=getSurveyManager().loadSurvey(survey.getId());
			this.initAction("/do/jpsurvey/Survey", "moveChoiceUp");
			this.addParameter("choiceId", survey.getQuestions().get(0).getChoices().get(1).getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(survey);
			assertEquals(survey.getQuestions().get(0).getChoices().get(1).getId(), test.getQuestions().get(0).getChoices().get(0).getId());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	public void testFreeTextList() throws Throwable {
		String result = null;
		ChoiceAction action = null;
		try {
			this.setUserOnSession("admin");
			
			// invoke without parameters
			this.initAction("/do/jpsurvey/Survey", "freeTextList");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			
			// invoke with invalid choice ID
			this.initAction("/do/jpsurvey/Survey", "freeTextList");
			this.addParameter("choiceId", -1);
			this.addParameter("questionId", 2);
			result = this.executeAction();
			
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			action = (ChoiceAction) this.getAction();
			assertNotNull(action.getFreeTextMap());
			assertTrue(action.getFreeTextMap().isEmpty());
			
			// invoke with NON free text choice ID
			this.initAction("/do/jpsurvey/Survey", "freeTextList");
			this.addParameter("choiceId", 2);
			this.addParameter("questionId", 2);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			action = (ChoiceAction) this.getAction();
			assertNotNull(action.getFreeTextMap());
			assertTrue(action.getFreeTextMap().isEmpty());
			
			this.initAction("/do/jpsurvey/Survey", "freeTextList");
			this.addParameter("choiceId", 6);
			this.addParameter("questionId", 2);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			action = (ChoiceAction) this.getAction();
			assertNotNull(action.getFreeTextMap());
			assertFalse(action.getFreeTextMap().isEmpty());
			assertEquals(1, action.getFreeTextMap().size());
			assertEquals(Integer.valueOf(1), action.getFreeTextMap().get("lorem ipsum dolor"));
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testTrashChoice() throws Throwable {
		String result = null;
		ChoiceAction action = null;
		try {
			this.setUserOnSession("admin");
			
			// invoke without parameters
			this.initAction("/do/jpsurvey/Survey", "trashChoice");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			
			// invoke with invalid choice ID
			this.initAction("/do/jpsurvey/Survey", "trashChoice");
			this.addParameter("choiceId", -1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			action = (ChoiceAction) this.getAction();
			assertNotNull(action.getChoices());
			assertTrue(action.getChoices().isEmpty());
			
			// invoke with known choice ID
			this.initAction("/do/jpsurvey/Survey", "trashChoice");
			this.addParameter("choiceId", 1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			action = (ChoiceAction) this.getAction();
			assertNotNull(action.getChoices());
			assertEquals(false, action.getChoice().getChoices().isEmpty()); 
			
			// invoke with known choice ID
			this.initAction("/do/jpsurvey/Survey", "trashChoice");
			this.addParameter("choiceId", 6);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			action = (ChoiceAction) this.getAction();
			assertNotNull(action.getChoice());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	protected IResponseManager getResponseManager() {
		return _responseManager;
	}
	public void setResponseManager(IResponseManager responseManager) {
		this._responseManager = responseManager;
	}
	
	protected ISurveyManager getSurveyManager() {
		return _surveyManager;
	}
	public void setSurveyManager(ISurveyManager surveyManager) {
		this._surveyManager = surveyManager;
	}
	
	private IResponseManager _responseManager;
	private ISurveyManager _surveyManager;
	
}
