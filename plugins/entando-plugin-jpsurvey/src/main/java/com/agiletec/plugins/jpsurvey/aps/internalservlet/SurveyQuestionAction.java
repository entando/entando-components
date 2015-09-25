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
package com.agiletec.plugins.jpsurvey.aps.internalservlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpsurvey.aps.internalservlet.model.CurrentVotingInfoBean;
import com.agiletec.plugins.jpsurvey.aps.internalservlet.system.AbstractSurveyAction;
import com.agiletec.plugins.jpsurvey.aps.internalservlet.system.CheckVotingUtil;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.VoterResponse;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.SingleQuestionResponse;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

/**
 * @author E.Santoboni
 */
public class SurveyQuestionAction extends AbstractSurveyAction implements ISurveyQuestionAction, ServletResponseAware {
	
	@Override
	public void validate() {
		super.validate();
		Question question = this.getCurrentQuestion();
		if (question.isSingleChoice()) {
			this.checkSingleChoiseQuestion(question);
		} else {
			if (this.getChoiceIds().size() < question.getMinResponseNumber()) {
					String[] args = {String.valueOf(question.getMinResponseNumber())};
					this.addActionError(this.getText("jpsurvey.front.wrongMinResponseNumber", args));	
			}
			if (this.getChoiceIds().size() > question.getMaxResponseNumber()) {
				String[] args = {String.valueOf(question.getMaxResponseNumber())};
				this.addActionError(this.getText("jpsurvey.front.wrongMaxResponseNumber", args));
			}
			for (int i=0; i<this.getChoiceIds().size(); i++) {
				Integer choiseId = this.getChoiceIds().get(i);
				this.checkSingleResponse(question, choiseId);
			}
		}
	}
	
	private void checkSingleChoiseQuestion(Question question) {
		if (this.getChoiceIds().isEmpty()) {
			this.addActionError(this.getText("jpsurvey.front.minResponse"));
		} else if (this.getChoiceIds().size()>1) {
			this.addActionError(this.getText("jpsurvey.front.oneResponse"));
		} else {
			Integer choiseId = this.getChoiceIds().get(0);
			this.checkSingleResponse(question, choiseId);
		}
	}
	
	private void checkSingleResponse(Question question, Integer choiseId) {
		Choice choise = question.getChoice(choiseId);
		if (null == choise) {
			this.addActionError(this.getText("jpsurvey.front.notCompatibleResponse"));
			ApsSystemUtils.getLogger().error("Single response'" + choiseId + 
					"' non compatibile con quelle ammesse in Domanda '" + question.getId() + 
					"' di questionario '" + this.getVoterResponse().getSurvey().getId() + "'");
		} else {
			if (choise.isFreeText() && (null == this.getInsertedFreeText() || this.getInsertedFreeText().trim().length() == 0)) {
				this.addActionError(this.getText("jpsurvey.front.freeTextRequired"));
			}
		}
	}
	
	@Override
	public String initQuestion() {
		try {
			this.setInsertedFreeText(null);
			this.setChoiceIds(new ArrayList<Integer>());
			CurrentVotingInfoBean currentVotingInfoBean = this.getCurrentVotingInfoBean();
			int currentIndexInt = this.getCurrentQuestionIndex().intValue()+1;
			if (currentIndexInt != currentVotingInfoBean.getCurrentQuestionIndex()) {
				//Check REFRESH
				return SUCCESS;
			}
			Survey survey = this.getVoterResponse().getSurvey();
			if (survey.getQuestions().size() == currentIndexInt) {
				this.getResponseManager().saveVoterResponse(this.getVoterResponse());
				this.addCookieVoting(survey.getId());
				this.getRequest().getSession().removeAttribute(ApsAdminSurveySystemConstants.SURVEY_CURRENT_VOTING_INFO_SESSION_PARAM);
				this.getRequest().getSession().removeAttribute(ApsAdminSurveySystemConstants.SURVEY_CURRENT_QUESTION_INDEX_SESSION_PARAM);
				this.setSurveyId(survey.getId());
				return "submitSurvey";
			}
			Integer currentIndex = new Integer(currentIndexInt);
			this.getRequest().getSession().setAttribute(ApsAdminSurveySystemConstants.SURVEY_CURRENT_QUESTION_INDEX_SESSION_PARAM, currentIndex);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "initQuestion", "Error inizialing question");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected void addCookieVoting(int surveyId) {
		UserDetails currentUser = this.getCurrentUser();
		String cookieName = CheckVotingUtil.getCookieName(currentUser.getUsername(), surveyId);
		String cookieValue = CheckVotingUtil.getCookieValue(currentUser.getUsername(), surveyId);
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(365*24*60*60);//one year
		this.getResponse().addCookie(cookie);
	}
	
	@Override
	public String saveResponse() {
		try {
			CurrentVotingInfoBean currentVotingInfoBean = this.getCurrentVotingInfoBean();
			int currentIndexInt = this.getCurrentQuestionIndex().intValue();
			if (currentIndexInt != currentVotingInfoBean.getCurrentQuestionIndex()) {
				//Check REFRESH
				return SUCCESS;
			}
			Question question = this.getCurrentQuestion();
			for (int i=0; i<this.getChoiceIds().size(); i++) {
				Integer choiceId = this.getChoiceIds().get(i);
				SingleQuestionResponse singleResponse = new SingleQuestionResponse();
				singleResponse.setChoiceId(choiceId);
				singleResponse.setQuestionId(question.getId());
				Choice choice = question.getChoice(choiceId);
				if (choice.isFreeText()) {
					singleResponse.setFreeText(this.getInsertedFreeText().trim());
				}
				currentVotingInfoBean.getVoterResponse().getResponses().add(singleResponse);
			}
			currentVotingInfoBean.setNextIndex();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveResponse", "Error saving response");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String submitSurvey() {
		return SUCCESS;
	}
	
	public Question getCurrentQuestion() {
		int currentIndexInt = this.getCurrentQuestionIndex().intValue();
		List<Question> questions = this.getVoterResponse().getSurvey().getQuestions();
		return questions.get(currentIndexInt);
	}
	
	public VoterResponse getVoterResponse() {
		return this.getCurrentVotingInfoBean().getVoterResponse();
	}
	
	public CurrentVotingInfoBean getCurrentVotingInfoBean() {
		return (CurrentVotingInfoBean) this.getRequest().getSession().getAttribute(ApsAdminSurveySystemConstants.SURVEY_CURRENT_VOTING_INFO_SESSION_PARAM);
	}
	
	public Integer getCurrentQuestionIndex() {
		return (Integer) this.getRequest().getSession().getAttribute(ApsAdminSurveySystemConstants.SURVEY_CURRENT_QUESTION_INDEX_SESSION_PARAM);
	}
	
	public List<Integer> getChoiceIds() {
		return _choiceIds;
	}
	public void setChoiceIds(List<Integer> choiceIds) {
		this._choiceIds = choiceIds;
	}
	
	public String getInsertedFreeText() {
		return _insertedFreeText;
	}
	public void setInsertedFreeText(String insertedFreeText) {
		this._insertedFreeText = insertedFreeText;
	}
	
	protected IResponseManager getResponseManager() {
		return _responseManager;
	}
	public void setResponseManager(IResponseManager responseManager) {
		this._responseManager = responseManager;
	}
	protected HttpServletResponse getResponse() {
		return this._response;
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this._response = response;
	}
	
	private IResponseManager _responseManager;
	
	private List<Integer> _choiceIds = new ArrayList<Integer>();
	private String _insertedFreeText;
	
	private HttpServletResponse _response;
	
}