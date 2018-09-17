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

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.opensymphony.xwork2.util.ValueStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.jsp.JspException;
import org.apache.struts2.views.jsp.StrutsBodyTagSupport;
import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.entando.entando.plugins.jpseo.aps.system.services.metatag.IMetatagCatalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class SeoMetatagCatalogTag extends StrutsBodyTagSupport {

    private static final Logger logger = LoggerFactory.getLogger(SeoMetatagCatalogTag.class);

    private String var;

    @Override
    public int doEndTag() throws JspException {
        try {
            IMetatagCatalog metatagCatalog = (IMetatagCatalog) ApsWebApplicationUtils.getBean(JpseoSystemConstants.SEO_METATAG_CATALOG, pageContext);
            List<String> keys = new ArrayList<>(metatagCatalog.getCatalog().keySet());
            Collections.sort(keys);
            ValueStack stack = this.getStack();
            stack.getContext().put(this.getVar(), keys);
            stack.setValue("#attr['" + this.getVar() + "']", keys, false);
        } catch (Throwable t) {
            logger.error("Error on doStartTag", t);
            throw new JspException("Error on doStartTag", t);
        }
        return super.doEndTag();
    }

    @Override
    public void release() {
        this.var = null;
    }

    public void setVar(String var) {
        this.var = var;
    }

    protected String getVar() {
        return var;
    }

}
