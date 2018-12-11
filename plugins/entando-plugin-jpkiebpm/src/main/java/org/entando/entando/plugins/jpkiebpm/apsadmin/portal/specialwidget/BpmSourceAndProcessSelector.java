package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.List;
import java.util.Map;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;

public interface BpmSourceAndProcessSelector<T> {

    String changeKnowledgeSourceForm();

    String chooseKnowledgeSourceForm();

    String changeForm();

    String chooseForm();

    List<T> getProcess();

    void setProcess(List<T> processes);

    void loadProcesses(KieBpmConfig config) throws ApsSystemException;

    void setProcessPath(String processPath);

    String getProcessPath();

    String getKnowledgeSourcePath();

    void setKnowledgeSourcePath(String knowledgeSourcePath);

    IKieFormManager getFormManager();

    Map<String, KieBpmConfig> getKnowledgeSource();

    void setKnowledgeSource(Map<String, KieBpmConfig> sources);
}
