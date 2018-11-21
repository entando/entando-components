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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieContainer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaseProgressWidgetHelpers {

    private static final Logger logger = LoggerFactory.getLogger(CaseProgressWidgetHelpers.class);

    public static JSONObject generateUiJson(final String progressBarType, final boolean showMilestones, final boolean showNumberOfTasks) {
        logger.debug("generated ui progressBarType:  {}", progressBarType);
        logger.debug("generated ui showMilestones:  {}", showMilestones);
        logger.debug("generated ui showNumberOfTasks:  {}", showNumberOfTasks);

        JSONObject result = new JSONObject();

        JSONObject ui = new JSONObject();
        ui.put("progress-bar-type", progressBarType);

        JSONArray additionalSettings = new JSONArray();
        if (showMilestones){
            additionalSettings.put("show-milestones");
        }
        if (showNumberOfTasks) {
            additionalSettings.put("show-number-of-tasks");
        }
        ui.put("additionalSettings", additionalSettings);
        result.put("ui", ui);
        logger.debug("return ui generated json: {}",result.toString() );
        return result;
    }

    public static JSONObject updateFrontEndMilestones(JSONObject currentMilestonesDataJson) {
        JSONObject result = new JSONObject();
        JSONArray updatedMilestones = new JSONArray();
        JSONArray milestones = null;
        if (null!=currentMilestonesDataJson){
            if (!(currentMilestonesDataJson.isNull("milestones"))) {
                logger.debug("currentMilestonesData: ", currentMilestonesDataJson.toString());
                milestones = currentMilestonesDataJson.getJSONArray("milestones");
            }
        }
        if (null != milestones) {
            int milestonesLength = milestones.length();
            int percentage = Math.round(100 / milestonesLength);
            int lastItemPercentage  = Math.round(100 - (percentage * (milestonesLength-1)));
            for (int i = 0; i < milestones.length(); i++) {
                JSONObject iMilestone = milestones.getJSONObject(i);
                iMilestone.put("visible", true);
                if (i == milestones.length()-1) {
                    iMilestone.put("percentage", lastItemPercentage);
                } else {
                    iMilestone.put("percentage", percentage);
                }
                updatedMilestones.put(iMilestone);
            }
            currentMilestonesDataJson.put("milestones", updatedMilestones);
            result.put("milestones",updatedMilestones);
        }
        return result;
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
        String uuid = UUID.randomUUID().toString() + currentDateTime.toString();
        uuid = uuid.replace("-", "").replace(":", "").replace(".", "").replace("{", "").replace("}", "");
        return uuid;
    }

    public static String getProcessIdFromProcessInstanceJson(JSONObject processInstanceJson) {
        String processId = processInstanceJson.getString("process-id");
        return processId;
    }

    public static Long getProcessInstanceIdFromProcessInstanceJson(JSONObject processInstanceJson) {
        Long processInstanceId = processInstanceJson.getLong("process-instance-id");
        return processInstanceId;
    }
}
