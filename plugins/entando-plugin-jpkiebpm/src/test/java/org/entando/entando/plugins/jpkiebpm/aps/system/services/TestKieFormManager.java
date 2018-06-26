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
package org.entando.entando.plugins.jpkiebpm.aps.system.services;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.EnvironmentBasedConfigHelper.KIE_SERVER_BASE_URL;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.EnvironmentBasedConfigHelper.KIE_SERVER_PASSWORD;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.EnvironmentBasedConfigHelper.KIE_SERVER_USERNAME;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.EnvironmentBasedConfigHelper.*;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpkiebpm.KieTestParameters;
import org.entando.entando.plugins.jpkiebpm.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KIEAuthenticationCredentials;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieClient;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FormToBpmHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieContainer;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessInstance;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcess;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;

/**
 *
 * @author Entando
 */
public class TestKieFormManager extends ApsPluginBaseTestCase  implements KieTestParameters {


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

    public void testConfigMarshalling() throws Throwable {
        KieBpmConfig cfg = new KieBpmConfig();
        final Boolean active = true;
        final String hostname = "HOSTNAME";
        final String username = "A_USERNAME";
        final String password = "A_PASSWORD";
        final String schema = "https";
        final String webapp = "kie";
        final Integer port = 443;
        final Integer timeout = 2677;

        cfg.setActive(active);
        cfg.setHostname(hostname);
        cfg.setUsername(username);
        cfg.setPassword(password);
        cfg.setPort(port);
        cfg.setSchema(schema);
        cfg.setWebapp(webapp);
        cfg.setTimeoutMsec(timeout);

        String xml = JAXBHelper.marshall(cfg, true, false);
        KieBpmConfig config = (KieBpmConfig) JAXBHelper.unmarshall(xml, KieBpmConfig.class, true, false);
        assertNotNull(config);
        assertNotSame(cfg, config);
        assertEquals(active, config.getActive());
        assertEquals(hostname, config.getHostname());
        assertEquals(password, config.getPassword());
        assertEquals(port, config.getPort());
        assertEquals(schema, config.getSchema());
        assertEquals(username, config.getUsername());
        assertEquals(webapp, config.getWebapp());
        assertEquals(timeout, config.getTimeoutMsec());
    }

    // Test against the DB script!
    public void testDefaultConfig() {
        KieBpmConfig cfg = _formManager.getConfig();

        assertNotNull(cfg);
        assertEquals(Boolean.TRUE, cfg.getActive());
        assertEquals("localhost", cfg.getHostname());
        assertEquals("krisv", cfg.getPassword());
        assertEquals((Integer) 8080, cfg.getPort());
        assertEquals("http", cfg.getSchema());
        assertEquals("krisv", cfg.getUsername());
        assertEquals("kie-server", cfg.getWebapp());
        assertEquals((Integer)1000, cfg.getTimeoutMsec());
    }
    public void testDefaultConfigFromEnvironment() throws ApsSystemException {
        try {
            System.setProperty(KIE_SERVER_BASE_URL, "http://someserver.somehost.com:9090/somecontxt");
            System.setProperty(KIE_SERVER_PASSWORD,"P@ssword");
            System.setProperty(KIE_SERVER_USERNAME, "johnnie");
            ((KieFormManager)_formManager).setupConfig();
            KieBpmConfig cfg = _formManager.getConfig();

            assertNotNull(cfg);
            assertEquals(Boolean.TRUE, cfg.getActive());
            assertEquals("someserver.somehost.com", cfg.getHostname());
            assertEquals("P@ssword", cfg.getPassword());
            assertEquals((Integer) 9090, cfg.getPort());
            assertEquals("http", cfg.getSchema());
            assertEquals("johnnie", cfg.getUsername());
            assertEquals("somecontxt", cfg.getWebapp());
            assertEquals((Integer) 5000, cfg.getTimeoutMsec());
        }finally{
            System.getProperties().remove(KIE_SERVER_BASE_URL);
            System.getProperties().remove(KIE_SERVER_PASSWORD);
            System.getProperties().remove(KIE_SERVER_USERNAME);
            _formManager.deleteConfig("environment-based-kie-config-#0001");
            ((KieFormManager)_formManager).setupConfig();
            KieBpmConfig cfg = _formManager.getConfig();
            testDefaultConfig();
        }
    }

    public void testUpdateConfig() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();

