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
package com.agiletec.plugins.jpversioning.aps.system.services.versioning;

import java.util.Date;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public class ContentVersion {
	
	public long getId() {
		return _id;
	}
	public void setId(long id) {
		this._id = id;
	}
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	public String getDescr() {
		return _descr;
	}
	public void setDescr(String descr) {
		this._descr = descr;
	}
	
	public void setStatus(String status) {
		this._status = status;
	}
	public String getStatus() {
		return _status;
	}
	
	public String getXml() {
		return _xml;
	}
	public void setXml(String xml) {
		this._xml = xml;
	}
	
	public Date getVersionDate() {
		return _versionDate;
	}
	public void setVersionDate(Date versionDate) {
		this._versionDate = versionDate;
	}
	
	public String getVersion() {
		return _version;
	}
	public void setVersion(String version) {
		this._version = version;
	}
	
	public int getOnlineVersion() {
		return _onlineVersion;
	}
	public void setOnlineVersion(int onlineVersion) {
		this._onlineVersion = onlineVersion;
	}
	
	public boolean isApproved() {
		return _approved;
	}
	public void setApproved(boolean approved) {
		this._approved = approved;
	}
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	private long _id;
	private String _contentId;
	private String _contentType;
	private String _descr;
	private String _status;
	private String _xml;
	private Date _versionDate;
	private String _version;
	private int _onlineVersion;
	private boolean _approved;
	private String _username;
	
}