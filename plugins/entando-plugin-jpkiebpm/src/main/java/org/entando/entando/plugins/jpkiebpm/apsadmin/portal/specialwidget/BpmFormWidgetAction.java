/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.util.KieApiUtil;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.EntityNaming;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcess;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.helper.DataUXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import com.opensymphony.xwork2.Preparable;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormOverrideInEditing;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.DefaultValueOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.PlaceHolderOverride;
import org.springframework.beans.factory.annotation.Autowired;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.OverrideList;

public class BpmFormWidgetAction extends SimpleWidgetConfigAction implements BpmSourceAndProcessSelector<KieProcess>, Preparable {

    private static final Logger logger = LoggerFactory.getLogger(BpmFormWidgetAction.class);
    private String processId;
    private String containerId;
    private String processPath;
    private String knowledgeSourcePath;
    private Map<String, KieBpmConfig> knowledgeSource;
    private IKieFormManager formManager;
    private IKieFormOverrideManager kieFormOverrideManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;
    private IDataObjectManager dataObjectManager;
    private IDataObjectModelManager dataObjectModelManager;
    private II18nManager i18nManager;
    private List<KieProcess> processes;
    private List<KieFormOverrideInEditing> overrides;
    private Integer overrideToDeleteIndex;

    @Autowired
    private DataUXBuilder uXBuilder;

    private BpmSourceAndProcessSelectorHelper<BpmFormWidgetAction, KieProcess> selectorHelper;

    /**
     * This method is called before each action.
     */
    @Override
    public void prepare() throws ApsSystemException {

        selectorHelper = new BpmSourceAndProcessSelectorHelper<BpmFormWidgetAction, KieProcess>(this) {

            @Override
            public void unsetAll() {
                super.unsetAll();
                overrides = null;
            }
        };
    }

    @Override
    public String save() {
        validateOnSave();
        if (!this.getFieldErrors().isEmpty()) {
            return INPUT;
        }
        return super.save();
    }

    private void validateOnSave() {
        if (overrides != null) {
            List<String> usedFields = new ArrayList<>();
            int index = 0;
            for (KieFormOverrideInEditing override : overrides) {
                if (usedFields.contains(override.getField())) {
                    String msg = this.getText("error.override.field.alreadyUsed", new String[]{override.getField()});
                    this.addFieldError("overrides[" + index + "].field", msg);
                }
                usedFields.add(override.getField());
                index++;
            }
        }
    }

    protected void setPropertyWidget(final List<WidgetTypeParameter> parameters, final Widget widget) {
        for (final WidgetTypeParameter param : parameters) {
            String paramName = param.getName();
            String value = this.getRequest().getParameter(paramName);
            if (value != null && value.trim().length() > 0) {
                widget.getConfig().setProperty(paramName, value);
            }
        }
    }

