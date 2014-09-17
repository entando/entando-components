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

	public void release() {
		_listName = null;
		_calMan = null;
		_requiredDate = null;
		_allowedGroups = null;
	}

	private void extractRequiredDate() {
		ServletRequest request = this.pageContext.getRequest();
		String stringData = request.getParameter("selectedDate");
		_requiredDate = DateConverter.parseDate(stringData,
				CalendarManager.REQUIRED_DATE_PATTERN);
		if (_requiredDate == null) {
			_requiredDate = (Date) this.pageContext.getSession().getAttribute(
					SESSION_PARAM_LAST_SELECTED_DATE);
			// PRESA DATA DI Ultima richiesta eventi;
		}
		if (_requiredDate == null) {
			_requiredDate = new Date();
			// PRESA DATA DI OGGI
		}
		this.pageContext.getSession().setAttribute(
				SESSION_PARAM_LAST_SELECTED_DATE, _requiredDate);
	}

	/**
	 * Restituisce il nome con il quale viene inserita nel pageContext la lista
	 * degli identificativi trovati.
	 * 
	 * @return Returns the listName.
	 */
	public String getListName() {
		return _listName;
	}

	/**
	 * Setta il nome con il quale viene inserita nel pageContext la lista degli
	 * identificativi trovati.
	 * 
	 * @param listName
	 *            The listName to set.
	 */
	public void setListName(String listName) {
		this._listName = listName;
	}
	
	public String getContentType() {
		return _calMan.getConfig().getContentTypeCode();
	}
	
	public Set getAllowedGroups() {
		return _allowedGroups;
	}
	
	public String getAttributeNameEnd() {
		return _calMan.getConfig().getEndAttributeName();
	}
	
	public String getAttributeNameStart() {
		return _calMan.getConfig().getStartAttributeName();
	}
	
	public Date getRequiredDay() {
		return _requiredDate;
	}

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
