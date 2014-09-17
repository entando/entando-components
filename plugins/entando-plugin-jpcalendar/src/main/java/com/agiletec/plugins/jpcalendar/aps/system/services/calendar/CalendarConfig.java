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
package com.agiletec.plugins.jpcalendar.aps.system.services.calendar;

/**
 * @author E.Santoboni
 */
public class CalendarConfig {
	
	public String getContentTypeCode() {
		return _contentTypeCode;
	}
	public void setContentTypeCode(String contentTypeCode) {
		this._contentTypeCode = contentTypeCode;
	}
	
	public String getStartAttributeName() {
		return _startAttributeName;
	}
	public void setStartAttributeName(String startAttributeName) {
		this._startAttributeName = startAttributeName;
	}
	
	public String getEndAttributeName() {
		return _endAttributeName;
	}
	public void setEndAttributeName(String endAttributeName) {
		this._endAttributeName = endAttributeName;
	}
	
	private String _contentTypeCode;
	private String _startAttributeName;
	private String _endAttributeName;
	
}