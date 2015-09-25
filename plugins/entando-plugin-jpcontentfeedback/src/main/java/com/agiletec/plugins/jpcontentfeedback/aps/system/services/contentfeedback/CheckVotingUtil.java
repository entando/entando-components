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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
/**
 * Classe di utilità per la gestione le operazioni di rating.
 * Effettua tutti i controlli necessari per garantire l'unicità della votazione da perte del singolo
 * utente. Si basa sull'impiego dei cookie, per cui, l'univocità non è garantita in caso di rimozioni
 * dei cookie stessi
 * @author D.Cherchi
 *
 */
public class CheckVotingUtil {

	/**
	 * Verifica se il contentuto è stato già votato
	 * @param contentId L'identificativo del contenuto votato
	 * @param request
	 * @return true se l'utente ha già votato
	 * @throws Throwable
	 */
	public static boolean isContentVoted(String contentId, HttpServletRequest request) throws Throwable {
		boolean checkCookie = false;
		checkCookie = isVotedContentByCookie(contentId, request);
		return checkCookie;
	}

	/**
	 * Verifica se il commento è stato già votato
	 * @param contentId L'identificativo del commento votato
	 * @param request
	 * @return true se l'utente ha già votato
	 * @throws Throwable
	 */
	public static boolean isCommentVoted(int commentId, HttpServletRequest request) throws Throwable {
		boolean checkCookie = false;
		checkCookie = isVotedCommentByCookie(commentId, request);
		return checkCookie;
	}

	private static boolean isVotedContentByCookie(String contentId, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (null == cookies) return false;
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		String expectedCookieName = getCookieName(currentUser.getUsername(),contentId);
		String expectedCookieValue = getCookieValue(currentUser.getUsername(), contentId);
		for (int i=0; i<cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie.getName().equals(expectedCookieName) && cookie.getValue().equals(expectedCookieValue)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isVotedCommentByCookie(int commentId, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (null == cookies) return false;
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		String expectedCookieName = getCookieName(currentUser.getUsername(), commentId);
		String expectedCookieValue = getCookieValue(currentUser.getUsername(), commentId);
		for (int i=0; i<cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie.getName().equals(expectedCookieName) && cookie.getValue().equals(expectedCookieValue)) {
				return true;
			}
		}
		return false;
	}


	public static String getCookieName(String username, String contentId) {
		return "jpcontentfeddback_" + username + "_" + contentId + "_NAME";
	}
	public static String getCookieValue(String username, String contentId) {
		return "jpcontentfeddback_" + username + "_" + contentId + "_VALUE";
	}
	public static String getCookieName(String username, int commentId) {
		return "jpcontentfeddback_" + username + "_" + commentId + "_NAME";
	}
	public static String getCookieValue(String username, int commentId) {
		return "jpcontentfeddback_" + username + "_" + commentId + "_VALUE";
	}

}