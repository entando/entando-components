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
package com.agiletec.plugins.jpcalendar.aps.tags.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpcalendar.aps.tags.util.ApsCalendar;
import com.agiletec.plugins.jpcalendar.aps.tags.util.CalendarDay;

/**
 * Helper for Calendar Tag
 */
public class CalendarTagHelper {

	public Calendar getPrevMonth(Calendar cal) {
		Calendar prevCal = (Calendar) cal.clone();
		if (prevCal.get(Calendar.MONTH) == Calendar.JANUARY) {
			prevCal.set(Calendar.MONTH, Calendar.DECEMBER);
			prevCal.set(Calendar.YEAR, prevCal.get(Calendar.YEAR) - 1);
		} else {
			prevCal.set(Calendar.MONTH, prevCal.get(Calendar.MONTH) - 1);
		}
		return prevCal;
	}

	public Calendar getNextMonth(Calendar cal) {
		Calendar nextCal = (Calendar) cal.clone();
		if (nextCal.get(Calendar.MONTH) == Calendar.DECEMBER) {
			nextCal.set(Calendar.MONTH, Calendar.JANUARY);
			nextCal.set(Calendar.YEAR, nextCal.get(Calendar.YEAR) + 1);
		} else {
			nextCal.set(Calendar.MONTH, nextCal.get(Calendar.MONTH) + 1);
		}
		return nextCal;
	}

	public int[] getYears(int firstYear, int lastYear) {
		int lenght = (lastYear - firstYear + 2);
		int[] years = new int[lenght];
		for (int i = 0; i < years.length; i++) {
			years[i] = firstYear;
			firstYear++;
		}
		return years;
	}

	public Calendar getRequiredCalendar(String monthString, String yearString, String selectedDate, String datePattern, Calendar lastRequiredCalendar) {
		Calendar cal = Calendar.getInstance(Locale.ENGLISH);
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		if (null != monthString && null != yearString
				&& monthString.length() > 0 && yearString.length() > 0) {
			int month = Integer.parseInt(monthString);
			int year = Integer.parseInt(yearString);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.YEAR, year);
		} else if (null != selectedDate) {
			if (selectedDate.length() > 0) {
				Date date = DateConverter.parseDate(selectedDate, datePattern);
				if (date != null) {
					cal.setTime(date);
				}
				return cal;
			}
		} else {
			if (lastRequiredCalendar != null) {
				return lastRequiredCalendar;
			}
		}
		return cal;
	}
	
	@Deprecated
	public ApsCalendar getCalendarioDelMese(Calendar cal, int[] array, String datePattern) {
		return this.getRequiredApsCalendar(cal, array, datePattern);
	}
	
	public ApsCalendar getRequiredApsCalendar(Calendar cal, int[] array, String datePattern) {
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int rows = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
		int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 2;
		if (firstDayOfWeek < 0) {
			rows++;
		}
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		int lastDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 2;
		if (lastDayOfWeek < 0) {
			rows--;
		}
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int columns = 7;
		CalendarDay[][] month = new CalendarDay[rows][columns];
		ApsCalendar apsCalendar = new ApsCalendar();
		apsCalendar.setCalendar(month);
		apsCalendar.setDate(cal.getTime());
		apsCalendar.setWeeks(this.getNumberOfWeek(cal, rows));
		int currentWeek = 0;
		int dayOfWeekIndex = cal.get(Calendar.DAY_OF_WEEK) - 2;
		if (dayOfWeekIndex < 0) {
			dayOfWeekIndex = 7 + dayOfWeekIndex;
		}
		int maxDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 1; i <= maxDayOfMonth; i++) {
			cal.set(Calendar.DAY_OF_MONTH, i);
			CalendarDay calendarDay = new CalendarDay();
			calendarDay.setDay(i);
			calendarDay.setFormattedDate(DateConverter.getFormattedDate(cal.getTime(), datePattern));
			int events = array[i - 1];
			calendarDay.setHasEvents(events > 0);
			month[currentWeek][dayOfWeekIndex] = calendarDay;
			dayOfWeekIndex++;
			if (dayOfWeekIndex > 6) {
				++currentWeek;
				dayOfWeekIndex = 0;
			}
		}
		return apsCalendar;
	}

	private int[] getNumberOfWeek(Calendar requiredCalendar, int rows) {
		int[] weeks = new int[rows];
		Calendar clone2 = Calendar.getInstance(Locale.ENGLISH);
		clone2.setTime(requiredCalendar.getTime());
		int j = 0;
		while ( j < rows) {
			int currentWeek = clone2.get(Calendar.WEEK_OF_YEAR);
			weeks[j++] = currentWeek;
			clone2.set(Calendar.WEEK_OF_YEAR, currentWeek+1);
		}
		return weeks;
	}

}
