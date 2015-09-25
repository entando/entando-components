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
package org.entando.entando.plugins.jpwebdynamicform.aps.system.services.api.model;

import java.util.Date;

import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;
import javax.xml.bind.annotation.XmlElement;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "messageAnswer")
@XmlType(propOrder = {"answerId", "messageId", "operator", "sendDate", "text"})
public class JAXBAnswer {
	
	public JAXBAnswer() {}
	
	public JAXBAnswer(Answer answer) {
		this.setAnswerId(answer.getAnswerId());
		this.setMessageId(answer.getMessageId());
		this.setOperator(answer.getOperator());
		this.setSendDate(answer.getSendDate());
		this.setText(answer.getText());
	}
	
	@XmlElement(name = "id", required = false)
	public String getAnswerId() {
		return _id;
	}
	public void setAnswerId(String id) {
		this._id = id;
	}
	
	@XmlElement(name = "messageId", required = false)
	public String getMessageId() {
		return _messageId;
	}
	public void setMessageId(String messageId) {
		this._messageId = messageId;
	}
	
	@XmlElement(name = "operator", required = false)
	public String getOperator() {
		return _operator;
	}
	public void setOperator(String operator) {
		this._operator = operator;
	}
	
	@XmlElement(name = "sendDate", required = false)
	public Date getSendDate() {
		return _sendDate;
	}
	public void setSendDate(Date sendDate) {
		this._sendDate = sendDate;
	}
	
	@XmlElement(name = "text", required = false)
	public String getText() {
		return _text;
	}
	public void setText(String text) {
		this._text = text;
	}
	
	private String _id;
	private String _messageId;
	private String _operator;
	private Date _sendDate;
	private String _text;
	
}
