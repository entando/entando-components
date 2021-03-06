package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.helper;

import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven.PamProcessQueryFormResult;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;

import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.DefaultValueOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.PlaceHolderOverride;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestDataUXBuilder extends TestCase {

    private final static transient Logger logger = LoggerFactory.getLogger(TestDataUXBuilder.class);

    private static final String CONTAINER_ID = "CONTAINER_ID";
    private static final String PROCESS_ID = "PROCESS_ID";
    private static final String TITLE = "Title";
    private static final String TEST_VALUE = "TEST_VALUE";
    private static final String TEST_PLACEHOLDER = "TEST_PLACEHOLDER";

    private IKieFormOverrideManager mockedFormOverrideManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mockedFormOverrideManager = mock(IKieFormOverrideManager.class);
        List<KieFormOverride> overrides = Arrays.asList(getFormOverride());
        when(mockedFormOverrideManager.getFormOverrides(any(Integer.class), any(Boolean.class))).thenReturn(overrides);
    }

    private KieFormOverride getFormOverride() {
        KieFormOverride override = new KieFormOverride();

        override.setField("employee");
        override.setActive(true);

        PlaceHolderOverride placeHolderOverride = new PlaceHolderOverride();
        placeHolderOverride.setPlaceHolder(TEST_PLACEHOLDER);
        override.addOverride(placeHolderOverride);

        DefaultValueOverride defaultValueOverride = new DefaultValueOverride();
        defaultValueOverride.setDefaultValue(TEST_VALUE);
        override.addOverride(defaultValueOverride);

        return override;
    }

    private DataUXBuilder getDataUXBuilder() throws Exception {
        DataUXBuilder dataUXBuilder = new DataUXBuilder();
        dataUXBuilder.setFormOverrideManager(mockedFormOverrideManager);
        dataUXBuilder.init();
        return dataUXBuilder;
    }

    public void testSimpleForms() throws Throwable {

        DataUXBuilder dataUXBuilder = getDataUXBuilder();

        String filePath = "src/test/resources/examples/xml/pam-7-test-process-form-1.xml";

        String pam7businessProcessForm = new String(Files.readAllBytes(Paths.get(filePath)));

        PamProcessQueryFormResult pamResult = (PamProcessQueryFormResult) JAXBHelper
                .unmarshall(pam7businessProcessForm, PamProcessQueryFormResult.class, true, false);
        assertNotNull(pamResult);
        KieProcessFormQueryResult kpfqr = KieVersionTransformer.pamSevenFormToPamSix(pamResult);
        String htmlForm = dataUXBuilder.createDataUx(kpfqr, 0, CONTAINER_ID, PROCESS_ID, TITLE);

        //Title Check
        assertTrue(htmlForm.contains("<h3 class=\"control-label editLabel\" id=\"JPKIE_TITLE_Title\">$i18n.getLabel(\"JPKIE_TITLE_Title\")</h3>\n"));

        //Hidden Fields Check
        assertTrue(htmlForm.contains("<input type=\"hidden\" id=\"processId\" name=\"processId\" class=\"ui-dform-hidden\" value=\"PROCESS_ID\""));
        assertTrue(htmlForm.contains("<input type=\"hidden\" id=\"containerId\" name=\"containerId\" class=\"ui-dform-hidden\" value=\"CONTAINER_ID\""));

        //TextBox field Checks + form override Checks
        assertTrue(htmlForm.contains(TEST_PLACEHOLDER + "</label>"));
        assertTrue(htmlForm.contains("<input type=\"text\" id=\"field_740177746345817E11\" name=\"$data.employee.type:employee\" labelkey=\"JPKIE_employee\" class=\"form-control ui-widget\" aria-required=\"true\" placeholder=\"Employee\" value=\"" + TEST_VALUE + "\">\n"));

        //TextArea field Checks
        assertTrue(htmlForm.contains("<textarea rows=\"4\" cols=\"50\" id=\"field_282038126127015E11\" name=\"$data.reason.type:reason\" labelkey=\"JPKIE_reason\" class=\"form-control ui-widget\" aria-required=\"true\" placeholder=\"Reason\" >$data.reason.text</textarea>"));

        //Number field Checks
        assertTrue(htmlForm.contains("<input type=\"number\" id=\"field_0897\" name=\"$data.performance.type:performance\" labelkey=\"JPKIE_performance\" class=\"form-control ui-widget\" aria-required=\"true\" placeholder=\"Performance\" value=\"$data.performance.number\">"));

        //DatePicker Checks

        assertTrue(htmlForm.contains("<input type=\"text\" id=\"field_8289\" name=\"$data.birthDate.type:birthDate\" labelkey=\"JPKIE_birthDate\" class=\"form-control date-picker\" aria-required=\"true\" placeholder=\"BirthDate\" value=\"$data.birthDate.getFormattedDate(\"yyyy-MM-dd_hh:mm\")\">"));
        assertTrue(htmlForm.contains("$(\"#datepicker_field_8289\").datetimepicker"));
        assertTrue(htmlForm.contains("format: 'YYYY-MM-DD hh:mm'"));
        assertTrue(htmlForm.contains("showTodayButton: true"));
        assertTrue(htmlForm.contains("allowInputToggle: true"));

        //CheckBox Checks
        assertTrue(htmlForm.contains("<input type=\"checkbox\" id=\"field_4192\" name=\"$data.checkBox1.type:checkBox1\" labelkey=\"JPKIE_checkBox1\" class=\"ui-widget \" aria-required=\"true\" value=\"true\" >"));
        assertTrue(htmlForm.contains("<input type=\"hidden\" id=\"field_4192_hiddenval\"  value=\"$data.checkBox1.getValue()\">"));
        assertTrue(htmlForm.contains("var hiddenVal = $(field_4192_hiddenval).val();"));
        assertTrue(htmlForm.contains("$(field_4192).prop('checked', true);"));
        assertTrue(htmlForm.contains("$(field_4192).prop('checked', false);"));

        //ListBox Checks
        assertTrue(htmlForm.contains("<select id=\"field_3229\" name=\"$data.listBox1.type:listBox1\" class=\"form-control\" >"));
        assertTrue(htmlForm.contains("<option></option>"));
        assertTrue(htmlForm.contains("<option value=\"val1\">Value 1</option>"));
        assertTrue(htmlForm.contains("<option selected value=\"val2\">Value 2</option>"));
        assertTrue(htmlForm.contains("<option value=\"val3\">Value 3</option>"));

        //RadioGroup Checks
        assertTrue(htmlForm.contains("<div class=\"radioNotInline\"><input type=\"radio\" id=\"field_4983\" name=\"$data.radioGroup.type:radioGroup\" class=\"ui-widget\" aria-required=\"true\" value=\"1\" > radio1</div>"));
        assertTrue(htmlForm.contains("<div class=\"radioNotInline\"><input type=\"radio\" id=\"field_4983\" name=\"$data.radioGroup.type:radioGroup\" class=\"ui-widget\" aria-required=\"true\" value=\"2\" > radio2</div>"));
        assertTrue(htmlForm.contains("<div class=\"radioNotInline\"><input type=\"radio\" id=\"field_4983\" name=\"$data.radioGroup.type:radioGroup\" class=\"ui-widget\" aria-required=\"true\" value=\"3\" checked > radio3</div>"));
        assertTrue(htmlForm.contains("<div class=\"radioInline\"><input type=\"radio\" id=\"field_4858\" name=\"$data.radioGroupInline.type:radioGroupInline\" class=\"ui-widget\" aria-required=\"true\" value=\"1inline\" checked > Radio1 Inline</div>"));
        assertTrue(htmlForm.contains("<div class=\"radioInline\"><input type=\"radio\" id=\"field_4858\" name=\"$data.radioGroupInline.type:radioGroupInline\" class=\"ui-widget\" aria-required=\"true\" value=\"2inline\" > Radio2 Inline</div>"));
        assertTrue(htmlForm.contains("<div class=\"radioInline\"><input type=\"radio\" id=\"field_4858\" name=\"$data.radioGroupInline.type:radioGroupInline\" class=\"ui-widget\" aria-required=\"true\" value=\"3inline\" > Radio3 Inline</div>"));
    }

    public void testFormWithNestedForms() throws Throwable {
        try {
            DataUXBuilder dataUXBuilder = getDataUXBuilder();

            String filePath = "src/test/resources/examples/xml/jbpm7-mortgage-process-form.xml";

            String pam7businessProcessForm = new String(Files.readAllBytes(Paths.get(filePath)));

            PamProcessQueryFormResult pamResult = (PamProcessQueryFormResult) JAXBHelper
                    .unmarshall(pam7businessProcessForm, PamProcessQueryFormResult.class, true, false);
            assertNotNull(pamResult);
            KieProcessFormQueryResult kpfqr = KieVersionTransformer.pamSevenFormToPamSix(pamResult);
            String htmlForm = dataUXBuilder.createDataUx(kpfqr, 0, CONTAINER_ID, PROCESS_ID, TITLE);

            //Title Check
            assertTrue(htmlForm.contains("<h3 class=\"control-label editLabel\" id=\"JPKIE_TITLE_Title\">$i18n.getLabel(\"JPKIE_TITLE_Title\")</h3>\n"));

            //Sections Check
            assertTrue(htmlForm.contains("$i18n.getLabel(\"JPKIE_FORM_application\")"));
            assertTrue(htmlForm.contains("$i18n.getLabel(\"JPKIE_FORM_property\")"));
            assertTrue(htmlForm.contains("$i18n.getLabel(\"JPKIE_FORM_applicant\")"));

            //Hidden Fields Check
            assertTrue(htmlForm.contains("<input type=\"hidden\" id=\"processId\" name=\"processId\" class=\"ui-dform-hidden\" value=\"PROCESS_ID\""));
            assertTrue(htmlForm.contains("<input type=\"hidden\" id=\"containerId\" name=\"containerId\" class=\"ui-dform-hidden\" value=\"CONTAINER_ID\""));

            //Fields Checks
            assertTrue(htmlForm.contains("<input type=\"number\" id=\"field_6703315525085E11\" name=\"$data.application_downPayment.type:application_downPayment\" labelkey=\"JPKIE_application_downPayment\" class=\"form-control ui-widget\" aria-required=\"true\" placeholder=\"Down Payment\" value=\"$data.application_downPayment.number\">"));

            assertTrue(htmlForm.contains("<input type=\"number\" id=\"field_5617742647838116E11\" name=\"$data.application_amortization.type:application_amortization\" labelkey=\"JPKIE_application_amortization\" class=\"form-control ui-widget\" aria-required=\"true\" placeholder=\"Mortgage Amortization\" value=\"$data.application_amortization.number\">"));
            assertTrue(htmlForm.contains("<input type=\"text\" id=\"field_2858683813813618E11\" name=\"$data.property_address.type:property_address\" labelkey=\"JPKIE_property_address\" class=\"form-control ui-widget\" aria-required=\"true\" placeholder=\"Property Address\" value=\"$data.property_address.text\">"));
            assertTrue(htmlForm.contains("<input type=\"number\" id=\"field_395082251075289E12\" name=\"$data.property_price.type:property_price\" labelkey=\"JPKIE_property_price\" class=\"form-control ui-widget\" aria-required=\"true\" placeholder=\"Sale Price\" value=\"$data.property_price.number\">"));
            assertTrue(htmlForm.contains("<input type=\"text\" id=\"field_2989496593197296E11\" name=\"$data.applicant_name.type:applicant_name\" labelkey=\"JPKIE_applicant_name\" class=\"form-control ui-widget\" aria-required=\"true\" placeholder=\"Applicant Name\" value=\"$data.applicant_name.text\">"));
            assertTrue(htmlForm.contains("<input type=\"number\" id=\"field_0861944899299075E12\" name=\"$data.applicant_ssn.type:applicant_ssn\" labelkey=\"JPKIE_applicant_ssn\" class=\"form-control ui-widget\" aria-required=\"true\" placeholder=\"Social Security Number\" value=\"$data.applicant_ssn.number\">"));
            assertTrue(htmlForm.contains("<input type=\"number\" id=\"field_3714315107900476E11\" name=\"$data.applicant_income.type:applicant_income\" labelkey=\"JPKIE_applicant_income\" class=\"form-control ui-widget\" aria-required=\"true\" placeholder=\"Annual Income\" value=\"$data.applicant_income.number\">"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void testFormsWithMultipleSubForm() throws Throwable {

        DataUXBuilder dataUXBuilder = getDataUXBuilder();

        String filePath = "src/test/resources/examples/xml/pam-7-test-process-multiple-sub-form.xml";

        String pam7businessProcessForm = new String(Files.readAllBytes(Paths.get(filePath)));

        PamProcessQueryFormResult pamResult = (PamProcessQueryFormResult) JAXBHelper
                .unmarshall(pam7businessProcessForm, PamProcessQueryFormResult.class, true, false);
        assertNotNull(pamResult);
        KieProcessFormQueryResult kpfqr = KieVersionTransformer.pamSevenFormToPamSix(pamResult);
        String htmlForm = dataUXBuilder.createDataUx(kpfqr, 0, CONTAINER_ID, PROCESS_ID, TITLE);

        System.out.println(htmlForm);
        assertTrue(htmlForm.contains("<label id=\"JPKIE_nestedForm_multiple1\" for=\"jpkieformparam_nestedForm_multiple1\" class=\"editLabel\">"));
        assertTrue(htmlForm.contains("$i18n.getLabel(\"JPKIE_nestedForm_multiple1\")"));
        assertTrue(htmlForm.contains("</label>"));
        assertTrue(htmlForm.contains("<table id=\"JPKIE_field_4156\"  class=\"display\" width=\"100%\"></table>"));
        assertTrue(htmlForm.contains("<script type=\"text/javascript\">"));
        assertTrue(htmlForm.contains("function pam2DataTable (array) {"));
        assertTrue(htmlForm.contains("if (array) {"));
        assertTrue(htmlForm.contains("return array.map(function(item) {"));
        assertTrue(htmlForm.contains("const fields = item[Object.keys(item)[0]];"));
        assertTrue(htmlForm.contains("const values = Object.values(fields);"));
        assertTrue(htmlForm.contains("return values;"));
        assertTrue(htmlForm.contains(""));
        assertTrue(htmlForm.contains("return values;"));
        assertTrue(htmlForm.contains("else return new Array();"));
        assertTrue(htmlForm.contains("var dataSet = pam2DataTable($data.nestedForm_multiple1.text);"));
        assertTrue(htmlForm.contains("var config = {"));
        assertTrue(htmlForm.contains("destroy: true,"));
        assertTrue(htmlForm.contains("responsive: true,"));
        assertTrue(htmlForm.contains("processing: true,"));
        assertTrue(htmlForm.contains("data: dataSet,"));
        assertTrue(htmlForm.contains("columns: [{\"title\":\"val2\"},{\"title\":\"val1\"}],"));
        assertTrue(htmlForm.contains("scrollX: true,"));
        assertTrue(htmlForm.contains(""));
        assertTrue(htmlForm.contains("dom: 'lfrtBip'"));
        assertTrue(htmlForm.contains("};"));
        assertTrue(htmlForm.contains("$('#JPKIE_field_4156').DataTable(config);"));
        assertTrue(htmlForm.contains("</script>"));
        assertTrue(htmlForm.contains("</fieldset>"));
        assertTrue(htmlForm.contains("<h3 class=\"control-label editLabel\" id=\"JPKIE_TITLE_Title\">$i18n.getLabel(\"JPKIE_TITLE_Title\")</h3>\n"));
    }





    public void testFormsWithMultipleColsPerRow() throws Throwable {

        DataUXBuilder dataUXBuilder = getDataUXBuilder();

        String filePath = "src/test/resources/examples/xml/pam-7-test-process-multiple-cols-form.xml";

        String pam7businessProcessForm = new String(Files.readAllBytes(Paths.get(filePath)));

        PamProcessQueryFormResult pamResult = (PamProcessQueryFormResult) JAXBHelper
                .unmarshall(pam7businessProcessForm, PamProcessQueryFormResult.class, true, false);
        assertNotNull(pamResult);
        KieProcessFormQueryResult kpfqr = KieVersionTransformer.pamSevenFormToPamSix(pamResult);
        String htmlForm = dataUXBuilder.createDataUx(kpfqr, 0, CONTAINER_ID, PROCESS_ID, TITLE);

        assertTrue(htmlForm.contains("<div class=\"col-md-6 col-sm-12\">"));
        assertTrue(htmlForm.contains("<div class=\"col-md-3 col-sm-12\">"));
        assertTrue(htmlForm.contains("<div class=\"col-md-12 col-sm-12\">"));
    }


}
