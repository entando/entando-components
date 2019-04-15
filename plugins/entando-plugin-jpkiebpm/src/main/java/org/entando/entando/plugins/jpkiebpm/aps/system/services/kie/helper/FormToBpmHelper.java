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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper;

import java.math.BigDecimal;
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

    private final static String FIELD_PROCESS_DEFINITION_ID = "processDefinitionId";
    private final static String FIELD_CONTAINER_ID = "containerId";

    private final static String STRING = "java.lang.String";
    private final static String INTEGER = "java.lang.Integer";
    private final static String BOOLEAN = "java.lang.Boolean";
    private final static String BIG_DECIMAL = "java.math.BigDecimal";
    private final static String DATE = "java.util.Date";
    private final static String LIST = "java.util.List";

    /**
     * Create a JSON for each section of the form
     *
     * @param fields
     * @return
     * @throws Throwable
     */
    private static Map<String, JSONObject> createSectionJson(
            List<KieProcessFormField> fields) {
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
     * @param map
     * @param input
     * @param formParams
     * @return JSONObject with all scalar values
     * @throws Throwable
     */
    private static JSONObject buildSections(Map<String, JSONObject> map,
                                            Map<String, Object> input,
                                            List<KieProcessFormField> formParams,
                                            boolean scalarForm) {
        JSONObject singles = new JSONObject();
        if (null == map
                || null == input
                || null == formParams
                || input.isEmpty()
                || map.isEmpty()
                || formParams.isEmpty()) {
            return singles;
        }
        logger.debug("INPUT KEYS:  {}", input.keySet().toString());

        for (KieProcessFormField formField : formParams) {

            logger.debug("Processing formField {} of type {}: ", formField, formField.getType());

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
                    logger.debug("add input parameter {} with value false", parameter);

                }
                if (!scalarForm) {
                    // select the json
                    JSONObject json = map.get(section);
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
                        || ((formField.getType().equals("CheckBox") && input.get(parameter) == null))) {
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
     * @param map
     * @throws Throwable
     */
    private static void buildInternalSection(String mainDataModelerEntry,
                                             Map<String, JSONObject> map) {
        if (null == map
                || map.isEmpty()) {
            return;
        }
        JSONObject main = map.get(mainDataModelerEntry);
        if (null == main) {
            logger.warn("could not find the '{}' JSON", mainDataModelerEntry);
            return;
        }
        for (String section : map.keySet()) {
            if (section.equals(mainDataModelerEntry)) {
                continue;
            }
            main.put(section,
                    map.get(section));
        }
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
     * Generate the payload to deliver to the BPM to start a new process
     *
     * @param bpmForm
     * @param input map containing
     * @param containerId
     * @param processDefinitionId
     * @return
     * @throws Throwable
     */
    // FIXME assume infinite level of nesting!
    public static String generateFormJson(final KieProcessFormQueryResult bpmForm,
                                          final Map<String, Object> input,
                                          final String containerId,
                                          final String processDefinitionId) throws Throwable {
        JSONObject ajson = new JSONObject();
        List<KieProcessFormField> formParams = BpmToFormHelper.getKieFormFields(bpmForm);
        Map<String, JSONObject> jmap = createSectionJson(formParams);
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
            // prepare main data holder section
            ajson.put(mainDataHolder.getValue(),
                    jmap.get(mainDataHolder.getId()));
            // finally prepare the overall JSON
            outJSON.put(mainDataHolder.getId(), ajson);
        }
        outJSON.put(FIELD_PROCESS_DEFINITION_ID, processDefinitionId);
        outJSON.put(FIELD_CONTAINER_ID, containerId);
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
            return null;
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
                logger.debug("not mandatory {} return NullFormField()", field.getName());
                return new NullFormField();
            } else {
                logger.debug("mandatory {} return {}", field.getName(), null);
                return null;
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
            return null;
        }
        try {
            logger.debug("field {} is {}", field, fieldClass);

            switch (fieldClass) {
                case STRING:
                case LIST:
                    result = value;
                    break;
                case INTEGER:
                    result = Integer.valueOf(value);
                    break;
                case BIG_DECIMAL:
                    result = new BigDecimal(value);
                    break;
                case BOOLEAN:
                    if (value.equalsIgnoreCase("true")
                            || value.equalsIgnoreCase("false")) {
                        result = Boolean.valueOf(value);
                    } else if ("on".equalsIgnoreCase(value)) {
                        result = true;
                    } else if ("off".equalsIgnoreCase(value)) {
                        result = false;
                    }
                    break;
                case DATE:
                    //Date Format YYYY-MM-DD or YYYY-MM-DD hh:mm
                    SimpleDateFormat dateFormat;
                    if (field.getProperty("showTime").getValue().equals("true")) {
                        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    } else {
                        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    }


                    result = dateFormat.parse(value);

                    break;

                default:
                    logger.warn("unknown field class type '{}'", fieldClass);
                    result = value;
                    break;
            }
        } catch (Throwable t) {
            result = null;
        }
        logger.warn("validateField {} return result '{}'", fieldName, result);

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
        Map<String, Object> output = new HashMap<>();

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
     * Generate the payload of the human task form
     *
     * @param form
     * @param task
     * @param input
     * @return
     * @throws Throwable
     */
    public static JSONObject generateHumanTaskFormJson(final KieProcessFormQueryResult form,
                                                       final JSONObject task,
                                                       final Map<String, Object> input) throws Throwable {
        JSONObject payload;
        JSONObject taskInputData = task.getJSONObject("task-input-data");

        payload = FormToBpmHelper.generateJsonPayload(form, taskInputData, input);
        return payload;
    }

    private static void logFormStructure(final KieProcessFormQueryResult formStructure) throws Throwable {
        logFormStructure(formStructure, 0);
    }

    private static void logFormStructure(final KieProcessFormQueryResult formStructure, int nestLevel) throws Throwable {

        if (nestLevel == 0) {
            logger.debug("");
            logger.debug("***********************************************");
            logger.debug("*              logFormStructure               *");
        }
        KieDataHolder dataModelerEntry = BpmToFormHelper.getFormDataModelerEntry(formStructure);

        int length = nestLevel * 4;
        String str = "";
        if (length > 1) {
            char[] charArray = new char[length - 1];
            Arrays.fill(charArray, ' ');
            str = new String(charArray);
        }

        str = str + "----";

        if (null != dataModelerEntry) {
            logger.debug(str + dataModelerEntry.getName() + " outId: " + dataModelerEntry.getOutId());
        }

        if (null != formStructure.getNestedForms() && !formStructure.getNestedForms().isEmpty()) {
            nestLevel += 1;
            for (KieProcessFormQueryResult subForm : formStructure.getNestedForms()) {
                logFormStructure(subForm, nestLevel);
            }
        }
        logger.debug("***********************************************");
    }

    static JSONObject generateJsonPayload(final KieProcessFormQueryResult formStructure, final JSONObject taskInputDataJson, final Map<String, Object> inputFields) throws Throwable {
        logger.info("*               generateJsonPayload              *");
        logFormStructure(formStructure);
        JSONObject taskJsonResult = new JSONObject();
        if (generateJsonCheckInputNull(formStructure, taskInputDataJson, inputFields)) {
            return null;
        }
        KieDataHolder mainDataHolder = BpmToFormHelper.getFormDataModelerEntry(formStructure);
        if ((mainDataHolder == null) || mainDataHolder.getOutId().equals("task")) {
            logger.debug("*   mainDataHolder == null  or equal task        *");
            taskJsonResult = taskInputDataJson;
        } else {
            logger.debug("*               mainDataHolder NOT null              *");
            logger.info("mainDataHolder id: {}", mainDataHolder.getId());
            logger.info("mainDataHolder.getValue : {}", mainDataHolder.getValue());
            logger.info("mainDataHolder name: {}", mainDataHolder.getName());
            logger.info("mainDataHolder out id: {}", mainDataHolder.getOutId());
            logger.info("mainDataHolder type: {}", mainDataHolder.getType());
            logger.info("mainDataHolder : {}", mainDataHolder);
            FindKeyInJsonResult keyInJsonRes = findKeyInJson(mainDataHolder, taskInputDataJson);
            logger.info("OBJECT: " + keyInJsonRes.getJsonResult());
            Object obj = keyInJsonRes.getJsonResult();
            if (!(obj instanceof JSONObject)) {
                taskJsonResult = taskInputDataJson;
            } else {
                // finally prepare the overall JSON

                JSONObject jsonObj = new JSONObject();
                jsonObj.put(mainDataHolder.getValue(), obj);
                taskJsonResult.put(mainDataHolder.getOutId(), jsonObj);
            }
        }
        logger.debug("generateJsonPayload taskJsonResult: {}", taskJsonResult.toString(4));
        return generateJsonPayloadDetail(formStructure, taskJsonResult, inputFields);
    }

    private static boolean generateJsonCheckInputNull(KieProcessFormQueryResult formStructure, JSONObject taskInputDataJson, Map<String, Object> inputFields) {
        if (null == formStructure
                || null == taskInputDataJson
                || null == inputFields
                || null == formStructure.getFields()) {
            logger.info("generateJsonPayload return null");
            return true;
        }
        return false;
    }

    /**
     * Generate a new JSON based on the task input data json returned by the PAM
     * api. if the dataModel is not null else generate a JSON with the same
     * level of nesting of the given form. This is the most important part of
     * the payload being sent to the PAM.
     * <p>
     * The REST API which returns the human task form DATA presents some fields
     * that are NOT present in the structure of the same form. Even if it is OK
     * to submit the payload without those extra data, it's not a mistake to
     * submit them too.
     *
     * @param formStructure     the human task form structure
     * @param taskInputDataJson the human task form data
     * @param inputFields       input with the data
     * @throws Throwable
     */
    private static JSONObject generateJsonPayloadDetail(final KieProcessFormQueryResult formStructure, final JSONObject taskInputDataJson, final Map<String, Object> inputFields) throws Throwable {
        logger.info("generateJsonPayloadDetail");

        KieDataHolder dataModelerEntry = BpmToFormHelper.getFormDataModelerEntry(formStructure);

        JSONObject taskJsonResult = taskInputDataJson;

        if (generateJsonCheckInputNull(formStructure, taskInputDataJson, inputFields)) return null;

        if (null != formStructure.getNestedForms() && !formStructure.getNestedForms().isEmpty()) {
            for (KieProcessFormQueryResult subForm : formStructure.getNestedForms()) {
                KieDataHolder dataModelerEntrySubForm = BpmToFormHelper.getFormDataModelerEntry(subForm);

                String lowerCaseOut = dataModelerEntrySubForm.getName().substring(0, 1).toLowerCase() + dataModelerEntrySubForm.getName().substring(1);
                logger.debug("elaborating subForm {}", lowerCaseOut);
                generateJsonPayloadDetail(subForm, taskJsonResult, inputFields);

            }
        }


        if (null != dataModelerEntry) {

            logger.debug(" dataModelerEntry.getName() {}", dataModelerEntry.getName());
            logger.debug(" dataModelerEntry.getInputId() {}", dataModelerEntry.getInputId());
            logger.debug(" dataModelerEntry.getType() {}", dataModelerEntry.getType());
            logger.debug(" dataModelerEntry.getValue() {}", dataModelerEntry.getValue());


            for (KieProcessFormField field : formStructure.getFields()) {
                boolean isReadOnly = false;

                logger.debug("field : " + field.getProperty("fieldClass").toString());


                if (null != field.getProperty("readOnly")) {
                    isReadOnly = Boolean.parseBoolean(field.getProperty("readOnly").getValue());
                }
                String name = field.getName();
                if (!isReadOnly) {
                    logger.debug("FIELD !isReadOnly REPLACE");
                    String[] splitName = name.split("_");

                    logger.info("splitName.length : " + splitName.length);

                    if (splitName.length > 1) {
                        logger.debug("splitName.length > 1 : {}", splitName.length);
                        logger.debug("field name : " + name);
                        logger.debug("dataModelerEntry.getName() {}", dataModelerEntry.getName());
                        logger.debug("dataModelerEntry.getValue() {}", dataModelerEntry.getValue());
                        String lowerFirstChar = Character.toLowerCase(name.charAt(0)) + name.substring(1);
                        FindKeyInJsonResult keyInJsonRes = findKeyInJson(dataModelerEntry, taskJsonResult);
                        logger.debug("OBJECT Key: {} value {}", keyInJsonRes.getKey(), keyInJsonRes.getJsonResult());
                        Object obj = keyInJsonRes.getJsonResult();
                        String newObjectKey = keyInJsonRes.getKey();
                        if (obj instanceof JSONObject) {
                            Object newValue = inputFields.get(lowerFirstChar);
                            if (null != newValue) {
                                JSONObject objNew = JsonHelper.replaceObject((JSONObject) obj, splitName[1], newValue);
                                taskJsonResult = JsonHelper.replaceObject(Objects.requireNonNull(taskJsonResult), newObjectKey, objNew);
                            }
                        }
                    }
                } else {
                    logger.debug("NOT splitName.length > 1 ");
                }
            }
        } else {
            // No data modeler entry, just add values directly to result
            logger.info("Found no dataModelEntry, so generating result directly");
            List<KieProcessFormField> formParams = BpmToFormHelper.getKieFormFields(formStructure);
            Map<String, JSONObject> map = createSectionJson(formParams);
            // process sections, compose inner json with all data, all components are still separated
            taskJsonResult = buildSections(map, inputFields, formParams, true);
        }
        if (null != taskJsonResult) {
            logger.debug("taskJsonResult:\n {}", taskJsonResult.toString(4));
        }
        return taskJsonResult;

    }


    private static FindKeyInJsonResult findKeyInJson(KieDataHolder dataModelerEntry, JSONObject taskJson) {
        logger.info("FindKeyInJsonResult");

        FindKeyInJsonResult result = new FindKeyInJsonResult();
        String newObjectKey = dataModelerEntry.getValue();
        Object obj = JsonHelper.findKey(taskJson, dataModelerEntry.getValue());

        if (!(obj instanceof JSONObject)) {
            newObjectKey = dataModelerEntry.getOutId();
            obj = JsonHelper.findKey(taskJson, newObjectKey);
        }
        //Brutal hack. PAM API changed and removed the top level references and appended taskInput
        //to the name of the
        //container. Replace once a proper transform layer is in place
        if (!(obj instanceof JSONObject)) {
            newObjectKey = "taskInput" + dataModelerEntry.getName();
            obj = JsonHelper.findKey(taskJson, newObjectKey);

        }

        if (!(obj instanceof JSONObject)) {
            //TODO Figure Out how to read this value from the form instead of generating it manually
            newObjectKey = dataModelerEntry.getName().substring(0, 1).toLowerCase() + dataModelerEntry.getName().substring(1);
            obj = JsonHelper.findKey(taskJson, newObjectKey);
        }

        result.setJsonResult(obj);
        result.setKey(newObjectKey);
        logger.debug("result.getKey {}", result.getKey());
        logger.debug("result.getJsonResult {}", result.getJsonResult());
        return result;
    }


    private static class FindKeyInJsonResult {
        private String key;
        private Object jsonResult;

        String getKey() {
            return key;
        }

        void setKey(String key) {
            this.key = key;
        }

        Object getJsonResult() {
            return jsonResult;
        }

        void setJsonResult(Object jsonResult) {
            this.jsonResult = jsonResult;
        }
    }
}
