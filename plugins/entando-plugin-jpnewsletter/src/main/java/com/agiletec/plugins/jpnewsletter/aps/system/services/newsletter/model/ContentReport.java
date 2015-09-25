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

import java.util.HashMap;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public class ContentReport {
	
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
	
	public String getTextBody() {
		return _textBody;
	}
	public void setTextBody(String textBody) {
		this._textBody = textBody;
	}
	
	public String getHtmlBody() {
		return _htmlBody;
	}
	public void setHtmlBody(String htmlBody) {
		this._htmlBody = htmlBody;
	}
	
	public Map<String, String> getRecipients() {
		return _recipients;
	}
	public void setRecipients(Map<String, String> recipients) {
		this._recipients = recipients;
	}
	public void addRecipient(String username, String eMail) {
		this._recipients.put(username, eMail);
	}
	
	private int _id;
	private String _contentId;
	private String _textBody;
	private String _htmlBody;
	private Map<String, String> _recipients = new HashMap<String, String>();
	
}