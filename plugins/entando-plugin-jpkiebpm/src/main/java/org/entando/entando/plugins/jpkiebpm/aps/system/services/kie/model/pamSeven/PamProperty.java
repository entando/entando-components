package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "properties")
@XmlAccessorType(XmlAccessType.FIELD)
public class PamProperty {

    @XmlElement
    private PamMetaData metaData;

    @XmlElement
    private PamTypeInfo typeInfo;

    @XmlAttribute
    private String name;

}
