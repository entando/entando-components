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
package com.agiletec.plugins.jpstats.aps.system.services.stats.model;

public abstract class VisitsStat implements Comparable<VisitsStat> {
	
	public Integer getVisits() {
		return this._visits;
	}
	public void setVisits(Integer visits) {
		this._visits = visits;
	}
	
	public int compareTo(VisitsStat v) {
		return v.getVisits().compareTo(this.getVisits());
	}
	
	private Integer _visits;
	
	public static final int STRING_TYPE = 1;
	public static final int DATE_TYPE = 2;
	
}