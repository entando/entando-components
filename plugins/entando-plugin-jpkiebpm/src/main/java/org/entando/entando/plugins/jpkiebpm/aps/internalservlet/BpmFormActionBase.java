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
package org.entando.entando.plugins.jpkiebpm.aps.internalservlet;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.*;
import com.agiletec.aps.system.common.renderer.EntityWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.I18nManagerWrapper;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityAction;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobjectdispenser.IDataObjectDispenser;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectrenderer.DataObjectWrapper;
import org.entando.entando.aps.system.services.dataobjectrenderer.SystemInfoWrapper;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.util.KieApiUtil;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.EntityNaming;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FormToBpmHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.*;
import org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.helper.DataUXBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BpmFormActionBase extends AbstractApsEntityAction implements BeanFactoryAware {

    static String formAction = "";

    /*
    If the task form field name have spaces in the name it's necessary to replace
    these spaces with the REPLACE_SPACE_STRING below because the velocity parser
    don't allow to have spaces in the varibles identifiers.
    In the REPLACE_SPACE_STRING variable is not possible to use
    the underscore symbol  ( _ )  because is used in the section field generation.
     */
    protected static String REPLACE_SPACE_STRING = "-SP-";
    protected static String DATE_FORMAT_PATTERN = "yyyy-MM-dd hh:mm";
    private static final Logger logger = LoggerFactory.getLogger(BpmFormActionBase.class);
    private BeanFactory beanFactory = null;

    private String typeCode;
    private Lang _currentLang;
    private DataObject dataObject;
    private DataObjectModel dataModel;
    private IKieFormManager formManager;
    private IKieFormOverrideManager formOverrideManager;
    private II18nManager i18nManager;
    private IDataObjectManager dataObjectManager;
    private IDataObjectDispenser dataObjectDispenser;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;
    protected KieProcessFormQueryResult kpfr;

    //Widget init params
    private String widgetInfoId;
    private BpmWidgetInfo widgetInfo;
    private ApsProperties configOnline;
    private String processId;
    private String containerId;
    private String kieSourceId;
    private KieBpmConfig config;

    private Map<String, Object> valuesMap;

    private String configId;

    @Autowired
    private DataUXBuilder uXBuilder;

    @Override
    public void validate() {
        DataObject message = this.getDataObject();
        if (message == null) {
            return;
        }
        super.validate();
    }

    @Override
    public IApsEntity getApsEntity() {
        return this.getDataObject();
    }


    //This method should be overridden from sub classes
    @Override
    public String createNew() {
        return null;
    }

    @Override
    public String view() {
        //Operation not allowed
        return null;
    }

    public String render(DataObject dataobject, String contentModel, String langCode, RequestContext reqCtx) {
        String renderedEntity;
        try {
            Context velocityContext = new VelocityContext();
            DataObjectWrapper contentWrapper = (DataObjectWrapper) this.getEntityWrapper(dataobject);
            contentWrapper.setRenderingLang(langCode);
            velocityContext.put("data", contentWrapper);
            I18nManagerWrapper i18nWrapper = new I18nManagerWrapper(langCode, this.getI18nManager());
            velocityContext.put("i18n", i18nWrapper);
            SystemInfoWrapper systemInfoWrapper = new SystemInfoWrapper(reqCtx);
            velocityContext.put("info", systemInfoWrapper);
            StringWriter stringWriter = new StringWriter();
            boolean isEvaluated = Velocity.evaluate(velocityContext, stringWriter, "render", contentModel);
            if (!isEvaluated) {
                throw new ApsSystemException("Error rendering DataObject");
            }
            stringWriter.flush();
            renderedEntity = stringWriter.toString();
        } catch (Throwable t) {
            logger.error("Error rendering dataobject", t);
            renderedEntity = "";
        }
        return renderedEntity;
    }

    protected EntityWrapper getEntityWrapper(IApsEntity entity) {
        return new DataObjectWrapper((DataObject) entity, beanFactory);
    }

    @Override
    public String edit() {
        // Operation Not Allowed
        return null;
    }

    @Override
    public String save() {
        return null;
    }

    public String entryDataObject() {
        DataObject dataObject = this.getDataObject();
        if (dataObject == null) {
            return "expiredMessage";
        }
        return SUCCESS;
    }


    protected void setWidgetInitParameters() {
        try {
            widgetInfoId = this.extractWidgetConfig(KieBpmSystemConstants.WIDGET_PARAM_INFO_ID);
            widgetInfo = this.getBpmWidgetInfoManager().getBpmWidgetInfo(Integer.valueOf(widgetInfoId));
            configOnline = widgetInfo.getConfigOnline();
            processId = configOnline.getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_PROCESS_ID);
            containerId = configOnline.getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_CONTAINER_ID);
            kieSourceId = configOnline.getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_KIE_SOURCE_ID);
            config = formManager.getKieServerConfigurations().get(kieSourceId);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "save");
        }
    }

    protected void validateForm(Map<String, Object> params, KieProcessFormQueryResult kieForm) throws Throwable {
        for (Map.Entry<String, Object> ff : params.entrySet()) {
            String key = ff.getKey();
            String value = null;
            if (ff.getValue() != null) {
                value = ff.getValue().toString();
            }
            logger.debug("validateForm field '{}' value {}", key, value);
            Object obj = FormToBpmHelper.validateField(kieForm, key, value);
            if (null != obj) {
                if (obj instanceof NullFormField) {
                    logger.debug("the field '{}' is null (NullFormField), but is not mandatory: ignoring", key);
                } else {
                    logger.debug("the field '{}' is  NOT null, but is not NullFormField", key);
                    if (value.isEmpty()) {
                        logger.debug("Invalid input '{}' on field '{}'", value, key);
                        String msg = String.format("Invalid input '%s' on field '%s'", value, key);
                        this.addFieldError(key, msg);
                    }
                }
            } else {
                logger.debug("Invalid input '{}' on field '{}'", value, key);
                String msg = String.format("Invalid input '%s' on field '%s'", value, key);
                this.addFieldError(key, msg);
            }
        }
    }

    /**
     * Returns the current session DataObject.
     *
     * @return The current session DataObject.
     */
    public DataObject getDataObject() {
        if (this.dataObject == null) {
            try {
                String sessionParamName = this.getSessionParamName();
                HttpServletRequest request = (null != this.getRequest()) ? this.getRequest() : ServletActionContext.getRequest();
                this.dataObject = (DataObject) request.getSession().getAttribute(sessionParamName);
            } catch (Throwable t) {
                logger.error("getDataObject", t);
                throw new RuntimeException("Error finding DataObject", t);
            }
        }
        return dataObject;
    }

    public String getRenderedForm() {
        String output = null;
        try {
            RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
            Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
            output = this.render(dataObject, getDataModel().getShape(), currentLang.getCode(), reqCtx);
        } catch (Throwable t) {
            logger.error("getDataObject", t);
            throw new RuntimeException("Error finding DataObject", t);
        }
        return output;
    }

    /**
     * Sets the dataObject into the session.
     *
     * @param dataObject The dataObject to set into the session.
     */
    protected void setDataObjectOnSession(DataObject dataObject) {
        String sessionParamName = this.getSessionParamName();
        if (null == dataObject) {
            this.getRequest().getSession().removeAttribute(sessionParamName);
        } else {
            this.getRequest().getSession().setAttribute(sessionParamName, dataObject);
        }

        logger.info("********************* setDataObjectOnSession {}",dataObject.toString());
        this.dataObject = dataObject;
    }

    /**
     * Returns the name of the session parameter containing the current
     * dataObject.
     *
     * @return The name of the session parameter containing the current
     * dataObject.
     */
    protected String getSessionParamName() {
        String typeCode = this.getTypeCode();
        return "sessionParam_currentBpmForm_" + typeCode;
    }

    /**
     * Returns the current language from front-end context.
     *
     * @return The current language.
     */
    @Override
    public Lang getCurrentLang() {
        super.getCurrentLang();
        if (null == this._currentLang) {
            RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
            if (null != reqCtx) {
                this._currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
            }
            if (null == this._currentLang) {
                this._currentLang = this.getLangManager().getDefaultLang();
            }
        }
        return this._currentLang;
    }

    /**
     * Extract the typeCode from the current widget.
     *
     * @return The type code extracted from the widget.
     */
    protected String extractTypeCode() {
        return this.extractWidgetConfig(KieBpmSystemConstants.WIDGET_PARAM_DATA_TYPE_CODE);
    }

    protected String extractWidgetConfig(String paramName) {
        String value = null;
        try {
            Widget widget = this.extractCurrentWidget();
            if (null != widget) {
                ApsProperties config = widget.getConfig();
                if (null != config) {
                    String widgetParam = config.getProperty(paramName);
                    if (widgetParam != null && widgetParam.trim().length() > 0) {
                        value = widgetParam.trim();
                    }
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Error extracting param " + paramName, t);
        }
        return value;
    }

    protected Widget extractCurrentWidget() {
        try {
            HttpServletRequest request = (null != this.getRequest()) ? this.getRequest() : ServletActionContext.getRequest();
            RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
            if (null != reqCtx) {
                return (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
            }
        } catch (Throwable t) {
            throw new RuntimeException("Error extracting currentWidget ", t);
        }
        return null;
    }

    /**
     * Returns the DataObject type search filter.
     *
     * @return The DataObject type search filter.
     */
    public String getTypeCode() {
        if (null == this.typeCode) {
            this.typeCode = this.extractTypeCode();
        }
        return typeCode;
    }

    /**
     * Sets the DataObject type search filter.
     *
     * @param typeCode The message type search filter.
     */
    public void setTypeCode(String typeCode) {
        String extractedTypeCode = this.extractTypeCode();
        this.typeCode = (null == extractedTypeCode) ? typeCode : extractedTypeCode;
    }

    /**
     * Returns the list of system languages.
     *
     * @return The list of system languages.
     */
    public List<Lang> getLangs() {
        return this.getLangManager().getLangs();
    }

    protected IKieFormManager getFormManager() {
        return formManager;
    }

    public void setFormManager(IKieFormManager formManager) {
        this.formManager = formManager;
    }

    public IKieFormOverrideManager getFormOverrideManager() {
        return formOverrideManager;
    }

    public void setFormOverrideManager(IKieFormOverrideManager formOverrideManager) {
        this.formOverrideManager = formOverrideManager;
    }

    protected II18nManager getI18nManager() {
        return i18nManager;
    }

    public void setI18nManager(II18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    protected IDataObjectManager getDataObjectManager() {
        return dataObjectManager;
    }

    public void setDataObjectManager(IDataObjectManager dataObjectManager) {
        this.dataObjectManager = dataObjectManager;
    }

    public IDataObjectDispenser getDataObjectDispenser() {
        return dataObjectDispenser;
    }

    public void setDataObjectDispenser(IDataObjectDispenser dataObjectDispenser) {
        this.dataObjectDispenser = dataObjectDispenser;
    }

    public IBpmWidgetInfoManager getBpmWidgetInfoManager() {
        return bpmWidgetInfoManager;
    }

    public void setBpmWidgetInfoManager(IBpmWidgetInfoManager bpmWidgetInfoManager) {
        this.bpmWidgetInfoManager = bpmWidgetInfoManager;
    }

    @Override
    public boolean isValidLocaleString(String localeStr) {
        return super.isValidLocaleString(localeStr);
    }

    protected void generateForm(String modelDescription, String typeDescription, String urlParameters) throws Exception {
        logger.info("generateForm");

        String widgetInfoId = this.extractWidgetConfig(KieBpmSystemConstants.WIDGET_PARAM_INFO_ID);
        BpmWidgetInfo widgetInfo = this.getBpmWidgetInfoManager().getBpmWidgetInfo(Integer.valueOf(widgetInfoId));
        ApsProperties configOnline = widgetInfo.getConfigOnline();
        String processId = configOnline.getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_PROCESS_ID);
        String containerId = configOnline.getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_CONTAINER_ID);
        String kieSourceId = configOnline.getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_KIE_SOURCE_ID);

        KieBpmConfig config = formManager.getKieServerConfigurations().get(kieSourceId);

        String xml = widgetInfo.getInformationDraft();
        ApsProperties props = new ApsProperties();
        props.loadFromXml(xml);

        String title = containerId;

        Map<String, IApsEntity> types = this.getDataObjectManager().getEntityPrototypes();
        String typeCode;

        typeCode = EntityNaming.generateEntityName(types);
        dataObject = (DataObject) this.getDataObjectManager().getEntityClass().newInstance();
        dataObject.setTypeCode(typeCode);
        //dataObject.setTypeDescription(processId + "_" + containerId + "_" + taskId);
        dataObject.setTypeDescription(typeDescription);


        this.addAttributesToEntityType(dataObject, kpfr);

        List<KieProcess> processDefinitionsList = this.getFormManager().getProcessDefinitionsList(config);
        for (KieProcess proc : processDefinitionsList) {
            if (proc.getProcessId().equalsIgnoreCase(processId)) {
                title = proc.getProcessName();
            }
        }
        try {
            this.processTitle(title, this.getCurrentLang().getCode());
        } catch (ApsSystemException ex) {
            logger.error("Error setting the process title", ex);

        }

        dataModel = new DataObjectModel();
        dataModel.setDataType(typeCode);
        //dataModel.setDescription("Model for " + containerId + " and " + taskId);
        dataModel.setDescription(modelDescription);
        this.setDataObjectOnSession(dataObject);

        //String urlParameters = "&configId=" + configId + "&containerId=" + containerId + "&taskId=" + taskId;

        String dataUx = uXBuilder.createDataUx(kpfr, widgetInfo.getId(), containerId, processId, title, formAction, urlParameters);

        dataModel.setShape(dataUx);

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
                this.addAttributeToEntityType(entityType, field);
            }
            if (null != form.getNestedForms()) {
                for (KieProcessFormQueryResult subForm : form.getNestedForms()) {
                    logger.debug("addAttributeToEntityType for nested forms");
                    this.addAttributesToEntityType(entityType, subForm);
                }
            }
        }
    }

    private void addAttributeToEntityType(IApsEntity entityType, KieProcessFormField field) {
        logger.debug("addAttributeToEntityType for field {}", field.getName());
        String velocityNameField = field.getName().replaceAll(" ", REPLACE_SPACE_STRING);
        field.setName(velocityNameField);
        String fieldRequired = KieApiUtil.getFieldProperty(field, "fieldRequired");
        boolean req = (fieldRequired != null && fieldRequired.equalsIgnoreCase("true"));
        if (field.getType().equalsIgnoreCase("TextBox")
                || (field.getType().equalsIgnoreCase("TextArea"))
                || (field.getType().equalsIgnoreCase("InputText"))
                || (field.getType().equalsIgnoreCase("ListBox"))
                || (field.getType().equalsIgnoreCase("RadioGroup"))
                || (field.getType().equalsIgnoreCase("MultipleSubForm"))) {
            MonoTextAttribute text = (MonoTextAttribute) this.getAttributePrototype("Monotext");
            text.setName(field.getName());
            text.setDefaultLangCode(this.getCurrentLang().getCode());
            text.setRequired(req);
            logger.info("------------------------------------------------");
            logger.info("valuesMap: {}", valuesMap);
            logger.info("valuesMap.getOrDefault(field.getName(),\"\"): {}", valuesMap.getOrDefault(field.getName(),""));

            text.setText((String) valuesMap.getOrDefault(field.getName(),""));
            entityType.addAttribute(text);
        }
        if (field.getType().equalsIgnoreCase("IntegerBox") || field.getType().equalsIgnoreCase("InputTextInteger")
                || field.getType().equalsIgnoreCase("InputTextFloat") || field.getType().equalsIgnoreCase("FloatBox")
                || field.getType().equalsIgnoreCase("DecimalBox")) {
            NumberAttribute number = (NumberAttribute) this.getAttributePrototype("Number");
            number.setName(velocityNameField);
            number.setName(field.getName());
            number.setDefaultLangCode(this.getCurrentLang().getCode());
            number.setRequired(req);
            Object val = valuesMap.getOrDefault(field.getName(),null);

            if (null != val) {
                logger.debug("VAL CLASS: {}", val.getClass());
                String valString = val.toString();

                if (field.getType().equalsIgnoreCase("DecimalBox")) {
                    Double doubleValue = Double.valueOf(valString);
                    //Check if the value have decimals or not, If it have decimal get Double.valueOf to show decimals in the textbox
                    // else get BigDecimal.valueOf to not show the ".0" decimal values
                    if (doubleValue % 1.0 > 0) {
                        number.setValue(BigDecimal.valueOf(Double.valueOf(valString)));
                    } else {
                        number.setValue(BigDecimal.valueOf(Long.valueOf(valString)));
                    }
                } else {
                    number.setValue(BigDecimal.valueOf(Long.valueOf(valString)));
                }
            }
            entityType.addAttribute(number);
        }

        if (field.getType().equalsIgnoreCase("CheckBox")) {
            BooleanAttribute bool = (BooleanAttribute) this.getAttributePrototype("Boolean");
            bool.setName(field.getName());
            bool.setDefaultLangCode(this.getCurrentLang().getCode());
            bool.setRequired(req);
            Object val = valuesMap.getOrDefault(field.getName(), false);

            if (val instanceof Boolean) {
                bool.setBooleanValue((Boolean) val);
            } else {
                String valString = (String) val;
                bool.setBooleanValue((Boolean.valueOf(valString)));
            }
            entityType.addAttribute(bool);
        }
        if (field.getType().equalsIgnoreCase("DatePicker")) {
            TimestampAttribute dateAttribute = (TimestampAttribute) this.getAttributePrototype("Timestamp");
            dateAttribute.setName(field.getName());
            dateAttribute.setDefaultLangCode(this.getCurrentLang().getCode());
            dateAttribute.setRequired(req);
            String dateValueString = (String) valuesMap.getOrDefault(field.getName(), null);
            if (null != dateValueString && !(dateValueString.equals("null"))) {
                Date dateVal = null;
                try {
                    dateVal = new SimpleDateFormat(DATE_FORMAT_PATTERN).parse(dateValueString);
                } catch (ParseException e) {
                    logger.error("DatePicker ParseException: {}", e);
                }
                dateAttribute.setDate(dateVal);
            }
            entityType.addAttribute(dateAttribute);
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
            //TODO Add values here for velocity mapping
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
        String fieldName = KieApiUtil.getI18nLabelProperty(field);
        if (org.apache.commons.lang.StringUtils.isNotBlank(bpmLabel)) {
            if (null == this.getI18nManager().getLabel(fieldName, langCode)) {
                this.saveEntandoLabel(fieldName, bpmLabel);
            }
        } else {
            if (null == this.getI18nManager().getLabel(fieldName, langCode)) {
                fieldName = StringUtils.capitalize(field.getName().replace("_"," "));

                int index =fieldName.lastIndexOf(" ");
                fieldName = fieldName.substring(index);

                this.saveEntandoLabel(fieldName, StringUtils.capitalize(field.getName().replace("_"," ")));
            }
        }
    }

    private void processForm(KieProcessFormField field, String langCode) throws ApsSystemException {
        String formLabel = KieApiUtil.getI18nFormLabelProperty(field);
        if (org.apache.commons.lang.StringUtils.isNotBlank(formLabel)) {
            String formValue = formValue = KieApiUtil.getI18nFormLabelValue(field);
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

    protected Map<String, Object> getValuesMap(JSONObject inputData, String parentKey, String oldParentKey, final KieProcessFormQueryResult form) {
        return null;
    }


    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public DataObjectModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(DataObjectModel dataModel) {
        this.dataModel = dataModel;
    }

    @Override
    public void setBeanFactory(BeanFactory bf) throws BeansException {
        this.beanFactory = bf;
    }

    public KieProcessFormQueryResult getKpfr() {
        return kpfr;
    }

    public void setKpfr(KieProcessFormQueryResult kpfr) {
        this.kpfr = kpfr;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getKieSourceId() {
        return kieSourceId;
    }

    public void setKieSourceId(String kieSourceId) {
        this.kieSourceId = kieSourceId;
    }

    public KieBpmConfig getConfig() {
        return config;
    }

    public void setConfig(KieBpmConfig config) {
        this.config = config;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public Map<String, Object> getValuesMap() {
        return valuesMap;
    }

    public void setValuesMap(Map<String, Object> valuesMap) {
        this.valuesMap = valuesMap;
    }

    public static String getFormAction() {
        return formAction;
    }

    public static void setFormAction(String formAction) {
        BpmFormActionBase.formAction = formAction;
    }
}
