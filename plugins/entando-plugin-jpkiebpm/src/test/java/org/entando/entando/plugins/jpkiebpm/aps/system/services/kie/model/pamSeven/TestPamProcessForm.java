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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import junit.framework.TestCase;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;

import java.util.List;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven.PamProcessQueryFormResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestPamProcessForm extends TestCase {

    private static final Logger logger = LoggerFactory.getLogger(TestPamProcessForm.class);

    public void testPAM7ProcessForm() throws Throwable {
        try {
            String pam7businessProcessForm = new String(Files.readAllBytes(Paths.get("src/test/resources/examples/xml/pam-7-test-process-form-1.xml")));

            PamProcessQueryFormResult pamResult = (PamProcessQueryFormResult) JAXBHelper
                    .unmarshall(pam7businessProcessForm, PamProcessQueryFormResult.class, true, false);
            assertNotNull(pamResult);

            List<PamFields> fields = new ArrayList<PamFields>();

            pamResult.getArrays().forEach(array -> {
                array.getPamFields().forEach(
                        field -> fields.add((PamFields) field)
                );
            });

            assertEquals(6, fields.size());
                       
            assertEquals("Employee", fields.get(0).getLabel());
            assertEquals("employee", fields.get(0).getName());
            assertEquals("employee", fields.get(0).getBinding());

            assertEquals("java.lang.String", fields.get(0).getStandaloneClassName());
            assertEquals("TextBox", fields.get(0).getCode());
            assertTrue(fields.get(0).getRequired());

            assertEquals("Reason", fields.get(1).getLabel());
            assertEquals("reason", fields.get(1).getName());
            assertEquals("reason", fields.get(1).getBinding());
            assertEquals("java.lang.String", fields.get(1).getStandaloneClassName());
            assertEquals("TextArea", fields.get(1).getCode());
            assertTrue(fields.get(1).getRequired());

            assertEquals("Performance", fields.get(2).getLabel());
            assertEquals("performance", fields.get(2).getName());
            assertEquals("performance", fields.get(2).getBinding());
            assertEquals("java.lang.Integer", fields.get(2).getStandaloneClassName());
            assertEquals("IntegerBox", fields.get(2).getCode());
            assertFalse(fields.get(2).getRequired());

            assertEquals("CheckBox1", fields.get(3).getLabel());
            assertEquals("checkBox1", fields.get(3).getName());
            assertEquals("checkBox1", fields.get(3).getBinding());
            assertEquals("java.lang.Boolean", fields.get(3).getStandaloneClassName());
            assertEquals("CheckBox", fields.get(3).getCode());
            assertFalse(fields.get(3).getRequired());

            assertEquals("ListBox", fields.get(4).getLabel());
            assertEquals("listBox1", fields.get(4).getName());
            assertEquals("listBox1", fields.get(4).getBinding());
            assertEquals("java.lang.String", fields.get(4).getStandaloneClassName());
            assertEquals("ListBox", fields.get(4).getCode());
            assertFalse(fields.get(4).getRequired());

            assertEquals("BirthDate", fields.get(5).getLabel());
            assertEquals("birthDate", fields.get(5).getName());
            assertEquals("birthDate", fields.get(5).getBinding());
            assertEquals("java.util.Date", fields.get(5).getStandaloneClassName());
            assertEquals("DatePicker", fields.get(5).getCode());
            assertFalse(fields.get(5).getRequired());
            
        } catch (Throwable t) {
            throw t;
        }
    }

}
