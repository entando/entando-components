/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.api.model;

import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieFormField;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "field")
@XmlType(propOrder = {"id", "cssStyle", "defaultValueFormula", "disabled", "errorMessage", "fieldClass", "fieldRequired",
    "formula", "groupWithPrevious", "inputBinding", "label", "labelCSSClass", "labelCSSStyle", "name", "outputBinding",
    "position", "rangeFormula", "readonly", "size", "styleclass", "title", "type"})
public class JAXBField {

    public JAXBField() {
    }

    public JAXBField(KieFormField field) {
        this.setId(field.getId());
        this.setCssStyle(field.getCssStyle());
        this.setDefaultValueFormula(field.getDefaultValueFormula());
        this.setDisabled(field.getDisabled());
        this.setErrorMessage(field.getErrorMessage());
        this.setFieldClass(field.getFieldClass());
        this.setFieldRequired(field.getFieldRequired());
        this.setFormula(field.getFormula());
        this.setGroupWithPrevious(field.getGroupWithPrevious());
        this.setInputBinding(field.getInputBinding());
        this.setLabel(field.getLabel());
        this.setLabelCSSClass(field.getLabelCSSClass());
        this.setCssStyle(field.getCssStyle());
        this.setName(field.getName());
        this.setOutputBinding(field.getOutputBinding());
        this.setPosition(field.getPosition());
        this.setRangeFormula(field.getRangeFormula());
        this.setReadonly(field.getReadonly());
        this.setSize(field.getSize());
        this.setTitle(field.getTitle());
        this.setType(field.getType());
    }

    @XmlElement(name = "cssStyle")
    public String getCssStyle() {
        return cssStyle;
    }

    public void setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }

    @XmlElement(name = "defaultValueFormula")
    public String getDefaultValueFormula() {
        return defaultValueFormula;
    }

    public void setDefaultValueFormula(String defaultValueFormula) {
        this.defaultValueFormula = defaultValueFormula;
    }

    @XmlElement(name = "disabled")
    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @XmlElement(name = "errorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @XmlElement(name = "fieldClass")
    public String getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(String fieldClass) {
        this.fieldClass = fieldClass;
    }

    @XmlElement(name = "fieldRequired")
    public Boolean getFieldRequired() {
        return fieldRequired;
    }

    public void setFieldRequired(Boolean fieldRequired) {
        this.fieldRequired = fieldRequired;
    }

    @XmlElement(name = "formula")
    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    @XmlElement(name = "groupWithPrevious")
    public Boolean getGroupWithPrevious() {
        return groupWithPrevious;
    }

    public void setGroupWithPrevious(Boolean groupWithPrevious) {
        this.groupWithPrevious = groupWithPrevious;
    }

    @XmlElement(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement(name = "inputBinding")
    public String getInputBinding() {
        return inputBinding;
    }

    public void setInputBinding(String inputBinding) {
        this.inputBinding = inputBinding;
    }

    @XmlElement(name = "label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlElement(name = "labelCSSClass")
    public String getLabelCSSClass() {
        return labelCSSClass;
    }

    public void setLabelCSSClass(String labelCSSClass) {
        this.labelCSSClass = labelCSSClass;
    }

    @XmlElement(name = "labelCSSStyle")
    public String getLabelCSSStyle() {
        return labelCSSStyle;
    }

    public void setLabelCSSStyle(String labelCSSStyle) {
        this.labelCSSStyle = labelCSSStyle;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "outputBinding")
    public String getOutputBinding() {
        return outputBinding;
    }

    public void setOutputBinding(String outputBinding) {
        this.outputBinding = outputBinding;
    }

    @XmlElement(name = "position")
    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @XmlElement(name = "rangeFormula")
    public String getRangeFormula() {
        return rangeFormula;
    }

    public void setRangeFormula(String rangeFormula) {
        this.rangeFormula = rangeFormula;
    }

    @XmlElement(name = "readonly")
    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    @XmlElement(name = "size")
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @XmlElement(name = "styleclass")
    public String getStyleclass() {
        return styleclass;
    }

    public void setStyleclass(String styleclass) {
        this.styleclass = styleclass;
    }

    @XmlElement(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String cssStyle;
    private String defaultValueFormula;
    private Boolean disabled;
    private String errorMessage;
    private String fieldClass;
    private Boolean fieldRequired;
    private String formula;
    private Boolean groupWithPrevious;
    private Long id;
    private String inputBinding;
    private String label;
    private String labelCSSClass;
    private String labelCSSStyle;
    private String name;
    private String outputBinding;
    private Integer position;
    private String rangeFormula;
    private Boolean readonly;
    private String size;
    private String styleclass;
    private String title;
    private String type;

}
