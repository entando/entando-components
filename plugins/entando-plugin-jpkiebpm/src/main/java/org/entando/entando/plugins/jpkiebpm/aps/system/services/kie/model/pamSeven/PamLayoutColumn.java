package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "layoutColumn")
@XmlAccessorType(XmlAccessType.FIELD)
public class PamLayoutColumn {


    @XmlElement(name = "layoutComponents")
    public List<PamLayoutComponent> layoutComponents;

    public List<PamLayoutComponent> getLayoutComponents() {
        return layoutComponents;
    }

    public void setLayoutComponents(List<PamLayoutComponent> layoutComponents) {
        this.layoutComponents = layoutComponents;
    }
}
