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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import junit.framework.TestCase;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;

/**
 *
 * @author Entando
 */
public class TestKieTask extends TestCase {

    public void testTaskList() throws Throwable {
        KieTaskQueryResult ktqr = (KieTaskQueryResult) JAXBHelper
                .unmarshall(taskListJson, KieTaskQueryResult.class, false, true);
        assertNotNull(ktqr);
        assertNotNull(ktqr.getList());
        assertFalse(ktqr.getList().isEmpty());
        assertEquals(10, ktqr.getList().size());

        KieTask task = ktqr.getList().get(0);
        assertEquals((Long)168L,
                task.getId());
        assertEquals("Review overall Financials Individually",
                task.getName());
        assertEquals("test subject",
                task.getSubject());
        assertEquals("randomic",
                task.getDesc());
        assertEquals("Reserved",
                task.getStatus());
        assertEquals((Long)2677L,
                task.getPriority());
        assertEquals(Boolean.TRUE,
                task.getSkipable());
        assertEquals("entandoBpmsAdmin",
                task.getOwner());
        assertEquals((Long)1498741293539L,
                task.getCreated());
        assertEquals((Long)1498741293549L,
                task.getActivated());
        assertEquals((Long)34L,
                task.getProcessInstanceId());
        assertEquals("com.redhat.bpms.examples.mortgage.entando.MortgageApplication",
                task.getProcessDefinitionId());
        assertEquals("com.redhat.bpms.examples:mortgage:3",
                task.getContainerId());
        assertEquals(Long.valueOf(-81),
                task.getParentId());
    }


