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

import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.TestKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormManager.TASK_STATES.COMPLETED;

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
        formManager = (IKieFormManager) this.getService(IKieFormManager.BEAN_NAME_ID);
    }


    // SIGNAL PROCESS WITH ACCOUNT NAME
    public void testSendSignal() throws Throwable {
        final String SIGNAL = "account_registered";
        KieBpmConfig config = getConfigForTests();
        try {
            String containerId = makeSureContainerListExists();
            Long processId = makeSureProcessInstancesListExists();

            boolean delivered = formManager.sendSignal(config, containerId, String.valueOf(processId), SIGNAL, "\"ddoyle\"", null);
            assertTrue(delivered);
        } catch (Throwable t) {
            throw t;
        } finally {
        }
    }

    // GET ENRICHMENT DOCUMENT HUMAN TASK
    public void testHumanTaskList() throws Throwable {
        KieBpmConfig config = getConfigForTests();
        Map<String, String> opt = new HashMap<String, String>();

        opt.put("user", "ddoyle");

        try {
            // invoke the manager
            List<KieTask> tasks = formManager.getHumanTaskList(config, "", opt);
            assertNotNull(tasks);
            if (TEST_ENABLED) {
                assertFalse(tasks.isEmpty());
            } else {
                assertTrue(tasks.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
        }
    }


    public void testProcessDelete() throws Throwable {
        KieBpmConfig config = getConfigForTests();

        try {
            String containerId = makeSureContainerListExists();
            Long processId = makeSureProcessInstancesListExists();
            
            

            // test
            formManager.deleteProcess(config, containerId, String.valueOf(processId), null);
        } catch (Throwable t) {
            throw t;
        } finally {
           
        }
    }

    //  START NEXT ENRICHMENT DOCUMENT HUMAN TASK (x2)
    public void testSetTaskState() throws Throwable {
        KieBpmConfig config = getConfigForTests();
        Map<String, String> opt = new HashMap<String, String>();
        String containerId = null;

        opt.put("user", "ddoyle");
        try {
            containerId = makeSureContainerListExists();
            List<KieTask> tasks = formManager.getHumanTaskList(config, "", opt);
            Long taskId = tasks.get(0).getId();

            // test
            formManager.setTaskState(config, containerId, String.valueOf(taskId), KieFormManager.TASK_STATES.STARTED, null, opt);
        } catch (Throwable t) {
            throw t;
        } finally {
           
        }
    }


    public void testGetHumanTaskDetail() throws Throwable {
        KieBpmConfig config = getConfigForTests();
        Map<String, String> opt = new HashMap<String, String>();
        String containerId = null;

        opt.put("user", "ddoyle");
        try {
            containerId = makeSureContainerListExists();
            List<KieTask> tasks = formManager.getHumanTaskList(config, "", opt);
            Long taskId = tasks.get(0).getId();

            // test
            KieTaskDetail form = formManager.getTaskDetail(config, containerId, taskId, opt);
            assertNotNull(form);

        } catch (Throwable t) {
            throw t;
        } finally {
           
        }
    }

    // COMPLETE ENRICH DOCUMENT TASK
    public void _testSubmitHumanFormTask() throws Throwable {
        KieBpmConfig config = getConfigForTests();
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
            List<KieTask> tasks = formManager.getHumanTaskList(config, "", null);

            KieTask task = getTaskForTestById("Enrichment Upload Document","CreditDocuments", tasks);
            assertNotNull(task);
            taskId = task.getId();
            formManager.submitHumanFormTask(config, containerId, String.valueOf(taskId), KieFormManager.TASK_STATES.STARTED, reqParams, input);
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testHumanTaskListForAdmin() throws Throwable {
        KieBpmConfig config = getConfigForTests();
        Map<String, String> opt = new HashMap<String, String>();

        try {

            // invoke the manager
            List<KieTask> tasks = formManager.getHumanTaskListForAdmin(config, "Administrator", null);
            assertNotNull(tasks);
            if (TEST_ENABLED) {
                assertFalse(tasks.isEmpty());
            } else {
                assertTrue(tasks.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
           
        }
    }
    public void testHumanTaskListForLegalWorker() throws Throwable {
        KieBpmConfig config = getConfigForTests();
        Map<String, String> opt = new HashMap<String, String>();

        try {
            List<KieTask> tasks = formManager.getHumanTaskListForUser(config, KieBpmSystemConstants.LEGAL_WORKER, null);
            assertNotNull(tasks);
            if (TEST_ENABLED) {
                assertFalse(tasks.isEmpty());
            } else {
                assertTrue(tasks.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
           
        }
    }

        public void _testGetCompleteEnrichmentDcumentApprovalTask() throws Throwable {
        KieBpmConfig config = getConfigForTests();
        Map<String, String> opt = new HashMap<String, String>();

        try {

            List<KieTask> tasks = formManager.getHumanTaskList(config, "", opt);

            // invoke the manager
            formManager.getCompleteEnrichmentDcumentApprovalTask(config, "legalWorker",
                    "5fdf1ed1672f5358e70570bd7f50b163",
                    "77", COMPLETED, "review ok", null);
            if (TEST_ENABLED) {
                assertFalse(tasks.isEmpty());
            } else {
                assertTrue(tasks.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
           
        }
    }
    public void testHumanTaskListForUsers() throws Throwable {
        KieBpmConfig config = getConfigForTests();
        Map<String, String> opt = new HashMap<String, String>();

        try {
            
            
            // invoke the manager
            List<KieTask> tasks = formManager.getHumanTaskListForUser(config, "ddoyle", null);
            assertNotNull(tasks);
            if (TEST_ENABLED) {
                assertFalse(tasks.isEmpty());
            } else {
                assertTrue(tasks.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
           
        }
    }


    public void testGetProcessInstancesWithClientData() throws Throwable {
        KieBpmConfig config = getConfigForTests();
        Map<String, String> opt = new HashMap<>();

        opt.put("page", "0");
        opt.put("pageSize", "100");
        try {
            
            
            // invoke the manager
            KieProcessInstancesQueryResult resp = formManager.getProcessInstancesWithClientData(config, null, opt);
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
           
        }
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

//    /**
//     * Make sure that a list of task with at least one element exists
//     * @return
//     * @throws Throwable
//     */
//    protected Long makeSureTasksListExists(KieBpmConfig) throws Throwable {
//
//        List<KieTask> tasks = formManager.getHumanTaskList(config, "", opt);
//
//        assertFalse(tasks.isEmpty());
//        return tasks.get(0).getId();
//    }
//
    /**
     *
     * @return
     * @throws Throwable
     */
    protected KieTask getTaskForTestById(String name, String subject, List<KieTask> tasks) throws Throwable {

        for (KieTask task: tasks) {
            if (task.getName().equals(name) || task.getSubject().equals(subject)) {
                return task;
            }
        }
        assertTrue(false); // make the test fail
        return null;
    }



}
