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
package org.entando.entando.plugins.jpfrontshortcut.apsadmin.system;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * Interceptor per la gestione degli "Aps Ajax Action Params", 
 * parametri inseriti nei nomi delle action invocate.
 * L'interceptor intercetta i parametri il cui nome è strutturato secondo la sintassi:<br />
 * &#60;ACTION_NAME&#62;@&#60;PARAM_NAME_1&#62;=&#60;PARAM_VALUE_1&#62;;&#60;PARAM_NAME_2&#62;=&#60;PARAM_VALUE_2&#62;;....;&#60;PARAM_NAME_N&#62;=&#60;PARAM_VALUE_N&#62;
 * <br />
 * L'interceptor effettua il parsing della stringa inserendo i parametri estratti nella richiesta corrente 
 * in maniera tale che vengano intercettati dal successivo "Parameters Interceptor" di default del sistema.
 * @author E.Santoboni
 */
public class ApsAjaxActionParamsInterceptor extends AbstractInterceptor {
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String apsParam =  request.getRequestURI();
		if (apsParam.indexOf("@") >= 0) {
			if (apsParam.endsWith(".x") || apsParam.endsWith(".y")) {
            	apsParam =  apsParam.substring(0, apsParam.length()-2);
            }
			if (apsParam.endsWith(".action")) {
            	apsParam =  apsParam.substring(0, apsParam.length() - ".action".length());
            }
			this.createApsActionParam(apsParam, invocation);
		}
        return invocation.invoke();
	}
	
	private void createApsActionParam(String apsParam, ActionInvocation invocation) {
		String[] blocks = apsParam.split("[@]");
		if (blocks.length == 2) {
			String paramBlock = blocks[1];
			String[] params = paramBlock.split(";");
			for (int i=0; i<params.length; i++) {
				Map parameters = invocation.getInvocationContext().getParameters();
				HttpServletRequest request = ServletActionContext.getRequest();
				String[] parameter = params[i].split("=");
				if (parameter.length == 2) {
					request.setAttribute(parameter[0], parameter[1]);
					parameters.put(parameter[0], parameter[1]);
				}
			}
		}
	}
	
}
