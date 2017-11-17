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
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author Entando
 */
public interface IKieFormManager {

    String BEAN_NAME_ID = "jpkiebpmsManager";

    /**
     * Return service configuration
     *
     * @return
     */
    KieBpmConfig getConfig();

    /**
     * List KIE containers
     *
     * @return
     * @throws com.agiletec.aps.system.exception.ApsSystemException
     */
    List<KieContainer> getContainersList() throws ApsSystemException;

    /**
     * Update service configuration
     *
     * @param config
     * @return
     * @throws ApsSystemException
     */
    KieBpmConfig updateConfig(KieBpmConfig config) throws ApsSystemException;

    /**
     * Return the process definition list
     *
     * @return
     * @throws ApsSystemException
     */
    List<kieProcess> getProcessDefinitionsList() throws ApsSystemException;

    /**
     * Get the process instances give the process ID
     *
     * @param processId
     * @param page
     * @param pageSize
     * @return
     * @throws ApsSystemException
     */
    List<KieProcessInstance> getProcessInstancesList(String processId, int page, int pageSize) throws ApsSystemException;

    /**
     * Get the list of human task
     *
     * @param groups
     * @param opt
     * @return
     * @throws ApsSystemException
     */
    List<KieTask> getHumanTaskList(String groups, Map<String, String> opt) throws ApsSystemException;

    /**
     * Get the deiser human task form given container ID and form ID
     *
     * @param containerId
     * @param taskId
     * @return
     * @throws ApsSystemException
     */
    KieProcessFormQueryResult getTaskForm(String containerId, long taskId) throws ApsSystemException;

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
    KieProcessFormQueryResult getProcessForm(String containerId, String processId) throws ApsSystemException;

    /**
     * Submit a form and start the related process
     *
     * @param containerId
     * @param processId
     * @param input
     * @return
     * @throws ApsSystemException
     */
    String startProcessSubmittingForm(String containerId, String processId, Map<String, Object> input) throws ApsSystemException;

    String startProcessSubmittingForm(KieProcessFormQueryResult form, String containerId, String processId, Map<String, Object> input) throws ApsSystemException;

    /**
     * Submit a form and start the related process
     *
     * @param process
     * @param input
     * @return
     * @throws ApsSystemException
     */
    String startNewProcess(KieApiProcessStart process, Map<String, Object> input) throws ApsSystemException;

    String startNewProcess(String containerId, String processId, Map<String, Object> input) throws ApsSystemException;

    /**
     * Get the process diagram
     *
     * @param containerId
     * @param processId
     * @return
     * @throws ApsSystemException
     */
    String getProcInstDiagramImage(String containerId, String processId) throws ApsSystemException;

    /**
     * Get the data of the current task
     *
     * @param containerId
     * @param taskId
     * @return
     * @throws ApsSystemException
     */
    JSONObject getTaskFormData(String containerId, long taskId, Map<String, String> opt) throws ApsSystemException;

    /**
     * @param containerId
     * @param processId
     * @param taskId
     * @param input
     * @return
     * @throws ApsSystemException
     */
    String completeHumanFormTask(final String containerId, final String processId, final long taskId, final Map<String, String> input) throws ApsSystemException;

    /**
     * @param containerId
     * @param taskId
     * @param form
     * @param task
     * @param input
     * @return
     * @throws ApsSystemException
     */
    String completeHumanFormTask(final String containerId, final long taskId, final KieProcessFormQueryResult form, final JSONObject task, final Map<String, Object> input)
            throws ApsSystemException;

    /**
     * Get a specific human task FIXME check whether there is already a specific
     * call for this
     *
     * @param processId
     * @return
     * @throws ApsSystemException
     */
    KieTask getHumanTask(String processId) throws ApsSystemException;

    /**
     * @param containerId
     * @param taskId
     * @param opt
     * @return
     * @throws ApsSystemException
     */
    KieTaskDetail getTaskDetail(final String containerId, final Long taskId, Map<String, String> opt) throws ApsSystemException;

    /**
     * @param containerId
     * @param processId
     * @param signal
     * @param accountId
     * @param opt
     * @return
     * @throws ApsSystemException
     */
    boolean sendSignal(final String containerId, final String processId, final String signal, String accountId, Map<String, String> opt) throws ApsSystemException;

    /**
     * @param containerId
     * @param processId
     * @param opt
     * @throws ApsSystemException
     */
    void deleteProcess(final String containerId, final String processId, Map<String, String> opt) throws ApsSystemException;

    /**
     * @param opt
     * @return
     * @throws ApsSystemException
     */
    List<KieProcessInstance> getAllProcessInstancesList(Map<String, String> opt) throws ApsSystemException;

    /**
     * @param containerId
     * @param taskId
     * @param state
     * @param queryStringParam
     * @param input
     * @param opt
     * @param payload
     * @return
     * @throws Throwable
     */
    String submitHumanFormTask(final String containerId, final String taskId, final TASK_STATES state, Map<String, String> queryStringParam, Map<String, Object> input) throws Throwable;

    /**
     * @param containerId
     * @param taskId
     * @param state
     * @param opt
     * @return
     * @throws Throwable
     */
    String setTaskState(final String containerId, final String taskId, final TASK_STATES state, Map<String, String> opt) throws Throwable;


    /**
     *
     * @param user
     * @param opt
     * @return
     * @throws ApsSystemException
     */
    public List<KieTask> getHumanTaskListForUser(String user, Map<String, String> opt) throws ApsSystemException;

    public List<KieTask> getHumanTaskListForAdmin(String user, Map<String, String> opt) throws ApsSystemException;
}