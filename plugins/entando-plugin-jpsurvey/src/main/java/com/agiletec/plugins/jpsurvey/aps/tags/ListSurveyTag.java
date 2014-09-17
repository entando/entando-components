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

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpsurvey.aps.system.services.SurveySystemConstants;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.ISurveyManager;

/**
 * This tag is used to get a list of the active surveys (both questionnairs and polls). 
 */
public class ListSurveyTag  extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(ListSurveyTag.class);
	
	public int doStartTag() throws JspException {
		try {
			UserDetails currentUser = (UserDetails) this.pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);			
			ISurveyManager surveyManager = (ISurveyManager) ApsWebApplicationUtils.getBean(SurveySystemConstants.SURVEY_MANAGER, pageContext);
			boolean isQuestionnaire = this.getCategory() != null && this.getCategory().equals("questionnaire");
			List<Integer> list = surveyManager.getActiveSurveyByUser(currentUser, isQuestionnaire, this.isExpired());
			this.pageContext.setAttribute(this.getCtxName(), list);
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error in tag \"SurveyListTag\"", t);
		}
		return SKIP_BODY;
	}
	
	@Override
	public void release() {
		super.release();
		this.setCategory(null);
		this.setCtxName(null);
		this.setExpired(false);
	}
	
	public void setCtxName(String ctxName) {
		this._ctxName = ctxName;
	}
	public String getCtxName() {
		return _ctxName;
	}
	
	public void setCategory(String category) {
		this._category = category;
	}
	public String getCategory() {
		return _category;
	}
	
	public boolean isExpired() {
		return _expired;
	}
	public void setExpired(boolean expired) {
		this._expired = expired;
	}
	
	private String _ctxName;
	private String _category;
	private boolean _expired;
	
}
