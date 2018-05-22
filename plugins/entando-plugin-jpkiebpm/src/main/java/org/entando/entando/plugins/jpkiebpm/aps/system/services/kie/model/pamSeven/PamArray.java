package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;

@XmlRootElement(name="array")
@XmlAccessorType(XmlAccessType.FIELD)
public class PamArray {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String id;

    @XmlElement(name="model")
    private List<PamModel> models;

    @XmlElement(name = "fields")
    private List<PamFields> pamFields;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PamModel> getModels() {
        return models;
    }

    public void setModels(List<PamModel> models) {
        this.models = models;
    }

    public List<PamFields> getPamFields() {
        return pamFields;
    }

    public void setPamFields(List<PamFields> pamFields) {
        this.pamFields = pamFields;
    }
}


