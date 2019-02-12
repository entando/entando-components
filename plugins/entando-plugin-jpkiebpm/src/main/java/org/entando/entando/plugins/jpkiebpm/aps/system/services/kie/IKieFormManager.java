/*
 * The MIT License
 *
 * Copyright 2017 Entando Inc..
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormManager.TASK_STATES;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiProcessStart;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Entando
 */
public interface IKieFormManager {

    String BEAN_NAME_ID = "jpkiebpmsManager";

    /**
     * add service configuration
     *
     * @param config
     * @throws com.agiletec.aps.system.exception.ApsSystemException
     */
    public void addConfig(KieBpmConfig config) throws ApsSystemException;

    /**
     * add service configuration
     *
     * @param kieId
     * @throws com.agiletec.aps.system.exception.ApsSystemException
     */
    public void deleteConfig(String kieId) throws ApsSystemException;

    /**
     * Get list of service configuration
     *
     * @return
     * @throws com.agiletec.aps.system.exception.ApsSystemException
     */
    public HashMap<String, KieBpmConfig> getKieServerConfigurations() throws ApsSystemException;

    /**
     * List KIE containers
     *
     * @return
     * @throws com.agiletec.aps.system.exception.ApsSystemException
     */
    List<KieContainer> getContainersList(KieBpmConfig config) throws ApsSystemException;

    /**
     * Return the process definition list
     *
     * @return
     * @throws ApsSystemException
     */
    List<KieProcess> getProcessDefinitionsList(KieBpmConfig config) throws ApsSystemException;

    /**
     * Get the process instances give the process ID
     *
     * @param processId
     * @param page
     * @param pageSize
     * @return
     * @throws ApsSystemException
     */
    List<KieProcessInstance> getProcessInstancesList(KieBpmConfig config, String processId, int page, int pageSize) throws ApsSystemException;

    /**
     * Get the list of human task
     *
     * @param groups
     * @param opt
     * @return
     * @throws ApsSystemException
     */
    List<KieTask> getHumanTaskList(KieBpmConfig config, String groups, Map<String, String> opt) throws ApsSystemException;

    /**
     * Get the deiser human task form given container ID and form ID
     *
     * @param containerId
     * @param taskId
     * @return
     * @throws ApsSystemException
     */
    KieProcessFormQueryResult getTaskForm(KieBpmConfig config, String containerId, long taskId) throws ApsSystemException;

    /**
     * Return the process forms.
     *
     * @param containerId
     * @param processId
     * @return
     * @throws ApsSystemException
     * @note This method marshals the XML returned by the server
     * @note This method marshals the XML returned by the server
     */
    KieProcessFormQueryResult getProcessForm(KieBpmConfig config, String containerId, String processId) throws ApsSystemException;

    /**
     * Submit a form and start the related process
     *
     * @param containerId
     * @param processId
     * @param input
     * @return
     * @throws ApsSystemException
     */
    String startProcessSubmittingForm(KieBpmConfig config, String containerId, String processId, Map<String, Object> input) throws ApsSystemException;

    String startProcessSubmittingForm(KieBpmConfig config, KieProcessFormQueryResult form, String containerId, String processId, Map<String, Object> input) throws ApsSystemException;

    /**
     * Submit a form and start the related process
     *
     * @param process
     * @param input
     * @return
     * @throws ApsSystemException
     */
    String startNewProcess(KieBpmConfig config, KieApiProcessStart process, Map<String, Object> input) throws ApsSystemException;

    String startNewProcess(KieBpmConfig config, String containerId, String processId, Map<String, Object> input) throws ApsSystemException;

    /**
     * Get the process diagram
     *
     * @param containerId
     * @return
     * @throws ApsSystemException
     */
    String getProcInstDiagramImage(KieBpmConfig config, String containerId) throws ApsSystemException;


    /**
     * Get the process diagram
     *
     * @param containerId
     * @param processId
     * @return
     * @throws ApsSystemException
     */
    String getProcInstDiagramImage(KieBpmConfig config, String containerId, String pInstanceId) throws ApsSystemException;

    /**
     * Get the data of the current task
     *
     * @param containerId
     * @param taskId
     * @return
     * @throws ApsSystemException
     */
    JSONObject getTaskFormData(KieBpmConfig config, String containerId, long taskId, Map<String, String> opt) throws ApsSystemException;

    /**
     * @param containerId
     * @param processId
     * @param taskId
     * @param input
     * @return
     * @throws ApsSystemException
     */
    String completeHumanFormTask(KieBpmConfig config, String containerId, String processId, final long taskId, final Map<String, String> input) throws ApsSystemException;

