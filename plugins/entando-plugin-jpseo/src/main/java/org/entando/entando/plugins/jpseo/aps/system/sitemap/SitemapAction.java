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
package org.entando.entando.plugins.jpseo.aps.system.sitemap;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.FriendlyCodeVO;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ISeoMappingManager;
import org.entando.entando.plugins.jpseo.aps.system.services.url.PageURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class SitemapAction extends BaseAction {

    private static final Logger _logger = LoggerFactory.getLogger(SitemapAction.class);

    private static final String header = "<urlset "
            + "xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" \n"
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n "
            + "xsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9 \n"
            + "http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\"> \n";

    public String extractSitemap() {
        try {
            String xml = header;
            List<String> urls = this.getListPage();
            for (int i = 0; i < urls.size(); i++) {
                xml += "<url><loc>" + urls.get(i) + "</loc></url>";
            }
            xml += "</urlset>";
            InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            //InputStream stream = this.getDatabaseManager().getTableDump(this.getTableName(), this.getDataSourceName(), this.getSubFolderName());
            this.setInputStream(is);
        } catch (Throwable t) {
            _logger.error("error in extractLastTableDump", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public List<String> getListPage() throws Throwable {
        List<String> urlList = new ArrayList<String>();
        IURLManager urlManager = (IURLManager) ApsWebApplicationUtils.getBean(SystemConstants.URL_MANAGER, this.getRequest());
        IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.getRequest());
        ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.getRequest());
        try {
            IPage root = pageManager.getOnlineRoot();
            Lang lang = langManager.getDefaultLang();
            this.addPageUrl(root, urlList, null, urlManager, pageManager, lang);
            this.addContentLinks(urlList, root, lang, null);
        } catch (Throwable t) {
            _logger.error("error in doStartTag", t);
            throw new ApsSystemException("Error during tag initialization ", t);
        }
        return urlList;
    }

    private void addContentLinks(List<String> urlList, IPage root, Lang lang, RequestContext reqCtx) throws Throwable {
        IURLManager urlManager = (IURLManager) ApsWebApplicationUtils.getBean(SystemConstants.URL_MANAGER, this.getRequest());
        IContentManager contentManager = (IContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER, this.getRequest());
        ISeoMappingManager seoManager = (ISeoMappingManager) ApsWebApplicationUtils.getBean(JpseoSystemConstants.SEO_MAPPING_MANAGER, this.getRequest());
        IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.getRequest());
        try {
            FieldSearchFilter filter = new FieldSearchFilter("contentid");
            FieldSearchFilter langFilter = new FieldSearchFilter("langcode", lang.getCode(), false);
            List<String> friendlyCodes = seoManager.searchFriendlyCode(new FieldSearchFilter[]{filter, langFilter});
            for (int i = 0; i < friendlyCodes.size(); i++) {
                String friendlyCode = friendlyCodes.get(i);
                FriendlyCodeVO vo = seoManager.getReference(friendlyCode);
                String contentId = vo.getContentId();
                if (null != contentId) {
                    String viewPageCode = contentManager.getViewPage(contentId);
                    if (null != viewPageCode && null != pageManager.getOnlinePage(viewPageCode)) {
                        IPage viewPage = pageManager.getOnlinePage(viewPageCode);
                        if (viewPage.isChildOf(root.getCode(), pageManager)) {
                            PageURL seoUrl = (PageURL) urlManager.createURL(reqCtx);
                            seoUrl.setPage(viewPage);
                            seoUrl.setLang(lang);
                            seoUrl.setFriendlyCode(friendlyCode);
                            seoUrl.addParam(SystemConstants.K_CONTENT_ID_PARAM, contentId);
                            urlList.add(seoUrl.getURL());
                        }
                    }
                }
            }
        } catch (Throwable t) {
            _logger.error("error in addContentLinks", t);
            throw new JspException("Error during tag initialization ", t);
        }
    }

    private void addPageUrl(IPage page, List<String> urlList,
            IPage requestPage, IURLManager urlManager, IPageManager pageManager, Lang currentLang) {
        if (null == page) {
            return;
        }
        if (this.isPageAllowed(page, requestPage)) {
            PageURL seoUrl = new PageURL(urlManager, null);
            seoUrl.setLang(currentLang);
            seoUrl.setPage(page);
            urlList.add(urlManager.getURLString(seoUrl, null));
        }
        String[] childrenCodes = page.getChildrenCodes();
        for (int i = 0; i < childrenCodes.length; i++) {
            IPage current = pageManager.getOnlinePage(childrenCodes[i]);
            this.addPageUrl(current, urlList, requestPage, urlManager, pageManager, currentLang);
        }
    }

    private boolean isPageAllowed(IPage page, IPage requestPage) {
        boolean allowed = false;
        if (page.isShowable()) {
            allowed = Group.FREE_GROUP_NAME.equals(page.getGroup());
            if (!allowed) {
                Collection<String> extraGroups = page.getExtraGroups();
                allowed = extraGroups != null && extraGroups.contains(Group.FREE_GROUP_NAME);
            }
            if (allowed && requestPage != null) {
                allowed = !page.getCode().equals(requestPage.getCode());
            }
        }
        return allowed;
    }

    public InputStream getInputStream() {
        return _inputStream;
    }

    protected void setInputStream(InputStream inputStream) {
        this._inputStream = inputStream;
    }

    private InputStream _inputStream;

}
