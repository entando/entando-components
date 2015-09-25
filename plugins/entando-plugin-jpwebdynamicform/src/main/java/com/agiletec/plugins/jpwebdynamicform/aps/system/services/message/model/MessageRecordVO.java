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
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model;

import java.util.Date;

import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;

/**
 * The message record Value Object containing the data as taken from the DB.
 * @author E.Mezzano
 */
public class MessageRecordVO extends ApsEntityRecord {
	
	/**
	 * returns the username of the author of the message.
	 * @return The username of the author of the message.
	 */
	public String getUsername() {
		return _username;
	}
	/**
	 * Sets the username of the author of the message.
	 * @param username The username of the author of the message.
	 */
	public void setUsername(String username) {
		this._username = username;
	}
	
	/**
	 * Returns the date of creation of the message.
	 * @return The date of creation of the message.
	 */
	public Date getCreationDate() {
		return _creationDate;
	}
	/**
	 * Returns the date of creation of the message.
	 * @param creationDate The date of creation of the message.
	 */
	public void setCreationDate(Date creationDate) {
		this._creationDate = creationDate;
	}
	
	/**
	 * Returns the lang code of the author of the message.
	 * @return The lang code of the author of the message.
	 */
	public String getLangCode() {
		return _langCode;
	}
	/**
	 * Sets the lang code of the author of the message.
	 * @param langCode The lang code of the author of the message.
	 */
	public void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	
	private String _username;
	private Date _creationDate;
	private String _langCode;
	
}