package org.entando.entando.plugins.jpkiebpm.aps.system.services.api;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieVersionTransformer;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessProperty;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven.PamProcessQueryFormResult;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.util.Optional;

public class TestApiTaskInterface extends TestCase {


    @Test
    public void testMergeTaskData() throws Throwable{

        String taskDataJson = FileUtils.readFileToString(new File("src/test/resources/examples/bpmSampleFiles/taskDetails.json-jbpm7.0.1.ga.json"));
        String kieProcessFormXML2 = FileUtils.readFileToString(new File("src/test/resources/examples/bpmSampleFiles/jbpm7-task-complete-form.xml"));

        JSONObject taskData = new JSONObject(taskDataJson);
        PamProcessQueryFormResult pamSeven2 = (PamProcessQueryFormResult) JAXBHelper
                .unmarshall(kieProcessFormXML2, PamProcessQueryFormResult.class, true, false);

        KieProcessFormQueryResult form = KieVersionTransformer.pamSevenFormToPamSix(pamSeven2);


        ApiTaskInterface apiTaskInterface = new ApiTaskInterface();
        apiTaskInterface.mergeTaskData(taskData, form);

        String downPayment = "";
        for(KieProcessFormField field :  form.getFields()) {
            if(field.getName().equals("application_downPayment")) {
                Optional<KieProcessProperty> downPaymentVal = field.getProperties()
                                                    .stream()
                                                    .filter(p -> p.getName().equals("inputBinding"))
                                                    .findFirst();
                if(downPaymentVal.isPresent()) {
                    downPayment = downPaymentVal.get().getValue();
                }

            }
        }

        assertEquals(12345.0, Double.parseDouble(downPayment) );

    }

}
