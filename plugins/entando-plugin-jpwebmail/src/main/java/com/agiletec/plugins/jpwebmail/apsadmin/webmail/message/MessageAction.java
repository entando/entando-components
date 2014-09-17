/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebmail.apsadmin.webmail.message;

import java.io.InputStream;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;

import com.agiletec.aps.system.ApsSystemUtils;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public class MessageAction extends AbstractMessageAction implements IMessageAction, ServletResponseAware {
	
	@Override
	public String openMessage() {
		try {
			Message message = this.getSelectedMessage();
			if (null == message) return "intro";
			this.setMessage(message);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteMessages");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String openAttachment() {
		try {
			Message message = this.getSelectedMessage();
			if (null == message) return "intro";
			BodyPart part = ((MimeMultipart) message.getContent()).getBodyPart(this.getAttachmentNumber());
			if (this.getSubPartAttachmentNumber()>-1) {
				BodyPart subPart = ((MimeMultipart) part.getContent()).getBodyPart(this.getSubPartAttachmentNumber());
				this.createResponse(subPart);
			} else {
				this.createResponse(part);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "openAttachment");
			return FAILURE;
		}
		return null;
	}
	
	private void createResponse(BodyPart part) throws Throwable {
		DataHandler handler = part.getDataHandler();
		this._response.setContentType(handler.getContentType());
		this._response.setHeader("Content-Disposition","attachment; filename="+handler.getName());
		ServletOutputStream out = _response.getOutputStream();
		try {
			InputStream is = handler.getInputStream();
			byte[] buffer = new byte[8789];
			int length = -1;
		    while ((length = is.read(buffer)) != -1) {
		    	out.write(buffer, 0, length);
		    	out.flush();
		    }
			is.close();
		} catch (Throwable t) {
			throw new ServletException("Errore in erogazione Attachment", t);
		}
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this._response = response;
	}
	
	public Message getMessage() {
		return _message;
	}
	public void setMessage(Message message) {
		this._message = message;
	}
	
	public int getAttachmentNumber() {
		return _attachmentNumber;
	}
	public void setAttachmentNumber(int attachmentNumber) {
		this._attachmentNumber = attachmentNumber;
	}
	
	public int getSubPartAttachmentNumber() {
		return _subPartAttachmentNumber;
	}
	public void setSubPartAttachmentNumber(int subPartAttachmentNumber) {
		this._subPartAttachmentNumber = subPartAttachmentNumber;
	}
	
	private Message _message;
	
	private int _attachmentNumber;
	private int _subPartAttachmentNumber = -1;
	
	private HttpServletResponse _response;
	
}