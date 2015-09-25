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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.SurveySystemConstants;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.ISurveyManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;
import com.agiletec.plugins.jpsurvey.apsadmin.survey.SurveyAction;
import com.opensymphony.xwork2.Action;

public class TestSurveyAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	private void init() {
		this.setSurveyManager((ISurveyManager) this.getService(SurveySystemConstants.SURVEY_MANAGER));
		this.setResponseManager((IResponseManager) this.getService(SurveySystemConstants.SURVEY_RESPONSE_MANAGER));
		this.setVoterManager((IVoterManager) this.getService(SurveySystemConstants.SURVEY_VOTER_MANAGER));
		this.setGroupManager((IGroupManager) this.getService(SystemConstants.GROUP_MANAGER));
		this.setResourceManager((IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER));
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testEditSurvey() throws Throwable {
		String result = null;
		try {
			this.setUserOnSession("admin");
			
			// with no params at all it must NOT crash!
			this.initAction("/do/jpsurvey/Survey", "editSurvey");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.FAILURE, result);
			// invalid questioannaire 
			this.initAction("/do/jpsurvey/Survey", "editSurvey");
			this.addParameter("surveyId", "-1");
			this.addParameter("questionnaire", true);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals("listSurveys", result);
			// invalid poll			
			this.initAction("/do/jpsurvey/Survey", "editSurvey");
			this.addParameter("surveyId", "-1");
			this.addParameter("questionnaire", false);
			result = this.executeAction();	
			assertNotNull(result);
			assertEquals("listSurveys", result);
			// existing questionnaire
			this.initAction("/do/jpsurvey/Survey", "editSurvey");
			this.addParameter("surveyId", "1");
			this.addParameter("questionnaire", true);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(Action.SUCCESS, result);
			// existing polls
			this.initAction("/do/jpsurvey/Survey", "editSurvey");
			this.addParameter("surveyId", "2");
			this.addParameter("questionnaire", false);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(Action.SUCCESS, result);
		} catch (Throwable t) {
			throw t;
		}
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testSaveSurvey() throws Throwable {
		String result = null;
		Survey poll = this.prepareSurveyForTest(false,false);
		Survey questionnaire = this.prepareSurveyForTest(true,false);
		SurveyAction action = null;
		try {
			this.setUserOnSession("admin");
			this.getSurveyManager().saveSurvey(poll);
			this.getSurveyManager().saveSurvey(questionnaire);
			// updating wrong ID
			this.initAction("/do/jpsurvey/Survey", "saveSurvey");
			this.addParameter("surveyId", "-1");
			this.addParameter("questionnaire", true);
			this.addParameter("checkCookie", "true");
			this.addParameter("description-it", "mucca");
			this.addParameter("title-it", "lilla");
			this.addParameter("groupName", Group.FREE_GROUP_NAME);
			this.addParameter("startDate", "02/06/2008");
			this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.FAILURE, result);
			
			// editing existing id
			this.initAction("/do/jpsurvey/Survey", "saveSurvey");
			this.addParameter("surveyId", poll.getId());
			assertEquals(3, poll.getId());
			this.addParameter("questionnaire", false);
			this.addParameter("description-it", "mucca");
			this.addParameter("checkCookie", "true");
			this.addParameter("title-it", "lilla");
			this.addParameter("groupName", Group.FREE_GROUP_NAME);
			this.addParameter("startDate", "02/06/2008");
			this.addParameter("publicPartialResult", "1");
			this.addParameter("publicResult", "1");
			this.addParameter("profileUser", "0");
			this.addParameter("gatherUserInfo", "0");
			this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
			this.addParameter("imageDescription-it", "Forza Cagliari");
			result = this.executeAction();
			assertEquals("listSurveys", result);
			
			// modify existing id with missing parameter
			this.initAction("/do/jpsurvey/Survey", "saveSurvey");
			this.addParameter("surveyId", questionnaire.getId());
			assertEquals(4, questionnaire.getId());
			this.addParameter("questionnaire", true);
//			this.addParameter("description-it", "mucca"); // oops!
			this.addParameter("checkCookie", "true");
			this.addParameter("title-it", "lilla");
			this.addParameter("groupName", Group.FREE_GROUP_NAME);
			this.addParameter("startDate", "02/06/2008");
			this.addParameter("publicPartialResult", "1");
			this.addParameter("publicResult", "1");
			this.addParameter("profileUser", "0");
			this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
			result = this.executeAction();
			assertEquals(Action.INPUT, result);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(poll.getId());
			this.getSurveyManager().deleteSurvey(questionnaire.getId());
		}
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testDeleteSurvey() throws Throwable {
		Survey poll = this.prepareSurveyForTest(false,false);
		Survey questionnaire = this.prepareSurveyForTest(true,false);
		Survey testPoll = null;
		String result = null;
		try {
			this.setUserOnSession("admin");
			this.getSurveyManager().saveSurvey(poll);
			this.getSurveyManager().saveSurvey(questionnaire);
			// deleting existing id
			this.initAction("/do/jpsurvey/Survey", "deleteSurvey");
			this.addParameter("surveyId", poll.getId());
			this.addParameter("questionnaire", false);
			assertEquals(3, poll.getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(Action.SUCCESS, result); 
			// silly check...
			testPoll = this.getSurveyManager().loadSurvey(poll.getId());
			assertNull(testPoll);
			// deleting existing id
			this.initAction("/do/jpsurvey/Survey", "deleteSurvey");
			this.addParameter("surveyId", questionnaire.getId());
			this.addParameter("questionnaire", true);
			assertEquals(4, questionnaire.getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			// unknown id
			this.initAction("/do/jpsurvey/Survey", "deleteSurvey");
			this.addParameter("surveyId", "-1");
			this.addParameter("questionnaire", true);
			assertEquals(4, questionnaire.getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			// missing parameters
			this.initAction("/do/jpsurvey/Survey", "deleteSurvey");
			assertEquals(4, questionnaire.getId());
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(poll.getId());
			this.getSurveyManager().deleteSurvey(questionnaire.getId());
		}
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testPublishSurvey() throws Throwable {
		Survey poll = this.prepareSurveyForTest(false, false);
		Survey questionnaire = this.prepareSurveyForTest(true, false);
		Survey actual = null;
		String result = null;
		try {
			this.setUserOnSession("admin");
			this.getSurveyManager().saveSurvey(poll);
			this.getSurveyManager().saveSurvey(questionnaire);
			// publish existing poll
			assertTrue(poll.isPublishable());
			this.initAction("/do/jpsurvey/Survey", "publishSurvey");
			assertEquals(3, poll.getId());
			assertFalse(poll.isActive());
			this.addParameter("surveyId", poll.getId());
			this.addParameter("questionnaire", false);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			actual = this.getSurveyManager().loadSurvey(poll.getId());
			assertNotNull(actual);
			assertTrue(actual.isActive());
			// publish existing questionnaire
			assertTrue(questionnaire.isPublishable());
			this.initAction("/do/jpsurvey/Survey", "publishSurvey");
			assertEquals(4, questionnaire.getId());
			assertFalse(questionnaire.isActive());
			this.addParameter("surveyId", questionnaire.getId());
			this.addParameter("questionnaire", true);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			actual = this.getSurveyManager().loadSurvey(questionnaire.getId());
			assertNotNull(actual);
			assertTrue(actual.isActive());
			
			// publish existing questionnaire
			this.initAction("/do/jpsurvey/Survey", "publishSurvey");
			assertFalse(questionnaire.isActive());
			this.addParameter("surveyId", questionnaire.getId());
			this.addParameter("questionnaire", true);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			actual = this.getSurveyManager().loadSurvey(questionnaire.getId());
			assertNotNull(actual);
			assertTrue(actual.isActive());
			questionnaire.setActive(false);
			questionnaire.getQuestions().get(1).getChoices().remove(0);
			questionnaire.getQuestions().get(1).getChoices().get(0).setFreeText(false);
			getSurveyManager().updateSurvey(questionnaire); // NOT PUBLISHABLE ANYMORE
			this.initAction("/do/jpsurvey/Survey", "publishSurvey");
			assertFalse(questionnaire.isActive());
			this.addParameter("surveyId", questionnaire.getId());
			this.addParameter("questionnaire", true);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			actual=this.getSurveyManager().loadSurvey(questionnaire.getId());
			assertNotNull(actual);
			assertFalse(actual.isActive());
			assertEquals(2, actual.getQuestions().size());
			
			// enable unknown survey
			this.initAction("/do/jpsurvey/Survey", "publishSurvey");
			assertEquals(4, questionnaire.getId());
			assertFalse(questionnaire.isActive());
			this.addParameter("surveyId", -1);
			this.addParameter("questionnaire", true);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result); // DO NOTHING
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(poll.getId());
			this.getSurveyManager().deleteSurvey(questionnaire.getId());
		}
	}
	
	//TODO FARE CONTROLLO NUMERI ERRORI E CAMPI
	public void testRestireSurvey() throws Throwable {
		Survey poll = this.prepareSurveyForTest(false, true);
		Survey questionnaire = this.prepareSurveyForTest(true, true);
		Survey testSurvey = null;
		String result = null;
		try {
			this.setUserOnSession("admin");
			this.getSurveyManager().saveSurvey(poll);
			this.getSurveyManager().saveSurvey(questionnaire);
			assertTrue(questionnaire.isActive());
			assertTrue(poll.isActive());
			// invoke action without parameters 
			this.initAction("/do/jpsurvey/Survey", "retireSurvey");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.INPUT, result);
			// retire questionnaire
			this.initAction("/do/jpsurvey/Survey", "retireSurvey");
			this.addParameter("surveyId", questionnaire.getId());
			this.addParameter("questionnaire", true);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(BaseAction.SUCCESS, result);
			testSurvey=this.getSurveyManager().loadSurvey(questionnaire.getId());
			assertNotNull(testSurvey);
			assertFalse(testSurvey.isActive());
			// retire poll
			this.initAction("/do/jpsurvey/Survey", "retireSurvey");
			this.addParameter("surveyId", poll.getId());
			this.addParameter("questionnaire", false);
			result = this.executeAction();
			assertEquals(BaseAction.SUCCESS, result);
			testSurvey=this.getSurveyManager().loadSurvey(poll.getId());
			assertNotNull(testSurvey);
			assertFalse(testSurvey.isActive());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(poll.getId());
			this.getSurveyManager().deleteSurvey(questionnaire.getId());
		}
	}
	
	public void testAssociateSurveyImageEntry() throws Throwable {
		Survey survey = this.prepareSurveyForTest(true, false);
		Survey actual = null;
		SurveyAction action = null;
		String result = null;
		try {
			this.setUserOnSession("admin");
			this.getSurveyManager().saveSurvey(survey);
			assertNotNull(survey);
			// Test with invalid strutsAction
			this.initAction("/do/jpsurvey/Survey", "associateSurveyImageEntry");
			this.addParameter("surveyId", survey.getId());
			this.addParameter("questionnaire", true);
			this.addParameter("checkCookie", true);
			this.addParameter("description-it", "mucca");
			this.addParameter("title-it", "lilla");
			this.addParameter("imageDescription-it", "cioccolatosa");
			this.addParameter("resourceTypeCode", "Image");
			this.addParameter("groupName", Group.FREE_GROUP_NAME);
			this.addParameter("startDate", "10/06/2009");
			this.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
			this.addParameter("profileUser", 0);
			this.addParameter("gatherUserInfo", survey.isGatherUserInfo());
			this.addParameter("imageId", 22);
			result = this.executeAction();
			action = (SurveyAction) this.getAction();
			assertNotNull(result);
			assertEquals(action.INPUT, result);
			assertEquals((Integer)survey.getId(), action.getSurveyId());
			// test with valid parameters
			this.initAction("/do/jpsurvey/Survey", "associateSurveyImageEntry");
			this.addParameter("surveyId", survey.getId());
//			this.addParameter("questionnaire", true);
			this.addParameter("checkCookie", false);
			this.addParameter("description-it", "mucca");
			this.addParameter("title-it", "lilla");
			this.addParameter("imageDescription-it", "cioccolatosa");
			this.addParameter("resourceTypeCode", "Image");
			this.addParameter("groupName", Group.FREE_GROUP_NAME);
			this.addParameter("startDate", "10/06/2009");
			this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
			this.addParameter("profileUser", 0);
			this.addParameter("gatherUserInfo", survey.isGatherUserInfo());
			this.addParameter("imageId", 22);
			result = this.executeAction();
			action = (SurveyAction) this.getAction();
			assertNotNull(result);
			assertEquals(action.SUCCESS, result);
			assertNotNull(action.getSurveyId());
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertEquals("cioccolatosa", actual.getImageDescriptions().get("it"));
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}

	public void testRemoveImage() throws Throwable {
		SurveyAction action = null;
		String result = null;
		Survey survey = this.prepareSurveyForTest(true, true);
		Survey verify = null;
		ApsProperties prop = null;
		try {
			this.setUserOnSession("admin");
			this.getSurveyManager().saveSurvey(survey);
			survey.setImageId("29975");
			this.getSurveyManager().updateSurvey(survey);
			this.initAction("/do/jpsurvey/Survey", "removeSurveyImage");
			this.addParameter("surveyId", survey.getId());
			this.addParameter("questionnaire", survey.isQuestionnaire());
			this.addParameter("checkCookie", survey.isCheckCookie());
			this.addParameter("imageId", survey.getImageId());
			this.addParameter("title-it", "Questa");
			this.addParameter("description-it", "è");
			this.addParameter("imageDescription-it", "Spartaaaa");
			this.addParameter("resourceTypeCode", "Image");
			this.addParameter("groupName", survey.getGroupName());
			this.addParameter("startDate", "10/06/2009");
			this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
			this.addParameter("gatherUserInfo", survey.isGatherUserInfo());
			result = this.executeAction();
			assertNotNull(result);
			action = (SurveyAction) this.getAction();
			assertEquals(action.SUCCESS, result);
			verify = this.getSurveyManager().loadSurvey(survey.getId());
			assertNull(verify.getImageId());
			assertTrue(verify.getImageDescriptions().isEmpty());
			prop = verify.getTitles();
			assertEquals("Questa", prop.getProperty("it"));
			prop = verify.getDescriptions();
			assertEquals("è", prop.getProperty("it"));
			prop = verify.getImageDescriptions();
			assertNull(prop.getProperty("it"));
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	public void testTrashSurvey() throws Throwable {
		String result = null;
		SurveyAction action = null;
		try {
			this.setUserOnSession("admin");
			// test with no survey ID
			this.initAction("/do/jpsurvey/Survey", "trashSurvey");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(Action.INPUT, result);
			// test with unknown survey ID
			this.initAction("/do/jpsurvey/Survey", "trashSurvey");
			this.addParameter("surveyId", -1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(Action.INPUT, result);
			action = (SurveyAction) this.getAction();
			assertNotNull(action.getTitles());
			assertTrue(action.getTitles().isEmpty());
			// test with known survey ID
			this.initAction("/do/jpsurvey/Survey", "trashSurvey");
			this.addParameter("surveyId", 1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(Action.SUCCESS, result);
			action = (SurveyAction) this.getAction();
			assertNotNull(action.getSurvey().getTitles());
			assertEquals(2, action.getSurvey().getTitles().size());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	private Survey prepareSurveyForTest(Boolean questionnaire, Boolean active) throws Throwable {
		Survey survey = this.getFakeActiveSurvey();
		survey.setActive(active);
		survey.setQuestionnaire(questionnaire);
		return survey;
	}
	
	public ISurveyManager getSurveyManager() {
		return _surveyManager;
	}
	public void setSurveyManager(ISurveyManager surveyManager) {
		this._surveyManager = surveyManager;
	}
	public IResponseManager getResponseManager() {
		return _responseManager;
	}
	public void setResponseManager(IResponseManager responseManager) {
		this._responseManager = responseManager;
	}
	public IVoterManager getVoterManager() {
		return _voterManager;
	}
	public void setVoterManager(IVoterManager voterManager) {
		this._voterManager = voterManager;
	}
	public IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	
	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}
	public IResourceManager getResourceManager() {
		return _resourceManager;
	}

	private ISurveyManager _surveyManager;
	private IResponseManager _responseManager;
	private IVoterManager _voterManager;
	private IGroupManager _groupManager;
	private IResourceManager _resourceManager;
}

