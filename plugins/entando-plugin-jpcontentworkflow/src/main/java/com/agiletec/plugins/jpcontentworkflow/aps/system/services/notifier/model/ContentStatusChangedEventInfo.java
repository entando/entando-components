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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.model;

import java.util.Date;

/**
 * @author E.Santoboni
 */
public class ContentStatusChangedEventInfo {
	
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
	
	public String getMainGroup() {
		return _mainGroup;
	}
	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}
	
	public String getStatus() {
		return _status;
	}
	public void setStatus(String status) {
		this._status = status;
	}
	
	private int _id;
	private String _contentId;
	private String _contentTypeCode;
	private String _contentDescr;
	private Date _date;
	private String _mainGroup;
	private String _status;
	
}