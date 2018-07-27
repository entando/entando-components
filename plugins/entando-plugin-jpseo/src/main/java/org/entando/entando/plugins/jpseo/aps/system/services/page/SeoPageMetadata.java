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
package org.entando.entando.plugins.jpseo.aps.system.services.page;

import java.util.Map;

import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.util.ApsProperties;

/**
 * This is the representation of a portal page metadata
 *
 * @author E.Santoboni
 */
public class SeoPageMetadata extends PageMetadata {

    private ApsProperties descriptions = new ApsProperties();
    private ApsProperties keywords = new ApsProperties();
    private boolean useExtraDescriptions = false;

    private String friendlyCode;

    private Map<String, Map<String, PageMetatag>> complexParameters;

    public String getDescription(String langCode) {
        return this.getDescriptions().getProperty(langCode);
    }

    public ApsProperties getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(ApsProperties descriptions) {
        this.descriptions = descriptions;
    }

    public ApsProperties getKeywords() {
        return keywords;
    }

    public void setKeywords(ApsProperties keywords) {
        this.keywords = keywords;
    }
    
    public boolean isUseExtraDescriptions() {
        return useExtraDescriptions;
    }

    public void setUseExtraDescriptions(boolean useExtraDescriptions) {
        this.useExtraDescriptions = useExtraDescriptions;
    }

    public String getFriendlyCode() {
        return friendlyCode;
    }

    public void setFriendlyCode(String friendlyCode) {
        this.friendlyCode = friendlyCode;
    }

    public Map<String, Map<String, PageMetatag>> getComplexParameters() {
        return complexParameters;
    }

    public void setComplexParameters(Map<String, Map<String, PageMetatag>> complexParameters) {
        this.complexParameters = complexParameters;
    }

    @Override
    public boolean hasEqualConfiguration(Object obj) {
        if (this == obj) {
            return true;
        }
        boolean check = super.hasEqualConfiguration(obj);
        if (!check) {
            return check;
        }
        SeoPageMetadata other = (SeoPageMetadata) obj;
        if ((null != other.getComplexParameters() && null == this.getComplexParameters())
                || (null == other.getComplexParameters() && null != this.getComplexParameters())
                || (null != other.getComplexParameters() && null != this.getComplexParameters()) && !other.getComplexParameters().equals(this.getComplexParameters())) {
            return false;
        }
        if ((null != other.getDescriptions() && null == this.getDescriptions())
                || (null == other.getDescriptions() && null != this.getDescriptions())
                || (null != other.getDescriptions() && null != this.getDescriptions()) && !other.getDescriptions().equals(this.getDescriptions())) {
            return false;
        }
        if ((null != other.getKeywords() && null == this.getKeywords())
                || (null == other.getKeywords() && null != this.getKeywords())
                || (null != other.getKeywords() && null != this.getKeywords()) && !other.getKeywords().equals(this.getKeywords())) {
            return false;
        }
        if (other.isUseExtraDescriptions() != this.isUseExtraDescriptions()) {
            return false;
        }
        return true;
    }

}
