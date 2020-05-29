/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.aps.system.services.linkresolver;

import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ISeoMappingManager;
import org.entando.entando.plugins.jpseo.aps.system.services.url.PageURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.attribute.AbstractResourceAttribute;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * @author E.Santoboni
 */
public class LinkResolverManager extends com.agiletec.plugins.jacms.aps.system.services.linkresolver.LinkResolverManager {

    private static final Logger _logger = LoggerFactory.getLogger(LinkResolverManager.class);

    private ISeoMappingManager seoMappingManager;

    @Override
    public String resolveLink(SymbolicLink symbolicLink, String contentId, RequestContext reqCtx) {
        if (null == symbolicLink) {
            _logger.error("Null Symbolic Link");
            return "";
        }
        String url = null;
        try {
            if (symbolicLink.getDestType() == SymbolicLink.URL_TYPE) {
                url = symbolicLink.getUrlDest();
            } else if (symbolicLink.getDestType() == SymbolicLink.PAGE_TYPE) {
                PageURL pageUrl = (PageURL) this.getUrlManager().createURL(reqCtx);
                pageUrl.setPageCode(symbolicLink.getPageDest());
                url = pageUrl.getURL();
            } else if (symbolicLink.getDestType() == SymbolicLink.CONTENT_ON_PAGE_TYPE) {
                PageURL pageUrl = (PageURL) this.getUrlManager().createURL(reqCtx);
                pageUrl.setPageCode(symbolicLink.getPageDest());
                pageUrl.addParam(SystemConstants.K_CONTENT_ID_PARAM, symbolicLink.getContentDest());
                url = pageUrl.getURL();
            } else if (symbolicLink.getDestType() == SymbolicLink.CONTENT_TYPE) {
                PageURL pageUrl = (PageURL) this.getUrlManager().createURL(reqCtx);
                String contentIdDest = symbolicLink.getContentDest();
                String pageCode = this.getContentPageMapperManager().getPageCode(contentIdDest);
                boolean forwardToDefaultPage = !this.isPageAllowed(reqCtx, pageCode);
                if (forwardToDefaultPage) {
                    String viewPageCode = this.getContentManager().getViewPage(contentIdDest);
                    pageUrl.setPageCode(viewPageCode);
                    String langCode = null;
                    if (null != reqCtx) {
                        Lang lang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
                        if (null != lang) {
                            langCode = lang.getCode();
                        }
                    }
                    String friendlyCode = this.getSeoMappingManager().getContentReference(contentIdDest, langCode);
                    if (null != friendlyCode) {
                        pageUrl.setFriendlyCode(friendlyCode);
                    }
                    pageUrl.addParam(SystemConstants.K_CONTENT_ID_PARAM, contentIdDest);
                } else {
                    pageUrl.setPageCode(pageCode);
                }
                url = pageUrl.getURL();
            } else if (symbolicLink.getDestType() == SymbolicLink.RESOURCE_TYPE) {
                ResourceInterface resource = this.getResourceManager().loadResource(symbolicLink.getResourceDest());
                if (null != resource) {
                    url = resource.getDefaultUrlPath();
                    if (!resource.getMainGroup().equals(Group.FREE_GROUP_NAME)) {
                        if (!url.endsWith("/")) {
                            url += "/";
                        }
                        url += AbstractResourceAttribute.REFERENCED_RESOURCE_INDICATOR + "/" + contentId + "/";
                    }
                }
            }
        } catch (Throwable t) {
            _logger.error("Error resolve link from SymbolicLink", t);
            throw new RuntimeException("Error resolve link from SymbolicLink", t);
        }
        return url;
    }

    protected ISeoMappingManager getSeoMappingManager() {
        return seoMappingManager;
    }
    public void setSeoMappingManager(ISeoMappingManager seoMappingManager) {
        this.seoMappingManager = seoMappingManager;
    }

}
