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
package com.agiletec.plugins.jpcalendar.aps.services.calendar.util;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.util.EventsOfDayDataBean;

public class MockEventsOfDayDataBean implements EventsOfDayDataBean {
	
	public String getAttributeNameEnd() {
		return _attributeNameEnd;
	}
	public void setAttributeNameEnd(String attributeNameEnd) {
		this._attributeNameEnd = attributeNameEnd;
	}
	
	public String getAttributeNameStart() {
		return _attributeNameStart;
	}
	public void setAttributeNameStart(String attributeNameStart) {
		this._attributeNameStart = attributeNameStart;
	}
	
	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	public String getCategory() {
		return _category;
	}
	public void setCategory(String category) {
		this._category = category;
	}
	
	public Date getFromDate() {
		return _fromDate;
	}
	public void setFromDate(Date fromDate) {
		this._fromDate = fromDate;
	}
	
	public Date getToDate() {
		return _toDate;
	}
	public void setToDate(Date toDate) {
		this._toDate = toDate;
	}
	
	public Date getRequiredDay() {
		return this._requiredDay;
	}
	public void setRequiredDay(Date requiredDay) {
		this._requiredDay = requiredDay;
	}
	
	public Set getAllowedGroups() {
		if (_allowedGroups == null) {
			_allowedGroups = new HashSet();
			_allowedGroups.add(Group.FREE_GROUP_NAME);
		}
		return _allowedGroups;
	}
	public void setAllowedGroups(Set allowedGroups) {
		this._allowedGroups = allowedGroups;
	}
	
	private String _contentType;
	private String _attributeNameStart;
	private String _attributeNameEnd;
	private String _category;
	private Date _toDate;
	private Date _fromDate;
	private Date _requiredDay;
	private Set _allowedGroups;
}
