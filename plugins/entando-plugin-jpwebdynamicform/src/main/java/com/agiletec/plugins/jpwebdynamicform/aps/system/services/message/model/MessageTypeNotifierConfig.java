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
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model;

public class MessageTypeNotifierConfig {
	
	/**
	 * Returns the message type code.
	 * @return The message type code.
	 */
	public String getTypeCode() {
		return _typeCode;
	}
	
	/** 
	 * Sets the message type code.
	 * @param typeCode The message type code.
	 */
	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}
	
	/**
	 * Returns the store flag, indicating whether to save the evidence of the message or not.
	 * @return The store flag, indicating whether to save the evidence of the message or not.
	 */
	public boolean isStore() {
		return _store;
	}
	
	/**
	 * When set to 'true' this will save the Message into the DB.
	 * @param store boolean indicating whether to save the evidence of the message or not.
	 */
	public void setStore(boolean store) {
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
	public boolean isNotifiable() {
		return _notifiable;
	}
	
	/**
	 * Sets the flag that says if this message type is notifiable or not.
	 * @param notifiable The flag that says if this message type is notifiable or not.
	 */
	public void setNotifiable(boolean notifiable) {
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
	public String[] getRecipientsTo() {
		return _recipientsTo;
	}
	
	/**
	 * Sets the main recipients.
	 * @param recipientsTo The main recipients.
	 */
	public void setRecipientsTo(String[] recipientsTo) {
		this._recipientsTo = recipientsTo;
	}
	
	/**
	 * Returns the recipients in Carbon Copy.
	 * @return The recipients in Carbon Copy.
	 */
	public String[] getRecipientsCc() {
		return _recipientsCc;
	}
	
	/**
	 * Sets the recipients in Carbon Copy.
	 * @param recipientsCc The recipients in Carbon Copy.
	 */
	public void setRecipientsCc(String[] recipientsCc) {
		this._recipientsCc = recipientsCc;
	}
	
	/**
	 * Returns the recipients in Blind Carbon Copy.
	 * @return The recipients in Blind Carbon Copy.
	 */
	public String[] getRecipientsBcc() {
		return _recipientsBcc;
	}
	
	/**
	 * Sets the recipients in Blind Carbon Copy.
	 * @param recipientsBcc The recipients in Blind Carbon Copy.
	 */
	public void setRecipientsBcc(String[] recipientsBcc) {
		this._recipientsBcc = recipientsBcc;
	}
	
	/**
	 * Returns the message model.
	 * @return The message model.
	 */
	public MessageModel getMessageModel() {
		return this._messageModel;
	}
	
	/**
	 * Sets the message model.
	 * @param messageModel The message model.
	 */
	public void setMessageModel(MessageModel messageModel) {
		this._messageModel = messageModel;
	}
	
	private String _typeCode;
	private boolean _store;
	private String _mailAttrName;
	
	private boolean _notifiable;
	private String _senderCode;
	private String[] _recipientsTo;
	private String[] _recipientsCc;
	private String[] _recipientsBcc;
	private MessageModel _messageModel;
	
}