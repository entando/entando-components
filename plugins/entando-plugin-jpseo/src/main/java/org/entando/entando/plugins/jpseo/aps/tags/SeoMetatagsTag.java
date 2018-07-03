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

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.entando.entando.plugins.jpseo.aps.system.services.metatag.Metatag;
import org.entando.entando.plugins.jpseo.aps.system.services.page.PageMetatag;
import org.entando.entando.plugins.jpseo.aps.system.services.page.SeoPageExtraConfigDOM;
import org.entando.entando.plugins.jpseo.aps.system.services.page.SeoPageMetadata;

public class SeoMetatagsTag extends OutSupport {

    private static final Logger _logger = LoggerFactory.getLogger(SeoMetatagsTag.class);
    
    private String var;

    @Override
    public int doEndTag() throws JspException {
        ServletRequest request = this.pageContext.getRequest();
        RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
        Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
        try {
            StringBuilder output = new StringBuilder();
            IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
            PageMetadata pageMetadata = page.getMetadata();
            if (!(pageMetadata instanceof SeoPageMetadata)) {
                this.release();
                return EVAL_PAGE;
            }
            ILangManager langManager
                    = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
            Lang defaultLang = langManager.getDefaultLang();
            Object descriptionObject = reqCtx.getExtraParam(JpseoSystemConstants.EXTRAPAR_EXTRA_PAGE_DESCRIPTIONS);
            String description = (null != descriptionObject) ? descriptionObject.toString() : null;
            if (null == description) {
                description = ((SeoPageMetadata) pageMetadata).getDescription(currentLang.getCode());
                if (StringUtils.isBlank(description)) {
                    description = ((SeoPageMetadata) pageMetadata).getDescription(defaultLang.getCode());
                }
            }
            this.appendMetadata(output, Metatag.ATTRIBUTE_NAME_NAME, "description", description);
            Map<String, Map<String, PageMetatag>> complexParameters = ((SeoPageMetadata) pageMetadata).getComplexParameters();
            if (null != complexParameters) {
                Map<String, Map<String, PageMetatag>> metas = SeoPageExtraConfigDOM.extractRightParams(complexParameters, defaultLang);
                Map<String, PageMetatag> defaultMap = metas.get(defaultLang.getCode());
                Map<String, PageMetatag> langMap = (currentLang.getCode().equals(defaultLang.getCode())) ? null : metas.get(currentLang.getCode());
                if (null != defaultMap) {
                    Iterator<PageMetatag> iter = defaultMap.values().iterator();
                    while (iter.hasNext()) {
                        PageMetatag defaultMetatag = iter.next();
                        PageMetatag metatag = (null != langMap) ? langMap.get(defaultMetatag.getKey()) : null;
                        PageMetatag current = (null != metatag && !metatag.isUseDefaultLangValue()) ? metatag : defaultMetatag;
                        this.appendMetadata(output, current);
                    }
                }
            }
            this.evalValue(output.toString());
        } catch (Throwable t) {
            _logger.error("error in doStartTag", t);
            throw new JspException("Error during tag initialization ", t);
        }
        this.release();
        return EVAL_PAGE;
    }

    protected void evalValue(String output) throws JspException {
        if (this.getVar() != null) {
            this.pageContext.setAttribute(this.getVar(), output);
        } else {
            try {
                if (this.getEscapeXml()) {
                    out(this.pageContext, this.getEscapeXml(), output);
                } else {
                    this.pageContext.getOut().print(output);
                }
            } catch (IOException e) {
                _logger.error("error in doEndTag", e);
                throw new JspException("Error closing tag ", e);
            }
        }
    }
    
    protected void appendMetadata(StringBuilder output, PageMetatag metatag) {
        this.appendMetadata(output, metatag.getKeyAttribute(), metatag.getKey(), metatag.getValue());
    }
    
    protected void appendMetadata(StringBuilder output, String attributeName, String key, String value) {
        if (StringUtils.isBlank(attributeName) || StringUtils.isBlank(key)) {
            return;
        }
        String valueToUse = (StringUtils.isBlank(value)) ? "" : value;
        output.append("<meta ").append(attributeName)
                .append("=\"").append(key).append("\" content=\"").append(valueToUse).append("\" />").append("\n");
    }
    
    @Override
    public void release() {
        this.var = null;
        super.escapeXml = true;
    }

    public void setVar(String var) {
        this.var = var;
    }

    protected String getVar() {
        return var;
    }

    /**
     * Returns True if the system escape the special characters.
     *
     * @return True if the system escape the special characters.
     */
    public boolean getEscapeXml() {
        return super.escapeXml;
    }

    /**
     * Set if the system has to escape the special characters.
     *
     * @param escapeXml True if the system has to escape the special characters,
     * else false.
     */
    public void setEscapeXml(boolean escapeXml) {
        super.escapeXml = escapeXml;
    }

}
