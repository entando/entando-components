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
import java.util.ArrayList;
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

//    public static final String SUCCESS = "success";
    private CaseManager caseManager;
    private IKieFormManager formManager;
    private IKieFormOverrideManager kieFormOverrideManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;
    String processPath;
    String processPathDefaultValue;
    String casesDefinitions;
    List<String> process;

    String _frontEndMilestonesData;

    @Override
    public String init() {
        String result = super.init();
        try {
            List<String> cids = new ArrayList();
            List<kieProcess> in = this.getCaseManager().getProcessDefinitionsList();
            for (int i = 0; i < in.size(); i++) {
                cids.add(in.get(i).getContainerId());

            }
            this.setProcess(cids);

        } catch (ApsSystemException ex) {
            Logger.getLogger(BpmCaseProgressWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }
//    @Override
//    public String save(){
//        String result = super.save();
//        Widget widget = this.createNewWidget();
//        
//        widget.getConfig().setProperty("frontEndMilestonesData", "frontEndMilestonesData");
//        
//        return result;
//    }

    protected String extractInitConfig() {
        String result = super.extractInitConfig();
        String configParam = this.getWidget().getConfig().getProperty("frontEndMilestonesData");
        if (StringUtils.isNotBlank(configParam)) {
            this.setFrontEndMilestonesData(configParam);
        }
        return result;
    }

    public String chooseForm() {

        try {
            this.setCasesDefinitions(this.getCaseManager().getCasesDefinitions(this.getProcessPath()).toString());
            this.setFrontEndMilestonesData("Choose Form");
//            if (this.getWidget() != null) {
//                this.getWidget().getConfig().setProperty("frontEndMilestonesData", "Choose Form");
//            } else{
//                System.out.println("String (nonSt )Conf: "+this.getWidget().toString());
//                System.out.println("String Conf: "+this.getWidget().toString());
//            }
//            System.out.println("After clicking choose " + this.getWidget().getConfig().getProperty("frontEndMilestonesData"));

            List<String> cids = new ArrayList();
            List<kieProcess> in = this.getCaseManager().getProcessDefinitionsList();
            for (int i = 0; i < in.size(); i++) {
                cids.add(in.get(i).getContainerId());
            }
            this.setProcess(cids);
            this.setProcessPathDefaultValue(processPath);
        } catch (ApsSystemException ex) {
            Logger.getLogger(BpmCaseProgressWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return SUCCESS;
    }

    public String changeForm() {
        try {
            this.setCasesDefinitions(this.getCaseManager().getCasesDefinitions(this.getProcessPath()).toString());
            this.setFrontEndMilestonesData("ChangeForm");

            List<String> cids = new ArrayList();
            List<kieProcess> in = this.getCaseManager().getProcessDefinitionsList();
            for (int i = 0; i < in.size(); i++) {
                cids.add(in.get(i).getContainerId());
            }
            this.setProcess(cids);
            this.setProcessPathDefaultValue(processPath);
        } catch (ApsSystemException ex) {
            Logger.getLogger(BpmCaseProgressWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return SUCCESS;
    }

//    public String chooseCaseForm() throws ApsSystemException {
//
//        this.setCases(this.getCaseManager().getCasesList(this.getProcessPath()));
//        this.setCasesPathDefaultValue(casesPath);
//        
//        JSONArray milestonJSAR = this.getCaseManager().getMilestonesList(this.getProcessPath(), this.getCasesPath());
//        
//        this.setMilestonesNameListString(this.getCaseManager().getMilestonesNameInList(milestonJSAR).toString());
//        this.setMilestoneJson(milestonJSAR.toString());
//
//        return SUCCESS;
//    }
//
//    public String changeCaseForm() throws ApsSystemException {
//
//        this.setCases(this.getCaseManager().getCasesList(this.getProcessPath()));
//        this.setCasesPathDefaultValue(casesPath);
//        
//        JSONArray milestonJSAR = this.getCaseManager().getMilestonesList(this.getProcessPath(), this.getCasesPath());
//        
//        this.setMilestonesNameListString(this.getCaseManager().getMilestonesNameInList(milestonJSAR).toString());
//        this.setMilestoneJson(milestonJSAR.toString());
//
//        return SUCCESS;
//    }
//    @Override
//    public IGroupManager getGroupManager() {
//        return groupManager;
//    }
//
//    @Override
//    public void setGroupManager(IGroupManager groupManager) {
//        this.groupManager = groupManager;
//    }
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

    public List<String> getProcess() {
        return process;
    }

    public void setProcess(List<String> process) {
        this.process = process;
    }

    public String getProcessPathDefaultValue() {
        return processPathDefaultValue;
    }

    public void setProcessPathDefaultValue(String processPathDefaultValue) {
        this.processPathDefaultValue = processPathDefaultValue;
    }

}
