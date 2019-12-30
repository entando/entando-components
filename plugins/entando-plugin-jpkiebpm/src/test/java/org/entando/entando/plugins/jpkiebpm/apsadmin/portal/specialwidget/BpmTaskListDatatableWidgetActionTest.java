package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import org.entando.entando.plugins.jpkiebpm.ActionTestHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcess;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTask;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.HashMap;

import static com.opensymphony.xwork2.Action.SUCCESS;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BpmTaskListDatatableWidgetActionTest {

    @Mock
    private IKieFormManager formManager;

    @Mock
    private IGroupManager groupManager;

    @InjectMocks
    @Spy
    private BpmTaskListDatatableWidgetAction action;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ActionTestHelper.initActionMocks(action);
        action.prepare();

        mockSourcesConfig();
        mockProcesses();
        mockHumanTaskList();
        mockGroups();
    }

    @Test
    public void testChooseForm() throws ApsSystemException {
        action.setKnowledgeSourcePath("default");
        action.setProcessPath("process");
        action.setContainerId("test");

        assertEquals(SUCCESS, action.chooseForm());
        assertNotNull(action.getFieldsDatatable());
        assertNotNull(action.getCheckedField());
        assertNotNull(action.getFieldsDatatable());
    }

    @Test
    public void testGetGroups() {
        assertNotNull(action.getListBpmGroups());
    }

    private void mockSourcesConfig() throws ApsSystemException {
        HashMap<String, KieBpmConfig> sources = new HashMap<>();
        KieBpmConfig config = new KieBpmConfig();
        config.setName("default");
        sources.put("default", config);
        when(formManager.getKieServerConfigurations())
                .thenReturn(sources);
    }

    private void mockProcesses() throws ApsSystemException {
        when(formManager.getProcessDefinitionsList(any()))
                .thenReturn(Arrays.asList(new KieProcess()));
    }

    private void mockHumanTaskList() throws ApsSystemException {
        when(formManager.getHumanTaskList(any(), any(), any()))
                .thenReturn(Arrays.asList(new KieTask()));
    }

    private void mockGroups() {
        Group group = new Group();
        group.setName("group1");
        when(groupManager.getGroups()).thenReturn(Arrays.asList(group));
    }
}
