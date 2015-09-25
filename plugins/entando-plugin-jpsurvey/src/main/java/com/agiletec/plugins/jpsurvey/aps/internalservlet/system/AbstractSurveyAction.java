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
package com.agiletec.plugins.jpsurvey.aps.internalservlet.system;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResource;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.ISurveyManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.SurveyRecord;

/**
 * @author E.Santoboni
 */
public abstract class AbstractSurveyAction extends BaseAction {
	
	public String getLabel(ApsProperties labels) {
		String label = labels.getProperty(this.getCurrentLang().getCode());
		if (label == null || label.trim().length() == 0) {
			Lang defaultLang = this.getLangManager().getDefaultLang();
			label = labels.getProperty(defaultLang.getCode());
		}
		return label;
	}
	
	public Lang getCurrentLang() {
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		Lang currentLang = null;
		if (null != reqCtx) {
			currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		} else {
			currentLang = this.getLangManager().getDefaultLang();
		}
		return currentLang;
	}
	
	public SurveyRecord getSurveyInfo() {
		SurveyRecord survey = null;
		try {
			survey = this.getSurveyManager().loadSurvey(this.getSurveyId());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSurveyInfo");
			throw new RuntimeException("Error loading survey: " + this.getSurveyId(), t);
		}
		return survey;
	}
	
	public Boolean getVoted() {
		Boolean voted = new Boolean(false);
		try {
			SurveyRecord surveyInfo = this.getSurveyInfo();
			voted = CheckVotingUtil.isSurveyVoted(surveyInfo, this.getRequest());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getVoted");
			throw new RuntimeException("Error in check vote", t);
		}
		return voted;
	}
	
	public String getSurveyImageURL(String surveyImageId, String dimension) {
		String URL = null;
		try {
			if (null == surveyImageId) return null;
			if (null == dimension) dimension = "0";
			ImageResource image = (ImageResource) this.getResourceManager().loadResource(surveyImageId);
			if (null != image) {
				URL = image.getImagePath(dimension);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSurveyImageURL");
			throw new RuntimeException("Error loading the image for the survey", t);
		}
		return URL;
	}
	
	public Integer getSurveyId() {
		return _surveyId;
	}
	public void setSurveyId(Integer surveyId) {
		this._surveyId = surveyId;
	}
	
	protected ISurveyManager getSurveyManager() {
		return _surveyManager;
	}
	public void setSurveyManager(ISurveyManager surveyManager) {
		this._surveyManager = surveyManager;
	}
	
	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}
	protected IResourceManager getResourceManager() {
		return _resourceManager;
	}

	private Integer _surveyId;
	
	private ISurveyManager _surveyManager;
	private IResourceManager _resourceManager; 

}
