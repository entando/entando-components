package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BpmProcessDatatableWidgetAction extends BpmDatatableWidgetAction {

    private static final Logger logger = LoggerFactory.getLogger(BpmProcessDatatableWidgetAction.class);

    @Override
    protected void loadFieldIntoDatatableFromBpm() throws ApsSystemException {
        KieBpmConfig config = getFormManager().getKieServerConfigurations().get(this.getKnowledgeSourcePath());
        List<KieProcess> processes = getFormManager().getProcessDefinitionsList(config);
        if (!processes.isEmpty()) {
            super.loadDataIntoFieldDatatable(processes);
        }

        Map<String, String> columns = new HashMap<>();

        columns.put("Status Progress", "statusProgress");
        columns.put("Customer Name", "customerName");
        columns.put("partyName", "partyName");
        columns.put("status", "status");
        columns.put("Company", "company");
        columns.put("Case Due In", "dueDate");

        this.loadDataIntoFieldDatatable(columns);
    }

    private void loadDataIntoFieldDatatable(Map<String, String> fields) {

        Byte position = 1;

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            final FieldDatatable fd = new FieldDatatable(entry.getValue());
            fd.setField(PREFIX_FIELD + entry.getKey());
            fd.setPosition(position++);
            fd.setVisible(true);
            fd.setOverride("");
            this.fieldsDatatable.add(fd);
        }
    }
}
