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

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.entando.entando.aps.system.services.searchengine.FacetedContentsResult;
import org.entando.entando.aps.system.services.searchengine.SearchEngineFilter;

/**
 * Interfaccia base per i Data Access Object dediti 
 * alle operazioni di ricerca ad uso del motore di ricerca interno.
 * @author E.Santoboni
 */
public interface ISearcherDAO {
	
	/**
	 * Inizializzazione del searcher.
	 * @param dir La cartella locale contenitore dei dati persistenti.
	 * @throws ApsSystemException In caso di errore
	 */
	public void init(File dir) throws ApsSystemException;
    
    public void setLangManager(ILangManager langManager);

    public void setTreeNodeManager(ITreeNodeManager treeNodeManager);
	
    public List<String> searchContentsId(SearchEngineFilter[] filters,
            SearchEngineFilter[] categories, Collection<String> allowedGroups) throws ApsSystemException;

    public FacetedContentsResult searchFacetedContents(SearchEngineFilter[] filters,
            SearchEngineFilter[] categories, Collection<String> allowedGroups) throws ApsSystemException;
	
    public void close();
	
}
