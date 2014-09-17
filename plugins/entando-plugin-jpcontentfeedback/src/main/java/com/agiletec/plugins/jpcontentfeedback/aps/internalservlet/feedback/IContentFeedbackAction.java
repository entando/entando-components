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
package com.agiletec.plugins.jpcontentfeedback.aps.internalservlet.feedback;

import java.util.List;

import java.util.Map;

import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.IComment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRating;

/**
 * Action principale per la gestione del sistema contentfeedback.
 * Attraverso le sue funzioni è possibile operare su tutta la gestione dei rating e commenti
 * @author D.Cherchi
 */
public interface IContentFeedbackAction {

	/**
	 * Inserisce un commento al contenuto specificato
	 * @return Il codice del risultato dell'azione.
	 */
	public String addComment();

	/**
	 * Effetua la cancellazione di un commento del contenuto specificato.
	 * @return Il codice del risultato dell'azione.
	 */
	public String delete();

	/**
	 * Carica un commento dato il suo identificativo
	 * @param commentId
	 * @return
	 */
	public IComment getComment(Integer commentId);


	/**
	 * Verifica se l'utente è autorizzato all'eventuale cancellazione del commento
	 * @param username
	 * @return true se autorizzato
	 */
	public boolean isAuthorizedToDelete(String username);

	/**
	 * Estrae la lista degli identificativi dei commenti inerenti un determinato contenuto
	 * @return La lista di identificativi
	 */
	public List<String> getContentCommentIds();


	/**
	 * Effetua l'inserimento di una votazione
	 * @return Il codice del risultato dell'azione.
	 */
	public String insertVote();

	/**
	 * Estrae il rating del commento in base al suo identificativo
	 * @param idComment L'identificativo del commento
	 * @return Il rating del commento
	 */
	public IRating getCommentRating(int idComment);

	/**
	 * Restituisce il rating del contenuto
	 * @return Il rating del centenuto
	 */
	public IRating getContentRating();

	/**
	 * Fornisce la mappa di tutti gli stati che può avere il commento
	 * @return La mappa degli stati
	 */
	public Map<Integer, String> getAllStatus();
}
