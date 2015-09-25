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
