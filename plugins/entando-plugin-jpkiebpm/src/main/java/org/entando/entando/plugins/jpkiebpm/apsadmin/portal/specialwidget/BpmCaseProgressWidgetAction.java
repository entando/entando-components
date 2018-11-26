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

import com.agiletec.aps.system.services.page.Widget;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class BpmCaseProgressWidgetAction extends BpmCaseActionBase {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BpmCaseProgressWidgetAction.class);

    private CaseManager caseManager;

    @Autowired
    private IKieFormManager formManager;

    private Boolean showMilestones;
    private String progressBarType;
    private Boolean showNumberOfTasks;
    private HashMap<String, String> progressBarTypes;
    private static String PROGRESS_BAR_BASIC ="basic";
    private static String PROGRESS_BAR_STACKED = "stacked";
    
    @Override
    public String init() {
        String result = super.init();
        initProgressBarTypes();
        return result;
    }
    
    @Override
    protected String extractInitConfig() {
        String result = super.extractInitConfig();
        Widget widget = this.getWidget();
        if (widget != null) {
            String channel = widget.getConfig().getProperty("channel");
            String progressBarType = widget.getConfig().getProperty("progressBarType");
            String showMilestones = widget.getConfig().getProperty("showMilestones");
            String showNumberOfTasks = widget.getConfig().getProperty("showNumberOfTasks");
            this.setChannel(channel);
            this.setProgressBarType(progressBarType);
            this.setShowMilestones(Boolean.valueOf(showMilestones));
            this.setShowNumberOfTasks(Boolean.valueOf(showNumberOfTasks));
            this.setWidgetTypeCode(this.getWidget().getType().getCode());
        } else {
            logger.warn(" widget is null in extraction ");
        }
        return result;
    }
    
    private void initProgressBarTypes(){
        progressBarTypes= new HashMap<>();
        progressBarTypes.put(PROGRESS_BAR_BASIC, PROGRESS_BAR_BASIC);
        progressBarTypes.put(PROGRESS_BAR_STACKED, PROGRESS_BAR_STACKED);
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

    public boolean isShowMilestones() {
        return showMilestones;
    }

    public void setShowMilestones(boolean showMilestones) {
        this.showMilestones = showMilestones;
    }

    public String getProgressBarType() {
        return progressBarType;
    }

    public void setProgressBarType(String progressBarType) {
        this.progressBarType = progressBarType;
    }

    public boolean isShowNumberOfTasks() {
        return showNumberOfTasks;
    }

    public void setShowNumberOfTasks(boolean showNumberOfTasks) {
        this.showNumberOfTasks = showNumberOfTasks;
    }

    public HashMap<String, String> getProgressBarTypes() {
        return progressBarTypes;
    }

    public void setProgressBarTypes(HashMap<String, String> progressBarTypes) {
        this.progressBarTypes = progressBarTypes;
    }

}
