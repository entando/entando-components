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

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.ISurveyManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.SurveyRecord;
import com.agiletec.plugins.jpsurvey.apsadmin.survey.helper.SurveyResourceFinderAction;

public class SurveyImageAction extends SurveyResourceFinderAction implements ISurveyImageAction {
	
	@Override
	public String associateSurveyImage() {
		SurveyRecord survey = null;
		try {
			if (null != this.getSurveyId()) {
				survey = this.getSurveyManager().loadSurvey(this.getSurveyId());
				if (null != survey) {
					this.setTitles(survey.getTitles());
				}
			} else {
				this.addActionError(this.getText("message.surveyAction.sysError", new String[]{"surveyId"}));
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "associateSurveyImage");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String joinImage() {
		Survey survey = null;
		try {
			survey = this.getSurveyManager().loadSurvey(this.getSurveyId());
			if (null != survey) {
				survey.setImageId(this.getResourceId());
				this.getSurveyManager().updateSurvey(survey);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "joinImage");
			return FAILURE;
		}
		return SUCCESS;		
	}

	public String getDefaultLangCode() {
		return this.getLangManager().getDefaultLang().getCode();
	}
	
	public void setSurveyId(Integer surveyId) {
		this._surveyId = surveyId;
	}
	public Integer getSurveyId() {
		return _surveyId;
	}
	public void setQuestionnaire(Boolean questionnaire) {
		this._questionnaire = questionnaire;
	}
	public Boolean getQuestionnaire() {
		return _questionnaire;
	}
	
	public void setSurveyManager(ISurveyManager surveyManager) {
		this._surveyManager = surveyManager;
	}
	public ISurveyManager getSurveyManager() {
		return _surveyManager;
	}

	public void setResourceId(String resourceId) {
		this._resourceId = resourceId;
	}
	public String getResourceId() {
		return _resourceId;
	}
	
	public void setTitles(ApsProperties titles) {
		this._titles = titles;
	}
	public ApsProperties getTitles() {
		return _titles;
	}

	private Integer _surveyId;
	private Boolean _questionnaire;
	private String _resourceId;
	
	private ApsProperties _titles = new ApsProperties(); 
	
	private ISurveyManager _surveyManager;
}
