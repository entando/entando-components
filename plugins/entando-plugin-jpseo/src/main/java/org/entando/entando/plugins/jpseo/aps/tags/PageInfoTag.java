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

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.entando.entando.plugins.jpseo.aps.system.services.page.PageMetatag;
import org.entando.entando.plugins.jpseo.aps.system.services.page.SeoPageMetadata;

/**
 * Returns the information of the specified page. This tag can use the sub-tag
 * "ParameterTag" to add url parameters if the info attribute is set to 'url'.
 *
 * @author E.Santoboni
 */
public class PageInfoTag extends com.agiletec.aps.tags.PageInfoTag {

    private static final Logger _logger = LoggerFactory.getLogger(PageInfoTag.class);

    @Override
    public int doEndTag() throws JspException {
        ServletRequest request = this.pageContext.getRequest();
        RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
        Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
        try {
            IPageManager pageManager
                    = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.pageContext);
            IPage page = pageManager.getOnlinePage(this.getPageCode());
            if (null == page) {
                _logger.error("Required info for null page : inserted code '{}'", this.getPageCode());
            }
            if (this.getInfo() == null || this.getInfo().equals(CODE_INFO)) {
                this.setValue(page.getCode());
            } else if (this.getInfo().equals(TITLE_INFO)) {
                this.extractPageTitle(page, currentLang);
            } else if (this.getInfo().equals(DESCRIPTION_INFO)) {
                this.extractPageDescription(page, currentLang);
            } else if (this.getInfo().equals(URL_INFO)) {
                this.extractPageUrl(page, currentLang, reqCtx);
            } else if (this.getInfo().equals(OWNER_INFO)) {
                this.extractPageOwner(page, reqCtx);
            } else if (this.getInfo().equals(CHILD_OF_INFO)) {
                this.extractIsChildOfTarget(page);
            } else if (this.getInfo().equals(HAS_CHILD)) {
                boolean hasChild = false;
                String[] childrenCodes = page.getChildrenCodes();
                if (null != childrenCodes) {
                    for (String childrenCode : childrenCodes) {
                        IPage child = pageManager.getOnlinePage(childrenCode);
                        if (null != child) {
                            hasChild = true;
                            break;
                        }
                    }
                }
                this.setValue(Boolean.toString(hasChild));
            }
            this.evalValue();
        } catch (Throwable t) {
            _logger.error("error in doStartTag", t);
            throw new JspException("Error during tag initialization ", t);
        }
        this.release();
        return EVAL_PAGE;
    }
    
    // METODO UGUALE
    protected void extractPageDescription(IPage page, Lang currentLang) {
        PageMetadata pageMetadata = page.getMetadata();
        if (!(pageMetadata instanceof SeoPageMetadata) || null == ((SeoPageMetadata) pageMetadata).getDescriptions()) {
            return;
        }
        ApsProperties descriptions = ((SeoPageMetadata) pageMetadata).getDescriptions();
        PageMetatag metatag = null;
        if ((this.getLangCode() == null) || (this.getLangCode().equals(""))
                || (currentLang.getCode().equalsIgnoreCase(this.getLangCode()))) {
            metatag = (PageMetatag) descriptions.get(currentLang.getCode());
        } else {
            metatag = (PageMetatag) descriptions.get(this.getLangCode());
        }
        if (metatag == null || null == metatag.getValue()) {
            ILangManager langManager
                    = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
            metatag = (PageMetatag) descriptions.get(langManager.getDefaultLang().getCode());
        }
        if (null != metatag) {
            this.setValue(metatag.getValue());
        }
    }

    public static final String DESCRIPTION_INFO = "description";

}
