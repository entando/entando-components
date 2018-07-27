/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.apsadmin.resource;

import java.util.List;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.system.AbstractTreeAction;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.resource.helper.IResourceActionHelper;

/**
 * Classe astratta base per le Action di gestione risorse.
 * @author E.Santoboni
 */
public abstract class AbstractResourceAction extends AbstractTreeAction {
	
	public List<String> getResourceTypeCodes() {
		return this.getResourceManager().getResourceTypeCodes();
	}
	
	public List<Group> getAllowedGroups() {
		return super.getActualAllowedGroups();
	}
	
	/**
	 * Restutuisce la root delle categorie.
	 * @return La root delle categorie.
	 */
	public Category getCategoryRoot() {
		return this.getCategoryManager().getRoot();
	}
	
	/**
	 * Restituisce una risorsa in base all'identificativo.
	 * @param resourceId L'identificativo della risorsa da caricare.
	 * @return La risorsa cercata.
	 * @throws Throwable In caso di errore.
	 */
	public ResourceInterface loadResource(String resourceId) throws Throwable {
		return this.getResourceManager().loadResource(resourceId);
	}
	
	public String getResourceTypeCode() {
		return _resourceTypeCode;
	}
	public void setResourceTypeCode(String resourceTypeCode) {
		this._resourceTypeCode = resourceTypeCode;
	}
	
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	
	protected IResourceManager getResourceManager() {
		return _resourceManager;
	}
	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}
	
	protected IResourceActionHelper getResourceActionHelper() {
		return _resourceActionHelper;
	}
	public void setResourceActionHelper(IResourceActionHelper resourceActionHelper) {
		this._resourceActionHelper = resourceActionHelper;
	}
	
	private String _resourceTypeCode;
	private IResourceManager _resourceManager;
	private ICategoryManager _categoryManager;
	private IResourceActionHelper _resourceActionHelper;
	
}