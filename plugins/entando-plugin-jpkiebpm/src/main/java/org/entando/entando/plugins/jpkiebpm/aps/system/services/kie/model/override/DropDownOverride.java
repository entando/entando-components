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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessProperty;

/**
 *
 * @author Entando
 */
@XmlRootElement(name="dropDownOverride")
@XmlAccessorType(XmlAccessType.FIELD)
public class DropDownOverride extends AbstractBpmOverride {

    @Override
    public KieProcessProperty toKieProperty() {
        KieProcessProperty prop = new KieProcessProperty();
        StringBuilder bld = new StringBuilder();

        prop.setName(this.getType());
        for (String value: getValues()) {
            bld.append(value);
            bld.append(",");
        }
        String csv = bld.toString();
        prop.setValue(csv.substring(0,
                csv.length() - 1));
        return prop;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @XmlElement(name="dropDownValues")
    private List<String> values;

    @Override
    public String getType() {
        return OVERRIDE_TYPE_NAME;
    }

    public final static String OVERRIDE_TYPE_NAME = "dropDownOverride";
}
