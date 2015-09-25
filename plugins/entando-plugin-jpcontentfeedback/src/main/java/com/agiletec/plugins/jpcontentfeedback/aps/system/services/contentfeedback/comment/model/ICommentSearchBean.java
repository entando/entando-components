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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model;

import java.util.Date;


/**
 * Bean per la gestione dei parametri ricercabili dei commenti
 * @author D.Cherchi
 *
 */
public interface ICommentSearchBean {

	/**
	 * @return L'identificativo del contenuto da ricercare
	 */
	public String getContentId();

	/**
	 * @return La data iniziale di publicazione  per la ricerca su range di date
	 */
	public Date getCreationFROMDate();

	/**
	 *
	 * @return La data finale di publicazione per la ricerca su range di date
	 */
	public Date getCreationTODate();

	/**
	 *
	 * @return Il testo del commento da ricercare
	 */
	public String getComment();

	/**
	 *
	 * @return Lo stato dei commenti da ricercare
	 */
	public int getStatus();

	/**
	 *
	 * @return L'utente autore dei commenti da ricercare
	 */
	public String getUsername();
	
	public String getSort();
	
	public static final String SORT_ASC = "ASC";
	public static final String SORT_DESC = "DESC";

}