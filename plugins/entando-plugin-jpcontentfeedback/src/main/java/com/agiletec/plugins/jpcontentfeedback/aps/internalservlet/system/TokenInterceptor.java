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
package com.agiletec.plugins.jpcontentfeedback.aps.internalservlet.system;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

/**
 * This interceptor can make sure that back buttons and double clicks don't cause un-intended side affects.
 * For example, you can use this to prevent careless users who might double click on a "checkout" button at an online store.
 * This interceptor uses a fairly primitive technique for when an invalid token is found: it returns the result invalid.token.
 *
 * TypeMessages parameter to specify the type of message to associate at invalid.token result
 * The values of type are the following:
 * 	  error: return an action error message
 * 	  message: return an action message
 * 	  none: don't return message
 * 
 * @author D.Cherchi
 */
public class TokenInterceptor extends org.apache.struts2.interceptor.TokenInterceptor {
	
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
        	if (this.getTypeMessages().equalsIgnoreCase(TYPE_RETURN_ACTION_ERROR_MESSAGE)){
        		((ValidationAware) action).addActionError(errorMessage);
        	} else if (this.getTypeMessages().equalsIgnoreCase(TYPE_RETURN_ACTION_MESSAGE)){
        		((ValidationAware) action).addActionMessage(message);
        	} else if (this.getTypeMessages().equalsIgnoreCase(TYPE_RETURN_NONE_MESSAGE)){
				//nothing to do
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
