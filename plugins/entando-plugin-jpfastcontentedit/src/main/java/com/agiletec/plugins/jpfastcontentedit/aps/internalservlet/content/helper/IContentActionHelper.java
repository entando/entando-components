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