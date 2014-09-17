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
