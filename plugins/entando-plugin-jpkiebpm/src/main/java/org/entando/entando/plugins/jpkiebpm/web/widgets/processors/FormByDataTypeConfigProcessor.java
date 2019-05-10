/*
 * The MIT License
 *
 * Copyright 2019 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.web.widgets.processors;

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
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import java.util.Map;

import com.agiletec.aps.util.ApsProperties;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.entando.entando.aps.system.services.widgettype.validators.WidgetConfigurationProcessor;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormOverrideInEditing;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.util.KieApiUtil;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.EntityNaming;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcess;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.DefaultValueOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.OverrideList;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.PlaceHolderOverride;
import org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.BpmSourceAndProcessSelectorHelper;
import org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.helper.DataUXBuilder;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public  class FormByDataTypeConfigProcessor implements WidgetConfigurationProcessor {

    private static final Logger logger = LoggerFactory.getLogger(FormByDataTypeConfigProcessor.class);

    public static final String WIDGET_CODE = "bpm-datatype-form";

    private String frame;
    private String pageCode;
    private String processId;
    private String containerId;
    private String processPath;
    private String knowledgeSourcePath;

    @Autowired
    private IKieFormManager formManager;

    @Autowired
    private IKieFormOverrideManager kieFormOverrideManager;

    @Autowired
    private IBpmWidgetInfoManager bpmWidgetInfoManager;

    @Autowired
    private IDataObjectManager dataObjectManager;

    @Autowired
    private IDataObjectModelManager dataObjectModelManager;

    @Autowired
    private II18nManager i18nManager;

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    @Autowired
    private ILangManager langManager;

    @Autowired
    private DataUXBuilder uXBuilder;

    private List<KieProcess> processes;
    private List<KieFormOverrideInEditing> overrides;
    private Integer overrideToDeleteIndex;
    private Widget widget;

    //private BpmSourceAndProcessSelectorHelper<BpmFormWidgetAction, KieProcess> selectorHelper;

    /*WidgetINFO XML:
            <property  key = "containerId" > evaluation_1.0.0-SNAPSHOT</property> 
            <property key = "kieSourceId" > @@form< / property> 
            <property key = "processId" > evaluation <  / property >
     */
    
    private static final String APP_BUILDER_WIDGET_CONFIG_KNOWLEDGE_SOURCE_KEY = "knowledgeSourcePath";
    private static final String APP_BUILDER_WIDGET_CONFIG_PROCESS_ID__KEY = "processPath";

    private static final String WIDGET_INFO_CONFIG_CONTAINER_ID_KEY = "containerId";
    private static final String WIDGET_INFO_CONFIG_KNOWLEDGE_SOURCE_KEY = "kieSourceId";
    private static final String WIDGET_INFO_CONFIG_PROCESS_ID_KEY = "processId";

    
    //CHANGE THIS - GET THIS FROM APPLICATION
    private String LANG = "en";

    List<WidgetTypeParameter> inputParameters;
    Map<String, String> inputParametersValues;

    @Override
    public boolean supports(String widgetCode) {
        return WIDGET_CODE.equals(widgetCode);
    }

    private void init(Map<String, Object> parameters) {
        logger.info("----------------------------------------------------");
        logger.info("init");
        
        logger.info("parameters  {}", parameters.toString());
      
        inputParameters = new ArrayList<>();
        inputParametersValues = new HashMap<>();
        frame = String.valueOf(parameters.get("frameId"));
        pageCode = String.valueOf(parameters.get("pageCode"));
        logger.info("frame  {}", frame);
        logger.info("pageCode  {}", pageCode);
        
        String overridesParameters = String.valueOf(parameters.get("overrides"));

        logger.info("overridesParameters  {}", overridesParameters);
        logger.info("----------------------------------------------------");

    }

    @SuppressWarnings("unchecked")
    @Override
    public Object buildConfiguration(WidgetConfigurationRequest widget,
                                     Map<String, Object> parameters) {
        logger.info("Build configuration for {}", WIDGET_CODE);
        parameters.forEach((key, value) -> {
            logger.info("parameters found: Key {} Value {}", key, value);
        });

        init(parameters);
        String propertyName;
        String propertyValue;

        ApsProperties properties = new ApsProperties();
        Map<String, Object> map = widget.getConfig();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            propertyName = entry.getKey();
            propertyValue = entry.getValue().toString();
            logger.debug("Widget Property found: k {} v {}", entry.getKey(), entry.getValue().toString());
            properties.put(entry.getKey(), entry.getValue().toString());
            if (propertyValue != null && propertyValue.trim().length() > 0) {
                WidgetTypeParameter wtp = new WidgetTypeParameter();
                wtp.setName(propertyName);
                inputParameters.add(wtp);
                inputParametersValues.put(propertyName, propertyValue);
            }
        }
        setProcessPath(inputParametersValues.get("processPath"));

        setKnowledgeSourcePath(inputParametersValues.get("knowledgeSourcePath"));

        logger.info("processPath {}", processPath);
        logger.info("knowledgeSourcePath {}", knowledgeSourcePath);

        ApsProperties ap = createWidget();

        logger.info("Build configuration return properties {}", ap);

        return ap;
    }

    protected void setPropertyWidget(final List<WidgetTypeParameter> parameters, final Widget widget) {
        for (final WidgetTypeParameter param : parameters) {
            String paramName = param.getName();
            String value = inputParametersValues.get(paramName);
            if (value != null && value.trim().length() > 0) {
                logger.debug("setPropertyWidget: propertyName {} value {}", paramName, value);
                widget.getConfig().setProperty(paramName, value);
            }
        }
    }

    @Override
    public ApsProperties extractConfiguration(ApsProperties widgetProperties) {
        logger.info("*********************************************");
        logger.info("extractConfiguration: widgetProperties {}", widgetProperties.toString());
        String widgetInfoId = widgetProperties.getProperty("widgetInfoId");
        logger.info("extractConfiguration: widgetInfoId {}", widgetInfoId.toString());
        BpmWidgetInfo bpmWidgetInfo = null;
        ApsProperties properties = new ApsProperties();
        try {
            bpmWidgetInfo = this.getBpmWidgetInfoManager().getBpmWidgetInfo(Integer.valueOf(widgetInfoId));
            logger.info("extractConfiguration: bpmWidgetInfo.getConfigOnline() {}", bpmWidgetInfo.getConfigOnline());
            logger.info("extractConfiguration: bpmWidgetInfo.getConfigDraft() {}", bpmWidgetInfo.getConfigDraft());
            
            properties.put(APP_BUILDER_WIDGET_CONFIG_PROCESS_ID__KEY, bpmWidgetInfo.getConfigDraft().get(WIDGET_INFO_CONFIG_PROCESS_ID_KEY)+"@"+bpmWidgetInfo.getConfigDraft().get(WIDGET_INFO_CONFIG_CONTAINER_ID_KEY));
            properties.put(APP_BUILDER_WIDGET_CONFIG_KNOWLEDGE_SOURCE_KEY, bpmWidgetInfo.getConfigDraft().get(WIDGET_INFO_CONFIG_KNOWLEDGE_SOURCE_KEY));

        } catch (ApsSystemException ex) {
            logger.error("ApsSystemException {}", ex);
        }
        logger.info("OUTPUT {}",properties);        
        logger.info("*********************************************");
        return properties;
    }

    /**
     * This method is called before each action.
     */
    /*
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
     */
