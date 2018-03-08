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
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.BaseAction;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.json.JSONArray;
import org.json.JSONException;
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
    private String casePath;
    private String _caseInstanceMilestones;

    public String view() {
        try {

            String frontEndMilestonesDataIn = extractWidgetConfig("frontEndMilestonesData");
            this.setFrontEndMilestonesData(frontEndMilestonesDataIn);
            this.setCases(this.getCaseManager().getCaseInstancesList(this.getContainerIDfromfrontEndMilestonesData(frontEndMilestonesDataIn)));

        } catch (Throwable t) {
            _logger.error("Error getting the configuration parameter", t);
            return FAILURE;
        }

        return SUCCESS;
    }

    public String selectCaseInstance() {
        try {
            String containerid = this.getContainerIDfromfrontEndMilestonesData(this.getFrontEndMilestonesData());
            this.setCases(this.getCaseManager().getCaseInstancesList(containerid));

            String updatedMilestones = this.getCaseManager().getMilestonesList(containerid, this.getCasePath()).toString();
            this.setCaseInstanceMilestones(this.updatefrontEndMilestonesDataMilestones(this.getFrontEndMilestonesData(), updatedMilestones));

        } catch (Throwable t) {
            _logger.error("Error getting the configuration parameter", t);
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
                        _logger.error("Null widget config");
                    }
                } else {
                    _logger.error("Null widget");
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Error extracting param " + paramName, t);
        }
        return value;
    }

    //Helper
    protected String getContainerIDfromfrontEndMilestonesData(String frontEndMilestonesData) {
        String containerID = null;

        try {
            JSONObject frontEndMilestonesDataJSON = new JSONObject(frontEndMilestonesData);
            containerID = frontEndMilestonesDataJSON.getString("container-id");

        } catch (Throwable t) {
            _logger.error("Front end Milestones Data json can not be recognised");
        }

        return containerID;
    }

    public String updatefrontEndMilestonesDataMilestones(String currentMilestonesData, String newMilestonesData) {

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
                    iMilestone.put("milestone-achieved-at", jMilestones.getString("milestone-achieved-at"));
                    iMilestone.put("milestone-status", jMilestones.getString("milestone-status"));
                }
            }
            updatedMilestones.put(iMilestone);
        }
        currentMilestonesDataJson.put("milestones", updatedMilestones);

        return updatedMilestones.toString();
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