    @Override
    protected void createValuedShowlet() throws Exception {
        // If the widget is not new we need to retrieve the widgetInfoId but not
        // the WidgetInfo itself (because we are overwriting it) so here we call
        // super.extractInitConfig() instead of this.extractInitConfig()
        super.extractInitConfig();

        Widget widget = getWidget();
        if (widget == null) {
            widget = this.createNewWidget();
        }

        List<WidgetTypeParameter> parameters = widget.getType().getTypeParameters();
        setPropertyWidget(parameters, widget);

        String widgetInfoId = widget.getConfig().getProperty(KieBpmSystemConstants.WIDGET_PARAM_INFO_ID);
        BpmWidgetInfo widgetInfo;
        if (widgetInfoId == null) {
            widgetInfo = this.storeWidgetInfo(widget);
        } else {
            // we want to keep old widget online information and modify widget draft information
            BpmWidgetInfo oldWidgetInfo = this.getBpmWidgetInfoManager().getBpmWidgetInfo(Integer.valueOf(widgetInfoId));
            widgetInfo = this.storeWidgetInfo(widget, oldWidgetInfo);
        }

        this.storeKieFormOverrides(widgetInfo.getId());

        WidgetType type = widget.getType();
        if (type.hasParameter(KieBpmSystemConstants.WIDGET_PARAM_DATA_TYPE_CODE)
                && type.hasParameter(KieBpmSystemConstants.WIDGET_PARAM_DATA_UX_ID)) {
            String xml = widgetInfo.getInformationDraft();
            ApsProperties props = new ApsProperties();
            props.loadFromXml(xml);
            String processId = props.getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_PROCESS_ID);
            String containerId = props.getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_CONTAINER_ID);
            String kieSourceId = props.getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_KIE_SOURCE_ID);
            String title = containerId;

            //DataModel - start
            KieBpmConfig config = this.getFormManager().getKieServerConfigurations().get(kieSourceId);
            KieProcessFormQueryResult kpfr = this.getFormManager().getProcessForm(config, containerId, processId);

            //add missing fields FIXME this should be in the form retrieved
            //kpfr = FSIDemoHelper.addMissinFields(kpfr);
            Map<String, IApsEntity> types = this.getDataObjectManager().getEntityPrototypes();
            String typeCode = null;
            IApsEntity entityType = null;
            for (IApsEntity etype : types.values()) {
                if (etype.getTypeDescription().contains(processId)) {
                    entityType = etype;
                    typeCode = etype.getTypeCode();
                }
            }
            if (entityType == null) {
                typeCode = EntityNaming.generateEntityName(types);
                entityType = (IApsEntity) this.getDataObjectManager().getEntityClass().newInstance();
                entityType.setTypeCode(typeCode);
                entityType.setTypeDescription(processId + "_" + containerId);
                this.addAttributesToEntityType(entityType, kpfr);

                if(selectorHelper.loadProcesses()) {
                    for (KieProcess proc : processes) {
                        if (proc.getProcessId().equalsIgnoreCase(processId)) {
                            title = proc.getProcessName();
                        }
                    }
                    try {
                        this.processTitle(title, this.getCurrentLang().getCode());
                    } catch (ApsSystemException ex) {
                        java.util.logging.Logger.getLogger(BpmFormWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                ((IEntityTypesConfigurer) this.getDataObjectManager()).addEntityPrototype(entityType);
            }
            DataObjectModel dataModel = new DataObjectModel();
            //dataModel.setId(0); TODO gestire id
            dataModel.setDataType(typeCode);
            //dataModel.setDescription(processId + "_" + containerId); exceeds 50 chars limit
            dataModel.setDescription("Model for " + containerId);

            String dataUx = uXBuilder.createDataUx(kpfr, widgetInfo.getId(), containerId, processId, title);
            dataModel.setShape(dataUx);
            this.getDataObjectModelManager().addDataObjectModel(dataModel);

            ((DataObject) entityType).setDefaultModel(String.valueOf(dataModel.getId()));
            ((DataObject) entityType).setListModel(String.valueOf(dataModel.getId()));
            ((IEntityTypesConfigurer) this.getDataObjectManager()).updateEntityPrototype(entityType);

            widget.getConfig().setProperty(KieBpmSystemConstants.WIDGET_PARAM_DATA_TYPE_CODE, entityType.getTypeCode());
            widget.getConfig().setProperty(KieBpmSystemConstants.WIDGET_PARAM_DATA_UX_ID, String.valueOf(dataModel.getId()));
            //DataModel - end
        }

        widget.getConfig().setProperty(KieBpmSystemConstants.WIDGET_PARAM_INFO_ID, String.valueOf(widgetInfo.getId()));
        this.setWidget(widget);
    }

    public DataUXBuilder getuXBuilder() {
        return uXBuilder;
    }

    public void setuXBuilder(DataUXBuilder uXBuilder) {
        this.uXBuilder = uXBuilder;
    }

    private void addAttributesToEntityType(IApsEntity entityType, KieProcessFormQueryResult form) {
        if (null != form && null != form.getFields()) {
            if (form.getFields().size() > 0) {
                try {
                    if (form.getFields().get(0).getName().contains("_")) {
                        this.processForm(form.getFields().get(0), this.getCurrentLang().getCode());
                    } else {
                        String formName = null;
                        if (KieApiUtil.getFieldProperty(form.getProperties(), "name").contains(".")) {
                            formName = KieApiUtil.getFieldProperty(form.getProperties(), "name")
                                    .substring(0, KieApiUtil.getFieldProperty(form.getProperties(), "name").indexOf("."));
                        } else {
                            formName = KieApiUtil.getFieldProperty(form.getProperties(), "name");
                        }
                        String formLabel = KieApiUtil.getI18nFormLabelProperty(formName);
                        if (null == this.getI18nManager().getLabel(formLabel, this.getCurrentLang().getCode())) {
                            this.saveEntandoLabel(formLabel, formName);
                        }
                    }
                } catch (ApsSystemException ex) {
                    logger.error("error on addAttributesToEntityType {}", ex);
                }
            }
            for (KieProcessFormField field : form.getFields()) {
                logger.debug("addAttributeToEntityType {}", field.getName());
                this.addAttributeToEntityType(entityType, field);
            }
            if (null != form.getNestedForms()) {
                for (KieProcessFormQueryResult subForm : form.getNestedForms()) {
                    logger.debug("addAttributeToEntityType getNestedForms");
                    this.addAttributesToEntityType(entityType, subForm);
                }
            }
        }
    }

    private void addAttributeToEntityType(IApsEntity entityType, KieProcessFormField field) {
        String fieldRequired = KieApiUtil.getFieldProperty(field, "fieldRequired");
        boolean req = (fieldRequired != null && fieldRequired.equalsIgnoreCase("true"));

        if (field.getType().equalsIgnoreCase("TextBox")
                || (field.getType().equalsIgnoreCase("TextArea"))
                || (field.getType().equalsIgnoreCase("InputText"))
                || (field.getType().equalsIgnoreCase("ListBox"))
                || (field.getType().equalsIgnoreCase("RadioGroup"))) {
            MonoTextAttribute text = (MonoTextAttribute) this.getAttributePrototype("Monotext");
            text.setName(field.getName());
            text.setDefaultLangCode(this.getCurrentLang().getCode());
            text.setRequired(req);
            entityType.addAttribute(text);
        }
        if (field.getType().equalsIgnoreCase("IntegerBox") || field.getType().equalsIgnoreCase("InputTextInteger")
                || field.getType().equalsIgnoreCase("InputTextFloat") || field.getType().equalsIgnoreCase("FloatBox")) {
            NumberAttribute number = (NumberAttribute) this.getAttributePrototype("Number");
            number.setName(field.getName());
            number.setDefaultLangCode(this.getCurrentLang().getCode());
            number.setRequired(req);
            entityType.addAttribute(number);
        }

        if (field.getType().equalsIgnoreCase("CheckBox")) {
            BooleanAttribute bool = (BooleanAttribute) this.getAttributePrototype("Boolean");
            bool.setName(field.getName());
            bool.setDefaultLangCode(this.getCurrentLang().getCode());
            bool.setRequired(req);
            entityType.addAttribute(bool);
        }
        if (field.getType().equalsIgnoreCase("DatePicker")) {
            DateAttribute date = (DateAttribute) this.getAttributePrototype("Date");
            date.setName(field.getName());
            date.setDefaultLangCode(this.getCurrentLang().getCode());
            date.setRequired(req);
            entityType.addAttribute(date);
        }
        if (field.getType().equalsIgnoreCase("MultipleSelector")) {

            MonoTextAttribute text = (MonoTextAttribute) this.getAttributePrototype("Monotext");
            text.setName(field.getName());
            text.setDefaultLangCode(this.getCurrentLang().getCode());
            text.setRequired(req);

            MonoListAttribute list = (MonoListAttribute) this.getAttributePrototype("Monolist");
            list.setName(field.getName());
            list.setDefaultLangCode(this.getCurrentLang().getCode());
            list.setNestedAttributeType(text);
            list.setRequired(req);

            entityType.addAttribute(list);
        }

        try {
            this.processField(field, this.getCurrentLang().getCode());
        } catch (ApsSystemException ex) {
            logger.debug("Errpr processing field {}", ex);
        }
    }

    private void processField(KieProcessFormField field, String langCode) throws ApsSystemException {
        String bpmLabel = KieApiUtil.getFieldProperty(field, "label");
        if (org.apache.commons.lang.StringUtils.isNotBlank(bpmLabel)) {
            String fieldName = KieApiUtil.getI18nLabelProperty(field);
            if (null == this.getI18nManager().getLabel(fieldName, langCode)) {
                this.saveEntandoLabel(fieldName, bpmLabel);
            }
        }
    }

    private void processForm(KieProcessFormField field, String langCode) throws ApsSystemException {
        String formLabel = KieApiUtil.getI18nFormLabelProperty(field);
        if (org.apache.commons.lang.StringUtils.isNotBlank(formLabel)) {
            String formValue = KieApiUtil.getI18nFormLabelValue(field);
            if (null == this.getI18nManager().getLabel(formLabel, langCode)) {
                this.saveEntandoLabel(formLabel, formValue);
            }
        }
    }

    private void processTitle(String containerId, String langCode) throws ApsSystemException {
        String titleLabel = KieApiUtil.getI18nTitleLabelProperty(containerId);
        if (org.apache.commons.lang.StringUtils.isNotBlank(titleLabel)) {
            String titleValue = KieApiUtil.getI18nTitleLabelValue(containerId);
            if (null == this.getI18nManager().getLabel(titleLabel, langCode)) {
                this.saveEntandoLabel(titleLabel, titleValue);
            }
        }
    }

    protected void saveEntandoLabel(String fieldName, String bpmLabel) throws ApsSystemException {
        ApsProperties apsLabels = new ApsProperties();
        for (Lang lang : this.getLangManager().getLangs()) {
            apsLabels.put(lang.getCode(), bpmLabel);
        }
        this.getI18nManager().addLabelGroup(fieldName, apsLabels);
    }

    protected AttributeInterface getAttributePrototype(String typeCode) {
        IEntityManager entityManager = this.getDataObjectManager();
        Map<String, AttributeInterface> attributeTypes = entityManager.getEntityAttributePrototypes();
        return attributeTypes.get(typeCode);
    }

    @Override
    protected String extractInitConfig() {
        String result = super.extractInitConfig();
        this.setWidgetTypeCode(this.getWidget().getType().getCode());
        this.loadWidgetInfo();
        return result;
    }

    protected void initSharedParameters(final BpmWidgetInfo widgetInfo) throws ApsSystemException {

        String widgetSourceId = widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_KIE_SOURCE_ID);
        String widgetProcessId = widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_PROCESS_ID);
        String widgetContainerId = widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_CONTAINER_ID);

        if (StringUtils.isNotBlank(widgetSourceId) && !widgetSourceId.equals("null")
                && StringUtils.isNotBlank(widgetProcessId) && !widgetProcessId.equals("null")
                && StringUtils.isNotBlank(widgetContainerId) && !widgetContainerId.equals("null")) {

            this.knowledgeSourcePath = widgetSourceId;

            if (selectorHelper.loadProcesses()) {
                this.processId = widgetProcessId;
                this.containerId = widgetContainerId;
                this.processPath = String.format("%s@%s@%s", processId, containerId, knowledgeSourcePath);
                logger.info("Setting processPath to {}", processPath);

                this.loadKieFormOverrides(widgetInfo.getId());
            }
        }
    }

    private void loadKieFormOverrides(int widgetInfoId) throws ApsSystemException {
        List<KieFormOverride> formOverrides = kieFormOverrideManager.getFormOverrides(widgetInfoId, false);
        if (!formOverrides.isEmpty()) {
            if (overrides == null) {
                overrides = new ArrayList<>();
            }
            for (KieFormOverride formOverride : formOverrides) {
                overrides.add(new KieFormOverrideInEditing(formOverride));
            }
        }
    }

    protected void loadWidgetInfo() {
        try {
            String widgetInfoId = this.getWidget().getConfig().getProperty(KieBpmSystemConstants.WIDGET_PARAM_INFO_ID);
            if (StringUtils.isNotBlank(widgetInfoId)) {
                BpmWidgetInfo widgetInfo = this.getBpmWidgetInfoManager().getBpmWidgetInfo(Integer.valueOf(widgetInfoId));
                if (null != widgetInfo) {
                    this.initSharedParameters(widgetInfo);
                }
            }
        } catch (ApsSystemException e) {
            logger.error("Error loading WidgetInfo", e);
        }
    }

    @Override
    public String chooseForm() {
        try {
            selectorHelper.loadProcesses();
            overrides = new ArrayList<>();
        } catch (Throwable t) {
            logger.error("Error in chooseForm", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    public String changeForm() {
        try {
            selectorHelper.loadProcesses();
            processPath = null;
            overrides = null;
        } catch (Throwable t) {
            logger.error("Error in changeForm", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    @SuppressWarnings("rawtypes")
    protected EntitySearchFilter[] addFilter(EntitySearchFilter[] filters, EntitySearchFilter filterToAdd) {
        int len = filters.length;
        EntitySearchFilter[] newFilters = new EntitySearchFilter[len + 1];
        for (int i = 0; i < len; i++) {
            newFilters[i] = filters[i];
        }
        newFilters[len] = filterToAdd;
        return newFilters;
    }

    public KieFormOverride getKieFormOverride(int id) throws ApsSystemException {
        KieFormOverride model = this.getKieFormOverrideManager().getKieFormOverride(id);
        return model;
    }

    public List<KieProcessFormField> getFields() throws ApsSystemException {
        String[] params = this.getProcessPath().split("@");
        String processId = params[0];
        String containerId = params[1];
        String kieSourceId = params[2];

        KieBpmConfig config = this.formManager.getKieServerConfigurations().get(kieSourceId);
        KieProcessFormQueryResult form = this.getFormManager().getProcessForm(config, containerId, processId);
        List<KieProcessFormField> fields = new ArrayList<>();
        this.addFields(form, fields);
        return fields;
    }

    @Override
    public Map<String, KieBpmConfig> getKnowledgeSource() {
        return knowledgeSource;
    }

    @Override
    public void setKnowledgeSource(Map<String, KieBpmConfig> sources) {
        this.knowledgeSource = sources;
    }

    protected void addFields(KieProcessFormQueryResult form, List<KieProcessFormField> fields) {
        if (null != form && null != form.getFields()) {
            for (KieProcessFormField kieProcessFormField : form.getFields()) {
                fields.add(kieProcessFormField);
            }
            if (null != form.getNestedForms()) {
                for (KieProcessFormQueryResult subForm : form.getNestedForms()) {
                    this.addFields(subForm, fields);
                }
            }
        }
    }

    protected BpmWidgetInfo storeWidgetInfo(Widget widget) throws Exception {
        return storeWidgetInfo(widget, new BpmWidgetInfo());
    }

    protected BpmWidgetInfo storeWidgetInfo(Widget widget, BpmWidgetInfo widgetInfo) throws Exception {
        try {
            widgetInfo.setFramePosDraft(this.getFrame());
            widgetInfo.setPageCode(this.getPageCode());
            String[] param = this.getProcessPath().split("@");
            String procId = param[0];
            String contId = param[1];
            widgetInfo.setWidgetType(widget.getType().getCode());
            ApsProperties properties = new ApsProperties();
            properties.put(KieBpmSystemConstants.WIDGET_INFO_PROP_PROCESS_ID, procId);
            properties.put(KieBpmSystemConstants.WIDGET_INFO_PROP_CONTAINER_ID, contId);
            properties.put(KieBpmSystemConstants.WIDGET_INFO_PROP_KIE_SOURCE_ID, this.getKnowledgeSourcePath());

            widgetInfo.setInformationDraft(properties.toXml());
            this.updateConfigWidget(widgetInfo, widget);
            return widgetInfo;
        } catch (Exception e) {
            logger.error("Error save WidgetInfo", e);
            throw e;
        }
    }

    private void storeKieFormOverrides(int widgetInfoId) throws Exception {

        // Delete old overrides
        FieldSearchFilter widgetInfoFilter = new FieldSearchFilter("widgetInfoId", (Integer) widgetInfoId, false);
        FieldSearchFilter draftFilter = new FieldSearchFilter("online", (Integer) 0, false);
        List<Integer> oldWidgetOverrides = kieFormOverrideManager.searchKieFormOverrides(new FieldSearchFilter[]{widgetInfoFilter, draftFilter});
        if (overrides != null) {
            List<Integer> editedOverrides = overrides.stream().filter(o -> o.getId() != null).map(o -> o.getId()).collect(Collectors.toList());
            oldWidgetOverrides.removeAll(editedOverrides); // keeps only overrides to delete
        }
        for (int id : oldWidgetOverrides) {
            kieFormOverrideManager.deleteKieFormOverride(id);
        }

        // Save edited/new overrides
        if (overrides != null) {
            String[] param = this.getProcessPath().split("@");
            String procId = param[0];
            String contId = param[1];
            for (KieFormOverrideInEditing overrideInEditing : overrides) {
                KieFormOverride oldData = null;
                if (overrideInEditing.getId() != null) {
                    oldData = kieFormOverrideManager.getKieFormOverride(overrideInEditing.getId());
                }

                KieFormOverride newData = getEditedKieFormOverride(oldData, overrideInEditing, widgetInfoId, procId, contId);
                if (oldData == null) {
                    kieFormOverrideManager.addKieFormOverride(newData);
                } else {
                    kieFormOverrideManager.updateKieFormOverride(newData);
                }
            }
        }
    }

    private KieFormOverride getEditedKieFormOverride(KieFormOverride oldData, KieFormOverrideInEditing overrideInEditing, int widgetInfoId, String processId, String containerId) {
        KieFormOverride kieFormOverride;
        if (oldData == null) {
            kieFormOverride = new KieFormOverride();
            kieFormOverride.setDate(new Date());
            kieFormOverride.setOverrides(new OverrideList());
        } else {
            kieFormOverride = oldData;
            kieFormOverride.getOverrides().getList().clear();
        }
        kieFormOverride.setActive(overrideInEditing.isActive());
        kieFormOverride.setField(overrideInEditing.getField());
        kieFormOverride.setContainerId(containerId);
        kieFormOverride.setProcessId(processId);
        kieFormOverride.setSourceId(this.getKnowledgeSourcePath());
        kieFormOverride.setWidgetInfoId(widgetInfoId);

        if (!StringUtils.isBlank(overrideInEditing.getDefaultValue())) {
            DefaultValueOverride override = new DefaultValueOverride();
            override.setDefaultValue(overrideInEditing.getDefaultValue());
            kieFormOverride.addOverride(override);
        }
        if (!StringUtils.isBlank(overrideInEditing.getPlaceHolderValue())) {
            PlaceHolderOverride override = new PlaceHolderOverride();
            override.setPlaceHolder(overrideInEditing.getPlaceHolderValue());
            kieFormOverride.addOverride(override);
        }
        return kieFormOverride;
    }

    protected void updateConfigWidget(final BpmWidgetInfo widgetInfo, final Widget widget) throws ApsSystemException {
        String currentConfId = widget.getConfig().getProperty(KieBpmSystemConstants.WIDGET_PARAM_INFO_ID);
        if (StringUtils.isBlank(currentConfId)) {
            this.getBpmWidgetInfoManager().deleteBpmWidgetInfo(widgetInfo);
            this.getBpmWidgetInfoManager().addBpmWidgetInfo(widgetInfo);
        } else {
            widgetInfo.setId(Integer.valueOf(currentConfId));
            this.getBpmWidgetInfoManager().updateBpmWidgetInfo(widgetInfo);
        }
    }

    @Override
    public String chooseKnowledgeSourceForm() {
        try {
            selectorHelper.loadProcesses();
        } catch (ApsSystemException t) {
            logger.error("Error in chooseKnowledgeSourceForm", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    public String changeKnowledgeSourceForm() {
        knowledgeSourcePath = null;
        processPath = null;
        overrides = null;
        return SUCCESS;
    }

    @Override
    public void loadProcesses(KieBpmConfig config) throws ApsSystemException {
        processes = this.formManager.getProcessDefinitionsList(config);
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @Override
    public IKieFormManager getFormManager() {
        return formManager;
    }

    public void setFormManager(IKieFormManager formManager) {
        this.formManager = formManager;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    @Override
    public String getKnowledgeSourcePath() {
        return knowledgeSourcePath;
    }

    @Override
    public void setKnowledgeSourcePath(String knowledgeSourcePath) {
        this.knowledgeSourcePath = knowledgeSourcePath;
    }

    @Override
    public String getProcessPath() {
        return processPath;
    }

    @Override
    public void setProcessPath(String processPath) {
        this.processPath = processPath;
    }

    @Override
    public List<KieProcess> getProcess() {
        return processes;
    }

    @Override
    public void setProcess(List<KieProcess> process) {
        this.processes = process;
    }

    public IKieFormOverrideManager getKieFormOverrideManager() {
        return kieFormOverrideManager;
    }

    public void setKieFormOverrideManager(IKieFormOverrideManager kieFormOverrideManager) {
        this.kieFormOverrideManager = kieFormOverrideManager;
    }

    public IBpmWidgetInfoManager getBpmWidgetInfoManager() {
        return bpmWidgetInfoManager;
    }

    public void setBpmWidgetInfoManager(IBpmWidgetInfoManager bpmWidgetInfoManager) {
        this.bpmWidgetInfoManager = bpmWidgetInfoManager;
    }

    protected IDataObjectManager getDataObjectManager() {
        return dataObjectManager;
    }

    public void setDataObjectManager(IDataObjectManager dataObjectManager) {
        this.dataObjectManager = dataObjectManager;
    }

    public IDataObjectModelManager getDataObjectModelManager() {
        return dataObjectModelManager;
    }

    public void setDataObjectModelManager(IDataObjectModelManager _dataObjectModelManager) {
        this.dataObjectModelManager = _dataObjectModelManager;
    }

    public II18nManager getI18nManager() {
        return i18nManager;
    }

    public void setI18nManager(II18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    public String addFormOverride() {
        try {
            selectorHelper.loadProcesses();
            if (overrides == null) {
                overrides = new ArrayList<>();
            }
            overrides.add(new KieFormOverrideInEditing());
        } catch (Throwable t) {
            logger.error("Error in addFormOverride", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public List<KieFormOverrideInEditing> getOverrides() {
        return overrides;
    }

    public void setOverrides(List<KieFormOverrideInEditing> overrides) {
        this.overrides = overrides;
    }

    public Integer getOverrideToDeleteIndex() {
        return overrideToDeleteIndex;
    }

    public void setOverrideToDeleteIndex(Integer overrideToDeleteIndex) {
        this.overrideToDeleteIndex = overrideToDeleteIndex;
    }

    public String deleteFormOverride() {
        try {
            selectorHelper.loadProcesses();
            if (overrideToDeleteIndex != null) {
                overrides.remove((int) overrideToDeleteIndex);
            }
        } catch (Throwable t) {
            logger.error("Error in deleteOverride", t);
            return FAILURE;
        }
        return SUCCESS;
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
