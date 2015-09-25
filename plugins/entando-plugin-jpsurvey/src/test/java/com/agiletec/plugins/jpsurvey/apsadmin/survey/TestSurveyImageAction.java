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

import com.agiletec.plugins.jpsurvey.aps.system.services.SurveySystemConstants;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.ISurveyManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;
import com.agiletec.plugins.jpsurvey.apsadmin.survey.SurveyImageAction;
import com.opensymphony.xwork2.Action;

public class TestSurveyImageAction extends ApsAdminPluginBaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	private void init() {
		this.setSurveyManager((ISurveyManager) this.getService(SurveySystemConstants.SURVEY_MANAGER));		
	}

	public void testList() throws Throwable {
		String result = null;
		SurveyImageAction action = null;
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/jpsurvey/Survey", "list");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(Action.SUCCESS, result);
			action = (SurveyImageAction) this.getAction();
			assertNotNull(action.getCategoryRoot());			
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testAssociateSurveyImage() throws Throwable {
		String result = null;
		SurveyImageAction action = null;
		try {
			this.setUserOnSession("admin");
			
			// test with no survey id
			this.initAction("/do/jpsurvey/Survey", "associateSurveyImage");
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(Action.INPUT, result);
			
			// test with unknown survey id
			this.initAction("/do/jpsurvey/Survey", "associateSurveyImage");
			this.addParameter("surveyId", -1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(Action.SUCCESS, result);
			action = (SurveyImageAction) this.getAction();
			assertNotNull(action.getTitles());
			assertTrue(action.getTitles().isEmpty());
			
			// test with normal survey ID
			this.initAction("/do/jpsurvey/Survey", "associateSurveyImage");
			this.addParameter("surveyId", 1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(Action.SUCCESS, result);
			action = (SurveyImageAction) this.getAction();
			assertNotNull(action.getTitles());
			assertEquals(2, action.getTitles().size());
			
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testJoinImage() throws Throwable {
		String result = null;
		SurveyImageAction action = null;
		Survey backup = null;
		Survey survey = null;
		try {
			this.setUserOnSession("admin");
			
			// test on unknown non existent survey
			this.initAction("/do/jpsurvey/Survey", "joinImage");
			this.addParameter("resourceId", "IMG69");
			this.addParameter("surveyId", -1);
			result = this.executeAction();
			assertNotNull(result);
			assertEquals(Action.SUCCESS, result);
			action = (SurveyImageAction) this.getAction();			
			assertNotNull(action.getResources());
			assertFalse(action.getResources().isEmpty());
			
			// test on effective survey
			backup = this.getSurveyManager().loadSurvey(1);
			if (null != backup) {
				this.initAction("/do/jpsurvey/Survey", "joinImage");
				this.addParameter("resourceId", "IMG069");
				this.addParameter("surveyId", 1);
				result = this.executeAction();
				assertNotNull(result);
				assertEquals(Action.SUCCESS, result);
				action = (SurveyImageAction) this.getAction();			
				assertNotNull(action.getResources());
				assertFalse(action.getResources().isEmpty());
				survey = this.getSurveyManager().loadSurvey(1);
				assertEquals("IMG069", survey.getImageId());
			}			
		} catch (Throwable t) {
			throw t;
		} finally {
			// RIPRISTINA LO STATO DEL DB
			if (null != backup) {
				this.getSurveyManager().updateSurvey(backup);
			}
		}
	}
	
	public void setSurveyManager(ISurveyManager surveyManager) {
		this.surveyManager = surveyManager;
	}
	public ISurveyManager getSurveyManager() {
		return surveyManager;
	}

	private ISurveyManager surveyManager;
	
}
