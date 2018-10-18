package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.Arrays.asList;

public abstract class BpmCaseActionBase  extends SimpleWidgetConfigAction {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BpmBpmCaseInstanceActionsWidgetAction.class);

    private String channel;
    private List<Integer> channels;
    private String frontEndCaseData;

    @Override
    public String init() {
        String result = super.init();
        return result;
    }

    @Override
    protected String extractInitConfig() {
        String result = super.extractInitConfig();

        Widget widget = this.getWidget();
        String frontEndCaseDatain;
        if (widget != null) {
            frontEndCaseDatain = widget.getConfig().getProperty("frontEndCaseData");
            String channel = widget.getConfig().getProperty("channel");
            this.setFrontEndCaseData(frontEndCaseDatain);
            this.setChannel(channel);
            this.setWidgetTypeCode(this.getWidget().getType().getCode());
        } else {
            logger.warn(" widget is null in extraction ");
        }

        return result;
    }

    public String getFrontEndCaseData() {
        return frontEndCaseData;
    }

    public void setFrontEndCaseData(String frontEndCaseData) {
        this.frontEndCaseData = frontEndCaseData;
    }

    public List<Integer> getChannels() {
        return channels = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    public void setChannels(List<Integer> channels) {
        this.channels = channels;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
