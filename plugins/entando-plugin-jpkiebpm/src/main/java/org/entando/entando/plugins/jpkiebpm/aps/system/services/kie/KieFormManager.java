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

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiProcessStart;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.BpmToFormHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FormToBpmHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.*;
import org.entando.entando.plugins.jprestapi.aps.core.Endpoint;
import org.entando.entando.plugins.jprestapi.aps.core.RequestBuilder;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.*;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.CaseProgressWidgetHelpers.generateNewUUID;
//import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FSIDemoHelper;

/**
 * @author Entando
 */
public class KieFormManager extends AbstractService implements IKieFormManager {

    private static final Logger logger = LoggerFactory.getLogger(KieFormManager.class);
    private Map<String, String> hostNameVersionMap = new HashMap<>();

    @Override
    public void init() {
        // Looking config up instead of holding singleton
    }

    @Override
    public KieBpmConfig loadFirstConfigurations() throws ApsSystemException {
        try {
            String xml = this.getConfigManager().getConfigItem(KieBpmSystemConstants.KIE_BPM_CONFIG_ITEM);
            KiaBpmConfigFactory kBpmConfFctry = (KiaBpmConfigFactory) JAXBHelper.unmarshall(xml, KiaBpmConfigFactory.class, true, false);
            //get the first entry in database
            config = kBpmConfFctry.getFirstKiaBpmConfig();
            return config;
//            _config = (KieBpmConfig) JAXBHelper.unmarshall(xml, KieBpmConfig.class, true, false);
        } catch (Throwable t) {
            throw new ApsSystemException("Error in loadConfigurations", t);
        }
    }

    @Override
    public KieBpmConfig updateConfig(KieBpmConfig config) throws ApsSystemException {
        try {
            if (null != config) {
                String xml = JAXBHelper.marshall(config, true, false);
                this.getConfigManager().updateConfigItem(KieBpmSystemConstants.KIE_BPM_CONFIG_ITEM, xml);
                this.config = config;
            }
        } catch (Throwable t) {
            throw new ApsSystemException("Error updating configuration", t);
        }
        return config;
    }

    @Override
    public void addConfig(KieBpmConfig config) throws ApsSystemException {
        try {
            if (null != config) {
                if (StringUtils.isBlank(config.getId())) {
                    //generate a unique id by using UUID + current datetime
                    //and set unique value for the id
                    String uuid = generateNewUUID();
                    config.setId(uuid);
                }
                //Get current config from database
                String xmlin = this.getConfigManager().getConfigItem(KieBpmSystemConstants.KIE_BPM_CONFIG_ITEM);
                //add new config
                KiaBpmConfigFactory kBpmConfFctry = (KiaBpmConfigFactory) JAXBHelper.unmarshall(xmlin, KiaBpmConfigFactory.class, true, false);
                kBpmConfFctry.addKiaBpmConfig(config);
                String xml = JAXBHelper.marshall(kBpmConfFctry, true, false);
                //load updated config
                this.getConfigManager().updateConfigItem(KieBpmSystemConstants.KIE_BPM_CONFIG_ITEM, xml);
                this.config = config;
            }
        } catch (Throwable t) {
            throw new ApsSystemException("Error adding configuration", t);
        }
    }

    @Override
    public void deleteConfig(String kieId) throws ApsSystemException {
        try {
            if (!StringUtils.isBlank(kieId)) {
                //Get current config from database
                String xmlin = this.getConfigManager().getConfigItem(KieBpmSystemConstants.KIE_BPM_CONFIG_ITEM);
                //delete new config
                KiaBpmConfigFactory kBpmConfFctry = (KiaBpmConfigFactory) JAXBHelper.unmarshall(xmlin, KiaBpmConfigFactory.class, true, false);
                kBpmConfFctry.removeKiaBpmConfig(kieId);
                String xml = JAXBHelper.marshall(kBpmConfFctry, true, false);
                //load updated config
                this.getConfigManager().updateConfigItem(KieBpmSystemConstants.KIE_BPM_CONFIG_ITEM, xml);
            } else {
                logger.error("Blank Kie ID ", kieId);
            }
        } catch (Throwable t) {
            throw new ApsSystemException("Error deleteing configuration", t);
        }
    }

