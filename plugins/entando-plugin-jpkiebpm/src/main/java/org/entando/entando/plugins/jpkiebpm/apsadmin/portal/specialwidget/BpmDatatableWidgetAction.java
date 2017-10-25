package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 *
 */
public class BpmDatatableWidgetAction extends BpmFormWidgetAction {

    private static final Logger _logger = LoggerFactory.getLogger(BpmDatatableWidgetAction.class);
    private IKieFormManager formManager;
    private IKieFormOverrideManager kieFormOverrideManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;
    private IGroupManager groupManager;
    private String groups;
    private List<String> selectedGroups = new ArrayList<>();
    private final List<String> checkedField = new ArrayList<>();
    private final List<FieldDatatable> fieldsDatatable = new ArrayList<>();
    private final String PROP_BPM_GROUP = "groups";
    private final String PREFIX_BPM_GROUP = "bpm_";
    private final String PREFIX_FIELD = "field_";
    private final String PREFIX_VISIBLE = "visible_";
    private final String PREFIX_OVERRIDE = "override_";

    private final String PREFIX_POSITION = "position_";


    @Override
    protected void loadWidgetInfo() {
        _logger.info("Method loadWidgetInfo");
        try {
            String widgetInfoId = this.getWidget().getConfig().getProperty(PROP_NAME_WIDGET_INFO_ID);
            if (StringUtils.isNotBlank(widgetInfoId)) {
                BpmWidgetInfo widgetInfo = this.getBpmWidgetInfoManager().getBpmWidgetInfo(Integer.valueOf(widgetInfoId));
                if (null != widgetInfo) {

                    this.initSharedParameters(widgetInfo);

                    String groups = widgetInfo.getConfigDraft().getProperty(PROP_BPM_GROUP);
                    if (StringUtils.isNotBlank(groups) && !groups.equals("null")) {
                        String[] idStrings = groups.split(",");
                        for (String string : idStrings) {
                            this.selectedGroups.add(string.trim());
                        }
                    }

                    for (final String prop : widgetInfo.getConfigDraft().stringPropertyNames()) {
                        if (prop.startsWith(PREFIX_FIELD)) {

                            final FieldDatatable fd = new FieldDatatable(prop.replace(PREFIX_FIELD, "").trim());
                            fd.setField(PREFIX_FIELD + fd.getName());
                            fd.setPosition(Byte.parseByte(widgetInfo.getConfigDraft().getProperty(PREFIX_POSITION + fd.getName())));
                            fd.setVisible(Boolean.valueOf(widgetInfo.getConfigDraft().getProperty(PREFIX_VISIBLE + fd.getName())));
                            fd.setOverride(widgetInfo.getConfigDraft().getProperty(PREFIX_OVERRIDE + fd.getName()));

                            this.fieldsDatatable.add(fd);
                        }

                    }

                    Collections.sort(this.fieldsDatatable, new Comparator<FieldDatatable>() {
                        @Override
                        public int compare(FieldDatatable o1, FieldDatatable o2) {
                            return o1.position.compareTo(o2.position);
                        }
                    });
                }

            }

        } catch (ApsSystemException e) {
            _logger.error("Error loading WidgetInfo", e);
        }
    }

    private void loadFieldIntoDatatableFromBpm() throws ApsSystemException {
        List<KieTask> task = formManager.getHumanTaskList(null, 0, 1);

        if (!task.isEmpty()) {

            StringTokenizer tokenizer = new StringTokenizer(task.get(0).toString(), ",");
            Byte position = 1;
            while (tokenizer.hasMoreTokens()) {
                final String name = tokenizer.nextToken().trim();
                final FieldDatatable fd = new FieldDatatable(name);
                fd.setField(PREFIX_FIELD + name);
                fd.setPosition(position++);
                fd.setVisible(Boolean.valueOf(true));
                fd.setOverride("");
                this.fieldsDatatable.add(fd);

            }
        }
    }

    private void setPropertiesIntoWidgetInfo(final ApsProperties properties, final String procId, final String contId) {
        properties.put(PROP_PROCESS_ID, procId);
        properties.put(PROP_CONTAINER_ID, contId);
        if (this.getGroups() != null) {
            properties.put(PROP_BPM_GROUP, this.getGroups());
        }
        for (final FieldDatatable field : fieldsDatatable) {
            properties.put(PREFIX_FIELD + field.getName(), field.getField());
            properties.put(PREFIX_VISIBLE + field.getName(), Boolean.toString(field.getVisible()));
            properties.put(PREFIX_OVERRIDE + field.getName(), field.getOverride());
            properties.put(PREFIX_POSITION + field.getName(), Byte.toString(field.getPosition()));

        }
    }

