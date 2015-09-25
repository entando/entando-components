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