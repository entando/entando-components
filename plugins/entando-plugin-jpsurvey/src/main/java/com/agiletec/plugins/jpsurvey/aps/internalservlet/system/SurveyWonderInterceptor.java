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
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpsurvey.aps.internalservlet.ApsAdminSurveySystemConstants;
import com.agiletec.plugins.jpsurvey.aps.internalservlet.model.CurrentVotingInfoBean;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.VoterResponse;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * @author E.Santoboni
 */
public class SurveyWonderInterceptor extends AbstractSurveyWondenInterceptor {
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		VoterResponse voterResponse = this.getVoterResponse(request);
		if (null == voterResponse) return "nullVoterResponse";
		
		Survey survey = voterResponse.getSurvey();
		Date today = new Date();
		boolean started = (today.getTime() > survey.getStartDate().getTime());
		//SE NON INIZIATO, ESCI
		if (!started) return "surveyNotBegunYet";
		boolean expired = (null != survey.getEndDate() && today.getTime() > survey.getEndDate().getTime());
		//SE FINITO ESCI
		if (expired) return "expiredSurvey";
		
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		if (survey.isCheckUsername() && (null == currentUser || currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME))) {
			return "userNotAllowedToSurvey";
		}
		
		String checkGroup = this.checkSurveyGroup(survey, request);
		if (null != checkGroup) return checkGroup;
		
		try {
			boolean voted = CheckVotingUtil.isSurveyVoted(survey, request);
			if (voted) return "surveyAlreadyVoted";
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "intercept", "Error checking the  the vote for survey " + survey.getId());
			throw new Exception("Error checking the  the vote for survey " + survey.getId(), t);
		}
		
		return invocation.invoke();
	}
	
	protected VoterResponse getVoterResponse(HttpServletRequest request) {
		CurrentVotingInfoBean infoBean = this.getCurrentVotingInfoBean(request);
		if (null == infoBean) return null;
		return infoBean.getVoterResponse();
	}
	
	protected CurrentVotingInfoBean getCurrentVotingInfoBean(HttpServletRequest request) {
		return (CurrentVotingInfoBean) request.getSession().getAttribute(ApsAdminSurveySystemConstants.SURVEY_CURRENT_VOTING_INFO_SESSION_PARAM);
	}

}
