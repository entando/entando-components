package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.IGroupManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.*;
import org.slf4j.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BpmTaskListDatatableWidgetAction extends BpmDatatableWidgetAction {

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
}
