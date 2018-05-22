package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;


import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieVersionTransformer;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;
import org.junit.Test;

import java.io.File;

public class TestPamQueryFormResult extends TestCase {

    @Test
    public void testPamSevenFormParse() throws Throwable{

        try {
            String kieProcessFormXML = FileUtils.readFileToString(new File("src/test/resources/examples/xml/jbpm7-mortgage-process-form.xml"));
            PamProcessQueryFormResult pamSeven = (PamProcessQueryFormResult) JAXBHelper
                    .unmarshall(kieProcessFormXML, PamProcessQueryFormResult.class, true, false);

            KieProcessFormQueryResult result = KieVersionTransformer.pamSevenFormToPamSix(pamSeven);

            String xml = JAXBHelper.marshall(result, true, false);
            System.out.println(xml);
        }catch(Throwable e){
            throw e;
        }


    }
}
