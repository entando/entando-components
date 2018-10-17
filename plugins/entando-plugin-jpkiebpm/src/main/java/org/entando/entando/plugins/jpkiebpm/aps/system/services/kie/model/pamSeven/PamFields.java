package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fields")
@XmlAccessorType(XmlAccessType.FIELD)
public class PamFields {

    @XmlElement
    private String serializedFieldClassName;

    @XmlElement
    private Boolean validateOnChange;

    @XmlElement
    private String code;

    @XmlElement
    private String name;

    @XmlElement
    private String binding;

    @XmlElement
    private Boolean readOnly;

    @XmlElement
    private String standaloneClassName;

    @XmlElement
    private String id;

    @XmlElement
    private String label;

    @XmlElement
    private Boolean required;

    @XmlElement
    private String helpMessage;

    @XmlElement
    private String nestedForm;

    @XmlElement
    private String placeHolder;

    public String getSerializedFieldClassName() {
        return serializedFieldClassName;
    }

    public void setSerializedFieldClassName(String serializedFieldClassName) {
        this.serializedFieldClassName = serializedFieldClassName;
    }

    public Boolean getValidateOnChange() {
        return validateOnChange;
    }

    public void setValidateOnChange(Boolean validateOnChange) {
        this.validateOnChange = validateOnChange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getStandaloneClassName() {
        return standaloneClassName;
    }

    public void setStandaloneClassName(String standaloneClassName) {
        this.standaloneClassName = standaloneClassName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    public void setHelpMessage(String helpMessage) {
        this.helpMessage = helpMessage;
    }

    public String getNestedForm() {
        return nestedForm;
    }

    public void setNestedForm(String nestedForm) {
        this.nestedForm = nestedForm;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

}
