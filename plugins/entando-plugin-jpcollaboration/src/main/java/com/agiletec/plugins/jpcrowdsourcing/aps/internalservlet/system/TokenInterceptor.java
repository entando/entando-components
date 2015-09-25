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
package com.agiletec.plugins.jpcrowdsourcing.aps.internalservlet.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

public class TokenInterceptor extends org.apache.struts2.interceptor.TokenInterceptor {

	private static final Logger _logger =  LoggerFactory.getLogger(TokenInterceptor.class);

	@Override
    protected String handleInvalidToken(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        String errorMessage = LocalizedTextUtil.findText(this.getClass(), "struts.messages.invalid.token",
                invocation.getInvocationContext().getLocale(),
                "The form has already been processed or no token was supplied, please try again.", new Object[0]);

        String message = LocalizedTextUtil.findText(this.getClass(), "struts.messages.invalid.token.message",
                invocation.getInvocationContext().getLocale(),
                "Stop double-submission of forms.", new Object[0]);

        if (action instanceof ValidationAware) {
        	if (this.getTypeMessages().equalsIgnoreCase(TYPE_RETURN_ACTION_ERROR_MESSAGE)) {
        		((ValidationAware) action).addActionError(errorMessage);
        	} else if (this.getTypeMessages().equalsIgnoreCase(TYPE_RETURN_ACTION_MESSAGE)) {
        		((ValidationAware) action).addActionMessage(message);
        	} else if (this.getTypeMessages().equalsIgnoreCase(TYPE_RETURN_NONE_MESSAGE)) {
        		_logger.debug("TokenInterceptor without message for class {}", invocation.getAction().getClass().getName());
        	}
        } else {
            log.warn(errorMessage);
        }

        return INVALID_TOKEN_CODE;
    }


	public void setTypeMessages(String typeMessages) {
		this._typeMessages = typeMessages;
	}

	public String getTypeMessages() {
		return _typeMessages;
	}

	private String _typeMessages;

	public static final String TYPE_RETURN_ACTION_ERROR_MESSAGE = "error";
	public static final String TYPE_RETURN_ACTION_MESSAGE = "message";
	public static final String TYPE_RETURN_NONE_MESSAGE = "none";




}
