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
package org.entando.entando.plugins.jpseo.aps.tags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.FriendlyCodeVO;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ISeoMappingManager;
import org.entando.entando.plugins.jpseo.aps.system.services.url.PageURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;

public class SiteMapTag extends TagSupport {

    private static final Logger _logger = LoggerFactory.getLogger(SiteMapTag.class);

    @Override
    public int doStartTag() throws JspException {
        IURLManager urlManager = (IURLManager) ApsWebApplicationUtils.getBean(SystemConstants.URL_MANAGER, this.pageContext);
        IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.pageContext);
        ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
        try {
            RequestContext reqCtx = (RequestContext) this.pageContext.getRequest().getAttribute(RequestContext.REQCTX);
            IPage requestPage = null;
            if (reqCtx != null) {
                requestPage = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
            }
            IPage root = this.getRoot(pageManager, requestPage);
            List<String> urlList = new ArrayList<>();
            Lang lang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
            if (this.isDefaultLang()) {
                lang = langManager.getDefaultLang();
            }
            this.addPageUrl(root, urlList, requestPage, reqCtx, urlManager, pageManager, lang);
            this.addContentLinks(urlList, root, lang, reqCtx);
            String var = this.getVar();
            this.pageContext.setAttribute(var, urlList);
        } catch (Throwable t) {
            _logger.error("error in doStartTag", t);
            throw new JspException("Error during tag initialization ", t);
        }
        return super.doStartTag();
    }

    private void addContentLinks(List<String> urlList, IPage root, Lang lang, RequestContext reqCtx) throws Throwable {
        IURLManager urlManager = (IURLManager) ApsWebApplicationUtils.getBean(SystemConstants.URL_MANAGER, this.pageContext);
        IContentManager contentManager = (IContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER, this.pageContext);
        ISeoMappingManager seoManager = (ISeoMappingManager) ApsWebApplicationUtils.getBean(JpseoSystemConstants.SEO_MAPPING_MANAGER, this.pageContext);
        IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.pageContext);
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
            IPage requestPage, RequestContext reqCtx, IURLManager urlManager, IPageManager pageManager, Lang currentLang) {
        if (null == page) {
            return;
        }
        if (this.isPageAllowed(page, requestPage)) {
            PageURL seoUrl = new PageURL(urlManager, reqCtx);
            seoUrl.setLang(currentLang);
            seoUrl.setPage(page);
            urlList.add(urlManager.getURLString(seoUrl, reqCtx));
        }
        String[] childrenCodes = page.getChildrenCodes();
        for (int i = 0; i < childrenCodes.length; i++) {
            IPage current = pageManager.getOnlinePage(childrenCodes[i]);
            this.addPageUrl(current, urlList, requestPage, reqCtx, urlManager, pageManager, currentLang);
        }
    }

    @Override
    public int doEndTag() throws JspException {
        this.reset();
        return super.doEndTag();
    }

    private IPage getRoot(IPageManager pageManager, IPage requestPage) {
        String pageCode = this.getRootPage();
        IPage root = null;
        if (pageCode != null && pageCode.length() > 0) {
            root = pageManager.getOnlinePage(pageCode);
        }
        if (root == null) {
            root = pageManager.getOnlineRoot();
        }
        return root;
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

    private void reset() {
        this.setVar(null);
        this.setRootPage(null);
        this.setDefaultLang(false);
    }

    public String getVar() {
        return _var;
    }

    public void setVar(String var) {
        this._var = var;
    }

    public String getRootPage() {
        return _rootPage;
    }

    public void setRootPage(String rootPage) {
        this._rootPage = rootPage;
    }

    public boolean isDefaultLang() {
        return defaultLang;
    }

    public void setDefaultLang(boolean defaultLang) {
        this.defaultLang = defaultLang;
    }

    private String _var;
    private String _rootPage;
    private boolean defaultLang;

}
