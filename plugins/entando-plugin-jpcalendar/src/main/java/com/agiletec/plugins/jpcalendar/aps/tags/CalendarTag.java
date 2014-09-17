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
	
	public int doStartTag() throws JspException {
		
		CalendarTagHelper calendarTagHelper = 
			(CalendarTagHelper) ApsWebApplicationUtils.getBean("jpcalendarCalendarTagHelper", pageContext);
		
		ServletRequest request = this.pageContext.getRequest();
		UserDetails currentUser = (UserDetails) this.pageContext.getSession().getAttribute(
				SystemConstants.SESSIONPARAM_CURRENT_USER);
		try {
			
			Calendar cal = (Calendar) 
				this.pageContext.getSession().getAttribute(CalendarConstants.LAST_REQUIRED_CALENDAR_SESSION_PARAM);
			
			
			String monthString = request.getParameter("month");
			String yearString = request.getParameter("year");
			String selectedDate = request.getParameter("selectedDate");
			
			Calendar requiredCalendar = calendarTagHelper.getRequiredCalendar(monthString, yearString, selectedDate , this.getDatePattern(), cal);
			this.pageContext.getSession().setAttribute(
					CalendarConstants.LAST_REQUIRED_CALENDAR_SESSION_PARAM, requiredCalendar);

			ICalendarManager calMan = (ICalendarManager) ApsWebApplicationUtils.getBean(CalendarConstants.CALENDAR_MANAGER, pageContext);

			int[] array = calMan.getEventsForMonth(requiredCalendar,
					currentUser);
//			for (int i = 0; i < array.length; i++) {
//				System.out.println(array[i]);
//			}
			
			ApsCalendar calendarioRichiesto = calendarTagHelper.getCalendarioDelMese(
					(Calendar) requiredCalendar.clone(), array, this.getDatePattern());
			this.pageContext.setAttribute(this.getNomeGruppo(),
					calendarioRichiesto);

			this.pageContext.setAttribute("selectedYear", String
					.valueOf(requiredCalendar.get(Calendar.YEAR)));
			this.pageContext.setAttribute("selectedMonth", String
					.valueOf(requiredCalendar.get(Calendar.MONTH)));

			Calendar prevCal = calendarTagHelper.getPrevMonth(requiredCalendar);
			this.pageContext.setAttribute("prevMonth", String.valueOf(prevCal
					.get(Calendar.MONTH)));
			this.pageContext.setAttribute("prevYear", String.valueOf(prevCal
					.get(Calendar.YEAR)));

			Calendar nextCal = calendarTagHelper.getNextMonth(requiredCalendar);
			this.pageContext.setAttribute("nextMonth", String.valueOf(nextCal
					.get(Calendar.MONTH)));
			this.pageContext.setAttribute("nextYear", String.valueOf(nextCal
					.get(Calendar.YEAR)));

			int firstYear = Math.min(prevCal.get(Calendar.YEAR), calMan
					.getFirstYear());

			Calendar today = Calendar.getInstance();
			int lastYear = Math.max(nextCal.get(Calendar.YEAR), today
					.get(Calendar.YEAR));

			this.pageContext.setAttribute("yearsForSelect", calendarTagHelper.getYears(
					firstYear, lastYear));

		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("error in doStartTag", t);
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		this.release();
		return super.doEndTag();
	}

	public void release() {
		super.release();
		this._nomeGruppo = null;
		this._datePattern = null;
	}

	public String getNomeGruppo() {
		return _nomeGruppo;
	}

	public void setNomeGruppo(String nomeGruppo) {
		this._nomeGruppo = nomeGruppo;
	}

	public String getDatePattern() {
		return _datePattern;
	}

	public void setDatePattern(String datePattern) {
		this._datePattern = datePattern;
	}

	public void setCalendarTagHelper(CalendarTagHelper calendarTagHelper) {
		this.calendarTagHelper = calendarTagHelper;
	}
	public CalendarTagHelper getCalendarTagHelper() {
		return calendarTagHelper;
	}

	private String _nomeGruppo;
	private String _datePattern;
	private CalendarTagHelper calendarTagHelper;

}