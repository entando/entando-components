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

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.BaseAction;
import static com.agiletec.apsadmin.system.BaseAction.FAILURE;
import static com.opensymphony.xwork2.Action.SUCCESS;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author own_strong
 */
public class BpmCaseInstanceActionsAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(BpmCaseInstanceActionsAction.class);
    private CaseManager caseManager;
    private String frontEndCaseData;
    private String channel;
    private String caseInstanceDetails;

    private String knowledgeSourceId;
    private String casePath;
    private String containerid;
    private String channelPath;

    public String view() {
        try {
            String frontEndCaseDataIn = extractWidgetConfig("frontEndCaseData");
            this.setFrontEndCaseData(frontEndCaseDataIn);
            String channelIn = extractWidgetConfig("channel");
            this.setChannel(channelIn);

            if ((!StringUtils.isBlank(this.getKnowledgeSourceId()) || !StringUtils.isBlank(this.getContainerid()) || !StringUtils.isBlank(this.getCasePath()) || !StringUtils.isBlank(this.getChannelPath()))
                    && (this.getChannelPath().equalsIgnoreCase(this.getChannel()))) {

                this.getCaseManager().setKieServerConfiguration(this.getKnowledgeSourceId());
                this.setCaseInstanceDetails(this.getCaseManager().getCaseInstancesDetails(this.getContainerid(), this.getCasePath()).toString());

            } else {

                //set the config to the first config in database
                this.setKnowledgeSourceId(this.getCaseManager().loadFirstConfigurations().getId());
                this.setContainerid(this.getCaseManager().getContainersList().get(0).getContainerId());

                this.setCasePath(this.getCaseManager().getCaseInstancesList(this.getContainerid()).get(0));
                this.setCaseInstanceDetails(this.getCaseManager().getCaseInstancesDetails(this.getContainerid(), this.getCasePath()).toString());
                this.setChannelPath(this.getChannel());
            }

        } catch (ApsSystemException t) {
            logger.error("Error getting the configuration parameter", t);
            return FAILURE;
        }

        return SUCCESS;
    }

    //Helper classes
    protected String extractWidgetConfig(String paramName) {
        String value = null;
        try {
            HttpServletRequest request = (null != this.getRequest()) ? this.getRequest() : ServletActionContext.getRequest();
            RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
            if (null != reqCtx) {
                Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
                if (null != widget) {
                    ApsProperties config = widget.getConfig();
                    if (null != config) {
                        String widgetParam = config.getProperty(paramName);
                        if (widgetParam != null && widgetParam.trim().length() > 0) {
                            value = widgetParam.trim();
                        }
                    } else {
                        value = "Null widget config";
                        logger.error("Null widget config");
                    }
                } else {
                    logger.error("Null widget");
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Error extracting param " + paramName, t);
        }
        return value;
    }

    public CaseManager getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(CaseManager caseManager) {
        this.caseManager = caseManager;
    }

    public String getFrontEndCaseData() {
        return frontEndCaseData;
    }

    public void setFrontEndCaseData(String frontEndCaseData) {
        this.frontEndCaseData = frontEndCaseData;
    }

    public String getCasePath() {
        return casePath;
    }

    public void setCasePath(String casePath) {
        this.casePath = casePath;
    }

    public String getCaseInstanceDetails() {
        return caseInstanceDetails;
    }

    public void setCaseInstanceDetails(String caseInstanceDetails) {
        this.caseInstanceDetails = caseInstanceDetails;
    }

    public String getKnowledgeSourceId() {
        return knowledgeSourceId;
    }

    public void setKnowledgeSourceId(String knowledgeSourceId) {
        this.knowledgeSourceId = knowledgeSourceId;
    }

    public String getContainerid() {
        return containerid;
    }

    public void setContainerid(String containerid) {
        this.containerid = containerid;
    }

    public String getChannelPath() {
        return channelPath;
    }

    public void setChannelPath(String channelPath) {
        this.channelPath = channelPath;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

}
