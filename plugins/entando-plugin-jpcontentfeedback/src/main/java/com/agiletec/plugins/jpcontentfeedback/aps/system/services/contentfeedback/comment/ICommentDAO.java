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

import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.IComment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.ICommentSearchBean;

/**
 * Classe DAO per la gestione della scrittura su dataSource dei commenti
 * @author D.Cherchi
 *
 */
public interface ICommentDAO {

	/**
	 * Effetua la strittura di un commento
	 * @param comment
	 */
	public void addComment(IComment comment);

	/**
	 * Effettua, se presente, la cancellazione del commento avente l'identificativo specificato
	 * @param id L'identificativo del commento da cancellare
	 */
	public void deleteComment(int id);

	/**
	 * Effetua la ricerca dei commenti in base all'insieme dei parametri di ricerca
	 * @param searchBean Il bean contente i parametri di ricerca.
	 * @return La lista degli identificativi dei commenti corrispondenti ai parametri di ricerca
	 */
	public List<String> searchCommentsId(ICommentSearchBean searchBean);

	/**
	 * Estra il commento dato il suo identificativo.
	 * @param id L'identificativo del commento da estrarre
	 * @return Il commento.
	 */
	public IComment getComment(int id);

	/**
	 * Effetua l'aggiornamento del commento dato il suo identificativo
	 * @param id L'identificativo del commento da aggiornare
	 * @param status Lo stato da settare al commento
	 */
	public void updateStatus(int id, int status);

}
