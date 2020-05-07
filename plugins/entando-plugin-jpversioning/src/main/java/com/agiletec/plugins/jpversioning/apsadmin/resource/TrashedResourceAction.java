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
package com.agiletec.plugins.jpversioning.apsadmin.resource;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMonoInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMultiInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.resource.ResourceFinderAction;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.ITrashedResourceManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrashedResourceAction extends ResourceFinderAction {
    
    private static final Logger logger = LoggerFactory.getLogger(TrashedResourceAction.class);
	
	private String _contentType;
	private String _nameFile;

	private String _resourceId;
	private String _size;
	private String _langCode;
    
	private ITrashedResourceManager _trashedResourceManager;
	
	public InputStream getInputStream() {
		InputStream documentInputStream = null;
		try {
			String resourceId = this.getResourceId();
			if (null ==  resourceId || resourceId.length() == 0) {
				return null;
			}
			ResourceInterface resource = this.getTrashedResourceManager().loadTrashedResource(resourceId);
			String mainGroup = resource.getMainGroup();
			UserDetails currentUser = this.getCurrentUser();
			if (!this.getAuthorizationManager().isAuthOnGroup(currentUser, mainGroup)) {
				return null;
			}
			int size = Integer.parseInt(this.getSize());
			ResourceInstance instance = null;
			if (resource.isMultiInstance()) {
				instance = ((AbstractMultiInstanceResource) resource).getInstance(size, getLangCode());
			} else {
				instance = ((AbstractMonoInstanceResource) resource).getInstance();
			}
			documentInputStream = this.getTrashedResourceManager().getTrashFileStream(resource, instance);
			this.setContentType(instance.getMimeType());
			this.setNameFile(instance.getFileName());
		} catch (Throwable t) {
			logger.error("Error extracting input stream", t);
		}
		return documentInputStream;
	}

	public String download(){
		return SUCCESS;
	}	

	public String restore(){
		try {
			String id = this.getResourceId();
			this.getTrashedResourceManager().restoreResource(id);
		} catch(Throwable t) {
			logger.error("Error on restore", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String trash() {
		return SUCCESS;
	}

	public String remove(){
		try {
			this.getTrashedResourceManager().removeFromTrash(this.getResourceId());
		} catch(Throwable t) {
			logger.error("Error removing trashed resource", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public ResourceInterface getTrashedResource(String id) throws Throwable{
		try {
			return this.getTrashedResourceManager().loadTrashedResource(id);
		} catch (Throwable t) {
			logger.error("Error extracting trashed resource", t);
			throw t;
		}
	}

	public List<String> getTrashedResources() throws Throwable {
		List<String> resources = new ArrayList<String>();
		try {
			resources = this.searchTrashedResources(this.getResourceTypeCode(), this.getText(), this.getCurrentUser());
		} catch (Throwable t) {
			logger.error("Error extracting trashed resources", t);
			throw t;
		}
		return resources;
	}

	private List<String> searchTrashedResources(String resourceTypeCode, String text, UserDetails currentUser) throws ApsSystemException {
		List<String> allowedGroups = new ArrayList<>();
		List<Group> userGroups = this.getAuthorizationManager().getUserGroups(currentUser);
		for (int i=0; i<userGroups.size(); i++) {
			Group group = userGroups.get(i);
			if (group.getName().equals(Group.ADMINS_GROUP_NAME)) {
				allowedGroups.clear();
				break;
			} else {
				allowedGroups.add(group.getName());
			}
		}
		List<String> resourcesId = this.getTrashedResourceManager().searchTrashedResourceIds(resourceTypeCode, text, allowedGroups);
		return resourcesId;
	}

	public String getResourceId() {
		return _resourceId;
	}
	public void setResourceId(String resourceId) {
		this._resourceId = resourceId;
	}

	public ITrashedResourceManager getTrashedResourceManager() {
		return _trashedResourceManager;
	}
	public void setTrashedResourceManager(ITrashedResourceManager trashedResourceManager) {
		this._trashedResourceManager = trashedResourceManager;
	}

	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	public String getContentType() {
		return _contentType;
	}

	public void setNameFile(String nameFile) {
		this._nameFile = nameFile;
	}
	public String getNameFile() {
		return _nameFile;
	}

	public void setSize(String size) {
		this._size = size;
	}
	public String getSize() {
		return _size;
	}

	public void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	public String getLangCode() {
		return _langCode;
	}

}