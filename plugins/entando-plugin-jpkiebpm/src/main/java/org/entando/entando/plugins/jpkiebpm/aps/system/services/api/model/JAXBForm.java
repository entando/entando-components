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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieDataHolder;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieForm;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieFormField;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "form")
@XmlType(propOrder = {"id", "data", "displayMode", "fields", "forms", "name", "status", "subject"})
public class JAXBForm {

    public JAXBForm() {
    }

    public JAXBForm(KieForm form) {
        this.setDataRaw(form.getData());
        this.setDisplayMode(form.getDisplayMode());
        this.setFieldsRaw(form.getFields());
        this.setId(form.getId());
        this.setName(form.getName());
        this.setFormsRaw(form.getForms());
        this.setStatus(form.getStatus());
        this.setSubject(form.getSubject());
    }

    @XmlElement(name = "data")
    public List<JAXBDataHolder> getData() {
        return data;
    }

    public void setData(List<JAXBDataHolder> data) {
        this.data = data;
    }

    public void setDataRaw(List<KieDataHolder> dataRaw) {
        List<JAXBDataHolder> data = new ArrayList<>();
        JAXBDataHolder jaxbdh = null;
        if (dataRaw != null) {
            for (KieDataHolder dh : dataRaw) {
                jaxbdh = new JAXBDataHolder(dh);
                data.add(jaxbdh);
            }
        }
        this.data = data;
    }

    @XmlElement(name = "displayMode")
    public String getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    @XmlElement(name = "fields")
    public List<JAXBField> getFields() {
        return fields;
    }

    public void setFields(List<JAXBField> fields) {
        this.fields = fields;
    }

    public void setFieldsRaw(List<KieFormField> fieldsRaw) {
        List<JAXBField> fields = new ArrayList<>();
        JAXBField jaxbfield = null;
        if (fieldsRaw != null) {
            for (KieFormField field : fieldsRaw) {
                jaxbfield = new JAXBField(field);
                fields.add(jaxbfield);
            }
        }
        this.fields = fields;
    }

    @XmlElement(name = "forms")
    public List<JAXBForm> getForms() {
        return forms;
    }

    public void setForms(List<JAXBForm> forms) {
        this.forms = forms;
    }

    public void setFormsRaw(List<KieForm> formsRaw) {
        List<JAXBForm> forms = new ArrayList<>();
        JAXBForm jaxbform = null;
        if (formsRaw != null) {
            for (KieForm form : formsRaw) {
                jaxbform = new JAXBForm(form);
                forms.add(jaxbform);
            }
        }
        this.forms = forms;
    }

    @XmlElement(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "status")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @XmlElement(name = "subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    private List<JAXBDataHolder> data;
    private String displayMode;
    private List<JAXBField> fields;
    private List<JAXBForm> forms;
    private Long id;
    private String name;
    private Long status;
    private String subject;

}
