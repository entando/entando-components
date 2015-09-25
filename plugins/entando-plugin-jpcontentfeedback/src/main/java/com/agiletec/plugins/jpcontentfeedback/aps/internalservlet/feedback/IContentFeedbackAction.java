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
