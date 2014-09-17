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

/**
 * @author E.Santoboni
 */
public class NewsletterContentReportVO extends ContentReport {
	
	public int getNewsletterId() {
		return _newsletterId;
	}
	public void setNewsletterId(int newsletterId) {
		this._newsletterId = newsletterId;
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
	
	private int _newsletterId;
	private Date _sendDate;
	private String _subject;
	
}