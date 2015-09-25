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
package com.agiletec.plugins.jpcrowdsourcing.aps.tags;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.JpCrowdSourcingSystemConstants;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.StatisticInfoBean;

public class CrowdsourcingStatisticTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(CrowdsourcingStatisticTag.class);

	@Override
	public int doEndTag() throws JspException {
		try {
			IIdeaManager ideaManager = (IIdeaManager) ApsWebApplicationUtils.getBean(JpCrowdSourcingSystemConstants.IDEA_MANAGER, this.pageContext);
			StatisticInfoBean statisticInfoBean = null;
			if (StringUtils.isNotBlank(this.getInstanceCode())) {
				statisticInfoBean = ideaManager.getActiveIdeaStatistics(this.getInstanceCode());
			} else {
				UserDetails currentUser = (UserDetails) pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
				if (currentUser == null) {
					IUserManager userManager = (IUserManager) ApsWebApplicationUtils.getBean(SystemConstants.USER_MANAGER, pageContext);
					currentUser = userManager.getGuestUser();
				}
				IAuthorizationManager authManager = (IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, pageContext);
				List<Group> groups = authManager.getUserGroups(currentUser);
				List<String> groupsColl = new ArrayList<String>();
				if (null != groups) {
					for (int i = 0; i< groups.size(); i++) {
						groupsColl.add(groups.get(i).getName());
					}
				}
				if (!groupsColl.contains(Group.FREE_GROUP_NAME)) groupsColl.add(Group.FREE_GROUP_NAME);
				statisticInfoBean = ideaManager.getActiveIdeaStatistics(groupsColl);
			}
			this.pageContext.setAttribute(this.getVar(), statisticInfoBean);
		} catch (Throwable t) {
			_logger.error("error in doEndTag", t);
			throw new JspException("Errore tag", t);
		}
		this.release();
		return super.doEndTag();
	}


	@Override
	public void release() {
		super.release();
		this._instanceCode = null;
		this._var = null;
	}

	public void setVar(String var) {
		this._var = var;
	}
	public String getVar() {
		return _var;
	}

	public String getInstanceCode() {
		return _instanceCode;
	}
	public void setInstanceCode(String instanceCode) {
		this._instanceCode = instanceCode;
	}

	private String _instanceCode;
	private String _var;

}
