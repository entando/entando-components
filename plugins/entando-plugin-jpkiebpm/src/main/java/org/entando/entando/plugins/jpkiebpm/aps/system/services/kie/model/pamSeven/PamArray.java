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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="array")
@XmlAccessorType(XmlAccessType.FIELD)
public class PamArray {

    @XmlElement
    private String name;

    @XmlElement
    private String id;

    @XmlElement(name="model")
    private PamModel model;

    @XmlElement(name = "fields")
    private List<PamFields> pamFields;

    @XmlElement(name = "layoutTemplate")
    private PamLayoutTemplate layoutTemplate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PamModel getModel() {
        return model;
    }

    public void setModels(PamModel model) {
        this.model = model;
    }

    public List<PamFields> getPamFields() {
        return pamFields;
    }

    public void setPamFields(List<PamFields> pamFields) {
        this.pamFields = pamFields;
    }

    public PamLayoutTemplate getLayoutTemplate() {
        return layoutTemplate;
    }

    public void setLayoutTemplate(PamLayoutTemplate layoutTemplate) {
        this.layoutTemplate = layoutTemplate;
    }
}


