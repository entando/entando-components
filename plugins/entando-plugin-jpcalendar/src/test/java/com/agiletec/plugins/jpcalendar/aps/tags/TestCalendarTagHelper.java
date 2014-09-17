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

import com.agiletec.plugins.jpcalendar.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.plugins.jpcalendar.aps.system.services.CalendarConstants;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.ICalendarManager;
import com.agiletec.plugins.jpcalendar.aps.tags.helper.CalendarTagHelper;
import com.agiletec.plugins.jpcalendar.aps.tags.util.ApsCalendar;
import com.agiletec.plugins.jpcalendar.aps.tags.util.CellaCalendar;

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
		CellaCalendar[][] calend = calendarioRichiesto.getCalendario();

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
