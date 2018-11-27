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
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.util.KieApiUtil;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.BpmToFormHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.EnvironmentBasedConfigHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FormToBpmHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven.PamProcessQueryFormResult;
import org.entando.entando.plugins.jprestapi.aps.core.Endpoint;
import org.entando.entando.plugins.jprestapi.aps.core.RequestBuilder;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.*;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.CaseProgressWidgetHelpers.generateNewUUID;

@Service
public class KieFormManager extends AbstractService implements IKieFormManager {

    private static final Logger logger = LoggerFactory.getLogger(KieFormManager.class);
    private Map<String, String> hostNameVersionMap = new HashMap<>();

//    public final static String KNOWLEDGE_WORKER = "knowledgeWorker";
//    public final static String LEGAL_WORKER = "legalWorker";
//
    //TODO -- JPW
    //1. Refactor request pattern
    //
    //TODO JPW -- Remove?
    private ConfigInterface configManager;

    @Override
    public void init() {
        setupConfig();
    }

    @PostConstruct
    public void setupConfig() {
        //Initialize the config and set an active configuration. This prevents exceptions when first starting a PAM
        //enabled app. Without this the config has to be manually saved after restart. In the future we may not need
        //both calls but this will validate that the connections in the db are still valid.
        //
        //Known issue here is that if one config goes bad you get none of them. TODO
        try {
            KieBpmConfig fromEnvironment = EnvironmentBasedConfigHelper.fromEnvironment();
            if (fromEnvironment != null) {
                addConfig(fromEnvironment);
            }
        } catch (Exception e) {
            logger.error("Failed to initialize Kie server configuration. Service will start but configuration for Kie server will need to be updated and or re-saved ", e);
        }
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

                HashMap<String, KieBpmConfig> currentConfigs = getKieServerConfigurations();
                currentConfigs.values().contains(config);

                //Get current config from database
                String xmlin = this.getConfigManager().getConfigItem(KieBpmSystemConstants.KIE_BPM_CONFIG_ITEM);
                //add new config
                KiaBpmConfigFactory kBpmConfFctry = (KiaBpmConfigFactory) JAXBHelper.unmarshall(xmlin, KiaBpmConfigFactory.class, true, false);
                kBpmConfFctry.addKiaBpmConfig(config);
                String xml = JAXBHelper.marshall(kBpmConfFctry, true, false);
                //load updated config
                this.getConfigManager().updateConfigItem(KieBpmSystemConstants.KIE_BPM_CONFIG_ITEM, xml);

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

            if (xml == null) {
                return new HashMap<String, KieBpmConfig>();
            }
            KiaBpmConfigFactory kBpmConfFctry = (KiaBpmConfigFactory) JAXBHelper.unmarshall(xml, KiaBpmConfigFactory.class, true, false);
            return kBpmConfFctry.getKieBpmConfigeMap();
        } catch (Throwable t) {
            throw new ApsSystemException("Error in getKieServerConfigurations", t);
        }
    }

    @Override
    public List<KieContainer> getContainersList(KieBpmConfig config) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        List<KieContainer> list = new ArrayList<>();
        if (!config.getActive()) {
            return list;
        }
        try {
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public List<KieProcess> getProcessDefinitionsList(KieBpmConfig config) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        List<KieProcess> list = new ArrayList<>();
        if (!config.getActive()) {
            return list;
        }
        try {
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public List<KieProcessInstance> getProcessInstancesList(KieBpmConfig config, String processId, int page, int pageSize) throws ApsSystemException {
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
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public List<KieTask> getHumanTaskListForUser(KieBpmConfig config, String user, Map<String, String> opt) throws ApsSystemException {
        if (null == opt
                && StringUtils.isNotBlank(user)) {
            opt = new HashMap<String, String>();
        }
        if (StringUtils.isNotBlank(user)) {
            opt.put("user", user);
        }
        return getHumanTaskList(config, null, opt);
    }

    @Override
    public List<KieTask> getHumanTaskList(KieBpmConfig config, String groups, Map<String, String> opt) throws ApsSystemException {
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
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
                Map<String, String> recurringParameters = new HashMap<String, String>();
                recurringParameters.put("groups", groups);
                ((KieRequestBuilder) requestBuilder).setRecurringParameters(recurringParameters);
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
    public List<KieTask> getHumanTaskListForAdmin(KieBpmConfig config, String user, Map<String, String> opt) throws ApsSystemException {
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
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public boolean getCompleteEnrichmentDcumentApprovalTask(KieBpmConfig config, String user, String containerId, String taskId, TASK_STATES state, String review, Map<String, String> opt) throws ApsSystemException {
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
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public KieTaskDetail getTaskDetail(KieBpmConfig config, String containerId, final Long taskId, Map<String, String> opt) throws ApsSystemException {
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
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public String getProcInstDiagramImage(KieBpmConfig config, String containerId) throws ApsSystemException {
        return getProcInstDiagramImage(config,
                 containerId, 
                 null);
    }

    @Override
    public String getProcInstDiagramImage(KieBpmConfig config, String containerId,  String pInstanceId) throws ApsSystemException {
        String result = null;
        if (!config.getActive() || StringUtils.isBlank(containerId)) {
            return result;
        }
        try {
            String ver = this.hostNameVersionMap.get(config.getId());
            logger.info("server version {} ", ver);
            boolean versionSix = false ;
                    if  ((ver != null) && ver.startsWith("6")) {
                        versionSix=true;
                    }
            logger.debug("Is server Version Six {} ", versionSix);

            // process endpoint first
            String endPoint = "";

            if (versionSix) {
                endPoint = API_GET_PROCESS_DIAGRAM_BPM6;
            } else {
                if (pInstanceId != null) {
                    endPoint = API_GET_PROCESS_INSTANCE_DIAGRAM;
                } else {
                    endPoint = API_GET_PROCESS_DIAGRAM;
                }
            }

            Endpoint ep = KieEndpointDictionary.create().get(endPoint);
          
            if (versionSix) {
                ep.resolveParams(containerId, pInstanceId);
            } else {
                if (pInstanceId != null) {
                    ep.resolveParams(containerId, String.valueOf(pInstanceId));
                } else {
                    ep.resolveParams(containerId);
                }
            }

            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public KieProcessFormQueryResult getTaskForm(KieBpmConfig config, String containerId, long taskId) throws ApsSystemException {
        KieProcessFormQueryResult form = null;
        if (!config.getActive()
                || StringUtils.isBlank(containerId)) {
            return form;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_TASK_FORM_DEFINITION).resolveParams(containerId, taskId);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
            String ver = this.hostNameVersionMap.get(config.getId());
            logger.info("server version {} ", ver);
            boolean versionSix = ver != null && ver.startsWith("6");
            String pamSevenString = "";
            if (versionSix) {
                // perform query
                form = (KieProcessFormQueryResult) new KieRequestBuilder(client)
                        .setEndpoint(ep)
                        .setUnmarshalOptions(false, true)
                        .setDebug(config.getDebug())
                        .doRequest(KieProcessFormQueryResult.class);
            } else {

                //Fetch a string. PAM 7 returns bad xml missing wrapper data
                pamSevenString = new KieRequestBuilder(client)
                        .setEndpoint(ep)
                        .setDebug(config.getDebug())
                        .setUnmarshalOptions(false, true)
                        .doRequest();
            }

            //Turn the pam seven xml into the object the rest of the plugin expects
            KieProcessFormQueryResult queryResult = null;
            if (!versionSix) {

                //PAM 7 returns invalid XML. Add a wrapper to make it valid.
                pamSevenString = "<pamFormResult>" + pamSevenString + "</pamFormResult>";
                PamProcessQueryFormResult pamResult = (PamProcessQueryFormResult) JAXBHelper
                        .unmarshall(pamSevenString, PamProcessQueryFormResult.class, true, false);
                form = KieVersionTransformer.pamSevenFormToPamSix(pamResult);
            }

        } catch (Throwable t) {

            logger.error("Error getting the human task form ", t);
            throw new ApsSystemException("Error getting the human task form", t);
        }
        return form;
    }

    @Override
    public JSONObject getTaskFormData(KieBpmConfig config, String containerId, long taskId, Map<String, String> opt) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        JSONObject json = null;
        if (!config.getActive() || StringUtils.isBlank(containerId)) {
            return json;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_DATA_HUMAN_TASK).resolveParams(containerId, taskId);
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
            String data = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setRequestParams(opt)
                    .setDebug(config.getDebug())
                    .doRequest();
            json = new JSONObject(data);
        } catch (Throwable t) {
            logger.error("Failed to get task form data ", t);
            throw new ApsSystemException("Error getting the human task form", t);
        }
        return json;
    }

    @Override
    public KieProcessFormQueryResult getProcessForm(KieBpmConfig config, String containerId, String processId) throws ApsSystemException {
        logger.info("invoking getProcessForm(containterId: {}, processId: {})", containerId, processId);

        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(processId)) {
            logger.warn("Config is not active or container id or process id is blank conf {} - {} - {} - {} - {} ", config.getActive(), config.getHostname(), config.getName(), containerId, processId);
            return null;
        }

        KieProcessFormQueryResult result = null;
        String ver = this.hostNameVersionMap.get(config.getId());
        logger.info("server version {} ", ver);
        boolean versionSix = ver != null && ver.startsWith("6");
        String pamSevenString = "";

        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_PROCESS_DEFINITION).resolveParams(containerId, processId);
            // generate client from the current configuration

            KieClient client = KieApiUtil.getClientFromConfig(config);
            // perform query and use the auto unmarshalling of RequestBuilder
            if (versionSix) {

                result = (KieProcessFormQueryResult) new KieRequestBuilder(client)
                        .setEndpoint(ep)
                        .setDebug(config.getDebug())
                        .setUnmarshalOptions(false, true)
                        .doRequest(KieProcessFormQueryResult.class);

            } else {

                //Fetch a string. PAM 7 returns bad xml
                pamSevenString = new KieRequestBuilder(client)
                        .setEndpoint(ep)
                        .setDebug(config.getDebug())
                        .setUnmarshalOptions(false, true)
                        .doRequest();
            }

            //Turn the pam seven xml into the object the rest of the plugin expects
            KieProcessFormQueryResult queryResult = null;
            if (!versionSix) {

                //PAM 7 returns invalid XML. Add a wrapper to make it valid.
                pamSevenString = "<pamFormResult>" + pamSevenString + "</pamFormResult>";
                PamProcessQueryFormResult pamResult = (PamProcessQueryFormResult) JAXBHelper
                        .unmarshall(pamSevenString, PamProcessQueryFormResult.class, true, false);
                result = KieVersionTransformer.pamSevenFormToPamSix(pamResult);
            }
        } catch (Throwable t) {
            logger.error("Failed to invoke kie get process form", t);
            throw new ApsSystemException("Error getting the process forms", t);
        }

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
    public String startProcessSubmittingForm(KieBpmConfig config, String containerId, String processId, Map<String, Object> input) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        String result = null;
        if (StringUtils.isBlank(containerId) || StringUtils.isBlank(processId) || null == input || input.isEmpty()) {
            return null;
        }
        try {
            // load the original form definition
            KieProcessFormQueryResult form = getProcessForm(config, containerId, processId);
            // generate payload
            String payload = FormToBpmHelper.generateFormJson(form, input, containerId, processId);
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_POST_PROCESS_START).resolveParams(containerId, processId);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public String startNewProcess(KieBpmConfig config, KieApiProcessStart process, Map<String, Object> input) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        String result = null;
        if (StringUtils.isBlank(process.getContainerId())
                || StringUtils.isBlank(process.getProcessId()) || StringUtils.isBlank(process.getCorrelation())) {
            return null;
        }
        try {
            KieProcessFormQueryResult form = getProcessForm(config, process.getContainerId(), process.getProcessId());
            String payload = FormToBpmHelper.generateFormJson(form, input, process.getContainerId(), process.getProcessId());
            logger.info("PAYLOAD CREATED: {}", payload);
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_POST_PROCESS_START)
                    .resolveParams(process.getContainerId(), process.getProcessId(), process.getCorrelation());
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public String startNewProcess(KieBpmConfig config, String containerId, String processId, Map<String, Object> input) throws ApsSystemException {
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
        return this.startNewProcess(config, process, input);
    }

    @Override
    // FIXME use the new routines to generate the payload
    public String startProcessSubmittingForm(KieBpmConfig config, KieProcessFormQueryResult form, String containerId, String processId, Map<String, Object> input) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        String result;
        if (StringUtils.isBlank(containerId) || StringUtils.isBlank(processId) || null == input || input.isEmpty()) {
            return null;
        }
        try {
            // generate payload
            String payload = FormToBpmHelper.generateFormJson(form, input, containerId, processId);
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_POST_PROCESS_START).resolveParams(containerId, processId);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public String completeHumanFormTask(KieBpmConfig config, String containerId, String processId, final long taskId,
            final Map<String, String> input) throws ApsSystemException {
        String result = null;
        try {
            // get human task definition
            KieProcessFormQueryResult form = getTaskForm(config, containerId, taskId);
            // load related data
            JSONObject taskData = getTaskFormData(config, containerId, taskId, null);
            //final KieProcessFormQueryResult formProcess = getProcessForm(containerId, processId);
            input.put("containerId", containerId);
            input.put("processId", processId);
            input.put("taskId", String.valueOf(taskId));
            final Map<String, Object> inputValidated = FormToBpmHelper.validateForm(form, input);
            final Map<String, Object> map = new HashMap<>();
            BpmToFormHelper.getHumanTaskFormData(form, taskData, map);
            map.putAll(inputValidated);

            //Change the first letter of the key to lower case so that it matches the form response. This is getting capitalized
            //elsewhere because the same string is used for display in a form. To be refactored
            Map<String, Object> fixedKeys = new HashMap<>();
            for (Map.Entry entry : map.entrySet()) {
                fixedKeys.put(StringUtils.uncapitalize((String) entry.getKey()), entry.getValue());
            }
            completeHumanFormTask(config, containerId, taskId, form, taskData, fixedKeys);

        } catch (Throwable t) {
            logger.error("Failed to complete kie task ", t);
            throw new ApsSystemException("Error completing the task", t);
        }

        return result;
    }

    @Override
    public String completeHumanFormTask(KieBpmConfig config, String containerId, final long taskId, final KieProcessFormQueryResult form, final JSONObject task, final Map<String, Object> input)
            throws ApsSystemException {
        final Map<String, String> headersMap = new HashMap<>();
        String result;

        try {
            // generate the payload
            String payload = FormToBpmHelper.generateHumanTaskFormJson(form, task, input);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
            logger.error("failed to complete human task ", t);
            throw new ApsSystemException("Error completing the task", t);
        }
        return result;
    }

    @Override
    public KieTask getHumanTask(KieBpmConfig config, String processId) throws ApsSystemException {
        KieTask task = null;
        //TODO pagination
        List<KieTask> tasks = this.getHumanTaskList(config, null, null);
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
    public boolean sendSignal(KieBpmConfig config, String containerId, String processId, String signal, String accountId, Map<String, String> opt) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        if (StringUtils.isBlank(containerId)
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
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public void deleteProcess(KieBpmConfig config, String containerId, String processId, Map<String, String> opt) throws ApsSystemException {
        if (StringUtils.isBlank(containerId) || StringUtils.isBlank(processId)) {
            return;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_DELETE_PROCESS)
                    .resolveParams(containerId, processId);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public List<KieProcessInstance> getAllProcessInstancesList(KieBpmConfig config, Map<String, String> opt) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        List<KieProcessInstance> list = new ArrayList<>();
        if (config.getActive()) {
            return null;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create()
                    .get(API_GET_ALL_PROCESS_INSTANCES_LIST);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public String submitHumanFormTask(KieBpmConfig config, String containerId, String taskId, final TASK_STATES state, Map<String, String> queryStringParam, Map<String, Object> input) throws Throwable {
        Map<String, String> headersMap = new HashMap<>();
        String result = null;

        if (StringUtils.isBlank(taskId)
                || StringUtils.isBlank(containerId)
                || null == input
                || null == state) {
            return null;
        }
        KieProcessFormQueryResult form = getTaskForm(config, containerId, Long.parseLong(taskId));
        JSONObject task = getTaskFormData(config, containerId, Long.parseLong(taskId), null);
        String payload = FormToBpmHelper.generateHumanTaskFormJson(form, task, input); //FSIDemoHelper.getPayloadForCompleteEnrichDocument(input);
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_PUT_HUMAN_TASK_STATE)
                    .resolveParams(containerId, taskId, state.getValue());
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public String setTaskState(KieBpmConfig config, String containerId, String taskId, final TASK_STATES state, Map<String, Object> input, Map<String, String> opt) throws Throwable {
        Map<String, String> headersMap = new HashMap<>();
        String result;
        if (StringUtils.isBlank(taskId)
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
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
    public KieProcessInstancesQueryResult getProcessInstancesWithClientData(KieBpmConfig config, Map<String, String> input, Map<String, String> opt) throws Throwable {
        Map<String, String> headersMap = new HashMap<>();
        KieProcessInstancesQueryResult result = new KieProcessInstancesQueryResult();

        if (null == opt || opt.isEmpty()) {
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
            KieClient client = KieApiUtil.getClientFromConfig(config);
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

//    public ConfigInterface getConfigManager() {
//        return configManager;
//    }
//
//    public void setConfigManager(ConfigInterface configManager) {
//        this.configManager = configManager;
//    }
    public IKieFormOverrideManager getOverrideManager() {
        return overrideManager;
    }

    public void setOverrideManager(IKieFormOverrideManager overrideManager) {
        this.overrideManager = overrideManager;
    }

//    @Override
//    public KieBpmConfig getConfig() {
//        return config;
//    }
//
//    @Override
//    public void setConfig(KieBpmConfig config) {
//        this.config = config;
//    }
//
//    public KiaBpmConfigFactory getKiaBpmConfigFactory() {
//        kiaBpmConfigFactory = new KiaBpmConfigFactory();
//        return kiaBpmConfigFactory;
//    }
//
//    public void setKiaBpmConfigFactory(KiaBpmConfigFactory kiaBpmConfigFactory) {
//        this.kiaBpmConfigFactory = kiaBpmConfigFactory;
//    }
    public Map<String, String> getHostNameVersionMap() {
        return this.hostNameVersionMap;
    }

    @Override
    public JSONArray getKieServerStatus() throws ApsSystemException {

        Map<String, String> headersMap = new HashMap<>();
        JSONArray serversStatus = new JSONArray();
        String result = null;
        JSONObject json = null;
        HashMap<String, KieBpmConfig> serverConfigurations = this.getKieServerConfigurations();
        for (String key : serverConfigurations.keySet()) {

            KieBpmConfig config = serverConfigurations.get(key);
            try {
                // process endpoint first
                Endpoint ep = KieEndpointDictionary.create().get(API_GET_SERVER_STATUS);
                // add header
                headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
                // generate client from the current configuration
                KieClient client = KieApiUtil.getClientFromConfig(config);
                // perform query
                result = (String) new KieRequestBuilder(client)
                        .setEndpoint(ep)
                        .setHeaders(headersMap)
                        .setDebug(config.getDebug())
                        .doRequest();
                if (!result.isEmpty()) {
                    json = new JSONObject(result);
                    JSONObject serverStatusJson = new JSONObject();
                    serverStatusJson.put("id", config.getId());
                    serverStatusJson.put("status", json);
                    JSONObject serverConfJson = new JSONObject();
                    serverConfJson.put("active", config.getActive());
                    serverConfJson.put("id", config.getId());
                    serverConfJson.put("name", config.getName());
                    serverConfJson.put("username", config.getUsername());
                    serverConfJson.put("password", config.getPassword());
                    serverConfJson.put("hostname", config.getHostname());
                    serverConfJson.put("port", config.getPort());
                    serverConfJson.put("schema", config.getSchema());
                    serverConfJson.put("webapp", config.getWebapp());
                    serverConfJson.put("timeoutMsec", config.getTimeoutMsec());
                    serverConfJson.put("debug", config.getDebug());

                    JSONObject resulObj = new JSONObject(result).getJSONObject("result");
                    JSONObject info = resulObj.getJSONObject("kie-server-info");
                    String version = info.getString("version");
                    serverConfJson.put("version", version);
                    this.hostNameVersionMap.put(config.getId(), version);
                    serverStatusJson.put("config", serverConfJson);
                    serversStatus.put(serverStatusJson);
                    logger.debug("received successful message: ", result);
                } else {
                    logger.debug("received empty message: ");
                }
            } catch (Throwable t) {
                JSONObject serverStatusJson = new JSONObject();
                serverStatusJson.put("id", config.getId());
                serverStatusJson.put("status", "null");

                JSONObject serverConfJson = new JSONObject();

                serverConfJson.put("active", config.getActive());
                serverConfJson.put("id", config.getId());
                serverConfJson.put("name", config.getName());
                serverConfJson.put("username", config.getUsername());
                serverConfJson.put("password", config.getPassword());
                serverConfJson.put("hostname", config.getHostname());
                serverConfJson.put("port", config.getPort());
                serverConfJson.put("schema", config.getSchema());
                serverConfJson.put("webapp", config.getWebapp());
                serverConfJson.put("timeoutMsec", config.getTimeoutMsec());
                serverConfJson.put("debug", config.getDebug());

                serverStatusJson.put("config", serverConfJson);

                serversStatus.put(serverStatusJson);
                logger.debug("Error connecting to the server: " + t);
            }
        }

        return serversStatus;
    }

    @Override
    public String startTask(KieBpmConfig config, String payload, String container, String taskId) throws ApsSystemException {

        logger.info("startTask(container: {}, taskId: {}", container, taskId);
        Map<String, String> headersMap = new HashMap<>();
        String result = null;
        if (!config.getActive()) {
            return result;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_PUT_HUMAN_TASK_START).resolveParams(container, taskId);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
            throw new ApsSystemException("Error task start", t);
        }

        return result;
    }

    @Override
    public String submitTask(KieBpmConfig config, String payload, String container, String taskId) throws ApsSystemException {

        logger.info("submitTask(container: {}, taskId: {}", container, taskId);
        Map<String, String> headersMap = new HashMap<>();
        String result = null;
        if (!config.getActive()) {
            return result;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_PUT_HUMAN_TASK_OUTPUT).resolveParams(container, taskId);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
            throw new ApsSystemException("Error task submit", t);
        }

        return result;
    }

    @Override
    public String completeTask(KieBpmConfig config, String payload, String container, String taskId) throws ApsSystemException {

        logger.info("completeTask(container: {}, taskId: {}", container, taskId);
        Map<String, String> headersMap = new HashMap<>();
        String result = null;
        if (!config.getActive()) {
            return result;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_PUT_HUMAN_TASK_COMPLETE).resolveParams(container, taskId);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
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
            throw new ApsSystemException("Error task complete", t);
        }

        return result;
    }

    @Override
    public JSONObject getTaskDetails(KieBpmConfig config, String taskId) throws ApsSystemException {
        
        
        logger.info("---------------------------------------------");

        logger.info("getTaskDetails (config: {}", config.toString());
        logger.info("getTaskDetails taskId: {}", taskId);
        Map<String, String> headersMap = new HashMap<>();
        JSONObject result;
        if (!config.getActive()) {
            return null;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_HUMAN_TASK_DETAILS).resolveParams(taskId);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            // perform query
            String kieResponse = new KieRequestBuilder(client).setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setDebug(config.getDebug())
                    .doRequest();
            result = new JSONObject(kieResponse);

        } catch (Throwable t) {
            throw new ApsSystemException("Error task complete", t);
        }

        return result;
    }

    @Override
    public JSONObject getAllCases(KieBpmConfig config, String containerId, String status) throws ApsSystemException {
        HashMap headersMap = new HashMap();
        Map<String, String> queryStringParam = new HashMap<>();

        String result = null;
        JSONObject json = null;

        logger.debug("kieFormManager getAllCases called ");
        try {

            if (status != null) {
                queryStringParam.put("status", status);
            }

            Endpoint t = ((Endpoint) KieEndpointDictionary.create().get(KieBpmSystemConstants.API_GET_CASES_LIST)).resolveParams(containerId);
            headersMap.put("Accept", "application/json");
            KieClient client = KieApiUtil.getClientFromConfig(config);
            result
                    = (new KieRequestBuilder(client))
                            .setEndpoint(t)
                            .setHeaders(headersMap)
                            .setRequestParams(queryStringParam)
                            .setDebug(config.getDebug()
                                    .booleanValue())
                            .doRequest();

            if (!result.isEmpty()) {
                json = new JSONObject(result);
                logger.debug("received successful message: ", result);

                List<Map<String, Object>> cases = (List<Map<String, Object>>) json.toMap().get("instances");

                List<Map<String, Object>> updatedCases = new ArrayList<>();
                for (Map<String, Object> caseMap : cases) {

                    String caseId = (String) caseMap.get("case-id");
                    JSONObject details = getCaseDetails(config, containerId, caseId);
                    caseMap.put("case-details", details);
                    updatedCases.add(caseMap);
                }

                json.put("instances", updatedCases);
            } else {
                logger.debug("received empty case definitions message: ");
            }

            return json;
        } catch (Throwable t) {
            logger.error("Failed to fetch cases ", t);
            throw new ApsSystemException("Error getting the cases definitions", t);
        }
    }

    @Override
    public String runAdditionalInfoRules(KieBpmConfig config, String jsonBody, String container) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        String result = null;

        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_POST_RUN_ADDITIONAL_INFO_RULES).resolveParams(container);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);
            // perform query
            result = new KieRequestBuilder(client).setEndpoint(ep)
                    .setHeaders(headersMap).setPayload(jsonBody).setDebug(config.getDebug()).doRequest();
        } catch (Throwable t) {
            throw new ApsSystemException("Error running additional file system", t);
        }
        return result;
    }

    @Override
    public String executeStartCase(KieBpmConfig config, String json, String container, String instance) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        String result = null;

        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_POST_START_CASE).resolveParams(container, instance);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);
            // perform query
            result = new KieRequestBuilder(client).setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setPayload(json)
                    .setDebug(config.getDebug())
                    .doRequest();
        } catch (Throwable t) {
            logger.error("Failed to start case ", t);
            throw new ApsSystemException("Error starting case", t);
        }
        return result;
    }

    public ConfigInterface getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigInterface configManager) {
        this.configManager = configManager;
    }

    public JSONObject getCaseDetails(KieBpmConfig config, String containerId, String caseId) throws ApsSystemException {

        HashMap headersMap = new HashMap();
        String result = null;
        JSONObject json = null;
        try {
            Endpoint t = ((Endpoint) KieEndpointDictionary.create().get(KieBpmSystemConstants.API_GET_CASE_FILE)).resolveParams(containerId, caseId);
            headersMap.put("Accept", "application/json");
            KieClient client = KieApiUtil.getClientFromConfig(config);
            result = (new KieRequestBuilder(client)).setEndpoint(t).setHeaders(headersMap).setDebug(config.getDebug().booleanValue()).doRequest();
            if (!result.isEmpty()) {
                json = new JSONObject(result);
                logger.debug("received successful message: ", result);
            } else {
                logger.debug("received empty case definitions message: ");
            }

            return json;
        } catch (Throwable t) {
            logger.error("Failed to fetch case details ", t);
            throw new ApsSystemException("Error getting the cases definitions", t);
        }
    }

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

        private String value;
    }

}
