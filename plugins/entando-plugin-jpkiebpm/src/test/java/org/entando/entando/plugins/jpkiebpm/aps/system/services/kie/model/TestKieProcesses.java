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

import java.util.List;
import junit.framework.TestCase;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;

/**
 *
 * @author Entando
 */
public class TestKieProcesses extends TestCase {

    public void testProcesses() throws Throwable {
        KieProcessesQueryResult res = (KieProcessesQueryResult) JAXBHelper
                .unmarshall(processesJson, KieProcessesQueryResult.class, false, true);
        assertNotNull(res);
        List<kieProcess> list = res.getProcesses();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(3, list.size());
        assertEquals("com.redhat.bpms.examples.mortgage.MortgageApplicationEntando",
                list.get(2).getProcessId());
        assertEquals("MortgageApplicationEntando",
                list.get(2).getProcessName());
        assertEquals("MortgageApplicationEntando",
                list.get(2).getProcessName());
        assertEquals("26.77",
                list.get(2).getProcessVersion());
        assertEquals("com.redhat.bpms.examples.mortgage.entando",
                list.get(2).getPackageName());
        assertEquals("com.redhat.bpms.examples:mortgage:2",
                list.get(2).getContainerId());
    }

    private final static String processesJson = "{\n" +
            "  \"processes\" : [ {\n" +
            "    \"process-id\" : \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "    \"process-name\" : \"MortgageApplication\",\n" +
            "    \"process-version\" : \"1.0\",\n" +
            "    \"package\" : \"com.redhat.bpms.examples.mortgage\",\n" +
            "    \"container-id\" : \"Mortgage2\"\n" +
            "  }, {\n" +
            "    \"process-id\" : \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "    \"process-name\" : \"MortgageApplication\",\n" +
            "    \"process-version\" : \"1.0\",\n" +
            "    \"package\" : \"com.redhat.bpms.examples.mortgage\",\n" +
            "    \"container-id\" : \"MortgageDemo\"\n" +
            "  }, {\n" +
            "    \"process-id\" : \"com.redhat.bpms.examples.mortgage.MortgageApplicationEntando\",\n" +
            "    \"process-name\" : \"MortgageApplicationEntando\",\n" +
            "    \"process-version\" : \"26.77\",\n" +
            "    \"package\" : \"com.redhat.bpms.examples.mortgage.entando\",\n" +
            "    \"container-id\" : \"com.redhat.bpms.examples:mortgage:2\"\n" +
            "  } ]\n" +
            "}";

}
