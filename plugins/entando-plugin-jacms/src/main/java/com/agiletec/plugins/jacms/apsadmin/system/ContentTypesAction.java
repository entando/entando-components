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
package com.agiletec.plugins.jacms.apsadmin.system;

import com.agiletec.apsadmin.system.entity.type.EntityTypesAction;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import org.slf4j.*;

public class ContentTypesAction extends EntityTypesAction implements IContentReferencesReloadingAction {

	private static final Logger logger = LoggerFactory.getLogger(ContentTypesAction.class);

	private transient ICmsSearchEngineManager searchEngineManager;

	@Override
	public String reloadContentsIndexes() {
		try {
			this.getSearchEngineManager().startReloadContentsReferences();
			logger.info("Reload context index started");
		} catch (Throwable t) {
			logger.error("error in reloadContentsIndexs", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public int getSearcherManagerStatus() {
		return searchEngineManager.getStatus();
	}
	
	protected ICmsSearchEngineManager getSearchEngineManager() {
		return searchEngineManager;
	}

	public void setSearchEngineManager(ICmsSearchEngineManager searchEngineManager) {
		this.searchEngineManager = searchEngineManager;
	}
}
