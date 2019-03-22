package org.entando.entando.plugins.jpkiebpm.apsadmin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.bpms.examples.mortgage.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.UserTaskServicesClient;

import javax.xml.bind.Marshaller;
import java.util.*;

public class KieClientTester {

    private final static transient Logger logger = LoggerFactory.getLogger(KieClientTester.class);

    //SEE
    //https://github.com/mswiderski/jbpm-examples/blob/master/kie-server-test/src/main/java/org/jbpm/test/kieserver/KieExecutionServerClientTest.java
    // jBPM Process and Project constants
    private final String USERNAME = "testBroker";

    private final String PASSWORD = "broker1!";

    private final String CONTAINER_ID = "mortgage_1.0";

    private final String PROCESS_ID = "4";

    private final String SERVER_URL = "http://localhost:8080/kie-server/services/rest/server";

    @Test
    public void run() {
        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(SERVER_URL, USERNAME, PASSWORD);

        Set<Class<?>> jaxbClasses = new HashSet<Class<?>>();
        jaxbClasses.add(Application.class);

        config.addJaxbClasses(jaxbClasses);
//        config.setMarshallingFormat(MarshallingFormat.JSON);
        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);
        UserTaskServicesClient taskServicesClient = client.getServicesClient(UserTaskServicesClient.class);

        List<TaskSummary> tasks = taskServicesClient.findTasksAssignedAsPotentialOwner(USERNAME, 0, 10);

        for (TaskSummary taskSummary : tasks) {
            logger.info("task: " + String.valueOf(taskSummary));
        }

//        taskServicesClient.claimTask(CONTAINER_ID, tasks.get(0).getId(), USERNAME);
//        taskServicesClient.activateTask(CONTAINER_ID, tasks.get(0).getId(), USERNAME);
//        taskServicesClient.startTask(CONTAINER_ID, tasks.get(0).getId(), USERNAME);
        //taskServicesClient.completeTask(CONTAINER_ID, tasks.get(0).getId(), USERNAME, null);
        //taskServicesClient.completeAutoProgress(containerId, taskId, userId, params);
//        void completeTask(String containerId, Long taskId, String userId, Map<String, Object> params);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("taskOutputApplication", generateDummyApp());

        try {
            ObjectMapper mapper = new ObjectMapper();
            String val = mapper.writeValueAsString(params);
            logger.info("json \n\n" + val);
        } catch (Exception e) {
            e.printStackTrace();
        }

        taskServicesClient.completeAutoProgress(CONTAINER_ID, tasks.get(0).getId(), USERNAME, params);
    }

    @Test
    public void testMarshal() throws Exception {
        javax.xml.bind.JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContext.newInstance(Application.class, HashMap.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        //jaxbMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
        //jaxbMarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("taskOutputApplication", generateDummyApp());
        jaxbMarshaller.marshal(params, System.out);
    }

    private Application generateDummyApp() {
        Application app = new Application();

        List<ValidationError> errors = new ArrayList<ValidationError>();
//    		errors.add(new ValidationError("cause1"));
//    		errors.add(new ValidationError("cause2"));
        app.setValidationErrors(errors);

        app.setApplicant(new Applicant("bob", 123132123, 50000, 25));
        app.setAmortization(30);

        Property property = new Property("123 maple lane", 10000);
        app.setAppraisal(new Appraisal(property, new java.util.Date(), 150000));

        app.setApr(1.5d);
        app.setDownPayment(10000);
        app.setMortgageAmount(175500);
        app.setProperty(property);
        return app;
    }

}
