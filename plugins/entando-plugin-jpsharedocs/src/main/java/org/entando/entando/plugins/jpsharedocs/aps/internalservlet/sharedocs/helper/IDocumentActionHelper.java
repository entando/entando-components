/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpsharedocs.aps.internalservlet.sharedocs.helper;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.opensymphony.xwork2.ActionSupport;

public interface IDocumentActionHelper {
	
	public String getContentType(HttpServletRequest request);
	
	public String getUserCompleteName(UserDetails currentUser);
	
	public boolean checkAllowedOnContent(Content content, UserDetails currentUser, HttpServletRequest request, ActionSupport action);
	
	public List<Group> getAllowedGroups(UserDetails currentUser);
	
	public boolean checkCategories(Collection<String> categories, ActionSupport action);
	
	public boolean checkGroups(Collection<String> groups, UserDetails currentUser, ActionSupport action);
	
	public boolean checkUploadedDocument(File document, String fileName, ActionSupport action);
	
	public String saveTemporaryFile(File document, String fileName) throws ApsSystemException;
	
	public File getTemporaryFile(String fileName);
	
	public ResourceDataBean createResource(File document, String fileName, String description, 
			String mainGroup, String mimeType) throws ApsSystemException;
	
}