//    @Override
    protected ApsProperties createWidget() {
        try {
            // If the widget is not new we need to retrieve the widgetInfoId but not
            // the WidgetInfo itself (because we are overwriting it) so here we call
            // super.extractInitConfig() instead of this.extractInitConfig()
            // zsuper.extractInitConfig();
            /*
        Widget widget = getWidget();
        if (widget == null) {
            widget = this.createNewWidget();
        }
             */
      
            
          //  this.extractInitConfig();
            
            Widget widget = widget = this.createNewWidget();
            
            
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

                    this.loadProcesses(config);
//                if (selectorHelper.loadProcesses()) {
                    for (KieProcess proc : processes) {
                        if (proc.getProcessId().equalsIgnoreCase(processId)) {
                            title = proc.getProcessName();
                            logger.info("********* title {}", title);

                        }
                    }
                    try {
                        logger.info("*********  set processTitle {}", title);
                        this.processTitle(title, LANG);
                    } catch (ApsSystemException ex) {
                        this.processTitle("*********  error reading title", LANG);

                        logger.error("Error {}", ex);

                    }
                    //              }
                    //            */
                  //  this.processTitle("TITLE TO FIX", LANG);

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
             
                //this.storeKieFormOverrides(widgetInfo.getId());

                //DataModel - end

            }

            widget.getConfig().setProperty(KieBpmSystemConstants.WIDGET_PARAM_INFO_ID, String.valueOf(widgetInfo.getId()));
            this.setWidget(widget);
        } catch (Exception ex) {
            logger.error("Exception {}", ex);
        }
        return widget.getConfig();
    }

    protected Widget createNewWidget() {
        /*if (this.getWidgetTypeCode() == null || this.getWidgetType(this.getWidgetTypeCode()) == null) {
            _logger.error("Widget Code missin or invalid : " + this.getWidgetTypeCode());
            //throw new Exception("Widget Code missin or invalid : " + this.getWidgetTypeCode());
            return null;
        }*/
        Widget widget = new Widget();
        WidgetType type = this.getWidgetType(this.getWidgetTypeCode());
        widget.setType(type);
        widget.setConfig(new ApsProperties());
        return widget;
    }

    public WidgetType getWidgetType(String typeCode) {
        return this.getWidgetTypeManager().getWidgetType(typeCode);
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
                        this.processForm(form.getFields().get(0), LANG);
                    } else {
                        String formName = null;
                        if (KieApiUtil.getFieldProperty(form.getProperties(), "name").contains(".")) {
                            formName = KieApiUtil.getFieldProperty(form.getProperties(), "name")
                                    .substring(0, KieApiUtil.getFieldProperty(form.getProperties(), "name").indexOf("."));
                        } else {
                            formName = KieApiUtil.getFieldProperty(form.getProperties(), "name");
                        }
                        String formLabel = KieApiUtil.getI18nFormLabelProperty(formName);
                        if (null == this.getI18nManager().getLabel(formLabel, LANG)) {
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
            text.setDefaultLangCode(LANG);
            text.setRequired(req);
            entityType.addAttribute(text);
        }
        if (field.getType().equalsIgnoreCase("IntegerBox") || field.getType().equalsIgnoreCase("InputTextInteger")
                || field.getType().equalsIgnoreCase("InputTextFloat") || field.getType().equalsIgnoreCase("FloatBox")) {
            NumberAttribute number = (NumberAttribute) this.getAttributePrototype("Number");
            number.setName(field.getName());
            number.setDefaultLangCode(LANG);
            number.setRequired(req);
            entityType.addAttribute(number);
        }

        if (field.getType().equalsIgnoreCase("CheckBox")) {
            BooleanAttribute bool = (BooleanAttribute) this.getAttributePrototype("Boolean");
            bool.setName(field.getName());
            bool.setDefaultLangCode(LANG);
            bool.setRequired(req);
            entityType.addAttribute(bool);
        }
        if (field.getType().equalsIgnoreCase("DatePicker")) {
            DateAttribute date = (DateAttribute) this.getAttributePrototype("Date");
            date.setName(field.getName());
            date.setDefaultLangCode(LANG);
            date.setRequired(req);
            entityType.addAttribute(date);
        }
        if (field.getType().equalsIgnoreCase("MultipleSelector")) {

            MonoTextAttribute text = (MonoTextAttribute) this.getAttributePrototype("Monotext");
            text.setName(field.getName());
            text.setDefaultLangCode(LANG);
            text.setRequired(req);

            MonoListAttribute list = (MonoListAttribute) this.getAttributePrototype("Monolist");
            list.setName(field.getName());
            list.setDefaultLangCode(LANG);
            list.setNestedAttributeType(text);
            list.setRequired(req);

            entityType.addAttribute(list);
        }

        try {
            this.processField(field, LANG);
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

    
   // @Override
    protected void extractInitConfig() {
    //    String result = super.extractInitConfig();
     //   this.setWidgetTypeCode(this.getWidget().getType().getCode());
        this.loadWidgetInfo();
       // return result;
    }
    
    protected void initSharedParameters(final BpmWidgetInfo widgetInfo) throws ApsSystemException {

        String widgetSourceId = widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_KIE_SOURCE_ID);
        String widgetProcessId = widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_PROCESS_ID);
        String widgetContainerId = widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_CONTAINER_ID);

        if (StringUtils.isNotBlank(widgetSourceId) && !widgetSourceId.equals("null")
                && StringUtils.isNotBlank(widgetProcessId) && !widgetProcessId.equals("null")
                && StringUtils.isNotBlank(widgetContainerId) && !widgetContainerId.equals("null")) {

            this.knowledgeSourcePath = widgetSourceId;

            //if (selectorHelper.loadProcesses()) {
                this.processId = widgetProcessId;
                this.containerId = widgetContainerId;
                this.processPath = String.format("%s@%s@%s", processId, containerId, knowledgeSourcePath);
                logger.info("Setting processPath to {}", processPath);

                this.loadKieFormOverrides(widgetInfo.getId());
            //}
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
     
 /* @Override
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
     */
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

    /*
    @Override
    public Map<String, KieBpmConfig> getKnowledgeSource() {
        return knowledgeSource;
    }

    @Override
    public void setKnowledgeSource(Map<String, KieBpmConfig> sources) {
        this.knowledgeSource = sources;
    }
     */
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
            int frameInt = Integer.valueOf(this.getFrame());
            widgetInfo.setFramePosDraft(frameInt);
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


        logger.info("-----------------------------------------------------------------");

        logger.info("storeKieFormOverrides fo widgetInfoId {}",widgetInfoId);
        
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

        logger.info("overrides {}",overrides);

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
        logger.info("-----------------------------------------------------------------");
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
        logger.info("getEditedKieFormOverride kieFormOverride {}", kieFormOverride);
        
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

    /*
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
     */
    public void loadProcesses(KieBpmConfig config) throws ApsSystemException {
        processes = this.formManager.getProcessDefinitionsList(config);
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    // @Override
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

    //  @Override
    public String getKnowledgeSourcePath() {
        return knowledgeSourcePath;
    }

    //  @Override
    public void setKnowledgeSourcePath(String knowledgeSourcePath) {
        this.knowledgeSourcePath = knowledgeSourcePath;
    }

    //   @Override
    public String getProcessPath() {
        return processPath;
    }

    //   @Override
    public void setProcessPath(String processPath) {
        this.processPath = processPath;
    }

    //  @Override
    public List<KieProcess> getProcess() {
        return processes;
    }

    //   @Override
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

    public void addFormOverride() {
        try {
            //  selectorHelper.loadProcesses();
            if (overrides == null) {
                overrides = new ArrayList<>();
            }
            overrides.add(new KieFormOverrideInEditing());
        } catch (Throwable t) {
            logger.error("Error in addFormOverride", t);
        }
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

    public void deleteFormOverride() {
        try {
            //   selectorHelper.loadProcesses();
            if (overrideToDeleteIndex != null) {
                overrides.remove((int) overrideToDeleteIndex);
            }
        } catch (Throwable t) {
            logger.error("Error in deleteOverride", t);
        }
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    public String getWidgetTypeCode() {
        return WIDGET_CODE;
    }

    protected IWidgetTypeManager getWidgetTypeManager() {
        return widgetTypeManager;
    }

    public ILangManager getLangManager() {
        return langManager;
    }

    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

}