    /**
     * @param containerId
     * @param taskId
     * @param form
     * @param task
     * @param input
     * @return
     * @throws ApsSystemException
     */
    String completeHumanFormTask(KieBpmConfig config, String containerId, final long taskId, final KieProcessFormQueryResult form, final JSONObject task, final Map<String, Object> input)
            throws ApsSystemException;

    /**
     * Get a specific human task FIXME check whether there is already a specific
     * call for this
     *
     * @param processId
     * @return
     * @throws ApsSystemException
     */
    KieTask getHumanTask(KieBpmConfig config, String processId) throws ApsSystemException;

    /**
     * @param containerId
     * @param taskId
     * @param opt
     * @return
     * @throws ApsSystemException
     */
    KieTaskDetail getTaskDetail(KieBpmConfig config, String containerId, final Long taskId, Map<String, String> opt) throws ApsSystemException;

    /**
     * @param containerId
     * @param processId
     * @param signal
     * @param accountId
     * @param opt
     * @return
     * @throws ApsSystemException
     */
    boolean sendSignal(KieBpmConfig config, String containerId, String processId, String signal, String accountId, Map<String, String> opt) throws ApsSystemException;

    /**
     * @param containerId
     * @param processId
     * @param opt
     * @throws ApsSystemException
     */
    void deleteProcess(KieBpmConfig config, String containerId, String processId, Map<String, String> opt) throws ApsSystemException;

    /**
     * @param opt
     * @return
     * @throws ApsSystemException
     */
    List<KieProcessInstance> getAllProcessInstancesList(KieBpmConfig config, Map<String, String> opt) throws ApsSystemException;

    /**
     * @param containerId
     * @param taskId
     * @param state
     * @param queryStringParam
     * @param input
     * @return
     * @throws Throwable
     */
    String submitHumanFormTask(KieBpmConfig config, String containerId, String taskId, final TASK_STATES state, Map<String, String> queryStringParam, Map<String, Object> input) throws Throwable;

    /**
     * @param containerId
     * @param taskId
     * @param state
     * @param opt
     * @return
     * @throws Throwable
     */
    String setTaskState(KieBpmConfig config, String containerId, String taskId, final TASK_STATES state, Map<String, Object> input, Map<String, String> opt) throws Throwable;

    /**
     * @param user
     * @param opt
     * @return
     * @throws ApsSystemException
     */
    public List<KieTask> getHumanTaskListForUser(KieBpmConfig config, String user, Map<String, String> opt) throws ApsSystemException;

    public List<KieTask> getHumanTaskListForAdmin(KieBpmConfig config, String user, Map<String, String> opt) throws ApsSystemException;

    /**
     * @param input
     * @param opt
     * @return
     * @throws Throwable
     */
    public KieProcessInstancesQueryResult getProcessInstancesWithClientData(KieBpmConfig config, Map<String, String> input, Map<String, String> opt) throws Throwable;

    /**
     * @param user
     * @param containerId
     * @param taskId
     * @param state
     * @param review
     * @param opt
     * @return
     * @throws ApsSystemException
     */
    public boolean getCompleteEnrichmentDcumentApprovalTask(KieBpmConfig config, String user, String containerId, String taskId, TASK_STATES state, String review, Map<String, String> opt) throws ApsSystemException;

    public Map<String, String> getHostNameVersionMap();

    public JSONArray getKieServerStatus() throws ApsSystemException;

    public JSONObject getAllCases(KieBpmConfig config, String container, String status) throws ApsSystemException;

    public String startTask(KieBpmConfig config, String payload, String container, String taskId) throws ApsSystemException;

    public String submitTask(KieBpmConfig config, String payload, String container, String taskId) throws ApsSystemException;

    public String completeTask(KieBpmConfig config, String payload, String container, String taskId) throws ApsSystemException;

    public JSONObject getTaskDetails(KieBpmConfig config, String taskId) throws ApsSystemException;

    public String runAdditionalInfoRules(KieBpmConfig config, String jsonBody, String instance) throws ApsSystemException;

    public String executeStartCase(KieBpmConfig config, String json, String container, String instance) throws ApsSystemException;

    public Set<String> getProcessVariables(KieBpmConfig config, String container, String processid) throws ApsSystemException;

    public Map<String, String> getProcessVariableInstances(KieBpmConfig config, String processInstanceIdd) throws ApsSystemException;

    public void setCasePathForChannel(String channel, String path);

    public String getCasePathForChannel(String channel);
}
