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
public class TestKieProcesseInstance extends TestCase {


    public void testProcessInstances() throws Throwable {
        KieProcessInstancesQueryResult kipr = (KieProcessInstancesQueryResult)
                JAXBHelper.unmarshall(processInstancesJson, KieProcessInstancesQueryResult.class, false, true);
        assertNotNull(kipr);
        List<KieProcessInstance> list = kipr.getInstances();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(10, list.size());

        KieProcessInstance process = list.get(7);
        assertEquals("entandoBpmsAdmin",
                process.getInitiator());
        assertEquals((Long)47L,
                process.getInstanceId());
        assertEquals("COM.REDHAT.BPMS.EXAMPLES.MORTGAGE.MORTGAGEAPPLICATION",
                process.getId());
        assertEquals("MortgageApplicationEntando",
                process.getName());
        assertEquals("2.0",
                process.getVersion());
        assertEquals((Long)2677L,
                process.getState());
        assertEquals("com.redhat.bpms.examples:mortgage:2",
                process.getContainerId());
        assertEquals((Long)1493906044602L,
                process.getStartDate());
        assertEquals("EntandoMortgageApplication",
                process.getDesc());
        assertEquals("",
                process.getCorrelationKey());
        assertEquals(Long.valueOf(-77),
                process.getParentInstanceId());
    }

    private final static String processInstancesJson = "{\n" +
            "    \"process-instance\": [\n" +
            "        {\n" +
            "            \"initiator\": \"bpmsAdmin\",\n" +
            "            \"process-instance-id\": 18,\n" +
            "            \"process-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"process-name\": \"MortgageApplication\",\n" +
            "            \"process-version\": \"1.0\",\n" +
            "            \"process-instance-state\": 1,\n" +
            "            \"container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"start-date\": 1493634344115,\n" +
            "            \"process-instance-desc\": \"MortgageApplication\",\n" +
            "            \"correlation-key\": \"\",\n" +
            "            \"parent-instance-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"initiator\": \"bpmsAdmin\",\n" +
            "            \"process-instance-id\": 20,\n" +
            "            \"process-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"process-name\": \"MortgageApplication\",\n" +
            "            \"process-version\": \"1.0\",\n" +
            "            \"process-instance-state\": 1,\n" +
            "            \"container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"start-date\": 1493635928703,\n" +
            "            \"process-instance-desc\": \"MortgageApplication\",\n" +
            "            \"correlation-key\": \"\",\n" +
            "            \"parent-instance-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"initiator\": \"bpmsAdmin\",\n" +
            "            \"process-instance-id\": 33,\n" +
            "            \"process-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"process-name\": \"MortgageApplication\",\n" +
            "            \"process-version\": \"1.0\",\n" +
            "            \"process-instance-state\": 1,\n" +
            "            \"container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"start-date\": 1493655536580,\n" +
            "            \"process-instance-desc\": \"MortgageApplication\",\n" +
            "            \"correlation-key\": \"\",\n" +
            "            \"parent-instance-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"initiator\": \"bpmsAdmin\",\n" +
            "            \"process-instance-id\": 34,\n" +
            "            \"process-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"process-name\": \"MortgageApplication\",\n" +
            "            \"process-version\": \"1.0\",\n" +
            "            \"process-instance-state\": 1,\n" +
            "            \"container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"start-date\": 1493655659076,\n" +
            "            \"process-instance-desc\": \"MortgageApplication\",\n" +
            "            \"correlation-key\": \"\",\n" +
            "            \"parent-instance-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"initiator\": \"bpmsAdmin\",\n" +
            "            \"process-instance-id\": 36,\n" +
            "            \"process-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"process-name\": \"MortgageApplication\",\n" +
            "            \"process-version\": \"1.0\",\n" +
            "            \"process-instance-state\": 1,\n" +
            "            \"container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"start-date\": 1493712528831,\n" +
            "            \"process-instance-desc\": \"MortgageApplication\",\n" +
            "            \"correlation-key\": \"\",\n" +
            "            \"parent-instance-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"initiator\": \"bpmsAdmin\",\n" +
            "            \"process-instance-id\": 37,\n" +
            "            \"process-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"process-name\": \"MortgageApplication\",\n" +
            "            \"process-version\": \"1.0\",\n" +
            "            \"process-instance-state\": 1,\n" +
            "            \"container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"start-date\": 1493712638112,\n" +
            "            \"process-instance-desc\": \"MortgageApplication\",\n" +
            "            \"correlation-key\": \"\",\n" +
            "            \"parent-instance-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"initiator\": \"bpmsAdmin\",\n" +
            "            \"process-instance-id\": 46,\n" +
            "            \"process-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"process-name\": \"MortgageApplication\",\n" +
            "            \"process-version\": \"1.0\",\n" +
            "            \"process-instance-state\": 1,\n" +
            "            \"container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"start-date\": 1493744111614,\n" +
            "            \"process-instance-desc\": \"MortgageApplication\",\n" +
            "            \"correlation-key\": \"\",\n" +
            "            \"parent-instance-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"initiator\": \"entandoBpmsAdmin\",\n" +
            "            \"process-instance-id\": 47,\n" +
            "            \"process-id\": \"COM.REDHAT.BPMS.EXAMPLES.MORTGAGE.MORTGAGEAPPLICATION\",\n" +
            "            \"process-name\": \"MortgageApplicationEntando\",\n" +
            "            \"process-version\": \"2.0\",\n" +
            "            \"process-instance-state\": 2677,\n" +
            "            \"container-id\": \"com.redhat.bpms.examples:mortgage:2\",\n" +
            "            \"start-date\": 1493906044602,\n" +
            "            \"process-instance-desc\": \"EntandoMortgageApplication\",\n" +
            "            \"correlation-key\": \"\",\n" +
            "            \"parent-instance-id\": -77\n" +
            "        },\n" +
            "        {\n" +
            "            \"initiator\": \"bpmsAdmin\",\n" +
            "            \"process-instance-id\": 48,\n" +
            "            \"process-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"process-name\": \"MortgageApplication\",\n" +
            "            \"process-version\": \"1.0\",\n" +
            "            \"process-instance-state\": 1,\n" +
            "            \"container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"start-date\": 1494241353833,\n" +
            "            \"process-instance-desc\": \"MortgageApplication\",\n" +
            "            \"correlation-key\": \"\",\n" +
            "            \"parent-instance-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"initiator\": \"bpmsAdmin\",\n" +
            "            \"process-instance-id\": 49,\n" +
            "            \"process-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"process-name\": \"MortgageApplication\",\n" +
            "            \"process-version\": \"1.0\",\n" +
            "            \"process-instance-state\": 1,\n" +
            "            \"container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"start-date\": 1495464291032,\n" +
            "            \"process-instance-desc\": \"MortgageApplication\",\n" +
            "            \"correlation-key\": \"\",\n" +
            "            \"parent-instance-id\": -1\n" +
            "        }\n" +
            "    ]\n" +
            "}";

}
