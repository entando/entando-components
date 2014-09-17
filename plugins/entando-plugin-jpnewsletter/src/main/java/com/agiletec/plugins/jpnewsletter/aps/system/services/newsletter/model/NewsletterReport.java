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