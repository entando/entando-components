/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
	
	public CellaCalendar[][] getCalendario() {
		return _calendario;
	}
	public void setCalendario(CellaCalendar[][] calendario) {
		this._calendario = calendario;
	}
	public Date getData() {
		return _data;
	}
	public void setData(Date data) {
		this._data = data;
	}
	public int[] getSettimane() {
		return _settimane;
	}
	public void setSettimane(int[] settimane) {
		this._settimane = settimane;
	}
	
	public int getCurrentWeek() {
		Calendar cal = Calendar.getInstance(Locale.ITALIAN);
		Calendar required = Calendar.getInstance(Locale.ITALIAN);
		required.setTime(_data);
		if (cal.get(Calendar.YEAR) == required.get(Calendar.YEAR)) {
			return cal.get(Calendar.WEEK_OF_YEAR);
		}
		return -1;
	}
	
	private Date _data;
	private CellaCalendar[][] _calendario;
	private int[] _settimane;
	
}
