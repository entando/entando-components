/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
