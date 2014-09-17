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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.IComment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.ICommentSearchBean;

/**
 * Classe per la gestone dei commenti ai contenuti.
 * Permette di associare ad un contenuto, di associare una serie di commenti fatti dagli utenti del sistema
 * @author D.Cherchi
 */
public interface ICommentManager {

	/**
	 * Inserisce un commento
	 * @param comment
	 * @throws ApsSystemException
	 */
	public void addComment(IComment comment) throws ApsSystemException;

	/**
	 * Permette di aggiornare lo stato del commento.
	 * @param commentId L'identificativo del commento da aggiornare
	 * @param status Lo stato da assegnare al commento
	 * @throws ApsSystemException
	 */
	public void updateCommentStatus(int id, int status) throws ApsSystemException;

	/**
	 * Permetta la ricerca degli indici dei commenti che rispettano i parametri di ricerca
	 * @param searchBean Il bean contenente tutti i paramtri da ricercare
	 * @return La lista degli identificativi dei commenti
	 * @throws ApsSystemException
	 */
	public List<String> searchCommentIds(ICommentSearchBean searchBean) throws ApsSystemException;

	/**
	 * Restituisce il commento richiesto. Null se non trovato
	 * @param id L'identificativo del commento
	 * @return Il commento richiesto
	 * @throws ApsSystemException
	 */
	public IComment getComment(int id) throws ApsSystemException;


	/**
	 * Rimuove in maniera persistente il commento con l'id specifico
	 * @param id L'identificativo del commento da rimuovere
	 * @throws ApsSystemException
	 */
	public void deleteComment(int id)throws ApsSystemException;


}
