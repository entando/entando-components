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

import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieDataHolder;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.NullFormField;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.BpmToFormHelper.SEPARATOR;

public class FormToBpmHelper {

    private static final Logger logger = LoggerFactory.getLogger(FormToBpmHelper.class);

    public final static String FIELD_PROCESS_DEFINITION_ID = "processDefinitionId";
    public final static String FIELD_CONTANER_ID = "containerId";

    public final static String STRING = "java.lang.String";
    public final static String INTEGER = "java.lang.Integer";
    public final static String BOOLEAN = "java.lang.Boolean";
    public final static String DATE = "java.util.Date";
    public final static String LIST = "java.util.List";

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
            List<KieProcessFormField> fields) throws Throwable {
        Map<String, JSONObject> map = new HashMap<>();

        if (null != fields
                && !fields.isEmpty()) {
            for (KieProcessFormField kieField : fields) {

                String sectionField = kieField.getName();
                String[] tok = sectionField.split(SEPARATOR);
                String section = tok[0];

                if (!map.containsKey(section)) {
                    map.put(section, new JSONObject());
                    logger.info("added json for section '{}'", section);
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
     * @return JSONObject with all scalar values
     * @throws Throwable
     */
    private static JSONObject buildSections(Map<String, JSONObject> jmap,
            Map<String, Object> input,
            List<KieProcessFormField> formParams,
            boolean scalarForm) throws Throwable {
        JSONObject singles = new JSONObject();
        if (null == jmap
                || null == input
                || null == formParams
                || input.isEmpty()
                || jmap.isEmpty()
                || formParams.isEmpty()) {
            return singles;
        }
        logger.debug("INPUT KEYS:  {}", input.keySet().toString());

        for (KieProcessFormField formField : formParams) {

            logger.debug("Processing formField {} of type {}: ",formField, formField.getType());
 
            String parameter = formField.getName();
            String[] tok = parameter.split(SEPARATOR);

            if (tok.length > 1) {
                String section = tok[0];
                String field = tok[1];
                //Boolean checkbox values need to get round tripped as false. Without it data erasure can occur
                //when a box is un-checked by a user. The input data from the user won't contain a key since the value
                //isn't sent by the FE
                if (formField.getType().equals("CheckBox") && !input.containsKey(parameter)) {
                    input.put(parameter, false);
                    logger.debug("add input parameter {} with value false {}" , parameter);

                }
                if (!scalarForm) {
                    // select the json
                    JSONObject json = jmap.get(section);
                    // add data
                    json.put(field, input.get(parameter));                    

                } else {
                    singles.put(field, input.get(parameter));                    
                }
                logger.info("adding field '{}' in section '{}'", field, section);
            } else {
                String field = tok[0];

                //Boolean checkbox values need to get round tripped as false. Without it data erasure can occur
                //when a box is un-checked by a user. The input data from the user won't contain a key since the value
                //isn't sent by the FE
                if ((formField.getType().equals("CheckBox") && !input.containsKey(parameter))
                    ||((formField.getType().equals("CheckBox") && input.get(parameter) == null))){
                    logger.debug("set {} to false ", parameter);

                    input.put(parameter, false);
                }

                singles.put(field, input.get(parameter));
                logger.info("adding single '{}' value '{}'", field, input.get(parameter));
            }

        }
        return singles;
    }

    /**
     * Merge all JSON objects into a unique one containing all data
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
            logger.warn("could not find the '{}' JSON", mainDataModelerEntry);
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
     * @param input map containing
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
        JSONObject ajson = new JSONObject();
        List<KieProcessFormField> formParams = BpmToFormHelper.getKieFormFields(bpmForm);
        Map<String, JSONObject> jmap = createSectionJson(bpmForm, formParams);
        KieDataHolder mainDataHolder = BpmToFormHelper.getFormDataModelerEntry(bpmForm);

        boolean scalarForm = false;
        if (mainDataHolder == null) {
            scalarForm = true;
        }

        // process sections, compose inner json with all data, all components are still separated
        JSONObject outJSON = buildSections(jmap, input, formParams, scalarForm);

        if (null == mainDataHolder
                || null == mainDataHolder.getId()
                || null == mainDataHolder.getValue()) {
            logger.warn("cannot find main mainDataHolder OR invalid mainDataHolder detected, aborting");
        } else {
            // prepare application object
            buildInternalSection(mainDataHolder.getId(), jmap);
            // prepare main dataholder section
            ajson.put(mainDataHolder.getValue(),
                    jmap.get(mainDataHolder.getId()));
            // finally prepare the overall JOSN
            outJSON.put(mainDataHolder.getId(), ajson);
        }
        outJSON.put(FIELD_PROCESS_DEFINITION_ID, processDefinitionId);
        outJSON.put(FIELD_CONTANER_ID, containerId);
        return outJSON.toString();
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
        final boolean mandatory
                = BpmToFormHelper.getFieldRequired(field).equalsIgnoreCase("true");

        logger.debug("the field {} is mandatory {}", field.getName(), mandatory);

        logger.debug("the field {} have value {}", field.getName(), value);

        if (null == value) {
            if (!mandatory) {
                logger.debug("not mandatory {} return NullFormField()", mandatory);

                return new NullFormField();
            } else {
                logger.debug("mandatory {} return {}", result);

                return result;
            }
        }
        if (value.equals("") || value.equals("null")) {

            if (!mandatory) {
                return new NullFormField();
            }
        }
        // get the data type
        final String fieldClass = BpmToFormHelper.getFieldClass(field);
        if (null == fieldClass) {
            return result;
        }
        try {
            logger.debug("{} is {}", field, fieldClass.toString());

            if (fieldClass.equals(STRING)) {
                result = String.valueOf(value);
            } else if (fieldClass.equals(INTEGER)) {
                result = Integer.valueOf(value);
            } else if (fieldClass.equals(BOOLEAN)) {
                if (value == null) {
                    result = false;
                } else if (value.equalsIgnoreCase("true")
                        || value.equalsIgnoreCase("false")) {
                    result = Boolean.valueOf(value);
                } else if ("on".equalsIgnoreCase(value)) {
                    result = true;
                } else if ("off".equalsIgnoreCase(value)) {
                    result = false;
                }
            } else if (fieldClass.equals(DATE)) {
                //Date Format YYYY-MM-DD or YYYY-MM-DD hh:mm 
                SimpleDateFormat dateFormat = null;
                if (field.getProperty("showTime").getValue().equals("true")) {
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                } else {
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                }
                result = dateFormat.parse(value);

            } else if (fieldClass.equals(LIST)) {
                result = value;

            } else {
                logger.warn("unknown field class type '{}'", fieldClass);
                result = value;
            }
        } catch (Throwable t) {
            result = null;
        }
        logger.warn("return result '{}'", result);

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
            logger.debug("validate field {} with value {}", key, value);

            Object obj = FormToBpmHelper.validateField(form, key, value);
            if (null != obj) {
                if (obj instanceof NullFormField) {
                    logger.debug("the field '{}' is null, but is not mandatory: ignoring", key);
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
     * @param id of the current dataModelerEntry of the form
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
                logger.debug("SKIPPING " + field.getName());
            }
            if (input.containsKey(key)) {
                json.put(name, input.get(key));
            } else {
                json.put(name, JSONObject.NULL);
            }
        }
    }

    /**
     * Generate a JSON with the same level of nesting of the given form. This is
     * the most important part of the payload being sent to the BPM. All keys
     * have the specified value
     *
     * @param form the human task form structure
     * @param input
     * @param json the result of the process is stored here
     * @throws java.lang.Throwable
     */
    public static void modelForm2Json(final KieProcessFormQueryResult form, final Map<String, Object> input, JSONObject json) throws Throwable {
        if (null == form
                || null == json) {
            return;
        }
        if (null != form.getNestedForms()
                && !form.getNestedForms().isEmpty()) {
            for (KieProcessFormQueryResult subForm : form.getNestedForms()) {
                JSONObject subJson = new JSONObject();

                modelForm2Json(subForm, input, subJson);
                // get the name of the current dataholder
                KieDataHolder dataModelerEntry
                        = BpmToFormHelper.getFormDataModelerEntry(subForm);
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
        KieDataHolder dataModelerEntry
                = BpmToFormHelper.getFormDataModelerEntry(form);
        for (KieProcessFormField field : form.getFields()) {
            modelField2Json(field, json, dataModelerEntry.getId(), input);
        }
    }

    /**
     * Generate a JSON with the same level of nesting of the given form. This is
     * the most important part of the payload being sent to the BPM. We also add
     * the extra fields of the form data into the payload.
     * <p>
     * The REST API which returns the human task form DATA presents some fields
     * that are NOT present in the structure of the same form. Even if it is OK
     * to submit the payload without those extra data, it's not a mistake to
     * submit them too.
     *
     * @param form the human task form structure
     * @param data the human task form data
     * @param input input with the data
     * @param result the result of the process is stored here
     * @throws Throwable
     */
    public static JSONObject modelForm2Json(final KieProcessFormQueryResult form,
            final JSONObject data, final Map<String, Object> input, JSONObject result) throws Throwable {
        if (null == form
                || null == result
                || null == data
                || null == input
                || null == form.getFields()
                || form.getFields().isEmpty()) {
            return null;
        }
        if (null != form.getNestedForms() && !form.getNestedForms().isEmpty()) {
            for (KieProcessFormQueryResult subForm : form.getNestedForms()) {
                JSONObject subJson = new JSONObject();

                modelForm2Json(subForm, data, input, subJson);
                // get the name of the current dataholder
                KieDataHolder dataModelerEntry = BpmToFormHelper.getFormDataModelerEntry(subForm);
                // add child
                result.put(dataModelerEntry.getOutId(), subJson);
            }
        }

        KieDataHolder dataModelerEntry = BpmToFormHelper.getFormDataModelerEntry(form);
        // get related section in data if dataModelerEntry is not null

        if (dataModelerEntry != null) {
            Object obj = JsonHelper.findKey(data, dataModelerEntry.getValue());
            if (null == obj || !(obj instanceof JSONObject)) {
                obj = JsonHelper.findKey(data, dataModelerEntry.getOutId());
            }

            //Brutal hack. PAM API changed and removed the top level references and appended taskInput to the name of the
            //container. Replace once a proper transform layer is in place
            if (null == obj || !(obj instanceof JSONObject)) {
                obj = JsonHelper.findKey(data, "taskInput" + dataModelerEntry.getName());
            }

            if (null == obj || !(obj instanceof JSONObject)) {
                throw new RuntimeException("Unexpected data for key " + dataModelerEntry.getValue());
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
                final String expectedFieldName
                        = BpmToFormHelper.generateFieldNameForOutput(key, dataModelerEntry.getId());

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
        } else {
            // No data modeler entry, just add values directly to result
            logger.debug("Found no dataModelEntry, so generating result directly");
            List<KieProcessFormField> formParams = BpmToFormHelper.getKieFormFields(form);
            Map<String, JSONObject> jmap = createSectionJson(form, formParams);

            // process sections, compose inner json with all data, all components are still separated
            result = buildSections(jmap, input, formParams, true);
        }
        return result;
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

        payload = FormToBpmHelper.modelForm2Json(form, task, input, payload);

        // get the modeler entry value to locate data
        KieDataHolder dataModeler = BpmToFormHelper.getFormDataModelerEntry(form);

        // incapsulate with the key with value of the output id
        JSONObject outputIdJson = new JSONObject();

        // incapsulate payload in a key with the value of the datamodeler
        JSONObject dataModelerJson = new JSONObject();

        if (dataModeler != null) {
            dataModelerJson.put(dataModeler.getValue(), payload);
            outputIdJson.put(dataModeler.getOutId(), dataModelerJson);

            JSONObject inputData = task.getJSONObject("task-input-data");
            mergeTaskData(outputIdJson, inputData);
        } else {
            outputIdJson = payload;
            logger.debug("outputIdJson: " + outputIdJson.toString());
        }

        return outputIdJson.toString();
    }

    //1. Find common top level keys to begin merge since the nesting is different. taskInput is the data from BPM and formOutput is what we plan to send back
    //2. Merge keys that exist in taskInput back into formOutput so that we round trip all the data
    //3. Don't overwrite anything.
    //
    //This assumes that there is a top level entity that holds all the forms. For example 'taskInputApplication'
    //and that the merge can occur against the children of that object.
    private static void mergeTaskData(JSONObject formOutput, JSONObject taskInput) {

        Iterator<String> keys = formOutput.keys();
        String firstKey = keys.next();

        if (formOutput.get(firstKey) instanceof JSONObject) {

            //Process the top level keys in the form to go back to BPM and find the corresponding form in the input data
            //so that the tree merge can be performed one-to-one
            Set<String> formChildKeys = formOutput.getJSONObject(firstKey).keySet();
            for (String formChildKey : formChildKeys) {
                Object starting = findStartingPoint(formChildKey, taskInput);
                if (starting != null && starting instanceof JSONObject) {

                    JSONObject mergePointChild = formOutput.getJSONObject(firstKey).getJSONObject(formChildKey);
                    mergeDetails(mergePointChild, (JSONObject) starting);
                }
            }
        }
    }

    //Take the keys from task input (this is the data we get from BPM about the process)
    // and if you find one that is missing copy it into the output we are sending back to round trip
    //the data that wasn't user visible. Recursively process pairs so that all of the nested data makes it back.
    //Only copies missing keys
    private static void mergeDetails(JSONObject formOutput, JSONObject taskInput) {

        Set<String> keys = taskInput.keySet();

        for (String key : keys) {
            if (!formOutput.has(key) || formOutput.get(key).equals(JSONObject.NULL)) {
                formOutput.put(key, taskInput.get(key));
            } else if (taskInput.get(key) instanceof JSONObject) {

                //Recurse and check for missing child forms (for example property nested under appraisal in the mortgage data)
                mergeDetails(formOutput.getJSONObject(key), taskInput.getJSONObject(key));
            }
        }
    }

    //Find a common top level key for a first level child represented by outputKey.
    private static Object findStartingPoint(String outputKey, JSONObject taskInput) {

        Set<String> inputKeys = taskInput.keySet();
        for (String inputKey : inputKeys) {
            if (inputKey.equals(outputKey)) {
                return taskInput.get(inputKey);
            }

        }

        for (String inputKey : inputKeys) {
            if (taskInput.get(inputKey) instanceof JSONObject) {
                return findStartingPoint(outputKey, taskInput.getJSONObject(inputKey));
            }
        }

        return null;
    }

}
