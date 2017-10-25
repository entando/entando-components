/*
* The MIT License
*
* Copyright 2017 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiError;

import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.BpmToFormHelper.SEPARATOR;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieDataHolder;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.NullFormField;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Entando
 */
public class FormToBpmHelper {

    private static final Logger _logger = LoggerFactory.getLogger(FormToBpmHelper.class);

    /**
     * Create a JSON for each section of the form
     *
     * @param bpmForm
     * @param fields
     * @return
     * @throws Throwable
     */
    private static Map<String, JSONObject> createSectionJson(
            KieProcessFormQueryResult bpmForm,
            List<String> fields) throws Throwable {
        Map<String, JSONObject> map = new HashMap<>();

        if (null != fields
                && !fields.isEmpty()) {
            for (String sectionField : fields) {
                String[] tok = sectionField.split(SEPARATOR);
                String section = tok[0];

                if (!map.containsKey(section)) {
                    map.put(section, new JSONObject());
                    _logger.info("added json for section '{}'", section);
                }
            }
        }
        return map;
    }

    /**
     * Fill each section JSON with appropriate data
     *
     * @param jmap
     * @param input
     * @param formParams
     * @throws Throwable
     */
    private static void buildSections(Map<String, JSONObject> jmap,
                                      Map<String, Object> input,
                                      List<String> formParams) throws Throwable {
        if (null == jmap
                || null == input
                || null == formParams
                || input.isEmpty()
                || jmap.isEmpty()
                || formParams.isEmpty()) {
            return;
        }
        for (String parameter : formParams) {
            String[] tok = parameter.split(SEPARATOR);
            String section = tok[0];
            String field = tok[1];

            // select the json
            JSONObject json = jmap.get(section);
            // add data
            json.put(field, input.get(parameter));
            _logger.info("adding field '{}' in section '{}'", field, section);
        }
    }

    /**
     * Merge all JSON objects into a unique one containig all data
     *
     * @param jmap
     * @throws Throwable
     */
    private static void buildInternalSection(String mainDataModelerEntry,
                                             Map<String, JSONObject> jmap) throws Throwable {
        if (null == jmap
                || jmap.isEmpty()) {
            return;
        }
        JSONObject main = jmap.get(mainDataModelerEntry);
        if (null == main) {
            _logger.warn("could not find the '{}' JSON", mainDataModelerEntry);
            return;
        }
        for (String section : jmap.keySet()) {
            if (section.equals(mainDataModelerEntry)) {
                continue;
            }
            main.put(section,
                    jmap.get(section));
        }
    }

    /**
     * Generate the payload to deliver to the BPM to start a new process
     *
     * @param bpmForm
     * @param input               map containing
     * @param containerId
     * @param processDefinitionId
     * @return
     * @throws Throwable
     */
    // FIXME assume infite level of nesting!
    public static String generateFormJson(final KieProcessFormQueryResult bpmForm,
                                          final Map<String, Object> input,
                                          final String containerId,
                                          final String processDefinitionId) throws Throwable {
        JSONObject ojson = new JSONObject();
        JSONObject ajson = new JSONObject();
        List<String> formParams = BpmToFormHelper.getParamersMap(bpmForm);
        Map<String, JSONObject> jmap = createSectionJson(bpmForm, formParams);
        KieDataHolder mainDataHolder = BpmToFormHelper.getFormDataModelerEntry(bpmForm);

        if (null == mainDataHolder
                || null == mainDataHolder.getId()
                || null == mainDataHolder.getValue()) {
            _logger.error("cannot find main mainDataHolder OR invalid mainDataHolder detected, aborting");
        }

        // process sections, compose inner json with all data, all components are still separated
        buildSections(jmap, input, formParams);
        // prepare application object
        buildInternalSection(mainDataHolder.getId(), jmap);
        // prepare main dataholder section
        ajson.put(mainDataHolder.getValue(),
                jmap.get(mainDataHolder.getId()));
        // finally prepare the overall JOSN
        ojson.put(mainDataHolder.getId(), ajson);
        ojson.put(FIELD_PROCESS_DEFINITION_ID, processDefinitionId);
        ojson.put(FIELD_CONTANER_ID, containerId);
        return ojson.toString();
    }

