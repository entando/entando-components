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
