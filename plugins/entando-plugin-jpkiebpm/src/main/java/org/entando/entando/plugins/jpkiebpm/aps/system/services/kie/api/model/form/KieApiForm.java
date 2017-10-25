package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mainForm")
public class KieApiForm {

    public static final String LABEL_PREFIX = "JPKIE_FORM_";
    public static final String LABEL_TITLE_PREFIX = "JPKIE_TITLE_";
    public static final String LABEL_TITLE_VALUE_PREFIX = "PROCESS ";

    private String id;
    private String name;
    private String containerId;
    private String processId;
    private String taskId;

    private static final Logger _logger = LoggerFactory.getLogger(KieApiForm.class);

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

    public void addFields(final KieApiFields fields) {
        if (this.fields == null) {
            this.fields = new ArrayList<>();
        }
        this.fields.add(fields);
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

}
