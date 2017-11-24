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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.entando.entando.plugins.jpkiebpm.KieTestParameters.TEST_ENABLED;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.TestKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FSIDemoHelper;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FSIDemoHelper.TASK_NAME.CLIENT_DETAILS;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessInstance;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessInstancesQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTask;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTaskDetail;

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


    // SIGNAL PROCESS WITH ACCOUNT NAME
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

    // GET ENRICHMENT DOCUMENT HUMAN TASK
    public void testHumanTaskList() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();
        Map<String, String> opt = new HashMap<String, String>();

        opt.put("user", "ddoyle");

        try {
            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());
            // invoke the manager
            _tasks = _formManager.getHumanTaskList("", opt);
            assertNotNull(_tasks);
            if (TEST_ENABLED) {
                assertFalse(_tasks.isEmpty());
            } else {
                assertTrue(_tasks.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
        }
    }


    public void testProcessDelete() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();

        try {
            String containerId = makeSureContainerListExists();
            Long processId = makeSureProcessInstancesListExists();
            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());

            // test
            _formManager.deleteProcess(containerId, String.valueOf(processId), null);
        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
        }
    }

    //  START NEXT ENRICHMENT DOCUMENT HUMAN TASK (x2)
    public void testSetTaskState() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();
        Map<String, String> opt = new HashMap<String, String>();
        String containerId = null;
        Long taskId = null;

        opt.put("user", "ddoyle");
        try {
            containerId = makeSureContainerListExists();
            taskId = makeSureTasksListExists();
            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());
            // test
            _formManager.setTaskState(containerId, String.valueOf(taskId), KieFormManager.TASK_STATES.STARTED, null, opt);
        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
        }
    }


    public void testGetHumanTaskDetail() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();
        Map<String, String> opt = new HashMap<String, String>();
        String containerId = null;
        Long taskId = null;

        opt.put("user", "ddoyle");
        try {
            containerId = makeSureContainerListExists();
            taskId = makeSureTasksListExists();
            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());
            // test
            KieTaskDetail form = _formManager.getTaskDetail(containerId, taskId, opt);
            assertNotNull(form);

        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
        }
    }

    // COMPLETE ENRICH DOCUMENT TASK
    public void _testSubmitHumanFormTask() throws Throwable {
        Map<String, String> reqParams = new HashMap<String, String>();
        String containerId = null;
        Long taskId = null;
        Map<String, Object> input = new HashMap<>();
        Date now = new Date();

        reqParams.put("user", "ddoyle");
        input.put("identifier", "anyIdentifier");
        input.put("name", "anyName");
        input.put("link", "anyLink");
        input.put("content", "anyBase64Content");

        input.put("size", new Long(1234567));
        input.put("lastModified", now.getTime());
        try {
            containerId = makeSureContainerListExists();
            makeSureTasksListExists();
            KieTask task = getTaskForTestById("Enrichment Upload Document","CreditDocuments");
            assertNotNull(task);
            taskId = task.getId();
            _formManager.submitHumanFormTask(
                    containerId, String.valueOf(taskId), KieFormManager.TASK_STATES.STARTED, reqParams, input);
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testHumanTaskListForAdmin() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();
        Map<String, String> opt = new HashMap<String, String>();

        try {
            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());
            // invoke the manager
            _tasks = _formManager.getHumanTaskListForAdmin("Administrator", null);
            assertNotNull(_tasks);
            if (TEST_ENABLED) {
                assertFalse(_tasks.isEmpty());
            } else {
                assertTrue(_tasks.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
        }
    }
    public void testHumanTaskListForAdminRestrict() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();
        Map<String, String> opt = new HashMap<String, String>();

        try {
            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());
            // invoke the manager
            _tasks = _formManager.getHumanTaskListForAdmin("Administrator", LEGAL_WORKER, null);
            assertNotNull(_tasks);
            if (TEST_ENABLED) {
                assertFalse(_tasks.isEmpty());
            } else {
                assertTrue(_tasks.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
        }
    }

    public void testHumanTaskListForUsers() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();
        Map<String, String> opt = new HashMap<String, String>();

        try {
            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());
            // invoke the manager
            _tasks = _formManager.getHumanTaskListForUser("ddoyle", null);
            assertNotNull(_tasks);
            if (TEST_ENABLED) {
                assertFalse(_tasks.isEmpty());
            } else {
                assertTrue(_tasks.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            _formManager.updateConfig(current);
        }
    }


    public void testGetProcessInstancesWithClientData() throws Throwable {
        KieBpmConfig current = _formManager.getConfig();
        Map<String, String> opt = new HashMap<>();

        opt.put("page", "0");
        opt.put("pageSize", "100");
        try {
            // update configuration to reflect test configuration
            _formManager.updateConfig(getConfigForTests());
            // invoke the manager
            KieProcessInstancesQueryResult resp = _formManager.getProcessInstancesWithClientData(null, opt);
            assertNotNull(resp);
            assertNotNull(resp.getInstances());
            assertFalse(resp.getInstances().isEmpty());
            KieProcessInstance proc = resp.getInstances().get(0);
            assertNotNull(proc.getProcess_instance_variables());
            assertFalse(proc.getProcess_instance_variables().isEmpty());
            assertTrue(proc.getProcess_instance_variables().containsKey("clientid"));
        } catch(Throwable t) {
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

    /**
     * Make sure that a list of task with at least one element exists
     * @return
     * @throws Throwable
     */
    protected Long makeSureTasksListExists() throws Throwable {
        if (null == _tasks
                || _tasks.isEmpty()) {
            testHumanTaskList();
        }
        assertFalse(_tasks.isEmpty());
        return _tasks.get(0).getId();
    }

    /**
     *
     * @param id
     * @return
     * @throws Throwable
     */
    protected KieTask getTaskForTestById(String name, String subject) throws Throwable {
        makeSureTasksListExists();
        for (KieTask task: _tasks) {
            if (task.getName().equals(name)
                    || task.getSubject().equals(subject)) {
                return task;
            }
        }
        assertTrue(false); // make the test fail
        return null;
    }

    protected List<KieTask> _tasks;
    private IKieFormManager _formManager;


}
