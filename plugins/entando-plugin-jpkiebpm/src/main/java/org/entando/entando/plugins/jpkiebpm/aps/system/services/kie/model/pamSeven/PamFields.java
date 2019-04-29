/*
 * The MIT License
 *
 * Copyright 2019 Entando Inc..
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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

    //MultipleSubForms Fields
    @XmlElement(required = false)
    private List<PamColumnMetas> columnMetas;

    @XmlElement(required = false)
    private String creationForm;

    @XmlElement(required = false)
    private String editionForm;

    //Common List Box Fields (Html Select) and RadioGroup Fields 

    @XmlElement(required = false)
    private List<PamFieldsOptions> options;

    @XmlElement(required = false)
    private String defaultValue;
    
    //List Box Fields (Html Select)

    @XmlElement(required = false)
    private boolean addEmptyOption;
    
    //RadioGroup Fields 

    @XmlElement(required = false)
    private boolean inline;
    
       
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

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public List<PamColumnMetas> getColumnMetas() {
        return columnMetas;
    }

    public void setColumnMetas(List<PamColumnMetas> columnMetas) {
        this.columnMetas = columnMetas;
    }

    public String getCreationForm() {
        return creationForm;
    }

    public void setCreationForm(String creationForm) {
        this.creationForm = creationForm;
    }

    public String getEditionForm() {
        return editionForm;
    }

    public void setEditionForm(String editionForm) {
        this.editionForm = editionForm;
    }
}
