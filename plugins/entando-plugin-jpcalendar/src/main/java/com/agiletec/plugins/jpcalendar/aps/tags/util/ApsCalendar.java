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
package com.agiletec.plugins.jpcalendar.aps.tags.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Calendar object. Used by the CalendarTag.
 * 
 * @author E.Santoboni
 */
public class ApsCalendar {
	
	public CalendarDay[][] getCalendar() {
		return _calendar;
	}
	public void setCalendar(CalendarDay[][] calendar) {
		this._calendar = calendar;
	}
	@Deprecated
	public CalendarDay[][] getCalendario() {
		return this.getCalendar();
	}
	@Deprecated
	public void setCalendario(CalendarDay[][] calendar) {
		this.setCalendar(calendar);
	}
	public Date getDate() {
		return _date;
	}
	public void setDate(Date date) {
		this._date = date;
	}
	@Deprecated
	public Date getData() {
		return this.getDate();
	}
	@Deprecated
	public void setData(Date data) {
		this.setDate(data);
	}
	public int[] getWeeks() {
		return _weeks;
	}
	public void setWeeks(int[] weeks) {
		this._weeks = weeks;
	}
	@Deprecated
	public int[] getSettimane() {
		return this.getWeeks();
	}
	@Deprecated
	public void setSettimane(int[] weeks) {
		this.setWeeks(weeks);
	}
	
	public int getCurrentWeek() {
		Calendar cal = Calendar.getInstance(Locale.ITALIAN);
		Calendar required = Calendar.getInstance(Locale.ITALIAN);
		required.setTime(this.getDate());
		if (cal.get(Calendar.YEAR) == required.get(Calendar.YEAR)) {
			return cal.get(Calendar.WEEK_OF_YEAR);
		}
		return -1;
	}
	
	private Date _date;
	private CalendarDay[][] _calendar;
	private int[] _weeks;
	
}
