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
package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.CaseProgressWidgetHelpers.convertKieContainerToListToJson;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieContainer;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 *
 * @author own_strong
 */
public class BpmBpmCaseInstanceSelectorWidgetAction extends SimpleWidgetConfigAction {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BpmDatatableWidgetAction.class);

    private CaseManager caseManager;
    private IKieFormManager formManager;
    private IKieFormOverrideManager kieFormOverrideManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;

    private HashMap<String, KieBpmConfig> knowledgeSource;
    private String knowledgeSourcePath;
    private List<KieContainer> process;
    private String processPath;

    private String knowledgeSourceJson;
    private String kieContainerListJson;

    private String frontEndCaseData;

    @Override
    public String init() {
        String result = super.init();
        this.extractInitConfig();

        return result;
    }

    @Override
    public String save() {

        String result = super.save();
        Widget widget = this.createNewWidget();

        try {
            if (widget != null) {

                JSONObject frontEndCaseDatajs = new JSONObject();
                frontEndCaseDatajs.put("knowledge-source-id", this.getKnowledgeSourcePath());
                frontEndCaseDatajs.put("container-id", this.getProcessPath());
                this.setFrontEndCaseData(frontEndCaseDatajs.toString());

                widget.getConfig().setProperty("frontEndCaseData", this.getFrontEndCaseData());
                System.out.println(" widget.getConfig() at save " + widget.getConfig());

            } else {
                logger.error("Null widget");
                // return INPUT;
            }
        } catch (Throwable t) {
            logger.error("error in save ", t);
            return FAILURE;
        }
        return result;
    }

    public String chooseKnowledgeSourceForm() {
        try {
            this.getCaseManager().setKieServerConfiguration(this.getKnowledgeSourcePath());
            this.setKnowledgeSource(this.getCaseManager().getKieServerConfigurations());
            this.setProcess(this.getCaseManager().getContainersList());

            this.setKnowledgeSourceJson(this.getCaseManager().getKieServerStasus().toString());
            this.setKieContainerListJson(convertKieContainerToListToJson(this.getCaseManager().getContainersList()).toString());

        } catch (ApsSystemException t) {
            logger.error("Error in chooseKnowledgeSourceForm()", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String changeKnowledgeSourceForm() {
        try {
            this.getCaseManager().setKieServerConfiguration(this.getKnowledgeSourcePath());
            this.setKnowledgeSource(this.getCaseManager().getKieServerConfigurations());
            this.setProcess(this.getCaseManager().getContainersList());

            this.setKnowledgeSourceJson(this.getCaseManager().getKieServerStasus().toString());
            this.setKieContainerListJson(convertKieContainerToListToJson(this.getCaseManager().getContainersList()).toString());

        } catch (ApsSystemException t) {
            logger.error("Error in chooseKnowledgeSourceForm()", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String chooseForm() {
        try {
            this.getCaseManager().setKieServerConfiguration(this.getKnowledgeSourcePath());
            this.setKnowledgeSource(this.getCaseManager().getKieServerConfigurations());
            this.setProcess(this.getCaseManager().getContainersList());

            this.setKnowledgeSourceJson(this.getCaseManager().getKieServerStasus().toString());
            this.setKieContainerListJson(convertKieContainerToListToJson(this.getCaseManager().getContainersList()).toString());

        } catch (ApsSystemException t) {
            logger.error("Error in chooseForm()", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String changeForm() {
        try {
            this.getCaseManager().setKieServerConfiguration(this.getKnowledgeSourcePath());
            this.setKnowledgeSource(this.getCaseManager().getKieServerConfigurations());
            this.setProcess(this.getCaseManager().getContainersList());

            this.setKnowledgeSourceJson(this.getCaseManager().getKieServerStasus().toString());
            this.setKieContainerListJson(convertKieContainerToListToJson(this.getCaseManager().getContainersList()).toString());

        } catch (ApsSystemException t) {
            logger.error("Error in chooseForm()", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    //Helper
    @Override
    protected String extractInitConfig() {
        String result = super.extractInitConfig();

        try {

            Widget widget = this.getWidget();
            String frontEndCaseDatain;

            if (widget != null) {

                System.out.println(" widget.getConfig() at extract " + widget.getConfig());
                frontEndCaseDatain = widget.getConfig().getProperty("frontEndCaseData");

                if (StringUtils.isNotBlank(frontEndCaseDatain)) {

                    this.setFrontEndCaseData(frontEndCaseDatain);
                    JSONObject frontEndCaseDatajs = new JSONObject(frontEndCaseDatain);

                    this.getCaseManager().setKieServerConfiguration(frontEndCaseDatajs.getString("knowledge-source-id"));

                    this.setKnowledgeSource(this.getCaseManager().getKieServerConfigurations());
                    this.setKnowledgeSourceJson(this.getCaseManager().getKieServerStasus().toString());
                    //Add if conf exist
//                    this.getCaseManager().setKieServerConfiguration(getKieIDfromfrontEndMilestonesData(frontEndCaseDatain));

//                    this.setKnowledgeSource(this.getCaseManager().getKieServerConfigurations());
//                    this.setKnowledgeSourceJson(this.getCaseManager().getKieServerStasus().toString());
//                    this.setKnowledgeSourcePath(getKieIDfromfrontEndMilestonesData(frontEndCaseDatain));
//                    this.setProcess(this.getCaseManager().getContainersList());
//                    this.setKieContainerListJson(convertKieContainerToListToJson(this.getCaseManager().getContainersList()).toString());
//                    //Add if Container exist
//                    this.setProcessPath(getContainerIDfromfrontEndMilestonesData(frontEndCaseDatain));
                } else {
                    this.setKnowledgeSource(this.getCaseManager().getKieServerConfigurations());
                    this.setKnowledgeSourceJson(this.getCaseManager().getKieServerStasus().toString());
                }
            }
        } catch (ApsSystemException ex) {
            Logger.getLogger(BpmCaseProgressWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public CaseManager getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(CaseManager caseManager) {
        this.caseManager = caseManager;
    }

    public IKieFormManager getFormManager() {
        return formManager;
    }

    public void setFormManager(IKieFormManager formManager) {
        this.formManager = formManager;
    }

    public IKieFormOverrideManager getKieFormOverrideManager() {
        return kieFormOverrideManager;
    }

    public void setKieFormOverrideManager(IKieFormOverrideManager kieFormOverrideManager) {
        this.kieFormOverrideManager = kieFormOverrideManager;
    }

    public IBpmWidgetInfoManager getBpmWidgetInfoManager() {
        return bpmWidgetInfoManager;
    }

    public void setBpmWidgetInfoManager(IBpmWidgetInfoManager bpmWidgetInfoManager) {
        this.bpmWidgetInfoManager = bpmWidgetInfoManager;
    }

    public HashMap<String, KieBpmConfig> getKnowledgeSource() {
        return knowledgeSource;
    }

    public void setKnowledgeSource(HashMap<String, KieBpmConfig> knowledgeSource) {
        this.knowledgeSource = knowledgeSource;
    }

    public String getKnowledgeSourcePath() {
        return knowledgeSourcePath;
    }

    public void setKnowledgeSourcePath(String knowledgeSourcePath) {
        this.knowledgeSourcePath = knowledgeSourcePath;
    }

    public List<KieContainer> getProcess() {
        return process;
    }

    public void setProcess(List<KieContainer> process) {
        this.process = process;
    }

    public String getProcessPath() {
        return processPath;
    }

    public void setProcessPath(String processPath) {
        this.processPath = processPath;
    }

    public String getKnowledgeSourceJson() {
        return knowledgeSourceJson;
    }

    public void setKnowledgeSourceJson(String knowledgeSourceJson) {
        this.knowledgeSourceJson = knowledgeSourceJson;
    }

    public String getKieContainerListJson() {
        return kieContainerListJson;
    }

    public void setKieContainerListJson(String kieContainerListJson) {
        this.kieContainerListJson = kieContainerListJson;
    }

    public String getFrontEndCaseData() {
        return frontEndCaseData;
    }

    public void setFrontEndCaseData(String frontEndCaseData) {
        this.frontEndCaseData = frontEndCaseData;
    }

}
