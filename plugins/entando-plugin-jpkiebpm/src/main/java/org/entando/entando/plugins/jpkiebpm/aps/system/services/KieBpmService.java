/*
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.logging.Level;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.util.KieApiUtil;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.EntityNaming;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcess;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTask;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.model.DatatableWidgetConfigDto;
import org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.BpmFormWidgetAction;
import org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.helper.DataUXBuilder;
import org.entando.entando.plugins.jpkiebpm.web.model.DatatableWidgetConfigRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author paddeo
 */
public class KieBpmService implements IKieBpmService {

    private final String PROP_BPM_GROUP = "groups";
    private final String PREFIX_BPM_GROUP = "bpm_";
    private final String PREFIX_FIELD = "field_";
    private final String PREFIX_VISIBLE = "visible_";
    private final String PREFIX_OVERRIDE = "override_";
    private final String PREFIX_POSITION = "position_";

    private static final String ERRCODE_PAGE_NOT_FOUND = "1";
    private static final String ERRCODE_FRAME_INVALID = "2";
    private static final String ERRCODE_WIDGET_INVALID = "4";

    public static final String PROP_NAME_WIDGET_INFO_ID = KieBpmSystemConstants.WIDGET_PARAM_INFO_ID;
    public static final String PROP_NAME_DATA_UX_ID = KieBpmSystemConstants.WIDGET_PARAM_DATA_UX_ID;
    public static final String PROP_NAME_DATA_TYPE = KieBpmSystemConstants.WIDGET_PARAM_DATA_TYPE_CODE;

    protected final String PROP_PROCESS_ID = KieBpmSystemConstants.WIDGET_INFO_PROP_PROCESS_ID;
    protected final String PROP_CONTAINER_ID = KieBpmSystemConstants.WIDGET_INFO_PROP_CONTAINER_ID;
    protected final String PROP_OVERRIDE_ID = KieBpmSystemConstants.WIDGET_INFO_PROP_OVERRIDE_ID;

    private String DEMO_USER = "taskUser";
    private String langCode;

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IBpmWidgetInfoManager bpmWidgetInfoManager;

    @Autowired
    @Qualifier("jpkiebpmsCaseManager")
    private IKieFormManager formManager;

    @Autowired
    private IDataObjectManager dataObjectManager;

    @Autowired
    private IDataObjectModelManager dataObjectModelManager;

    @Autowired
    private II18nManager i18nManager;

    @Autowired
    private ILangManager langManager;

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    @Autowired
    private IDtoBuilder<BpmWidgetInfo, DatatableWidgetConfigDto> dtoBuilder;
    
    @Autowired
    private DataUXBuilder uXBuilder;
    
    public IBpmWidgetInfoManager getBpmWidgetInfoManager() {
        return bpmWidgetInfoManager;
    }

    public void setBpmWidgetInfoManager(IBpmWidgetInfoManager bpmWidgetInfoManager) {
        this.bpmWidgetInfoManager = bpmWidgetInfoManager;
    }

    public IKieFormManager getFormManager() {
        return formManager;
    }

    public void setFormManager(IKieFormManager formManager) {
        this.formManager = formManager;
    }

    public IDataObjectManager getDataObjectManager() {
        return dataObjectManager;
    }

    public void setDataObjectManager(IDataObjectManager dataObjectManager) {
        this.dataObjectManager = dataObjectManager;
    }

    public IDataObjectModelManager getDataObjectModelManager() {
        return dataObjectModelManager;
    }

    public void setDataObjectModelManager(IDataObjectModelManager dataObjectModelManager) {
        this.dataObjectModelManager = dataObjectModelManager;
    }

    public II18nManager getI18nManager() {
        return i18nManager;
    }

    public void setI18nManager(II18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    public ILangManager getLangManager() {
        return langManager;
    }

    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }

    public IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    public IWidgetTypeManager getWidgetTypeManager() {
        return widgetTypeManager;
    }

