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

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieDataHolder;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessProperty;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.IBpmOverride;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Entando
 */
public class BpmToFormHelper {

    private static final Logger _logger = LoggerFactory.getLogger(BpmToFormHelper.class);

    /**
     * Return the desired field value from the given KIE form field object
     *
     * @param field
     * @param paramName
     * @return
     * @throws Throwable
     */
    public static String getField(final KieProcessFormField field, final String paramName) throws Throwable {
        String section = null;

        if (null == field) {
            return section;
        }
        for (KieProcessProperty prop : field.getProperties()) {
            if (prop.getName().equals(paramName)) {
                return prop.getValue();
            }
        }
        return section;
    }

    /**
     * Return the output binding value of the given KIE form field object
     *
     * @param field
     * @return
     * @throws Throwable
     */
    public static String getFieldOutputBinding(final KieProcessFormField field) throws Throwable {
        return getField(field, OUTPUT_BINDING);
    }

    /**
     * Return the input binding value of the given KIE form field object
     *
     * @param field
     * @return
     * @throws Throwable
     * @deprecated
     */
    public static String getFieldInputBinding(final KieProcessFormField field) throws Throwable {
        return getField(field, INPUT_BINDING);
    }

    /**
     * Return the field class value of the given form field object
     *
     * @param field
     * @return
     * @throws Throwable
     */
    public static String getFieldClass(final KieProcessFormField field) throws Throwable {
        return getField(field, FIELD_CLASS);
    }

    /**
     * Return the value of the required field
     *
     * @param field
     * @return
     * @throws Throwable
     */
    public static String getFieldRequired(final KieProcessFormField field) throws Throwable {
        return getField(field, FIELD_REQUIRED);
    }

    private static List<String> getParamersMap(List<String> map, KieProcessFormQueryResult form) throws Throwable {
        if (null == form) {
            return map;
        }
        if (null != form.getForms()) {
            for (KieProcessFormQueryResult subForm : form.getForms()) {
                map = getParamersMap(map, subForm);
            }
        }
        // iterate over form data;
        for (KieProcessFormField field : form.getFields()) {
            final String val = field.getName(); // getFieldOutpuBinding(field);

            // skip if needed
            if (StringUtils.isBlank(val)) {
                continue;
            }
            map.add(val);
            _logger.info("inserting value in parameters map '{}'", val);
        }
        return map;
    }

    /**
     * Get the map of the form output bindings of each field
     *
     * @param map
     * @param form
     * @return
     * @throws Throwable
     */
    public static List<String> getParamersMap(KieProcessFormQueryResult form) throws Throwable {
        return getParamersMap(new ArrayList<String>(),
                form);
    }

    /**
     * Get the data modeler entry of the desired form
     *
     * @param form
     * @return
     * @throws Throwable
     */
    public static KieDataHolder getFormDataModelerEntry(KieProcessFormQueryResult form) throws Throwable {
        if (null == form) {
            return null;
        }

        final List<KieDataHolder> holders = form.getHolders();

        for (KieDataHolder holder : holders) {
            if (holder.getType().equals(DATA_MODELER_ENTRY)) {
                return holder;
            }
        }
        return null;
    }

    /**
     * Finf the given field in a form
     *
     * @param result
     * @param form
     * @param name
     * @return
     * @throws Throwable
     */
    private static KieProcessFormField getFormField(KieProcessFormField result, KieProcessFormQueryResult form, String name) throws Throwable {
        if (null == form) {
            return result;
        }
        if (null != form.getForms()
                && null == result) {
            for (KieProcessFormQueryResult subForm : form.getForms()) {
                result = getFormField(result, subForm, name);
            }
        }
        // iterate over form data
        for (KieProcessFormField field : form.getFields()) {
            final String val = field.getName();

            // skip if needed
            if (StringUtils.isBlank(val)) {
                continue;
            }
            if (val.equals(name)) {
                return field;
            }
        }
        return result;
    }

    /**
     * Get the desired field from a form
     *
     * @param form
     * @param name
     * @return
     * @throws Throwable
     */
    public static KieProcessFormField getFormField(KieProcessFormQueryResult form, String name) throws Throwable {
        return getFormField(null, form, name);
    }


