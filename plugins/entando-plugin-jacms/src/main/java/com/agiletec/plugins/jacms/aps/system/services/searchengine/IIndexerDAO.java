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
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import java.io.File;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;

/**
 * Data Access Object dedita alla indicizzazione di documenti.
 * @author W.Ambu
 */
public interface IIndexerDAO {
	
	/**
	 * Inizializzazione dell'indicizzatore.
	 * @param dir La cartella locale contenitore dei dati persistenti.
	 * @throws ApsSystemException In caso di errori.
	 */
	public void init(File dir) throws ApsSystemException;
	
	/**
	 * Aggiunge un contenuto nel db del motore di ricerca.
     * @param entity Il contenuto da aggiungere.
	 * @throws ApsSystemException In caso di errori.
	 */
	public void add(IApsEntity entity) throws ApsSystemException;
	
	/**
     * Cancella un documento indicizzato.
     * @param name Il nome del campo Field da utilizzare per recupero del documento.
     * @param value La chiave mediante il quale è stato indicizzato il documento.
     * @throws ApsSystemException In caso di errori.
     */
    public void delete(String name, String value) throws ApsSystemException;
    
    public void close();
	
	public void setLangManager(ILangManager langManager);
    
    public void setCategoryManager(ICategoryManager categoryManager);
    
	public static final String FIELD_PREFIX = "entity:"; 
    public static final String CONTENT_ID_FIELD_NAME = FIELD_PREFIX + "id";
    public static final String CONTENT_TYPE_FIELD_NAME = FIELD_PREFIX + "type";
    public static final String CONTENT_GROUP_FIELD_NAME = FIELD_PREFIX + "group";
    public static final String CONTENT_CATEGORY_FIELD_NAME = FIELD_PREFIX + "category";
	public static final String CONTENT_CATEGORY_SEPARATOR = "/";
    
    public static final String CONTENT_DESCRIPTION_FIELD_NAME = FIELD_PREFIX + IContentManager.CONTENT_DESCR_FILTER_KEY;
    public static final String CONTENT_LAST_MODIFY_FIELD_NAME = FIELD_PREFIX + IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY;
    public static final String CONTENT_CREATION_FIELD_NAME = FIELD_PREFIX + IContentManager.CONTENT_CREATION_DATE_FILTER_KEY;
    public static final String CONTENT_TYPE_CODE_FIELD_NAME = FIELD_PREFIX + IContentManager.ENTITY_TYPE_CODE_FILTER_KEY;
    public static final String CONTENT_MAIN_GROUP_FIELD_NAME = FIELD_PREFIX + IContentManager.CONTENT_MAIN_GROUP_FILTER_KEY;
	
}