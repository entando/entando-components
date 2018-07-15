package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="array")
@XmlAccessorType(XmlAccessType.FIELD)
public class PamArray {

    @XmlElement
    private String name;

    @XmlElement
    private String id;

    @XmlElement(name="model")
    private PamModel model;

    @XmlElement(name = "fields")
    private List<PamFields> pamFields;

    @XmlElement(name = "layoutTemplate")
    private PamLayoutTemplate layoutTemplate;

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

    public PamModel getModel() {
        return model;
    }

    public void setModels(PamModel model) {
        this.model = model;
    }

    public List<PamFields> getPamFields() {
        return pamFields;
    }

    public void setPamFields(List<PamFields> pamFields) {
        this.pamFields = pamFields;
    }

    public PamLayoutTemplate getLayoutTemplate() {
        return layoutTemplate;
    }

    public void setLayoutTemplate(PamLayoutTemplate layoutTemplate) {
        this.layoutTemplate = layoutTemplate;
    }
}


