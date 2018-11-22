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
import org.apache.struts2.ServletActionContext;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;

public abstract class BpmCaseInstanceActionBase extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(BpmCaseInstanceActionBase.class);

    public static final int ERROR_NULL_CONFIG = 1;
    public static final int ERROR_EMPTY_CASES = 2;

    @Autowired
    protected CaseManager caseManager;

    @Autowired
    protected KieFormManager formManager;
    private String frontEndCaseData;
    private String channel;
    private String knowledgeSourceId;
    private String containerid;
    private String casePath;
    private String channelPath;

    private int errorCode;

    public BpmCaseInstanceActionBase() {
    }

    //Constructor added for testing purpose
    public BpmCaseInstanceActionBase(final CaseManager caseManager, 
                                     final KieFormManager formManager, 
                                     final String frontEndCaseData, 
                                     final String channel, 
                                     final String knowledgeSourceId, 
                                     final String containerid, 
                                     final String casePath, 
                                     final String channelPath) {
        this.caseManager = caseManager;
        this.formManager = formManager;
        this.frontEndCaseData = frontEndCaseData;
        this.channel = channel;
        this.knowledgeSourceId = knowledgeSourceId;
        this.containerid = containerid;
        this.casePath = casePath;
        this.channelPath = channelPath;

    }
    
    //Helper methods
    public boolean isKieServerConfigurationValid() {
        logger.debug("--------------------------------------------");

        logger.debug("Check Configuration for knowledgeSourceId {}", this.getKnowledgeSourceId());
        KieBpmConfig config;
        try {
            config = formManager.getKieServerConfigurations().get(this.getKnowledgeSourceId());            
            if (null == config) {
                logger.warn("The configuration is null, return false");
                this.setErrorCode(ERROR_NULL_CONFIG);
                return false;
            }
        } catch (ApsSystemException ex) {
            logger.error("Error reading the configuration", ex);
            return false;
        }
        logger.debug("The configuration is valid, return true");
        return true;
    }

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

    public String getCasePath() {
        return casePath;
    }

    public void setCasePath(String casePath) {
        this.casePath = casePath;
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

    public KieFormManager getFormManager() {
        return formManager;
    }

    public void setFormManager(KieFormManager formManager) {
        this.formManager = formManager;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
