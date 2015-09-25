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
