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
package org.entando.entando.plugins.jpseo.apsadmin.portal;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.plugins.jacms.apsadmin.portal.PageAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpseo.aps.system.services.metatag.Metatag;
import org.entando.entando.plugins.jpseo.aps.system.services.page.PageMetatag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.entando.entando.plugins.jpseo.aps.system.services.metatag.IMetatagCatalog;

/**
 * @author E.Santoboni
 */
public class SeoPageAction extends PageAction {
    
    private static final Logger logger = LoggerFactory.getLogger(SeoPageAction.class);
    
    private IMetatagCatalog metatagCatalog;
    
    private String metatagKey;
    
    private String metatagValue;

    public String addMetatag() {
        try {
            this.extractAndSetOtherParams();
            Map<String, Map<String, PageMetatag>> seoParameters = SeoPageActionUtils.extractSeoParameters(this.getRequest());
            String key = this.getMetatagKey();
            boolean hasError = false;
            Metatag metatag = (!StringUtils.isBlank(key)) ? this.getMetatagCatalog().getCatalog().get(key) : null;
            if (StringUtils.isBlank(key)) {
                this.addFieldError("metatagKey", this.getText("jpseo.errors.metatagKey.invalid", new String[]{key}));
                hasError = true;
            }
            String value = this.getMetatagValue();
            if (StringUtils.isBlank(value)) {
                this.addFieldError("metatagValue", this.getText("jpseo.errors.metatagValue.invalid", new String[]{value}));
                hasError = true;
            }
            if (!hasError) {
                Iterator<Lang> langsIter = getLangManager().getLangs().iterator();
                while (langsIter.hasNext()) {
                    Lang lang = langsIter.next();
                    String valueToSet = (lang.isDefault()) ? value : null; 
                    PageMetatag pageMetatag = new PageMetatag(lang.getCode(), key, valueToSet);
                    pageMetatag.setUseDefaultLangValue(!lang.isDefault());
                    if (null != metatag) {
                        pageMetatag.setKeyAttribute(metatag.getAttributeKey());
                    }
                    Map<String, PageMetatag> langMetatag = seoParameters.get(lang.getCode());
                    if (null == langMetatag) {
                        langMetatag = new HashMap<>();
                        seoParameters.put(lang.getCode(), langMetatag);
                    }
                    langMetatag.put(key, pageMetatag);
                }
            }
            SeoPageActionUtils.setSeoParameters(seoParameters, this.getRequest());
        } catch (Throwable t) {
            logger.error("error in addMetatag", t);
            return FAILURE;
        }
        return SUCCESS;
    }
    
    public String removeMetatag() {
        try {
            this.extractAndSetOtherParams();
            Map<String, Map<String, PageMetatag>> seoParameters = SeoPageActionUtils.extractSeoParameters(this.getRequest());
            String metaKey = this.getMetatagKey();
            if (!StringUtils.isBlank(metaKey) && null != seoParameters) {
                seoParameters.values().stream().forEach(langMap -> langMap.remove(metaKey));
            }
            SeoPageActionUtils.setSeoParameters(seoParameters, this.getRequest());
        } catch (Throwable t) {
            logger.error("error in removeMetatag", t);
            return FAILURE;
        }
        return SUCCESS;
    }
    
    private void extractAndSetOtherParams() {
        this.updateTitles();
        SeoPageActionUtils.extractAndSetDescriptionAndKeywords(this.getRequest());
        SeoPageActionUtils.extractAndSetFriendlyCode(this.getRequest());
    }

    public String getMetatagKey() {
        return metatagKey;
    }

    public void setMetatagKey(String metatagKey) {
        this.metatagKey = metatagKey;
    }

    public String getMetatagValue() {
        return metatagValue;
    }

    public void setMetatagValue(String metatagValue) {
        this.metatagValue = metatagValue;
    }

    protected IMetatagCatalog getMetatagCatalog() {
        return metatagCatalog;
    }

    public void setMetatagCatalog(IMetatagCatalog metatagCatalog) {
        this.metatagCatalog = metatagCatalog;
    }
    
}
