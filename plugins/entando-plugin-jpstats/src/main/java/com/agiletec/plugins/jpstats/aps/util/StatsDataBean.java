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
package com.agiletec.plugins.jpstats.aps.util;

import java.util.Calendar;

public class StatsDataBean implements IStatsDataBean {
	
	public Calendar getStart() {
		return _start;
	}
	
	public void setStart(Calendar start) {
		this._start = start;
	}
	
	public Calendar getEnd() {
		return _end;
	}

	public void setEnd(Calendar end) {
		this._end = end;
	}
	
	private Calendar _end;
	private Calendar _start;

}
