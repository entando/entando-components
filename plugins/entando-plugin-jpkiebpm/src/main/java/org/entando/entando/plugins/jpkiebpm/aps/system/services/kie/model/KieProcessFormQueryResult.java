/*
 * The MIT License
 *
 * Copyright 2017 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Entando
 */
@XmlRootElement(name="form")
@XmlAccessorType(XmlAccessType.FIELD)
public class KieProcessFormQueryResult {

     public List<KieProcessFormQueryResult> getForms() {
        return forms;
    }

    public void setForms(List<KieProcessFormQueryResult> forms) {
        this.forms = forms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<KieProcessProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<KieProcessProperty> properties) {
        this.properties = properties;
    }

    public List<KieDataHolder> getHolders() {
        return holders;
    }

    public void setHolders(List<KieDataHolder> holders) {
        this.holders = holders;
    }

    public List<KieProcessFormField> getFields() {
        return fields;
    }

    public void setFields(List<KieProcessFormField> fields) {
        this.fields = fields;
    }

    @XmlAttribute
    private Long id;

    @XmlElement(name="property")
    private List<KieProcessProperty> properties;

    @XmlElement(name="form")
    private List<KieProcessFormQueryResult> forms;

    @XmlElement(name="field")
    private List<KieProcessFormField> fields;

    @XmlElement(name="dataHolder")
    private List<KieDataHolder> holders;

}
