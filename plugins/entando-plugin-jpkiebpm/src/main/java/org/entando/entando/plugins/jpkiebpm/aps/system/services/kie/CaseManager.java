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
import org.apache.commons.lang.StringUtils;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jprestapi.aps.core.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author own_strong
 */
public class CaseManager extends KieFormManager implements ICaseManager {

    private static final Logger _logger = LoggerFactory.getLogger(KieFormManager.class);

    @Override
    public void init() throws Exception {
        super.init();
        this.setKieBpmConfig(super.getConfig());
    }

    @Override
    public String getCasesList(String containerId) throws ApsSystemException {
        String result = null;
        if (!_config.getActive() || StringUtils.isBlank(containerId)) {
            return result;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_CASES_LIST).resolveParams(containerId);
            // generate client from the current configuration
            KieClient client = super.getCurrentClient();
            // perform query
            result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setDebug(_config.getDebug())
                    .setUnmarshalOptions(false, true)
                    .doRequest(String.class);
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the cases list", t);
        }

        return result;
    }

    @Override
    public String getMilestonesList(String containerId, String caseID) throws ApsSystemException {
        String result = null;
        if (!_config.getActive() || StringUtils.isBlank(containerId) || StringUtils.isBlank(caseID)) {
            return result;
        }
        try {
            // process endpoint first
            Endpoint ep = KieEndpointDictionary.create().get(API_GET_MILESTONES_LIST).resolveParams(containerId, caseID);
            // generate client from the current configuration
            KieClient client = super.getCurrentClient();
            // perform query
            result = (String) new KieRequestBuilder(client)
                    .setEndpoint(ep)
                    .setDebug(_config.getDebug())
                    .setUnmarshalOptions(false, true)
                    .doRequest(String.class);
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the cases list", t);
        }

        return result;
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
