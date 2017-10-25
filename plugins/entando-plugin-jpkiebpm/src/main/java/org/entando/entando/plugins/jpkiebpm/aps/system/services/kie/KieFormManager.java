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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.util.KieApiUtil;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.BpmToFormHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FormToBpmHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.JsonHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.*;
import org.entando.entando.plugins.jprestapi.aps.core.Endpoint;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;

import javax.xml.bind.JAXBContext;

import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.*;

/**
 * @author Entando
 */
public class KieFormManager extends AbstractService implements IKieFormManager {

    private static final Logger logger = LoggerFactory.getLogger(KieFormManager.class);

    @Override
    public void init() throws Exception {
        try {
            loadConfig();
            logger.info("{} ready, enabled: {}", this.getClass().getName(), _config.getActive());
        } catch (ApsSystemException t) {
            logger.error("{} Manager: Error on initialization", this.getClass().getName(), t);
        }
    }

    private void loadConfig() throws ApsSystemException {
        try {
            ConfigInterface configManager = this.getConfigManager();
            String xml = configManager.getConfigItem(KieBpmSystemConstants.KIE_BPM_CONFIG_ITEM);

            _config = (KieBpmConfig) JAXBHelper.unmarshall(xml, KieBpmConfig.class, true, false);
        } catch (Throwable t) {
            throw new ApsSystemException("Error in loadConfigs", t);
        }
    }

    @Override
    public KieBpmConfig updateConfig(KieBpmConfig config) throws ApsSystemException {
        try {
            if (null != config) {
                String xml = JAXBHelper.marshall(config, true, false);

                this.getConfigManager().updateConfigItem(KieBpmSystemConstants.KIE_BPM_CONFIG_ITEM, xml);
                this._config = config;
            }
        } catch (Throwable t) {
            throw new ApsSystemException("Error updating configuration", t);
        }
        return config;
    }

    @Override
    public List<KieContainer> getContainersList() throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        List<KieContainer> list = new ArrayList<>();

