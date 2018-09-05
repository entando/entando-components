package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.IGroupManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class BpmTaskListDatatableWidgetAction extends BpmDatatableWidgetAction {

    private IKieFormOverrideManager kieFormOverrideManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;
    private static final Logger logger = LoggerFactory.getLogger(BpmProcessDatatableWidgetAction.class);
    private String DEMO_USER = "taskUser";


    @Override
    protected void loadFieldIntoDatatableFromBpm() throws ApsSystemException {
        HashMap<String, String> opt = new HashMap<>();
        opt.put("user", DEMO_USER);
        KieBpmConfig config = super.getFormManager().getKieServerConfigurations().get(this.getKnowledgeSourcePath());
        List<KieTask> task = super.getFormManager().getHumanTaskList(config, null, opt);
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
