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
package org.entando.entando.plugins.jpfileattribute.aps.system.file;

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "file")
@XmlType(propOrder = {"id", "filename", "contentType", "entityId", "entityClass", "base64"})
public class AttachedFile {
	
	@XmlElement(name = "id", required = true)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	
	@XmlElement(name = "filename", required = true)
	public String getFilename() {
		return _filename;
	}
	public void setFilename(String filename) {
		this._filename = filename;
	}
	
	@XmlElement(name = "contentType", required = true)
	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	@XmlElement(name = "entityId", required = true)
	public String getEntityId() {
		return _entityId;
	}
	public void setEntityId(String entityId) {
		this._entityId = entityId;
	}
	
	@XmlElement(name = "entityClass", required = true)
	public String getEntityClass() {
		return _entityClass;
	}
	public void setEntityClass(String entityClass) {
		this._entityClass = entityClass;
	}
	
	@XmlElement(name = "base64", required = true)
	public byte[] getBase64() {
		return _base64;
	}
	public void setBase64(byte[] base64) {
		this._base64 = base64;
	}
	
	@XmlTransient
	public Date getDate() {
		return _date;
	}
	public void setDate(Date date) {
		this._date = date;
	}
	
	@XmlTransient
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	@XmlTransient
	public boolean isApproved() {
		return _approved;
	}
	public void setApproved(boolean approved) {
		this._approved = approved;
	}
	
	private int _id;
	private String _filename;
	private String _contentType;
	private String _entityId;
	private String _entityClass;
	private byte[] _base64;
	private Date _date;
	private String _username;
	private boolean _approved;
	
}