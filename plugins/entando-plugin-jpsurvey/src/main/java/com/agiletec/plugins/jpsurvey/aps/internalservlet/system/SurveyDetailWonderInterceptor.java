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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpsurvey.aps.system.services.SurveySystemConstants;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.ISurveyManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.SurveyRecord;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * @author E.Santoboni
 */
public class SurveyDetailWonderInterceptor extends AbstractSurveyWondenInterceptor {
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String surveyIdString = request.getParameter("surveyId");
		int surveyId = 0;
		try {
			surveyId = Integer.parseInt(surveyIdString);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "intercept", "Error parsing idSurvey '" + surveyIdString + "'");
		}
		
		ISurveyManager surveyManager = (ISurveyManager) ApsWebApplicationUtils.getBean(SurveySystemConstants.SURVEY_MANAGER, request);
		SurveyRecord surveyInfo = surveyManager.loadSurvey(surveyId);
		if (null == surveyInfo) {
			return "nullRequiredSurvey";
		}
		
		//SE NON ABILITATO A GRUPPO, ESCI
		String checkGroup = this.checkSurveyGroup(surveyInfo, request);
		if (null != checkGroup) return checkGroup;
		
		Date today = new Date();
		boolean started = (today.getTime() > surveyInfo.getStartDate().getTime());
		
		//SE NON INIZIATO, ESCI
		if (!started) return "surveyNotBegunYet";
		
		boolean expired = (null != surveyInfo.getEndDate() && today.getTime() > surveyInfo.getEndDate().getTime());
		
		//SE ATTIVO, non scaduto E NON ABILITATO A RISULTATI PARZIALI, ESCI
		if (started && !expired && !surveyInfo.isPublicPartialResult()) {
			return "partialResultNotAllowed";
		}
		
		//SE SCADUTO E NON ABILITATO A RISULTATI TOTALI, ESCI
		if (expired && !surveyInfo.isPublicResult()) {
			return "finalResultNotAllowed";
		}
		
		return invocation.invoke();
	}	
}