/*
The MIT License

Copyright 2018 Entando Inc..

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

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

import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.CaseProgressWidgetHelpers.convertKieContainerToListToJson;
import org.springframework.beans.factory.annotation.Autowired;

public class BpmFormWidgetAction extends SimpleWidgetConfigAction {
    private static final Logger logger = LoggerFactory.getLogger(BpmFormWidgetAction.class);
    private String processId;
    private String containerId;
    private String processPath;
    private String knowledgeSourcePath;
    private HashMap<String, KieBpmConfig> knowledgeSource;
    private IKieFormManager formManager;
    private IKieFormOverrideManager kieFormOverrideManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;
    private Set<Integer> ovrd;
    private IDataObjectManager dataObjectManager;
    private IDataObjectModelManager dataObjectModelManager;
    private II18nManager i18nManager;
    private String knowledgeSourceJson;
    private String kieContainerListJson;
    private List<KieProcess> process;

    @Autowired
    private DataUXBuilder uXBuilder;

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

                List<KieProcess> processes = this.getProcess();
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
                ((IEntityTypesConfigurer) this.getDataObjectManager()).addEntityPrototype(entityType);
            }
            DataObjectModel dataModel = new DataObjectModel();
            //dataModel.setId(0); TODO gestire id
            dataModel.setDataType(typeCode);
            //dataModel.setDescription(processId + "_" + containerId); exceeds 50 chars limit
            dataModel.setDescription("Model for " + containerId);

            String dataUx = uXBuilder.createDataUx(kpfr, containerId, processId, title);
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
                logger.debug ("addAttributeToEntityType {}", field.getName());
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
        String fieldRequired = KieApiUtil.getFieldProperty(field, "fieldRequired").toString();
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
        String bpmLabel = KieApiUtil.getFieldProperty(field, "label").toString();
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
        String processId = widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_PROCESS_ID);
        if (StringUtils.isNotBlank(processId) && !processId.equals("null")) {
            String procString = widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_PROCESS_ID) + "@" + widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_CONTAINER_ID) + "@" + widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_KIE_SOURCE_ID);
            logger.info("Setting processPath to {}", procString);
            this.setProcessPath(procString);
            String[] param = this.getProcessPath().split("@");
            this.setProcessId(param[0]);
            this.setContainerId(param[1]);
            this.setKnowledgeSourcePath(param[2]);
        }
        String kieSourceId = widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_KIE_SOURCE_ID);
        if (StringUtils.isNotBlank(kieSourceId) && !"null".equalsIgnoreCase(kieSourceId)) {
            this.setKnowledgeSourcePath(kieSourceId);
        }
        String listOverrrides = widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_OVERRIDE_ID);
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

    public String chooseForm() {
        try {
            //System.out.println("nothing to do");
        } catch (Throwable t) {
            logger.error("Error in chooseForm", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String changeForm() {
        try {
            this.setProcessPath(null);
        } catch (Throwable t) {
            logger.error("Error in changeForm", t);
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

        //TODO -- JPW will this always be present?
        String kieSourceId = params[2];

        KieBpmConfig config = this.formManager.getKieServerConfigurations().get(kieSourceId);
        KieProcessFormQueryResult form = this.getFormManager().getProcessForm(config, containerId, processId);
        List<KieProcessFormField> fileds = new ArrayList<>();
        this.addFileds(form, fileds);
        return fileds;
    }

    public HashMap<String, KieBpmConfig> getKnowledgeSource() {

        try {
            knowledgeSource = this.formManager.getKieServerConfigurations();
        } catch (Exception e) {
            logger.error("Failed to fetch knowledge sources ", e);
        }

        return knowledgeSource;
    }

    protected void addFileds(KieProcessFormQueryResult form, List<KieProcessFormField> fileds) {
        if (null != form && null != form.getFields()) {
            for (KieProcessFormField kieProcessFormField : form.getFields()) {
                fileds.add(kieProcessFormField);
            }
            if (null != form.getNestedForms()) {
                for (KieProcessFormQueryResult subForm : form.getNestedForms()) {
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
            properties.put(KieBpmSystemConstants.WIDGET_INFO_PROP_PROCESS_ID, procId);
            properties.put(KieBpmSystemConstants.WIDGET_INFO_PROP_CONTAINER_ID, contId);

            //TODO -- JPW can this be null here? Should always be set by this point?
//            if (this.getKnowledgeSourceId() == null) {
//                this.setKnowledgeSourceId(this.getFormManager().getConfig().getId());
//            }
            properties.put(KieBpmSystemConstants.WIDGET_INFO_PROP_KIE_SOURCE_ID, this.getKnowledgeSourcePath());

            if (null != this.getOvrd() && !this.getOvrd().isEmpty()) {
                String checkedOverrides = StringUtils.join(this.getOvrd(), ",");
                overridesId = checkedOverrides;
                properties.put(KieBpmSystemConstants.WIDGET_INFO_PROP_OVERRIDE_ID, overridesId);
            }
            widgetInfo.setInformationDraft(properties.toXml());
            this.updateConfigWidget(widgetInfo, widget);
        } catch (Exception e) {
            logger.error("Error save WidgetInfo", e);
            throw e;
        }
        return widgetInfo;
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

    public String chooseKnowledgeSourceForm() {
        return updateKnowledgeSource();
    }

    public String changeKnowledgeSourceForm() {
        return updateKnowledgeSource();
    }

    private String updateKnowledgeSource() {
        try {

            KieBpmConfig config = formManager.getKieServerConfigurations().get(knowledgeSourcePath);
            this.setProcess(this.formManager.getProcessDefinitionsList(config));

            this.setKnowledgeSourceJson(this.formManager.getKieServerStatus().toString());
            this.setKieContainerListJson(convertKieContainerToListToJson(this.formManager.getContainersList(config)).toString());

        } catch (ApsSystemException t) {
            logger.error("Error in chooseKnowledgeSourceForm()", t);
            return FAILURE;
        }
        return SUCCESS;

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

    public String getKnowledgeSourcePath() {
        return knowledgeSourcePath;
    }

    public void setKnowledgeSourcePath(String knowledgeSourcePath) {
        this.knowledgeSourcePath = knowledgeSourcePath;
    }

    public String getProcessPath() {
        return processPath;
    }

    public void setProcessPath(String processPath) {
        this.processPath = processPath;
    }

    public List<KieProcess> getProcess() {
        List<KieProcess> processes = new ArrayList<>();
        try {
            KieBpmConfig config = this.formManager.getKieServerConfigurations().get(this.getKnowledgeSourcePath());
            processes = this.formManager.getProcessDefinitionsList(config);
        } catch (Exception e) {
            logger.error("Failed to fetch processes ", e);
        }
        return processes;
    }

    public void setProcess(List<KieProcess> process) {
        this.process = process;
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
        return dataObjectManager;
    }

    public void setDataObjectManager(IDataObjectManager dataObjectManager) {
        this.dataObjectManager = dataObjectManager;
    }

    public String getKnowledgeSourceJson() {
        return knowledgeSourceJson;
    }

    public void setKnowledgeSourceJson(String knowledgeSourceJson) {
        this.knowledgeSourceJson = knowledgeSourceJson;
    }

    public String getKieContainerListJson() {
        return kieContainerListJson;
    }

    public void setKieContainerListJson(String kieContainerListJson) {
        this.kieContainerListJson = kieContainerListJson;
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
