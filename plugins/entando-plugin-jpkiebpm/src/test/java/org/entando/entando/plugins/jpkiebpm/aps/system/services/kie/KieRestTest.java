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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.entando.entando.plugins.jpkiebpm.KieTestParameters;
import static org.entando.entando.plugins.jpkiebpm.KieTestParameters.HOSTNAME;
import static org.entando.entando.plugins.jpkiebpm.KieTestParameters.PASSWORD;
import static org.entando.entando.plugins.jpkiebpm.KieTestParameters.PORT;
import static org.entando.entando.plugins.jpkiebpm.KieTestParameters.SCHEMA;
import static org.entando.entando.plugins.jpkiebpm.KieTestParameters.TEST_ENABLED;
import static org.entando.entando.plugins.jpkiebpm.KieTestParameters.USERNAME;
import static org.entando.entando.plugins.jpkiebpm.KieTestParameters.WEBAPP;
import org.entando.entando.plugins.jpkiebpm.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;

/**
 *
 * @author own_strong
 */
public class KieRestTest extends ApsPluginBaseTestCase implements KieTestParameters {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        final String expected = SCHEMA + "://" + HOSTNAME + ":" + PORT + "/" + WEBAPP;
        KIEAuthenticationCredentials credentials = new KIEAuthenticationCredentials(USERNAME, PASSWORD);
        KieClient client = new KieClient(credentials);

        client.setHostname(HOSTNAME);
        client.setPort(PORT);
        client.setSchema(SCHEMA);
        client.setWebapp(WEBAPP);

        assertEquals(expected, client.getBaseUrl());
        // get manager
        _caseManager = (CaseManager) this.getService(ICaseManager.BEAN_NAME_ID);
    }

    public void getCasesList() throws Throwable {
        KieBpmConfig current = _caseManager.getConfig();

        try {
            // update configuration to reflect test configuration
            _caseManager.updateConfig(getConfigForTests());
            // invoke the manager
            _cases = _caseManager.getCasesList(TARGET_CONTAINER_ID);
            assertNotNull(_cases);
            if (TEST_ENABLED) {
                assertFalse(_cases.isEmpty());
            } else {
                assertTrue(_cases.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            _caseManager.updateConfig(current);
        }
    }

    public void getMilestonesList() throws Throwable {
        KieBpmConfig current = _caseManager.getConfig();

        try {
            // update configuration to reflect test configuration
            _caseManager.updateConfig(getConfigForTests());
            // invoke the manager
            _milestones = _caseManager.getMilestonesList(HOSTNAME, TARGET_CASE_ID);
            assertNotNull(_milestones);
            if (TEST_ENABLED) {
                assertFalse(_milestones.isEmpty());
            } else {
                assertTrue(_milestones.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            _caseManager.updateConfig(current);
        }
    }

    private KieBpmConfig getConfigForTests() {
        KieBpmConfig cfg = new KieBpmConfig();

        cfg.setActive(TEST_ENABLED);
        cfg.setHostname(HOSTNAME);
        cfg.setPassword(PASSWORD);
        cfg.setPort(PORT);
        cfg.setSchema(SCHEMA);
        cfg.setUsername(USERNAME);
        cfg.setWebapp(WEBAPP);
        cfg.setTimeoutMsec(TIMEOUT);

        return cfg;
    }

    private CaseManager _caseManager;
    private String _cases;
    private String _milestones;

}
