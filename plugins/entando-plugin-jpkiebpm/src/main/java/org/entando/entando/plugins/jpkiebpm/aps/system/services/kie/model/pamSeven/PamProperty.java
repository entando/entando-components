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
