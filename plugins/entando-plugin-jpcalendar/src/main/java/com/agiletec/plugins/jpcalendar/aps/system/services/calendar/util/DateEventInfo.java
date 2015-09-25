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
package com.agiletec.plugins.jpcalendar.aps.system.services.calendar.util;

import java.util.Calendar;
import java.util.Date;

public class DateEventInfo {
	
	public DateEventInfo(Date startRange, Date endRange) {
		this._startRange = startRange;
		
		Calendar temp = Calendar.getInstance();
		temp.setTime(endRange);
		temp.set(Calendar.HOUR_OF_DAY, 23);
		temp.set(Calendar.MINUTE, 59);
		temp.set(Calendar.SECOND, 59);
		this._endRange = temp.getTime();
	}
	
	public boolean isValidEvent() {
		Date start = this.getStart();
		Date end = this.getEnd();
		if ((start == null && end == null) || 
				start.after(end)) {
			return false;
		}
		return (_startRange.before(_end) && _start.before(_endRange));
	}
	
	public void setStart(Date start) {
		this._start = start;
	}
	
	public int getStartDay() {
		if (_start.before(_startRange)) {
			return this.getDayOfMonth(_startRange);
		} else {
			return this.getDayOfMonth(_start);
		}
	}
	
	public void setEnd(Date end) {
		if (end != null) {
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(end);
			endCal.set(Calendar.HOUR_OF_DAY, 23);
			endCal.set(Calendar.MINUTE, 59);
			endCal.set(Calendar.SECOND, 58);
			this._end = endCal.getTime();
		} else {
			this._end = null;
		}
	}
	
	public int getEndDay() {
		if (_end.after(_endRange)) {
			return this.getDayOfMonth(_endRange);
		} else {
			return this.getDayOfMonth(_end);
		}
	}
	
	/**
	 * Restituisce la data di fine dell'evento.
	 * Nel caso in cui la data di fine sia nulla, 
	 * viene imposta uguale alla data di inizio.
	 * @return La data di fine dell'evento.
	 */
	private Date getEnd() {
		if (this._end == null) this.setEnd(((Date) _start.clone()));
		return _end;
	}
	
	private int getDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	
	/**
	 * Restituisce la data di inizio dell'evento.
	 * Nel caso in cui la data di inizio sia nulla, 
	 * viene imposta uguale alla data di fine.
	 * @return La data di inizio dell'evento.
	 */
	private Date getStart() {
		if (this._start == null) this._start = _end;
		return _start;
	}
	
	private Date _start;
	private Date _end;
	
	private Date _startRange;
	private Date _endRange;
	
}
