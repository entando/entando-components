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

import junit.framework.TestCase;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;


/**
 *
 * @author Entando
 */
public class TestKieProcessForm extends TestCase {

    public void testKieProcessForm() throws Throwable {
        KieProcessFormQueryResult kpfq = (KieProcessFormQueryResult) JAXBHelper
                .unmarshall(kieProcessFormXML, KieProcessFormQueryResult.class, true, false);

        assertNotNull(kpfq);
        assertEquals((Long)1849151362L,
                kpfq.getId());

        KieProcessProperty property = kpfq.getProperties().get(2);
        assertEquals("default",
                property.getValue());

        // test 7th holder
        assertFalse(kpfq.getHolders().isEmpty());
        assertEquals(14, kpfq.getHolders().size());
        KieDataHolder holder = kpfq.getHolders().get(6);
        assertEquals("brokerOverride",
                holder.getId());
        assertEquals("",
                holder.getInputId());
        assertEquals("#0000A0",
                holder.getName());
        assertEquals("brokerOverride",
                holder.getOutId());
        assertEquals("basicType",
                holder.getType());
        assertEquals("java.lang.Boolean",
                holder.getValue());
        // test 2nd field
        assertNotNull(kpfq.getFields());
        assertFalse(kpfq.getFields().isEmpty());
        KieProcessFormField field = kpfq.getFields().get(1);

        assertEquals((Long)469114348L,
                field.getId());
        assertEquals("application_amortization",
                field.getName());
        assertEquals((Integer)3,
                field.getPosition());

        assertNotNull(field.getProperties());
        assertFalse(field.getProperties().isEmpty());
        assertEquals(16, field.getProperties().size());
        KieProcessProperty prop = field.getProperties().get(6);

        assertEquals("readonly",
                prop.getName());
        assertEquals("false",
                prop.getValue());

        // check the 2nd form

        assertNotNull(kpfq.getForms());
        assertEquals(2, kpfq.getForms().size());
        KieProcessFormQueryResult form = kpfq.getForms().get(1);

        assertEquals((Long)1458843297L,
                form.getId());

        assertNotNull(form.getProperties());
        assertFalse(form.getProperties().isEmpty());
        assertEquals(4,
                form.getProperties().size());
        assertEquals("default",
                form.getProperties().get(2).getValue());
        assertEquals("displayMode",
                form.getProperties().get(2).getName());

        assertEquals(2,
                form.getFields().size());
        field = form.getFields().get(1);
        assertEquals((Long)1518985727L,
                field.getId());
        assertEquals("property_price",
                field.getName());
        assertEquals("InputTextInteger",
                field.getType());
        assertEquals((Integer)1,
                field.getPosition());

        assertNotNull(field.getProperties());
        assertFalse(field.getProperties().isEmpty());
        assertEquals(16,
                field.getProperties().size());

        assertEquals("readonly",
                field.getProperties().get(6).getName());
        assertEquals("false",
                field.getProperties().get(6).getValue());

        assertNotNull(form.getHolders());
        assertFalse(form.getHolders().isEmpty());
        assertEquals(1,
                form.getHolders().size());

        assertEquals("property1",
                form.getHolders().get(0).getId());
        assertEquals("property2",
                form.getHolders().get(0).getInputId());
        assertEquals("property3",
                form.getHolders().get(0).getOutId());
        assertEquals("#0000A0",
                form.getHolders().get(0).getName());
        assertEquals("dataModelerEntry",
                form.getHolders().get(0).getType());
        assertEquals("com.redhat.bpms.examples.mortgage.Property",
                form.getHolders().get(0).getValue());
    }

    public final static String kieProcessFormXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><form id=\"1849151362\">\n" +
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

}
