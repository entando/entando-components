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
