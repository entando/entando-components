package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.IGroupManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.kieProcess;

import java.util.List;

/**
 *
 */
public class BpmProcessDatatableWidgetAction extends BpmDatatableWidgetAction {

    private IKieFormManager formManager;
    private IKieFormOverrideManager kieFormOverrideManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;

    protected void loadFieldIntoDatatableFromBpm() throws ApsSystemException {
        List<kieProcess> processes = formManager.getProcessDefinitionsList();
        if (!processes.isEmpty()) {
            super.loadDataIntoFieldDatatable(processes);
        }
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

}
