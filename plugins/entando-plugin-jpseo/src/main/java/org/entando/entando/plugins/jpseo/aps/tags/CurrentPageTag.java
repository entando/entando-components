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

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
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
 * @author E.Santoboni
 */
public class CurrentPageTag extends com.agiletec.aps.tags.CurrentPageTag {

    private static final Logger _logger = LoggerFactory.getLogger(CurrentPageTag.class);

    public static final String DESCRIPTION_INFO = "description";

    @Override
    public int doEndTag() throws JspException {
        ServletRequest request = this.pageContext.getRequest();
        RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
        Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
        try {
            IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
            if (this.getParam() == null || this.getParam().equals(TITLE_INFO)) {
                this.extractPageTitle(page, currentLang, reqCtx);
            } else if (this.getParam().equals(DESCRIPTION_INFO)) {
                this.extractPageDescription(page, currentLang, reqCtx);
            } else if (this.getParam().equals(CODE_INFO)) {
                this.setValue(page.getCode());
            } else if (this.getParam().equals(OWNER_INFO)) {
                this.extractPageOwner(page, reqCtx);
            } else if (this.getInfo().equals(CHILD_OF_INFO)) {
                this.extractIsChildOfTarget(page);
            } else if (this.getInfo().equals(HAS_CHILD)) {
                boolean hasChild = false;
                String[] childrenCodes = page.getChildrenCodes();
                if (null != page.getChildrenCodes()) {
                    IPageManager pageManager
                            = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.pageContext);
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
            if (null == this.getValue()) {
                this.setValue("");
            }
            this.evalValue();
        } catch (Throwable t) {
            _logger.error("error in doStartTag", t);
            throw new JspException("Error during tag initialization ", t);
        }
        return EVAL_PAGE;
    }

    protected void extractPageDescription(IPage page, Lang currentLang, RequestContext reqCtx) {
        PageMetadata pageMetadata = page.getMetadata();
        if (!(pageMetadata instanceof SeoPageMetadata) || !((SeoPageMetadata) pageMetadata).isUseExtraDescriptions()) {
            this.extractPageDescription(page, currentLang);
            return;
        }
        Object extraDescriptionObject = reqCtx.getExtraParam(JpseoSystemConstants.EXTRAPAR_EXTRA_PAGE_DESCRIPTIONS);
        if (null == extraDescriptionObject) {
            this.extractPageDescription(page, currentLang);
            return;
        }
        if (extraDescriptionObject instanceof String) {
            String extraDescriptionString = (String) extraDescriptionObject;
            if (extraDescriptionString.trim().length() > 0) {
                this.setValue(extraDescriptionString);
            } else {
                this.extractPageDescription(page, currentLang);
            }
        } else if (extraDescriptionObject instanceof Map<?, ?>) {
            this.extractPageDescriptionFromMap(currentLang, (Map) extraDescriptionObject);
            if (null == this.getValue()) {
                this.extractPageDescription(page, currentLang);
            }
        } else {
            this.extractPageDescription(page, currentLang);
        }
    }

    // METODO UGUALE
    //method equals than tag PageInfoTag
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

    private void extractPageDescriptionFromMap(Lang currentLang, Map extraDescriptionsMap) {
        Object value = null;
        if ((this.getLangCode() == null) || (this.getLangCode().equals(""))
                || (currentLang.getCode().equalsIgnoreCase(this.getLangCode()))) {
            value = extraDescriptionsMap.get(currentLang.getCode());
        } else {
            value = extraDescriptionsMap.get(this.getLangCode());
        }
        if (value == null || value.toString().trim().equals("")) {
            ILangManager langManager
                    = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
            value = extraDescriptionsMap.get(langManager.getDefaultLang().getCode());
        }
        if (null != value && value.toString().trim().length() > 0) {
            this.setValue(value.toString());
        }
    }

}
