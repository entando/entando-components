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

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jprestapi.aps.core.Endpoint;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author own_strong
 */
public class CaseManager extends KieFormManager {

    private static final Logger logger = LoggerFactory.getLogger(KieFormManager.class);


    public JSONArray getKieServerStasus() throws ApsSystemException {
        
        //Save the current Config
        KieBpmConfig setKieBpmConfig = super.getConfig();
        
        Map<String, String> headersMap = new HashMap<>();

        JSONArray ServersStatus = new JSONArray();
        String result = null;
        JSONObject json = null;

        HashMap<String, KieBpmConfig> ServerConfigurations = super.getKieServerConfigurations();

        for (String key : ServerConfigurations.keySet()) {

            super.setConfig(ServerConfigurations.get(key));

            try {
                // process endpoint first
                Endpoint ep = KieEndpointDictionary.create().get(API_GET_SERVER_STATUS);
                // add header
                headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
                // generate client from the current configuration
                KieClient client = super.getCurrentClient();
                // perform query
                result = (String) new KieRequestBuilder(client)
                        .setEndpoint(ep)
                        .setHeaders(headersMap)
                        .setDebug(super.getConfig().getDebug())
                        .doRequest();

                if (!result.isEmpty()) {

                    json = new JSONObject(result);

                    JSONObject serverStatusJson = new JSONObject();
                    serverStatusJson.put("id", super.getConfig().getId());
                    serverStatusJson.put("status", json);

                    JSONObject serverConfJson = new JSONObject();

                    serverConfJson.put("active", super.getConfig().getActive());
                    serverConfJson.put("id", super.getConfig().getId());
                    serverConfJson.put("name", super.getConfig().getName());
                    serverConfJson.put("username", super.getConfig().getUsername());
                    serverConfJson.put("password", super.getConfig().getPassword());
                    serverConfJson.put("hostname", super.getConfig().getHostname());
                    serverConfJson.put("port", super.getConfig().getPort());
                    serverConfJson.put("schema", super.getConfig().getSchema());
                    serverConfJson.put("webapp", super.getConfig().getWebapp());
                    serverConfJson.put("timeoutMsec", super.getConfig().getTimeoutMsec());
                    serverConfJson.put("debug", super.getConfig().getDebug());

                    serverStatusJson.put("config", serverConfJson);

                    ServersStatus.put(serverStatusJson);

                    logger.debug("received successful message: ", result);
                } else {
                    logger.debug("received empty message: ");
                }

            } catch (Throwable t) {
                JSONObject serverStatusJson = new JSONObject();
                serverStatusJson.put("id", super.getConfig().getId());
                serverStatusJson.put("status", "null");

                JSONObject serverConfJson = new JSONObject();

                serverConfJson.put("active", super.getConfig().getActive());
                serverConfJson.put("id", super.getConfig().getId());
                serverConfJson.put("name", super.getConfig().getName());
                serverConfJson.put("username", super.getConfig().getUsername());
                serverConfJson.put("password", super.getConfig().getPassword());
                serverConfJson.put("hostname", super.getConfig().getHostname());
                serverConfJson.put("port", super.getConfig().getPort());
                serverConfJson.put("schema", super.getConfig().getSchema());
                serverConfJson.put("webapp", super.getConfig().getWebapp());
                serverConfJson.put("timeoutMsec", super.getConfig().getTimeoutMsec());
                serverConfJson.put("debug", super.getConfig().getDebug());

                serverStatusJson.put("config", serverConfJson);

                ServersStatus.put(serverStatusJson);
                logger.debug("Error connecting to the server: " + t);
            }
        }
        
        //load the current config
        super.setConfig(setKieBpmConfig);

        return ServersStatus;
    }

    public JSONObject getCasesDefinitions(String containerId) throws ApsSystemException {

        Map<String, String> headersMap = new HashMap<>();

        String result = null;
        JSONObject json = null;

        if (!super.getConfig().getActive() || StringUtils.isBlank(containerId)) {
            return json;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_CASES_DEFINITIONS).resolveParams(containerId);
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            // generate client from the current configuration
            KieClient client = super.getCurrentClient();
            // perform query
            result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setDebug(super.getConfig().getDebug())
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

    public List<String> getCaseInstancesList(String containerId) throws ApsSystemException {

        List<String> casesList = new ArrayList<>();
        Map<String, String> headersMap = new HashMap<>();

        String result;
        JSONObject json = null;

        if (!super.getConfig().getActive() || StringUtils.isBlank(containerId)) {
            return casesList;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_CASES_LIST).resolveParams(containerId);
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            // generate client from the current configuration
            KieClient client = super.getCurrentClient();
            // perform query
            result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setDebug(super.getConfig().getDebug())
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

    public JSONArray getMilestonesList(String containerId, String caseID) throws ApsSystemException {

        super.setConfig(super.getConfig());

        JSONArray milestonesList = null;
        Map<String, String> headersMap = new HashMap<>();
        Map<String, String> param = new HashMap<>();

        String result;
        JSONObject json;

        if (!super.getConfig().getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID)) {
            return milestonesList;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_MILESTONES_LIST).resolveParams(containerId, caseID);
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            param.put(HEADER_MILESTONES_ACHIEVEDONLY_PARM, HEADER_MILESTONES_ACHIEVEDONLY_FALSE);
            // generate client from the current configuration
            KieClient client = super.getCurrentClient();
            // perform query
            result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setHeaders(headersMap)
                    .setRequestParams(param)
                    .setDebug(super.getConfig().getDebug())
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

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

//    private KieBpmConfig config;
    private String containerId;
    private String caseID;

}
