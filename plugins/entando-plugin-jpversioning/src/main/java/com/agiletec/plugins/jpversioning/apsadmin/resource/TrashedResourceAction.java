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
package com.agiletec.plugins.jpversioning.apsadmin.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMonoInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMultiInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.resource.ResourceFinderAction;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.ITrashedResourceManager;

/**
 * @version 1.0
 * @author G.Cocco
 */
public class TrashedResourceAction extends ResourceFinderAction implements ITrashedResourceAction {
	
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
			if (!this.getAuthManager().isAuthOnGroup(currentUser, mainGroup)) {
				return null;
			}
			int size = Integer.parseInt(this.getSize());
			ResourceInstance instance = null;
			//String path = null;
			if (resource.isMultiInstance()) {
				instance = ((AbstractMultiInstanceResource) resource).getInstance(size, getLangCode());
				//Map<String,String> trashPathsForInstances = getTrashedResourceManager().resourceInstancesTrashFilePaths(resource);
				//path = trashPathsForInstances.get(this.getSize());
			} else {
				instance = ((AbstractMonoInstanceResource) resource).getInstance();
				//Map<String,String> trashPathsForInstances = getTrashedResourceManager().resourceInstancesTrashFilePaths(resource);
				//path = trashPathsForInstances.get("0");
			}
			documentInputStream = this.getTrashedResourceManager().getTrashFileStream(resource, instance);
			this.setContentType(instance.getMimeType());
			this.setNameFile(instance.getFileName());
			//File fileTemp = new File(path);
			//ApsSystemUtils.getLogger().info(" path " + fileTemp);
			//if (fileTemp.exists()) {
			//documentInputStream = new FileInputStream(fileTemp);
			//}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getInputStream");
		}
		return documentInputStream;
	}

	public String download(){
		return SUCCESS;
	}	

	@Override
	public String restore(){
		try {
			String id = this.getResourceId();
			this.getTrashedResourceManager().restoreResource(id);
		} catch(Throwable t) {
			t.printStackTrace();
			ApsSystemUtils.logThrowable(t, this, "restore");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String remove(){
		try {
			this.getTrashedResourceManager().removeFromTrash(this.getResourceId());
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "remove");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public ResourceInterface getTrashedResource(String id) throws Throwable{
		try {
			return this.getTrashedResourceManager().loadTrashedResource(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getTrashedResource");
			throw t;
		}
	}

	@Override
	public List<String> getTrashedResources() throws Throwable {
		List<String> resources = new ArrayList<String>();
		try {
			resources = this.searchTrashedResources(this.getResourceTypeCode(), this.getText(), this.getCurrentUser());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getTrashedResources");
			throw t;
		}
		return resources;
	}

	private List<String> searchTrashedResources(String resourceTypeCode, String text, UserDetails currentUser) throws ApsSystemException {
		List<String> allowedGroups = new ArrayList<String>();
		List<Group> userGroups = this.getAuthorizationManager().getGroupsOfUser(currentUser);
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

	public String getText() {
		return _text;
	}
	public void setText(String text) {
		this._text = text;
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
	
	public void setAuthManager(IAuthorizationManager authManager) {
		this._authManager = authManager;
	}
	public IAuthorizationManager getAuthManager() {
		return _authManager;
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

	private IAuthorizationManager _authManager;
	private ITrashedResourceManager _trashedResourceManager;
	
	private String _contentType;
	private String _nameFile;

	private String _resourceId;
	private String _size;
	private String _langCode;

	private String _text;

}