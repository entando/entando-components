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