    public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
        this.widgetTypeManager = widgetTypeManager;
    }

    public IDtoBuilder<BpmWidgetInfo, DatatableWidgetConfigDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<BpmWidgetInfo, DatatableWidgetConfigDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public DataUXBuilder getuXBuilder() {
        return uXBuilder;
    }

    public void setuXBuilder(DataUXBuilder uXBuilder) {
        this.uXBuilder = uXBuilder;
    }

    @Override
    public DatatableWidgetConfigDto getDataTableWIdgetConfig(int configId) {
        try {
            BpmWidgetInfo info = this.getBpmWidgetInfoManager().getBpmWidgetInfo(configId);
            return this.getDtoBuilder().convert(info);
        } catch (Throwable t) {
            logger.error("error in loading widget config", t);
            throw new RestServerError("error in loading widget config", t);
        }
    }

    @Override
    public DatatableWidgetConfigDto updateDataTableWIdgetConfig(DatatableWidgetConfigRequest req) {
        try {
            BpmWidgetInfo info = this.storeWidgetInfo(req);
            DatatableWidgetConfigDto dto = this.getDtoBuilder().convert(info);
            this.updateWidgetConfiguration(dto);
            return dto;
        } catch (Throwable t) {
            logger.error("error in updating widget config", t);
            throw new RestServerError("error in updating widget config", t);
        }
    }

    @Override
    public DatatableWidgetConfigDto deleteDataTableWIdgetConfig(int configId) {
        try {
            BpmWidgetInfo info = this.getBpmWidgetInfoManager().getBpmWidgetInfo(configId);
            this.getBpmWidgetInfoManager().deleteBpmWidgetInfo(configId);
            DatatableWidgetConfigDto dto = this.getDtoBuilder().convert(info);
            this.deleteWidgetConfiguration(dto.getPageCode(), dto.getFramePosDraft());
            return dto;
        } catch (Throwable t) {
            logger.error("error in config delete", t);
            throw new RestServerError("error in config delete", t);
        }
    }

    @Override
    public DatatableWidgetConfigDto updateDataTypeWIdgetConfig(DatatableWidgetConfigRequest req) {
        try {
            BpmWidgetInfo info = this.storeWidgetInfo(req);
            DatatableWidgetConfigDto dto = this.getDtoBuilder().convert(info);
            this.setLangCode(req.getLang());
            dto.setContainerId(req.getContainerId());
            dto.setProcessId(req.getProcessId());
            dto.setKnowledgeSourceId(req.getKnowledgeSourceId());
            this.createDataType(dto);
            this.updateWidgetConfiguration(dto);
            return dto;
        } catch (Throwable t) {
            logger.error("error in updating widget config", t);
            throw new RestServerError("error in updating widget config", t);
        }
    }

    @Override
    public DatatableWidgetConfigDto chooseForm() {
        try {
            return this.loadFieldIntoDatatableFromBpm();
        } catch (Throwable t) {
            logger.error("error in choose form", t);
            throw new RestServerError("error in chose form", t);
        }
    }

    @Override
    public DatatableWidgetConfigDto chooseProcessForm() {
        try {
            return this.loadFieldIntoDatatableProcessFromBpm();
        } catch (Throwable t) {
            logger.error("error in choose form", t);
            throw new RestServerError("error in chose form", t);
        }
    }

    private BpmWidgetInfo storeWidgetInfo(DatatableWidgetConfigRequest req) {
        BpmWidgetInfo widgetInfo = null;
        try {
            widgetInfo = new BpmWidgetInfo();
            widgetInfo.setFramePosDraft(req.getFramePosDraft());
            widgetInfo.setPageCode(req.getPageCode());

            widgetInfo.setWidgetType(req.getWidgetType());
            ApsProperties properties = new ApsProperties();
            this.setPropertiesIntoWidgetInfo(properties, req);
            widgetInfo.setInformationDraft(properties.toXml());
            widgetInfo.setId(req.getId());
            this.updateConfigWidget(widgetInfo);
        } catch (Exception e) {
            logger.error("Error in save WidgetInfo", e);

        }
        return widgetInfo;
    }

    private void setPropertiesIntoWidgetInfo(ApsProperties properties, DatatableWidgetConfigRequest req) {
        String procId = req.getProcessId();
        String contId = req.getContainerId();
        String sourceId = req.getKnowledgeSourceId();
        properties.put(PROP_PROCESS_ID, procId);
        properties.put(PROP_CONTAINER_ID, contId);
        if (sourceId != null) {
            properties.put(KieBpmSystemConstants.WIDGET_INFO_PROP_KIE_SOURCE_ID, sourceId);
        }
        if (req.getGroup() != null) {
            properties.put(PROP_BPM_GROUP, req.getGroup());
        }
        req.getInformationDraft().forEach(fieldMap -> {
            String field = (String) fieldMap.get("field") != null ? (String) fieldMap.get("field") : "";
            String visible = (String) fieldMap.get("visible") != null ? (String) fieldMap.get("visible") : "";
            String override = (String) fieldMap.get("override") != null ? (String) fieldMap.get("override") : "";
            String position = (String) fieldMap.get("position") != null ? (String) fieldMap.get("position") : "";
            properties.put(PREFIX_FIELD + field, PREFIX_FIELD + field);
            properties.put(PREFIX_VISIBLE + field, visible);
            properties.put(PREFIX_OVERRIDE + field, override);
            properties.put(PREFIX_POSITION + field, position);
        });
    }

    private void updateConfigWidget(BpmWidgetInfo widgetInfo) throws ApsSystemException {
        if (widgetInfo.getId() == 0) {
            this.getBpmWidgetInfoManager().deleteBpmWidgetInfo(widgetInfo);
            this.getBpmWidgetInfoManager().addBpmWidgetInfo(widgetInfo);
        } else {
            this.getBpmWidgetInfoManager().updateBpmWidgetInfo(widgetInfo);
        }
    }

    private DatatableWidgetConfigDto loadFieldIntoDatatableFromBpm() throws ApsSystemException {
        DatatableWidgetConfigDto info = null;
        HashMap<String, String> opt = new HashMap<>();
        opt.put("user", DEMO_USER);
        List<KieTask> task = formManager.getHumanTaskList(null, opt);
        if (!task.isEmpty()) {
            info = this.loadDataIntoFieldDatatable(task);
        }
        return info;
    }

    private DatatableWidgetConfigDto loadFieldIntoDatatableProcessFromBpm() throws ApsSystemException {
        List<KieProcess> processes = formManager.getProcessDefinitionsList();
        DatatableWidgetConfigDto dto = new DatatableWidgetConfigDto();
        if (!processes.isEmpty()) {
            dto = this.loadDataIntoFieldDatatable(processes);
        }

        HashMap<String, String> columns = new HashMap<>();

        columns.put("Status Progress", "statusProgress");
        columns.put("Customer Name", "customerName");
        columns.put("partyName", "partyName");
        columns.put("status", "status");
        columns.put("Company", "company");
        columns.put("Case Due In", "dueDate");
        dto.getInformationDraft().addAll(this.loadDataIntoFieldDatatable(columns));
        return dto;
    }

    private DatatableWidgetConfigDto loadDataIntoFieldDatatable(List element) {
        DatatableWidgetConfigDto info = new DatatableWidgetConfigDto();
        List<Map<String, String>> props = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(element.get(0).toString(), ",");
        Byte position = 1;
        while (tokenizer.hasMoreTokens()) {
            Map<String, String> prop = new HashMap<>();
            String name = tokenizer.nextToken().trim();
            prop.put("field", name);
            prop.put("position", position++ + "");
            prop.put("visible", "true");
            prop.put("override", "");
            props.add(prop);
        }
        info.setInformationDraft(props);
        return info;
    }

    private List<Map<String, String>> loadDataIntoFieldDatatable(HashMap<String, String> fields) {

        int position = 1;
        List<Map<String, String>> fieldsToAdd = new ArrayList<>();
        for (Iterator<Map.Entry<String, String>> iter = fields.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<String, String> obj = iter.next();
            final String name = obj.getValue();
            Map<String, String> prop = new HashMap<>();
            prop.put("field", name);
            prop.put("position", position++ + "");
            prop.put("visible", "true");
            prop.put("override", "");
            fieldsToAdd.add(prop);
        }
        return fieldsToAdd;
    }

    private void createDataType(DatatableWidgetConfigDto dto) throws Exception {
        String containerId = dto.getContainerId(),
                processId = dto.getProcessId();
        String title = containerId;
        KieProcessFormQueryResult kpfr = this.getFormManager()
                .getProcessForm(containerId, processId);

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

            List<KieProcess> processes = this.getFormManager().getProcessDefinitionsList();
            for (KieProcess proc : processes) {
                if (proc.getProcessId().equalsIgnoreCase(processId)) {
                    title = proc.getProcessName();
                }
            }
            try {
                this.processTitle(title, this.getLangCode());
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
        //DataUXBuilder uXBuilder = new DataUXBuilder();
                
        String dataUx = uXBuilder.createDataUx(kpfr, containerId, processId, title);
        dataModel.setShape(dataUx);
        this.getDataObjectModelManager().addDataObjectModel(dataModel);

        ((DataObject) entityType).setDefaultModel(String.valueOf(dataModel.getId()));
        ((DataObject) entityType).setListModel(String.valueOf(dataModel.getId()));
        ((IEntityTypesConfigurer) this.getDataObjectManager()).updateEntityPrototype(entityType);
        dto.setDataType(typeCode);
        dto.setDataUxId(dataModel.getId());
    }

    private void addAttributesToEntityType(IApsEntity entityType, KieProcessFormQueryResult form) {
        if (null != form && null != form.getFields()) {
            if (form.getFields().size() > 0) {
                try {
                    if (form.getFields().get(0).getName().contains("_")) {
                        this.processForm(form.getFields().get(0), this.getLangCode());
                    } else {
                        String formName = null;
                        if (KieApiUtil.getFieldProperty(form.getProperties(), "name").contains(".")) {
                            formName = KieApiUtil.getFieldProperty(form.getProperties(), "name")
                                    .substring(0, KieApiUtil.getFieldProperty(form.getProperties(), "name").indexOf("."));
                        } else {
                            formName = KieApiUtil.getFieldProperty(form.getProperties(), "name");
                        }
                        String formLabel = KieApiUtil.getI18nFormLabelProperty(formName);
                        if (null == this.getI18nManager().getLabel(formLabel, this.getLangCode())) {
                            this.saveEntandoLabel(formLabel, formName);
                        }
                    }
                } catch (ApsSystemException ex) {
                    java.util.logging.Logger.getLogger(BpmFormWidgetAction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            for (KieProcessFormField field : form.getFields()) {
                this.addAttributeToEntityType(entityType, field);
            }
            if (null != form.getNestedForms()) {
                for (KieProcessFormQueryResult subForm : form.getNestedForms()) {
                    this.addAttributesToEntityType(entityType, subForm);
                }
            }
        }
    }

    private void addAttributeToEntityType(IApsEntity entityType, KieProcessFormField field) {
        if (field.getType().equalsIgnoreCase("InputText")) {
            MonoTextAttribute text = (MonoTextAttribute) this.getAttributePrototype("Monotext");
            text.setName(field.getName());
            text.setDefaultLangCode(this.getLangCode());
            String fieldRequired = KieApiUtil.getFieldProperty(field, "fieldRequired");
            boolean req = (fieldRequired != null && fieldRequired.equalsIgnoreCase("true"));
            text.setRequired(req);
            entityType.addAttribute(text);
        }
        if (field.getType().equalsIgnoreCase("InputTextInteger")
                || field.getType().equalsIgnoreCase("InputTextFloat")) {
            NumberAttribute number = (NumberAttribute) this.getAttributePrototype("Number");
            number.setName(field.getName());
            number.setDefaultLangCode(this.getLangCode());
            String fieldRequired = KieApiUtil.getFieldProperty(field, "fieldRequired");
            boolean req = (fieldRequired != null && fieldRequired.equalsIgnoreCase("true"));
            number.setRequired(req);
            entityType.addAttribute(number);
        }
        try {
            this.processField(field, this.getLangCode());
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

    private void saveEntandoLabel(String fieldName, String bpmLabel) throws ApsSystemException {
        ApsProperties apsLabels = new ApsProperties();
        for (Lang lang : this.getLangManager().getLangs()) {
            apsLabels.put(lang.getCode(), bpmLabel);
        }
        this.getI18nManager().addLabelGroup(fieldName, apsLabels);
    }

    private AttributeInterface getAttributePrototype(String typeCode) {
        IEntityManager entityManager = this.getDataObjectManager();
        Map<String, AttributeInterface> attributeTypes = entityManager.getEntityAttributePrototypes();
        return attributeTypes.get(typeCode);
    }

    private void updateWidgetConfiguration(DatatableWidgetConfigDto widgetConf) {
        try {
            IPage page = this.getPageManager().getDraftPage(widgetConf.getPageCode());
            if (null == page) {
                throw new RestRourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", widgetConf.getPageCode());
            }
            if (widgetConf.getFramePosDraft() > page.getWidgets().length) {
                throw new RestRourceNotFoundException(ERRCODE_FRAME_INVALID, "frame", String.valueOf(widgetConf.getFramePosDraft()));
            }
            if (null == this.getWidgetTypeManager().getWidgetType(widgetConf.getWidgetType())) {
                throw new RestRourceNotFoundException(ERRCODE_WIDGET_INVALID, "widget", String.valueOf(widgetConf.getWidgetType()));
            }

            ApsProperties properties = new ApsProperties();
            properties.setProperty(PROP_NAME_DATA_TYPE, widgetConf.getDataType());
            properties.setProperty(PROP_NAME_DATA_UX_ID, String.valueOf(widgetConf.getDataUxId()));
            properties.setProperty(PROP_NAME_WIDGET_INFO_ID, String.valueOf(widgetConf.getId()));
            WidgetType widgetType = this.getWidgetTypeManager().getWidgetType(widgetConf.getWidgetType());
            Optional.ofNullable(widgetType).ifPresent(type -> type.getTypeParameters().forEach(param -> {
                String key = param.getName();
                switch (key) {
                    case PROP_NAME_DATA_TYPE:
                        properties.setProperty(PROP_NAME_DATA_TYPE, widgetConf.getDataType());
                        break;
                    case PROP_NAME_DATA_UX_ID:
                        properties.setProperty(PROP_NAME_DATA_UX_ID, String.valueOf(widgetConf.getDataUxId()));
                        break;
                    case PROP_NAME_WIDGET_INFO_ID:
                        properties.setProperty(PROP_NAME_WIDGET_INFO_ID, String.valueOf(widgetConf.getId()));
                        break;
                }
            }));
            Widget widget = new Widget();
            widget.setType(widgetType);
            widget.setConfig(properties);
            this.getPageManager().joinWidget(widgetConf.getPageCode(), widget, widgetConf.getFramePosDraft());

        } catch (ApsSystemException e) {
            logger.error("Error in update widget configuration {}", widgetConf.getPageCode(), e);
            throw new RestServerError("error in update widget configuration", e);
        }

    }

    public void deleteWidgetConfiguration(String pageCode, int frameId) {
        try {
            IPage page = this.getPageManager().getDraftPage(pageCode);
            if (null == page) {
                throw new RestRourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
            }
            if (frameId > page.getWidgets().length) {
                throw new RestRourceNotFoundException(ERRCODE_FRAME_INVALID, "frame", String.valueOf(frameId));
            }
            this.pageManager.removeWidget(pageCode, frameId);
        } catch (ApsSystemException e) {
            logger.error("Error in delete widget configuration for page {} and frame {}", pageCode, frameId, e);
            throw new RestServerError("error in delete widget configuration", e);
        }
    }

}
