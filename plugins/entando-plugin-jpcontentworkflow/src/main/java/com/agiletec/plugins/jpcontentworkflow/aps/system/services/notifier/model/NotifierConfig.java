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
public class NotifierConfig implements Cloneable {
	
	@Override
	public NotifierConfig clone() {
		NotifierConfig config = new NotifierConfig();
		config.setActive(this.isActive());
		config.setStartScheduler(this.getStartScheduler());
		config.setHoursDelay(this.getHoursDelay());
		config.setSenderCode(this.getSenderCode());
		//config.setMailAttrName(this.getMailAttrName());
		config.setHtml(this.isHtml());
		config.setSubject(this.getSubject());
		config.setHeader(this.getHeader());
		config.setTemplate(this.getTemplate());
		config.setFooter(this.getFooter());
		return config;
	}
	
	public boolean isActive() {
		return _active;
	}
	public void setActive(boolean active) {
		this._active = active;
	}
	
	public int getHoursDelay() {
		return _hoursDelay;
	}
	public void setHoursDelay(int hoursDelay) {
		this._hoursDelay = hoursDelay;
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
	/*
	public String getMailAttrName() {
		return _mailAttrName;
	}
	public void setMailAttrName(String mailAttrName) {
		this._mailAttrName = mailAttrName;
	}
	*/
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
	
	public String getTemplate() {
		return _template;
	}
	public void setTemplate(String template) {
		this._template = template;
	}
	
	public String getFooter() {
		return _footer;
	}
	public void setFooter(String footer) {
		this._footer = footer;
	}
	
	private boolean _active = false;
	private Date _startScheduler = new Date();
	private int _hoursDelay;
	
	private String _senderCode;
	//private String _mailAttrName;
	private boolean _html;
	private String _subject = "";
	private String _header = "";
	private String _template = "";
	private String _footer = "";
	
}