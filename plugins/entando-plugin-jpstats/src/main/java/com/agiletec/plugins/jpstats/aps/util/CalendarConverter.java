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
package com.agiletec.plugins.jpstats.aps.util;

import java.util.Calendar;
import java.util.Date;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpstats.aps.system.services.JpStatsSystemConstants;

public class CalendarConverter {
	
	/**
	 * Converts a String to a Calendar Object
	 * @param date the string in the format StatsConstants.DATE_FORMAT to be converted in Calendar
	 * @param hour int value for the hour of the Calendar Object 
	 * @param minute int value for the minutes of the Calendar Object 
	 * @param second int value for the seconds of the Calendar Object 
	 * @param millisencond int value for the millisecond of the Calendar Object 
	 * @return Calendar object
	 */
	public static Calendar getCalendarDay(String date, int hour, int minute, int second, int millisencond) {
		Calendar calendar = Calendar.getInstance();
		Date formattedDate = DateConverter.parseDate(date, JpStatsSystemConstants.DATE_FORMAT);
		if(null != formattedDate) {
			calendar.setTime(formattedDate);
			calendar.set(Calendar.AM_PM, Calendar.AM);
			calendar.set(Calendar.HOUR, hour);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, second);
			calendar.set(Calendar.MILLISECOND, millisencond);
			return calendar;
		} else {
			return null;
		}
	}
	
}
