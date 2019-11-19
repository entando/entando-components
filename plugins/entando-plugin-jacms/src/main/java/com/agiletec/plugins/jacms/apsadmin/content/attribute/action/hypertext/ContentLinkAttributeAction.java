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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.hypertext;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.content.ContentFinderAction;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Classe action delegata alla gestione dei jAPSLinks (link interni al testo degli attributi Hypertext) su contenuto.
 * @author E.Santoboni
 */
public class ContentLinkAttributeAction extends ContentFinderAction {

	private static final Logger logger = LoggerFactory.getLogger(ContentLinkAttributeAction.class);
	
	private String contentOnSessionMarker;
    
    @Override
    public SearcherDaoPaginatedResult<String> getPaginatedContentsId(Integer limit) {
        SearcherDaoPaginatedResult<String> result = null;
		try {
			List<String> allowedGroups = this.getContentGroupCodes();
            EntitySearchFilter[] filters = this.getFilters();
            if (null != limit) {
                filters = ArrayUtils.add(filters, this.getPagerFilter(limit));
            }
			result = this.getContentManager().getPaginatedPublicContentsId(null, false, filters, allowedGroups);
		} catch (Exception e) {
			logger.error("error loading paginated contents", e);
			throw new RuntimeException("error loading paginated contents", e);
		}
		return result;
    }
	
    @Deprecated
	@Override
	public List<String> getContents() {
		List<String> result = null;
		try {
			List<String> allowedGroups = this.getContentGroupCodes();
			result = this.getContentManager().loadPublicContentsId(null, this.getFilters(), allowedGroups);
		} catch (Exception e) {
			logger.error("error in getContents", e);
			throw new RuntimeException("error in getContents", e);
		}
		return result;
	}
	
	/**
	 * Sovrascrittura del metodo della {@link ContentFinderAction}.
	 * Il metodo fà in modo di ricercare i contenuti che hanno, come gruppo proprietario o gruppo abilitato alla visualizzazione, 
	 * o il gruppo "free" o il gruppo proprietario del contenuto in redazione.
	 * @return La lista dei codici dei gruppi del contenuto.
	 */
	@Override
	protected List<String> getContentGroupCodes() {
		List<String> allowedGroups = new ArrayList<>();
		allowedGroups.add(Group.FREE_GROUP_NAME);
		Content currentContent = this.getContent();
		allowedGroups.add(currentContent.getMainGroup());
		return allowedGroups;
	}
	
	public Content getContent() {
		return (Content) this.getRequest().getSession()
				.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker());
	}
	
	public String getContentOnSessionMarker() {
		return contentOnSessionMarker;
	}
	public void setContentOnSessionMarker(String contentOnSessionMarker) {
		this.contentOnSessionMarker = contentOnSessionMarker;
	}
	
}
