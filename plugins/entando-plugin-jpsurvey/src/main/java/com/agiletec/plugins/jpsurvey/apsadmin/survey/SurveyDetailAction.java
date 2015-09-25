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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResource;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.ISurveyManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

public class SurveyDetailAction extends BaseAction implements ISurveyDetailAction {
	
	@Override
	public String view() {
		try {
			Survey survey = this.getSurvey();
			boolean isQuestionnarie = this.getQuestionnaire().booleanValue();
			if (survey == null || survey.isQuestionnaire() != isQuestionnarie) {
				String errorMsg = isQuestionnarie ? "Error.questionnairy.notFound" : "Error.poll.notFound";
				this.addActionError(this.getText(errorMsg));
				return "listSurveys";
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSurvey");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public Survey getSurvey() {
		if (this._survey == null) {
			try {
				this._survey = this.getSurveyManager().loadSurvey(this.getSurveyId());
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getSurvey");
				throw new RuntimeException("Error loading survey" + this.getSurveyId(), t);
			}
		}
		return this._survey;
	}
	
	public Integer getTotalVoters() {
		Integer voters = new Integer(0);
		try {
			List<Integer> votersId = this.getVoterManager().searchVotersByIds(null, null, null, null, null, this.getSurveyId(), null);
			if (null == votersId) return voters;
			voters = new Integer(votersId.size());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getTotalVoters");
			throw new RuntimeException("getTotalVoters surveyId " + this.getSurveyId(), t);
		}
		return voters;
	}
	
	public Map<Integer, Integer> getQuestionStatistics(Integer questionId) {
		Map<Integer, Integer> stats = null;
		try {
			stats = this.getResponseManager().loadQuestionStatistics(questionId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getQuestionStatistics");
			throw new RuntimeException("getQuestionStatistics id " + questionId, t);
		}
		return stats;
	}
	
	public double getChoicePercentage(Map<Integer, Integer> questionStats, Integer choiceId) {
		int sum = 0;
		Iterator<Integer> iter = questionStats.values().iterator();
		while (iter.hasNext()) {
			Integer integer = (Integer) iter.next();
			sum += integer;
		}
		int occurrence = questionStats.get(choiceId);
		double percent = (((double) occurrence)/((double) sum))*100;
		return percent;
	}
	
	public ImageResource getImage() {
		if (this._image == null) {
			try {
				String imageId = this.getSurvey().getImageId();
				this._image = (ImageResource) this.getResourceManager().loadResource(imageId);
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getImage");
				throw new RuntimeException("Error loading the image related the the survey", t);
			}
		}
		return this._image;
	}
	
	public String getLabel(ApsProperties labels) {
		String label = labels.getProperty(this.getCurrentLang().getCode());
		if (label == null || label.trim().length() == 0) {
			Lang defaultLang = this.getLangManager().getDefaultLang();
			label = labels.getProperty(defaultLang.getCode());
		}
		return label;
	}
	
	public Integer getSurveyId() {
		return _surveyId;
	}
	public void setSurveyId(Integer surveyId) {
		this._surveyId = surveyId;
	}
	
	public Boolean getQuestionnaire() {
		return _questionnaire;
	}
	public void setQuestionnaire(Boolean questionnaire) {
		this._questionnaire = questionnaire;
	}
	
	public ISurveyManager getSurveyManager() {
		return _surveyManager;
	}
	public void setSurveyManager(ISurveyManager surveyManager) {
		this._surveyManager = surveyManager;
	}
	
	protected IVoterManager getVoterManager() {
		return _voterManager;
	}
	public void setVoterManager(IVoterManager voterManager) {
		this._voterManager = voterManager;
	}
	
	protected IResponseManager getResponseManager() {
		return _responseManager;
	}
	public void setResponseManager(IResponseManager responseManager) {
		this._responseManager = responseManager;
	}
	
	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}
	public IResourceManager getResourceManager() {
		return _resourceManager;
	}
	
	private Survey _survey;
	private ImageResource _image;
	
	private Integer _surveyId;
	private Boolean _questionnaire;
	
	private ISurveyManager _surveyManager;
	private IVoterManager _voterManager;
	private IResponseManager _responseManager;
	private IResourceManager _resourceManager;
}