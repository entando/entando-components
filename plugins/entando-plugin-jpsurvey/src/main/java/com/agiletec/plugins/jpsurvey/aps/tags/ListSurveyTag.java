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
