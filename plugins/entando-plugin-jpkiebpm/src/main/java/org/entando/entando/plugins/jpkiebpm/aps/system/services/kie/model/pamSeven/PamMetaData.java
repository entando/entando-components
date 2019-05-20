package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="metaData")
@XmlAccessorType(XmlAccessType.FIELD)
public class PamMetaData {

    @XmlElement
    private List<PamEntry> entries;

    public List<PamEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<PamEntry> entries) {
        this.entries = entries;
    }
}
