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
package com.agiletec.plugins.jpcrowdsourcing.apsadmin.system;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LoggeduserInterceptor extends AbstractInterceptor {

	private static final Logger _logger =  LoggerFactory.getLogger(LoggeduserInterceptor.class);

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		boolean isAuthorized = false;
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			isAuthorized = null != currentUser && !currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME);
			if (isAuthorized) {
				return this.invoke(invocation);
			}
		} catch (Throwable t) {
			_logger.error("Error occurred verifying logged user", t);
		}
		return this.getErrorResultName();
	}
	
	public String getErrorResultName() {
		if (this._errorResultName == null) {
			return DEFAULT_ERROR_RESULT;
		}
		return this._errorResultName;
	}
	
	public void setErrorResultName(String errorResultName) {
		this._errorResultName = errorResultName;
	}
	
	/**
	 * Invokes the next step in processing this ActionInvocation. 
	 * @see com.opensymphony.xwork2.ActionInvocation#invoke()
	 * @return The code of the execution result.
	 */
	protected String invoke(ActionInvocation invocation) throws Exception {
		return invocation.invoke();
	}
	
	private String _errorResultName;
	
	public static final String DEFAULT_ERROR_RESULT = "userNotAllowed";
	
}
