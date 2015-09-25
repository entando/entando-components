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
package org.entando.entando.plugins.jpwebform.aps.system.services.message.model;

import java.util.Date;
import java.util.Properties;

/**
 * Bean class representing an Answer to a Message.
 * @author E.Mezzano
 */
public class Answer {
	
	/**
	 * Returns the id of the answer.
	 * @return The id of the answer.
	 */
	public String getAnswerId() {
		return _id;
	}
	/**
	 * Sets the id of the answer.
	 * @param id The id of the answer.
	 */
	public void setAnswerId(String id) {
		this._id = id;
	}
	
	/**
	 * Returns the id of the message.
	 * @return The id of the message.
	 */
	public String getMessageId() {
		return _messageId;
	}
	/**
	 * Sets the id of the message.
	 * @param messageId The id of the message.
	 */
	public void setMessageId(String messageId) {
		this._messageId = messageId;
	}
	
	/**
	 * Returns the operator username.
	 * @return The operator username.
	 */
	public String getOperator() {
		return _operator;
	}
	/**
	 * Sets the operator username.
	 * @param operator The operator username.
	 */
	public void setOperator(String operator) {
		this._operator = operator;
	}
	
	/**
	 * Returns the date of sending of the answer.
	 * @return The date of sending of the answer.
	 */
	public Date getSendDate() {
		return _sendDate;
	}
	/**
	 * Sets the date of sending of the answer.
	 * @param sendDate The date of sending of the answer.
	 */
	public void setSendDate(Date sendDate) {
		this._sendDate = sendDate;
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
	
	/**
	 * Returns the attachments to send with the answer.
	 * @return The attachments of the answer. Is a map containing the name of the attachment name and the file path.
	 */
	public Properties getAttachments() {
		return _attachments;
	}
	/**
	 * Sets the attachments of the answer. Must be a map containing the name of the attachment and its input stream.
	 * @param attachments The attachments of the answer. Is a map containing the name of the attachment name and the file path.
	 */
	public void setAttachments(Properties attachments) {
		this._attachments = attachments;
	}
	
	private String _id;
	private String _messageId;
	private String _operator;
	private Date _sendDate;
	private String _text;
	private Properties _attachments;
	
}