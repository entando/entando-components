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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessProperty;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.TestKieProcessForm.kieProcessFormXML;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.DefaultValueOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.DropDownOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.PlaceHolderOverride;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;
import org.json.JSONObject;

/**
 *
 * @author Entando
 */
public class TestBpmToFormHelper extends TestCase {

    public void testFind() throws Throwable {
        KieProcessFormQueryResult kpfq = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(kieProcessFormXML, KieProcessFormQueryResult.class, true, false);

        assertNotNull(kpfq);
        KieProcessFormField res = BpmToFormHelper.getFormField(kpfq, "application_downPayment");
        assertNotNull(res);
        assertEquals((Integer)2,
                res.getPosition());
        assertEquals("InputTextInteger",
                res.getType());
        assertEquals((Long)1601486948L,
                res.getId());

        res = BpmToFormHelper.getFormField(kpfq, "property_address");
        assertNotNull(res);
        assertEquals((Integer)0,
                res.getPosition());
        assertEquals("InputText",
                res.getType());
        assertEquals((Long)2013333214L,
                res.getId());

    }


    public void testFindInvalid() throws Throwable {
        KieProcessFormQueryResult kpfq = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(kieProcessFormXML, KieProcessFormQueryResult.class, true, false);

        assertNotNull(kpfq);
        KieProcessFormField res = BpmToFormHelper.getFormField(kpfq, "property_address_blah");
        assertNull(res);
    }


    public void testOverrideMeshup() throws Throwable {
        final KieFormOverride kfo1 = new KieFormOverride();
        final KieFormOverride kfo2 = new KieFormOverride();
        final KieProcessFormQueryResult kpfq = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(kieProcessFormXML, KieProcessFormQueryResult.class, true, false);

        // field one
        final DropDownOverride ddv = new DropDownOverride();
        final DefaultValueOverride dfv = new DefaultValueOverride();

        ddv.setValues(Arrays.asList(new String[] {"val1", "val2"}));
        dfv.setDefaultValue("default value");
        kfo1.addOverride(ddv);
        kfo1.addOverride(dfv);
        kfo1.setField("applicant_income");

        // field two
        final PlaceHolderOverride pho = new PlaceHolderOverride();

        pho.setPlaceHolder("placeholder");
        kfo2.setField("application_downPayment");
        kfo2.addOverride(pho);

        BpmToFormHelper.appendOverridesToForm(kpfq,
                Arrays.asList(new KieFormOverride[] {kfo1, kfo2}));


        // verify the override of the field #1
        KieProcessFormField field = BpmToFormHelper.getFormField(kpfq, "applicant_income");
        assertNotNull(field);
        assertNotNull(field.getProperties());
        assertFalse(field.getProperties().isEmpty());
        boolean ok = false;
        for (KieProcessProperty prop: field.getProperties()) {
            if (prop.getName().equals(DropDownOverride.OVERRIDE_TYPE_NAME)
                    && prop.getValue().equals("val1,val2")) {
                ok = true;
            }
        }
        assertTrue(ok);
        ok = false;
        for (KieProcessProperty prop: field.getProperties()) {
            if (prop.getName().equals(DefaultValueOverride.OVERRIDE_TYPE_NAME)
                    && prop.getValue().equals("default value")) {
                ok = true;
            }
        }
        assertTrue(ok);

        // verify the override of the field #2
        field = BpmToFormHelper.getFormField(kpfq, "application_downPayment");
        assertNotNull(field);
        assertNotNull(field.getProperties());
        assertFalse(field.getProperties().isEmpty());
        ok = false;
        for (KieProcessProperty prop: field.getProperties()) {
            if (prop.getName().equals(PlaceHolderOverride.OVERRIDE_TYPE_NAME)
                    && prop.getValue().equals("placeholder")) {
                ok = true;
            }
        }
        assertTrue(ok);
    }