    /**
     * Add the override
     *
     * @param form
     * @param overrides
     * @throws Throwable
     */
    public static void appendOverridesToForm(KieProcessFormQueryResult form, List<KieFormOverride> overrides) throws Throwable {
        if (null == form
                || null == overrides
                || overrides.isEmpty()) {
            return;
        }
        List<KieProcessFormField> fields = form.getFields();
        for (KieFormOverride override : overrides) {
            String name = override.getField();
            List<IBpmOverride> overrideList = override.getOverrides().getList();

            final KieProcessFormField kieField = getFormField(form, name);

            if (null != kieField) {
                for (IBpmOverride ovr : overrideList) {
                    // insert the override in the kie-bsp
                    kieField.getProperties().add(ovr.toKieProperty());
                }
            } else {
                _logger.error("override field '{}' does not match with the corresponding field in the form", name);
            }
        }
    }

    /**
     * Collect form data recursively
     *
     * @param form
     * @param result
     * @throws Throwable
     */
    public static void collectHumanTaskdata(final KieProcessFormQueryResult form, Map<String, String> result) throws Throwable {
        if (null != form.getForms()) {
            for (KieProcessFormQueryResult innerForm : form.getForms()) {
                collectHumanTaskdata(innerForm, result);
            }
        }
        if (null != form.getFields()) {
            for (KieProcessFormField field : form.getFields()) {
                result.put(field.getName(), getFieldInputBinding(field));
            }
        }
    }

    /**
     * Generate the name of the field expected to be found in the JSON of the
     * form
     *
     * @param name
     * @param id
     * @return
     * @throws Throwable
     */
    public static String generateFieldNameForOutput(final String name, final String id) throws Throwable {
        if (StringUtils.isBlank(id)
                || StringUtils.isBlank(name)) {
            return null;
        }
        return id.concat("_").concat(name);
    }


    /**
     * Collect data from the human task form data
     *
     * @param form
     * @param data data JSON returned from the API
     * @return
     * @throws Throwable
     * @note the visit is destructive of the data field
     */
    private static void fetchHumanTaskFormData(final KieProcessFormQueryResult form, final JSONObject data, Map<String, Object> result) throws Throwable {
        if (null == form
                || null == data) {
            return;
        }
        if (null != form.getForms()
                && !form.getForms().isEmpty()) {
            for (KieProcessFormQueryResult subForm : form.getForms()) {
                getHumanTaskFormData(subForm, data, result);
            }
        }
        if (null == form.getFields()
                || form.getFields().isEmpty()) {
            return;
        }
        KieDataHolder dataModeler =
                BpmToFormHelper.getFormDataModelerEntry(form);
        Object obj = JsonHelper.findKey(data, dataModeler.getValue());
        if (null == obj
                || !(obj instanceof JSONObject)) {
            throw new RuntimeException("Unexpected data for key " + dataModeler.getValue());
        }
        JSONObject section = (JSONObject) obj;
        String sectionName = dataModeler.getId();
        List<String> sectionFields = new ArrayList<>();
        // collect field
        for (KieProcessFormField field : form.getFields()) {
            String jsonName = FormToBpmHelper.generateFieldNameForInput(field, sectionName);
            Object value = null;
            try {
                value = section.get(jsonName);
            } catch (org.json.JSONException ex) {
            }
            if (JSONObject.NULL == value) {
                result.put(field.getName(), null);
            } else {
                result.put(field.getName(), value);
            }
        }
        // collect JSON data. Some field might not exist in the form definition!
        for (String name : JSONObject.getNames(section)) {
            final String key = generateFieldNameForOutput(name, sectionName);
            final Object value = section.get(name);

            // skip json objects
            if (value instanceof JSONObject) {
                continue;
            }
            if (JSONObject.NULL == value) {
                result.put(key, null);
            } else {
                result.put(key, value);
            }
        }
        // to avoid name collisions we delete the section just inspected
        JsonHelper.replaceKey(data, dataModeler.getValue(), "  ");
    }

    /**
     * Collect data from the human task form data
     *
     * @param form
     * @param data
     * @return
     * @throws Throwable
     */
    public static void getHumanTaskFormData(final KieProcessFormQueryResult form, final JSONObject data, Map<String, Object> result) throws Throwable {
        fetchHumanTaskFormData(form, new JSONObject(data.toString()), result);
    }

    public final static String DATA_MODELER_ENTRY = "dataModelerEntry";
    public final static String SEPARATOR = "_";
    public final static String OUTPUT_BINDING = "outputBinding";
    public final static String INPUT_BINDING = "inputBinding";
    public final static String FIELD_CLASS = "fieldClass";
    public final static String FIELD_REQUIRED = "fieldRequired";
}