        try {
            KieBpmConfig cfg = getConfigForTests();

            _formManager.updateConfig(cfg);
            KieBpmConfig config = _formManager.getConfig();
            assertNotNull(config);
            assertEquals(TEST_ENABLED, config.getActive());
            assertEquals(HOSTNAME, config.getHostname());
            assertEquals(PASSWORD, config.getPassword());
            assertEquals(PORT, config.getPort());
            assertEquals(SCHEMA, config.getSchema());
            assertEquals(USERNAME, config.getUsername());
            assertEquals(WEBAPP, config.getWebapp());
            assertEquals(TIMEOUT, config.getTimeoutMsec());
        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
        }
    }
    public void testUpdateConfigWithGetKieServerConfigurations() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();

        try {
            KieBpmConfig cfg = getConfigForTests();

            _formManager.updateConfig(cfg);
            HashMap<String, KieBpmConfig> configs = _formManager.getKieServerConfigurations();
            Optional<KieBpmConfig> first = configs.values().stream().filter((found) -> found.getHostname().equals(cfg.getHostname())).findFirst();
            assertTrue(first.isPresent());
            KieBpmConfig config=first.get();
            assertEquals(TEST_ENABLED, config.getActive());
            assertEquals(HOSTNAME, config.getHostname());
            assertEquals(PASSWORD, config.getPassword());
            assertEquals(PORT, config.getPort());
            assertEquals(SCHEMA, config.getSchema());
            assertEquals(USERNAME, config.getUsername());
            assertEquals(WEBAPP, config.getWebapp());
            assertEquals(TIMEOUT, config.getTimeoutMsec());
        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
        }
    }

    public void testListContainers() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();

        try {
            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());
            // invoke the manager
            _containers = _formManager.getContainersList();
            assertNotNull(_containers);
            if (TEST_ENABLED) {
                assertFalse(_containers.isEmpty());
            } else {
                assertTrue(_containers.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
        }
    }

    public void testListProcessDefinitions() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();

        if (!TEST_ENABLED) {
            return;
        }
        try {
            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());
            // invoke the manager
            _processDefinitions = _formManager.getProcessDefinitionsList();
            assertNotNull(_processDefinitions);
            if (TEST_ENABLED) {
                assertFalse(_processDefinitions.isEmpty());

            } else {
                assertTrue(_processDefinitions.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
        }
    }

    public void testProcessInstancesList() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();

        if (!TEST_ENABLED) {
            return;
        }

        try {
            final String processId = makeSureProcessDefinitionsListExist();

            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());
            // invoke the manager
            _processes = _formManager.getProcessInstancesList(processId, 0, 5000);
            assertNotNull(_processes);
            if (TEST_ENABLED) {
                assertFalse(_processes.isEmpty());
            } else {
                assertTrue(_processes.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
        }
    }

    public void testProcessForm() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();

        if (!TEST_ENABLED) {
            return;
        }

        try {
            final String containerId = makeSureContainerListExists();
            final String processId = makeSureProcessDefinitionsListExist();

            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());
            // invoke the manager
            KieProcessFormQueryResult form = _formManager
                    .getProcessForm(containerId, processId);
            assertNotNull(form);

            String mah = FormToBpmHelper.generateFormJson(form,
                    new HashMap<String, Object>(), containerId, processId);

        } finally {
            _formManager.updateConfig(current);
        }
    }

    public void testGetAllProcessInstancesList() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();

        if (!TEST_ENABLED) {
            return;
        }
        try {
            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());

            List<KieProcessInstance> list = _formManager.getAllProcessInstancesList(null);
            assertNotNull(list);
            assertFalse(list.isEmpty());
        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
        }
    }

    /**
     * Make sure we have some process definition to use for test whatever it is
     * @return
     * @throws Throwable
     */
    protected String makeSureProcessDefinitionsListExist() throws Throwable {
        if (!TEST_ENABLED) {
            return "";
        }
        if (null == _processDefinitions
                || _processDefinitions.isEmpty()) {
            makeSureContainerListExists();
            testListProcessDefinitions();
        }
        assertFalse(_processDefinitions.isEmpty());
        return _processDefinitions.get(0).getProcessId();
    }

    /**
     * Make sure we have some container to use for test whatever it is
     * @return
     * @throws Throwable
     */
    protected String makeSureContainerListExists() throws Throwable {
        if (!TEST_ENABLED) {
            return "";
        }
        if (null == _containers
                || _containers.isEmpty()) {
            testListContainers();
        }
        assertFalse(_containers.isEmpty());
        return _containers.get(0).getContainerId();
    }

    /**
     * Make sure we have some process instance to use for test whatever it is
     * @return
     * @throws Throwable
     */
    protected Long makeSureProcessInstancesListExists() throws Throwable {
        if (!TEST_ENABLED) {
            return 0L;
        }
        if (null == _processes
                || _processes.isEmpty()) {
            makeSureProcessDefinitionsListExist();
            testProcessInstancesList();
        }
        assertFalse(_processes.isEmpty());
        return _processes.get(0).getInstanceId();
    }

    protected KieBpmConfig getConfigForTests() {
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

    protected List<KieProcess> _processDefinitions;
    protected List<KieContainer> _containers;
    protected List<KieProcessInstance> _processes;

    protected IKieFormManager _formManager;
}
