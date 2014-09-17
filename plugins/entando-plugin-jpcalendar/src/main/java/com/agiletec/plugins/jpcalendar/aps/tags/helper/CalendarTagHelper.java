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
package com.agiletec.plugins.jpcalendar.aps.tags.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpcalendar.aps.tags.util.ApsCalendar;
import com.agiletec.plugins.jpcalendar.aps.tags.util.CellaCalendar;

/**
 * Helper for Calendar Tag
 *
 * */
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

	/**
	 * Create the calendar for month and year required; if month and year are
	 * not specified, it returns the calendar for the current month
	 *
	 * @param request
	 *            the request
	 * @return the required Calendar
	 */
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
			if (selectedDate != null && selectedDate.length() > 0) {
				Date date = DateConverter.parseDate(selectedDate, datePattern);
				if (date != null)
					cal.setTime(date);
				return cal;
			}
		} else {
//			Calendar lastRequiredCalendar = (Calendar) this.pageContext
//					.getSession().getAttribute(
//							LAST_REQUIRED_CALENDAR_SESSION_PARAM);
			if (lastRequiredCalendar != null)
				return lastRequiredCalendar;
		}
		return cal;
	}

	public ApsCalendar getCalendarioDelMese(Calendar cal, int[] array, String datePattern) {
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int righe = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);

		int giornoSettimanaPrimoDelMese = cal.get(Calendar.DAY_OF_WEEK) - 2;
		// Sapendo che il calendario richiesto parte da 1
		if (giornoSettimanaPrimoDelMese < 0) {
			righe++;
		}

		cal.set(Calendar.DAY_OF_MONTH, cal
				.getActualMaximum(Calendar.DAY_OF_MONTH));
		int giornoSettimanaUltimoDelMese = cal.get(Calendar.DAY_OF_WEEK) - 2;
		if (giornoSettimanaUltimoDelMese < 0) {
			righe--;
		}

		cal.set(Calendar.DAY_OF_MONTH, 1);

		int colonne = 7;
		CellaCalendar[][] mese = new CellaCalendar[righe][colonne];
		ApsCalendar apsCalendar = new ApsCalendar();
		apsCalendar.setCalendario(mese);
		apsCalendar.setData(cal.getTime());
		apsCalendar.setSettimane(this.getNumeriSettimana(cal, righe));

		int settimanaCorrente = 0;
		int indiceGiornoSettimana = cal.get(Calendar.DAY_OF_WEEK) - 2;
		if (indiceGiornoSettimana < 0) {
			indiceGiornoSettimana = 7 + indiceGiornoSettimana;
		}
		int giornoMaxDelMese = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 1; i <= giornoMaxDelMese; i++) {
			cal.set(Calendar.DAY_OF_MONTH, i);
			CellaCalendar cella = new CellaCalendar();
			cella.setDay(i);
			cella.setFormattedDate(DateConverter.getFormattedDate(cal.getTime(), datePattern));
			int events = array[i - 1];
			cella.setHasEvents(events > 0);
			mese[settimanaCorrente][indiceGiornoSettimana] = cella;
			indiceGiornoSettimana++;
			if (indiceGiornoSettimana > 6) {
				++settimanaCorrente;
				indiceGiornoSettimana = 0;
			}
		}
		return apsCalendar;
	}

	private int[] getNumeriSettimana(Calendar requiredCalendar, int righe) {
		int[] settimane = new int[righe];
		Calendar clone2 = Calendar.getInstance(Locale.ENGLISH);
		clone2.setTime(requiredCalendar.getTime());
		int j = 0;
		while ( j < righe) {
			int currentWeek = clone2.get(Calendar.WEEK_OF_YEAR);
			settimane[j++] = currentWeek;
			clone2.set(Calendar.WEEK_OF_YEAR, currentWeek+1);
		}
		return settimane;
	}

}
