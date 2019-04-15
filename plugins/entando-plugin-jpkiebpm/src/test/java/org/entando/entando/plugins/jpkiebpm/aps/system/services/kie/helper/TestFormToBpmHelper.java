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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieVersionTransformer;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.NullFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven.PamProcessQueryFormResult;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;
import org.json.JSONObject;

import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.TestBpmToFormHelper.TASK_DATA_JSON;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Entando
 */
public class TestFormToBpmHelper extends TestCase {

    private static String CONTAINER_ID = "container-id";
    private static String PROCESS_ID = "process-id";

    public void testValidationInteger() throws Throwable {
        KieProcessFormQueryResult kpfq = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(KIE_PROCESS_FORM_XML, KieProcessFormQueryResult.class, true, false);

        assertNotNull(kpfq);
        final String property_price = "400000";

        Object result = FormToBpmHelper.validateField(kpfq, "property_price", property_price);
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals((Integer) 400000,
                result);
    }

    public void testValidationNotMandatory() throws Throwable {
        KieProcessFormQueryResult kpfq = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(KIE_PROCESS_FORM_XML, KieProcessFormQueryResult.class, true, false);

        assertNotNull(kpfq);

        // property price is not mandatory
        Object result = FormToBpmHelper.validateField(kpfq, "property_price", null);
        assertNotNull(result);
        assertTrue(result instanceof NullFormField);
    }

    public void testValidationIntegerInvalid() throws Throwable {
        KieProcessFormQueryResult kpfq = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(KIE_PROCESS_FORM_XML, KieProcessFormQueryResult.class, true, false);
        Object result = null;

        assertNotNull(kpfq);
        final String property_price = "400000a";

        result = FormToBpmHelper.validateField(kpfq, "property_price", property_price);
        assertNull(result);
    }

    public void testValidationString() throws Throwable {
        KieProcessFormQueryResult kpfq = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(KIE_PROCESS_FORM_XML, KieProcessFormQueryResult.class, true, false);

        assertNotNull(kpfq);
        final String applicant_name = "400000A";

        Object result = FormToBpmHelper.validateField(kpfq, "applicant_name", applicant_name);
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("400000A",
                result);
    }

    public void testValidationStringInvalid() throws Throwable {
        KieProcessFormQueryResult kpfq = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(KIE_PROCESS_FORM_XML, KieProcessFormQueryResult.class, true, false);

        assertNotNull(kpfq);

        // applicant name is mandatory
        Object result = FormToBpmHelper.validateField(kpfq, "applicant_name", null);
        assertNull(result);
    }

