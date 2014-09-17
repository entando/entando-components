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

public interface IComment {

	/**
	 * Restituisce l'identificativo del commento
	 * @return L'identificativo del commento
	 */
	public abstract int getId();

	/**
	 * Restituisce l'identificativo del contentuto a cui è riferito il commento
	 * @return l'identificativo del contentuto
	 */
	public abstract String getContentId();

	/**
	 *
	 * @return The creation date
	 */
	public abstract Date getCreationDate();

	/**
	 *
	 * @return the comment text
	 */
	public abstract String getComment();

	/**
	 *
	 * @return The comment's status
	 */
	public abstract int getStatus();

	/**
	 *
	 * @return The comment's username
	 */
	public abstract String getUsername();

	public abstract void setId(int key);

	public abstract void setCreationDate(Date date);

}