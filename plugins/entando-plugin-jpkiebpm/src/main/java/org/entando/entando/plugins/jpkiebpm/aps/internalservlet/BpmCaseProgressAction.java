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
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author own_strong
 */
public class BpmCaseProgressAction extends BaseAction {

    private static final Logger _logger = LoggerFactory.getLogger(BpmFormAction.class);
    private CaseManager caseManager;
    private String _frontEndMilestonesData;
    private List<String> _cases;
    String casePath;
    String _caseInstanceMilestones;

//    final String CONTAINER_ID = "itorders_1.0.0-SNAPSHOT";
    public String view() {
        try {
            String frontEndMilestonesDataIn = extractWidgetConfig("frontEndMilestonesData");
            this.setFrontEndMilestonesData(frontEndMilestonesDataIn);
            this.setCases(this.getCaseManager().getCasesList(this.getContainerIDfromfrontEndMilestonesData(frontEndMilestonesDataIn)));

        } catch (Throwable t) {
            _logger.error("Error getting the configuration parameter", t);
            return FAILURE;
        }

        return SUCCESS;
    }

    public String selectCaseInstance() throws ApsSystemException {

        String containerid = this.getContainerIDfromfrontEndMilestonesData(this.getFrontEndMilestonesData());
        this.setCases(this.getCaseManager().getCasesList(containerid));
        this.setCaseInstanceMilestones(this.getCaseManager().getMilestonesList(containerid, this.getCasePath()).toString());

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
                        System.out.println(config);
                        value = "Empty Widget";
                    }
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Error extracting param " + paramName, t);
        }
        return value;
    }

    protected String getContainerIDfromfrontEndMilestonesData(String frontEndMilestonesData) {

        JSONObject frontEndMilestonesDataJSON = new JSONObject(frontEndMilestonesData);
        JSONArray definitions = frontEndMilestonesDataJSON.getJSONArray("definitions");
        JSONObject first_object = definitions.getJSONObject(0);
        String containerID = first_object.getString("container-id");

        return containerID;
    }

    public CaseManager getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(CaseManager caseManager) {
        this.caseManager = caseManager;
    }

    public String getFrontEndMilestonesData() {
        return _frontEndMilestonesData;
    }

    public void setFrontEndMilestonesData(String frontEndMilestonesData) {
        this._frontEndMilestonesData = frontEndMilestonesData;
    }

    public List<String> getCases() {
        return _cases;
    }

    public void setCases(List<String> _cases) {
        this._cases = _cases;
    }

    public String getCasePath() {
        return casePath;
    }

    public void setCasePath(String casePath) {
        this.casePath = casePath;
    }

    public String getCaseInstanceMilestones() {
        return _caseInstanceMilestones;
    }

    public void setCaseInstanceMilestones(String _caseInstanceMilestones) {
        this._caseInstanceMilestones = _caseInstanceMilestones;
    }

}
