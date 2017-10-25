package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.kieProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.logging.Level;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.util.KieApiUtil;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.EntityNaming;
import org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.helper.DataUXBuilder;

public class BpmFormWidgetAction extends SimpleWidgetConfigAction {

    private static final Logger _logger = LoggerFactory.getLogger(BpmFormWidgetAction.class);

    @Override
    public String save() {
        //Widget widget = this.createNewWidget();
        return super.save();
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
        Widget widget = this.createNewWidget();
        List<WidgetTypeParameter> parameters = widget.getType().getTypeParameters();
        setPropertyWidget(parameters, widget);
        BpmWidgetInfo widgetInfo = this.storeWidgetInfo(widget);

        WidgetType type = widget.getType();
        if (type.hasParameter(KieBpmSystemConstants.WIDGET_PARAM_DATA_TYPE_CODE/*"dataTypeCode"*/)
                && type.hasParameter(KieBpmSystemConstants.WIDGET_PARAM_DATA_UX_ID/*"dataUxId"*/)) {
            String xml = widgetInfo.getInformationDraft();
            ApsProperties props = new ApsProperties();
            props.loadFromXml(xml);
            String processId = props.getProperty(PROP_PROCESS_ID),
                    containerId = props.getProperty(PROP_CONTAINER_ID);
            //DataModel - start
            KieProcessFormQueryResult kpfr = this.getFormManager()
                    .getProcessForm(containerId, processId);
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
                try {
                    this.processTitle(containerId, this.getCurrentLang().getCode());
                } catch (ApsSystemException ex) {
                    java.util.logging.Logger.getLogger(BpmFormWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
                }
                ((IEntityTypesConfigurer) this.getDataObjectManager()).addEntityPrototype(entityType);
            }
            DataObjectModel dataModel = new DataObjectModel();
            //dataModel.setId(0); TODO gestire id
            dataModel.setDataType(typeCode);
            //dataModel.setDescription(processId + "_" + containerId); exceeds 50 chars limit
            dataModel.setDescription("Model for " + containerId);
            DataUXBuilder uXBuilder = new DataUXBuilder();
            String dataUx = uXBuilder.createDataUx(kpfr, containerId, processId);
            dataModel.setShape(dataUx);
            this.getDataObjectModelManager().addDataObjectModel(dataModel);

            ((DataObject) entityType).setDefaultModel(String.valueOf(dataModel.getId()));
            ((DataObject) entityType).setListModel(String.valueOf(dataModel.getId()));
            ((IEntityTypesConfigurer) this.getDataObjectManager()).updateEntityPrototype(entityType);

            widget.getConfig().setProperty(KieBpmSystemConstants.WIDGET_PARAM_DATA_TYPE_CODE/*"dataTypeCode"*/, entityType.getTypeCode());
            widget.getConfig().setProperty(KieBpmSystemConstants.WIDGET_PARAM_DATA_UX_ID/*"dataUxId"*/, String.valueOf(dataModel.getId()));
            //DataModel - end
        }

        widget.getConfig().setProperty(PROP_NAME_WIDGET_INFO_ID, String.valueOf(widgetInfo.getId()));
        this.setWidget(widget);
    }

