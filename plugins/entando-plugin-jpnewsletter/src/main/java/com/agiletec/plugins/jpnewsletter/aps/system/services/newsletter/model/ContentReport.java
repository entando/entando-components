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
	/**
	 * Aggiunge un destinatario della newsletter.
	 * @param username Lo username del destinatario.
	 * @param mailAddress L'indirizzo eMail del destinatario.
	 */
	public void addRecipient(String username, String eMail) {
		this._recipients.put(username, eMail);
	}
	
	private int _id;
	private String _contentId;
	private String _textBody;
	private String _htmlBody;
	private Map<String, String> _recipients = new HashMap<String, String>();
	
}