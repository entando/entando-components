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
package org.entando.entando.plugins.jpfrontshortcut.aps.internalservlet.portal;

import com.agiletec.aps.system.ApsSystemUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxyFactory;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import java.util.HashMap;

/**
 * @author E.Santoboni
 */
public class SpecialWidgetWardenInterceptor extends AbstractInterceptor {
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		FrontPageConfigAction action = (FrontPageConfigAction) invocation.getAction();
		ActionProxyFactory proxyFactory = (ActionProxyFactory) invocation.getInvocationContext().getContainer().getInstance(ActionProxyFactory.class);
		try {
			proxyFactory.createActionProxy("/do/jpfrontshortcut/Page/SpecialWidget", action.getShowletAction(), null, new HashMap());
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().error("There is no Action mapped for namespace /do/jpfrontshortcut/Page/SpecialWidget and action name " + action.getShowletAction() + ".");
			return "configSimpleParameter";
		}
		return invocation.invoke();
	}
	
}