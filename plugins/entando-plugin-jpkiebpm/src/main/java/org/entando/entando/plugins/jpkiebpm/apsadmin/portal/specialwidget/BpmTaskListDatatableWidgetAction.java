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
    protected void loadFieldIntoDatatableFromBpm(String containerId, String procId) throws ApsSystemException {
        HashMap<String, String> opt = new HashMap<>();
        opt.put("user", DEMO_USER);
        KieBpmConfig config = super.getFormManager().getKieServerConfigurations().get(this.getKnowledgeSourcePath());
        List<KieTask> task = super.getFormManager().getHumanTaskList(config, null, opt);

        Set<String> variableFields = super.getFormManager().getProcessVariables(config, containerId, procId);
        //JPW -- Assign process variables for datatable
//        /server/queries/processes/instances/{pInstanceId}/variables/instances
//        /server/containers/{id}/processes/definitions/{pId}/variables
        //JPW -- Assog tasl varoab;es

        List<String> fields = new ArrayList<>();
        fields.addAll(variableFields);


        //Horrible hack. To be replaced by an actual call.
        if (!task.isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(task.get(0).toString(), ",");
            Byte position = 1;
            while (tokenizer.hasMoreTokens()) {
                final String name = tokenizer.nextToken().trim();
                fields.add(name);
            }
        }

        loadDataIntoFieldDatatable(fields);
    }
}
