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