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

/**
 * @author E.Santoboni
 */
public class SmallEventOfDay {
		
	public void setEnd(Date end) {
		if (end != null) {
			this._end = this.buildCurrectDate(end, false);
		} else {
			_end = null;
		}
	}
	
	public void verifyDates() {
		if (_end == null) {
			_end = this.buildCurrectDate(_start, false);
		}
		if (_start == null) {
			_start = this.buildCurrectDate(_end, true);
		}
	}
	
	private Date buildCurrectDate(Date date, boolean start) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (start) {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
		}
		return calendar.getTime();
	}
	
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
	}
	
	public Date getStart() {
		return _start;
	}
	public void setStart(Date start) {
		this._start = start;
	}
	
	public Date getEnd() {
		return _end;
	}
	
	private String _id;
	private Date _start;
	private Date _end;
	
}
