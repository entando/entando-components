package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.opensymphony.xwork2.Action;
import java.util.HashMap;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.json.JSONArray;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static junit.framework.TestCase.assertEquals;
import static org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.BpmCaseActionBase.ERROR_NULL_CONFIG;

public class BpmBpmCaseInstanceSelectorWidgetActionUnitTest {

    @Mock
    private IKieFormManager formManager;

    @InjectMocks
    private BpmBpmCaseInstanceSelectorWidgetAction action;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldHandleNullConfiguration() throws ApsSystemException {
        action.setWidget(createWidget());

        when(this.formManager.getKieServerConfigurations()).thenReturn(new HashMap<>());
        when(this.formManager.getKieServerStatus()).thenReturn(new JSONArray());

        String result = action.extractInitConfig();

        assertEquals(ERROR_NULL_CONFIG, action.getErrorCode());
        assertEquals(Action.SUCCESS, result);
    }

    private Widget createWidget() {
        Widget widget = new Widget();

        ApsProperties config = new ApsProperties();
        config.put("channel", "1");
        config.put("frontEndCaseData", "{\"container-id\":\"DynamicQuestions_1.0.0\",\"knowledge-source-id\":\"1\"}");
        widget.setConfig(config);

        WidgetType widgetType = new WidgetType();
        widgetType.setCode("bpm-case-instance-selector");
        widget.setType(widgetType);

        return widget;
    }
}