    public void testsGetFormData() throws Throwable {
        Map<String, Object> map = new HashMap<String, Object>();
        KieProcessFormQueryResult form = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(TASK_FORM_DEF_XML, KieProcessFormQueryResult.class, true, false);
        assertNotNull(form);
        JSONObject data = new JSONObject(TASK_DATA_JSON);
        BpmToFormHelper.getHumanTaskFormData(form, data, map);
//        for (Map.Entry<String, Object> entry: map.entrySet()) {
//            System.out.println("--> " + entry.getKey() + ":" + entry.getValue());
//        }
        assertTrue(map.containsKey("application_amortization"));
        assertTrue(map.containsKey("applicant_name"));
        assertTrue(map.containsKey("applicant_ssn"));
        assertTrue(map.containsKey("applicant_income"));
        assertTrue(map.containsKey("application_downPayment"));
        assertTrue(map.containsKey("property_address"));
        assertTrue(map.containsKey("property_price"));
        // check extra fields coming from the task data API
        assertTrue(map.containsKey("application_appraisal"));
        assertTrue(map.containsKey("application_mortgageAmount"));
        assertTrue(map.containsKey("application_validationErrors"));
        assertTrue(map.containsKey("application_apr"));
        assertTrue(map.containsKey("applicant_creditScore"));


        assertEquals(10, map.get("application_amortization"));
        assertEquals("Matteo", map.get("applicant_name"));
        assertEquals(1234567890, map.get("applicant_ssn"));
        assertEquals(70000, map.get("applicant_income"));
        assertEquals(40000, map.get("application_downPayment"));
        assertEquals("via San Giuseppe", map.get("property_address"));
        assertEquals(240000, map.get("property_price"));
        // check extra fields coming from the task data API
        assertNull(map.get("application_appraisal"));
        assertEquals(200000, map.get("application_mortgageAmount"));
        assertNull(map.get("application_validationErrors"));
        assertNull(map.get("application_apr"));
        assertNull(map.get("applicant_creditScore"));
    }