    @Override
    public HashMap<String, KieBpmConfig> getKieServerConfigurations() throws ApsSystemException {
        try {
            String xml = this.getConfigManager().getConfigItem(KieBpmSystemConstants.KIE_BPM_CONFIG_ITEM);

            if(xml ==null) {
                return new HashMap<String, KieBpmConfig>();
            }
            KiaBpmConfigFactory kBpmConfFctry = (KiaBpmConfigFactory) JAXBHelper.unmarshall(xml, KiaBpmConfigFactory.class, true, false);
            return kBpmConfFctry.getKieBpmConfigeMap();
        } catch (Throwable t) {
            throw new ApsSystemException("Error in getKieServerConfigurations", t);
        }
    }

    @Override
    public void setKieServerConfiguration(String kieId) throws ApsSystemException {
        try {
            config = this.getKieServerConfigurations().get(kieId);
        } catch (Throwable t) {
            throw new ApsSystemException("Error in setKieServerConfiguration", t);
        }
    }

    @Override
    public List<KieContainer> getContainersList() throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        List<KieContainer> list = new ArrayList<>();
        if (!config.getActive()) {
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
                    .setDebug(config.getDebug())
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
    public List<KieProcess> getProcessDefinitionsList() throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        List<KieProcess> list = new ArrayList<>();
        if (!config.getActive()) {
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
                    .setDebug(config.getDebug())
                    .doRequest(KieProcessesQueryResult.class);
            // unfold returned object to get the payload
            if (null != result && null != result.getProcesses() && !result.getProcesses().isEmpty()) {
                list = result.getProcesses();
                for (KieProcess process : list) {
                		process.setKieSourceId(config.getId());
                }
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
        if (!config.getActive() || StringUtils.isBlank(processId)) {
            return list;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_PROCESS_INSTANCES_LIST)
                    .resolveParams(processId, page, pageSize);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            // perform query
            KieProcessInstancesQueryResult result = (KieProcessInstancesQueryResult) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setDebug(config.getDebug())
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
    public List<KieTask> getHumanTaskListForUser(String user, Map<String, String> opt) throws ApsSystemException {
        if (null == opt
                && StringUtils.isNotBlank(user)) {
            opt = new HashMap<String, String>();
        }
        if (StringUtils.isNotBlank(user)) {
            opt.put("user", user);
        }
        return getHumanTaskList(null, opt);
    }

    @Override
    public List<KieTask> getHumanTaskList(String groups, Map<String, String> opt) throws ApsSystemException {
	    	logger.info("getHumanTaskList(groups: {}, opt: {}", groups, opt);
        Map<String, String> headersMap = new HashMap<>();
        List<KieTask> list = new ArrayList<>();
        if (!config.getActive()) {
            return list;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_HUMAN_TASK_LIST);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            
            // Configure query
            RequestBuilder requestBuilder = new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setRequestParams(opt)
                    .setDebug(config.getDebug());
            
            // Add groups to request
            if (groups != null && !"null".equalsIgnoreCase(groups)) {
            		Map<String,String> recurringParameters = new HashMap<String,String>();
            		recurringParameters.put("groups", groups);
            		((KieRequestBuilder)requestBuilder).setRecurringParameters(recurringParameters);
            }
                    
            // unfold returned object to get the payload
            
            KieTaskQueryResult result = (KieTaskQueryResult) requestBuilder.doRequest(KieTaskQueryResult.class);
            if (null != result && null != result.getList() && !result.getList().isEmpty()) {
                list = result.getList();
            }
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the list of human tasks", t);
        }
        return list;
    }

    @Override
    public List<KieTask> getHumanTaskListForAdmin(String user, Map<String, String> opt) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        List<KieTask> list = new ArrayList<>();
        if (!config.getActive()) {
            return list;
        }
        if (null == opt) {
            opt = new HashMap<>();
        }
        opt.put("user", user);
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_ALL_TASK_LIST_ADMIN);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            // perform query
            KieTaskQueryResult result = (KieTaskQueryResult) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setRequestParams(opt)
                    .setDebug(config.getDebug())
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
    public boolean getCompleteEnrichmentDcumentApprovalTask(final String user, final String containerId, final String taskId, TASK_STATES state, String review, Map<String, String> opt) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        if (null == opt) {
            opt = new HashMap<>();
        }
        opt.put("auto-progress", "true");
        opt.put("user", user);

        try {
            String payload = "";//FSIDemoHelper.getPayloadForCompleteEnrichmentDocumentApproval(review);
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_PUT_COMPLETE_ENRICHMENT_DOCUMENT_APPROVAL_TASK)
                    .resolveParams(containerId, taskId, state.getValue());
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            headersMap.put("X-KIE-ContentType", "JSON");
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);
            // perform query
            String result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setRequestParams(opt)
                    .setPayload(payload)
                    .setDebug(config.getDebug())
                    .doRequest();
        } catch (Throwable t) {
            logger.error("Error whole approving document for enrichment", t);
//            throw new ApsSystemException("Error whole approving document for enrichment", t);
            return false;
        }
        return true;
    }

    @Override
    public List<KieTask> getLegalWorkerTaskList(Map<String, String> opt) throws ApsSystemException {
        if (null == opt) {
            opt = new HashMap<>();
        }
        opt.put("user", LEGAL_WORKER);
        return getHumanTaskList(null, opt);
    }

    @Override
    public List<KieTask> getKnowledgeWorkerTaskList(Map<String, String> opt) throws ApsSystemException {
        if (null == opt) {
            opt = new HashMap<>();
        }
        opt.put("user", KNOWLEDGE_WORKER);
        return getHumanTaskList(null, opt);
    }

    @Override
    public KieTaskDetail getTaskDetail(final String containerId, final Long taskId, Map<String, String> opt) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        if (!config.getActive()) {
            return new KieTaskDetail();
        }
        try {
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_DATA_HUMAN_TASK_DETAIL)
                    .resolveParams(containerId, taskId);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            KieTaskDetail result = (KieTaskDetail) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setRequestParams(opt)
                    .setDebug(config.getDebug())
                    .doRequest(KieTaskDetail.class);
            // unfold returned object to get the payload
            if (null != result) {
                return result;
            }
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the list of human tasks", t);
        }
        return null;
    }

    @Override
    public String getProcInstDiagramImage(String containerId, String processId) throws ApsSystemException {
        String result = null;
        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(processId)) {
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
                    .setDebug(config.getDebug())
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
        if (!config.getActive()
                || StringUtils.isBlank(containerId)) {
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
                    .setDebug(config.getDebug())
                    .doRequest(KieProcessFormQueryResult.class);
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the human task form", t);
        }
        return form;
    }

    @Override
    public JSONObject getTaskFormData(String containerId, long taskId, Map<String, String> opt) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        JSONObject json = null;
        if (!this.getConfig().getActive() || StringUtils.isBlank(containerId)) {
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
                    .setRequestParams(opt)
                    .setDebug(config.getDebug())
                    .doRequest();
            json = new JSONObject(data);
        } catch (Throwable t) {
            logger.error("Failed to get task form data ",t);
            throw new ApsSystemException("Error getting the human task form", t);
        }
        return json;
    }
    
    @Override
    // This uses XML unmarshaling
    public KieProcessFormQueryResult getProcessForm(String containerId, String processId) throws ApsSystemException {
    		logger.info("invoking getProcessForm(containterId: {}, processId: {})", containerId, processId);

        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(processId)) {
            logger.warn("Config is not active or container id or process id is blank conf {} {} {} ", config.getActive(), containerId, processId);
            return null;
        }


        Object result = null;

        try {

            String ver = this.hostNameVersionMap.get(config.getId());
            logger.info("ver {} ",ver);

//            if(ver !=null  && ver.startsWith("6")) {
//                invokeSixProcessForm();
//            }else {
//                invokeSevenProcessForm();
//            }
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_PROCESS_DEFINITION).resolveParams(containerId, processId);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            result = (KieProcessFormQueryResult) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setDebug(config.getDebug())
                    .setUnmarshalOptions(false, true)
                    .doRequest(KieProcessFormQueryResult.class);
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the process forms", t);
        }

        KieProcessFormQueryResult result = null;
        // if this fails it must affect the overrides only!
        try {
            // load overrides
            List<KieFormOverride> overrides = overrideManager.getFormOverrides(containerId, processId);

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
        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(processId) || null == input || input.isEmpty()) {
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
                    .setDebug(config.getDebug())
                    .doRequest();
        } catch (Throwable t) {
            throw new ApsSystemException("Error starting the process", t);
        }
        return result;
    }

    @Override
    public String startNewProcess(KieApiProcessStart process, Map<String, Object> input) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        String result = null;
        if (!config.getActive() || StringUtils.isBlank(process.getContainerId())
                || StringUtils.isBlank(process.getProcessId()) || StringUtils.isBlank(process.getCorrelation())) {
            return null;
        }
        try {
        		KieProcessFormQueryResult form = getProcessForm(process.getContainerId(), process.getProcessId());
            String payload = FormToBpmHelper.generateFormJson(form, input, process.getContainerId(), process.getProcessId());
            logger.info("PAYLOAD CREATED: {}", payload);
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_POST_PROCESS_START)
                    .resolveParams(process.getContainerId(), process.getProcessId(), process.getCorrelation());
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);
            // perform query
            result = new KieRequestBuilder(client).setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setPayload(payload)
                    .setDebug(config.getDebug())
                    //.setTestMode(true)
                    .doRequest();
        } catch (Throwable t) {
            throw new ApsSystemException("Error starting the process", t);
        }
        return result;
    }

    @Override
    public String startNewProcess(String containerId, String processId, Map<String, Object> input) throws ApsSystemException {
        KieApiProcessStart process = new KieApiProcessStart();
        process.setContainerId(containerId);
        process.setProcessId(processId);
        process.setCorrelation(UUID.randomUUID().toString());
        
        /*try {
            // generate payload FIXME this should be dynamic
            process = FSIDemoHelper.createStartProcessPayload(containerId, processId, input);
        } catch (Throwable t) {
            throw new ApsSystemException("Error starting the process", t);
        }*/
        return this.startNewProcess(process, input);
    }

    @Override
    // FIXME use the new routines to generate the payload
    public String startProcessSubmittingForm(KieProcessFormQueryResult form, String containerId, String processId, Map<String, Object> input) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        String result = null;
        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(processId) || null == input || input.isEmpty()) {
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
                    .setDebug(config.getDebug())
                    .doRequest();
        } catch (Throwable t) {
            throw new ApsSystemException("Error starting the process", t);
        }
        return result;
    }

    /*
    private KieApiProcessStart convertDataFormToProcessForm(KieProcessFormQueryResult form, String containerId, String processId) {
        KieApiProcessStart process = new KieApiProcessStart();
        process.setContainerId(containerId);
        process.setProcessId(processId);
        for (KieProcessFormField field : form.getFields()) {
            if (field.getName().equals("accountManager")) {
                process.setAccountManager(field.getProperty("value").getValue());
            }
        }
        return process;
    }
     */
    @Override
    public String completeHumanFormTask(final String containerId, final String processId, final long taskId,
            final Map<String, String> input) throws ApsSystemException {
        String result = null;
        try {
            // get human task definition
            KieProcessFormQueryResult form = getTaskForm(containerId, taskId);
            // load related data
            JSONObject taskData = getTaskFormData(containerId, taskId, null);
            //final KieProcessFormQueryResult formProcess = getProcessForm(containerId, processId);
            input.put("containerId", containerId);
            input.put("processId", processId);
            input.put("taskId",String.valueOf(taskId));
            final Map<String, Object> inputValidated = FormToBpmHelper.validateForm(form, input);
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
            logger.error("Failed to complete kie task ",throwable);
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
            Endpoint ep = KieEndpointDictionary.create().get(API_PUT_HUMAN_TASK)
                    .resolveParams(containerId, taskId);
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);
            // execute
            result = new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setPayload(payload)
                    .setDebug(config.getDebug())
                    .doRequest();
        } catch (Throwable t) {
            throw new ApsSystemException("Error completing the task", t);
        }
        return result;
    }

    @Override
    public KieTask getHumanTask(String processId) throws ApsSystemException {
        KieTask task = null;
        //TODO pagination
        List<KieTask> tasks = this.getHumanTaskList(null, null);
        if (null != tasks && !tasks.isEmpty()) {
            for (KieTask elem : tasks) {
                if (elem.getProcessInstanceId().equals(Long.valueOf(processId))) {
                    task = elem;
                }
            }
        }
        return task;
    }

    @Override
    public boolean sendSignal(final String containerId, final String processId, final String signal, String accountId, Map<String, String> opt) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        if (!this.getConfig().getActive()
                || StringUtils.isBlank(containerId)
                || StringUtils.isBlank(processId)
                || StringUtils.isBlank(signal)
                || StringUtils.isBlank(accountId)) {
            return false;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_POST_SIGNAL)
                    .resolveParams(containerId, processId, signal);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);
            // perform query
            String result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setPayload(accountId)
                    .setRequestParams(opt)
                    .setDebug(config.getDebug())
                    .doRequest();
            return true;
        } catch (Throwable t) {
            throw new ApsSystemException("Error delivering signal", t);
        }
    }

    /**
     * Delete the desired process
     *
     * @param containerId
     * @param processId
     * @param opt
     * @throws ApsSystemException
     */
    @Override
    public void deleteProcess(final String containerId, final String processId, Map<String, String> opt) throws ApsSystemException {
        if (!this.getConfig().getActive()
                || StringUtils.isBlank(containerId)
                || StringUtils.isBlank(processId)) {
            return;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_DELETE_PROCESS)
                    .resolveParams(containerId, processId);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // do request
            String res = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setRequestParams(opt)
                    .setDebug(config.getDebug())
                    .doRequest();
        } catch (Throwable t) {
            throw new ApsSystemException("Error deleting process", t);
        }
    }

    @Override
    public List<KieProcessInstance> getAllProcessInstancesList(Map<String, String> opt) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        List<KieProcessInstance> list = new ArrayList<>();
        if (!this.getConfig().getActive()) {
            return null;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create()
                    .get(API_GET_ALL_PROCESS_INSTANCES_LIST);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // perform query
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            // perform query
            KieProcessInstancesQueryResult result = (KieProcessInstancesQueryResult) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setRequestParams(opt)
                    .setDebug(config.getDebug())
                    .doRequest(KieProcessInstancesQueryResult.class);
            // unfold returned object to get the payload
            if (null != result
                    && null != result.getInstances()
                    && !result.getInstances().isEmpty()) {
                list = result.getInstances();
            }
            return list;
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the instances of all processes", t);
        }
    }

    @Override
    public String submitHumanFormTask(final String containerId, final String taskId, final TASK_STATES state, Map<String, String> queryStringParam, Map<String, Object> input) throws Throwable {
        Map<String, String> headersMap = new HashMap<>();
        String result = null;

        if (!this.getConfig().getActive()
                || StringUtils.isBlank(taskId)
                || StringUtils.isBlank(containerId)
                || null == input
                || null == state) {
            return null;
        }
        KieProcessFormQueryResult form = getTaskForm(containerId, Long.parseLong(taskId));
        JSONObject task = getTaskFormData(containerId, Long.parseLong(taskId), null);
        String payload = FormToBpmHelper.generateHumanTaskFormJson(form, task, input); //FSIDemoHelper.getPayloadForCompleteEnrichDocument(input);
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_PUT_HUMAN_TASK_STATE)
                    .resolveParams(containerId, taskId, state.getValue());
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put("X-KIE-ContentType", "JSON");
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);
            // perform query
            result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setRequestParams(queryStringParam)
                    .setPayload(payload)
                    .setDebug(config.getDebug())
                    .doRequest();
        } catch (Throwable t) {
            throw new ApsSystemException("error submitting human task with state: " + state.getValue(), t);
        }
        return result;
    }

    @Override
    public String setTaskState(final String containerId, final String taskId, final TASK_STATES state, Map<String, Object> input, Map<String, String> opt) throws Throwable {
        Map<String, String> headersMap = new HashMap<>();
        String result = null;
        if (!this.getConfig().getActive()
                || StringUtils.isBlank(taskId)
                || StringUtils.isBlank(containerId)
                || null == state) {
            return null;
        }
        try {
            // process endpoint first
            if (opt == null) {
                opt = new HashMap<>();
            }
            opt.put("auto-progress", "true");
            Endpoint ep = KieEndpointDictionary.create().get(API_PUT_SET_TASK_STATE)
                    .resolveParams(containerId, taskId, state.getValue());
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put("X-KIE-ContentType", "JSON");
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);
            if (null != input) {
                // generate payload
                String payload = "";//FSIDemoHelper.getPayloadForAdditionalClientDetailTask(input);
                // perform query
                result = (String) new KieRequestBuilder(client)
                        .setEndpoint(ep)
                        .setHeaders(headersMap)
                        .setPayload(payload)
                        .setRequestParams(opt)
                        .setDebug(config.getDebug())
                        .doRequest();
            } else {
                result = (String) new KieRequestBuilder(client)
                        .setEndpoint(ep)
                        .setHeaders(headersMap)
                        .setRequestParams(opt)
                        .setDebug(config.getDebug())
                        .doRequest();
            }
        } catch (Throwable t) {
            throw new ApsSystemException("error submitting human task with state: " + state.getValue(), t);
        }
        return result;
    }

    @Override
    public KieProcessInstancesQueryResult getProcessInstancesWithClientData(Map<String, String> input, Map<String, String> opt) throws Throwable {
        Map<String, String> headersMap = new HashMap<>();
        KieProcessInstancesQueryResult result = new KieProcessInstancesQueryResult();
        if (!this.getConfig().getActive() //                || null == input
                ) {
            return null;
        }
        if (null == opt
                || opt.isEmpty()) {
            opt = new HashMap<>();
        }
        // add mandatory args
        if (!opt.containsKey("mapper")) {
            opt.put("mapper", "ClientOnboardingProcessInstancesWithCustomVariables");
        }
        try {
            String payoload = "";//FSIDemoHelper.getPayloadForProcessInstancesWithClient(input);
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create()
                    .get(API_POST_ALL_PROCESS_INSTANCES_W_CLIENT_DATA);
            // generate client from the current configuration
            KieClient client = getCurrentClient();
            // header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);
            headersMap.put("X-KIE-ContentType", "JSON");
            // perform query
//            result = (KieProcessInstancesQueryResult) new KieRequestBuilder(client)
//                    .setEndpoint(ep)
//                    .setHeaders(headersMap)
//                    .setPayload(payoload)
//                    .setRequestParams(opt)
//                    .setDebug(_config.getDebug())
//                    .doRequest(KieProcessInstancesQueryResult.class);
            String res = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setPayload(payoload)
                    .setRequestParams(opt)
                    .setDebug(config.getDebug())
                    .doRequest();
            // necessary as we cannot change the property name looked by JAXB
            res = res.replaceAll("process-instance-variables", "process_instance_variables");
            result = (KieProcessInstancesQueryResult) JAXBHelper
                    .unmarshall(res, KieProcessInstancesQueryResult.class, false, true);
        } catch (Throwable t) {
            throw new ApsSystemException("error getting process list with data ", t);
        }
        return result;
    }

    /**
     * Return a KIE CLient given the configuration
     *
     * @return
     */
    protected KieClient getCurrentClient() {
        KieClient client = null;
        if (null != config) {
            KIEAuthenticationCredentials credentials = new KIEAuthenticationCredentials(config.getUsername(), config.getPassword());
            client = new KieClient();
            client.setHostname(config.getHostname());
            client.setPort(config.getPort());
            client.setSchema(config.getSchema());
            client.setWebapp(config.getWebapp());
            client.setCredentials(credentials);
            client.setTimeoutMsec(config.getTimeoutMsec());
        }
        return client;
    }

    public ConfigInterface getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigInterface configManager) {
        this.configManager = configManager;
    }

    public IKieFormOverrideManager getOverrideManager() {
        return overrideManager;
    }

    public void setOverrideManager(IKieFormOverrideManager overrideManager) {
        this.overrideManager = overrideManager;
    }

    @Override
    public KieBpmConfig getConfig() {
        return config;
    }

    @Override
    public void setConfig(KieBpmConfig config) {
        this.config = config;
    }

    public KiaBpmConfigFactory getKiaBpmConfigFactory() {
        kiaBpmConfigFactory = new KiaBpmConfigFactory();
        return kiaBpmConfigFactory;
    }

    public void setKiaBpmConfigFactory(KiaBpmConfigFactory kiaBpmConfigFactory) {
        this.kiaBpmConfigFactory = kiaBpmConfigFactory;
    }

    public Map<String, String> getHostNameVersionMap(){
        return this.hostNameVersionMap;
    }

    private KiaBpmConfigFactory kiaBpmConfigFactory;
    protected KieBpmConfig config;
    private ConfigInterface configManager;
    private IKieFormOverrideManager overrideManager;

    public enum TASK_STATES {
        ACTIVATED("activated"),
        CLAIMED("claimed"),
        STARTED("started"),
        STOPPED("stopped"),
        COMPLETED("completed"),
        DELEGATED("delegated"),
        EXITED("exited"),
        FAILED("failed"),
        FORWARDED("forwarded"),
        RELEASED("released"),
        RESUMED("resumed"),
        SKIPPED("skipped"),
        SUSPENDED("suspended"),
        NOMINATED("nominated");

        //TODO pagination
        TASK_STATES(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        private final String value;
    }

    public final static String KNOWLEDGE_WORKER = "knowledgeWorker";
    public final static String LEGAL_WORKER = "legalWorker";
}
