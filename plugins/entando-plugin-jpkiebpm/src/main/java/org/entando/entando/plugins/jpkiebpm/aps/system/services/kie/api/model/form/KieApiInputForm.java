package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "mainForm")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "name", "fields", "containerId", "processId"})
public class KieApiInputForm {

    private static final Logger logger = LoggerFactory.getLogger(KieApiInputForm.class);
    private String id;
    private String name;
    private String containerId;
    private String processId;

    @XmlElement(name = "fields")
    private List<KieApiFields> fields;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<KieApiFields> getFields() {
        return fields;
    }

    public void setFields(List<KieApiFields> fields) {
        this.fields = fields;
    }

    public String getContainerId() {
        return this.containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getProcessId() {
        return this.processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @XmlTransient
    public Map<String, String> getParamsMap() {
        final Map<String, String> params = new HashMap<>();
        if (null == fields) {
            return params;
        }
        for (final KieApiFields fields : this.getFields()) {
            for (final KieApiFieldset fieldset : fields.getFieldset()) {
                for (final KieApiField field : fieldset.getFields()) {
                    params.put(field.getName().replace(KieApiField.FIELD_NAME_PREFIX, "").trim(), field.getValue());
                }
            }
        }

        return params;
    }


}
