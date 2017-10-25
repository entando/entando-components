package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.util;

import com.agiletec.aps.system.services.i18n.II18nManager;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiFields;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiFieldset;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiForm;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessProperty;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.DefaultValueOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.IBpmOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.PlaceHolderOverride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class KieApiUtil {

    protected KieApiUtil() {
    }

    protected static final String FIELD_TYPE_INPUT_TEXT_INTEGER = "InputTextInteger";
    protected static final String FIELD_TYPE_INPUT_CHECKBOX = "CheckBox";

    private static final Logger _logger = LoggerFactory.getLogger(KieApiUtil.class);

    public static KieApiForm createForm(KieProcessFormQueryResult processForm, II18nManager ii18nManager, String langCode, Map<String, KieFormOverride> overrideMap) {
        KieApiForm kieApiForm = new KieApiForm();
        kieApiForm.setId(processForm.getId().toString());
        kieApiForm.setName(getFieldProperty(processForm.getProperties(), "name"));
        addFields(kieApiForm, processForm, ii18nManager, langCode, overrideMap);
        return kieApiForm;
    }

    protected static void addFields(final KieApiForm mainForm, final KieProcessFormQueryResult processForm, final II18nManager ii18nManager, final String langCode,
            final Map<String, KieFormOverride> overrideMap) {

        final KieApiFields fields = new KieApiFields();
        final KieApiFieldset fieldset = new KieApiFieldset(processForm.getHolders().get(0).getId());

        if (null != processForm.getFields()) {
            for (KieProcessFormField field : processForm.getFields()) {
                fieldset.getFields().add(createField(field, ii18nManager, langCode, overrideMap));
            }
            fields.addFieldset(fieldset);
            mainForm.addFields(fields);
        }

        if (null != processForm.getForms()) {
            for (KieProcessFormQueryResult subForm : processForm.getForms()) {
                addFields(mainForm, subForm, ii18nManager, langCode, overrideMap);
            }

        }

    }

    protected static KieApiField createField(KieProcessFormField field, II18nManager ii18nManager, String langCode, Map<String, KieFormOverride> overrides) {
        KieApiField apiField = new KieApiField();
        String labelKey = getI18nLabelProperty(field);
        apiField.setLabelKey(labelKey);
        String caption = resolveCaption(field, ii18nManager, langCode, labelKey);
        apiField.setCaption(caption);
        apiField.setId(getFieldProperty(field.getProperties(), "id"));
        apiField.setName(getFieldName(field));
        apiField.setType(getFieldType(field.getType()));
        apiField.setReadonly(getFieldProperty(field.getProperties(), "readonly").equalsIgnoreCase("true"));
        apiField.setRequired(getFieldProperty(field.getProperties(), "fieldRequired").equalsIgnoreCase("true"));
        apiField.setValue(getFieldProperty(field.getProperties(), "inputBinding"));

        String sizeVal = getFieldProperty(field.getProperties(), "size");
        if (StringUtils.isNotBlank(sizeVal)) {
            apiField.setSize(Integer.valueOf(sizeVal));
        }
        applyOverride(field, overrides, apiField);
        return apiField;
    }

    private static void applyOverride(KieProcessFormField field, Map<String, KieFormOverride> overrides, KieApiField apiField) {
        String valueOverride = null;
        String placeholderOverride = null;

        if (overrides.containsKey(field.getName())) {
            KieFormOverride override = overrides.get(field.getName());
            if (null != override.getOverrides() && null != override.getOverrides().getList()) {
                for (IBpmOverride item : override.getOverrides().getList()) {
                    if (item.getType().equals(DefaultValueOverride.OVERRIDE_TYPE_NAME)) {
                        valueOverride = ((DefaultValueOverride) item).getDefaultValue();
                    } else if (item.getType().equals(PlaceHolderOverride.OVERRIDE_TYPE_NAME)) {

                        placeholderOverride = ((PlaceHolderOverride) item).getPlaceHolder();
                    }
                }
            }
        }
        if (null != valueOverride) {
            apiField.setValue(valueOverride);
        }

        if (null != placeholderOverride) {
            apiField.setPlaceholder(placeholderOverride);
        }
    }

    private static String resolveCaption(KieProcessFormField field, II18nManager ii18nManager, String langCode, String labelKey) {
        String defaultCaption = getFieldProperty(field.getProperties(), "label");
        try {
            String entandoCaption = ii18nManager.getLabel(labelKey, langCode);
            if (StringUtils.isNotBlank(entandoCaption)) {
                defaultCaption = entandoCaption;
            }
        } catch (Exception e) {
            _logger.error("error loading i18n label", e);
        }
        return defaultCaption;
    }

    public static String getI18nLabelProperty(KieProcessFormField field) {
        return KieApiField.LABEL_PREFIX + field.getName();
    }

    public static String getI18nFormLabelProperty(KieProcessFormField field) {
        return KieApiForm.LABEL_PREFIX + field.getName().substring(0, field.getName().indexOf("_"));
    }

    public static String getI18nTitleLabelProperty(String containerId) {
        return KieApiForm.LABEL_TITLE_PREFIX + containerId;
    }

    public static String getI18nTitleLabelValue(String containerId) {
        return KieApiForm.LABEL_TITLE_VALUE_PREFIX + containerId.toUpperCase();
    }

    public static String getI18nFormLabelValue(KieProcessFormField field) {
        return field.getName().substring(0, field.getName().indexOf("_")).toUpperCase();
    }

    public static String getFormNameFromField(KieProcessFormField field) {
        return field.getName().substring(0, field.getName().indexOf("_"));
    }

    public static String getFieldName(KieProcessFormField field) {
        return KieApiField.FIELD_NAME_PREFIX + field.getName();
    }

    protected static String getFieldType(String bpmType) {
        String retVal;
        switch (bpmType) {
            case FIELD_TYPE_INPUT_TEXT_INTEGER:
                retVal = "number";
                break;
            case FIELD_TYPE_INPUT_CHECKBOX:
                retVal = "checkbox";
                break;
            default:
                retVal = "text";
                break;
        }
        return retVal;
    }

    protected static String getFieldProperty(List<KieProcessProperty> props, String property) {
        if (null != props && StringUtils.isNotBlank(property)) {
            for (KieProcessProperty prop : props) {
                if (prop.getName().equals(property)) {
                    return prop.getValue();
                }
            }
        }
        return "";
    }

    public static String getFieldProperty(KieProcessFormField field, String property) {
        if (null != field) {
            for (KieProcessProperty prop : field.getProperties()) {
                if (prop.getName().equals(property)) {
                    return prop.getValue();
                }
            }
        }
        return null;
    }

    public static String showXml(JAXBContext pContext, Object pObject) throws JAXBException {
        java.io.StringWriter sw = new StringWriter();
        Marshaller marshaller = pContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(pObject, sw);
        return sw.toString();
    }
}
