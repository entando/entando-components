package org.entando.entando.plugins.jpkiebpm.apsadmin.util;

import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.client.*;

import java.util.List;

public class KieClientTester {


    //SEE
    //https://github.com/mswiderski/jbpm-examples/blob/master/kie-server-test/src/main/java/org/jbpm/test/kieserver/KieExecutionServerClientTest.java
    // jBPM Process and Project constants

    private final String USERNAME = "testBroker";

    private final String PASSWORD = "broker1!";

    private final String CONTAINER_ID = "mortgage_1.0";

    private final String PROCESS_ID = "4";

    private final String SERVER_URL = "http://localhost:8080/kie-server/services/rest/server";

    public void run() {
        KieServicesConfiguration config =  KieServicesFactory.newRestConfiguration(SERVER_URL, USERNAME, PASSWORD);

        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);
        ProcessServicesClient processServices = client.getServicesClient(ProcessServicesClient.class);
        UserTaskServicesClient taskServicesClient = client.getServicesClient(UserTaskServicesClient.class);

        List<TaskSummary> tasks = taskServicesClient.findTasksAssignedAsPotentialOwner(USERNAME, 0, 10);

        taskServicesClient.startTask(CONTAINER_ID, tasks.get(tasks.size()-1).getId(), USERNAME);
        taskServicesClient.completeTask(CONTAINER_ID, tasks.get(tasks.size()-1).getId(), USERNAME, null);

        System.out.println("tasks "+tasks);
//        void completeTask(String containerId, Long taskId, String userId, Map<String, Object> params);
//        taskServicesClient.completeTask(CONTAINER_ID, TASK_ID, USER_ID, );
    }

    public static void main(String[] args ){

        KieClientTester tester = new KieClientTester();
        tester.run();
    }
}
