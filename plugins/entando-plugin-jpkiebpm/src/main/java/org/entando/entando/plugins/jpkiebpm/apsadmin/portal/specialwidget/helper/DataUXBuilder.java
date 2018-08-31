/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.helper;

import com.agiletec.aps.util.FileTextReader;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.util.KieApiUtil;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author eu
 */
public class DataUXBuilder {

    public DataUXBuilder() {
        this.typeMapping.put("InputText", "text");
        this.typeMapping.put("InputTextInteger", "number");
        this.typeMapping.put("IntegerBox", "number");
        this.typeMapping.put("CheckBox", "checkbox");
        this.typeMapping.put("DatePicker", "date");
        this.typeMapping.put("TextBox", "text");
        this.typeMapping.put("DecimalBox", "number");

        this.valueMapping.put("InputText", "$data.%s.text");
        this.valueMapping.put("InputTextInteger", "$data.%s.number");

//        this.valueMapping.put("InputText", "$data.%s.text");
//        this.valueMapping.put("InputTextInteger", "$data.%s.number");
//        this.valueMapping.put("IntegerBox", "$data.%s.number");
//        this.valueMapping.put("CheckBox", "$data.%s.checkbox");
//
//        this.valueMapping.put("DatePicker", "$data.%s.date");
//        this.valueMapping.put("TextBox", "$data.%s.text");
//        this.valueMapping.put("DecimalBox", "$data.%s.number");
    }

    public String createDataUx(KieProcessFormQueryResult kpfr, String containerId, String processId, String title) throws Exception {
        StringBuilder builder = new StringBuilder();
        String header = this.extractTemplate("modelHeader.txt");
        builder.append(String.format(header, title, title, processId, containerId));
        this.addFormFields(kpfr, builder);
        String submit = this.extractTemplate("submit.txt");
        builder.append(submit);
        String footer = this.extractTemplate("modelFooter.txt");
        builder.append(footer);
        return builder.toString();
    }

    private void addFormFields(KieProcessFormQueryResult kpfr, StringBuilder builder) throws Exception {
        String sectionHeader = this.extractTemplate("sectionHeader.txt");
        if (kpfr.getFields().size() > 0) {
            String formName = null;
            if (kpfr.getFields().get(0).getName().contains("_")) {
                formName = KieApiUtil.getFormNameFromField(kpfr.getFields().get(0));
            } else {
                if (KieApiUtil.getFieldProperty(kpfr.getProperties(), "name").contains(".")) {
                    formName = KieApiUtil.getFieldProperty(kpfr.getProperties(), "name")
                            .substring(0, KieApiUtil.getFieldProperty(kpfr.getProperties(), "name").indexOf("."));

                } else {
                    formName = KieApiUtil.getFieldProperty(kpfr.getProperties(), "name");
                }
            }
            sectionHeader = String.format(sectionHeader, formName);
            builder.append(sectionHeader);

        }

        List<KieProcessFormField> fields = kpfr.getFields();
        for (int i = 0; i < fields.size(); i++) {
            KieProcessFormField field = fields.get(i);
            this.addFormField(field, builder);
        }
        String sectionFooter = this.extractTemplate("sectionFooter.txt");

        if (kpfr.getFields().size() > 0) {
            builder.append(sectionFooter);
        }
        List<KieProcessFormQueryResult> subForms = kpfr.getNestedForms();
        if (null != subForms && !subForms.isEmpty()) {
            for (int i = 0; i < subForms.size(); i++) {
                KieProcessFormQueryResult form = subForms.get(i);
                this.addFormFields(form, builder);
            }
        }
    }

    private String extractTemplate(String filename) throws Exception {
        InputStream is = this.getClass().getResourceAsStream(filename);
        return FileTextReader.getText(is);
    }

    private void addFormField(KieProcessFormField field, StringBuilder builder) throws Exception {
        String subInputField = this.extractTemplate("inputField.txt");
        String fieldName = field.getName();
        String fieldType = this.typeMapping.get(field.getType());
        String fieldValueExpr = this.valueMapping.get(field.getType());
        String fieldValue = (null != fieldValueExpr) ? String.format(fieldValueExpr, field.getName()) : "";
        String readonly = (field.getProperty("readOnly") != null && 
        		"true".equalsIgnoreCase(field.getProperty("readOnly").getValue())) ? 
        				"readonly" : "";
        builder.append(String.format(subInputField, fieldName, fieldName,
                fieldName, fieldType,
                fieldName, fieldName, fieldName, fieldName, fieldValue, readonly));
    }

    private Map<String, String> typeMapping = new HashMap<>();
    private Map<String, String> valueMapping = new HashMap<>();

}