    private void addAttributesToEntityType(IApsEntity entityType, KieProcessFormQueryResult form) {
        if (null != form && null != form.getFields()) {
            if (form.getFields().size() > 0) {
                try {
                    this.processForm(form.getFields().get(0), this.getCurrentLang().getCode());
                } catch (ApsSystemException ex) {
                    java.util.logging.Logger.getLogger(BpmFormWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            for (KieProcessFormField field : form.getFields()) {
                this.addAttributeToEntityType(entityType, field);
            }
            if (null != form.getForms()) {
                for (KieProcessFormQueryResult subForm : form.getForms()) {
                    this.addAttributesToEntityType(entityType, subForm);
                }
            }
        }
    }

    private void addAttributeToEntityType(IApsEntity entityType, KieProcessFormField field) {
        if (field.getType().equalsIgnoreCase("InputText")) {
            MonoTextAttribute text = (MonoTextAttribute) this.getAttributePrototype("Monotext");
            text.setName(field.getName());
            text.setDefaultLangCode(this.getCurrentLang().getCode());
            String fieldRequired = KieApiUtil.getFieldProperty(field, "fieldRequired");
            boolean req = (fieldRequired != null && fieldRequired.equalsIgnoreCase("true"));
            text.setRequired(req);
            entityType.addAttribute(text);
        }
        if (field.getType().equalsIgnoreCase("InputTextInteger")
                || field.getType().equalsIgnoreCase("InputTextFloat")) {
            NumberAttribute number = (NumberAttribute) this.getAttributePrototype("Number");
            number.setName(field.getName());
            number.setDefaultLangCode(this.getCurrentLang().getCode());
            String fieldRequired = KieApiUtil.getFieldProperty(field, "fieldRequired");
            boolean req = (fieldRequired != null && fieldRequired.equalsIgnoreCase("true"));
            number.setRequired(req);
            entityType.addAttribute(number);
        }
        try {
            this.processField(field, this.getCurrentLang().getCode());
        } catch (ApsSystemException ex) {
            java.util.logging.Logger.getLogger(BpmFormWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
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

    protected void initSharedParameters(final BpmWidgetInfo widgetInfo) {
        String processId = widgetInfo.getConfigDraft().getProperty(PROP_PROCESS_ID);
        if (StringUtils.isNotBlank(processId) && !processId.equals("null")) {
            String procString = widgetInfo.getConfigDraft().getProperty(PROP_PROCESS_ID) + "@" + widgetInfo.getConfigDraft().getProperty(PROP_CONTAINER_ID);
            this.setProcessPath(procString);
            String[] param = this.getProcessPath().split("@");
            this.setProcessId(param[0]);
            this.setContainerId(param[1]);
        }
        String listOverrrides = widgetInfo.getConfigDraft().getProperty(PROP_OVERRIDE_ID);
        if (StringUtils.isNotBlank(listOverrrides) && !listOverrrides.equals("null")) {
            String[] idStrings = listOverrrides.split(",");
            this.setOvrd(new HashSet<Integer>());
            for (String string : idStrings) {
                this.getOvrd().add(new Integer(string));
            }
        }
    }

    protected void loadWidgetInfo() {
        try {
            String widgetInfoId = this.getWidget().getConfig().getProperty(PROP_NAME_WIDGET_INFO_ID);
            if (StringUtils.isNotBlank(widgetInfoId)) {
                BpmWidgetInfo widgetInfo = this.getBpmWidgetInfoManager().getBpmWidgetInfo(Integer.valueOf(widgetInfoId));
                if (null != widgetInfo) {

                    this.initSharedParameters(widgetInfo);

                }
            }

        } catch (ApsSystemException e) {
            _logger.error("Error loading WidgetInfo", e);
        }
    }

    public String chooseForm() {
        try {
            //System.out.println("nothing to do");
        } catch (Throwable t) {
            _logger.error("Error in chooseForm", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String changeForm() {
        try {
            this.setProcessPath(null);
        } catch (Throwable t) {
            _logger.error("Error in changeForm", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public Map<String, KieFormOverride> getFormOverridesMap() throws ApsSystemException {
        Map<String, KieFormOverride> map = new HashMap<>();
        List<Integer> ids = this.getOverrideList();
        if (null != ids) {
            for (Integer id : ids) {
                KieFormOverride override = this.getKieFormOverride(id);
                map.put(override.getField(), override);
            }
        }
        return map;
    }

    @SuppressWarnings("rawtypes")
    public List<Integer> getOverrideList() throws ApsSystemException {
        List<Integer> list = new ArrayList<>();
        EntitySearchFilter[] filters = this.createSearchFilters();
        list = this.getKieFormOverrideManager().searchKieFormOverrides(filters);
        return list;
    }

    @SuppressWarnings("rawtypes")
    private EntitySearchFilter[] createSearchFilters() {
        EntitySearchFilter[] filters = new EntitySearchFilter[0];
        if (StringUtils.isNotBlank(this.getProcessPath())) {
            String[] params = this.getProcessPath().split("@");
            String processId = params[0];
            String containerId = params[1];
            EntitySearchFilter processIdFilter = new EntitySearchFilter("processId", false, processId, true);
            EntitySearchFilter containerIdFilter = new EntitySearchFilter("containerId", false, containerId, true);
            filters = this.addFilter(filters, processIdFilter);
            filters = this.addFilter(filters, containerIdFilter);
        }
        return filters;
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
        String processId = (params[0]);
        String containerId = (params[1]);
        KieProcessFormQueryResult form = this.getFormManager().getProcessForm(containerId, processId);
        List<KieProcessFormField> fileds = new ArrayList<>();
        this.addFileds(form, fileds);
        return fileds;
    }

    public List<kieProcess> getProcess() throws ApsSystemException {
        List<kieProcess> list = this.getFormManager().getProcessDefinitionsList();
        return list;
    }

    protected void addFileds(KieProcessFormQueryResult form, List<KieProcessFormField> fileds) {
        if (null != form && null != form.getFields()) {
            for (KieProcessFormField kieProcessFormField : form.getFields()) {
                fileds.add(kieProcessFormField);
            }
            if (null != form.getForms()) {
                for (KieProcessFormQueryResult subForm : form.getForms()) {
                    this.addFileds(subForm, fileds);
                }
            }
        }
    }

    protected BpmWidgetInfo storeWidgetInfo(Widget widget) throws Exception {
        BpmWidgetInfo widgetInfo = null;
        try {
            widgetInfo = new BpmWidgetInfo();
            widgetInfo.setFramePosDraft(this.getFrame());
            widgetInfo.setPageCode(this.getPageCode());
            String[] param = this.getProcessPath().split("@");
            String procId = param[0];
            String contId = param[1];
            String overridesId = null;
            widgetInfo.setWidgetType(widget.getType().getCode());
            ApsProperties properties = new ApsProperties();
            properties.put(PROP_PROCESS_ID, procId);
            properties.put(PROP_CONTAINER_ID, contId);

            if (null != this.getOvrd() && !this.getOvrd().isEmpty()) {
                String checkedOverrides = StringUtils.join(this.getOvrd(), ",");
                overridesId = checkedOverrides;
                properties.put(PROP_OVERRIDE_ID, overridesId);
            }
            widgetInfo.setInformationDraft(properties.toXml());
            this.updateConfigWidget(widgetInfo, widget);
        } catch (Exception e) {
            _logger.error("Error save WidgetInfo", e);
            throw e;
        }
        return widgetInfo;
    }

    protected void updateConfigWidget(final BpmWidgetInfo widgetInfo, final Widget widget) throws ApsSystemException {
        String currentConfId = widget.getConfig().getProperty(PROP_NAME_WIDGET_INFO_ID);
        if (StringUtils.isBlank(currentConfId)) {
            this.getBpmWidgetInfoManager().deleteBpmWidgetInfo(widgetInfo);
            this.getBpmWidgetInfoManager().addBpmWidgetInfo(widgetInfo);
        } else {
            widgetInfo.setId(Integer.valueOf(currentConfId));
            this.getBpmWidgetInfoManager().updateBpmWidgetInfo(widgetInfo);
        }
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

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

    public String getProcessPath() {
        return processPath;
    }

    public void setProcessPath(String processPath) {
        this.processPath = processPath;
    }

    public IKieFormOverrideManager getKieFormOverrideManager() {
        return kieFormOverrideManager;
    }

    public void setKieFormOverrideManager(IKieFormOverrideManager kieFormOverrideManager) {
        this.kieFormOverrideManager = kieFormOverrideManager;
    }

    public Set<Integer> getOvrd() {
        return ovrd;
    }

    public void setOvrd(Set<Integer> ovrd) {
        this.ovrd = ovrd;
    }

    public IBpmWidgetInfoManager getBpmWidgetInfoManager() {
        return bpmWidgetInfoManager;
    }

    public void setBpmWidgetInfoManager(IBpmWidgetInfoManager bpmWidgetInfoManager) {
        this.bpmWidgetInfoManager = bpmWidgetInfoManager;
    }

    protected IDataObjectManager getDataObjectManager() {
        return _dataObjectManager;
    }

    public void setDataObjectManager(IDataObjectManager dataObjectManager) {
        this._dataObjectManager = dataObjectManager;
    }

    public IDataObjectModelManager getDataObjectModelManager() {
        return _dataObjectModelManager;
    }

    public void setDataObjectModelManager(IDataObjectModelManager _dataObjectModelManager) {
        this._dataObjectModelManager = _dataObjectModelManager;
    }

    public II18nManager getI18nManager() {
        return i18nManager;
    }

    public void setI18nManager(II18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    protected String processId;
    protected String containerId;
    protected String processPath;

    public static final String PROP_NAME_WIDGET_INFO_ID = KieBpmSystemConstants.WIDGET_PARAM_INFO_ID;//"widgetInfoId";

    protected final String PROP_PROCESS_ID = KieBpmSystemConstants.WIDGET_INFO_PROP_PROCESS_ID;
    protected final String PROP_CONTAINER_ID = KieBpmSystemConstants.WIDGET_INFO_PROP_CONTAINER_ID;
    protected final String PROP_OVERRIDE_ID = KieBpmSystemConstants.WIDGET_INFO_PROP_OVERRIDE_ID;

    private IKieFormManager formManager;
    private IKieFormOverrideManager kieFormOverrideManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;
    private Set<Integer> ovrd;
    private IDataObjectManager _dataObjectManager;
    private IDataObjectModelManager _dataObjectModelManager;
    private II18nManager i18nManager;

}
