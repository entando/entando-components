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
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public class NewsletterReport {
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	
	public Date getSendDate() {
		return _sendDate;
	}
	public void setSendDate(Date sendDate) {
		this._sendDate = sendDate;
	}
	
	public String getSubject() {
		return _subject;
	}
	public void setSubject(String subject) {
		this._subject = subject;
	}
	
	public Map<String, ContentReport> getContentReports() {
		return _contentReports;
	}
	public void setContentReports(Map<String, ContentReport> contentReports) {
		this._contentReports = contentReports;
	}
	public ContentReport getContentReport(String contentId) {
		return _contentReports.get(contentId);
	}
	public void addContentReport(ContentReport contentReport) {
		this._contentReports.put(contentReport.getContentId(), contentReport);
	}
	
	private int _id;
	private Date _sendDate;
	private String _subject;
	
	private Map<String, ContentReport> _contentReports = new HashMap<String, ContentReport>();
	
}