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