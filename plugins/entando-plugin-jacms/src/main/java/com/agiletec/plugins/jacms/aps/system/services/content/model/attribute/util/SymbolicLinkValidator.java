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
package com.agiletec.plugins.jacms.aps.system.services.content.model.attribute.util;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import java.util.Set;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import java.util.HashSet;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Classe di utilità per la validazione degli attributi in cui negli elementi
 * compositivi vi può essere in link rappresentato dal proprio link simbolico.
 *
 * @author E.Santoboni
 */
public class SymbolicLinkValidator {

    public SymbolicLinkValidator(IContentManager contentManager, IPageManager pageManager, IResourceManager resourceManager) {
        this.setContentManager(contentManager);
        this.setPageManager(pageManager);
        this.setResourceManager(resourceManager);
    }

    /**
     * Analizza un link simbolico ne verifica la correttezza e restituisce un
     * intero rappresentante il codice dell'eventuale errore riscontrato. In
     * caso di link a pagina ed a contenuto controlla la validità dell'elemento
     * referenziato.
     *
     * @param symbLink Il link simbolico da verificare.
     * @param content Il contenuto corrente in fase di verifica.
     * @return Il {@link AttributeFieldError}.
     */
    public AttributeFieldError scan(SymbolicLink symbLink, Content content) {
        if (symbLink != null) {
            switch (symbLink.getDestType()) {
                case SymbolicLink.URL_TYPE:
                    return null;
                case SymbolicLink.PAGE_TYPE:
                    return this.checkPageDest(symbLink, content);
                case SymbolicLink.CONTENT_TYPE:
                    return this.checkContentDest(symbLink, content);
                case SymbolicLink.CONTENT_ON_PAGE_TYPE:
                    return this.checkContentOnPageDest(symbLink, content);
                case SymbolicLink.RESOURCE_TYPE:
                    return this.checkResourceDest(symbLink, content);
            }
        }
        return null;
    }

    protected AttributeFieldError checkPageDest(SymbolicLink symbLink, Content content) {
        String pageCode = symbLink.getPageDest();
        IPage page = this.getPageManager().getOnlinePage(pageCode);
        if (null == page) {
            AttributeFieldError result = new AttributeFieldError(null, ICmsAttributeErrorCodes.INVALID_PAGE, null);
            result.setMessage("The destination page must be published");
            return result;
        } else if (this.isVoidPage(page)) {
            AttributeFieldError result = new AttributeFieldError(null, ICmsAttributeErrorCodes.VOID_PAGE, null);
            result.setMessage("The destination page must have a widget set");
            return result;
        } else {
            Set<String> pageGroups = new HashSet<>();
            pageGroups.add(page.getGroup());
            Set<String> extraGroups = page.getMetadata().getExtraGroups();
            if (null != extraGroups) {
                pageGroups.addAll(extraGroups);
            }
            if (pageGroups.contains(Group.FREE_GROUP_NAME)) {
                return null;
            }
            Set<String> linkingContentGroups = this.extractContentGroups(content);
            boolean check = CollectionUtils.containsAll(pageGroups, linkingContentGroups);
            if (!check) {
                boolean first = true;
                StringBuilder sb = new StringBuilder("The destination page must belong to the group(s): ");
                for (String group : linkingContentGroups) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(", ");
                    }
                    sb.append(group);
                }
                AttributeFieldError result = new AttributeFieldError(null, ICmsAttributeErrorCodes.INVALID_PAGE_GROUPS, null);
                result.setMessage(sb.toString());
                return result;
            }
        }
        return null;
    }

    protected AttributeFieldError checkContentDest(SymbolicLink symbLink, Content content) {
        Content linkedContent = null;
        try {
            linkedContent = this.getContentManager().loadContent(symbLink.getContentDest(), true);
        } catch (Throwable e) {
            throw new RuntimeException("Errore in caricamento contenuto " + symbLink.getContentDest(), e);
        }
        if (null == linkedContent) {
            return new AttributeFieldError(null, ICmsAttributeErrorCodes.INVALID_CONTENT, null);
        }
        Set<String> linkedContentGroups = this.extractContentGroups(linkedContent);
        if (linkedContentGroups.contains(Group.FREE_GROUP_NAME)) {
            return null;
        }
        Set<String> linkingContentGroups = this.extractContentGroups(content);
        boolean check = CollectionUtils.containsAll(linkedContentGroups, linkingContentGroups);
        if (!check) {
            return new AttributeFieldError(null, ICmsAttributeErrorCodes.INVALID_CONTENT_GROUPS, null);
        }
        return null;
    }

    protected AttributeFieldError checkResourceDest(SymbolicLink symbLink, Content content) {
        ResourceInterface linkedResource = null;
        try {
            linkedResource = _resourceManager.loadResource(symbLink.getResourceDest());
        } catch (Throwable e) {
            throw new RuntimeException("Error loading resource " + symbLink.getResourceDest(), e);
        }
        if (null == linkedResource) {
            return new AttributeFieldError(null, ICmsAttributeErrorCodes.INVALID_RESOURCE, null);
        }

        return null;
    }


    private Set<String> extractContentGroups(Content content) {
        Set<String> groups = new HashSet<>();
        groups.add(content.getMainGroup());
        groups.addAll(content.getGroups());
        return groups;
    }

    protected AttributeFieldError checkContentOnPageDest(SymbolicLink symbLink, Content content) {
        AttributeFieldError error = this.checkContentDest(symbLink, content);
        if (error == null) {
            error = this.checkPageDest(symbLink, content);
        }
        return error;
    }

    /**
     * Metodo di servizio: verifica che la pagina abbia dei widget configurate.
     * Restituisce true nel caso tutti i frame siano vuoti, false in caso che
     * anche un frame sia occupato da una widget.
     *
     * @param page La pagina da controllare.
     * @return true nel caso tutti i frame siano vuoti, false in caso che anche
     * un frame sia occupato da una widget.
     */
    protected boolean isVoidPage(IPage page) {
        Widget[] widgets = page.getWidgets();
        for (int i = 0; i < widgets.length; i++) {
            if (null != widgets[i]) {
                return false;
            }
        }
        return true;
    }

    protected IContentManager getContentManager() {
        return _contentManager;
    }

    protected void setContentManager(IContentManager contentManager) {
        this._contentManager = contentManager;
    }

    protected IPageManager getPageManager() {
        return _pageManager;
    }

    protected void setPageManager(IPageManager pageManager) {
        this._pageManager = pageManager;
    }

    public void setResourceManager(IResourceManager resourceManager) {
        this._resourceManager = resourceManager;
    }

    private IContentManager _contentManager;
    private IPageManager _pageManager;
    private IResourceManager _resourceManager;

}
