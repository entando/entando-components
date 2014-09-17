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
package com.agiletec.plugins.jpcalendar.aps.services.calendar;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.agiletec.plugins.jpcalendar.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpcalendar.aps.services.calendar.util.MockEventsOfDayDataBean;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpcalendar.aps.system.services.CalendarConstants;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.ICalendarManager;

public class TestCalendarManager extends ApsPluginBaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetEventsForMonth() throws Throwable {
		Calendar cal = this.createCalendar(1, Calendar.APRIL, 1999);
		this.setUserOnSession("guest");
		UserDetails user = (UserDetails) this.getRequestContext().getRequest().getSession()
				.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		int[] eventsForMonth = _calendarManager.getEventsForMonth(cal, user);
		assertNotNull(eventsForMonth);
		// DEVE TROVARE SOLO EVN192 che parte dal 14-04 e finisce il 14-06
		assertEquals(0, eventsForMonth[2]);
		assertEquals(0, eventsForMonth[12]);
		assertEquals(1, eventsForMonth[13]);// Contenuto EVN192 del gruppo free
		assertEquals(1, eventsForMonth[14]);// Contenuto EVN192 del gruppo free
		assertEquals(1, eventsForMonth[27]);// Contenuto EVN192 del gruppo free
		this.setUserOnSession("admin");
		user = (UserDetails) this.getRequestContext().getRequest().getSession()
				.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		eventsForMonth = _calendarManager.getEventsForMonth(cal, user);
		assertNotNull(eventsForMonth);
		// DEVE TROVARE SOLO EVN192 (dal 14-04 e finisce il 14-06) e EVN103 (dal
		// 15-04 e finisce il 14-06-2007)
		assertEquals(0, eventsForMonth[2]);
		assertEquals(0, eventsForMonth[12]);
		assertEquals(1, eventsForMonth[13]);// Contenuto EVN192 (free)
		assertEquals(2, eventsForMonth[14]);// Contenuto EVN192 (free) e EVN103
		// (coach)
		assertEquals(2, eventsForMonth[27]);// Contenuto EVN192 (free) e EVN103
		// (coach)
	}

	public void testLoadEventsOfDayId() throws Throwable {
		MockEventsOfDayDataBean bean = new MockEventsOfDayDataBean();
		bean.setAttributeNameStart(this._calendarManager.getConfig().getStartAttributeName());
		bean.setAttributeNameEnd(this._calendarManager.getConfig().getEndAttributeName());
		bean.setContentType(this._calendarManager.getConfig().getContentTypeCode());
		this.setUserOnSession("guest");
		UserDetails user = (UserDetails) this.getRequestContext().getRequest()
				.getSession().getAttribute(
						SystemConstants.SESSIONPARAM_CURRENT_USER);
		List<Group> userGroups = _authorizatorManager.getUserGroups(user);
		Set allowedGroup = new HashSet();
		allowedGroup.add(Group.FREE_GROUP_NAME);
		for (Group group : userGroups) {
			allowedGroup.add(group.getName());
		}
		bean.setAllowedGroups(allowedGroup);
		// bean.setAllowedGroups(user.getAuthorities().getGroups());
		Calendar requiredDay = this.createCalendar(3, Calendar.APRIL, 1999);
		bean.setRequiredDay(requiredDay.getTime());
		List list = this._calendarManager.loadEventsOfDayId(bean);
		assertTrue(list.isEmpty());

		requiredDay = this.createCalendar(13, Calendar.APRIL, 1999);
		bean.setRequiredDay(requiredDay.getTime());
		list = this._calendarManager.loadEventsOfDayId(bean);
		assertTrue(list.isEmpty());

		requiredDay = this.createCalendar(14, Calendar.APRIL, 1999);
		bean.setRequiredDay(requiredDay.getTime());
		list = this._calendarManager.loadEventsOfDayId(bean);
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());
		assertTrue(list.contains("EVN192"));

		requiredDay = this.createCalendar(15, Calendar.APRIL, 1999);
		bean.setRequiredDay(requiredDay.getTime());
		list = this._calendarManager.loadEventsOfDayId(bean);
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());
		assertTrue(list.contains("EVN192"));
		
		this.setUserOnSession("admin");
		user = (UserDetails) this.getRequestContext().getRequest().getSession()
				.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		userGroups = _authorizatorManager.getGroupsOfUser(user);
		allowedGroup = new HashSet();
		allowedGroup.add(Group.FREE_GROUP_NAME);
		for (Group group : userGroups) {
			allowedGroup.add(group.getName());
		}
		bean.setAllowedGroups(allowedGroup);		
		//bean.setAllowedGroups(user.getGroups());
		list = this._calendarManager.loadEventsOfDayId(bean);
		assertFalse(list.isEmpty());
		assertEquals(2, list.size());
		assertTrue(list.contains("EVN192"));
		assertTrue(list.contains("EVN103"));

	}

	private void init() throws Exception {
		try {
			_calendarManager = (ICalendarManager) this.getService(CalendarConstants.CALENDAR_MANAGER);
			_authorizatorManager=(IAuthorizationManager) this.getService(SystemConstants.AUTHORIZATION_SERVICE);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	private Calendar createCalendar(int day, int month, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
		return cal;
	}
	
	private ICalendarManager _calendarManager = null;
	private IAuthorizationManager _authorizatorManager = null;

}
