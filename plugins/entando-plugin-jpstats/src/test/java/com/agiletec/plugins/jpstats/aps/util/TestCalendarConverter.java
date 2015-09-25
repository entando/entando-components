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
