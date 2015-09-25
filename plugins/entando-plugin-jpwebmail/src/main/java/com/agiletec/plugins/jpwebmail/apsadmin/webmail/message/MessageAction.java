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