package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BpmProcessDatatableWidgetAction extends BpmDatatableWidgetAction {

    private static final Logger logger = LoggerFactory.getLogger(BpmProcessDatatableWidgetAction.class);

    @Override
    protected void loadFieldIntoDatatableFromBpm(String containerId, String processId) throws ApsSystemException {
        KieBpmConfig config = getFormManager().getKieServerConfigurations().get(this.getKnowledgeSourcePath());
        List<KieProcess> processes = getFormManager().getProcessDefinitionsList(config);
        if (!processes.isEmpty()) {

            //Horrible hack. To be replaced by an actual call.
            List<String> fields = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(processes.get(0).toString(), ",");
            Byte position = 1;
            while (tokenizer.hasMoreTokens()) {
                final String name = tokenizer.nextToken().trim();
                fields.add(name);

            }

            super.loadDataIntoFieldDatatable(fields);
        }

        Set<String> processVars = getFormManager().getProcessVariables(config, containerId, processId);

        Map<String, String> columns = new HashMap<>();
        processVars.stream().forEach( s -> columns.put(s ,s));
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
