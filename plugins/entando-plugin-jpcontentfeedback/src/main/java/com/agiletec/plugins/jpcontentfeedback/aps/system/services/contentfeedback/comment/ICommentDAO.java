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
