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