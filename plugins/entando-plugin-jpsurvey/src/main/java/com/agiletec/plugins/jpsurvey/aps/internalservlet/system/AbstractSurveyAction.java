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
