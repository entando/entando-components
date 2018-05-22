package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="pamFormResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class PamProcessQueryFormResult {

    @XmlElement(name="array")
    List<PamArray> arrays;


    public List<PamArray> getArrays() {
        return arrays;
    }

    public void setArrays(List<PamArray> arrays) {
        this.arrays = arrays;
    }
}
