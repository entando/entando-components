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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import junit.framework.TestCase;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;

/**
 *
 * @author Entando
 */
public class TestKieForm extends TestCase {


    public void testKieForm() throws Throwable {
        KieFormQueryResult kfqf = (KieFormQueryResult) JAXBHelper
                .unmarshall(formJson, KieFormQueryResult.class, false, true);
        assertNotNull(kfqf);
        assertNotNull(kfqf.getForms().isEmpty());
        KieForm kf = kfqf.getForms().get(0);
        assertNotNull(kf);
        assertEquals("default",
                kf.getDisplayMode());
        assertEquals((Long)1442332707L,
                kf.getId());
        assertEquals("FinancialReview-taskform.form",
                kf.getName());
        assertEquals((Long)0L,
                kf.getStatus());
        assertEquals("test",
                kf.getSubject());
        assertNotNull(kf.getData());
        assertFalse(kf.getData().isEmpty());
        assertNotNull(kf.getFields());
        assertFalse(kf.getFields().isEmpty());
        assertEquals(2,
                kf.getData().size());
        assertEquals(8,
                kf.getFields().size());

        KieDataHolder data = kf.getData().get(0);
        assertEquals("entando application",
                data.getId());
        assertEquals("entandoTaskInputApplication",
                data.getInputId());
        assertEquals("#0000F0",
                data.getName());
        assertEquals("unit test",
                data.getOutId());
        assertEquals("dataModelerEntry",
                data.getType());
        assertEquals("COM.REDHAT.BPMS.EXAMPLES.MORTGAGE.APPLICATION",
                data.getValue());

        KieFormField form = kf.getFields().get(7);
        assertEquals("cssStyleValue",
                form.getCssStyle());
        assertEquals("defaultValueFormulaValue",
                form.getDefaultValueFormula());
        assertEquals(Boolean.FALSE,
                form.getDisabled());
        assertEquals("error message",
                form.getErrorMessage());
        assertEquals("java.lang.Boolean",
                form.getFieldClass());
        assertEquals(Boolean.FALSE,
                form.getFieldRequired());
        assertEquals(Boolean.TRUE,
                form.getGroupWithPrevious());
        assertEquals((Long)699567020L,
                form.getId());
        assertEquals("randomBind",
                form.getInputBinding());
        assertEquals("Approve Mortgage",
                form.getLabel());
        assertEquals("cssClass",
                form.getLabelCSSClass());
        assertEquals("cssStyle",
                form.getLabelCSSStyle());
        assertEquals("brokerOverride",
                form.getName());
        assertEquals("brokerOverride",
                form.getName());
        assertEquals("brokerOverrideTaskOutput",
                form.getOutputBinding());
        assertEquals((Integer)7,
                form.getPosition());
        assertEquals(Boolean.FALSE,
                form.getReadonly());
        assertEquals("style class",
                form.getStyleclass());
        assertEquals("random title",
                form.getTitle());
        assertEquals("CheckBox",
                form.getType());
    }