    public final static String TASK_FORM_DEF_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><form id=\"643225839\">\n" +
            "<property name=\"subject\" value=\"\"/>\n" +
            "<property name=\"name\" value=\"DataCorrection-taskform.form\"/>\n" +
            "<property name=\"displayMode\" value=\"default\"/>\n" +
            "<property name=\"status\" value=\"0\"/>\n" +
            "<field id=\"2141949774\" name=\"application_amortization\" position=\"3\" type=\"InputTextInteger\">\n" +
            "<property name=\"fieldRequired\" value=\"false\"/>\n" +
            "<property name=\"groupWithPrevious\" value=\"false\"/>\n" +
            "<property name=\"labelCSSClass\" value=\"\"/>\n" +
            "<property name=\"labelCSSStyle\" value=\"\"/>\n" +
            "<property name=\"label\" value=\"Amortization\"/>\n" +
            "<property name=\"errorMessage\" value=\"\"/>\n" +
            "<property name=\"title\" value=\"\"/>\n" +
            "<property name=\"readonly\" value=\"false\"/>\n" +
            "<property name=\"size\" value=\"\"/>\n" +
            "<property name=\"formula\" value=\"\"/>\n" +
            "<property name=\"rangeFormula\" value=\"\"/>\n" +
            "<property name=\"styleclass\" value=\"\"/>\n" +
            "<property name=\"cssStyle\" value=\"\"/>\n" +
            "<property name=\"defaultValueFormula\" value=\"\"/>\n" +
            "<property name=\"inputBinding\" value=\"10\"/>\n" +
            "<property name=\"outputBinding\" value=\"taskOutputApplication/amortization\"/>\n" +
            "<property name=\"fieldClass\" value=\"java.lang.Integer\"/>\n" +
            "</field>\n" +
            "<form id=\"895131254\">\n" +
            "<property name=\"subject\" value=\"\"/>\n" +
            "<property name=\"name\" value=\"MortgageApplicant.form\"/>\n" +
            "<property name=\"displayMode\" value=\"default\"/>\n" +
            "<property name=\"status\" value=\"0\"/>\n" +
            "<field id=\"1502946087\" name=\"applicant_name\" position=\"0\" type=\"InputText\">\n" +
            "<property name=\"fieldRequired\" value=\"false\"/>\n" +
            "<property name=\"labelCSSClass\" value=\"\"/>\n" +
            "<property name=\"labelCSSStyle\" value=\"\"/>\n" +
            "<property name=\"label\" value=\"Name\"/>\n" +
            "<property name=\"errorMessage\" value=\"\"/>\n" +
            "<property name=\"title\" value=\"\"/>\n" +
            "<property name=\"readonly\" value=\"false\"/>\n" +
            "<property name=\"size\" value=\"\"/>\n" +
            "<property name=\"formula\" value=\"\"/>\n" +
            "<property name=\"rangeFormula\" value=\"\"/>\n" +
            "<property name=\"pattern\" value=\"\"/>\n" +
            "<property name=\"styleclass\" value=\"\"/>\n" +
            "<property name=\"cssStyle\" value=\"\"/>\n" +
            "<property name=\"isHTML\" value=\"false\"/>\n" +
            "<property name=\"hideContent\" value=\"false\"/>\n" +
            "<property name=\"defaultValueFormula\" value=\"\"/>\n" +
            "<property name=\"inputBinding\" value=\"Matteo\"/>\n" +
            "<property name=\"outputBinding\" value=\"applicant/name\"/>\n" +
            "<property name=\"fieldClass\" value=\"java.lang.String\"/>\n" +
            "</field>\n" +
            "<field id=\"462770498\" name=\"applicant_ssn\" position=\"1\" type=\"InputTextInteger\">\n" +
            "<property name=\"fieldRequired\" value=\"false\"/>\n" +
            "<property name=\"labelCSSClass\" value=\"\"/>\n" +
            "<property name=\"labelCSSStyle\" value=\"\"/>\n" +
            "<property name=\"label\" value=\"Social Security Number\"/>\n" +
            "<property name=\"errorMessage\" value=\"\"/>\n" +
            "<property name=\"title\" value=\"\"/>\n" +
            "<property name=\"readonly\" value=\"false\"/>\n" +
            "<property name=\"size\" value=\"\"/>\n" +
            "<property name=\"formula\" value=\"\"/>\n" +
            "<property name=\"rangeFormula\" value=\"\"/>\n" +
            "<property name=\"styleclass\" value=\"\"/>\n" +
            "<property name=\"cssStyle\" value=\"\"/>\n" +
            "<property name=\"defaultValueFormula\" value=\"\"/>\n" +
            "<property name=\"inputBinding\" value=\"1234567890\"/>\n" +
            "<property name=\"outputBinding\" value=\"applicant/ssn\"/>\n" +
            "<property name=\"fieldClass\" value=\"java.lang.Integer\"/>\n" +
            "</field>\n" +
            "<field id=\"1926790606\" name=\"applicant_income\" position=\"2\" type=\"InputTextInteger\">\n" +
            "<property name=\"fieldRequired\" value=\"false\"/>\n" +
            "<property name=\"labelCSSClass\" value=\"\"/>\n" +
            "<property name=\"labelCSSStyle\" value=\"\"/>\n" +
            "<property name=\"label\" value=\"Annual Income\"/>\n" +
            "<property name=\"errorMessage\" value=\"\"/>\n" +
            "<property name=\"title\" value=\"\"/>\n" +
            "<property name=\"readonly\" value=\"false\"/>\n" +
            "<property name=\"size\" value=\"\"/>\n" +
            "<property name=\"formula\" value=\"\"/>\n" +
            "<property name=\"rangeFormula\" value=\"\"/>\n" +
            "<property name=\"styleclass\" value=\"\"/>\n" +
            "<property name=\"cssStyle\" value=\"\"/>\n" +
            "<property name=\"defaultValueFormula\" value=\"\"/>\n" +
            "<property name=\"inputBinding\" value=\"70000\"/>\n" +
            "<property name=\"outputBinding\" value=\"applicant/income\"/>\n" +
            "<property name=\"fieldClass\" value=\"java.lang.Integer\"/>\n" +
            "</field>\n" +
            "<dataHolder id=\"applicant\" inputId=\"applicant\" name=\"#0000A0\" outId=\"applicant\" type=\"dataModelerEntry\" value=\"com.redhat.bpms.examples.mortgage.Applicant\"/>\n" +
            "</form>\n" +
            "<field id=\"601721302\" name=\"application_downPayment\" position=\"2\" type=\"InputTextInteger\">\n" +
            "<property name=\"fieldRequired\" value=\"false\"/>\n" +
            "<property name=\"groupWithPrevious\" value=\"false\"/>\n" +
            "<property name=\"labelCSSClass\" value=\"\"/>\n" +
            "<property name=\"labelCSSStyle\" value=\"\"/>\n" +
            "<property name=\"label\" value=\"Down Payment\"/>\n" +
            "<property name=\"errorMessage\" value=\"\"/>\n" +
            "<property name=\"title\" value=\"\"/>\n" +
            "<property name=\"readonly\" value=\"false\"/>\n" +
            "<property name=\"size\" value=\"\"/>\n" +
            "<property name=\"formula\" value=\"\"/>\n" +
            "<property name=\"rangeFormula\" value=\"\"/>\n" +
            "<property name=\"styleclass\" value=\"\"/>\n" +
            "<property name=\"cssStyle\" value=\"\"/>\n" +
            "<property name=\"defaultValueFormula\" value=\"\"/>\n" +
            "<property name=\"inputBinding\" value=\"40000\"/>\n" +
            "<property name=\"outputBinding\" value=\"taskOutputApplication/downPayment\"/>\n" +
            "<property name=\"fieldClass\" value=\"java.lang.Integer\"/>\n" +
            "</field>\n" +
            "<form id=\"1458843297\">\n" +
            "<property name=\"subject\" value=\"\"/>\n" +
            "<property name=\"name\" value=\"MortgageProperty.form\"/>\n" +
            "<property name=\"displayMode\" value=\"default\"/>\n" +
            "<property name=\"status\" value=\"0\"/>\n" +
            "<field id=\"2013333214\" name=\"property_address\" position=\"0\" type=\"InputText\">\n" +
            "<property name=\"fieldRequired\" value=\"false\"/>\n" +
            "<property name=\"labelCSSClass\" value=\"\"/>\n" +
            "<property name=\"labelCSSStyle\" value=\"\"/>\n" +
            "<property name=\"label\" value=\"Address\"/>\n" +
            "<property name=\"errorMessage\" value=\"\"/>\n" +
            "<property name=\"title\" value=\"\"/>\n" +
            "<property name=\"readonly\" value=\"false\"/>\n" +
            "<property name=\"size\" value=\"\"/>\n" +
            "<property name=\"formula\" value=\"\"/>\n" +
            "<property name=\"rangeFormula\" value=\"\"/>\n" +
            "<property name=\"pattern\" value=\"\"/>\n" +
            "<property name=\"styleclass\" value=\"\"/>\n" +
            "<property name=\"cssStyle\" value=\"\"/>\n" +
            "<property name=\"isHTML\" value=\"false\"/>\n" +
            "<property name=\"hideContent\" value=\"false\"/>\n" +
            "<property name=\"defaultValueFormula\" value=\"\"/>\n" +
            "<property name=\"inputBinding\" value=\"via San Giuseppe\"/>\n" +
            "<property name=\"outputBinding\" value=\"property/address\"/>\n" +
            "<property name=\"fieldClass\" value=\"java.lang.String\"/>\n" +
            "</field>\n" +
            "<field id=\"1518985727\" name=\"property_price\" position=\"1\" type=\"InputTextInteger\">\n" +
            "<property name=\"fieldRequired\" value=\"false\"/>\n" +
            "<property name=\"labelCSSClass\" value=\"\"/>\n" +
            "<property name=\"labelCSSStyle\" value=\"\"/>\n" +
            "<property name=\"label\" value=\"Sale Price\"/>\n" +
            "<property name=\"errorMessage\" value=\"\"/>\n" +
            "<property name=\"title\" value=\"\"/>\n" +
            "<property name=\"readonly\" value=\"false\"/>\n" +
            "<property name=\"size\" value=\"\"/>\n" +
            "<property name=\"formula\" value=\"\"/>\n" +
            "<property name=\"rangeFormula\" value=\"\"/>\n" +
            "<property name=\"styleclass\" value=\"\"/>\n" +
            "<property name=\"cssStyle\" value=\"\"/>\n" +
            "<property name=\"defaultValueFormula\" value=\"\"/>\n" +
            "<property name=\"inputBinding\" value=\"240000\"/>\n" +
            "<property name=\"outputBinding\" value=\"property/price\"/>\n" +
            "<property name=\"fieldClass\" value=\"java.lang.Integer\"/>\n" +
            "</field>\n" +
            "<dataHolder id=\"property\" inputId=\"property\" name=\"#0000A0\" outId=\"property\" type=\"dataModelerEntry\" value=\"com.redhat.bpms.examples.mortgage.Property\"/>\n" +
            "</form>\n" +
            "<dataHolder id=\"application\" inputId=\"taskInputApplication\" name=\"#BBBBBB\" outId=\"taskOutputApplication\" type=\"dataModelerEntry\" value=\"com.redhat.bpms.examples.mortgage.Application\"/>\n" +
            "</form>";

