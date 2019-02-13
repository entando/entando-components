package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;
import java.net.ConnectException;
import java.util.Arrays;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.entando.entando.plugins.jpkiebpm.ActionTestHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfigMapBuilder;
import org.json.JSONArray;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public abstract class BpmSourceAndProcessSelectorTest<T extends SimpleWidgetConfigAction & BpmSourceAndProcessSelector & Preparable> {

    private T action;
    private IKieFormManager formManager;

    protected void setAction(T action, IKieFormManager formManager) throws Exception {
        this.action = action;
        this.formManager = formManager;

        ActionTestHelper.initActionMocks(action);
        initFormManagerMocks();
        action.prepare();
    }

    @Test
    public void testChangeKnowledgeSource() {
        action.changeKnowledgeSourceForm();
        assertNotNull(action.getKnowledgeSource());
        assertNull(action.getKnowledgeSourcePath());
        assertNull(action.getProcess());
        assertNull(action.getProcessPath());
    }

    @Test
    public void testChooseKnowledgeSource() {
        action.setKnowledgeSourcePath("default");
        action.chooseKnowledgeSourceForm();
        assertNotNull(action.getKnowledgeSource());
        assertNotNull(action.getKnowledgeSourcePath());
        assertNotNull(action.getProcess());
        assertEquals(1, action.getProcess().size());
    }

    @Test
    public void testChangeForm() {
        action.setKnowledgeSourcePath("default");
        action.setProcessPath("process");
        action.changeForm();
        assertNotNull(action.getKnowledgeSource());
        assertNotNull(action.getKnowledgeSourcePath());
        assertNotNull(action.getProcess());
        assertNull(action.getProcessPath());
    }

    @Test
    public void testChooseForm() {
        action.setKnowledgeSourcePath("default");
        action.setProcessPath("process");
        action.chooseForm();
        assertNotNull(action.getKnowledgeSource());
        assertNotNull(action.getKnowledgeSourcePath());
        assertNotNull(action.getProcess());
        assertNotNull(action.getProcessPath());
    }

    @Test
    public void testBrokenKnowledgeSource() {
        action.setKnowledgeSourcePath("broken");
        String result = action.chooseKnowledgeSourceForm();

        assertEquals(Action.SUCCESS, result);
        assertNotNull(action.getKnowledgeSource());
        assertEquals(1, action.getActionErrors().size());
        verifyError("kie.server.fail");
        verifyAllUnset();
    }

    @Test
    public void testUnreachableKnowledgeSource() {
        action.setKnowledgeSourcePath("unreachable");
        String result = action.chooseKnowledgeSourceForm();

        assertEquals(Action.SUCCESS, result);
        assertNotNull(action.getKnowledgeSource());
        assertEquals(1, action.getActionErrors().size());
        verifyError("kie.server.fail");
        verifyAllUnset();
    }

    @Test
    public void testInitWithNullPAMConfig() throws ApsSystemException {

        mockForInit();

        action.setFrame(0);
        String result = action.init();

        assertEquals(Action.SUCCESS, result);
        assertNotNull(action.getKnowledgeSource());
        assertEquals(1, action.getActionErrors().size());
        verifyError("kie.config.notFound");
        verifyAllUnset();
    }

    @Test
    public void testChangeFormOnBrokenInstance() {
        action.setKnowledgeSourcePath("broken");
        String result = action.changeForm();

        assertEquals(Action.SUCCESS, result);
        assertNotNull(action.getKnowledgeSource());
        assertEquals(1, action.getActionErrors().size());
        verifyError("kie.server.fail");
        verifyAllUnset();
    }

    @Test
    public void testChangeFormOnDisabledInstance() {
        action.setKnowledgeSourcePath("disabled");
        String result = action.changeForm();

        assertEquals(Action.SUCCESS, result);
        assertNotNull(action.getKnowledgeSource());
        assertEquals(1, action.getActionErrors().size());
        verifyError("kie.config.disabled");
        verifyAllUnset();
    }

    @Test
    public void testChangeFormWithNullPAMConfig() {
        action.setKnowledgeSourcePath("unexisting-source");
        String result = action.changeForm();

        assertEquals(Action.SUCCESS, result);
        assertNotNull(action.getKnowledgeSource());
        assertEquals(1, action.getActionErrors().size());
        verifyError("kie.config.notFound");
        verifyAllUnset();
    }

    private void verifyError(String msgCode) {
        assertEquals(1, action.getActionErrors().size());
        assertEquals(msgCode, action.getActionErrors().iterator().next());
    }

    protected void verifyAllUnset() {
        assertNull(action.getKnowledgeSourcePath());
        assertNull(action.getProcess());
        assertNull(action.getProcessPath());
    }

    protected abstract void mockForInit() throws ApsSystemException;

    protected void initFormManagerMocks() throws Exception {

        HashMap<String, KieBpmConfig> configs = getConfigs();

        when(formManager.getKieServerConfigurations()).thenReturn(configs);
        when(formManager.getKieServerStatus()).thenReturn(new JSONArray());
    }

    protected <A> Answer getPAMInstanceAnswer(A validAnswer) {
        return invocation -> {
            KieBpmConfig config = invocation.getArgument(0);
            switch (config.getName()) {
                case "default":
                    return Arrays.asList(validAnswer);
                case "broken":
                    throw new ApsSystemException("",
                            new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
                case "unreachable":
                    throw new ApsSystemException("", new ConnectException());
                default:
                    return null;
            }
        };
    }

    private HashMap<String, KieBpmConfig> getConfigs() {

        return new KieBpmConfigMapBuilder()
                .addConfig("default")
                .addConfig("broken")
                .addConfig("unreachable")
                .addConfig("disabled", false)
                .build();
    }
}
