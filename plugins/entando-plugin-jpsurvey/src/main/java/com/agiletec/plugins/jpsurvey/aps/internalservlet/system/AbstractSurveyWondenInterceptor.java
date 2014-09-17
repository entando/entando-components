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
package com.agiletec.plugins.jpsurvey.aps.internalservlet.system;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.SurveyRecord;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * @author E.Santoboni
 */
public abstract class AbstractSurveyWondenInterceptor extends AbstractInterceptor {
	
	protected String checkSurveyGroup(SurveyRecord survey, HttpServletRequest request) {
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		IAuthorizationManager authManager = (IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, ServletActionContext.getRequest());
		if (!survey.getGroupName().equals(Group.FREE_GROUP_NAME) 
				&& !authManager.isAuthOnGroup(currentUser, Group.ADMINS_GROUP_NAME) 
				&& !authManager.isAuthOnGroup(currentUser, survey.getGroupName())) {
			return "userNotAllowedToSurvey";
		}
		return null;
	}
	
}
