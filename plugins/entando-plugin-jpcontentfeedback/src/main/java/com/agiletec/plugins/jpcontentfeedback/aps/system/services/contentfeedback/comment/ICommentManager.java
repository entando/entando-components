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
