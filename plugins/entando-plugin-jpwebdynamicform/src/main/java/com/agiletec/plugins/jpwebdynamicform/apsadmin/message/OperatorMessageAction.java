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
package com.agiletec.plugins.jpwebdynamicform.apsadmin.message;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.plugins.jpmail.aps.services.mail.util.EmailAddressValidator;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.common.AbstractMessageAction;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.common.IOperatorMessageAction;

/**
 * Implementation of Operator actions on a Message.
 * @author E.Mezzano
 */
public class OperatorMessageAction extends AbstractMessageAction implements IOperatorMessageAction {
	
	@Override
	public void validate() {
		super.validate();
		this.checkFileName();
	}
	
	protected void checkFileName() {
		String fileName = this.getAttachmentFileName();
		if (null!=fileName && fileName.length()>0 && !fileName.matches("[a-zA-Z_\\.0-9]+")) {
			String[] args = {fileName};
			this.addFieldError("upload", this.getText("Errors.file.wrongFileNameFormat", args));
		}
	}
	
	@Override
	protected boolean isUserAllowed(Message message) {
		return true;
	}
	
	@Override
	public String newAnswer() {
		String result = null;
		try {
			result = this.view();
			if (result.equals(SUCCESS) && !this.checkEmailAddress()) {
				result = INPUT;
			}
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "newAnswer");
			return FAILURE;
		}
		return result;
	}
	
	@Override
	public String answer() {
		try {
			Message message = this.getMessage();
			if (message==null) {
				this.addActionError(this.getText("Message.message.notFound"));
				return "messageNotFound";
			} else if (!this.checkEmailAddress()) {
				return "messageNotFound";
			}
			Answer answer = this.prepareAnswer();
			boolean sent = this.getMessageManager().sendAnswer(answer);
			if (!sent) {
				this.addActionError(this.getText("Errors.answer.sendingError"));
				return INPUT;
			}
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "answer");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String trash() {
		try {
			Message message = this.getMessage();
			if (message==null) {
				this.addActionError(this.getText("Message.message.notFound"));
				return "messageNotFound";
			}
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "answer");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String delete() {
		try {
			Message message = this.getMessage();
			if (message==null) {
				this.addActionError(this.getText("Message.message.notFound"));
				return "messageNotFound";
			}
			this.getMessageManager().deleteMessage(message.getId());
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "answer");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public boolean isEmailAttribute(String attributeName) {
		return attributeName.equals(this.getEmailAttribute());
	}
	
	protected String getEmailAttribute() {
		if (this._emailAttributeName==null) {
			Message message = this.getMessage();
			String mailAttributeName = this.getMessageManager().getMailAttributeName(message.getTypeCode());
			if (null!=mailAttributeName) {
				this._emailAttributeName = mailAttributeName!=null ? mailAttributeName : "";
			}
		}
		return this._emailAttributeName;
	}
	
	/**
	 * Prepare an answer with the form parameters, ready to be saved.
	 * @return The answer ready to be saved.
	 */
	protected Answer prepareAnswer() {
		Answer answer = new Answer();
		answer.setMessageId(this.getId());
		answer.setOperator(this.getCurrentUser().getUsername());
		answer.setSendDate(new Date());
		answer.setText(this.getText());
		String fileName = this.getAttachmentFileName();
		File file = this.getAttachment();
		if (null!=fileName && null!=file && fileName.length()>0) {
			Properties attachments = new Properties();
			attachments.setProperty(fileName, file.getAbsolutePath());
			answer.setAttachments(attachments);
		}
		return answer;
	}
	
	protected boolean checkEmailAddress() {
		boolean validated = false;
		Message message = this.getMessage();
		String mailAttributeName = this.getMessageManager().getMailAttributeName(message.getTypeCode());
		if (null!=mailAttributeName) {
			ITextAttribute mailAttribute = (ITextAttribute) this.getMessage().getAttribute(mailAttributeName);
			if (null!=mailAttribute) {
				String eMail = mailAttribute.getText();
				if (null!=eMail && eMail.length()>0 && EmailAddressValidator.isValidEmailAddress(eMail)) {
					validated = true;
				}
			}
		}
		if (!validated) {
			this.addActionError(this.getText("Message.eMailAddress.notFound"));
		}
		return validated;
	}
	
	/**
	 * Returns the text of the answer.
	 * @return The text of the answer.
	 */
	public String getText() {
		return _text;
	}
	
	/**
	 * Sets the text of the answer.
	 * @param text The text of the answer.
	 */
	public void setText(String text) {
		this._text = text;
	}
	
	public File getAttachment() {
		return _attachment;
	}
	public void setAttachment(File attachment) {
		this._attachment = attachment;
	}
	
	public void setAttachmentContentType(String attachmentContentType) {
		// Not used
	}
	
	public String getAttachmentFileName() {
		return _attachmentFileName;
	}
	public void setAttachmentFileName(String attachmentFileName) {
		this._attachmentFileName = attachmentFileName;
	}
	
	private String _text;
	private String _emailAttributeName;
	
	private File _attachment;
	private String _attachmentFileName;
	
}