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
package com.agiletec.plugins.jpsurvey.aps.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResource;
import com.agiletec.plugins.jpsurvey.aps.internalservlet.system.CheckVotingUtil;
import com.agiletec.plugins.jpsurvey.aps.system.services.SurveySystemConstants;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.ISurveyManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.SurveyRecord;

public class LoadSurveyTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(LoadSurveyTag.class);
	
	public int doStartTag() throws JspException {
		ISurveyManager surveyManager = (ISurveyManager) ApsWebApplicationUtils.getBean(SurveySystemConstants.SURVEY_MANAGER, pageContext);
		ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER,pageContext);
		IResourceManager resourceManager = (IResourceManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.RESOURCE_MANAGER, this.pageContext);
		SurveyRecord survey = null;	
		try {
			String defaultLang = langManager.getDefaultLang().getCode();
			survey = surveyManager.loadSurvey(this.getSurveyId());
			if (this._preferredLang == null) {
				this._preferredLang = defaultLang;
			}
			this.pageContext.setAttribute(this.getCtxName(), survey);
			// fetch image
			if (null != this.getCtxImageUrl() && null != survey.getImageId()) {
				ImageResource resource = (ImageResource) resourceManager.loadResource(survey.getImageId());
				if (null != this.getImageDimension() && null != resource.getImagePath(this.getImageDimension())) {
					this.pageContext.setAttribute(this.getCtxImageUrl(), resource.getImagePath(this.getImageDimension()));
				} else {
					this.pageContext.setAttribute(this.getCtxImageUrl(), resource.getImagePath("0"));
				}
			}
			if (null != this.getVotedParamName()) {
				boolean voted = CheckVotingUtil.isSurveyVoted(survey, (HttpServletRequest) this.pageContext.getRequest());
				this.pageContext.setAttribute(this.getVotedParamName(), new Boolean(voted));
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error in tag \"LoadSurveyTag\"", t);
		}
		return SKIP_BODY;
	}
	
	
	@Override
	public void release() {
		super.release();
		this.setCtxName(null);
		this.setSurveyId(null);
		this.setPreferredLang(null);
		this.setVotedParamName(null);
	}
	
	public void setSurveyId(Integer surveyId) {
		this._surveyId = surveyId;
	}
	public Integer getSurveyId() {
		return _surveyId;
	}
	
	public void setCtxName(String ctxName) {
		this._ctxName = ctxName;
	}
	public String getCtxName() {
		return _ctxName;
	}
	
	public void setPreferredLang(String preferredLang) {
		this._preferredLang = preferredLang;
	}
	public String getPreferredLang() {
		return _preferredLang;
	}
	
	public String getVotedParamName() {
		return _votedParamName;
	}
	public void setVotedParamName(String votedParamName) {
		this._votedParamName = votedParamName;
	}
	
	public void setCtxImageUrl(String ctxImageUrl) {
		this._ctxImageUrl = ctxImageUrl;
	}
	public String getCtxImageUrl() {
		return _ctxImageUrl;
	}

	public void setImageDimension(String imageDimension) {
		this._imageDimension = imageDimension;
	}
	public String getImageDimension() {
		return _imageDimension;
	}

	private String _preferredLang;
	private Integer _surveyId;
	private String _ctxName;
	private String _ctxImageUrl;
	private String _imageDimension;
	
	private String _votedParamName;
	
}
