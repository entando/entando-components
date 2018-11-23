package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven.*;
import org.slf4j.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to manage transformations between the 6.x and 7.x Kie server formats.
 * In most cases the downstream code in the plugin expects the 6.x beans so
 * going out to Kie we transform from 6 to 7 when required. And coming in we go
 * from 7 back to 6 when needed
 */
public class KieVersionTransformer {

    private static final Logger logger = LoggerFactory.getLogger(KieVersionTransformer.class);

    private static Random rand = new Random();

    public static KieProcessFormQueryResult pamSevenFormToPamSix(PamProcessQueryFormResult pamProcessQueryFormResult) {

        KieProcessFormQueryResult queryResult = null;

        List<String> formModelTypes = new ArrayList<>();
        Map<String, PamArray> pamIdMap = new HashMap<>();
        Map<String, String> fieldIdToArrayId = new HashMap<>();

        //Nested forms have two ids. One is a "fieldId" and one is the "formid" of the form itself. This crosswalk between
        //form and field matters to the processing because the top level entry only references the fieldId but the formId
        //is needed to capture the actual data. So build  two maps. One to map from formId to the data in the <array> and
        //one to go from the "fieldId" to the "formId". Used below to order the forms in the transformed output.
        for (PamArray array : pamProcessQueryFormResult.getArrays()) {
            String modelType = array.getModel().getClassName();
            formModelTypes.add(modelType);

            pamIdMap.put(array.getId(), array);
            List<PamFields> fields = array.getPamFields();

            for (PamFields field : fields) {
                String id = field.getId();
                String nestedForm = field.getNestedForm();

                if (nestedForm != null) {
                    fieldIdToArrayId.put(id, nestedForm);
                }
            }
        }

        Map<String, PamArray> fieldToForms = new HashMap<>();
        for (Map.Entry<String, String> entry : fieldIdToArrayId.entrySet()) {
            fieldToForms.put(entry.getKey(), pamIdMap.get(entry.getValue()));
        }

        List<String> fieldIdOrder = new ArrayList<String>();

        for (PamArray array : pamProcessQueryFormResult.getArrays()) {
            if (array.getModel().getProcessId() != null) {
                queryResult = pamSevenFormToPamSix(array, formModelTypes);

                //This call processes the child forms of the parent in order based on the form id references. The output
                //here is an ordered list of the child forms in the order they should appear when rendered
                buildFormOrder(array, fieldIdOrder, fieldToForms);
                break;
            }
        }

        if (queryResult.getNestedForms() == null) {
            queryResult.setNestedForms(new ArrayList<>());
        }

        //TODO This count thing is a total hack. This should be done by processing the forms sequentially based on layout
        //order. Fix me
        //Process the forms in the order identified in the buildFormOrder call and add to the result in that order
        int count = 0;
        String parentDataHolderOutId = queryResult.getHolders().get(0).getOutId();
        for (String fieldId : fieldIdOrder) {

            PamArray pamArray = fieldToForms.get(fieldId);
            
            if (pamArray != null) {

                if (count == 0) {
                    queryResult = pamSevenFormToPamSix(pamArray, formModelTypes);
                    queryResult.getHolders().get(0).setOutId(parentDataHolderOutId);
                }

                if (count > 0) {
                    if (queryResult.getNestedForms() == null) {
                        queryResult.setNestedForms(new ArrayList<>());
                    }
                    queryResult.getNestedForms().add(pamSevenFormToPamSix(pamArray, formModelTypes));
                }

                count++;
            }
        }
        return queryResult;
    }

