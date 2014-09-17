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

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpsurvey.aps.system.services.SurveySystemConstants;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.Voter;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.SurveyRecord;

/**
 * @author E.Santoboni
 */
public class CheckVotingUtil {

	public static boolean isSurveyVoted(SurveyRecord surveyInfo, HttpServletRequest request) throws Throwable {
		boolean checkCookie = false;
		if (surveyInfo.isCheckCookie()) {
			checkCookie = isVotedByCookie(surveyInfo, request);
		}
		boolean checkIpAddress = false;
		if (surveyInfo.isCheckIpAddress()) {
			checkIpAddress = isVotedByIpAddressAndUser(surveyInfo, request);
		}
		boolean checkUsername = false;
		if (surveyInfo.isCheckUsername()) {
			checkUsername = isVotedByUser(surveyInfo, request);
		}
		return (checkCookie || checkIpAddress || checkUsername);
	}

	private static boolean isVotedByCookie(SurveyRecord surveyInfo, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (null == cookies) return false;
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		String expectedCookieName = getCookieName(currentUser.getUsername(), surveyInfo.getId());
		String expectedCookieValue = getCookieValue(currentUser.getUsername(), surveyInfo.getId());
		for (int i=0; i<cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie.getName().equals(expectedCookieName) && cookie.getValue().equals(expectedCookieValue)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isVotedByIpAddressAndUser(SurveyRecord surveyInfo, HttpServletRequest request) throws ApsSystemException {
		String remoteAddress = request.getRemoteAddr();
		IVoterManager voterManager = (IVoterManager) ApsWebApplicationUtils.getBean(SurveySystemConstants.SURVEY_VOTER_MANAGER, request);
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		Voter voter = voterManager.getVoter(currentUser.getUsername(), remoteAddress, surveyInfo.getId());
		return (voter != null);
	}
	private static boolean isVotedByUser(SurveyRecord surveyInfo, HttpServletRequest request) throws ApsSystemException {
		//String remoteAddress = request.getRemoteAddr();
		IVoterManager voterManager = (IVoterManager) ApsWebApplicationUtils.getBean(SurveySystemConstants.SURVEY_VOTER_MANAGER, request);
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);

		FieldSearchFilter usernameFilter = new FieldSearchFilter((IVoterManager.FIELD_USERNAME), currentUser.getUsername(), false);
		FieldSearchFilter surveyIdFilter = new FieldSearchFilter((IVoterManager.FIELD_SURVEY_ID), new Integer(surveyInfo.getId()), false);
		FieldSearchFilter[] filters = new FieldSearchFilter[]{usernameFilter, surveyIdFilter};

		List<Integer> voters = voterManager.searchVoters(filters);
		return (null != voters && !voters.isEmpty());
	}

	public static String getCookieName(String username, int surveyId) {
		return "jpsurvey_" + username + "_" + surveyId + "_NAME";
	}

	public static String getCookieValue(String username, int surveyId) {
		return "jpsurvey_" + username + "_" + surveyId + "_VALUE";
	}

}