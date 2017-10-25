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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.eclipse.persistence.oxm.annotations.XmlPaths;

/**
 *
 * @author Entando
 */
@XmlRootElement(name="overrideList")
@XmlAccessorType(XmlAccessType.FIELD)
public class OverrideList {

    /**
     * Add a new override to the list of overrides
     *
     * @param override
     */
    public void addOverride(IBpmOverride override) {
        if (null == override) {
            return;
        }
        if (null == this.getList()) {
            this.setList(new ArrayList<IBpmOverride>());
        }
        this.getList().add(override);
    }


    public List<IBpmOverride> getList() {
        return list;
    }

    public void setList(List<IBpmOverride> list) {
        this.list = list;
    }

    @XmlElements({
        @XmlElement(type=DefaultValueOverride.class),
        @XmlElement(type=PlaceHolderOverride.class),
        @XmlElement(type=DropDownOverride.class)
    })
    @XmlPaths({
        @XmlPath("override[@type='defaultValueOverride']"),
        @XmlPath("override[@type='placeHolderOverride']"),
        @XmlPath("override[@type='dropDownOverride']"),
    })
    @XmlElement(name="override")
    private List<IBpmOverride> list;

}