        if (!_config.getActive()) {
            return list;
        }
        try {
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            // perform query
            KieContainersQueryResult result = (KieContainersQueryResult) new KieRequestBuilder(client)
                    .setEndpoint(KieEndpointDictionary.create().get(API_GET_CONTAINERS_LIST))
                    .setHeaders(headersMap)
                    .doRequest(KieContainersQueryResult.class);
            // unfold returned object to get the payload
            if (result.getType().equals(SUCCESS)) {
                logger.debug("received successful message: ", result.getMsg());
                if (null != result.getContainers() && null != result.getContainers().getKieContainerList()) {
                    list = result.getContainers().getKieContainerList().getList();
                }
            }
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the list of KIE containers", t);
        }
        return list;
    }

    @Override
    public List<kieProcess> getProcessDefinitionsList() throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        List<kieProcess> list = new ArrayList<>();

        if (!_config.getActive()) {
            return list;
        }
        try {
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            // perform query
            KieProcessesQueryResult result = (KieProcessesQueryResult) new KieRequestBuilder(client)
                    .setEndpoint(KieEndpointDictionary.create().get(API_GET_PROCESS_DEFINITIONS_LIST))
                    .setHeaders(headersMap)
                    .doRequest(KieProcessesQueryResult.class);
            // unfold returned object to get the payload
            if (null != result && null != result.getProcesses() && !result.getProcesses().isEmpty()) {
                list = result.getProcesses();
            }
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the list of process definitions", t);
        }
        return list;
    }

    @Override
    public List<KieProcessInstance> getProcessInstancesList(String processId, int page, int pageSize) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        List<KieProcessInstance> list = new ArrayList<>();

        if (!_config.getActive() || StringUtils.isBlank(processId)) {
            return list;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_PROCESS_INSTANCES_LIST).resolveParams(processId, page, pageSize);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            // perform query
            KieProcessInstancesQueryResult result = (KieProcessInstancesQueryResult) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .doRequest(KieProcessInstancesQueryResult.class);
            // unfold returned object to get the payload
            if (null != result && null != result.getInstances() && !result.getInstances().isEmpty()) {
                list = result.getInstances();
            }
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the list of process definitions", t);
        }
        return list;
    }

    @Override
    public List<KieTask> getHumanTaskList(String groups, int page, int pageSize) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        List<KieTask> list = new ArrayList<>();

        if (!_config.getActive()) {
            return list;
        }
        try {
            if (pageSize == 0) {
                pageSize = 2000;
            }
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_HUMAN_TASK_LIST).resolveParams(page, pageSize);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            // perform query
            KieTaskQueryResult result = (KieTaskQueryResult) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setDebug(true)
                    .doRequest(KieTaskQueryResult.class);
            // unfold returned object to get the payload
            if (null != result && null != result.getList() && !result.getList().isEmpty()) {
                list = result.getList();
            }
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the list of human tasks", t);
        }
        return list;
    }


    @Override
    public KieTaskDetail getTaskDetail(final String containerId, final Long taskId) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        if (!_config.getActive()) {
            return new KieTaskDetail();
        }
        try {

            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_DATA_HUMAN_TASK_DETAIL).resolveParams(containerId, taskId);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            KieTaskDetail result = (KieTaskDetail) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setUnmarshalOptions(false, false)
                    .setDebug(true)
                    .doRequest(KieTaskDetail.class);
            // unfold returned object to get the payload
            if (null != result) {
                return result;
            }
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the list of human tasks", t);
        }
        return new KieTaskDetail();
    }


    @Override
    public String getProcInstDiagramImage(String containerId, String processId) throws ApsSystemException {
        String result = null;
        if (!_config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(processId)) {
            return result;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_PROCESS_DIAGRAM).resolveParams(containerId, processId);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            result = new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .doRequest();
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the process diagram", t);
        }
        return result;
    }

    @Override
    // TODO rename method to getHumanTaskForm or similar
    public KieProcessFormQueryResult getTaskForm(String containerId, long taskId) throws ApsSystemException {
        KieProcessFormQueryResult form = null;

        if (!_config.getActive() || StringUtils.isBlank(containerId)) {
            return form;
        }
        try {

            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_TASK_FORM_DEFINITION).resolveParams(containerId, taskId);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            form = (KieProcessFormQueryResult) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setUnmarshalOptions(false, true)
                    .setDebug(true)
                    .doRequest(KieProcessFormQueryResult.class);

        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the human task form", t);
        }
        return form;
    }

    @Override
    public JSONObject getTaskFormData(String containerId, long taskId) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        JSONObject json = null;

        if (!_config.getActive() || StringUtils.isBlank(containerId)) {
            return json;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_DATA_HUMAN_TASK).resolveParams(containerId, taskId);
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            String data = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setDebug(true)
                    .doRequest();
            json = new JSONObject(data);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new ApsSystemException("Error getting the human task form", t);
        }
        return json;
    }


    @Override
    // This uses XML unmarshaling
    public KieProcessFormQueryResult getProcessForm(String containerId, String processId) throws ApsSystemException {
        logger.info("containerId:{} #  processId {} ", containerId, processId);
        KieProcessFormQueryResult result = null;
        if (!_config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(processId)) {
            return result;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_PROCESS_DEFINITION).resolveParams(containerId, processId);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            result = (KieProcessFormQueryResult) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setDebug(true)
                    .setUnmarshalOptions(false, true)
                    .doRequest(KieProcessFormQueryResult.class);
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the process forms", t);
        }
        // if this fails it must affect the overrides only!
        try {
            // load overrides
            List<KieFormOverride> overrides = _overrideManager.getFormOverrides(containerId, processId);

            // meshup overrides
            BpmToFormHelper.appendOverridesToForm(result, overrides);
        } catch (Throwable t) {
            logger.error("error retrieving overrides for the form; they will be IGNORED!", t);
        }
        return result;
    }

    @Override
    public String startProcessSubmittingForm(String containerId, String processId, Map<String, Object> input) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        String result = null;

        if (!_config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(processId) || null == input || input.isEmpty()) {
            return null;
        }
        try {
            // load the original form definition
            KieProcessFormQueryResult form = getProcessForm(containerId, processId);
            // generate payload
            String payload = FormToBpmHelper.generateFormJson(form, input, containerId, processId);
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_POST_PROCESS_START).resolveParams(containerId, processId);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);
            // perform query
            result = new KieRequestBuilder(client).setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setPayload(payload)
                    //.setDebug(true)
                    //.setTestMode(true)
                    .doRequest();
        } catch (Throwable t) {
            throw new ApsSystemException("Error starting the process", t);
        }
        return result;
    }

    @Override
    // FIXME use the new routines to generate the payload
    public String startProcessSubmittingForm(KieProcessFormQueryResult form, String containerId, String processId, Map<String, Object> input) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        String result = null;

        if (!_config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(processId) || null == input || input.isEmpty()) {
            return null;
        }
        try {
            // generate payload
            String payload = FormToBpmHelper.generateFormJson(form, input, containerId, processId);
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_POST_PROCESS_START).resolveParams(containerId, processId);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);
            // perform query
            result = new KieRequestBuilder(client).setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setPayload(payload)
