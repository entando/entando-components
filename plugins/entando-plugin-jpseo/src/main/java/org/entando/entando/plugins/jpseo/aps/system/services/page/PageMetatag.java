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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import org.entando.entando.plugins.jpseo.aps.system.services.metatag.Metatag;

/**
 * @author E.Santoboni
 */
public class PageMetatag implements Serializable {
    
    private String keyAttribute = Metatag.ATTRIBUTE_NAME_NAME;
    private String key;
    private String value;
    private String langCode;
    private boolean useDefaultLangValue;
    
    public PageMetatag(String langCode, String key, String value) {
        this.setLangCode(langCode);
        this.setKey(key);
        this.setValue(value);
    }
    
    @Override
    public PageMetatag clone() {
        PageMetatag meta = new PageMetatag(this.getLangCode(), this.getKey(), this.getValue());
        meta.setKeyAttribute(this.getKeyAttribute());
        return meta;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PageMetatag other = (PageMetatag) obj;
        if (!Objects.equals(this.keyAttribute, other.keyAttribute)) {
            return false;
        }
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Objects.equals(this.langCode, other.langCode)) {
            return false;
        }
        if (!Objects.equals(this.useDefaultLangValue, other.useDefaultLangValue)) {
            return false;
        }
        return true;
    }
    
    public String getKeyAttribute() {
        return keyAttribute;
    }

    public void setKeyAttribute(String keyAttribute) {
        if (null == keyAttribute) {
            return;
        }
        if (!Arrays.asList(Metatag.getAttributeNames()).contains(keyAttribute)) {
            return;
        }
        this.keyAttribute = keyAttribute;
    }
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public boolean isUseDefaultLangValue() {
        return useDefaultLangValue;
    }

    public void setUseDefaultLangValue(boolean useDefaultLangValue) {
        this.useDefaultLangValue = useDefaultLangValue;
    }
    
}
