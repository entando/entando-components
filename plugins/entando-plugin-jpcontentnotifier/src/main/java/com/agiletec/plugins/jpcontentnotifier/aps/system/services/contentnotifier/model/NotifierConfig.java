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

import java.util.Date;

/**
 * 
 * @author E.Santoboni
 */
public class NotifierConfig {
	
	public boolean isActive() {
		return _active;
	}
	public void setActive(boolean active) {
		this._active = active;
	}
	
	public long getHoursDelay() {
		return _hoursDelay;
	}
	public void setHoursDelay(long hoursDelay) {
		this._hoursDelay = hoursDelay;
	}
	
	public boolean isOnlyOwner() {
		return _onlyOwner;
	}
	public void setOnlyOwner(boolean onlyOwner) {
		this._onlyOwner = onlyOwner;
	}
	
	public Date getStartScheduler() {
		return _startScheduler;
	}
	public void setStartScheduler(Date startScheduler) {
		this._startScheduler = startScheduler;
	}
	
	public String getSenderCode() {
		return _senderCode;
	}
	public void setSenderCode(String senderCode) {
		this._senderCode = senderCode;
	}
	
	public String getMailAttrName() {
		return _mailAttrName;
	}
	public void setMailAttrName(String mailAttrName) {
		this._mailAttrName = mailAttrName;
	}
	
	public boolean isHtml() {
		return _html;
	}
	public void setHtml(boolean html) {
		this._html = html;
	}
	
	public String getSubject() {
		return _subject;
	}
	public void setSubject(String subject) {
		this._subject = subject;
	}
	
	public String getHeader() {
		return _header;
	}
	public void setHeader(String header) {
		this._header = header;
	}
	
	public String getTemplateInsert() {
		return _templateInsert;
	}
	public void setTemplateInsert(String templateInsert) {
		this._templateInsert = templateInsert;
	}
	
	public String getTemplateUpdate() {
		return _templateUpdate;
	}
	public void setTemplateUpdate(String templateUpdate) {
		this._templateUpdate = templateUpdate;
	}
	
	public boolean isNotifyRemove() {
		String templateRemove = this.getTemplateRemove();
		return templateRemove!=null && templateRemove.length()>0;
	}
	
	public String getTemplateRemove() {
		return _templateRemove;
	}
	public void setTemplateRemove(String templateRemove) {
		this._templateRemove = templateRemove;
	}
	
	public String getFooter() {
		return _footer;
	}
	public void setFooter(String footer) {
		this._footer = footer;
	}
	
	private boolean _active = false;
	private Date _startScheduler = new Date();
	private long _hoursDelay = 1;
	private boolean _onlyOwner = false;
	
	private String _senderCode;
	private String _mailAttrName;
	private boolean _html;
	private String _subject = "";
	private String _header = "";
	private String _templateInsert = "";
	private String _templateUpdate = "";
	private String _templateRemove = "";
	private String _footer = "";
	
}