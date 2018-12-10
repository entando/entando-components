package org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo;

import com.agiletec.aps.util.ApsProperties;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class BpmWidgetInfoBuilder {

    private final BpmWidgetInfo bpmWidgetInfo;
    private final ApsProperties configDraft;
    private final ApsProperties configOnline;

    public BpmWidgetInfoBuilder() {
        this.bpmWidgetInfo = spy(new BpmWidgetInfo());

        this.configDraft = new ApsProperties();
        this.configOnline = new ApsProperties();
    }

    public BpmWidgetInfoBuilder setConfigDraftProperty(String key, String value) {
        configDraft.setProperty(key, value);
        return this;
    }

    public BpmWidgetInfoBuilder setConfigOnlineProperty(String key, String value) {
        configOnline.setProperty(key, value);
        return this;
    }

    public BpmWidgetInfo build() {
        doReturn(configDraft).when(bpmWidgetInfo).getConfigDraft();
        doReturn(configOnline).when(bpmWidgetInfo).getConfigOnline();

        return bpmWidgetInfo;
    }
}
