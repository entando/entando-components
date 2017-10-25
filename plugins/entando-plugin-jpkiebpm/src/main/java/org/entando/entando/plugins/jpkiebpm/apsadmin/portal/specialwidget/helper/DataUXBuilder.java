/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.helper;

import com.agiletec.aps.util.FileTextReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.util.KieApiUtil;
//import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieDataHolder;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessProperty;

/**
 *
 * @author eu
 */
public class DataUXBuilder {

    public DataUXBuilder() {
        this.typeMapping.put("InputText", "text");
        this.typeMapping.put("InputTextInteger", "number");

        this.valueMapping.put("InputText", "$data.%s.text");
        this.valueMapping.put("InputTextInteger", "$data.%s.number");
    }

    public String createDataUx(KieProcessFormQueryResult kpfr, String containerId, String processId) throws Exception {
        StringBuilder builder = new StringBuilder();
        String header = this.extractTemplate("modelHeader.txt");
        builder.append(String.format(header, containerId, containerId, processId, containerId));
        /*
		List<KieProcessProperty> formProperties = kpfr.getProperties();
		System.out.println("------------------");
		for (int i = 0; i < formProperties.size(); i++) {
			KieProcessProperty property = formProperties.get(i);
			System.out.println("property  name -> " + property.getName() + " - VALUE " + property.getValue());
		}
         */
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
            String formName = KieApiUtil.getFormNameFromField(kpfr.getFields().get(0));
            sectionHeader = String.format(sectionHeader, formName);
        }
        builder.append(sectionHeader);
        /*
		List<KieDataHolder> formHolders = kpfr.getHolders();
		for (int i = 0; i < formHolders.size(); i++) {
			KieDataHolder holder = formHolders.get(i);
			System.out.println("FORM holder -> " + holder.getName()
					+ " - id " + holder.getId() + " - inputId " + holder.getInputId() + " - Name " + holder.getName()
					+ " - outId " + holder.getOutId() + " - Type " + holder.getType() + " - Value " + holder.getValue());
		}
         */
 /*
		List<KieProcessProperty> formProperties = kpfr.getProperties();
		System.out.println("------------------");
		for (int j = 0; j < formProperties.size(); j++) {
			KieProcessProperty property = formProperties.get(j);
			System.out.println("FORM property  name -> " + property.getName() + " - VALUE " + property.getValue());
		}
		System.out.println("------------------");
         */
        List<KieProcessFormField> fields = kpfr.getFields();
        //System.out.println("------------------");
        for (int i = 0; i < fields.size(); i++) {
            KieProcessFormField field = fields.get(i);
            this.addFormField(field, builder);
        }
        String sectionFooter = this.extractTemplate("sectionFooter.txt");
        builder.append(sectionFooter);
        List<KieProcessFormQueryResult> subForms = kpfr.getForms();
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

    /*
	private void printProperties(List<KieProcessProperty> fieldProperties) {
		for (int i = 0; i < fieldProperties.size(); i++) {
			KieProcessProperty property = fieldProperties.get(i);
			System.out.println("FIELD property  name -> " + property.getName() + " - VALUE " + property.getValue());
		}
	}
     */
    private void addFormField(KieProcessFormField field, StringBuilder builder) throws Exception {
        String subInputField = this.extractTemplate("inputField.txt");
        /*
		System.out.println("------------------");
		System.out.println("Field  getId          -> " + field.getId());
		System.out.println("Field  getName        -> " + field.getName());
		System.out.println("Field  getPosition    -> " + field.getPosition());
		System.out.println("Field  getType        -> " + field.getType());
		System.out.println("Field  getProperties  -> " + field.getProperties());
		this.printProperties(field.getProperties());
		System.out.println("------------------");
         */
        String fieldName = field.getName();
        KieProcessProperty labelProperty = field.getProperty("label");
        //String label = (null != labelProperty) ? labelProperty.getValue() : null;
        String fieldType = this.typeMapping.get(field.getType());
        String fieldValueExpr = this.valueMapping.get(field.getType());
        String fieldValue = (null != fieldValueExpr) ? String.format(fieldValueExpr, field.getName()) : "";
        builder.append(String.format(subInputField, fieldName, fieldName,
                fieldName, fieldType,
                fieldName, fieldName, fieldName, fieldName, fieldValue));
    }

    private Map<String, String> typeMapping = new HashMap<>();
    private Map<String, String> valueMapping = new HashMap<>();

}
