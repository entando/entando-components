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
 * @author Entando
 */
public class TestKieTaskDetail extends TestCase {

    public void testTaskDetailXml() throws Throwable {
        KieTaskDetail task = (KieTaskDetail) JAXBHelper
                .unmarshall(taskDetailXml, KieTaskDetail.class, false, false);
        assertNotNull(task);
        assertEquals((Long) 256L, task.getId());
        assertEquals("New Mortgage",
                task.getName());
        assertEquals("InProgress",
                task.getStatus());

        assertEquals("bpmsAdmin", task.getPotentialOwners().getPotentialOwners().get(0));
        assertEquals("Administrators", task.getBusinessAdmins().getBusinessAdmins().get(1));
    }

    public void testTaskDetailJson() throws Throwable {
        KieTaskDetail task = (KieTaskDetail) JAXBHelper
                .unmarshall(taskDetailJson, KieTaskDetail.class, false, true);
        assertNotNull(task);
        assertEquals((Long) 256L, task.getId());
        assertEquals("New Mortgage",
                task.getName());
        assertEquals("InProgress",
                task.getStatus());

        System.out.println(task.getPotentialOwners().getPotentialOwners().size());
        System.out.println(task.getPotentialOwners().getPotentialOwners());
    }


    private final static String taskDetailJson = "{\n" +
            "\t\"task-id\": 256,\n" +
            "\t\"task-priority\": 0,\n" +
            "\t\"task-name\": \"New Mortgage\",\n" +
            "\t\"task-form\": \"MortgageApplicant.form\",\n" +
            "\t\"task-status\": \"InProgress\",\n" +
            "\t\"task-actual-owner\": \"bpmsAdmin\",\n" +
            "\t\"task-created-by\": \"\",\n" +
            "\t\"task-created-on\": 1500403218113,\n" +
            "\t\"task-expiration-time\": 1500490800000,\n" +
            "\t\"task-skippable\": false,\n" +
            "\t\"task-workitem-id\": -1,\n" +
            "\t\"task-process-instance-id\": -1,\n" +
            "\t\"task-parent-id\": -1,\n" +
            "\t\"task-container-id\": \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "\t\"potential-owners\": [\"bpmsAdmin\"],\n" +
            "\t\"excluded-owners\": [],\n" +
            "\t\"business-admins\": [\"Administrator\", \"Administrators\"]\n" +
            "}";

    private static String taskDetailXml = "<task-instance>" +
            "<task-id>256</task-id>\n" +
            "<task-priority>0</task-priority>\n" +
            "<task-name>New Mortgage</task-name>\n" +
            "<task-form>MortgageApplicant.form</task-form>\n" +
            "<task-status>InProgress</task-status>\n" +
            "<task-actual-owner>bpmsAdmin</task-actual-owner>\n" +
            "<task-created-by/>\n" +
            "<task-created-on>2017-07-18T20:40:18.113+02:00</task-created-on>\n" +
            "<task-expiration-time>2017-07-19T21:00:00+02:00</task-expiration-time>\n" +
            "<task-skippable>false</task-skippable>\n" +
            "<task-workitem-id>-1</task-workitem-id>\n" +
            "<task-process-instance-id>-1</task-process-instance-id>\n" +
            "<task-parent-id>-1</task-parent-id>\n" +
            "<task-container-id>com.redhat.bpms.examples:mortgage:1</task-container-id>\n" +
            "<potential-owners>\n" +
            "<task-pot-owners>bpmsAdmin</task-pot-owners>\n" +
            "</potential-owners>\n" +
            "<excluded-owners/>\n" +
            "<business-admins>\n" +
            "<task-business-admins>Administrator</task-business-admins>\n" +
            "<task-business-admins>Administrators</task-business-admins>\n" +
            "</business-admins>\n" +
            "</task-instance>";
}
