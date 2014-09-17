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

/**
 * Calendar Cell. Object used by CalendarTag
 * 
 * @author E.Santoboni
 */
public class CellaCalendar {
	
	public int getDay() {
		return _day;
	}
	public void setDay(int day) {
		this._day = day;
	}
	
	public String getFormattedDate() {
		return _formattedDate;
	}
	public void setFormattedDate(String formattedDate) {
		this._formattedDate = formattedDate;
	}
	
	public boolean isHasEvents() {
		return _hasEvents;
	}
	public void setHasEvents(boolean hasEvents) {
		this._hasEvents = hasEvents;
	}

	private int _day;
	private String _formattedDate;
	private boolean _hasEvents;
	
}