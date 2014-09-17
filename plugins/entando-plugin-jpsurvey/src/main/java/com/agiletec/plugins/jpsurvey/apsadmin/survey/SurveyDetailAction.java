/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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