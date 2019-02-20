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
import static com.agiletec.apsadmin.system.BaseAction.FAILURE;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import static org.entando.entando.plugins.jpkiebpm.aps.internalservlet.BpmCaseInstanceActionBase.ERROR_EMPTY_CASES;
import static org.entando.entando.plugins.jpkiebpm.aps.internalservlet.BpmCaseInstanceActionBase.ERROR_NULL_CONFIG;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BpmCaseInstanceCommentsAction extends BpmCaseInstanceActionBase {

    private static final Logger logger = LoggerFactory.getLogger(BpmFormAction.class);
    private String comments;
    private String commentInput;
    private String caseCommentId;
    
    private String configId;
    private String containerId ;
    private String taskId;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    

    
    private String updateInstance() {
        try {
            String frontEndCaseDataIn = extractWidgetConfig("frontEndCaseData");
            JSONObject frontEndCaseDataInjs = new JSONObject(frontEndCaseDataIn);
            String channelIn = extractWidgetConfig("channel");
            this.setChannel(channelIn);
            this.setKnowledgeSourceId(frontEndCaseDataInjs.getString("knowledge-source-id"));
            this.setContainerid(frontEndCaseDataInjs.getString("container-id"));
            this.setChannelPath(this.getChannel());
            KieBpmConfig config = this.formManager.getKieServerConfigurations().get(this.getKnowledgeSourceId());
            if (null == config) {
                logger.warn("Null KieBpmConfig - Check the configuration");
                this.setErrorCode(ERROR_NULL_CONFIG);
                return SUCCESS;
            }
            List<String> cases = this.caseManager.getCaseInstancesList(config, this.getContainerid());
            if (null == cases || cases.isEmpty()) {
                logger.warn("No instances found - Check the configuration");
                this.setErrorCode(ERROR_EMPTY_CASES);
                return SUCCESS;
            }

        } catch (ApsSystemException t) {
            logger.error("Error getting the configuration parameter", t);
            return FAILURE;
        }
        return SUCCESS;
    }
    
    
    public String view() {
        try {
            KieBpmConfig config;
            
            if (StringUtils.isNotBlank(taskId))
            {
                   
                String frontEndCaseDataIn = extractWidgetConfig("frontEndCaseData");
                JSONObject frontEndCaseDataInjs = new JSONObject(frontEndCaseDataIn);

                this.setFrontEndCaseData(frontEndCaseDataIn);
                String channelIn = extractWidgetConfig("channel");
                this.setChannel(channelIn);

                this.setKnowledgeSourceId(frontEndCaseDataInjs.getString("knowledge-source-id"));
                this.setContainerid(frontEndCaseDataInjs.getString("container-id"));
                this.setChannelPath(this.getChannel());

                config = formManager.getKieServerConfigurations().get(frontEndCaseDataInjs.getString("knowledge-source-id"));
//                List<String> cases = this.caseManager.getCaseInstancesList(config, this.getContainerid());



                //TODO This seems a bit excessive just to get the case id
                JSONObject taskData = formManager.getTaskFormData(config, this.getContainerid(), Long.valueOf(taskId), null);
                JSONObject inputData = taskData.getJSONObject("task-input-data");


                String casePath = (String) inputData.get("Exception ID");

                this.setCasePath(casePath);
         
            }   
            else{
                String frontEndCaseDataIn = extractWidgetConfig("frontEndCaseData");
                this.setFrontEndCaseData(frontEndCaseDataIn);
                String channelIn = extractWidgetConfig("channel");
                this.setChannel(channelIn);
                config = formManager.getKieServerConfigurations().get(this.getKnowledgeSourceId());            
            }
            
            
            if (null == config) {
                logger.warn("Null configuration");
                this.setErrorCode(ERROR_NULL_CONFIG);
                return SUCCESS;
            }
           
            this.setComments(this.getCaseManager().getCaseComments(config, this.getContainerid(), this.getCasePath()).toString());
                       
        } catch (ApsSystemException t) {
            logger.error("Error getting the configuration parameter", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String postComment() {
        try {
            String frontEndCaseDataIn = extractWidgetConfig("frontEndCaseData");
            this.setFrontEndCaseData(frontEndCaseDataIn);
            String channelIn = extractWidgetConfig("channel");
            this.setChannel(channelIn);

            if (this.getChannelPath().equalsIgnoreCase(this.getChannel())) {

                KieBpmConfig config = formManager.getKieServerConfigurations().get(this.getKnowledgeSourceId());
                caseManager.postCaseComments(config, this.getContainerid(), this.getCasePath(), this.getCommentInput());
                this.setComments(caseManager.getCaseComments(config, this.getContainerid(), this.getCasePath()).toString());

            }

        } catch (ApsSystemException t) {
            logger.error("Error getting the configuration parameter", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String updateComment() {
        try {
            String frontEndCaseDataIn = extractWidgetConfig("frontEndCaseData");
            this.setFrontEndCaseData(frontEndCaseDataIn);
            String channelIn = extractWidgetConfig("channel");
            this.setChannel(channelIn);

            if (this.getChannelPath().equalsIgnoreCase(this.getChannel())) {

                KieBpmConfig config = formManager.getKieServerConfigurations().get(this.getKnowledgeSourceId());
                caseManager.updateCaseComments(config, this.getContainerid(), this.getCasePath(), this.getCaseCommentId(), this.getCommentInput());
                this.setComments(caseManager.getCaseComments(config, this.getContainerid(), this.getCasePath()).toString());
            }
        } catch (ApsSystemException t) {
            logger.error("Error getting the configuration parameter", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String deleteComment() {
        try {
            String frontEndCaseDataIn = extractWidgetConfig("frontEndCaseData");
            this.setFrontEndCaseData(frontEndCaseDataIn);
            String channelIn = extractWidgetConfig("channel");
            this.setChannel(channelIn);

            if (this.getChannelPath().equalsIgnoreCase(this.getChannel())) {

                KieBpmConfig config = formManager.getKieServerConfigurations().get(this.getKnowledgeSourceId());

                caseManager.deleteCaseComments(config, this.getContainerid(), this.getCasePath(), this.getCaseCommentId());
                this.setComments(caseManager.getCaseComments(config, this.getContainerid(), this.getCasePath()).toString());
            }
        } catch (ApsSystemException t) {
            logger.error("Error getting the configuration parameter", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCommentInput() {
        return commentInput;
    }

    public void setCommentInput(String commentInput) {
        this.commentInput = commentInput;
    }

    public String getCaseCommentId() {
        return caseCommentId;
    }

    public void setCaseCommentId(String caseCommentId) {
        this.caseCommentId = caseCommentId;
    }

}
