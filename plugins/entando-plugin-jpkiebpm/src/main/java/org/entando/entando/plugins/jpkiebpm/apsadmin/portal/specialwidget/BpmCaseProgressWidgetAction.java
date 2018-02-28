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
import com.agiletec.aps.system.services.group.IGroupManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.json.JSONArray;

/**
 *
 * @author own_strong
 */
public class BpmCaseProgressWidgetAction extends BpmProcessDatatableWidgetAction {

//    public static final String SUCCESS = "success";
    private CaseManager caseManager;
    private IKieFormManager formManager;
    private IKieFormOverrideManager kieFormOverrideManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;
    String processPath;
    String casesPath;
    List cases;
    String casesPathDefaultValue;
    String milestones;
    String milestoneJson;
    
    String frontEndMilestonesData;

    @Override
    protected void loadFieldIntoDatatableFromBpm() throws ApsSystemException {
        super.loadFieldIntoDatatableFromBpm();

    }

    @Override
    public String chooseForm() {

        try {
            this.setCases(this.getCaseManager().getCasesList(this.getProcessPath()));
        } catch (ApsSystemException ex) {
            Logger.getLogger(BpmCaseProgressWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return SUCCESS;
    }

    @Override
    public String changeForm() {

        try {
            this.setCases(this.getCaseManager().getCasesList(this.getProcessPath()));
        } catch (ApsSystemException ex) {
            Logger.getLogger(BpmCaseProgressWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return SUCCESS;
    }

    public String chooseCaseForm() throws ApsSystemException {

        this.setCases(this.getCaseManager().getCasesList(this.getProcessPath()));
        this.setCasesPathDefaultValue(casesPath);
        
        JSONArray milestonJSAR = this.getCaseManager().getMilestonesList(this.getProcessPath(), this.getCasesPath());
        
        this.setMilestones(this.getCaseManager().getMilestonesNameInList(milestonJSAR).toString());
        this.setMilestoneJson(milestonJSAR.toString());

        return SUCCESS;
    }

    public String changeCaseForm() throws ApsSystemException {

        this.setCases(this.getCaseManager().getCasesList(this.getProcessPath()));
        this.setCasesPathDefaultValue(casesPath);
        
        JSONArray milestonJSAR = this.getCaseManager().getMilestonesList(this.getProcessPath(), this.getCasesPath());
        
        this.setMilestones(this.getCaseManager().getMilestonesNameInList(milestonJSAR).toString());
        this.setMilestoneJson(milestonJSAR.toString());

        return SUCCESS;
    }

    @Override
    public IGroupManager getGroupManager() {
        return groupManager;
    }

    @Override
    public void setGroupManager(IGroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public IKieFormManager getFormManager() {
        return formManager;
    }

    public CaseManager getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(CaseManager caseManager) {
        this.caseManager = caseManager;
    }

    @Override
    public void setFormManager(IKieFormManager formManager) {
        this.formManager = formManager;
    }

    @Override
    public IBpmWidgetInfoManager getBpmWidgetInfoManager() {
        return bpmWidgetInfoManager;
    }

    @Override
    public void setBpmWidgetInfoManager(IBpmWidgetInfoManager bpmWidgetInfoManager) {
        this.bpmWidgetInfoManager = bpmWidgetInfoManager;
    }

    @Override
    public IKieFormOverrideManager getKieFormOverrideManager() {
        return kieFormOverrideManager;
    }

    @Override
    public void setKieFormOverrideManager(IKieFormOverrideManager kieFormOverrideManager) {
        this.kieFormOverrideManager = kieFormOverrideManager;
    }

    @Override
    public String getProcessPath() {
        return processPath;
    }

    @Override
    public void setProcessPath(String processPath) {
        this.processPath = processPath;
    }

    public List getCases() {
        return cases;
    }

    public void setCases(List cases) {
        this.cases = cases;
    }

    public String getMilestones() {
        return milestones;
    }

    public void setMilestones(String milestones) {
        this.milestones = milestones;
    }

    public String getCasesPath() {
        return casesPath;
    }

    public void setCasesPath(String casesPath) {
        this.casesPath = casesPath;
    }

    public String getCasesPathDefaultValue() {
        return casesPathDefaultValue;
    }

    public void setCasesPathDefaultValue(String casesPathDefaultValue) {
        this.casesPathDefaultValue = casesPathDefaultValue;
    }

    public String getFrontEndMilestonesData() {
        return frontEndMilestonesData;
    }

    public void setFrontEndMilestonesData(String frontEndMilestonesData) {
        this.frontEndMilestonesData = frontEndMilestonesData;
    }

    public String getMilestoneJson() {
        return milestoneJson;
    }

    public void setMilestoneJson(String milestoneJson) {
        this.milestoneJson = milestoneJson;
    }
    
    

}