    protected BpmWidgetInfo storeWidgetInfo(final Widget widget) {
        BpmWidgetInfo widgetInfo = null;
        try {
            widgetInfo = new BpmWidgetInfo();
            widgetInfo.setFramePosDraft(this.getFrame());
            widgetInfo.setPageCode(this.getPageCode());
            final String[] param = this.getProcessPath().split("@");
            final String procId = param[0];
            final String contId = param[1];
            widgetInfo.setWidgetType(widget.getType().getCode());
            ApsProperties properties = new ApsProperties();
            this.setPropertiesIntoWidgetInfo(properties, procId, contId);
            widgetInfo.setInformationDraft(properties.toXml());
            this.updateConfigWidget(widgetInfo,widget);
        } catch (Exception e) {
            _logger.error("Error in save WidgetInfo", e);

        }
        return widgetInfo;
    }

    protected void createValuedShowlet() throws Exception {
        _logger.info("Method createValuedShowlet");
        for (final Enumeration enumeration = this.getRequest().getParameterNames(); enumeration.hasMoreElements(); ) {
            final String key = (String) enumeration.nextElement();

            if (key.startsWith(PREFIX_FIELD)) {
                this.fieldsDatatable.add(new FieldDatatable(key.replace(PREFIX_FIELD, "").trim()));
            }
        }
        for (final FieldDatatable field : fieldsDatatable) {


            String keyParameter = PREFIX_FIELD + field.getName();
            field.setField(this.getRequest().getParameter(keyParameter));

            keyParameter = PREFIX_VISIBLE + field.getName();
            field.setVisible(this.getRequest().getParameter(keyParameter) != null);

            keyParameter = PREFIX_OVERRIDE + field.getName();
            field.setOverride(this.getRequest().getParameter(keyParameter));

            keyParameter = PREFIX_POSITION + field.getName();
            String value = this.getRequest().getParameter(keyParameter);
            field.setPosition(Byte.decode(value));
        }

        final Widget widget = this.createNewWidget();
        List<WidgetTypeParameter> parameters = widget.getType().getTypeParameters();
        super.setPropertyWidget(parameters, widget);
        BpmWidgetInfo widgetInfo = this.storeWidgetInfo(widget);
        widget.getConfig().setProperty(PROP_NAME_WIDGET_INFO_ID, String.valueOf(widgetInfo.getId()));
        this.setWidget(widget);


    }


    public String chooseForm() {
        try {
            this.loadFieldIntoDatatableFromBpm();
        } catch (Throwable t) {
            _logger.error("Error in chooseForm", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public List<String> getSelectedGroups() {
        return this.selectedGroups;
    }

    public void setSelectedGroups(List<String> selectedGroups) {
        this.selectedGroups = selectedGroups;
    }

    public List<String> getListBpmGroups() {
        final List<String> groups = new ArrayList<>();
        for (final Group gr : this.groupManager.getGroups()) {
            if (gr.getName().startsWith(PREFIX_BPM_GROUP)) {
                groups.add(gr.getName().replace(PREFIX_BPM_GROUP, "").trim());
            }
        }
        return groups;


    }

    public List<String> getCheckedField() {
        final List<String> visible = new ArrayList<>();
        for (Iterator iterator = this.fieldsDatatable.iterator(); iterator.hasNext(); ) {
            final FieldDatatable fd = (FieldDatatable) iterator.next();
            if (fd.getVisible()) {
                visible.add(PREFIX_VISIBLE + fd.getName());
            }
        }
        return visible;
    }


    public List<FieldDatatable> getFieldsDatatable() throws ApsSystemException {

        if (this.fieldsDatatable.isEmpty()) {
            this.loadFieldIntoDatatableFromBpm();
        }

        return this.fieldsDatatable;
    }

    @Override
    public IGroupManager getGroupManager() {
        return groupManager;
    }

    @Override
    public void setGroupManager(IGroupManager groupManager) {
        this.groupManager = groupManager;
    }


    @Override
    public IKieFormManager getFormManager() {
        return formManager;
    }

    @Override
    public void setFormManager(IKieFormManager formManager) {
        this.formManager = formManager;
    }

    @Override
    public IBpmWidgetInfoManager getBpmWidgetInfoManager() {
        return bpmWidgetInfoManager;
    }

    @Override
    public void setBpmWidgetInfoManager(IBpmWidgetInfoManager bpmWidgetInfoManager) {
        this.bpmWidgetInfoManager = bpmWidgetInfoManager;
    }

    @Override
    public IKieFormOverrideManager getKieFormOverrideManager() {
        return kieFormOverrideManager;
    }

    @Override
    public void setKieFormOverrideManager(IKieFormOverrideManager kieFormOverrideManager) {
        this.kieFormOverrideManager = kieFormOverrideManager;
    }


    public static class FieldDatatable {

        private String name;
        private String field;
        private Boolean visible;
        private String override;
        private Byte position;


        FieldDatatable(final String name) {
            this.name = name;

        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Boolean getVisible() {
            return visible;
        }

        public void setVisible(Boolean visible) {
            this.visible = visible;
        }

        public String getOverride() {
            return override;
        }

        public void setOverride(String override) {
            this.override = override;
        }

        public Byte getPosition() {
            return position;
        }

        public void setPosition(Byte position) {
            this.position = position;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}