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

public interface IComment {

	/**
	 * Restituisce l'identificativo del commento
	 * @return L'identificativo del commento
	 */
	public abstract int getId();

	/**
	 * Restituisce l'identificativo del contentuto a cui è riferito il commento
	 * @return l'identificativo del contentuto
	 */
	public abstract String getContentId();

	/**
	 *
	 * @return The creation date
	 */
	public abstract Date getCreationDate();

	/**
	 *
	 * @return the comment text
	 */
	public abstract String getComment();

	/**
	 *
	 * @return The comment's status
	 */
	public abstract int getStatus();

	/**
	 *
	 * @return The comment's username
	 */
	public abstract String getUsername();

	public abstract void setId(int key);

	public abstract void setCreationDate(Date date);

}