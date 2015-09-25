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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model;

import java.util.Date;

/**
 * Oggetto rappresentante un commento utente.
 * Un commento è sempre riferito ad un contenuto publicato nel portale
 * @author D.cherchi
 *
 */
public class Comment implements IComment {
	/**
	 * Restituisce l'identificativo del commento
	 * @return L'identificativo del commento
	 */
	public int getId() {
		return _id;
	}

	/**
	 * Setta l'identificativo del commento
	 * @param id L'identificativo del commento
	 */
	public void setId(int id) {
		this._id = id;
	}

	/**
	 * Restituisce l'identificativo del contentuto a cui è riferito il commento
	 * @return l'identificativo del contentuto
	 */
	public String getContentId() {
		return _contentId;
	}

	/**
	 * Setta l'identificativo del contentuto a cui è riferito il commento
	 * @param id L'identificativo del contentuto
	 */
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}

	/**
	 *
	 * @return The creation date
	 */
	public Date getCreationDate() {
		return _creationDate;
	}

	/**
	 *
	 * @param creationDate The creation date
	 */
	public void setCreationDate(Date creationDate) {
		this._creationDate = creationDate;
	}

	/**
	 *
	 * @return the comment text
	 */
	public String getComment() {
		return _comment;
	}

	/**
	 *
	 * @param comment The comment text
	 */
	public void setComment(String comment) {
		this._comment = comment;
	}

	/**
	 *
	 * @return The comment's status
	 */
	public int getStatus() {
		return _status;
	}

	/**
	 *
	 * @param status The comment's status
	 */
	public void setStatus(int status) {
		this._status = status;
	}

	/**
	 *
	 * @param username Comment creator username
	 */
	public void setUsername(String username) {
		this._username = username;
	}

	/**
	 * Comment creator username
	 * @return
	 */
	public String getUsername() {
		return _username;
	}

	public int _id;
	public String _contentId;
	public Date _creationDate;
	public String _comment;
	public int _status;
	private String _username;

	/**
	 * Status of not approved
	 */
	public static int STATUS_NOT_APPROVED = 1;

	/**
	 * Status of to approve
	 */
	public static int STATUS_TO_APPROVE = 2;

	/**
	 * Status of approved
	 */
	public static int STATUS_APPROVED = 3;



}
