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
package org.entando.entando.plugins.jpwebform.apsadmin.message.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.MessageModel;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.MessageTypeNotifierConfig;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.SmallMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;

public class NotifierConfigAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(NotifierConfigAction.class);

	@Override
	public void validate() {
		super.validate();
		try {
			String mailAttrName = this.getMailAttrName();
			boolean hasMailAddress = mailAttrName != null && mailAttrName.length() > 0;
			if (hasMailAddress) {
				IApsEntity prototype = this.getMessageManager().getEntityPrototype(this.getTypeCode());
				Object attribute = prototype.getAttribute(mailAttrName);
				if (attribute == null || !(attribute instanceof ITextAttribute)) {
					this.addFieldError("mailAttrName", this.getText("Errors.mailAttrName.notValid"));
				}
			}
			boolean isNotifiable = this.getNotifiable() != null && this.getNotifiable().booleanValue();
			if (hasMailAddress || isNotifiable) {
				// senderCode
				String senderCode = this.getSenderCode();
				if (senderCode == null || senderCode.length() == 0 || this.getMailManager().getMailConfig().getSender(senderCode) == null) {
					this.addFieldError("senderCode", this.getText("Errors.senderCode.notValid"));
				}
				// subject
				if (this.getSubjectModel() == null || this.getSubjectModel().length() == 0) {
					this.addFieldError("subjectModel", this.getText("Errors.subjectModel.required"));
				}
			}
			if (isNotifiable) {
				this.checkNotifierFields();
			}
		} catch (Throwable t) {
			_logger.error("Errors.genericError", t);
			this.addActionError(this.getText("Errors.genericError"));
		}
	}
	
	private void checkNotifierFields() throws ApsSystemException {
		// recipients
		if (this.getRecipientsTo().isEmpty() && this.getRecipientsCc().isEmpty() && 
				this.getRecipientsBcc().isEmpty()) {
			this.addActionError(this.getText("Errors.recipients.emptyList"));
		} else {
			this.checkRecipient(this.getRecipientsTo(), "recipientsTo");
			this.checkRecipient(this.getRecipientsCc(), "recipientsCc");
			this.checkRecipient(this.getRecipientsBcc(), "recipientsBcc");
		}
		// body
		if (this.getBodyModel() == null || this.getBodyModel().length() == 0) {
			this.addFieldError("bodyModel", this.getText("Errors.bodyModel.required"));
		}
	}
	
	private void checkRecipient(Set<String> recipient, String fieldName) {
		for (String address : recipient) {
			String fieldLabel = this.getText(fieldName);
			if (!address.matches(MAIL_REGEX)) {
				String[] args = { fieldLabel, address };
				this.addFieldError(fieldName, this.getText("Errors.recipient.address.notValid", args));
			}
		}
	}
	
	public List<SmallMessageType> getMessageTypes() {
		try {
			return this.getMessageManager().getSmallMessageTypes();
		} catch (Throwable t) {
			_logger.error("Error searching message types", t);
			throw new RuntimeException("Error searching message types", t);
		}
	}
	
	public String edit() {
		try {
			String typeCode = this.getTypeCode();
			if (this.getMessageManager().getSmallMessageTypesMap().get(typeCode)!=null) {
				MessageTypeNotifierConfig config = this.getMessageManager().getNotifierConfig(typeCode);
				this.populateForm(config);
			} else {
				this.addActionError(this.getText("Errors.messageType.notExistant", new String[] { typeCode }));
				return "wrongType";
			}
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String addAddress() {
		try {
			String address = this.getAddress();
			if (address == null || address.length()==0) {
				this.addFieldError("address", this.getText("Errors.address.required"));
			} else if (!address.matches(MAIL_REGEX)) {
				this.addFieldError("address", this.getText("Errors.address.notValid"));
			} else {
				Set<String> recipient = this.getRecipient(this.getRecipientType());
				if (recipient!=null) {
					recipient.add(address);
				}
				this.setAddress(null);
				return SUCCESS;
			}
		} catch (Throwable t) {
			_logger.error("error in addRecipient", t);
			return FAILURE;
		}
		return INPUT;
	}
	
	public String removeAddress() {
		try {
			String address = this.getAddress();
			if (address != null) {
				Set<String> recipient = this.getRecipient(this.getRecipientType());
				if (recipient!=null) {
					recipient.remove(address);
				}
				this.setAddress(null);
			}
		} catch (Throwable t) {
			_logger.error("error in removeRecipient", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String save() {
		try {
			String typeCode = this.getTypeCode();
			if (this.getMessageManager().getSmallMessageTypesMap().get(typeCode)!=null) {
				MessageTypeNotifierConfig config = this.prepareConfig();
				this.getMessageManager().saveNotifierConfig(config);
			} else {
				this.addActionError(this.getText("Errors.messageType.notExistant", new String[] { typeCode }));
				return "wrongType";
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void populateForm(MessageTypeNotifierConfig config) {
		if (config!=null) {
			this.setTypeCode(config.getTypeCode());
			this.setStore(new Boolean(config.isStore()));
			this.setMailAttrName(config.getMailAttrName());
			
			this.setNotifiable(new Boolean(config.isNotifiable()));
			this.setSenderCode(config.getSenderCode());
			this.setRecipientsTo(this.convertRecipients(config.getRecipientsTo()));
			this.setRecipientsCc(this.convertRecipients(config.getRecipientsCc()));
			this.setRecipientsBcc(this.convertRecipients(config.getRecipientsBcc()));
			
			MessageModel messageModel = config.getMessageModel();
			if (messageModel!=null) {
				this.setBodyModel(messageModel.getBodyModel());
				this.setSubjectModel(messageModel.getSubjectModel());
			}
			MessageModel messageModelResp = config.getMessageModelResp();
			if (messageModelResp!=null) {
				this.setBodyModelResp(messageModelResp.getBodyModel());
				this.setSubjectModelResp(messageModelResp.getSubjectModel());
			}
		}
	}
	
	private MessageTypeNotifierConfig prepareConfig() {
		MessageTypeNotifierConfig config = new MessageTypeNotifierConfig();
		config.setTypeCode(this.getTypeCode());
		config.setSenderCode(this.getSenderCode());
		config.setMailAttrName(this.getMailAttrName());
		Boolean store = this.getStore();
		config.setStore(store!=null && store.booleanValue());
		Boolean notifiable = this.getNotifiable();
		config.setNotifiable(notifiable!=null && notifiable.booleanValue());
		config.setRecipientsTo(this.convertRecipients(this.getRecipientsTo()));
		config.setRecipientsCc(this.convertRecipients(this.getRecipientsCc()));
		config.setRecipientsBcc(this.convertRecipients(this.getRecipientsBcc()));
		
		MessageModel messageModel = new MessageModel();
		messageModel.setSubjectModel(this.getSubjectModel());
		messageModel.setBodyModel(this.getBodyModel());
		config.setMessageModel(messageModel);
		
		MessageModel messageModelResp = new MessageModel();
		messageModelResp.setSubjectModel(this.getSubjectModelResp());
		messageModelResp.setBodyModel(this.getBodyModelResp());
		config.setMessageModelResp(messageModelResp);
		
		return config;
	}
	
	private Set<String> getRecipient(int type) {
		Set<String> recipient = null;
		switch (type) {
		case RECIPIENT_TO:
			recipient = this.getRecipientsTo();
			break;
		case RECIPIENT_CC:
			recipient = this.getRecipientsCc();
			break;
		case RECIPIENT_BCC:
			recipient = this.getRecipientsBcc();
			break;
		}
		return recipient;
	}
	
	private String[] convertRecipients(Collection<String> recipients) {
		if (recipients==null || recipients.isEmpty()) {
			return new String[] { };
		}
		String[] recipientsArray = new String[recipients.size()];
		int index = 0;
		for (String recipient : recipients) {
			recipientsArray[index++] = recipient;
		}
		return recipientsArray;
	}
	
	private Set<String> convertRecipients(String[] recipients) {
		Set<String> recipientsSet = new HashSet<String>();
		if (recipients!=null && recipients.length>0) {
			for (String recipient : recipients) {
				recipientsSet.add(recipient);
			}
		}
		return recipientsSet;
	}
	
	public SmallMessageType getMessageType() {
		try {
			return this.getMessageManager().getSmallMessageTypesMap().get(this.getTypeCode());
		} catch (Throwable t) {
			_logger.error("Error searching message type of code {}",this.getTypeCode(), t);
			throw new RuntimeException("Error searching message type of code " + this.getTypeCode(), t);
		}
	}
	
	public Map<String, String> getSenders() {
		try {
			return this.getMailManager().getMailConfig().getSenders();
		} catch (Throwable t) {
			_logger.error("Error loading mail senders", t);
			throw new RuntimeException("Error loading mail senders", t);
		}
	}
	
	public List<AttributeInterface> getTextAttributes() {
		if (this._textAttributes == null) {
			this._textAttributes = new ArrayList<AttributeInterface>();
			IApsEntity prototype = this.getMessageManager().getEntityPrototype(this.getTypeCode());
			for (AttributeInterface attribute : prototype.getAttributeList()) {
				if (attribute instanceof ITextAttribute) {
					this._textAttributes.add(attribute);
				}
			}
		}
		return this._textAttributes;
	}
	
	/**
	 * Returns the message type code.
	 * @return The message type code.
	 */
	public String getTypeCode() {
		return _typeCode;
	}
	
	/** Sets the message type code.
	 * @param typeCode The message type code.
	 */
	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}
	
	/**
	 * Returns the store flag, indicating whether to save the evidence of the message or not.
	 * @return The store flag, indicating whether to save the evidence of the message or not.
	 */
	public Boolean getStore() {
		return _store;
	}
	
	/**
	 * When set to 'true' this will save the Message into the DB.
	 * @param store boolean indicating whether to save the evidence of the message or not.
	 */
	public void setStore(Boolean store) {
		this._store = store;
	}
	
	/**
	 * Returns the name of the attribute containing the mail address.
	 * @return The name of the attribute containing the mail address.
	 */
	public String getMailAttrName() {
		return _mailAttrName;
	}
	
	/**
	 * Sets the name of the attribute containing the mail address.
	 * @param mailAttrName The name of the attribute containing the mail address.
	 */
	public void setMailAttrName(String mailAttrName) {
		this._mailAttrName = mailAttrName;
	}
	
	/**
	 * Returns true if this message type is notifiable, false otherwise.
	 * @return True if this message type is notifiable, false otherwise.
	 */
	public Boolean getNotifiable() {
		return _notifiable;
	}
	
	/**
	 * Sets the flag that says if this message type is notifiable or not.
	 * @param notifiable The flag that says if this message type is notifiable or not.
	 */
	public void setNotifiable(Boolean notifiable) {
		this._notifiable = notifiable;
	}
	
	/**
	 * Returns the sender code, one of those configured in the mail service configuration.
	 * @return The sender code, one of those configured in the mail service configuration.
	 */
	public String getSenderCode() {
		return _senderCode;
	}
	
	/**
	 * Sets the sender code, one of those configured in the mail service configuration.
	 * @param senderCode The sender code, one of those configured in the mail service configuration.
	 */
	public void setSenderCode(String senderCode) {
		this._senderCode = senderCode;
	}
	
	/**
	 * Returns the main recipients.
	 * @return The main recipients.
	 */
	public Set<String> getRecipientsTo() {
		return _recipientsTo;
	}
	
	/**
	 * Sets the main recipients.
	 * @param recipientsTo The main recipients.
	 */
	public void setRecipientsTo(Set<String> recipientsTo) {
		this._recipientsTo = recipientsTo;
	}
	
	/**
	 * Returns the recipients in Carbon Copy.
	 * @return The recipients in Carbon Copy.
	 */
	public Set<String> getRecipientsCc() {
		return _recipientsCc;
	}
	
	/**
	 * Sets the recipients in Carbon Copy.
	 * @param recipientsCc The recipients in Carbon Copy.
	 */
	public void setRecipientsCc(Set<String> recipientsCc) {
		this._recipientsCc = recipientsCc;
	}
	
	/**
	 * Returns the recipients in Blind Carbon Copy.
	 * @return The recipients in Blind Carbon Copy.
	 */
	public Set<String> getRecipientsBcc() {
		return _recipientsBcc;
	}
	
	/**
	 * Sets the recipients in Blind Carbon Copy.
	 * @param recipientsBcc The recipients in Blind Carbon Copy.
	 */
	public void setRecipientsBcc(Set<String> recipientsBcc) {
		this._recipientsBcc = recipientsBcc;
	}
	
	/**
	 * Returns the mail body.
	 * @return The mail body.
	 */
	public String getBodyModel() {
		return _bodyModel;
	}
	
	/**
	 * Sets the mail body.
	 * @param subjectModel Sets the mail body.
	 */
	public void setBodyModel(String bodyModel) {
		this._bodyModel = bodyModel;
	}
	
	/**
	 * Returns the mail subject.
	 * @return The mail subject.
	 */
	public String getSubjectModel() {
		return _subjectModel;
	}
	
	/**
	 * Sets the mail subject.
	 * @param subjectModel Sets the mail subject.
	 */
	public void setSubjectModel(String subjectModel) {
		this._subjectModel = subjectModel;
	}
	
	public int getRecipientType() {
		return recipientType;
	}
	public void setRecipientType(int recipientType) {
		this.recipientType = recipientType;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getBodyModelResp() {
		return _bodyModelResp;
	}

	public void setBodyModelResp(String bodyModelResp) {
		this._bodyModelResp = bodyModelResp;
	}

	public String getSubjectModelResp() {
		return _subjectModelResp;
	}

	public void setSubjectModelResp(String subjectModelResp) {
		this._subjectModelResp = subjectModelResp;
	}
	
	/**
	 * Returns the MessageManager.
	 * @return The MessageManager.
	 */
	protected IFormManager getMessageManager() {
		return _messageManager;
	}
	
	/**
	 * Sets the MessageManager. Must be setted with Spring bean injection.
	 * @param messageManager The MessageManager.
	 */
	public void setMessageManager(IFormManager messageManager) {
		this._messageManager = messageManager;
	}
	
	/**
	 * Returns the MailManager.
	 * @return The MailManager.
	 */
	public IMailManager getMailManager() {
		return mailManager;
	}
	
	/**
	 * Sets the MailManager. Must be setted with Spring bean injection.
	 * @param mailManager The MailManager.
	 */
	public void setMailManager(IMailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	private String _typeCode;
	private Boolean _store;
	private String _mailAttrName;
	
	private Boolean _notifiable;
	private String _senderCode;
	private Set<String> _recipientsTo = new HashSet<String>();
	private Set<String> _recipientsCc = new HashSet<String>();
	private Set<String> _recipientsBcc = new HashSet<String>();
	private String _bodyModel;
	private String _subjectModel;
	
	private String _bodyModelResp;
	private String _subjectModelResp;
	
	private int recipientType;
	private String address;
	
	private List<AttributeInterface> _textAttributes;
	
	private IFormManager _messageManager;
	private IMailManager mailManager;
	
	private static final String MAIL_REGEX = "(.*<.+@.+\\.[a-z]+>)|(.+@.+\\.[a-z]+)";
	
	public static final int RECIPIENT_TO = 1;
	public static final int RECIPIENT_CC = 2;
	public static final int RECIPIENT_BCC = 3;
	
}
