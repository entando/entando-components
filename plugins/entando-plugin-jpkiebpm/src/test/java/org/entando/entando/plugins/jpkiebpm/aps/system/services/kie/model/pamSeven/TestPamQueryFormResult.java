package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;


import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieVersionTransformer;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestPamQueryFormResult extends TestCase {

    @Test
    public void testPamSevenFormParse() throws Throwable{

        try {
            String kieProcessFormXML = FileUtils.readFileToString(new File("src/test/resources/examples/xml/jbpm7-mortgage-process-form.xml"));
           String kieProcessFormXML2 = FileUtils.readFileToString(new File("src/test/resources/examples/xml/jbpm7-task-complete-form.xml"));

            PamProcessQueryFormResult pamSeven = (PamProcessQueryFormResult) JAXBHelper
                    .unmarshall(kieProcessFormXML, PamProcessQueryFormResult.class, true, false);

            assertNotNull(pamSeven.getArrays().get(0).getLayoutTemplate());

            assertNotNull(pamSeven.getArrays().get(0).getLayoutTemplate().getRows());
            assertNotNull(pamSeven.getArrays().get(0).getLayoutTemplate().getRows().get(0).getLayoutColums().get(0).getLayoutComponents().get(0));
            KieProcessFormQueryResult result = KieVersionTransformer.pamSevenFormToPamSix(pamSeven);
            List<KieProcessFormQueryResult> forms =  result.getNestedForms();

            PamProcessQueryFormResult pamSeven2 = (PamProcessQueryFormResult) JAXBHelper
                    .unmarshall(kieProcessFormXML2, PamProcessQueryFormResult.class, true, false);

            KieProcessFormQueryResult result2 = KieVersionTransformer.pamSevenFormToPamSix(pamSeven2);
            List<KieProcessFormQueryResult> forms2 =  result2.getNestedForms();


            assertEquals(3, forms.size());


            String xml = JAXBHelper.marshall(result, true, false);

            String xml2 = JAXBHelper.marshall(result2, true, false);
            System.out.println(xml);

            System.out.println(xml2);
        }catch(Throwable e){
            throw e;
        }


    }
}