    private final static String formJson = "{\n" +
            "    \"form\": {\n" +
            "        \"dataHolder\": [\n" +
            "            {\n" +
            "                \"id\": \"entando application\",\n" +
            "                \"inputId\": \"entandoTaskInputApplication\",\n" +
            "                \"name\": \"#0000F0\",\n" +
            "                \"outId\": \"unit test\",\n" +
            "                \"type\": \"dataModelerEntry\",\n" +
            "                \"value\": \"COM.REDHAT.BPMS.EXAMPLES.MORTGAGE.APPLICATION\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"id\": \"brokerOverride\",\n" +
            "                \"inputId\": \"\",\n" +
            "                \"name\": \"#0000A0\",\n" +
            "                \"outId\": \"brokerOverrideTaskOutput\",\n" +
            "                \"type\": \"basicType\",\n" +
            "                \"value\": \"java.lang.Boolean\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"displayMode\": \"default\",\n" +
            "        \"field\": [\n" +
            "            {\n" +
            "                \"cssStyle\": \"\",\n" +
            "                \"defaultValueFormula\": \"\",\n" +
            "                \"disabled\": false,\n" +
            "                \"errorMessage\": \"\",\n" +
            "                \"fieldClass\": \"java.lang.Integer\",\n" +
            "                \"fieldRequired\": false,\n" +
            "                \"formula\": \"\",\n" +
            "                \"id\": 423245560,\n" +
            "                \"inputBinding\": 240000,\n" +
            "                \"label\": \"Property Sale Price\",\n" +
            "                \"labelCSSClass\": \"\",\n" +
            "                \"labelCSSStyle\": \"\",\n" +
            "                \"name\": \"propertySalePrice\",\n" +
            "                \"outputBinding\": \"\",\n" +
            "                \"position\": 0,\n" +
            "                \"rangeFormula\": \"\",\n" +
            "                \"readonly\": true,\n" +
            "                \"size\": \"\",\n" +
            "                \"styleclass\": \"\",\n" +
            "                \"title\": \"\",\n" +
            "                \"type\": \"InputTextInteger\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"cssStyle\": \"\",\n" +
            "                \"defaultValueFormula\": \"\",\n" +
            "                \"disabled\": false,\n" +
            "                \"errorMessage\": \"\",\n" +
            "                \"fieldClass\": \"java.lang.Integer\",\n" +
            "                \"fieldRequired\": false,\n" +
            "                \"formula\": \"\",\n" +
            "                \"id\": 1790155346,\n" +
            "                \"inputBinding\": 122,\n" +
            "                \"label\": \"Appraised Value\",\n" +
            "                \"labelCSSClass\": \"\",\n" +
            "                \"labelCSSStyle\": \"\",\n" +
            "                \"name\": \"appraisedValue\",\n" +
            "                \"outputBinding\": \"\",\n" +
            "                \"position\": 1,\n" +
            "                \"rangeFormula\": \"\",\n" +
            "                \"readonly\": true,\n" +
            "                \"size\": \"\",\n" +
            "                \"styleclass\": \"\",\n" +
            "                \"title\": \"\",\n" +
            "                \"type\": \"InputTextInteger\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"cssStyle\": \"\",\n" +
            "                \"defaultValueFormula\": \"\",\n" +
            "                \"disabled\": false,\n" +
            "                \"errorMessage\": \"\",\n" +
            "                \"fieldClass\": \"java.lang.Integer\",\n" +
            "                \"fieldRequired\": false,\n" +
            "                \"formula\": \"\",\n" +
            "                \"id\": 1443012093,\n" +
            "                \"inputBinding\": 40000,\n" +
            "                \"label\": \"Down Payment\",\n" +
            "                \"labelCSSClass\": \"\",\n" +
            "                \"labelCSSStyle\": \"\",\n" +
            "                \"name\": \"downPayment\",\n" +
            "                \"outputBinding\": \"\",\n" +
            "                \"position\": 2,\n" +
            "                \"rangeFormula\": \"\",\n" +
            "                \"readonly\": true,\n" +
            "                \"size\": \"\",\n" +
            "                \"styleclass\": \"\",\n" +
            "                \"title\": \"\",\n" +
            "                \"type\": \"InputTextInteger\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"cssStyle\": \"\",\n" +
            "                \"defaultValueFormula\": \"\",\n" +
            "                \"disabled\": false,\n" +
            "                \"errorMessage\": \"\",\n" +
            "                \"fieldClass\": \"java.lang.Integer\",\n" +
            "                \"fieldRequired\": false,\n" +
            "                \"formula\": \"\",\n" +
            "                \"id\": 531971798,\n" +
            "                \"inputBinding\": 10,\n" +
            "                \"label\": \"Amortization\",\n" +
            "                \"labelCSSClass\": \"\",\n" +
            "                \"labelCSSStyle\": \"\",\n" +
            "                \"name\": \"amortization\",\n" +
            "                \"outputBinding\": \"\",\n" +
            "                \"position\": 3,\n" +
            "                \"rangeFormula\": \"\",\n" +
            "                \"readonly\": true,\n" +
            "                \"size\": \"\",\n" +
            "                \"styleclass\": \"\",\n" +
            "                \"title\": \"\",\n" +
            "                \"type\": \"InputTextInteger\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"cssStyle\": \"\",\n" +
            "                \"defaultValueFormula\": \"\",\n" +
            "                \"disabled\": false,\n" +
            "                \"errorMessage\": \"\",\n" +
            "                \"fieldClass\": \"java.lang.Float\",\n" +
            "                \"fieldRequired\": false,\n" +
            "                \"formula\": \"\",\n" +
            "                \"id\": 720516065,\n" +
            "                \"inputBinding\": 24583.3,\n" +
            "                \"label\": \"Mortgage APR\",\n" +
            "                \"labelCSSClass\": \"\",\n" +
            "                \"labelCSSStyle\": \"\",\n" +
            "                \"name\": \"apr\",\n" +
            "                \"outputBinding\": \"\",\n" +
            "                \"pattern\": \"\",\n" +
            "                \"position\": 4,\n" +
            "                \"rangeFormula\": \"\",\n" +
            "                \"readonly\": true,\n" +
            "                \"size\": \"\",\n" +
            "                \"styleclass\": \"\",\n" +
            "                \"title\": \"\",\n" +
            "                \"type\": \"InputTextFloat\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"cssStyle\": \"\",\n" +
            "                \"defaultValueFormula\": \"\",\n" +
            "                \"disabled\": false,\n" +
            "                \"errorMessage\": \"\",\n" +
            "                \"fieldClass\": \"java.lang.Integer\",\n" +
            "                \"fieldRequired\": false,\n" +
            "                \"formula\": \"\",\n" +
            "                \"id\": 1230280580,\n" +
            "                \"inputBinding\": 780,\n" +
            "                \"label\": \"Credit Score\",\n" +
            "                \"labelCSSClass\": \"\",\n" +
            "                \"labelCSSStyle\": \"\",\n" +
            "                \"name\": \"creditScore\",\n" +
            "                \"outputBinding\": \"\",\n" +
            "                \"pattern\": \"\",\n" +
            "                \"position\": 5,\n" +
            "                \"rangeFormula\": \"\",\n" +
            "                \"readonly\": true,\n" +
            "                \"size\": \"\",\n" +
            "                \"styleclass\": \"\",\n" +
            "                \"title\": \"\",\n" +
            "                \"type\": \"InputTextInteger\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"cssStyle\": \"\",\n" +
            "                \"defaultValueFormula\": \"\",\n" +
            "                \"disabled\": false,\n" +
            "                \"errorMessage\": \"\",\n" +
            "                \"fieldClass\": \"java.lang.Integer\",\n" +
            "                \"fieldRequired\": false,\n" +
            "                \"formula\": \"\",\n" +
            "                \"id\": 1522743489,\n" +
            "                \"inputBinding\": 70000,\n" +
            "                \"label\": \"Annual Income\",\n" +
            "                \"labelCSSClass\": \"\",\n" +
            "                \"labelCSSStyle\": \"\",\n" +
            "                \"name\": \"income\",\n" +
            "                \"outputBinding\": \"\",\n" +
            "                \"pattern\": \"\",\n" +
            "                \"position\": 6,\n" +
            "                \"rangeFormula\": \"\",\n" +
            "                \"readonly\": true,\n" +
            "                \"size\": \"\",\n" +
            "                \"styleclass\": \"\",\n" +
            "                \"title\": \"\",\n" +
            "                \"type\": \"InputTextInteger\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"cssStyle\": \"cssStyleValue\",\n" +
            "                \"defaultValueFormula\": \"defaultValueFormulaValue\",\n" +
            "                \"disabled\": false,\n" +
            "                \"errorMessage\": \"error message\",\n" +
            "                \"fieldClass\": \"java.lang.Boolean\",\n" +
            "                \"fieldRequired\": false,\n" +
            "                \"groupWithPrevious\": true,\n" +
            "                \"id\": 699567020,\n" +
            "                \"inputBinding\": \"randomBind\",\n" +
            "                \"label\": \"Approve Mortgage\",\n" +
            "                \"labelCSSClass\": \"cssClass\",\n" +
            "                \"labelCSSStyle\": \"cssStyle\",\n" +
            "                \"name\": \"brokerOverride\",\n" +
            "                \"outputBinding\": \"brokerOverrideTaskOutput\",\n" +
            "                \"position\": 7,\n" +
            "                \"readonly\": false,\n" +
            "                \"styleclass\": \"style class\",\n" +
            "                \"title\": \"random title\",\n" +
            "                \"type\": \"CheckBox\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"id\": 1442332707,\n" +
            "        \"name\": \"FinancialReview-taskform.form\",\n" +
            "        \"status\": 0,\n" +
            "        \"subject\": \"test\"\n" +
            "    }\n" +
            "}";
}
