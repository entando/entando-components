package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieDataHolder;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessProperty;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven.*;

import java.util.ArrayList;
import java.util.Random;

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
        for(PamArray array : pamProcessQueryFormResult.getArrays()) {
            if(first){
                queryResult = pamSevenFormToPamSix(array);
                first = false;
            }else{
                if(queryResult.getNestedForms() ==null) {
                    queryResult.setNestedForms(new ArrayList<>());
                }
                queryResult.getNestedForms().add(pamSevenFormToPamSix(array));
            }
        }

        return queryResult;
    }
    public static KieProcessFormQueryResult pamSevenFormToPamSix(PamArray pamSeven) {

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

            addDataholder(result, propName, propName, propName, type, className);
        }

        if(result.getFields() == null) {
            result.setFields(new ArrayList<>());
        }

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
            addField(result, fieldId, modelName, fieldName, label, code, required, type);
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

    private static void addField(KieProcessFormQueryResult result, String id, String modelName, String fieldName, String label, String code, Boolean required, String type) {


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


        result.getFields().add(field);



    }
}
