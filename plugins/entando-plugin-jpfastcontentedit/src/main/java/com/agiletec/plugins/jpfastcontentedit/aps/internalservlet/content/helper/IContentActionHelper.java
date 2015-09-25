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
package com.agiletec.plugins.jpfastcontentedit.aps.internalservlet.content.helper;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public interface IContentActionHelper extends com.agiletec.plugins.jacms.apsadmin.content.helper.IContentActionHelper {
	
	/**
	 * Extract a param from the current showlet.
	 * @param request The request.
	 * @param paramName The name of the showlet param.
	 * @return The value of the showlet param.
	 */
	public String extractShowletParam(HttpServletRequest request, String paramName);
	
	/**
	 * Extract the name of the content attribute containing the author param.
	 * @param request The request.
	 * @return The name of the content attribute containing the author param.
	 * @deprecated use getAuthorAttributeName
	 */
	public String extractAuthor(HttpServletRequest request);
	
	public String getAuthorAttributeName(HttpServletRequest request);
	
	/**
	 * Extract the name of the content attribute containing the author param.
	 * @param request The request.
	 * @return The name of the content attribute containing the author param.
	 */
	public String getAuthor(Content content, HttpServletRequest request);
	
	public void checkTypeLabels(Content content);
	
}