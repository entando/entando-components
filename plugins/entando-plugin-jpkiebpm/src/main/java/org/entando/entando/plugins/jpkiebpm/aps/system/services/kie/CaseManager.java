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

    private static final Logger _logger = LoggerFactory.getLogger(KieFormManager.class);

    @Override
    public void init() throws Exception {
        super.init();
        this.setKieBpmConfig(super.getConfig());
    }

    public JSONObject getCasesDefinitions(String containerId) throws ApsSystemException {

        Map<String, String> headersMap = new HashMap<>();

        String result = null;
        JSONObject json = null;

        if (!_config.getActive() || StringUtils.isBlank(containerId)) {
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
                    .setDebug(_config.getDebug())
                    .doRequest();

            json = new JSONObject(result);

        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the cases list", t);
        }

        return json;
    }

    public List<String> getCasesList(String containerId) throws ApsSystemException {

        List<String> casesList = new ArrayList<>();
        Map<String, String> headersMap = new HashMap<>();

        String result = null;
        JSONObject json = null;

        if (!_config.getActive() || StringUtils.isBlank(containerId)) {
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
                    .setDebug(_config.getDebug())
                    .doRequest();

            json = new JSONObject(result);

            JSONArray instances = (JSONArray) json.get("instances");

            for (int i = 0; i < instances.length(); i++) {
                JSONObject iJson = instances.getJSONObject(i);
                casesList.add(iJson.getString("case-id"));

            }

        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the cases list", t);
        }

        return casesList;
    }

    public JSONArray getMilestonesList(String containerId, String caseID) throws ApsSystemException {

        JSONArray milestonesList = null;
        Map<String, String> headersMap = new HashMap<>();
        Map<String, String> param = new HashMap<>();

        String result = null;
        JSONObject json = null;

        if (!_config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID)) {
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
                    .setDebug(_config.getDebug())
                    .doRequest();

            json = new JSONObject(result);

            milestonesList = (JSONArray) json.get("milestones");

        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the cases list", t);
        }

        return milestonesList;
    }

    ////Helpers
    public List<String> getCaseListFromCaseDefinitions(JSONObject caseDefinitions) {
        List<String> caseNameList = new ArrayList<>();
        JSONArray definitions = (JSONArray) caseDefinitions.getJSONArray("definitions");

        for (int i = 0; i < definitions.length(); i++) {
            JSONObject iJson = definitions.getJSONObject(i);
            caseNameList.add(iJson.getString("name"));

        }
        return caseNameList;
    }
    

    public List<String> getMilestonesNameInList(JSONArray milestonesList) {
        List<String> milestonesNameInList = new ArrayList<>();

        for (int i = 0; i < milestonesList.length(); i++) {
            JSONObject iJson = milestonesList.getJSONObject(i);
            milestonesNameInList.add(iJson.getString("milestone-name"));

        }
        return milestonesNameInList;

    }

    public KieBpmConfig getKieBpmConfig() {
        return _config;
    }

    public void setKieBpmConfig(KieBpmConfig _config) {
        this._config = _config;
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

    private KieBpmConfig _config;

    String containerId;
    String caseID;

}
