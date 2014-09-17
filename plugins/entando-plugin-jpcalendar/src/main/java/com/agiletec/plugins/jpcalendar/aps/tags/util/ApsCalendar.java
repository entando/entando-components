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
