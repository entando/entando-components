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
package com.agiletec.plugins.jpcasclient.aps.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.session.HashMapBackedSessionMappingStorage;
import org.jasig.cas.client.session.SessionMappingStorage;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpcasclient.CasClientPluginSystemCostants;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.ICasClientConfigManager;

/**
 * Implements the Single Sign Out protocol. It handles registering the session and destroying the session.
 * Zuanni's change setting encodnig to UTF8 and minor refactorings like managing plugin disactivation config
 * parameter and entando style logs.
 * @author Scott Battaglia
 * @author zuanni
 * @version $Revision$ $Date$
 * @since 3.1
 */
public class CasSingleSignOutFilter implements Filter {

	private static final Logger _logger =  LoggerFactory.getLogger(CasSingleSignOutFilter.class);
	
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		
	}
	
	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		request.setCharacterEncoding("UTF-8");
		ICasClientConfigManager configManager = (ICasClientConfigManager) ApsWebApplicationUtils.getBean(CasClientPluginSystemCostants.JPCASCLIENT_CONFIG_MANAGER, request);
		boolean isActive = configManager.getClientConfig().isActive();
		if (isActive) {
			if ("POST".equals(request.getMethod())) {
				final String logoutRequest = request.getParameter("logoutRequest");
				if (CommonUtils.isNotBlank(logoutRequest)) {
					_logger.debug("Logout request=[{}]", logoutRequest);
					final String sessionIdentifier = XmlUtils.getTextForElement(logoutRequest, "SessionIndex");
					if (CommonUtils.isNotBlank(sessionIdentifier)) {
						final HttpSession session = SESSION_MAPPING_STORAGE.removeSessionByMappingId(sessionIdentifier);
						if (session != null) {
							String sessionID = session.getId();
							_logger.debug("Invalidating session [{}] for ST [{}]", sessionID, sessionIdentifier);
							try {
								session.invalidate();
							} catch (final IllegalStateException e) {
								_logger.error("error in doFilter", e);
							}
						}
						return;
					}
				}
			} else {
				final String artifact = request.getParameter(this._artifactParameterName);
				final HttpSession session = request.getSession();

				if (session != null) {
					_logger.debug("Storing session identifier for {}", session.getId());
				}
				if (CommonUtils.isNotBlank(artifact)) {
					try {
						SESSION_MAPPING_STORAGE.removeBySessionById(session.getId());
					} catch (final Exception e) {
						// ignore if the session is already marked as invalid.  Nothing we can do!
					}
					SESSION_MAPPING_STORAGE.addSessionById(artifact, session);
				}
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	public void setSessionMappingStorage(final SessionMappingStorage storage) {
		SESSION_MAPPING_STORAGE = storage;
	}

	public static SessionMappingStorage getSessionMappingStorage() {
		return SESSION_MAPPING_STORAGE;
	}
	
	@Override
	public void destroy() {
		// nothing to do
	}
	
	/**
	 * The name of the artifact parameter.  This is used to capture the session identifier.
	 */
	private final static String _artifactParameterName = "ticket";
	
	private static SessionMappingStorage SESSION_MAPPING_STORAGE = new HashMapBackedSessionMappingStorage();
	
}