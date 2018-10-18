package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "layoutComponents")
@XmlAccessorType(XmlAccessType.FIELD)
public class PamLayoutComponent {

    @XmlElement(name = "properties")
    public PamLayoutComponentProperty properties;

    public PamLayoutComponentProperty getProperties() {
        return properties;
    }

    public void setProperties(PamLayoutComponentProperty properties) {
        this.properties = properties;
    }
}