//                  .setDebug(true)
//                  .setTestMode(true)
                    .doRequest();
        } catch (Throwable t) {
            throw new ApsSystemException("Error starting the process", t);
        }
        return result;
    }

    @Override
    public String completeHumanFormTask(final String containerId, final long taskId, final Map<String, String> input) throws ApsSystemException {
        String result = null;
        logger.info("Method completeHumanFormTask");
        try {
            // get human task definition
            KieProcessFormQueryResult form = getTaskForm(containerId, taskId);
            // load related data
            JSONObject taskData = getTaskFormData(containerId, taskId);
            final KieProcessFormQueryResult formProcess = getProcessForm(containerId, "com.redhat.bpms.examples.mortgage.MortgageApplication");
            final Map<String, Object> inputValidated = FormToBpmHelper.validateForm(formProcess, input);
            final Map<String, Object> map = new HashMap<>();
            BpmToFormHelper.getHumanTaskFormData(form, taskData, map);
            map.putAll(inputValidated);
            final String brokerOverrideValue = input.get("brokerOverride") == null ? "" : input.get("brokerOverride");
            if (map.containsKey("brokerOverride")) {
                map.remove("brokerOverride");
            }
            map.put("brokerOverride", brokerOverrideValue);
            // finally
            completeHumanFormTask(containerId, taskId, form, taskData, map);

        } catch (ApsSystemException t) {
            throw new ApsSystemException("Error completing the task", t);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    @Override
    public String completeHumanFormTask(final String containerId, final long taskId, final KieProcessFormQueryResult form, final JSONObject task, final Map<String, Object> input)
            throws ApsSystemException {
        final Map<String, String> headersMap = new HashMap<>();
        String result = null;

        try {
            // generate the payload
            String payload = FormToBpmHelper.generateHumanTaskFormJson(form, task, input);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_PUT_HUMAN_TASK).resolveParams(containerId, taskId);
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);
            // execute
            result = new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setPayload(payload)
                    .setDebug(true)
                    .setTestMode(false)
                    .doRequest();
        } catch (Throwable t) {
            throw new ApsSystemException("Error completing the task", t);
        }
        return result;
    }

    /**
     * Return a KIE CLient given the configuration
     *
     * @return
     */
    private KieClient getCurrentClient() {
        KieClient client = null;

        if (null != _config) {
            KIEAuthenticationCredentials credentials = new KIEAuthenticationCredentials(_config.getUsername(), _config.getPassword());
            client = new KieClient();
            client.setHostname(_config.getHostname());
            client.setPort(_config.getPort());
            client.setSchema(_config.getSchema());
            client.setWebapp(_config.getWebapp());
            client.setCredentials(credentials);
        }
        return client;
    }

    public ConfigInterface getConfigManager() {
        return _configManager;
    }

    public void setConfigManager(ConfigInterface configManager) {
        this._configManager = configManager;
    }

    public IKieFormOverrideManager getOverrideManager() {
        return _overrideManager;
    }

    public void setOverrideManager(IKieFormOverrideManager overrideManager) {
        this._overrideManager = overrideManager;
    }

    @Override
    public KieBpmConfig getConfig() {
        return _config.clone();
    }

    private KieBpmConfig _config;
    private ConfigInterface _configManager;
    private IKieFormOverrideManager _overrideManager;

    @Override
    public KieTask getHumanTask(String processId) throws ApsSystemException {
        KieTask task = null;
        //TODO pagination
        List<KieTask> tasks = this.getHumanTaskList(null, 0, 5000);
        if (null != tasks && !tasks.isEmpty()) {
            for (KieTask elem : tasks) {
                if (elem.getProcessInstanceId().equals(Long.valueOf(processId))) {
                    task = elem;
                }
            }
        }
        return task;

    }

}
