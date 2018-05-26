package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="model")
@XmlAccessorType(XmlAccessType.FIELD)
public class PamModel {

    @XmlElement
    private String processName;

    @XmlElement
    private String processId;

    @XmlElement
    private String name;

    @XmlElement
    private String formModelType;

    @XmlElement
    private String className;

    @XmlElement
    private List<PamProperty> properties;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormModelType() {
        return formModelType;
    }

    public void setFormModelType(String formModelType) {
        this.formModelType = formModelType;
    }

    public List<PamProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<PamProperty> properties) {
        this.properties = properties;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
