package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="model")
public class PamModel {

    @XmlAttribute
    private String processName;

    @XmlAttribute
    private String processId;

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String formModelType;

    @XmlElement
    private List<PamSevenProperties> properties;
}
