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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.BpmToFormHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FormToBpmHelper;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.TestKieProcessForm.kieProcessFormXML;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;

/**
 *
 * @author Entando
 */
public class TestPayload extends TestCase {


    public void testFormPayloadGeneration() throws Throwable {
        String xml = TestKieProcessForm.kieProcessFormXML;
        Map<String, Object> input = new HashMap<String, Object>();
        KieProcessFormQueryResult kpfq = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(kieProcessFormXML, KieProcessFormQueryResult.class, true, false);

        // form submitted values
        input.put("applicant_name", "Matteo");
        input.put("applicant_ssn", 1234567890);
        input.put("applicant_income", 70000);
        input.put("property_address", "via San Giuseppe");
        input.put("property_price", 240000);
        input.put("application_downPayment", 40000);
        input.put("application_amortization", 10);

        BpmToFormHelper.getParamersMap(kpfq);
        String json = FormToBpmHelper.generateFormJson(kpfq, input,
                "com.redhat.bpms.examples:mortgage:1", // container ID
                "com.redhat.bpms.examples.mortgage.MortgageApplication"); // process definition ID

        assertNotNull(json);
//        System.out.println(">>> " + json);
    }



}
