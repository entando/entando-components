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

import com.agiletec.plugins.jpcalendar.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.plugins.jpcalendar.aps.system.services.CalendarConstants;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.ICalendarManager;
import com.agiletec.plugins.jpcalendar.aps.tags.helper.CalendarTagHelper;
import com.agiletec.plugins.jpcalendar.aps.tags.util.ApsCalendar;
import com.agiletec.plugins.jpcalendar.aps.tags.util.CalendarDay;

/**
* @author G.Cocco
*/
public class TestCalendarTagHelper extends ApsPluginBaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void test() throws ApsSystemException {

		String month = "";
		String year = "";
		String selectedDate = "";
		String datePattern = "yyyyMMdd";
		Calendar lastRequiredCalendar = null;

		System.out.println("current");
		Calendar cal = calendarTagHelper.getRequiredCalendar(month, year, selectedDate, datePattern, lastRequiredCalendar);
		assertNotNull(cal);

		int[] array = _calendarManager.getEventsForMonth(cal, userManager.getUser("admin"));
		assertNotNull(array);
		ApsCalendar calendarioRichiesto = calendarTagHelper.getCalendarioDelMese( (Calendar) cal.clone(), array, datePattern);

		assertNotNull(calendarioRichiesto);


		month = "0";
		year = "2010";
		cal = calendarTagHelper.getRequiredCalendar(month, year, selectedDate, datePattern, lastRequiredCalendar);
		assertNotNull(cal);
		array = _calendarManager.getEventsForMonth(cal, userManager.getUser("admin"));
		assertNotNull(array);
		calendarioRichiesto = calendarTagHelper.getCalendarioDelMese( (Calendar) cal.clone(), array, datePattern);

		assertNotNull(calendarioRichiesto);
		CalendarDay[][] calend = calendarioRichiesto.getCalendario();

		assertNotNull(calend);
		assertEquals(5, calend.length);
		assertEquals(7, calend[0].length);

		int[] settNumbers = calendarioRichiesto.getSettimane();
		assertNotNull(settNumbers);
		assertEquals(5, settNumbers.length);

		int[] settNumbersCheck = {1,2,3,4,5};
		int i = 0;
		assertEquals(settNumbersCheck[i], settNumbers[i++]);
		assertEquals(settNumbersCheck[i], settNumbers[i++]);
		assertEquals(settNumbersCheck[i], settNumbers[i++]);
		assertEquals(settNumbersCheck[i], settNumbers[i++]);
		assertEquals(settNumbersCheck[i], settNumbers[i++]);


		System.out.println("11 2009");
		month = "11";
		year = "2009";
		cal = calendarTagHelper.getRequiredCalendar(month, year, selectedDate, datePattern, lastRequiredCalendar);
		assertNotNull(cal);

		array = _calendarManager.getEventsForMonth(cal, userManager.getUser("admin"));
		assertNotNull(array);
		calendarioRichiesto = calendarTagHelper.getCalendarioDelMese( (Calendar) cal.clone(), array, datePattern);

		assertNotNull(calendarioRichiesto);

		calend = calendarioRichiesto.getCalendario();

		assertNotNull(calend);
		assertEquals(5, calend.length);
		assertEquals(7, calend[0].length);

		settNumbers = calendarioRichiesto.getSettimane();
		assertNotNull(settNumbers);
		assertEquals(5, settNumbers.length);

		int[] settNumbersCheck2 = {49,50,51,52,1};
		i = 0;
		assertEquals(settNumbersCheck2[i], settNumbers[i++]);
		assertEquals(settNumbersCheck2[i], settNumbers[i++]);
		assertEquals(settNumbersCheck2[i], settNumbers[i++]);
		assertEquals(settNumbersCheck2[i], settNumbers[i++]);
		assertEquals(settNumbersCheck2[i], settNumbers[i++]);
	}


/*	public void testWeek () {
		Calendar clone2 = Calendar.getInstance(Locale.ITALIAN);
//		clone2.setTime(requiredCalendar.getTime());
		clone2.set(Calendar.WEEK_OF_YEAR, 1);

		while ( clone2.get(Calendar.YEAR) == 2010  ) {

			int currentWeek = clone2.get(Calendar.WEEK_OF_YEAR);
			clone2.set(Calendar.WEEK_OF_YEAR, currentWeek+1);
			System.out.println(" week: " + currentWeek);
		}

	}*/

	private void init() {
		calendarTagHelper =
			(CalendarTagHelper) this.getApplicationContext().getBean("jpcalendarCalendarTagHelper");
		_calendarManager = (ICalendarManager) this.getService(CalendarConstants.CALENDAR_MANAGER);
		userManager = (IUserManager) this.getService("UserManager");
	}


	private ICalendarManager _calendarManager = null;

	private IUserManager userManager;

	private CalendarTagHelper calendarTagHelper;

}
