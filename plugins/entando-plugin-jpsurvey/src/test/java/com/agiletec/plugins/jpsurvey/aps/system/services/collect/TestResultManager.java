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
package com.agiletec.plugins.jpsurvey.aps.system.services.collect;

import java.util.List;

import com.agiletec.plugins.jpsurvey.aps.ApsPluginBaseTestCase;

import com.agiletec.plugins.jpsurvey.aps.system.services.SurveySystemConstants;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.SingleQuestionResponse;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

public class TestResultManager extends ApsPluginBaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	private void init() {
		this._responseManager = (IResponseManager) this.getService(SurveySystemConstants.SURVEY_RESPONSE_MANAGER);
	}
	
	public void testSaveResponse() throws Throwable {
		SingleQuestionResponse response = this.getFakeResponse();
		try {
			this.getResponseManager().submitResponse(response);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getResponseManager().deleteResponse(response);
		}
	}
	
	public void testAggregateResults() throws Throwable {
		SingleQuestionResponse r1 = this.getFakeResponse();
		SingleQuestionResponse r2 = this.getFakeResponse();
		SingleQuestionResponse r3 = this.getFakeResponse();
		List<SingleQuestionResponse> list = null;
		try {
			r1.setFreeText("0123456789");
			r2.setChoiceId(2);
			r3.setQuestionId(2);
			r3.setChoiceId(2);
			this.getResponseManager().submitResponse(r1);
			this.getResponseManager().submitResponse(r3);
			this.getResponseManager().submitResponse(r2);
			list = this.getResponseManager().aggregateResponseByIds(-1, null, null, null);
			assertNull(list);
			
			list = this.getResponseManager().aggregateResponseByIds(null, null, null, null);
			assertEquals(4, list.size());
			
			list = this.getResponseManager().aggregateResponseByIds(1, null, null, null);
			assertEquals(4, list.size());
			
			list = this.getResponseManager().aggregateResponseByIds(null, 2, null, null);
			assertEquals(2, list.size());
			
			list = this.getResponseManager().aggregateResponseByIds(null, null, 2, null);
			assertEquals(2, list.size());
			
			list = this.getResponseManager().aggregateResponseByIds(null, null, null, "ello");
			assertEquals(2, list.size());
			
			list = this.getResponseManager().aggregateResponseByIds(null, null, null, "3456");
			assertEquals(1, list.size());
			assertEquals(10, list.get(0).getFreeText().length());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getResponseManager().deleteResponse(r1);
			this.getResponseManager().deleteResponse(r2);
			this.getResponseManager().deleteResponse(r3);
		}
	}
	
	public void testDeleteResponseBySurvey() throws Throwable {
		Survey survey = this.getFakeActiveSurvey();
		try {
			this.getSurveyManager().saveSurvey(survey);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			for (Question question: survey.getQuestions()) {
				// WE SUPPOSE THAT ALL THE CHOICES CAN BE ANSWERED
				int choiceAnswered = 0;
				for (Choice choice: question.getChoices()) {
					SingleQuestionResponse response = this.getFakeResponse();
					if (choice.isFreeText()) response.setFreeText("Forza Cagliari!");
					response.setChoiceId(choice.getId());
					response.setQuestionId(question.getId());
					response.setVoterId(1); // ALWAYS THE SAME VOTER
					this._responseManager.submitResponse(response);
					choiceAnswered++;
				}
				List<SingleQuestionResponse> list = this._responseManager.aggregateResponseByIds(null, question.getId(), null, null);
				assertEquals(choiceAnswered, list.size());
			}
			// delete all the choices
			this._responseManager.deleteResponseBySurvey(survey);
			// VERIFY QUESTION
			for (Question question: survey.getQuestions()) {
				List<SingleQuestionResponse> questionsAnswered = this._responseManager.aggregateResponseByIds(null, question.getId(), null, null);
				assertNull(questionsAnswered);
				// VERIFY ANSWER
				for (Choice choice: question.getChoices()) {
					List<SingleQuestionResponse> choiceAnswered = this._responseManager.aggregateResponseByIds(null, null, choice.getId(), null);
					assertNull(choiceAnswered);
				}
			}
			List<SingleQuestionResponse> answered = this._responseManager.aggregateResponseByIds(null, null, null, "Cagliari");
			assertNull(answered);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	public void testDeleteQuestionByQuestionId() throws Throwable {
		Survey survey = this.getFakeActiveSurvey();
		try {
			this.getSurveyManager().saveSurvey(survey);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			for (Question question: survey.getQuestions()) {
				// WE SUPPOSE THAT ALL THE CHOICES CAN BE ANSWERED
				int choiceAnswered = 0;
				for (Choice choice: question.getChoices()) {
					SingleQuestionResponse response = this.getFakeResponse();
					if (choice.isFreeText()) response.setFreeText("Forza Milan!");
					response.setChoiceId(choice.getId());
					response.setQuestionId(question.getId());
					response.setVoterId(1); // ALWAYS THE SAME VOTER
					this._responseManager.submitResponse(response);
					choiceAnswered++;
				}
				this._responseManager.deleteResponseByQuestionId(question.getId());
				List<SingleQuestionResponse> list = this._responseManager.aggregateResponseByIds(null, question.getId(), null, null);
				assertNull(list);
			}
			// VERIFY QUESTION
			for (Question question: survey.getQuestions()) {
				List<SingleQuestionResponse> questionsAnswered = this._responseManager.aggregateResponseByIds(null, question.getId(), null, null);
				assertNull(questionsAnswered);
				// VERIFY ANSWER
				for (Choice choice: question.getChoices()) {
					List<SingleQuestionResponse> choiceAnswered = this._responseManager.aggregateResponseByIds(null, null, choice.getId(), null);
					assertNull(choiceAnswered);
				}
			}
			List<SingleQuestionResponse> answered = this._responseManager.aggregateResponseByIds(null, null, null, "Milan");
			assertNull(answered);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._responseManager.deleteResponseBySurvey(survey);
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	private SingleQuestionResponse getFakeResponse() {
		SingleQuestionResponse response = new SingleQuestionResponse();
		response.setQuestionId(1);
		response.setChoiceId(1);
		response.setVoterId(1);
		response.setFreeText("Non è bello ciò che è bello");
		return response;
	}
	
	public void setResponseManager(IResponseManager responseManager) {
		this._responseManager = responseManager;
	}
	public IResponseManager getResponseManager() {
		return _responseManager;
	}

	private IResponseManager _responseManager;
}
