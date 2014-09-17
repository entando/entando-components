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
