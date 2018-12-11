/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
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

@XmlRootElement(name="overrideList")
@XmlAccessorType(XmlAccessType.FIELD)
public class OverrideList {


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
    
    public OverrideList() {
        list = new ArrayList<>();
    }
    
    /**
     * Add a new override to the list of overrides
     *
     * @param override
     */
    public void addOverride(IBpmOverride override) {
        if (null == override) {
            return;
        }
        this.getList().add(override);
    }

    public List<IBpmOverride> getList() {
        return list;
    }

    public void setList(List<IBpmOverride> list) {
        this.list = list;
    }
}
