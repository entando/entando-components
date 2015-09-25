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
package com.agiletec.plugins.jpcontentfeedback.apsadmin.feedback;

import java.util.List;

import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.IComment;

/**
 * Action principale per la gestione del sistema contentfeedback.
 * Attraverso le sue funzioni è possibile operare su tutta la gestione dei rating e commenti di backend
 * @author D.Cherchi
 *
 */
public interface IContentFeedbackAction {

	/**
	 *	Esegue l'aggiornamento dello stato di un commento.
	 * @return Il codice del risultato dell'azione.
	 */
	public String updateStatus();

	/**
	 * Esegue la ricerca dei commenti in base ai possibili parametri del commento.
	 * @return Il codice del risultato dell'azione.
	 */
	public String search();

	/**
	 *	Estrae la lista di tutti gli identificativi dei commenti che rispettano i parametri di ricerca
	 * @return La lista degli identificativi dei commenti
	 */
	public List<String> getCommentIds();

	/**
	 * Esegue la cancellazione del commento selezionato.
	 * L'eliminazione di un commento genera anche l'eliminazione del rating eventualmente associato
	 * @return Il codice del risultato dell'azione.
	 */
	public String delete();

	/**
	 * Esegue il caricamento del commento selezionato
	 * @return  Il codice del risultato dell'azione.
	 */
	public String view();

	/**
	 * Estrae il commento in base al suo identificativo
	 * @param id L'identificativo del commento da caricare
	 * @return Il commento selezionato
	 */
	public IComment getComment(int id);

}
