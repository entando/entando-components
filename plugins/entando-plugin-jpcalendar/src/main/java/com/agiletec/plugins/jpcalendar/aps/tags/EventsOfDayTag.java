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
package com.agiletec.plugins.jpcalendar.aps.tags;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpcalendar.aps.system.services.CalendarConstants;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.CalendarManager;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.ICalendarManager;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.util.EventsOfDayDataBean;

/**
 * Tag providing identifiers for events of the day.
 * 
 * @author E.Santoboni
 */
public class EventsOfDayTag extends TagSupport implements EventsOfDayDataBean {

	private static final Logger _logger =  LoggerFactory.getLogger(EventsOfDayTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		RequestContext reqCtx = (RequestContext) this.pageContext.getRequest().getAttribute(RequestContext.REQCTX);
		ApsSystemUtils.getLogger().trace("EventsOfDayTag Invoked");
		try {
			this._calMan = (ICalendarManager) ApsWebApplicationUtils.getBean(CalendarConstants.CALENDAR_MANAGER, this.pageContext);
			IAuthorizationManager authorizatorManager = (IAuthorizationManager) 
					ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, this.pageContext);
			this.extractRequiredDate();
			UserDetails currentUser = (UserDetails) reqCtx.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (authorizatorManager.isAuthOnGroup(currentUser, Group.ADMINS_GROUP_NAME)) {
				this.setAllowedGroups(null);
			} else {
				List<Group> userGroups = authorizatorManager.getUserGroups(currentUser);
				Set allowedGroup = new HashSet();
				allowedGroup.add(Group.FREE_GROUP_NAME);
				for (Group group : userGroups) {
					allowedGroup.add(group.getName());
				}
				this.setAllowedGroups(allowedGroup);
			}
			List contents = _calMan.loadEventsOfDayId(this);
			this.pageContext.setAttribute(this.getListName(), contents);
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("error in doStartTag", t);
		}
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public void release() {
		this._listName = null;
		this._calMan = null;
		this._requiredDate = null;
		this._allowedGroups = null;
	}
	
	private void extractRequiredDate() {
		ServletRequest request = this.pageContext.getRequest();
		String stringData = request.getParameter("selectedDate");
		this._requiredDate = DateConverter.parseDate(stringData,
				CalendarManager.REQUIRED_DATE_PATTERN);
		if (this._requiredDate == null) {
			this._requiredDate = (Date) this.pageContext.getSession().getAttribute(
					SESSION_PARAM_LAST_SELECTED_DATE);
			// PRESA DATA DI Ultima richiesta eventi;
		}
		if (this._requiredDate == null) {
			this._requiredDate = new Date();
			// PRESA DATA DI OGGI
		}
		this.pageContext.getSession().setAttribute(SESSION_PARAM_LAST_SELECTED_DATE, this._requiredDate);
	}

	public String getListName() {
		return _listName;
	}
	public void setListName(String listName) {
		this._listName = listName;
	}
	
	@Override
	public String getContentType() {
		return _calMan.getConfig().getContentTypeCode();
	}
	
	@Override
	public Set getAllowedGroups() {
		return _allowedGroups;
	}
	
	@Override
	public String getAttributeNameEnd() {
		return _calMan.getConfig().getEndAttributeName();
	}
	
	@Override
	public String getAttributeNameStart() {
		return _calMan.getConfig().getStartAttributeName();
	}
	
	@Override
	public Date getRequiredDay() {
		return _requiredDate;
	}
	
	@Override
	public void setAllowedGroups(Set allowedGroups) {
		Set temp = new HashSet();
		if (allowedGroups != null && !allowedGroups.isEmpty()) {
			temp.addAll(allowedGroups);
		}
		this._allowedGroups = temp;
	}

	private String _listName;
	private ICalendarManager _calMan;
	private Date _requiredDate;
	private Set _allowedGroups;

	public static final String SESSION_PARAM_LAST_SELECTED_DATE = "lastSelectedDate";

}