    public void testValidationBoolean() throws Throwable {
        KieProcessFormQueryResult kpfq = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(KIE_PROCESS_FORM_XML, KieProcessFormQueryResult.class, true, false);

        assertNotNull(kpfq);
        final String applicant_legal_age = "tRuE";

        Object result = FormToBpmHelper.validateField(kpfq, "applicant_legal_age", applicant_legal_age);
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE,
                result);
    }

    public void testValidationBooleanInvalid() throws Throwable {
        KieProcessFormQueryResult kpfq = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(KIE_PROCESS_FORM_XML, KieProcessFormQueryResult.class, true, false);

        assertNotNull(kpfq);
        final String applicant_legal_age = "400000a";

        Object result = FormToBpmHelper.validateField(kpfq, "applicant_legal_age", applicant_legal_age);
        assertNull(result);
    }

    public void testBuildSectionWithCheckBoxFields() throws Throwable {
        String filePath1 = "src/test/resources/examples/xml/pam-7-test-process-form-checkbox.xml";
        String pam7businessProcessForm1 = new String(Files.readAllBytes(Paths.get(filePath1)));

        PamProcessQueryFormResult pamResult1 = (PamProcessQueryFormResult) JAXBHelper
                .unmarshall(pam7businessProcessForm1, PamProcessQueryFormResult.class, true, false);

        String filePath2 = "src/test/resources/examples/xml/pam-7-test-process-form-checkbox-section.xml";
        String pam7businessProcessForm2 = new String(Files.readAllBytes(Paths.get(filePath2)));

        PamProcessQueryFormResult pamResult2 = (PamProcessQueryFormResult) JAXBHelper
                .unmarshall(pam7businessProcessForm2, PamProcessQueryFormResult.class, true, false);

        assertNotNull(pamResult1);
        KieProcessFormQueryResult form1 = KieVersionTransformer.pamSevenFormToPamSix(pamResult1);
        assertNotNull(pamResult2);
        KieProcessFormQueryResult form2 = KieVersionTransformer.pamSevenFormToPamSix(pamResult2);

        Map<String, Object> input1 = createValiDPayloadForTestWithBooleanValues1();
        String formJson = FormToBpmHelper.generateFormJson(form1, input1, CONTAINER_ID, PROCESS_ID);
        assertNotNull(formJson);
        JSONObject jsonObject = new JSONObject(formJson);
        assertTrue(formJson.contains("\"checkBox1\":true"));
        assertTrue(formJson.contains("\"checkBox2\":false"));
        assertTrue(jsonObject.has("checkBox1"));
        assertEquals(true,
                jsonObject.get("checkBox1"));
        assertTrue(jsonObject.has("checkBox2"));
        assertEquals(false,
                jsonObject.get("checkBox2"));


        Map<String, Object> input2 = createValiDPayloadForTestWithBooleanValues2();
        formJson = FormToBpmHelper.generateFormJson(form2, input2, CONTAINER_ID, PROCESS_ID);
        assertNotNull(formJson);
        jsonObject = new JSONObject(formJson);
        JSONObject nestedForm = jsonObject.getJSONObject("application")
                .getJSONObject("com.myspace.test_app.Application")
                .getJSONObject("nestedForm");
        assertTrue(formJson.contains("\"checkBox1\":true"));
        assertTrue(formJson.contains("\"checkBox2\":false"));
        assertTrue(nestedForm.has("checkBox1"));
        assertEquals(true,
                nestedForm.get("checkBox1"));
        assertTrue(nestedForm.has("checkBox2"));
        assertEquals(false,
                nestedForm.get("checkBox2"));

        Map<String, Object> input3 = createValiDPayloadForTestWithBooleanValues3();
        formJson = FormToBpmHelper.generateFormJson(form1, input3, CONTAINER_ID, PROCESS_ID);
        assertNotNull(formJson);
        jsonObject = new JSONObject(formJson);
        assertTrue(formJson.contains("\"checkBox1\":true"));
        assertTrue(formJson.contains("\"checkBox2\":false"));
        assertTrue(jsonObject.has("checkBox1"));
        assertEquals(true,
                jsonObject.get("checkBox1"));
        assertTrue(jsonObject.has("checkBox2"));
        assertEquals(false,
                jsonObject.get("checkBox2"));

        Map<String, Object> input4 = createValiDPayloadForTestWithBooleanValues4();
        formJson = FormToBpmHelper.generateFormJson(form2, input4, CONTAINER_ID, PROCESS_ID);
        assertNotNull(formJson);
        jsonObject = new JSONObject(formJson);
        nestedForm = jsonObject.getJSONObject("application")
                .getJSONObject("com.myspace.test_app.Application")
                .getJSONObject("nestedForm");
        assertTrue(formJson.contains("\"checkBox1\":true"));
        assertTrue(formJson.contains("\"checkBox2\":false"));
        assertTrue(nestedForm.has("checkBox1"));
        assertEquals(true,
                nestedForm.get("checkBox1"));
        assertTrue(nestedForm.has("checkBox2"));
        assertEquals(false,
                nestedForm.get("checkBox2"));

    }

    public void testGenerateJsonPayload() throws Throwable {
        KieProcessFormQueryResult form = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(TASK_FORM_DEF_XML, KieProcessFormQueryResult.class, true, false);
        JSONObject task_data = new JSONObject(TASK_DATA_JSON);
        Map<String, Object> input = createValiDPayloadForTest();
        JSONObject json = FormToBpmHelper.generateJsonPayload(form, task_data, input);

        System.out.println(json.toString(4));


        assertTrue(json.has("taskOutputApplication"));
        JSONObject taskOutputApplication = json.getJSONObject("taskOutputApplication");

        assertTrue(taskOutputApplication.has("com.redhat.bpms.examples.mortgage.Application"));

        JSONObject application = taskOutputApplication.getJSONObject("com.redhat.bpms.examples.mortgage.Application");


        assertTrue(application.has("downPayment"));
        assertEquals(40001,
                application.get("downPayment"));
        assertTrue(application.has("amortization"));
        assertEquals(19,
                application.get("amortization"));
        // verify 'property' child
        assertTrue(application.has("property"));


        JSONObject property = application.getJSONObject("property");
        assertTrue(property instanceof JSONObject);

        assertTrue(property.has("com.redhat.bpms.examples.mortgage.Property"));
        property = property.getJSONObject("com.redhat.bpms.examples.mortgage.Property");

        assertTrue(property.has("address"));
        assertEquals("San Jose ave #2677",
                property.get("address"));
        assertTrue(property.has("price"));
        assertEquals(888888,
                property.get("price"));
        // verify 'applicant' child

        assertTrue(application.has("applicant"));
        JSONObject applicant = application.getJSONObject("applicant");

        assertTrue(applicant.has("com.redhat.bpms.examples.mortgage.Applicant"));
        applicant = applicant.getJSONObject("com.redhat.bpms.examples.mortgage.Applicant");

        assertTrue(applicant instanceof JSONObject);
        assertTrue(applicant.has("income"));
        assertEquals(65432,
                applicant.get("income"));
        assertTrue(applicant.has("name"));
        assertEquals("Matteo",
                applicant.get("name"));
        assertTrue(applicant.has("ssn"));
        assertEquals(1234567890,
                applicant.get("ssn"));
    }

    public void testGenerateJsonPayloadWithExtraData() throws Throwable {

        JSONObject task_data = new JSONObject(TASK_DATA_JSON);
        KieProcessFormQueryResult form = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(TASK_FORM_DEF_XML, KieProcessFormQueryResult.class, true, false);
        Map<String, Object> input = createValiDPayloadForTestWithExtraData();


        System.out.println(task_data.toString(4));

        System.out.println("--------------------------------------------------------");

        JSONObject json = FormToBpmHelper.generateJsonPayload(form, task_data, input);
        System.out.println(json.toString(4));


        assertTrue(json.has("taskOutputApplication"));
        JSONObject taskOutputApplication = json.getJSONObject("taskOutputApplication");

        assertTrue(taskOutputApplication.has("com.redhat.bpms.examples.mortgage.Application"));

        JSONObject application = taskOutputApplication.getJSONObject("com.redhat.bpms.examples.mortgage.Application");

        assertTrue(application.has("downPayment"));
        assertEquals(43210,
                application.get("downPayment"));
        assertTrue(application.has("amortization"));
        assertEquals(10,
                application.get("amortization"));
        assertTrue(application.has("mortgageAmount"));
        assertEquals(200000,
                application.get("mortgageAmount"));
        assertTrue(application.has("validationErrors"));
        assertEquals(JSONObject.NULL,
                application.get("validationErrors"));
        assertTrue(application.has("property"));

        JSONObject property = application.getJSONObject("property");
        assertTrue(property instanceof JSONObject);

        assertTrue(property.has("com.redhat.bpms.examples.mortgage.Property"));
        property = property.getJSONObject("com.redhat.bpms.examples.mortgage.Property");
        assertTrue(property instanceof JSONObject);
        assertTrue(property.has("address"));
        assertEquals("San Jose ave",
                property.get("address"));
        assertTrue(property.has("price"));
        assertEquals(245678,
                property.get("price"));


        // verify 'applicant' child
        assertTrue(application.has("applicant"));
        JSONObject applicant = application.getJSONObject("applicant");

        assertTrue(applicant.has("com.redhat.bpms.examples.mortgage.Applicant"));
        applicant = applicant.getJSONObject("com.redhat.bpms.examples.mortgage.Applicant");

        assertTrue(applicant instanceof JSONObject);

        assertTrue(applicant.has("income"));
        assertEquals(71234,
                applicant.get("income"));
        assertTrue(applicant.has("name"));
        assertEquals("Matteo",
                applicant.get("name"));
        assertTrue(applicant.has("ssn"));
        assertEquals(1234567890,
                applicant.get("ssn"));


        //THESE FIELDS ARE NOT PRESENT IN THE xml FORM and also they are not editable FIELDS, SHOULDN'T BE OVERRIDEN

        /*form.getFields().forEach(f ->
                System.out.println(f.getName() + "-" + f.getProperties().toString())
        );*/


        assertTrue(applicant.has("creditScore"));
        assertTrue(application.has("apr"));
        assertTrue(application.has("appraisal"));

        assertNotEquals(11,
                application.get("apr"));
        assertNotEquals(2677,
                application.get("appraisal"));

        assertNotEquals(381,
                applicant.get("creditScore"));

    }


    public void testGetHumanTaskFormData() throws Throwable {

        JSONObject task_data = new JSONObject(TASK_DATA_JSON);
        KieProcessFormQueryResult form = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(TASK_FORM_DEF_XML, KieProcessFormQueryResult.class, true, false);
        Map<String, Object> input = createValiDPayloadForTestWithExtraData();

        JSONObject payload = FormToBpmHelper.generateHumanTaskFormJson(form, task_data, input);
        assertNotNull(payload);
        //JSONObject json = new JSONObject(payload);
        assertTrue(payload.has("taskOutputApplication"));
        payload = payload.getJSONObject("taskOutputApplication");
        assertTrue(payload.has("com.redhat.bpms.examples.mortgage.Application"));
    }

    private Map<String, Object> createValiDPayloadForTest() {
        Map<String, Object> input = new HashMap<String, Object>();

        input.put("applicant_name", "Matteo");
        input.put("applicant_ssn", 1234567890);
        input.put("applicant_income", 65432);
        input.put("property_address", "San Jose ave #2677");
        input.put("property_price", 888888);
        input.put("application_downPayment", 40001);
        input.put("application_amortization", 19);

        return input;
    }

    private Map<String, Object> createValiDPayloadForTestWithExtraData() {
        Map<String, Object> input = new HashMap<String, Object>();

        input.put("applicant_name", "Matteo");
        input.put("applicant_ssn", 1234567890);
        input.put("applicant_income", 71234);
        input.put("property_address", "San Jose ave");
        input.put("property_price", 245678);
        input.put("application_downPayment", 43210);
        input.put("application_amortization", 10);

        // generate extra data
        input.put("application_appraisal", 2677);
        input.put("application_mortgageAmount", 200000);
        input.put("application_validationErrors", null);
        input.put("application_apr", 11);
        input.put("applicant_creditScore", 381);

        return input;
    }

    private Map<String, Object> createValiDPayloadForTestWithBooleanValues1() {
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("checkBox1", true);
        input.put("checkBox2", false);
        return input;
    }

    private Map<String, Object> createValiDPayloadForTestWithBooleanValues2() {
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("nestedForm_checkBox1", true);
        input.put("nestedForm_checkBox2", false);
        return input;
    }

    private Map<String, Object> createValiDPayloadForTestWithBooleanValues3() {
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("checkBox1", true);
        return input;
    }

    private Map<String, Object> createValiDPayloadForTestWithBooleanValues4() {
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("nestedForm_checkBox1", true);
        return input;
    }

    public final static String KIE_PROCESS_FORM_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><form id=\"1849151362\">\n" +
            "<property name=\"subject\" value=\"\"/>\n" +
            "<property name=\"name\" value=\"com.redhat.bpms.examples.mortgage.MortgageApplication-taskform.form\"/>\n" +
            "<property name=\"displayMode\" value=\"default\"/>\n" +
            "<property name=\"status\" value=\"0\"/>\n" +
            "<form id=\"895131254\">\n" +
            "<property name=\"subject\" value=\"\"/>\n" +
            "<property name=\"name\" value=\"MortgageApplicant.form\"/>\n" +
            "<property name=\"displayMode\" value=\"default\"/>\n" +
            "<property name=\"status\" value=\"0\"/>\n" +
            "<field id=\"1502946087\" name=\"applicant_name\" position=\"0\" type=\"InputText\">\n" +
            "<property name=\"fieldRequired\" value=\"true\"/>\n" +
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
            "<property name=\"inputBinding\" value=\"applicant/name\"/>\n" +
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
            "<property name=\"inputBinding\" value=\"applicant/ssn\"/>\n" +
            "<property name=\"outputBinding\" value=\"applicant/ssn\"/>\n" +
            "<property name=\"fieldClass\" value=\"java.lang.Integer\"/>\n" +
            "</field>\n" +

            "<field id=\"462770498\" name=\"applicant_legal_age\" position=\"1\" type=\"InputTextBoolean\">\n" +
            "<property name=\"fieldRequired\" value=\"false\"/>\n" +
            "<property name=\"labelCSSClass\" value=\"\"/>\n" +
            "<property name=\"labelCSSStyle\" value=\"\"/>\n" +
            "<property name=\"label\" value=\"Legal age\"/>\n" +
            "<property name=\"errorMessage\" value=\"\"/>\n" +
            "<property name=\"title\" value=\"\"/>\n" +
            "<property name=\"readonly\" value=\"false\"/>\n" +
            "<property name=\"size\" value=\"\"/>\n" +
            "<property name=\"formula\" value=\"\"/>\n" +
            "<property name=\"rangeFormula\" value=\"\"/>\n" +
            "<property name=\"styleclass\" value=\"\"/>\n" +
            "<property name=\"cssStyle\" value=\"\"/>\n" +
            "<property name=\"defaultValueFormula\" value=\"\"/>\n" +
            "<property name=\"inputBinding\" value=\"applicant/legalage\"/>\n" +
            "<property name=\"outputBinding\" value=\"applicant/legalage\"/>\n" +
            "<property name=\"fieldClass\" value=\"java.lang.Boolean\"/>\n" +
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
            "<property name=\"inputBinding\" value=\"applicant/income\"/>\n" +
            "<property name=\"outputBinding\" value=\"applicant/income\"/>\n" +
            "<property name=\"fieldClass\" value=\"java.lang.Integer\"/>\n" +
            "</field>\n" +
            "<dataHolder id=\"applicant\" inputId=\"applicant\" name=\"#0000A0\" outId=\"applicant\" type=\"dataModelerEntry\" value=\"com.redhat.bpms.examples.mortgage.Applicant\"/>\n" +
            "</form>\n" +
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
            "<property name=\"inputBinding\" value=\"property/address\"/>\n" +
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
            "<property name=\"inputBinding\" value=\"property/price\"/>\n" +
            "<property name=\"outputBinding\" value=\"property/price\"/>\n" +
            "<property name=\"fieldClass\" value=\"java.lang.Integer\"/>\n" +
            "</field>\n" +
            "<dataHolder id=\"property1\" inputId=\"property2\" name=\"#0000A0\" outId=\"property3\" type=\"dataModelerEntry\" value=\"com.redhat.bpms.examples.mortgage.Property\"/>\n" +
            "</form>\n" +
            "<field id=\"1601486948\" name=\"application_downPayment\" position=\"2\" type=\"InputTextInteger\">\n" +
            "<property name=\"fieldRequired\" value=\"false\"/>\n" +
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
            "<property name=\"inputBinding\" value=\"\"/>\n" +
            "<property name=\"outputBinding\" value=\"application/downPayment\"/>\n" +
            "<property name=\"fieldClass\" value=\"java.lang.Integer\"/>\n" +
            "</field>\n" +
            "<field id=\"469114348\" name=\"application_amortization\" position=\"3\" type=\"InputTextInteger\">\n" +
            "<property name=\"fieldRequired\" value=\"true\"/>\n" +
            "<property name=\"labelCSSClass\" value=\"cssClass\"/>\n" +
            "<property name=\"labelCSSStyle\" value=\"cssStyle\"/>\n" +
            "<property name=\"label\" value=\"Amortization\"/>\n" +
            "<property name=\"errorMessage\" value=\"none\"/>\n" +
            "<property name=\"title\" value=\"\"/>\n" +
            "<property name=\"readonly\" value=\"false\"/>\n" +
            "<property name=\"size\" value=\"\"/>\n" +
            "<property name=\"formula\" value=\"formula\"/>\n" +
            "<property name=\"rangeFormula\" value=\"ranformula\"/>\n" +
            "<property name=\"styleclass\" value=\"\"/>\n" +
            "<property name=\"cssStyle\" value=\"cssStyleValue\"/>\n" +
            "<property name=\"defaultValueFormula\" value=\"defaultValueFormulaValue\"/>\n" +
            "<property name=\"inputBinding\" value=\"binding\"/>\n" +
            "<property name=\"outputBinding\" value=\"application/amortization\"/>\n" +
            "<property name=\"fieldClass\" value=\"java.lang.Integer\"/>\n" +
            "</field>\n" +
            "<dataHolder id=\"address\" inputId=\"\" name=\"#B29FE4\" outId=\"address\" type=\"basicType\" value=\"java.lang.String\"/>\n" +
            "<dataHolder id=\"amortization\" inputId=\"\" name=\"#FBB767\" outId=\"amortization\" type=\"basicType\" value=\"java.lang.Integer\"/>\n" +
            "<dataHolder id=\"application\" inputId=\"\" name=\"#BBBBBB\" outId=\"application\" type=\"dataModelerEntry\" value=\"com.redhat.bpms.examples.mortgage.Application\"/>\n" +
            "<dataHolder id=\"appraisalValue\" inputId=\"\" name=\"#A7E690\" outId=\"appraisalValue\" type=\"basicType\" value=\"java.lang.Integer\"/>\n" +
            "<dataHolder id=\"apr\" inputId=\"\" name=\"#B29FE4\" outId=\"apr\" type=\"basicType\" value=\"java.lang.Float\"/>\n" +
            "<dataHolder id=\"borrowerQualified\" inputId=\"\" name=\"#E9E371\" outId=\"borrowerQualified\" type=\"basicType\" value=\"java.lang.Boolean\"/>\n" +
            "<dataHolder id=\"brokerOverride\" inputId=\"\" name=\"#0000A0\" outId=\"brokerOverride\" type=\"basicType\" value=\"java.lang.Boolean\"/>\n" +
            "<dataHolder id=\"creditScore\" inputId=\"\" name=\"#FF54A7\" outId=\"creditScore\" type=\"basicType\" value=\"java.lang.Integer\"/>\n" +
            "<dataHolder id=\"downPayment\" inputId=\"\" name=\"#FF8881\" outId=\"downPayment\" type=\"basicType\" value=\"java.lang.Integer\"/>\n" +
            "<dataHolder id=\"financialReview\" inputId=\"\" name=\"#000000\" outId=\"financialReview\" type=\"basicType\" value=\"java.lang.Boolean\"/>\n" +
            "<dataHolder id=\"income\" inputId=\"\" name=\"#A7E690\" outId=\"income\" type=\"basicType\" value=\"java.lang.Integer\"/>\n" +
            "<dataHolder id=\"name\" inputId=\"\" name=\"#0000A0\" outId=\"name\" type=\"basicType\" value=\"java.lang.String\"/>\n" +
            "<dataHolder id=\"price\" inputId=\"\" name=\"#9BCAFA\" outId=\"price\" type=\"basicType\" value=\"java.lang.Integer\"/>\n" +
            "<dataHolder id=\"ssn\" inputId=\"\" name=\"#FF54A7\" outId=\"ssn\" type=\"basicType\" value=\"java.lang.Integer\"/>\n" +
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

}