    private final static String taskListJson = "{\n" +
            "    \"task-summary\": [\n" +
            "        {\n" +
            "            \"task-id\": 168,\n" +
            "            \"task-name\": \"Review overall Financials Individually\",\n" +
            "            \"task-subject\": \"test subject\",\n" +
            "            \"task-description\": \"randomic\",\n" +
            "            \"task-status\": \"Reserved\",\n" +
            "            \"task-priority\": 2677,\n" +
            "            \"task-is-skipable\": true,\n" +
            "            \"task-actual-owner\": \"entandoBpmsAdmin\",\n" +
            "            \"task-created-on\": 1498741293539,\n" +
            "            \"task-activation-time\": 1498741293549,\n" +
            "            \"task-proc-inst-id\": 34,\n" +
            "            \"task-proc-def-id\": \"com.redhat.bpms.examples.mortgage.entando.MortgageApplication\",\n" +
            "            \"task-container-id\": \"com.redhat.bpms.examples:mortgage:3\",\n" +
            "            \"task-parent-id\": -81\n" +
            "        },\n" +
            "        {\n" +
            "            \"task-id\": 167,\n" +
            "            \"task-name\": \"Correct Data\",\n" +
            "            \"task-subject\": \"\",\n" +
            "            \"task-description\": \"\",\n" +
            "            \"task-status\": \"Ready\",\n" +
            "            \"task-priority\": 0,\n" +
            "            \"task-is-skipable\": true,\n" +
            "            \"task-created-on\": 1498582107113,\n" +
            "            \"task-activation-time\": 1498582107113,\n" +
            "            \"task-proc-inst-id\": 65,\n" +
            "            \"task-proc-def-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"task-container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"task-parent-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"task-id\": 166,\n" +
            "            \"task-name\": \"Correct Data\",\n" +
            "            \"task-subject\": \"\",\n" +
            "            \"task-description\": \"\",\n" +
            "            \"task-status\": \"Ready\",\n" +
            "            \"task-priority\": 0,\n" +
            "            \"task-is-skipable\": true,\n" +
            "            \"task-created-on\": 1497606055998,\n" +
            "            \"task-activation-time\": 1497606055998,\n" +
            "            \"task-proc-inst-id\": 58,\n" +
            "            \"task-proc-def-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"task-container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"task-parent-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"task-id\": 165,\n" +
            "            \"task-name\": \"Correct Data\",\n" +
            "            \"task-subject\": \"\",\n" +
            "            \"task-description\": \"\",\n" +
            "            \"task-status\": \"Reserved\",\n" +
            "            \"task-priority\": 0,\n" +
            "            \"task-is-skipable\": true,\n" +
            "            \"task-actual-owner\": \"bpmsAdmin\",\n" +
            "            \"task-created-on\": 1497283891501,\n" +
            "            \"task-activation-time\": 1497283891501,\n" +
            "            \"task-proc-inst-id\": 56,\n" +
            "            \"task-proc-def-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"task-container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"task-parent-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"task-id\": 154,\n" +
            "            \"task-name\": \"Correct Data\",\n" +
            "            \"task-subject\": \"\",\n" +
            "            \"task-description\": \"\",\n" +
            "            \"task-status\": \"Reserved\",\n" +
            "            \"task-priority\": 0,\n" +
            "            \"task-is-skipable\": true,\n" +
            "            \"task-actual-owner\": \"bpmsAdmin\",\n" +
            "            \"task-created-on\": 1495636687634,\n" +
            "            \"task-activation-time\": 1495636687634,\n" +
            "            \"task-proc-inst-id\": 52,\n" +
            "            \"task-proc-def-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"task-container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"task-parent-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"task-id\": 152,\n" +
            "            \"task-name\": \"Correct Data\",\n" +
            "            \"task-subject\": \"\",\n" +
            "            \"task-description\": \"\",\n" +
            "            \"task-status\": \"Reserved\",\n" +
            "            \"task-priority\": 0,\n" +
            "            \"task-is-skipable\": true,\n" +
            "            \"task-actual-owner\": \"bpmsAdmin\",\n" +
            "            \"task-created-on\": 1495546817688,\n" +
            "            \"task-activation-time\": 1495546817688,\n" +
            "            \"task-proc-inst-id\": 50,\n" +
            "            \"task-proc-def-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"task-container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"task-parent-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"task-id\": 150,\n" +
            "            \"task-name\": \"Correct Data\",\n" +
            "            \"task-subject\": \"\",\n" +
            "            \"task-description\": \"\",\n" +
            "            \"task-status\": \"Reserved\",\n" +
            "            \"task-priority\": 0,\n" +
            "            \"task-is-skipable\": true,\n" +
            "            \"task-actual-owner\": \"bpmsAdmin\",\n" +
            "            \"task-created-on\": 1495464329231,\n" +
            "            \"task-activation-time\": 1495464329231,\n" +
            "            \"task-proc-inst-id\": 49,\n" +
            "            \"task-proc-def-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"task-container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"task-parent-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"task-id\": 147,\n" +
            "            \"task-name\": \"Review Financials Individually\",\n" +
            "            \"task-subject\": \"\",\n" +
            "            \"task-description\": \"\",\n" +
            "            \"task-status\": \"Ready\",\n" +
            "            \"task-priority\": 0,\n" +
            "            \"task-is-skipable\": true,\n" +
            "            \"task-created-on\": 1494241386341,\n" +
            "            \"task-activation-time\": 1494241386341,\n" +
            "            \"task-proc-inst-id\": 33,\n" +
            "            \"task-proc-def-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"task-container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"task-parent-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"task-id\": 146,\n" +
            "            \"task-name\": \"Correct Data\",\n" +
            "            \"task-subject\": \"\",\n" +
            "            \"task-description\": \"\",\n" +
            "            \"task-status\": \"Ready\",\n" +
            "            \"task-priority\": 0,\n" +
            "            \"task-is-skipable\": true,\n" +
            "            \"task-created-on\": 1494241353841,\n" +
            "            \"task-activation-time\": 1494241353841,\n" +
            "            \"task-proc-inst-id\": 48,\n" +
            "            \"task-proc-def-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"task-container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"task-parent-id\": -1\n" +
            "        },\n" +
            "        {\n" +
            "            \"task-id\": 145,\n" +
            "            \"task-name\": \"Correct Data\",\n" +
            "            \"task-subject\": \"\",\n" +
            "            \"task-description\": \"\",\n" +
            "            \"task-status\": \"Reserved\",\n" +
            "            \"task-priority\": 0,\n" +
            "            \"task-is-skipable\": true,\n" +
            "            \"task-actual-owner\": \"bpmsAdmin\",\n" +
            "            \"task-created-on\": 1493914402028,\n" +
            "            \"task-activation-time\": 1493914402028,\n" +
            "            \"task-proc-inst-id\": 18,\n" +
            "            \"task-proc-def-id\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" +
            "            \"task-container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"task-parent-id\": -1\n" +
            "        }\n" +
            "    ]\n" +
            "}";

}