    private static void buildFormOrder(PamArray array, List<String> fieldIdOrder, Map<String, PamArray> nestedFormMap) {
        
        List<PamFields> fields = array.getPamFields();
        Map<String, PamFields> fieldMap = new HashMap<>();
        for (PamFields field : fields) {
            fieldMap.put(field.getId(), field);
        }

        //This method starts with the top level entry that is identified as the one that has a "process-id"
        //and then iterates all of the children that are forms. This allows the forms to get processed and displayed in the
        //order that is implicit in the layoutTemplate in the forms. So start with the forms referenced by the top level and
        //add further child ids to the list.
        //
        //This is an abbomination and should be replaced soon. The downstream processing of the form is very dependent
        //on the order the forms and fields arrive in at this time.
        List<PamLayoutTemplateRow> rows = array.getLayoutTemplate().getRows();
        for (PamLayoutTemplateRow row : rows) {
            List<PamLayoutColumn> columns = row.getLayoutColums();
            for (PamLayoutColumn column : columns) {
                List<PamLayoutComponent> components = column.getLayoutComponents();
                if (components != null) {
                    for (PamLayoutComponent component : components) {
                        if (component.getProperties() != null && component.getProperties().getFieldId() != null) {
                            String fieldId = component.getProperties().getFieldId();

                            if (nestedFormMap.containsKey(fieldId)) {

                                PamFields field = fieldMap.get(fieldId);
                                if (!field.getReadOnly()) {
                                    PamArray nestedArray = nestedFormMap.get(fieldId);
                                    if(nestedArray != null) {
                                        fieldIdOrder.add(fieldId);
                                        buildFormOrder(nestedArray, fieldIdOrder, nestedFormMap);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static KieProcessFormQueryResult pamSevenFormToPamSix(PamArray pamSeven, List<String> formModelTypes) {
        
        KieProcessFormQueryResult result = new KieProcessFormQueryResult();

        result.setId(rand.nextLong());
        result.setProperties(new ArrayList<>());
        String name = pamSeven.getName();
        addProperty(result, "name", name);

        String id = pamSeven.getId();
        addProperty(result, "pamSevenId", id);

        PamModel model = pamSeven.getModel();
        String modelName = model.getName();
        String modelerType = "dataModelerEntry";;
        String modelClassName = model.getClassName();

        //Make sure the model name is the first "dataModelerEntry" if a subfield is first it breaks bindings later on.
        //This ensures the form levels are labeled properly
        String modelNameDisplay = StringUtils.capitalize(modelName);
        if (modelNameDisplay != null) {
            addDataholder(result, modelName, modelNameDisplay, modelName, modelerType, modelClassName);
        }
        for (PamProperty property : model.getProperties()) {
            String propName = property.getName();
            String className = property.getTypeInfo().getClassName();
            String type = property.getTypeInfo().getType();

            boolean readOnly = false;
            if (property.getMetaData() != null && property.getMetaData().getEntries() != null) {

                Optional<PamEntry> readOnlyEntry = property.getMetaData()
                        .getEntries().stream()
                        .filter(e -> e.getName().equals("field-readOnly"))
                        .findFirst();

                if (readOnlyEntry.isPresent() && readOnlyEntry.get().getValue().equals("true")) {
                    readOnly = true;
                }

            }

            String propId = propName;
            //In PAM 6.x the main container entity for each form was marked  as the dataModelerEntry. Downstream the form helper
            //logic depends on this marker. The marker doesn't exist in 7.x so we add manually when the value of the class
            //on the interior property matches the value of a top level entity on one of the other forms.
            if (formModelTypes.contains(className)) {
                type = "dataModelerEntry";
            }

            if (!readOnly && propName != null) {
                addDataholder(result, propName, propName, propName, type, className);
            }
        }

        if (result.getFields() == null) {
            result.setFields(new ArrayList<>(pamSeven.getPamFields().size()));
        }

        //Build a map of the fields so they can be added to the form. The order the fields are added matters to the rendering
        //so this loop just creates the fields and indexes them. That then gets crosswalked based on the layout tempalte in the XML in the loop below.
        Map<String, KieProcessFormField> fieldMap = new HashMap<>();
        for (PamFields field : pamSeven.getPamFields()) {
            String fieldId = field.getId();
            String fieldName = field.getName();
            String label = field.getLabel();
            String type = field.getStandaloneClassName();
            String code = field.getCode();
            Boolean required = field.getRequired();
            Boolean showTime = field.getShowTime();
            String placeHolder = field.getPlaceHolder();
            
            Boolean addEmptyOption = field.isAddEmptyOption();
            String defaultValue = field.getDefaultValue();
            //MultipleSelector properties
            List<String> options =  field.getListOfValues();
         
            //RadioGroup properties
            Boolean inline = field.isInline();

            //ListBox properties
            List<PamFieldsOptions>  optionsList = field.getOptions();
            if (null!=optionsList) {
                options = new ArrayList<String>();
                for(PamFieldsOptions option : optionsList){
                        options.add(option.getText() + "=" + option.getValue());
                        };

            }
            
            int maxElementsOnTitle = field.getMaxElementsOnTitle();
            int maxDropdownElements = field.getMaxDropdownElements();
            boolean allowClearSelection = field.isAllowClearSelection();
            boolean allowFilter = field.isAllowFilter();

            if (code.equals("SubForm")) {
                //Sub forms will get processed and be nested under the root. This prevents duplicates
                continue;
            }
            KieProcessFormField builtField = buildField(fieldId, 
                    modelName, fieldName, label, code, required, type, showTime, placeHolder,
                    options, maxElementsOnTitle, maxDropdownElements, allowClearSelection, allowFilter,
                    addEmptyOption, defaultValue, inline);
       
            if (field.getReadOnly()) {
                builtField.addProperty("readOnly", true);
            }
            
            fieldMap.put(fieldId, builtField);
        }

        //The fields have to be added to the parent form in order. So start with the <rows> inside the <layoutTemplate>
        //and then lookup the field definition from the fieldMap created above to add the details to the parent form.
        int count = 0;
        List<PamLayoutTemplateRow> rows = pamSeven.getLayoutTemplate().getRows();

        for (PamLayoutTemplateRow row : rows) {

            //TODO Support columns in form gen from PAM fields
            //The PAM 7 form supports the creation of forms in rows and columns. The current data model
            //layout doesn't suppor the creation of separate columns for fields on the same row. So move everything to
            //a row in the result for now.
            List<PamLayoutColumn> columns = row.getLayoutColums();
            for (PamLayoutColumn column : columns) {

                List<PamLayoutComponent> components = column.getLayoutComponents();
                if (components != null) {
                    for (PamLayoutComponent component : components) {
                        String fieldId = component.getProperties().getFieldId();

                        //If the value is a form it won't be in the map. Those get ordered elsewhere. This just orders fields
                        if (fieldMap.containsKey(fieldId)) {
                            fieldMap.get(fieldId).setPosition(count);
                            result.getFields().add(fieldMap.get(fieldId));
                            count++;
                        }
                    }
                }
            }
        }
        return result;
    }

    public static void pamSixFormToPamSeven() {

    }

    private static void addProperty(KieProcessFormQueryResult result, String name, String value) {
        KieProcessProperty prop = new KieProcessProperty();
        prop.setName(name);
        prop.setValue(value);
        result.getProperties().add(prop);
    }

    private static void addDataholder(KieProcessFormQueryResult result, String id, String name, String outId, String basicType, String value) {

        KieDataHolder holder = new KieDataHolder();
        holder.setId(id);
        holder.setName(name);
        holder.setOutId(outId);
        holder.setType(basicType);
        holder.setValue(value);
        if (result.getHolders() == null) {
            result.setHolders(new ArrayList<>());
        }
        result.getHolders().add(holder);

    }

    private static KieProcessFormField buildField(  String id, String modelName,
                                                    String fieldName, 
                                                    String label, 
                                                    String code, 
                                                    Boolean required, 
                                                    String type, 
                                                    boolean dateTime, 
                                                    String placeHolder,
                                                    List<String> options,
                                                    int maxElementsOnTitle,
                                                    int maxDropdownElements,
                                                    boolean allowClearSelection,
                                                    boolean allowFilter,
                                                    boolean addEmptyOption,
                                                    String defaultValue,
                                                    boolean inline) {

        KieProcessFormField field = new KieProcessFormField();
        field.setProperties(new ArrayList<>());

        field.setId(id);
        field.setName((modelName != null
                && !"process".equalsIgnoreCase(modelName)
                && !"task".equalsIgnoreCase(modelName))
                ? modelName + "_" + fieldName
                : fieldName);
        field.setType(code);

        KieProcessProperty prop = new KieProcessProperty();
        prop.setName("label");
        prop.setValue(label);
        field.getProperties().add(prop);

        KieProcessProperty req = new KieProcessProperty();
        req.setName("fieldRequired");
        req.setValue(required + "");
        field.getProperties().add(req);

        KieProcessProperty fieldClass = new KieProcessProperty();
        fieldClass.setName("fieldClass");
        fieldClass.setValue(type);
        field.getProperties().add(fieldClass);

        KieProcessProperty placeHolderProperty = new KieProcessProperty();
        placeHolderProperty.setName("placeHolder");
        placeHolderProperty.setValue(placeHolder);
        field.getProperties().add(placeHolderProperty);

        if (field.getType().equals("DatePicker")) {
            KieProcessProperty showTime = new KieProcessProperty();
            showTime.setName("showTime");
            showTime.setValue(String.valueOf(dateTime));
            field.getProperties().add(showTime);
        }
         
        if (field.getType().equals("ListBox")) {
            KieProcessProperty optionsProperty = new KieProcessProperty();
            optionsProperty.setName("options");
            String collect = options.stream().collect(Collectors.joining(","));

            optionsProperty.setValue(collect);
            field.getProperties().add(optionsProperty);            
            
            KieProcessProperty addEmptyOptionProperty = new KieProcessProperty();
            addEmptyOptionProperty.setName("addEmptyOption");
            addEmptyOptionProperty.setValue(String.valueOf(addEmptyOption));
            field.getProperties().add(addEmptyOptionProperty);

            KieProcessProperty defaultValueProperty = new KieProcessProperty();
            defaultValueProperty.setName("defaultValue");
            defaultValueProperty.setValue(String.valueOf(defaultValue));
            field.getProperties().add(defaultValueProperty);


        }
        
        if (field.getType().equals("RadioGroup")) {
            KieProcessProperty optionsProperty = new KieProcessProperty();
            optionsProperty.setName("options");
            String collect = options.stream().collect(Collectors.joining(","));

            optionsProperty.setValue(collect);
            field.getProperties().add(optionsProperty);

            KieProcessProperty inlineProperty = new KieProcessProperty();
            inlineProperty.setName("inline");
            inlineProperty.setValue(String.valueOf(inline));
            field.getProperties().add(inlineProperty);

            KieProcessProperty defaultValueProperty = new KieProcessProperty();
            defaultValueProperty.setName("defaultValue");
            defaultValueProperty.setValue(String.valueOf(defaultValue));
            field.getProperties().add(defaultValueProperty);

        }
        
        if (field.getType().equals("MultipleSelector")) {
            KieProcessProperty maxElementsOnTitleProperty = new KieProcessProperty();
            maxElementsOnTitleProperty.setName("maxElementsOnTitle");
            maxElementsOnTitleProperty.setValue(String.valueOf(maxElementsOnTitle));
            field.getProperties().add(maxElementsOnTitleProperty);

            KieProcessProperty maxDropdownElementsProperty = new KieProcessProperty();
            maxDropdownElementsProperty.setName("maxDropdownElements");
            maxDropdownElementsProperty.setValue(String.valueOf(maxDropdownElements));
            field.getProperties().add(maxDropdownElementsProperty);

            KieProcessProperty allowClearSelectionProperty = new KieProcessProperty();
            allowClearSelectionProperty.setName("allowClearSelection");
            allowClearSelectionProperty.setValue(String.valueOf(allowClearSelection));
            field.getProperties().add(allowClearSelectionProperty);

            KieProcessProperty allowFilterProperty = new KieProcessProperty();
            allowFilterProperty.setName("allowFilter");
            allowFilterProperty.setValue(String.valueOf(allowFilter));
            field.getProperties().add(allowFilterProperty);

            KieProcessProperty optionsProperty = new KieProcessProperty();
            optionsProperty.setName("listOfValues");
            String collect = options.stream().collect(Collectors.joining(","));

            optionsProperty.setValue(collect);
            field.getProperties().add(optionsProperty);

        }
        return field;
    }

}
