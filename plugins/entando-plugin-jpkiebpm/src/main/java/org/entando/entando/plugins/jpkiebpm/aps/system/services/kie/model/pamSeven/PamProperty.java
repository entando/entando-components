/*
 * The MIT License
 *
 * Copyright 2019 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "properties")
@XmlAccessorType(XmlAccessType.FIELD)
public class PamProperty {

    @XmlElement
    private PamMetaData metaData;

    @XmlElement
    private PamTypeInfo typeInfo;

    @XmlElement(name="name")
    private String name;

    public PamMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(PamMetaData metaData) {
        this.metaData = metaData;
    }

    public PamTypeInfo getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(PamTypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
