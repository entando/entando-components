package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by josephwhite on 8/22/18.
 */
public interface ICaseManager {
    JSONObject getCasesDefinitions(KieBpmConfig config, String containerId) throws ApsSystemException;

    List<String> getCaseInstancesList(KieBpmConfig config, String containerId) throws ApsSystemException;

    JSONObject getCaseInstancesDetails(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException;

    JSONArray getMilestonesList(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException;

    JSONArray getCaseComments(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException;

    boolean postCaseComments(KieBpmConfig config, String containerId, String caseID, String comment) throws ApsSystemException;

    boolean updateCaseComments(KieBpmConfig config, String containerId, String caseID, String caseCommentId, String comment) throws ApsSystemException;

    boolean deleteCaseComments(KieBpmConfig config, String containerId, String caseID, String caseCommentId) throws ApsSystemException;

    JSONObject getCaseRoles(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException;

    boolean addCaseRoles(KieBpmConfig config, String containerId, String caseID, String caseRoleName, String user, String group) throws ApsSystemException;

    boolean deleteCaseRoles(KieBpmConfig config, String containerId, String caseID, String caseRoleName, String user, String group) throws ApsSystemException;

    JSONObject getCaseFile(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException;

    boolean postCaseFile(KieBpmConfig config, String containerId, String caseID, String data) throws ApsSystemException;

    boolean deleteCaseFile(KieBpmConfig config, String containerId, String caseID, String dataId) throws ApsSystemException;

    JSONObject getProcessInstanceByCorrelationKey(KieBpmConfig config, String correlationKey) throws ApsSystemException;
}
