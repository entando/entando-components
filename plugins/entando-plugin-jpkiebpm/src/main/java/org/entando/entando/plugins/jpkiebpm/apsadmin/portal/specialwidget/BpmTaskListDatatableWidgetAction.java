package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.IGroupManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTask;

import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class BpmTaskListDatatableWidgetAction extends BpmDatatableWidgetAction {

    private IKieFormManager formManager;
    private IKieFormOverrideManager kieFormOverrideManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;

    private String DEMO_USER = "taskUser";


    @Override
    protected void loadFieldIntoDatatableFromBpm() throws ApsSystemException {
        HashMap<String, String> opt = new HashMap<>();
        opt.put("user", DEMO_USER);
        KieBpmConfig config = formManager.getKieServerConfigurations().get(this.getKnowledgeSourceId());
        List<KieTask> task = formManager.getHumanTaskList(config, null, opt);
        if (!task.isEmpty()) {
            super.loadDataIntoFieldDatatable(task);
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
