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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRating;

public interface IRatingManager {

	/**
	 * Inserisce una votazione al contenuto desiderato
	 * @param contentId Il contenuto votato
	 * @param vote Il voto assegnato
	 * @throws ApsSystemException
	 */
	public void addRatingToContent(String contentId, int vote) throws ApsSystemException;

	/**
	 *  Inserisce una votazione al comemnto desiderato
	 * @param commentId Il commento votato
	 * @param vote Il voto assegnato
	 * @throws ApsSystemException
	 */
	public void addRatingToComment(int commentId, int vote) throws ApsSystemException;

	/**
	 * Restituisce l'oggetto rating associato al contenuto desiderato
	 * @param contentId L'identificativo del contenuto
	 * @return Il rating del centenuto
	 * @throws ApsSystemException
	 */
	public IRating getContentRating(String contentId) throws ApsSystemException;

	/**
	 * Restituisce l'oggetto rating associato al commento desiderato
	 * @param commentId L'identificativo del commento
	 * @return Il rating del commento
	 * @throws ApsSystemException
	 */
	public IRating getCommentRating(int commentId) throws ApsSystemException;

}
