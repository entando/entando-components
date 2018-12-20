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
package org.entando.entando.plugins.jpkiebpm.aps.internalservlet;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.CaseProgressWidgetHelpers;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.json.JSONObject;
import org.slf4j.*;

public class BpmCaseProgressAction extends BpmCaseInstanceActionBase {

    private static final Logger logger = LoggerFactory.getLogger(BpmCaseProgressAction.class);
    private String frontEndMilestonesData;
    private String caseInstanceMilestones;

    public String view() {
        try {
            if (!isKieServerConfigurationValid()) {
                return SUCCESS;
            }
            String channelIn = extractWidgetConfig("channel");
            String progressBarType= extractWidgetConfig("progressBarType");
            String showNumberOfTasks = extractWidgetConfig("showNumberOfTasks");
            String showMilestones = extractWidgetConfig("showMilestones");
            
            this.setChannel(channelIn);
            this.setChannelPath(this.getChannel());

            KieBpmConfig config = formManager.getKieServerConfigurations().get(this.getKnowledgeSourceId());
            
            if (this.getCasePath() == null) {
                this.setCasePath(this.getCaseManager().getCaseInstancesList(config, this.getContainerid()).get(0));
            }

            JSONObject milestonesList = new JSONObject();
            milestonesList.put("milestones",this.getCaseManager().getMilestonesList(config, this.getContainerid(), this.getCasePath()));            
            JSONObject generateUiJson = CaseProgressWidgetHelpers.generateUiJson(progressBarType,Boolean.valueOf(showMilestones),Boolean.valueOf(showNumberOfTasks));
            JSONObject updatedMilestonesJson = CaseProgressWidgetHelpers.updateFrontEndMilestones(milestonesList);
            JSONObject frontEndMilestonesData = new JSONObject();
            frontEndMilestonesData.put("ui",generateUiJson.get("ui"));            
            frontEndMilestonesData.put("milestones",updatedMilestonesJson.get("milestones"));            
            
            this.setFrontEndMilestonesData(frontEndMilestonesData.toString());
            this.setCaseInstanceMilestones(updatedMilestonesJson.toString());
            
        } catch (ApsSystemException t) {
            logger.error("Error getting the configuration parameter", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String getFrontEndMilestonesData() {
        return frontEndMilestonesData;
    }

    public void setFrontEndMilestonesData(String frontEndMilestonesData) {
        this.frontEndMilestonesData = frontEndMilestonesData;
    }

    public String getCaseInstanceMilestones() {
        return caseInstanceMilestones;
    }

    public void setCaseInstanceMilestones(String caseInstanceMilestones) {
        this.caseInstanceMilestones = caseInstanceMilestones;
    }
}
