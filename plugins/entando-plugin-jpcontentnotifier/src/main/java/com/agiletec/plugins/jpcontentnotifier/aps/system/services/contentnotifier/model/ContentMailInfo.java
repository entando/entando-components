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
package com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContentMailInfo {
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	public String getContentTypeCode() {
		return _contentTypeCode;
	}
	public void setContentTypeCode(String contentTypeCode) {
		this._contentTypeCode = contentTypeCode;
	}
	
	public String getContentDescr() {
		return _contentDescr;
	}
	public void setContentDescr(String contentDescr) {
		this._contentDescr = contentDescr;
	}
	
	public Date getDate() {
		return _date;
	}
	public void setDate(Date date) {
		this._date = date;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public String getMainGroup() {
		return _mainGroup;
	}
	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}
	
	public void setGroups(String[] groups) {
		this._groups = new ArrayList<String>();
		for (String group : groups) {
			this._groups.add(group);
		}
	}
	public void setGroups(List<String> groups) {
		this._groups = groups;
	}
	public List<String> getGroups() {
		return this._groups;
	}
	
	private int _id;
	private String _contentId;
	private String _contentTypeCode;
	private String _contentDescr;
	private Date _date;
	private int _operationCode;
	private String _mainGroup;
	private List<String> _groups;
	
}