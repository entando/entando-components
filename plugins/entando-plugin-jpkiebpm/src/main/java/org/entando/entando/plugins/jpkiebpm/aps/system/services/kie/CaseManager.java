/*
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
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
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.util.KieApiUtil;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jprestapi.aps.core.Endpoint;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.*;


public class CaseManager extends AbstractService implements ICaseManager {

    private static final Logger logger = LoggerFactory.getLogger(CaseManager.class);

    private Map<String, String> casePathChannel = new HashMap<>();


    @Override
    public void init() throws Exception {

    }

    @Override
    public JSONObject getCasesDefinitions(KieBpmConfig config, String containerId) throws ApsSystemException {

        Map<String, String> headersMap = new HashMap<>();

        String result = null;
        JSONObject json = null;

        if (!config.getActive() || StringUtils.isBlank(containerId)) {
            return json;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_CASES_DEFINITIONS).resolveParams(containerId);
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
                logger.debug("received successful message: ", result);
            } else {
                logger.debug("received empty case definitions message: ");
            }

        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the cases definitions", t);
        }

        return json;
    }

    @Override
    public List<String> getCaseInstancesList(KieBpmConfig config, String containerId) throws ApsSystemException {

        List<String> casesList = new ArrayList<>();
        Map<String, String> headersMap = new HashMap<>();

        String result;
        JSONObject json = null;

        if (!config.getActive() || StringUtils.isBlank(containerId)) {
            return casesList;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_CASES_LIST).resolveParams(containerId);
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
                JSONArray instances = (JSONArray) json.get("instances");

                for (int i = 0; i < instances.length(); i++) {
                    JSONObject iJson = instances.getJSONObject(i);
                    casesList.add(iJson.getString("case-id"));
                }
                logger.debug("received successful message: ", result);
            } else {
                logger.debug("received empty case instances message: ");
            }

        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the cases instances list", t);
        }

        return casesList;
    }

    @Override
    public JSONObject getCaseInstancesDetails(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException {

        Map<String, String> headersMap = new HashMap<>();
        Map<String, String> param = new HashMap<>();

        String result;
        JSONObject casesDetails = null;

        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID)) {
            return casesDetails;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_CASES_DETAILS).resolveParams(containerId, caseID);
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            param.put(HEADER_CASE_DETAILS_WITHDATA_PARM, HEADER_TRUE);
            param.put(HEADER_CASE_DETAILS_WITHROLES_PARM, HEADER_TRUE);
            param.put(HEADER_CASE_DETAILS_WITHMILESTONES_PARM, HEADER_TRUE);
            param.put(HEADER_CASE_DETAILS_WITHSTAGES_PARM, HEADER_TRUE);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
            // perform query
            result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setRequestParams(param)
                    .setDebug(config.getDebug())
                    .doRequest();

            if (!result.isEmpty()) {
                casesDetails = new JSONObject(result);
                logger.debug("received successful message: ", result);
            } else {
                logger.debug("received empty case instance message: ");
            }

        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the cases instance details", t);
        }

        return casesDetails;
    }

    @Override
    public JSONArray getMilestonesList(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException {

        JSONArray milestonesList = null;
        Map<String, String> headersMap = new HashMap<>();
        Map<String, String> param = new HashMap<>();

        String result;
        JSONObject json;

        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID)) {
            return milestonesList;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_MILESTONES_LIST).resolveParams(containerId, caseID);
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            param.put(HEADER_MILESTONES_ACHIEVEDONLY_PARM, HEADER_FALSE);
            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);
            // perform query
            result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setRequestParams(param)
                    .setDebug(config.getDebug())
                    .doRequest();

            if (!result.isEmpty()) {
                json = new JSONObject(result);
                milestonesList = (JSONArray) json.get("milestones");
                logger.debug("received successful message: ", result);

            } else {
                logger.debug("received empty case instances message: ");
            }

        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the cases list", t);
        }

        return milestonesList;
    }

    @Override
    public JSONArray getCaseComments(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException {

        JSONArray commentsList = null;
        JSONObject json;
        Map<String, String> headersMap = new HashMap<>();

        String result;

        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID)) {
            return commentsList;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_COMMENTS_LIST).resolveParams(containerId, caseID);
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
                commentsList = (JSONArray) json.get("comments");
                logger.debug("received successful message: ", result);

            } else {
                logger.debug("received empty case instances message: ");
            }

        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the cases list", t);
        }
        return commentsList;
    }

    @Override
    public boolean postCaseComments(KieBpmConfig config, String containerId, String caseID, String comment) throws ApsSystemException {

        Map<String, String> headersMap = new HashMap<>();

        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID)) {
            return false;
        }

        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_POST_COMMENTS).resolveParams(containerId, caseID);
            // add header

            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);

            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);

            // perform query
            String result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setPayload("\"" + comment + "\"")
                    .setDebug(config.getDebug())
                    .doRequest();

            return true;

        } catch (Throwable t) {
            throw new ApsSystemException("Error posting comments", t);
        }
    }

    @Override
    public boolean updateCaseComments(KieBpmConfig config, String containerId, String caseID, String caseCommentId, String comment) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();

        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID) || StringUtils.isBlank(caseCommentId) || StringUtils.isBlank(comment)) {
            return false;
        }

        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_PUT_UPDATE_COMMENTS).resolveParams(containerId, caseID, caseCommentId);
            // add header

            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);

            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);

            // perform query
            String result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setPayload("\"" + comment + "\"")
                    .setDebug(config.getDebug())
                    .doRequest();

            return true;

        } catch (Throwable t) {
            throw new ApsSystemException("Error posting comments", t);
        }
    }

    @Override
    public boolean deleteCaseComments(KieBpmConfig config, String containerId, String caseID, String caseCommentId) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();

        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID) || StringUtils.isBlank(caseCommentId)) {
            return false;
        }

        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_DELETE_COMMENTS).resolveParams(containerId, caseID, caseCommentId);
            // add header

            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);

            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);

            // perform query
            String result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setDebug(config.getDebug())
                    .doRequest();

            return true;

        } catch (Throwable t) {
            throw new ApsSystemException("Error posting comments", t);
        }
    }

    @Override
    public JSONObject getCaseRoles(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException {

        JSONObject caseRoles = null;
        Map<String, String> headersMap = new HashMap<>();

        String result;

        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID)) {
            return caseRoles;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_ROLE).resolveParams(containerId, caseID);
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
                caseRoles = new JSONObject(result);

                logger.debug("received successful message: ", result);

            } else {
                logger.debug("received empty case instances message: ");
            }

        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the cases roles", t);
        }
        return caseRoles;

    }

    @Override
    public boolean addCaseRoles(KieBpmConfig config, String containerId, String caseID, String caseRoleName, String user, String group) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        Map<String, String> requestParams = new HashMap<>();

        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID) || StringUtils.isBlank(caseRoleName) || StringUtils.isBlank(user) || StringUtils.isBlank(group)) {
            return false;
        }

        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_PUT_ROLE).resolveParams(containerId, caseID, caseRoleName);
            // add header

            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);

            requestParams.put(HEADER_CASE_ROLES_USER_PARM, user);
            requestParams.put(HEADER_CASE_ROLES_GROUP_PARM, group);

            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);

            // perform query
            String result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setRequestParams(requestParams)
                    .setDebug(config.getDebug())
                    .doRequest();

            return true;

        } catch (Throwable t) {
            throw new ApsSystemException("Error putting role", t);
        }

    }

    @Override
    public boolean deleteCaseRoles(KieBpmConfig config, String containerId, String caseID, String caseRoleName, String user, String group) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        Map<String, String> requestParams = new HashMap<>();

        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID) || StringUtils.isBlank(caseRoleName) || StringUtils.isBlank(user) || StringUtils.isBlank(group)) {
            return false;
        }

        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_DELETE_ROLE).resolveParams(containerId, caseID, caseRoleName);
            // add header

            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);

            requestParams.put(HEADER_CASE_ROLES_USER_PARM, user);
            requestParams.put(HEADER_CASE_ROLES_GROUP_PARM, group);

            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);

            // perform query
            String result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setRequestParams(requestParams)
                    .setDebug(config.getDebug())
                    .doRequest();

            return true;

        } catch (Throwable t) {
            throw new ApsSystemException("Error deleting role", t);
        }
    }

    @Override
    public JSONObject getCaseFile(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException {

        JSONObject caseFile = null;
        Map<String, String> headersMap = new HashMap<>();

        String result;

        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID)) {
            return caseFile;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_CASE_FILE).resolveParams(containerId, caseID);
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
                caseFile = new JSONObject(result);

                logger.debug("received successful message: ", result);

            } else {
                logger.debug("received empty case file message: ");
            }

        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the cases roles", t);
        }
        return caseFile;

    }

    @Override
    public boolean postCaseFile(KieBpmConfig config, String containerId, String caseID, String data) throws ApsSystemException {

        Map<String, String> headersMap = new HashMap<>();

        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID) || StringUtils.isBlank(data)) {
            return false;
        }

        try {
            JSONObject datajs = new JSONObject(data);

            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_POST_CASE_FILE).resolveParams(containerId, caseID);
            // add header

            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            headersMap.put(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);

            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);

            // perform query
            String result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setPayload("" + datajs + "")
                    .setDebug(config.getDebug())
                    .doRequest();

            return true;

        } catch (Throwable t) {
            throw new ApsSystemException("Error posting data into case file", t);
        }

    }

    @Override
    public boolean deleteCaseFile(KieBpmConfig config, String containerId, String caseID, String dataId) throws ApsSystemException {

        Map<String, String> headersMap = new HashMap<>();
        Map<String, String> requestParams = new HashMap<>();

        if (!config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID) || StringUtils.isBlank(dataId)) {
            return false;
        }

        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_DELETE_CASE_FILE).resolveParams(containerId, caseID);
            // add header

            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            requestParams.put(HEADER_CASE_FILE_DATA_ID_PARM, dataId);

            // generate client from the current configuration
            KieClient client = KieApiUtil.getClientFromConfig(config);

            // perform query
            String result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setRequestParams(requestParams)
                    .setDebug(config.getDebug())
                    .doRequest();

            return true;

        } catch (Throwable t) {
            throw new ApsSystemException("Error deleting data", t);
        }

    }

    @Override
    public JSONObject getProcessInstanceByCorrelationKey(KieBpmConfig config, String correlationKey) throws ApsSystemException {

        JSONObject processInstance = null;
        Map<String, String> headersMap = new HashMap<>();

        String result;

        if (!config.getActive() || StringUtils.isBlank(correlationKey)) {
            return processInstance;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_PROCESS_INSTANCE).resolveParams(correlationKey);
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
                processInstance = new JSONObject(result);

                logger.debug("received successful message: ", result);

            } else {
                logger.debug("received empty process instance message: ");
            }

        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the cases roles", t);
        }
        return processInstance;

    }
}
