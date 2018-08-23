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
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieContainer;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.CaseProgressWidgetHelpers.*;

/**
 *
 * @author own_strong
 */
public class BpmCaseProgressWidgetAction extends BpmCaseActionBase {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BpmCaseProgressWidgetAction.class);

    private CaseManager caseManager;
    private KieFormManager formManager;
    private String processPath;
    private String casesDefinitions;
    private List<KieContainer> process;
    private String frontEndMilestonesData;
    private HashMap<String, KieBpmConfig> knowledgeSources;
    private String knowledgeSourceId;

    private String knowledgeSourceJson;
    private String kieContainerListJson;

    private String channel;
    private List<Integer> channels;

    @Override
    public String init() {
        String result = super.init();
        return result;
    }

    public String chooseKnowledgeSourceForm() {
        return  updateKnowledgeSource();
    }

    public String changeKnowledgeSourceForm() {
        return  updateKnowledgeSource();
    }

    private String updateKnowledgeSource() {
        try {

            KieBpmConfig config = formManager.getKieServerConfigurations().get(knowledgeSourceId);
            this.setKnowledgeSources(this.formManager.getKieServerConfigurations());
            this.setProcess(this.formManager.getContainersList(config));

            this.setKnowledgeSourceJson(this.formManager.getKieServerStatus().toString());
            this.setKieContainerListJson(convertKieContainerToListToJson(this.formManager.getContainersList(config)).toString());

        } catch (ApsSystemException t) {
            logger.error("Error in chooseKnowledgeSourceForm()", t);
            return FAILURE;
        }
        return SUCCESS;

    }

    public String chooseForm() {
        return updateForm();
    }

    public String changeForm() {
        return updateForm();
    }

    private String updateForm() {
        try {
            JSONObject frontEndCaseDatajs = new JSONObject();
            frontEndCaseDatajs.put("knowledge-source-id", this.getKnowledgeSourceId());
            frontEndCaseDatajs.put("container-id", this.getProcessPath());
            this.setFrontEndCaseData(frontEndCaseDatajs.toString());

            KieBpmConfig config = formManager.getKieServerConfigurations().get(knowledgeSourceId);

            this.setKnowledgeSources(this.formManager.getKieServerConfigurations());
            this.setProcess(this.formManager.getContainersList(config));

            this.setKnowledgeSourceJson(this.formManager.getKieServerStatus().toString());
            this.setKieContainerListJson(convertKieContainerToListToJson(this.formManager.getContainersList(config)).toString());

        } catch (ApsSystemException t) {
            logger.error("Error in changeForm()", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    protected String extractInitConfig() {
        String result = super.extractInitConfig();

        try {

            Widget widget = this.getWidget();
            String frontEndMilestonesDatain;
            String channel;

            if (widget != null) {
                this.setWidgetTypeCode(this.getWidget().getType().getCode());
                frontEndMilestonesDatain = widget.getConfig().getProperty("frontEndMilestonesData");
                channel = widget.getConfig().getProperty("channel");

                if (StringUtils.isNotBlank(frontEndMilestonesDatain)) {

                    this.setFrontEndMilestonesData(frontEndMilestonesDatain);
                    //Add if conf exist

                    KieBpmConfig config = formManager.getKieServerConfigurations().get(knowledgeSourceId);

                    this.setKnowledgeSources(this.formManager.getKieServerConfigurations());
                    this.setKnowledgeSourceJson(this.formManager.getKieServerStatus().toString());
                    this.setKnowledgeSourceId(getKieIDfromfrontEndMilestonesData(frontEndMilestonesDatain));
                    this.setProcess(this.formManager.getContainersList(config));
                    this.setKieContainerListJson(convertKieContainerToListToJson(this.formManager.getContainersList(config)).toString());
                    //Add if Container exist
                    this.setProcessPath(getContainerIDfromfrontEndMilestonesData(frontEndMilestonesDatain));

                    this.setCasesDefinitions(this.getCaseManager().getCasesDefinitions(config, this.getProcessPath()).toString());
                    this.setChannel(channel);

                } else {
                    this.setKnowledgeSources(this.formManager.getKieServerConfigurations());
                    this.setKnowledgeSourceJson(this.formManager.getKieServerStatus().toString());
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

    public String getProcessPath() {
        return processPath;
    }

    public void setProcessPath(String processPath) {
        this.processPath = processPath;
    }

    public String getCasesDefinitions() {
        return casesDefinitions;
    }

    public void setCasesDefinitions(String casesDefinitions) {
        this.casesDefinitions = casesDefinitions;
    }

    public String getFrontEndMilestonesData() {
        return frontEndMilestonesData;
    }

    public void setFrontEndMilestonesData(String frontEndMilestonesData) {
        this.frontEndMilestonesData = frontEndMilestonesData;
    }

    public List<KieContainer> getProcess() {
        return process;
    }

    public void setProcess(List<KieContainer> process) {
        this.process = process;
    }

    public HashMap<String, KieBpmConfig> getKnowledgeSources() {
        return knowledgeSources;
    }

    public void setKnowledgeSources(HashMap<String, KieBpmConfig> knowledgeSources) {
        this.knowledgeSources = knowledgeSources;
    }

    public String getKnowledgeSourceId() {
        return knowledgeSourceId;
    }

    public void setKnowledgeSourceId(String knowledgeSourceId) {
        this.knowledgeSourceId = knowledgeSourceId;
    }

    public String getKieContainerListJson() {
        return kieContainerListJson;
    }

    public void setKieContainerListJson(String kieContainerListJson) {
        this.kieContainerListJson = kieContainerListJson;
    }

    public String getKnowledgeSourceJson() {
        return knowledgeSourceJson;
    }

    public void setKnowledgeSourceJson(String knowledgeSourceJson) {
        this.knowledgeSourceJson = knowledgeSourceJson;
    }

}
