package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieContainer;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcess;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessProperty;
import org.json.JSONArray;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * This class provides a spied KieFormManager instance useful in integration
 * tests that need to make fake calls to a Kie server.
 */
public class SpyKieFormManagerUtil {

    public static IKieFormManager getSpiedKieFormManager(IKieFormManager formManager) throws ApsSystemException {
        IKieFormManager formManagerSpy = spy(formManager);

        KieProcess kieProcess = new KieProcess();
        kieProcess.setContainerId("container1");
        kieProcess.setProcessId("process1");
        kieProcess.setKieSourceId("1");
        kieProcess.setProcessName("name1");
        kieProcess.setProcessVersion("1");
        kieProcess.setPackageName("org.entando");

        doReturn(Collections.singletonList(kieProcess)).when(formManagerSpy).getProcessDefinitionsList(any());

        KieContainer kieContainer = new KieContainer();
        kieContainer.setContainerId("container1");
        doReturn(Collections.singletonList(kieContainer)).when(formManagerSpy).getContainersList(any());

        doReturn(getFakeKieProcessFormQueryResult()).when(formManagerSpy).getProcessForm(any(), any(), any());

        doReturn(new JSONArray()).when(formManagerSpy).getKieServerStatus();

        return formManagerSpy;
    }

    private static KieProcessFormQueryResult getFakeKieProcessFormQueryResult() {
        KieProcessFormQueryResult result = new KieProcessFormQueryResult();

        List<KieProcessFormField> fields = new ArrayList<>();
        result.setFields(fields);

        KieProcessFormField field1 = getFakeKieProcessFormField("employee", true);
        field1.setType("TextBox");
        fields.add(field1);

        KieProcessFormField field2 = getFakeKieProcessFormField("reason", true);
        field2.setType("TextArea");
        fields.add(field2);

        return result;
    }

    private static KieProcessFormField getFakeKieProcessFormField(String name, boolean required) {
        KieProcessFormField field = new KieProcessFormField();
        field.setId(name);
        field.setName(name);
        List<KieProcessProperty> properties = new ArrayList<>();
        KieProcessProperty requiredProp = new KieProcessProperty();
        requiredProp.setName("fieldRequired");
        requiredProp.setValue(String.valueOf(required));
        properties.add(requiredProp);
        KieProcessProperty placeHolderProp = new KieProcessProperty();
        placeHolderProp.setName("placeHolder");
        placeHolderProp.setValue(name);
        properties.add(placeHolderProp);
        field.setProperties(properties);
        return field;
    }
}
