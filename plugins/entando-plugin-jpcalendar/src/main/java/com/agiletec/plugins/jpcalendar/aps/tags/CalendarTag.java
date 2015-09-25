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

import java.util.Calendar;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpcalendar.aps.system.services.CalendarConstants;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.ICalendarManager;
import com.agiletec.plugins.jpcalendar.aps.tags.helper.CalendarTagHelper;
import com.agiletec.plugins.jpcalendar.aps.tags.util.ApsCalendar;

/**
 * @author E.Santoboni
 */
public class CalendarTag extends TagSupport {
	
	private static final Logger _logger =  LoggerFactory.getLogger(CalendarTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		CalendarTagHelper calendarTagHelper = (CalendarTagHelper) ApsWebApplicationUtils.getBean("jpcalendarCalendarTagHelper", pageContext);
		ServletRequest request = this.pageContext.getRequest();
		UserDetails currentUser = (UserDetails) this.pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		try {
			Calendar cal = (Calendar) this.pageContext.getSession().getAttribute(CalendarConstants.LAST_REQUIRED_CALENDAR_SESSION_PARAM);
			String monthString = request.getParameter("month");
			String yearString = request.getParameter("year");
			String selectedDate = request.getParameter("selectedDate");
			Calendar requiredCalendar = calendarTagHelper.getRequiredCalendar(monthString, yearString, selectedDate , this.getDatePattern(), cal);
			this.pageContext.getSession().setAttribute(
					CalendarConstants.LAST_REQUIRED_CALENDAR_SESSION_PARAM, requiredCalendar);
			ICalendarManager calMan = (ICalendarManager) ApsWebApplicationUtils.getBean(CalendarConstants.CALENDAR_MANAGER, pageContext);
			int[] array = calMan.getEventsForMonth(requiredCalendar, currentUser);
			ApsCalendar requiredApsCalendar = calendarTagHelper.getCalendarioDelMese(
					(Calendar) requiredCalendar.clone(), array, this.getDatePattern());
			this.pageContext.setAttribute(this.getGroupName(), requiredApsCalendar);
			this.pageContext.setAttribute("selectedYear", String.valueOf(requiredCalendar.get(Calendar.YEAR)));
			this.pageContext.setAttribute("selectedMonth", String.valueOf(requiredCalendar.get(Calendar.MONTH)));
			Calendar prevCal = calendarTagHelper.getPrevMonth(requiredCalendar);
			this.pageContext.setAttribute("prevMonth", String.valueOf(prevCal.get(Calendar.MONTH)));
			this.pageContext.setAttribute("prevYear", String.valueOf(prevCal.get(Calendar.YEAR)));
			Calendar nextCal = calendarTagHelper.getNextMonth(requiredCalendar);
			this.pageContext.setAttribute("nextMonth", String.valueOf(nextCal.get(Calendar.MONTH)));
			this.pageContext.setAttribute("nextYear", String.valueOf(nextCal.get(Calendar.YEAR)));
			int firstYear = Math.min(prevCal.get(Calendar.YEAR), calMan.getFirstYear());
			Calendar today = Calendar.getInstance();
			int lastYear = Math.max(nextCal.get(Calendar.YEAR), today.get(Calendar.YEAR));
			this.pageContext.setAttribute("yearsForSelect", calendarTagHelper.getYears(firstYear, lastYear));
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("error in doStartTag", t);
		}
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag() throws JspException {
		this.release();
		return super.doEndTag();
	}
	
	@Override
	public void release() {
		super.release();
		this._groupName = null;
		this._datePattern = null;
	}
	
	@Deprecated
	public String getNomeGruppo() {
		return this.getGroupName();
	}
	@Deprecated
	public void setNomeGruppo(String nomeGruppo) {
		this.setGroupName(nomeGruppo);
	}
	
	public String getGroupName() {
		return _groupName;
	}
	public void setGroupName(String groupName) {
		this._groupName = groupName;
	}
	
	public String getDatePattern() {
		return _datePattern;
	}
	public void setDatePattern(String datePattern) {
		this._datePattern = datePattern;
	}
	
	private String _groupName;
	private String _datePattern;
	
}