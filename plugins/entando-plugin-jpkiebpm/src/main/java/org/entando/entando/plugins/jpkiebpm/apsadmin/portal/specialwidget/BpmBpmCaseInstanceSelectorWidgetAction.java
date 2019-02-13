/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.opensymphony.xwork2.Preparable;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.*;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.logging.*;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.CaseProgressWidgetHelpers.convertKieContainerToListToJson;

public class BpmBpmCaseInstanceSelectorWidgetAction extends BpmCaseActionBase implements BpmSourceAndProcessSelector<KieContainer>, Preparable {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BpmBpmCaseInstanceSelectorWidgetAction.class);

    private CaseManager caseManager;

    @Autowired
    private IKieFormManager formManager;
    private Map<String, KieBpmConfig> knowledgeSource;
    private List<KieContainer> process;
    private String processPath;

    private String knowledgeSourcePath;

    private String knowledgeSourceJson;
    private String kieContainerListJson;

    private BpmSourceAndProcessSelectorHelper<BpmBpmCaseInstanceSelectorWidgetAction, KieContainer> selectorHelper;

    /**
     * This method is called before each action.
     */
    @Override
    public void prepare() throws ApsSystemException {
        selectorHelper = new BpmSourceAndProcessSelectorHelper<>(this);
    }

    @Override
    public String chooseKnowledgeSourceForm() {
        try {
            selectorHelper.loadProcesses();
        } catch (ApsSystemException t) {
            logger.error("Error in chooseKnowledgeSourceForm", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    public String changeKnowledgeSourceForm() {
        knowledgeSourcePath = null;
        processPath = null;
        return SUCCESS;
    }

    @Override
    public String chooseForm() {
        try {
            selectorHelper.loadProcesses();
            JSONObject frontEndCaseDatajs = new JSONObject();
            frontEndCaseDatajs.put("knowledge-source-id", this.getKnowledgeSourcePath());
            frontEndCaseDatajs.put("container-id", this.getProcessPath());
            this.setFrontEndCaseData(frontEndCaseDatajs.toString());
        } catch (ApsSystemException t) {
            logger.error("Error in chooseForm", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    public String changeForm() {
        try {
            processPath = null;
            selectorHelper.loadProcesses();
        } catch (ApsSystemException t) {
            logger.error("Error in changeForm()", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    protected String extractInitConfig() {
        String result = super.extractInitConfig();

        Widget widget = this.getWidget();
        String frontEndCaseDatain;
        String channel;
        try {
            if (widget != null) {
                this.setWidgetTypeCode(this.getWidget().getType().getCode());
                frontEndCaseDatain = widget.getConfig().getProperty("frontEndCaseData");
                channel = widget.getConfig().getProperty("channel");

                this.setKnowledgeSourceJson(this.formManager.getKieServerStatus().toString());

                if (StringUtils.isNotBlank(frontEndCaseDatain)) {

                    this.setFrontEndCaseData(frontEndCaseDatain);
                    JSONObject frontEndCaseDatainjs = new JSONObject(frontEndCaseDatain);

                    String configuredSourceId = frontEndCaseDatainjs.getString("knowledge-source-id");
                    this.setKnowledgeSourcePath(configuredSourceId);
                    if (selectorHelper.loadProcesses()) {
                        this.setProcessPath(frontEndCaseDatainjs.getString("container-id"));
                        this.setChannel(channel);
                    }
                }
            } else {
                logger.warn(" widget is null in extraction ");
            }
        } catch (ApsSystemException ex) {
            Logger.getLogger(BpmBpmCaseInstanceSelectorWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public void loadProcesses(KieBpmConfig config) throws ApsSystemException {
        List<KieContainer> containers = this.formManager.getContainersList(config);
        this.setProcess(containers);
        this.setKnowledgeSourceJson(this.formManager.getKieServerStatus().toString());
        this.setKieContainerListJson(convertKieContainerToListToJson(containers).toString());
    }

    public CaseManager getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(CaseManager caseManager) {
        this.caseManager = caseManager;
    }

    @Override
    public Map<String, KieBpmConfig> getKnowledgeSource() {
        return knowledgeSource;
    }

    @Override
    public void setKnowledgeSource(Map<String, KieBpmConfig> sources) {
        this.knowledgeSource = sources;
    }

    @Override
    public List<KieContainer> getProcess() {
        return process;
    }

    @Override
    public void setProcess(List<KieContainer> process) {
        this.process = process;
    }

    @Override
    public String getProcessPath() {
        return processPath;
    }

    @Override
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

    @Override
    public IKieFormManager getFormManager() {
        return formManager;
    }

    public void setFormManager(IKieFormManager formManager) {
        this.formManager = formManager;
    }

    @Override
    public String getKnowledgeSourcePath() {
        return knowledgeSourcePath;
    }

    @Override
    public void setKnowledgeSourcePath(String knowledgeSourcePath) {
        this.knowledgeSourcePath = knowledgeSourcePath;
    }
}
