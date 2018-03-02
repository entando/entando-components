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
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.kieProcess;

/**
 *
 * @author own_strong
 */
public class BpmCaseProgressWidgetAction extends SimpleWidgetConfigAction {

    private CaseManager caseManager;
    private IKieFormManager formManager;
    private IKieFormOverrideManager kieFormOverrideManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;
    String processPath;
    String processPathDefaultValue;
    String casesDefinitions;
    List<kieProcess> process;
    String _frontEndMilestonesData;

    @Override
    public String init() {
        String result = super.init();
        try {
            this.setProcess(this.getCaseManager().getProcessDefinitionsList());
        } catch (ApsSystemException ex) {
            Logger.getLogger(BpmCaseProgressWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String save() {
        String result = super.save();
        Widget widget = this.createNewWidget();
        
        if (widget != null) {
            widget.getConfig().setProperty("frontEndMilestonesData",  this.getFrontEndMilestonesData());
        } else {
            System.out.println("NULL WIDGET");
        }

        return result;
    }

    protected String extractInitConfig() {
        String result = super.extractInitConfig();
        String configParam = this.getWidget().getConfig().getProperty("frontEndMilestonesData");
        
        if (StringUtils.isNotBlank(configParam)) {
            this.setFrontEndMilestonesData(configParam);
        }
        return result;
    }

    public String chooseForm() throws IOException {

        try {
            this.setCasesDefinitions(this.getCaseManager().getCasesDefinitions(this.getProcessPath()).toString());

            ///This will set the Conf json to case definition input json
            //After modifying the conf json at the front end this must be removed
            this.setFrontEndMilestonesData(this.getCasesDefinitions());
           
            this.setProcess(this.getCaseManager().getProcessDefinitionsList());
        } catch (ApsSystemException ex) {
            Logger.getLogger(BpmCaseProgressWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return SUCCESS;
    }

    public String changeForm() {

        try {
            this.setCasesDefinitions(this.getCaseManager().getCasesDefinitions(this.getProcessPath()).toString());

            ///This will set the Conf json to case definition input json
            //After modifying the conf json at the front end this must be removed
            this.setFrontEndMilestonesData(this.getCasesDefinitions());

            
            this.setProcess(this.getCaseManager().getProcessDefinitionsList());
        } catch (ApsSystemException ex) {
            Logger.getLogger(BpmCaseProgressWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return SUCCESS;
    }

    public IKieFormManager getFormManager() {
        return formManager;
    }

    public CaseManager getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(CaseManager caseManager) {
        this.caseManager = caseManager;
    }

    public void setFormManager(IKieFormManager formManager) {
        this.formManager = formManager;
    }

    public IBpmWidgetInfoManager getBpmWidgetInfoManager() {
        return bpmWidgetInfoManager;
    }

    public void setBpmWidgetInfoManager(IBpmWidgetInfoManager bpmWidgetInfoManager) {
        this.bpmWidgetInfoManager = bpmWidgetInfoManager;
    }

    public IKieFormOverrideManager getKieFormOverrideManager() {
        return kieFormOverrideManager;
    }

    public void setKieFormOverrideManager(IKieFormOverrideManager kieFormOverrideManager) {
        this.kieFormOverrideManager = kieFormOverrideManager;
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
        return _frontEndMilestonesData;
    }

    public void setFrontEndMilestonesData(String frontEndMilestonesData) {
        this._frontEndMilestonesData = frontEndMilestonesData;
    }

    public List<kieProcess> getProcess() {
        return process;
    }

    public void setProcess(List<kieProcess> process) {
        this.process = process;
    }

    public String getProcessPathDefaultValue() {
        return processPathDefaultValue;
    }

    public void setProcessPathDefaultValue(String processPathDefaultValue) {
        this.processPathDefaultValue = processPathDefaultValue;
    }

}
