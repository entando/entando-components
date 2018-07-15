package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieDataHolder;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessProperty;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven.*;

import java.util.*;

/**
 * Class to manage transformations between the 6.x and 7.x Kie server formats. In most cases the downstream code
 * in the plugin expects the 6.x beans so going out to Kie we transform from 6 to 7 when required. And coming in we go from
 * 7 back to 6 when needed
 */
public class KieVersionTransformer {

    private static Random rand = new Random();
    public static KieProcessFormQueryResult pamSevenFormToPamSix(PamProcessQueryFormResult pamProcessQueryFormResult) {

        boolean first = true;
        KieProcessFormQueryResult queryResult = null;

        List<String> formModelTypes = new ArrayList<>();
        Map<String, PamArray> pamIdMap = new HashMap<>();
        Map<String, String> fieldIdToArrayId = new HashMap<>();

        for(PamArray array : pamProcessQueryFormResult.getArrays()) {
            String modelType = array.getModel().getClassName();
            formModelTypes.add(modelType);

            pamIdMap.put(array.getId(), array);
            List<PamFields> fields = array.getPamFields();

            for(PamFields field : fields) {
                String id = field.getId();
                String nestedForm = field.getNestedForm();

                if(nestedForm !=null) {
                    fieldIdToArrayId.put(id, nestedForm);
                }
            }
        }

        Map<String, PamArray> fieldToForms = new HashMap<>();
        for(Map.Entry<String, String> entry : fieldIdToArrayId.entrySet()) {

            fieldToForms.put(entry.getKey(), pamIdMap.get(entry.getValue()));
        }

        List<String> fieldIdOrder = new ArrayList<String>();

        for(PamArray array : pamProcessQueryFormResult.getArrays()) {
            if(first){
                queryResult = pamSevenFormToPamSix(array, formModelTypes);
                buildFormOrder(array, fieldIdOrder, fieldToForms);
                first = false;
            }
        }


        if(queryResult.getNestedForms() ==null) {
            queryResult.setNestedForms(new ArrayList<>());
        }

        for(String fieldId : fieldIdOrder) {
            queryResult.getNestedForms().add(pamSevenFormToPamSix(fieldToForms.get(fieldId), formModelTypes));
        }
        return queryResult;
    }

    private static void buildFormOrder(PamArray array,List<String> fieldIdOrder,  Map<String, PamArray> nestedFormMap) {

        List<PamLayoutTemplateRow> rows = array.getLayoutTemplate().getRows();
        for(PamLayoutTemplateRow row : rows) {
            List<PamLayoutColumn> columns = row.getLayoutColums();
            for(PamLayoutColumn column : columns) {
                List<PamLayoutComponent> components = column.getLayoutComponents();
                for(PamLayoutComponent component : components) {
                    if(component.getProperties()!=null && component.getProperties().getFieldId() !=null){
                        String fieldId = component.getProperties().getFieldId();


                        if(nestedFormMap.containsKey(fieldId)) {
                            fieldIdOrder.add(fieldId);
                            buildFormOrder(nestedFormMap.get(fieldId), fieldIdOrder, nestedFormMap);
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

        for(PamProperty property : model.getProperties()) {
            String propName = property.getName();
            String className = property.getTypeInfo().getClassName();
            String type = property.getTypeInfo().getType();

            //In PAM 6.x the main container entity for each form was marked  as the dataModelerEntry. Downstream the form helper
            //logic depends on this marker. The marker doesn't exist in 7.x so we add manually when the value of the class
            //on the interior property matches the value of a top level entity on one of the other forms.
            if(formModelTypes.contains(className)) {
                type = "dataModelerEntry";
            }
            addDataholder(result, propName, propName, propName, type, className);
        }

        if(result.getFields() == null) {
            result.setFields(new ArrayList<>(pamSeven.getPamFields().size()));
        }

        List<PamLayoutTemplateRow> rows =  pamSeven.getLayoutTemplate().getRows();
        Map<String, Integer> rowIndex = new HashMap<>();

        Map<String, KieProcessFormField> fieldMap = new HashMap<>();
        for(PamFields field : pamSeven.getPamFields()) {
            String fieldId = field.getId();
            String modelName = pamSeven.getName().toLowerCase();
            String fieldName = field.getName();
            String label = field.getLabel();
            String type = field.getStandaloneClassName();
            String code = field.getCode();
            Boolean required = field.getRequired();

            if(code.equals("SubForm")) {
                //Sub forms will get processed and be nested under the root. This prevents duplicates
                continue;
            }
            KieProcessFormField builtField = buildField(fieldId, modelName, fieldName, label, code, required, type);
            fieldMap.put(fieldId, builtField);
        }

        int count = 0;
        for(PamLayoutTemplateRow row : rows) {

            //TODO Support columns in form gen from PAM fields
            //The PAM 7 form supports the creation of forms in rows and columns. The current data model
            //layout doesn't suppor the creation of separate columns for fields on the same row. So move everything to
            //a row in the result for now.
            List<PamLayoutColumn> columns = row.getLayoutColums();
            for(PamLayoutColumn column : columns) {

                List<PamLayoutComponent> components = column.getLayoutComponents();
                for(PamLayoutComponent component : components){
                    String fieldId = component.getProperties().getFieldId();

                    //If the value is a form it won't be in the map. Those get ordered elsewhere. This just orders fields
                    if(fieldMap.containsKey(fieldId)) {
                        fieldMap.get(fieldId).setPosition(count);
                        result.getFields().add(fieldMap.get(fieldId));
                        count++;
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
        if(result.getHolders()==null) {
            result.setHolders(new ArrayList<>());
        }
        result.getHolders().add(holder);
    }

    private static KieProcessFormField buildField(String id, String modelName, String fieldName, String label, String code, Boolean required, String type) {


        KieProcessFormField field = new KieProcessFormField();
        field.setProperties(new ArrayList<>());

        field.setId(id);
        field.setName(modelName+"_"+fieldName);
        field.setType(code);

        KieProcessProperty prop = new KieProcessProperty();
        prop.setName("label");
        prop.setValue(label);
        field.getProperties().add(prop);

        KieProcessProperty req =  new KieProcessProperty();
        req.setName("fieldRequired");
        req.setValue(required+"");
        field.getProperties().add(req);

        KieProcessProperty fieldClass =  new KieProcessProperty();
        fieldClass.setName("fieldClass");
        fieldClass.setValue(type);
        field.getProperties().add(fieldClass);


        return field;
    }

}
