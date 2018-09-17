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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.tags.ExtendedTagSupport;
import org.entando.entando.plugins.jpseo.aps.system.services.page.PageMetatag;
import org.entando.entando.plugins.jpseo.aps.system.services.page.SeoPageMetadata;

public class SeoMetatagTag extends ExtendedTagSupport {

    private static final Logger _logger = LoggerFactory.getLogger(SeoMetatagTag.class);

    @Override
    public int doEndTag() throws JspException {
        ServletRequest request = this.pageContext.getRequest();
        RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
        Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
        try {
            IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
            PageMetadata pageMetadata = page.getMetadata();
            if (!(pageMetadata instanceof SeoPageMetadata)) {
                this.release();
                return EVAL_PAGE;
            }
            Map<String, Map<String, PageMetatag>> complexParameters = ((SeoPageMetadata) pageMetadata).getComplexParameters();
            if (null != complexParameters) {
                Map<String, PageMetatag> mapvalue = complexParameters.get(currentLang.getCode());
                Map<String, PageMetatag> defaultMapvalue = complexParameters.get("default");
                if (null == defaultMapvalue) {
                    ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
                    Lang defaultLang = langManager.getDefaultLang();
                    defaultMapvalue = complexParameters.get(defaultLang.getCode());
                }
                if (null != mapvalue) {
                    PageMetatag pageMetatag = mapvalue.get(this.getKey());
                    if (null != pageMetatag && !pageMetatag.isUseDefaultLangValue() && !StringUtils.isBlank(pageMetatag.getValue())) {
                        this.setValue(pageMetatag.getValue());
                    }
                }
                if (null == this.getValue()) {
                    PageMetatag pageMetatag = defaultMapvalue.get(this.getKey());
                    if (null != pageMetatag && !StringUtils.isBlank(pageMetatag.getValue())) {
                        this.setValue(pageMetatag.getValue());
                    }
                }
            }
            if (null != this.getValue()) {
                this.evalValue();
            }
        } catch (Throwable t) {
            _logger.error("error in doStartTag", t);
            throw new JspException("Error during tag initialization ", t);
        }
        this.release();
        return EVAL_PAGE;
    }

    protected void evalValue() throws JspException {
        if (this.getVar() != null) {
            this.pageContext.setAttribute(this.getVar(), this.getValue());
        } else {
            try {
                if (this.getEscapeXml()) {
                    out(this.pageContext, this.getEscapeXml(), this.getValue());
                } else {
                    this.pageContext.getOut().print(this.getValue());
                }
            } catch (IOException e) {
                _logger.error("error in doEndTag", e);
                throw new JspException("Error closing tag ", e);
            }
        }
    }

    @Override
    public void release() {
        this._key = null;
        this._var = null;
        this._value = null;
        super.setEscapeXml(true);
    }

    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        this._key = key;
    }

    public void setVar(String var) {
        this._var = var;
    }

    protected String getVar() {
        return _var;
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        this._value = value;
    }

    private String _key;

    private String _var;
    private String _value;

}
