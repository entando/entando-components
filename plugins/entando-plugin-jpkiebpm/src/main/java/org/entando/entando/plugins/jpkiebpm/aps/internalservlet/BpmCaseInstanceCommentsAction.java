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
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author own_strong
 */
public class BpmCaseInstanceCommentsAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(BpmFormAction.class);
    private CaseManager caseManager;
    private String frontEndCaseData;
    private List<String> cases;
    private String casePath;
    private String comments;
    private String commentInput;

    public String view() {
        try {

            String frontEndCaseDataIn = extractWidgetConfig("frontEndCaseData");
            JSONObject frontEndCaseDataInjs = new JSONObject(frontEndCaseDataIn);

            String knowledgeSourceId = frontEndCaseDataInjs.getString("knowledge-source-id");
            String containerid = frontEndCaseDataInjs.getString("container-id");

            this.getCaseManager().setKieServerConfiguration(knowledgeSourceId);
            this.setCases(this.getCaseManager().getCaseInstancesList(containerid));

            if (!StringUtils.isBlank(this.getCasePath())) {
                this.setComments(this.getCaseManager().getCaseComments(containerid, this.getCasePath()).toString());
            } else {
                this.setCasePath(this.getCaseManager().getCaseInstancesList(containerid).get(0));
                this.setComments(this.getCaseManager().getCaseComments(containerid, this.getCasePath()).toString());
            }

        } catch (ApsSystemException t) {
            logger.error("Error getting the configuration parameter", t);
            return FAILURE;
        }

        return SUCCESS;
    }

    public String postComment() {
        try {
            String frontEndCaseDataIn = extractWidgetConfig("frontEndCaseData");
            JSONObject frontEndCaseDataInjs = new JSONObject(frontEndCaseDataIn);

            String knowledgeSourceId = frontEndCaseDataInjs.getString("knowledge-source-id");
            String containerid = frontEndCaseDataInjs.getString("container-id");
            
            this.getCaseManager().setKieServerConfiguration(knowledgeSourceId);
            
            boolean result = this.getCaseManager().postCaseComments(containerid, this.getCasePath(), this.getCommentInput());
            
            System.out.println("Posting "+this.getCasePath()+" - "+this.getCommentInput()+" Result: "+result);
            
//            boolean result = this.getCaseManager().postCaseComments("itorders_1.0.0-SNAPSHOT", "IT-0000000001", "test");
            
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

    public List<String> getCases() {
        return cases;
    }

    public void setCases(List<String> cases) {
        this.cases = cases;
    }

}
