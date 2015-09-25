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
package com.agiletec.plugins.jpcrowdsourcing.aps.internalservlet.idea.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;

public class CookieUtil {

	public static String getCookieName(String username, String ideaId) {
		return "collaboration_" + username + "_" + ideaId + "_NAME";
	}
	public static String getCookieValue(String username, String ideaId) {
		return "collaboration_" + username + "_" + ideaId + "_VALUE";
	}
	
	public static boolean isIdeaVoted(String ideaId, HttpServletRequest request) throws Throwable {
		boolean checkCookie = false;
		checkCookie = isVotedByCookie(ideaId, request);
		return checkCookie;
	}
	
	private static boolean isVotedByCookie(String ideaId, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (null == cookies) return false;
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		String expectedCookieName = getCookieName(currentUser.getUsername(), ideaId);
		String expectedCookieValue = getCookieValue(currentUser.getUsername(), ideaId);
		for (int i=0; i<cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie.getName().equals(expectedCookieName) && cookie.getValue().equals(expectedCookieValue)) {
				return true;
			}
		}
		return false;
	}

}