    /**
     * Validate the input field against the given form field and return the
     * input with a appropriate object type
     *
     * @param bpmForm
     * @param fieldName
     * @param value
     * @return the input value with the proper class, NULL otherwise
     * @throws Throwable
     */
    public static Object validateField(final KieProcessFormQueryResult bpmForm, final String fieldName, final String value) throws Throwable {
        Object result = null;

        if (null == bpmForm
                || StringUtils.isBlank(fieldName)) {
            return result;
        }
        // get the field
        KieProcessFormField field = BpmToFormHelper.getFormField(bpmForm, fieldName);
        if (null == field) {
            return new NullFormField();
        }
        // check whether the data is mandatory
        final boolean mandatory =
                BpmToFormHelper.getFieldRequired(field).equalsIgnoreCase("true");
        if (null == value) {
            if (!mandatory) {
                return new NullFormField();
            } else {
                return result;
            }
        }
        // get the data type
        final String fieldClass = BpmToFormHelper.getFieldClass(field);
        if (null == fieldClass) {
            return result;
        }
        try {
            if (fieldClass.equals(STRING)) {
                result = String.valueOf(value);
            } else if (fieldClass.equals(INTEGER)) {
                result = Integer.valueOf(value);
            } else if (fieldClass.equals(BOOLEAN)) {
                if (value.equalsIgnoreCase("true")
                        || value.equalsIgnoreCase("false")) {
                    result = Boolean.valueOf(value);
                }
            } else {
                _logger.warn("unknown field class type '{}'", fieldClass);
                result = value;
            }
        } catch (Throwable t) {
            result = null;
        }
        return result;
    }

    /**
     * Validate all form fields, turning them into expected type
     *
     * @param form
     * @param input
     * @return
     * @throws Throwable if a validation fails against given data
     */
    public static Map<String, Object> validateForm(final KieProcessFormQueryResult form, final Map<String, String> input) throws Throwable {
        Map<String, Object> output = new HashMap<String, Object>();

        for (Map.Entry<String, String> ff : input.entrySet()) {
            String key = ff.getKey();
            String value = ff.getValue();

            Object obj = FormToBpmHelper.validateField(form, key, value);
            if (null != obj) {
                if (obj instanceof NullFormField) {
                    _logger.info("the field '{}' is null, but is not mandatory: ignoring", key);
                } else {
                    output.put(key, obj);
                }
            } else {
                // no luck
                throw new RuntimeException("validation failed for field " + key + " with value " + value);
            }
        }
        return output;
    }


    /**
     * Create the name of a form field to be used in the payload
     *
     * @param field
     * @param id    of the current dataModelerEntry of the form
     * @return
     */
    public static String generateFieldNameForInput(final KieProcessFormField field, final String id) {
        if (StringUtils.isBlank(id)
                || null == field) {
            return null;
        }
        String name = field.getName();

        // process name
        return name.replace(id.concat("_"), "");
    }

    /**
     * Insert in the given JSON the name of the given field with the given value
     *
     * @param field
     * @param json
     * @param id
     */
    private static void modelField2Json(final KieProcessFormField field, final JSONObject json, final String id, Map<String, Object> input) throws Throwable {


        if (null != field) {
            final String out = BpmToFormHelper.getFieldOutputBinding(field);
            final String name = generateFieldNameForInput(field, id);
            final String key = field.getName();

            if (StringUtils.isBlank(out)) {
                System.out.println("SKIPPING " + field.getName());
//               return;
            }
            if (input.containsKey(key)) {
                json.put(name, input.get(key));
            } else {
                json.put(name, JSONObject.NULL);
            }
        }
    }

    /**
     * Generate a JSON with the same level of nesting of the given form. This
     * is the most important part of the payload being sent to the BPM.
     * All keys have the specified value
     *
     * @param form  the human task form structure
     * @param input
     * @param json  the result of the process is stored here
     * @throws java.lang.Throwable
     */
    public static void modelForm2Json(final KieProcessFormQueryResult form, final Map<String, Object> input, JSONObject json) throws Throwable {
        if (null == form
                || null == json) {
            return;
        }
        if (null != form.getForms()
                && !form.getForms().isEmpty()) {
            for (KieProcessFormQueryResult subForm : form.getForms()) {
                JSONObject subJson = new JSONObject();

                modelForm2Json(subForm, input, subJson);
                // get the name of the current dataholder
                KieDataHolder dataModelerEntry =
                        BpmToFormHelper.getFormDataModelerEntry(subForm);
                // add child
                json.put(dataModelerEntry.getOutId(),
                        subJson);
            }
        }
        // process form fields
        if (null == form.getFields()
                || form.getFields().isEmpty()) {
            return;
        }
        KieDataHolder dataModelerEntry =
                BpmToFormHelper.getFormDataModelerEntry(form);
        for (KieProcessFormField field : form.getFields()) {
            modelField2Json(field, json, dataModelerEntry.getId(), input);
        }
    }

