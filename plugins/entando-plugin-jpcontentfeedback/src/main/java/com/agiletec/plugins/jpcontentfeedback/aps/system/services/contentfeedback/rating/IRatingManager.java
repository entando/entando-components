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
