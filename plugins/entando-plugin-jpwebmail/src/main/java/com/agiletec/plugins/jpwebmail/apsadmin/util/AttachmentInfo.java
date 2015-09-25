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
