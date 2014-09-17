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
package com.agiletec.plugins.jpwebmail.apsadmin.util;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;

/**
 * @author E.Santoboni
 */
public class AttachmentInfo {
	
	public AttachmentInfo(int number, BodyPart bodyPart) {
		this.setBodyPart(bodyPart);
		this.setNumber(number);
		try {
			DataHandler handler = bodyPart.getDataHandler();
			this.setFileName(handler.getName());
			this.setSize(bodyPart.getSize());
		} catch (MessagingException e) {
			throw new RuntimeException("Errore in creazione info Attachment", e);
		}
	}
	
	public AttachmentInfo(int number, BodyPart bodyPart, int subPartNumber) {
		this(number, bodyPart);
		this.setSubPartNumber(subPartNumber);
	}
	
	public int getNumber() {
		return _number;
	}
	public void setNumber(int number) {
		this._number = number;
	}
	
	public int getSubPartNumber() {
		return _subPartNumber;
	}
	public void setSubPartNumber(int subPartNumber) {
		this._subPartNumber = subPartNumber;
	}
	
	public int getSize() {
		return _size;
	}
	public void setSize(int size) {
		this._size = size;
	}
	public String getFileName() {
		return _fileName;
	}
	public void setFileName(String fileName) {
		this._fileName = fileName;
	}
	
	public BodyPart getBodyPart() {
		return _bodyPart;
	}
	public void setBodyPart(BodyPart bodyPart) {
		this._bodyPart = bodyPart;
	}
	
	private int _number;
	private int _subPartNumber = -1;
	private int _size;
	private String _fileName;
	
	private BodyPart _bodyPart;
	
}
