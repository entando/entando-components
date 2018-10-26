package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;

import java.util.List;
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

    //Text Fields
    @XmlElement(required = false)
    private String placeHolder;
    
    //DatePicker Fields
    @XmlElement(required=false)
    private boolean showTime;

    
    //Multiple Select Fields
        
    @XmlElement(required = false)
    private List<String> listOfValues;

    @XmlElement(required = false)   
    private int maxDropdownElements;
    
    @XmlElement(required = false)
    private int maxElementsOnTitle;
    
    @XmlElement(required = false)
    private boolean allowFilter;
    
    @XmlElement(required = false)
    private boolean  allowClearSelection;
    
    //List Box Fields (Html Select)

    @XmlElement(required = false)
    private List<PamFieldsOptions> options;
    private boolean addEmptyOption;
    private String defaultValue;
    
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

    public boolean getShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    public List<String> getListOfValues() {
        return listOfValues;
    }

    public void setListOfValues(List<String> listOfValues) {
        this.listOfValues = listOfValues;
    }

    public int getMaxDropdownElements() {
        return maxDropdownElements;
    }

    public void setMaxDropdownElements(int maxDropdownElements) {
        this.maxDropdownElements = maxDropdownElements;
    }

    public int getMaxElementsOnTitle() {
        return maxElementsOnTitle;
    }

    public void setMaxElementsOnTitle(int maxElementsOnTitle) {
        this.maxElementsOnTitle = maxElementsOnTitle;
    }

    public boolean isAllowFilter() {
        return allowFilter;
    }

    public void setAllowFilter(boolean allowFilter) {
        this.allowFilter = allowFilter;
    }

    public boolean isAllowClearSelection() {
        return allowClearSelection;
    }

    public void setAllowClearSelection(boolean allowClearSelection) {
        this.allowClearSelection = allowClearSelection;
    }

    public List<PamFieldsOptions> getOptions() {
        return options;
    }

    public void setOptions(List<PamFieldsOptions> options) {
        this.options = options;
    }

    public boolean isAddEmptyOption() {
        return addEmptyOption;
    }

    public void setAddEmptyOption(boolean addEmptyOption) {
        this.addEmptyOption = addEmptyOption;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

}
