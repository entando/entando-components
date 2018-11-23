/*
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
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

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieContainer;
import org.json.*;
import org.slf4j.*;

import java.time.*;
import java.util.*;

/**
 *
 * @author own_strong
 */
public class CaseProgressWidgetHelpers {

    private static final Logger logger = LoggerFactory.getLogger(BpmToFormHelper.class);

    public static String getContainerIDfromfrontEndMilestonesData(String frontEndMilestonesData) {
        String containerID = null;

        try {
            JSONObject frontEndMilestonesDataJSON = new JSONObject(frontEndMilestonesData);
            containerID = frontEndMilestonesDataJSON.getString("container-id");

        } catch (JSONException t) {
            logger.error("Front end Milestones Data json can not be recognised");
        }

        return containerID;
    }

    public static String getKieIDfromfrontEndMilestonesData(String frontEndMilestonesData) {
        String knowledgeSourceID = null;

        try {
            JSONObject frontEndMilestonesDataJSON = new JSONObject(frontEndMilestonesData);
            knowledgeSourceID = frontEndMilestonesDataJSON.getString("knowledge-source-id");

        } catch (JSONException t) {
            logger.error("Front end Milestones Data json can not be recognised");
        }

        return knowledgeSourceID;
    }

    public static String updatefrontEndMilestonesDataMilestones(String currentMilestonesData, String newMilestonesData) {

        JSONObject currentMilestonesDataJson = null;
        JSONArray newMilestonesDataJsonar = null;
        JSONArray updatedMilestones = new JSONArray();

        try {
            currentMilestonesDataJson = new JSONObject(currentMilestonesData);
        } catch (JSONException t) {
            throw new RuntimeException("Error parsing json " + currentMilestonesData, t);
        }
        try {
            newMilestonesDataJsonar = new JSONArray(newMilestonesData);
        } catch (JSONException t) {
            throw new RuntimeException("Error parsing json " + newMilestonesData, t);
        }
        JSONArray milestones = currentMilestonesDataJson.getJSONArray("milestones");

        for (int i = 0; i < milestones.length(); i++) {

            JSONObject iMilestone = milestones.getJSONObject(i);
            String iMilestoneName = iMilestone.getString("milestone-name");

            for (int j = 0; j < newMilestonesDataJsonar.length(); j++) {

                JSONObject jMilestones = newMilestonesDataJsonar.getJSONObject(j);
                String jMilestoneName = jMilestones.getString("milestone-name");

                if (jMilestoneName.equals(iMilestoneName)) {
                    iMilestone.put("milestone-name", jMilestones.getString("milestone-name"));
                    iMilestone.put("milestone-id", jMilestones.getString("milestone-id"));
                    iMilestone.put("milestone-achieved", jMilestones.getBoolean("milestone-achieved"));
                    iMilestone.put("milestone-achieved-at", jMilestones.get("milestone-achieved-at").toString());
                    iMilestone.put("milestone-status", jMilestones.getString("milestone-status"));
                }
            }
            updatedMilestones.put(iMilestone);
        }
        currentMilestonesDataJson.put("milestones", updatedMilestones);

        return updatedMilestones.toString();
    }

    public static JSONArray convertKieContainerToListToJson(List<KieContainer> kieContainerList) {

        JSONArray kieContainetListjs = new JSONArray();

        for (KieContainer kc : kieContainerList) {

            JSONObject kieContainetjs = new JSONObject();
            kieContainetjs.put("container-id", kc.getContainerId());

            kieContainetListjs.put(kieContainetjs);
        }

        return kieContainetListjs;
    }

    public static String generateNewUUID() {
        LocalDateTime currentDateTime = LocalDate.now().atTime(LocalTime.now());
//        String uuid = UUID.randomUUID().toString().replace("-", "").replace(":", "").replace(".", "");
        String uuid = UUID.randomUUID().toString() + currentDateTime.toString();
        uuid = uuid.replace("-", "").replace(":", "").replace(".", "").replace("{", "").replace("}", "");
        return uuid;
    }
    
    public static String getProcessIdFromProcessInstanceJson(JSONObject processInstanceJson) {
        String processId = processInstanceJson.getString("process-id");
        return processId;
    }
    
    public static Long getProcessInstanceIdFromProcessInstanceJson(JSONObject processInstanceJson){          
        Long processInstanceId = processInstanceJson.getLong("process-instance-id");        
        return processInstanceId;
    }
}
