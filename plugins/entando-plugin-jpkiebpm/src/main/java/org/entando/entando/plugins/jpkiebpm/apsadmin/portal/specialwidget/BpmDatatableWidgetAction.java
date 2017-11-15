package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 *
 */
public abstract class BpmDatatableWidgetAction extends BpmFormWidgetAction implements Serializable {

    private static final Logger _logger = LoggerFactory.getLogger(BpmDatatableWidgetAction.class);

    protected List<String> selectedGroups = new ArrayList<>();
    protected final List<String> checkedField = new ArrayList<>();
    protected final List<FieldDatatable> fieldsDatatable = new ArrayList<>();
    protected final String PROP_BPM_GROUP = "groups";
    protected final String PREFIX_BPM_GROUP = "bpm_";
    protected final String PREFIX_FIELD = "field_";
    protected final String PREFIX_VISIBLE = "visible_";
    protected final String PREFIX_OVERRIDE = "override_";
    protected final String PREFIX_POSITION = "position_";
    protected String groups;
    protected IGroupManager groupManager;


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
        final List<String> listGroups = new ArrayList<>();
        for (final Group gr : this.groupManager.getGroups()) {
            if (gr.getName().startsWith(PREFIX_BPM_GROUP)) {
                listGroups.add(gr.getName().replace(PREFIX_BPM_GROUP, "").trim());
            }
        }
        return listGroups;


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


    protected abstract void loadFieldIntoDatatableFromBpm() throws ApsSystemException;

    protected void loadDataIntoFieldDatatable(List element) {

        StringTokenizer tokenizer = new StringTokenizer(element.get(0).toString(), ",");
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

    protected void setPropertiesIntoWidgetInfo(final ApsProperties properties, final String procId, final String contId) {
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

    @Override
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
            this.updateConfigWidget(widgetInfo, widget);
        } catch (Exception e) {
            _logger.error("Error in save WidgetInfo", e);

        }
        return widgetInfo;
    }

    @Override
    public String chooseForm() {
        try {
            this.loadFieldIntoDatatableFromBpm();
        } catch (Exception t) {
            _logger.error("Error in chooseForm", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
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

    @Override
    protected void loadWidgetInfo() {
        try {
            final String widgetInfoId = this.getWidget().getConfig().getProperty(PROP_NAME_WIDGET_INFO_ID);
            if (StringUtils.isNotBlank(widgetInfoId)) {
                BpmWidgetInfo widgetInfo = this.getBpmWidgetInfoManager().getBpmWidgetInfo(Integer.valueOf(widgetInfoId));
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
                        this.fieldsDatatable.add(this.getNewFieldDatatable(prop, widgetInfo));
                    }

                }

                Collections.sort(this.fieldsDatatable, new Comparator<FieldDatatable>() {
                    @Override
                    public int compare(FieldDatatable o1, FieldDatatable o2) {
                        return o1.getPosition().compareTo(o2.getPosition());
                    }
                });


            }

        } catch (ApsSystemException e) {
            _logger.error("Error loading WidgetInfo", e);
        }
    }

    private FieldDatatable getNewFieldDatatable(final String data, final BpmWidgetInfo widgetInfo) {
        final FieldDatatable fd = new FieldDatatable(data.replace(PREFIX_FIELD, "").trim());
        fd.setField(PREFIX_FIELD + fd.getName());
        fd.setPosition(Byte.parseByte(widgetInfo.getConfigDraft().getProperty(PREFIX_POSITION + fd.getName())));
        fd.setVisible(Boolean.valueOf(widgetInfo.getConfigDraft().getProperty(PREFIX_VISIBLE + fd.getName())));
        fd.setOverride(widgetInfo.getConfigDraft().getProperty(PREFIX_OVERRIDE + fd.getName()));
        return fd;
    }
}