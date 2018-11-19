package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.IGroupManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.CaseProgressWidgetHelpers.convertKieContainerToListToJson;

public class BpmProcessDatatableWidgetAction extends BpmDatatableWidgetAction {

    private static final Logger logger = LoggerFactory.getLogger(BpmProcessDatatableWidgetAction.class);
    private IKieFormManager formManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;

    private String knowledgeSourcePath;
    private HashMap<String, KieBpmConfig> knowledgeSource;

    protected void loadFieldIntoDatatableFromBpm() throws ApsSystemException {
        KieBpmConfig config = formManager.getKieServerConfigurations().get(this.getKnowledgeSourcePath());
        List<KieProcess> processes = formManager.getProcessDefinitionsList(config);
        if (!processes.isEmpty()) {
            super.loadDataIntoFieldDatatable(processes);
        }

        HashMap<String,String> columns = new HashMap<>();

        columns.put("Status Progress","statusProgress");
        columns.put("Customer Name","customerName");
        columns.put("partyName","partyName");
        columns.put("status","status");
        columns.put("Company","company");
        columns.put("Case Due In","dueDate");

        this.loadDataIntoFieldDatatable(columns);
    }

    private void loadDataIntoFieldDatatable(HashMap fields) {

        Byte position = 1;
        for (Iterator<Map.Entry> iter = fields.entrySet().iterator(); iter.hasNext();){
            Map.Entry<String,String> obj = iter.next();
            final String name = obj.getValue();
            final FieldDatatable fd = new FieldDatatable(name);
            fd.setField(PREFIX_FIELD + obj.getKey());
            fd.setPosition(position++);
            fd.setVisible(Boolean.valueOf(true));
            fd.setOverride("");
            this.fieldsDatatable.add(fd);

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

    public String getKnowledgeSourcePath() {
        return knowledgeSourcePath;
    }

    public void setKnowledgeSourcePath(String knowledgeSourcePath) {
        this.knowledgeSourcePath = knowledgeSourcePath;
    }

    public String chooseKnowledgeSourceForm() {
        return  updateKnowledgeSource();
    }

    public String changeKnowledgeSourceForm() {
        return  updateKnowledgeSource();
    }

    private String updateKnowledgeSource() {
        try {

            KieBpmConfig config = formManager.getKieServerConfigurations().get(knowledgeSourcePath);
            this.setProcess(this.formManager.getProcessDefinitionsList(config));

            this.setKnowledgeSourceJson(this.formManager.getKieServerStatus().toString());
            this.setKieContainerListJson(convertKieContainerToListToJson(this.formManager.getContainersList(config)).toString());

        } catch (ApsSystemException t) {
            logger.error("Error in chooseKnowledgeSourceForm()", t);
            return FAILURE;
        }
        return SUCCESS;

    }

    @Override
    public HashMap<String, KieBpmConfig> getKnowledgeSource() {
        return knowledgeSource;
    }

    public void setKnowledgeSource(HashMap<String, KieBpmConfig> knowledgeSource) {
        this.knowledgeSource = knowledgeSource;
    }
}
