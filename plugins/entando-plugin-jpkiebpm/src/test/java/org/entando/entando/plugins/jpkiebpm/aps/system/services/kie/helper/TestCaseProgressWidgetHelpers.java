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

import junit.framework.TestCase;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieContainer;
import org.json.*;

import java.nio.file.*;
import java.util.*;

public class TestCaseProgressWidgetHelpers extends TestCase {

    public void testUiJsonGenerator() throws Throwable {
        JSONObject generateUiJson1 = CaseProgressWidgetHelpers.generateUiJson("stacked", true, true);
        JSONObject ui = generateUiJson1.getJSONObject("ui");
        JSONArray additionalSettings = ui.getJSONArray("additionalSettings");

        assertNotNull(ui);
        assertEquals("stacked", ui.get("progress-bar-type"));
        assertEquals(2, additionalSettings.length());
        assertEquals("show-milestones", additionalSettings.get(0));
        assertEquals("show-number-of-tasks", additionalSettings.get(1));

        JSONObject generateUiJson2 = CaseProgressWidgetHelpers.generateUiJson("stacked", false, true);
        ui = generateUiJson2.getJSONObject("ui");
        additionalSettings = ui.getJSONArray("additionalSettings");

        assertNotNull(ui);
        assertEquals("stacked", ui.get("progress-bar-type"));
        assertEquals(1, additionalSettings.length());
        assertEquals("show-number-of-tasks", additionalSettings.get(0));

        JSONObject generateUiJson3 = CaseProgressWidgetHelpers.generateUiJson("basic", true, false);
        ui = generateUiJson3.getJSONObject("ui");
        additionalSettings = ui.getJSONArray("additionalSettings");

        assertNotNull(ui);
        assertEquals("basic", ui.get("progress-bar-type"));
        assertEquals(1, additionalSettings.length());
        assertEquals("show-milestones", additionalSettings.get(0));

    }

    public void testUpdateFrontEndMilestones() throws Throwable {
        String filePath = "src/test/resources/examples/json/milestones.json";
        String milestonesString = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject milestones = new JSONObject(milestonesString);

        JSONObject milestonesUpdated = CaseProgressWidgetHelpers.updateFrontEndMilestones(milestones);

        assertNotNull(milestonesUpdated);
        JSONArray milestonesArray = milestonesUpdated.getJSONArray("milestones");
        assertEquals(3, milestonesArray.length());

        JSONObject milestone1 = milestonesArray.getJSONObject(0);
        JSONObject milestone2 = milestonesArray.getJSONObject(1);
        JSONObject milestone3 = milestonesArray.getJSONObject(2);

        assertEquals(true, milestone1.get("visible"));
        assertEquals("4", milestone1.get("milestone-id"));
        assertEquals("Milestone 2", milestone1.get("milestone-name"));
        assertEquals(33, milestone1.get("percentage"));
        assertEquals("Completed", milestone1.get("milestone-status"));
        assertEquals(1541751636922L, milestone1.get("milestone-achieved-at"));
        assertEquals(true, milestone1.get("milestone-achieved"));

        System.out.println(milestone2);
        assertEquals(true, milestone2.get("visible"));
        assertEquals("_5DF9B265-4140-42B1-B34B-2A85ED6DBFBF", milestone2.get("milestone-id"));
        assertEquals("Milestone 1", milestone2.get("milestone-name"));
        assertEquals(33, milestone2.get("percentage"));
        assertEquals("Available", milestone2.get("milestone-status"));
        assertTrue( milestone2.isNull("milestone-achieved-at"));
        assertEquals(false, milestone2.get("milestone-achieved"));
        assertEquals(true, milestone2.get("visible"));

        assertEquals("_AA3A4A87-9512-40CD-A573-D2913D67FBA4", milestone3.get("milestone-id"));
        assertEquals("Milestone 3", milestone3.get("milestone-name"));
        assertEquals(34, milestone3.get("percentage"));
        assertEquals("Available", milestone3.get("milestone-status"));
        assertTrue(milestone3.isNull("milestone-achieved-at"));
        assertEquals(false, milestone3.get("milestone-achieved"));

    }
    public void testUpdateFrontEndMilestonesNullCheck() throws Throwable {


        JSONObject milestonesUpdated = CaseProgressWidgetHelpers.updateFrontEndMilestones(null);
        assertNotNull(milestonesUpdated);
        milestonesUpdated = CaseProgressWidgetHelpers.updateFrontEndMilestones(new JSONObject());
        assertNotNull(milestonesUpdated);
    }
        public void testGenerateNewUUID() throws Throwable {
        String uuid = CaseProgressWidgetHelpers.generateNewUUID();
        assertNotNull(uuid);
    }

    public void testConvertKieContainerToListToJson() throws Throwable {
        KieContainer kieContainer1 = new KieContainer();
        KieContainer kieContainer2 = new KieContainer();
        kieContainer1.setContainerId("container1");
        kieContainer2.setContainerId("container2");
        List<KieContainer> kieContainerList = new ArrayList<>();
        kieContainerList.add(kieContainer1);
        kieContainerList.add(kieContainer2);
        JSONArray kieContainerArray = CaseProgressWidgetHelpers.convertKieContainerToListToJson(kieContainerList);
        assertNotNull(kieContainerArray);
        assertEquals(kieContainerArray.length(), 2);
        JSONObject container1 = kieContainerArray.getJSONObject(0);
        JSONObject container2 = kieContainerArray.getJSONObject(1);
        assertEquals("container1", container1.get("container-id"));
        assertEquals("container2", container2.get("container-id"));

    }

    public void testGetProcessIdFromProcessInstanceJson() throws Throwable {
        JSONObject processInstanceJson = new JSONObject();
        processInstanceJson.put("process-id", "test-process-id");
        String processIdFromProcessInstanceJson = CaseProgressWidgetHelpers.getProcessIdFromProcessInstanceJson(processInstanceJson);
        assertEquals("test-process-id", processIdFromProcessInstanceJson);
    }

    public void testGetProcessInstanceIdFromProcessInstanceJson() throws Throwable {
        JSONObject processInstanceJson = new JSONObject();
        processInstanceJson.put("process-instance-id", 1541751636922L);
        Long processIdFromProcessInstanceJson = CaseProgressWidgetHelpers.getProcessInstanceIdFromProcessInstanceJson(processInstanceJson);
        assertEquals(Long.valueOf(1541751636922L), Long.valueOf(processIdFromProcessInstanceJson));

    }
}