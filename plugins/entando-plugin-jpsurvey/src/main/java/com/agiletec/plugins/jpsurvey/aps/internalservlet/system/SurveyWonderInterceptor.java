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
