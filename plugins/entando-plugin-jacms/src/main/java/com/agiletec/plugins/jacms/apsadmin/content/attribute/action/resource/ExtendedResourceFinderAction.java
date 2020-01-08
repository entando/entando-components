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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.resource.ResourceFinderAction;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Classe action a servizio della gestione attributi risorsa, estensione della
 * action gestrice delle operazioni di ricerca risorse.
 *
 * @author E.Santoboni
 */
public class ExtendedResourceFinderAction extends ResourceFinderAction {

    private static final Logger logger = LoggerFactory.getLogger(ExtendedResourceFinderAction.class);

    private String contentOnSessionMarker;

    private String resourceId;
    private String entryContentAnchorDest;

    public String entryFindResource() {
        this.setCategoryCode(null);
        return SUCCESS;
    }

    @Override
    public List<String> getResources() throws Throwable {
        List<String> resourceIds = null;
        try {
            List<String> groupCodes = this.getGroupCodesForFilters();
            resourceIds = this.getResourceManager().searchResourcesId(this.createSearchFilters(), this.getCategoryCode(), groupCodes);
        } catch (Throwable t) {
            logger.error("error in getResources", t);
            throw t;
        }
        return resourceIds;
    }
    
    @Override
    public SearcherDaoPaginatedResult<String> getPaginatedResourcesId(Integer limit) {
        SearcherDaoPaginatedResult<String> result = null;
        try {
            List<String> groupCodes = this.getGroupCodesForFilters();
            FieldSearchFilter[] filters = this.createSearchFilters();
            if (null != limit) {
                filters = ArrayUtils.add(filters, this.getPagerFilter(limit));
            }
            List<String> categories = (StringUtils.isBlank(this.getCategoryCode())) ? null : Arrays.asList(this.getCategoryCode());
            result = this.getResourceManager().getPaginatedResourcesId(filters, categories, groupCodes);
        } catch (Throwable t) {
            logger.error("error in getPaginateResourcesId", t);
            throw new RuntimeException("error in getPaginateResourcesId", t);
        }
        return result;
    }
    
    private List<String> getGroupCodesForFilters() {
        List<String> groupCodes = new ArrayList<>();
        groupCodes.add(Group.FREE_GROUP_NAME);
        if (null != this.getContent().getMainGroup()) {
            groupCodes.add(this.getContent().getMainGroup());
        }
        return (groupCodes.contains(Group.ADMINS_GROUP_NAME)) ? null : groupCodes;
    }

    /**
     * Restituisce il contenuto in sessione.
     *
     * @return Il contenuto in sessione.
     */
    public Content getContent() {
        return (Content) this.getRequest().getSession()
                .getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker());
    }

    /**
     * Aggiunge una risorsa ad un Attributo.
     *
     * @return SUCCESS se è andato a buon fine, FAILURE in caso contrario
     */
    public String joinResource() {
        try {
            String resourceId = this.getResourceId();
            ResourceInterface resource = this.getResourceManager().loadResource(resourceId);
            this.buildEntryContentAnchorDest();
            List<ResourceInterface> resources = new ArrayList<>();
            resources.add(resource);
            ResourceAttributeActionHelper.joinResources(resources, this.getRequest());
        } catch (Throwable t) {
            logger.error("error in joinResource", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public ResourceInterface getResource(String resourceId) {
        try {
            ResourceInterface resource = this.getResourceManager().loadResource(resourceId);
            return resource;
        } catch (Throwable t) {
            logger.error("error loading resource", t);
            throw new RuntimeException("error loading resource " + resourceId, t);
        }
    }

    private void buildEntryContentAnchorDest() {
        HttpSession session = this.getRequest().getSession();
        String anchorDest = ResourceAttributeActionHelper.buildEntryContentAnchorDest(session);
        this.setEntryContentAnchorDest(anchorDest);
    }

    public boolean isOnEditContent() {
        return true;
    }

    public String getContentOnSessionMarker() {
        return contentOnSessionMarker;
    }

    public void setContentOnSessionMarker(String contentOnSessionMarker) {
        this.contentOnSessionMarker = contentOnSessionMarker;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getEntryContentAnchorDest() {
        if (null == this.entryContentAnchorDest) {
            this.buildEntryContentAnchorDest();
        }
        return entryContentAnchorDest;
    }

    protected void setEntryContentAnchorDest(String entryContentAnchorDest) {
        this.entryContentAnchorDest = entryContentAnchorDest;
    }

}
