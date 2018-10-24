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

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.content.helper.IContentActionHelper;

/**
 * Classe action base delegata alla gestione base delle operazione sugli
 * attributi risorsa. L'azione rappresenta l'entry point per tutte le richieste
 * effettuate dall'interfaccia di redazione contenuto.
 *
 * @author E.Santoboni
 */
public class ResourceAttributeAction extends BaseAction implements IResourceAttributeAction {

    private static final Logger _logger = LoggerFactory.getLogger(ResourceAttributeAction.class);

    private String contentOnSessionMarker;

    private String attributeName;
    private String parentAttributeName;
    private int elementIndex = -1;
    private String resourceLangCode;
    private String resourceTypeCode;

    private String entryContentAnchorDest;

    private IContentActionHelper contentActionHelper;

    @Override
    public String chooseResource() {
        try {
            ResourceAttributeActionHelper.removeSessionParams(this.getRequest().getSession());
            this.getContentActionHelper().updateEntity(this.getContent(), this.getRequest());
            ResourceAttributeActionHelper.initSessionParams(this, this.getRequest());
            String resourceTypeCode = (String) this.getRequest().getSession().getAttribute(ResourceAttributeActionHelper.RESOURCE_TYPE_CODE_SESSION_PARAM);
            this.setResourceTypeCode(resourceTypeCode);
        } catch (Throwable t) {
            _logger.error("error in findResource", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    public String removeResource() {
        try {
            this.getContentActionHelper().updateEntity(this.getContent(), this.getRequest());
            ResourceAttributeActionHelper.initSessionParams(this, this.getRequest());
            ResourceAttributeActionHelper.removeResource(this.getRequest());
        } catch (Throwable t) {
            _logger.error("error in removeResource", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String backToEntryContent() {
        HttpSession session = this.getRequest().getSession();
        String anchorDest = ResourceAttributeActionHelper.buildEntryContentAnchorDest(session);
        this.setEntryContentAnchorDest(anchorDest);
        ResourceAttributeActionHelper.removeSessionParams(session);
        return SUCCESS;
    }

    /**
     * Restituisce il contenuto in sesione.
     *
     * @return Il contenuto in sesione.
     */
    public Content getContent() {
        return (Content) this.getRequest().getSession()
                .getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker());
    }

    public String getEntryContentAnchorDestFromRemove() {
        StringBuilder buffer = new StringBuilder("contentedit_");
        buffer.append(this.getResourceLangCode());
        buffer.append("_");
        if (null != this.getParentAttributeName()) {
            buffer.append(this.getParentAttributeName());
        } else {
            buffer.append(this.getAttributeName());
        }
        return buffer.toString();
    }

    public String getContentOnSessionMarker() {
        return contentOnSessionMarker;
    }

    public void setContentOnSessionMarker(String contentOnSessionMarker) {
        this.contentOnSessionMarker = contentOnSessionMarker;
    }

    @Override
    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    @Override
    public String getParentAttributeName() {
        return parentAttributeName;
    }

    public void setParentAttributeName(String parentAttributeName) {
        this.parentAttributeName = parentAttributeName;
    }

    @Override
    public int getElementIndex() {
        return elementIndex;
    }

    public void setElementIndex(int elementIndex) {
        this.elementIndex = elementIndex;
    }

    @Override
    public String getResourceLangCode() {
        return resourceLangCode;
    }

    public void setResourceLangCode(String resourceLangCode) {
        this.resourceLangCode = resourceLangCode;
    }

    @Override
    public String getResourceTypeCode() {
        return resourceTypeCode;
    }

    public void setResourceTypeCode(String resourceTypeCode) {
        this.resourceTypeCode = resourceTypeCode;
    }

    public String getEntryContentAnchorDest() {
        if (null == this.entryContentAnchorDest) {
            HttpSession session = this.getRequest().getSession();
            String anchorDest = ResourceAttributeActionHelper.buildEntryContentAnchorDest(session);
            this.setEntryContentAnchorDest(anchorDest);
        }
        return entryContentAnchorDest;
    }

    protected void setEntryContentAnchorDest(String entryContentAnchorDest) {
        this.entryContentAnchorDest = entryContentAnchorDest;
    }

    /**
     * Restituisce la classe helper della gestione contenuti.
     *
     * @return La classe helper della gestione contenuti.
     */
    protected IContentActionHelper getContentActionHelper() {
        return contentActionHelper;
    }

    /**
     * Setta la classe helper della gestione contenuti.
     *
     * @param contentActionHelper La classe helper della gestione contenuti.
     */
    public void setContentActionHelper(IContentActionHelper contentActionHelper) {
        this.contentActionHelper = contentActionHelper;
    }

}
