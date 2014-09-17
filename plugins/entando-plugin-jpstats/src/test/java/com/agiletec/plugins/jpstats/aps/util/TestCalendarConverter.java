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

import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpstats.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpstats.aps.system.services.JpStatsSystemConstants;

public class TestCalendarConverter extends ApsPluginBaseTestCase {
	
	public void testGetCalendarDay() {
		Calendar day1 = CalendarConverter.getCalendarDay("13/11/1976", 0, 0, 0, 0);
		Calendar day2 = CalendarConverter.getCalendarDay("31/12/2007", 23, 59, 59, 999);
		Calendar day3 = CalendarConverter.getCalendarDay("aa/bb/cccc", 23, 59, 59, 999);
		assertNotNull(day1);
		assertNotNull(day2);
		assertNull(day3);
		assertEquals("13/11/1976", DateConverter.getFormattedDate(day1.getTime(), JpStatsSystemConstants.DATE_FORMAT));
		String data =null;
		Calendar day4 = CalendarConverter.getCalendarDay(data, 0, 0, 0, 0);
		assertNull(day4);
		data ="";
		day4 = CalendarConverter.getCalendarDay(data, 0, 0, 0, 0);
		assertNull(day4);
	}
	
}
