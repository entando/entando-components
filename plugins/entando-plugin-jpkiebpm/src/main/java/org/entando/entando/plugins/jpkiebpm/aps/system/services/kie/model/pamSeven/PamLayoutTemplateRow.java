package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "rows")
@XmlAccessorType(XmlAccessType.FIELD)
public class PamLayoutTemplateRow {


    @XmlElement(name = "layoutColumns")
    public List<PamLayoutColumn> layoutColums;

    public List<PamLayoutColumn> getLayoutColums() {
        return layoutColums;
    }

    public void setLayoutColums(List<PamLayoutColumn> layoutColums) {
        this.layoutColums = layoutColums;
    }
}
