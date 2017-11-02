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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.entando.entando.plugins.jpkiebpm.KieTestParameters.TEST_ENABLED;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.TestKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTask;

/**
 *
 * @author Entando
 */
public class FsiMortgageDemoTest extends TestKieFormManager {

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
        _formManager = (IKieFormManager) this.getService(IKieFormManager.BEAN_NAME_ID);
    }


    public void testSendSignal() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();
        final String SIGNAL = "account_registered";

        try {
            String containerId = makeSureContainerListExists();
            Long processId = makeSureProcessInstancesListExists();


            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());

            boolean delivered = _formManager.sendSignal(containerId, String.valueOf(processId), SIGNAL, "\"ddoyle\"", null);
            assertTrue(delivered);
        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
        }
    }

    public void testHumanTaskList() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();
        Map<String, String> opt = new HashMap<String, String>();

        opt.put("user", "ddoyle");

        try {
            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());
            // invoke the manager
            List<KieTask> list = _formManager.getHumanTaskList("", 0, 10, opt);
            assertNotNull(list);
            if (TEST_ENABLED) {
                assertFalse(list.isEmpty());
            } else {
                assertTrue(list.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
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

    private IKieFormManager _formManager;

}