    public final static String TASK_DATA_JSON = "{\n" +
            "\n" +
            "    \"task-id\":295,\n" +
            "    \"task-priority\":0,\n" +
            "    \"task-name\":\"Correct Data\",\n" +
            "    \"task-subject\":\"\",\n" +
            "    \"task-description\":\"\",\n" +
            "    \"task-form\":\"DataCorrection\",\n" +
            "    \"task-status\":\"Ready\",\n" +
            "    \"task-actual-owner\":\"\",\n" +
            "    \"task-created-by\":\"\",\n" +
            "    \"task-created-on\":1501100179987,\n" +
            "    \"task-activation-time\":1501100179987,\n" +
            "    \"task-skippable\":true,\n" +
            "    \"task-workitem-id\":340,\n" +
            "    \"task-process-instance-id\":188,\n" +
            "    \"task-parent-id\":-1,\n" +
            "    \"task-process-id\":\"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "    \"task-container-id\":\"com.redhat.bpms.examples:mortgage:1\",\n" +
            "    \"task-input-data\":{\n" +
            "        \"TaskName\":\"DataCorrection\",\n" +
            "        \"NodeName\":\"Correct Data\",\n" +
            "        \"taskInputApplication\":{\n" +
            "            \"com.redhat.bpms.examples.mortgage.Application\":{\n" +
            "                \"validationErrors\":null,\n" +
            "                \"apr\":null,\n" +
            "                \"mortgageAmount\":200000,\n" +
            "                \"applicant\":{\n" +
            "                    \"com.redhat.bpms.examples.mortgage.Applicant\":{\n" +
            "                        \"creditScore\":null,\n" +
            "                        \"income\":70000,\n" +
            "                        \"name\":\"Matteo\",\n" +
            "                        \"ssn\":1234567890\n" +
            "                    }\n" +
            "                },\n" +
            "                \"amortization\":10,\n" +
            "                \"downPayment\":40000,\n" +
            "                \"property\":{\n" +
            "                    \"com.redhat.bpms.examples.mortgage.Property\":{\n" +
            "                        \"price\":240000,\n" +
            "                        \"address\":\"via San Giuseppe\"\n" +
            "                    }\n" +
            "                },\n" +
            "                \"appraisal\":null\n" +
            "            }\n" +
            "        },\n" +
            "        \"Skippable\":\"true\",\n" +
            "        \"SwimlaneActorId\":null,\n" +
            "        \"GroupId\":\"broker\"\n" +
            "    },\n" +
            "    \"task-output-data\":{\n" +
            "    }\n" +
            "\n" +
            "}";

}