    /**
     * Generate a JSON with the same level of nesting of the given form. This
     * is the most important part of the payload being sent to the BPM.
     * We also add the extra fields of the form data into the payload.
     * <p>
     * The REST API which returns the human task form DATA presents some fields
     * that are NOT present in the structure of the same form. Even if it is OK
     * to submit the payload without those extra data, it's not a mistake to
     * submit them too.
     *
     * @param form   the human task form structure
     * @param data   the human task form data
     * @param input  input with the data
     * @param result the result of the process is stored here
     * @throws Throwable
     */
    public static void modelForm2Json(final KieProcessFormQueryResult form,
                                      final JSONObject data, final Map<String, Object> input, JSONObject result) throws Throwable {
        if (null == form
                || null == result
                || null == data
                || null == input) {
            return;
        }
        if (null != form.getForms()
                && !form.getForms().isEmpty()) {
            for (KieProcessFormQueryResult subForm : form.getForms()) {
                JSONObject subJson = new JSONObject();

                modelForm2Json(subForm, data, input, subJson);
                // get the name of the current dataholder
                KieDataHolder dataModelerEntry =
                        BpmToFormHelper.getFormDataModelerEntry(subForm);
                // add child
                result.put(dataModelerEntry.getOutId(),
                        subJson);
            }
        }
        // process form fields
        if (null == form.getFields()
                || form.getFields().isEmpty()) {
            return;
        }
        KieDataHolder dataModelerEntry =
                BpmToFormHelper.getFormDataModelerEntry(form);
        // get related section in data
        Object obj = JsonHelper.findKey(data, dataModelerEntry.getValue());
        if (null == obj
                || !(obj instanceof JSONObject)) {
            throw new RuntimeException("Unexpected data format for index " +
                    dataModelerEntry.getValue());
        }
        // get the section from the task data with extra fields
        JSONObject section = (JSONObject) obj;
        // process fields present in the form structure
        for (KieProcessFormField field : form.getFields()) {
            modelField2Json(field, result, dataModelerEntry.getId(), input);
        }
        // process extra fields present in the form and add them to the payload
        final List<String> formFields = Arrays.asList(JSONObject.getNames(result));
        final List<String> dataFields = Arrays.asList(JSONObject.getNames(section));

        for (String key : dataFields) {
            final String expectedFieldName =
                    BpmToFormHelper.generateFieldNameForOutput(key,
                            dataModelerEntry.getId());

            if (!formFields.contains(key)) {

                if (input.containsKey(expectedFieldName)) {
                    Object value = input.get(expectedFieldName);

                    if (null != value) {
                        result.put(key, input.get(expectedFieldName));
                    } else {
                        result.put(key, JSONObject.NULL);
                    }
                } else {
                    // must exists, assign a null value
                    result.put(key, JSONObject.NULL);
                }
            }
        }
    }

    /**
     * Generate the payload of the human task form
     *
     * @param form
     * @param task
     * @param input
     * @return
     * @throws Throwable
     */
    public static String generateHumanTaskFormJson(final KieProcessFormQueryResult form,
                                                   final JSONObject task,
                                                   final Map<String, Object> input) throws Throwable {
        JSONObject payload = new JSONObject();
        // get the modeler entry value to locate data
        KieDataHolder dataModeler =
                BpmToFormHelper.getFormDataModelerEntry(form);

        FormToBpmHelper.modelForm2Json(form, task, input, payload);
        // incapsulate payload in a key with the value of the datamodeler
        JSONObject dataModelerJson = new JSONObject();

        if (task.get("task-form").equals("FinancialReview")) {
            JSONObject obj = new JSONObject();
            if  (((String)input.get("brokerOverride")).equalsIgnoreCase("on")){
                obj.put("brokerOverrideTaskOutput", "true");
            }
            else {
                obj.put("brokerOverrideTaskOutput", "false");
            }

            return obj.toString();

        } else {
            dataModelerJson.put(dataModeler.getValue(), payload);
        }


        // incapsulate with the key with value of the output id
        JSONObject outputIdJson = new JSONObject();

        outputIdJson.put(dataModeler.getOutId(),
                dataModelerJson);
        return outputIdJson.toString();
    }


    public final static String FIELD_PROCESS_DEFINITION_ID = "processDefinitionId";
    public final static String FIELD_CONTANER_ID = "containerId";

    public final static String STRING = "java.lang.String";
    public final static String INTEGER = "java.lang.Integer";
    public final static String BOOLEAN = "java.lang.Boolean";
}
