/*
 * The MIT License
 *
 * Copyright 2017 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiError;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.api.model.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiForm;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiInputForm;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiProcessStart;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiSignal;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.util.KieApiUtil;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FormToBpmHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessInstance;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.NullFormField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class KieApiManager extends AbstractService implements IKieApiManager {

    private static final Logger logger = LoggerFactory.getLogger(KieApiManager.class);

    @Override
    public void init() throws Exception {
        /**/
    }

    private void initFieldMandatory() {
        fieldMandatory = new HashMap<>();
        fieldMandatory.put("processInstanceId", Boolean.TRUE);
        fieldMandatory.put("id", Boolean.TRUE);
        fieldMandatory.put("processDefinitionId", Boolean.TRUE);
    }

    protected Map<String, KieFormOverride> getFormOverridesMap(String containerId, String processId, String overrides) throws ApsSystemException {
        Map<String, KieFormOverride> map = new HashMap<>();
        String[] overridesList = new String[0];
        if (null != overrides) {
            overridesList = overrides.split(",");
        }
        List<KieFormOverride> overrideList = this.getKieFormOverrideManager().getFormOverrides(containerId, processId);
        if (null != overrideList) {
            for (KieFormOverride override : overrideList) {
                if (ArrayUtils.contains(overridesList, String.valueOf(override.getId()))) {
                    map.put(override.getField(), override);
                }
            }
        }
        return map;
    }

    @Override
    public KieApiForm getBpmForm(final Properties properties) throws ApiException, ApsSystemException {
        KieApiForm form = null;

        final String configId = properties.getProperty("configId");

        if (null != configId) {
            final BpmWidgetInfo bpmWidgetInfo = bpmWidgetInfoManager.getBpmWidgetInfo(Integer.parseInt(configId));

            final String information = bpmWidgetInfo.getInformationOnline();
            if (StringUtils.isNotEmpty(information)) {

                final ApsProperties config = new ApsProperties();
                try {
                    config.loadFromXml(information);
                } catch (IOException e) {
                    logger.error("Error load configuration  {} ", e.getMessage());
                }

                final String containerId = config.getProperty("containerId");
                final String processId = config.getProperty("processId");
                final String overrideList = config.getProperty("overrides");
                String langCode = properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER);
                KieProcessFormQueryResult processForm = this.getKieFormManager().getProcessForm(containerId, processId);
                if (null == processForm) {
                    String msg = String.format("No form found with containerId %s and processId %s does not exist", containerId, processId);
                    throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, msg, Response.Status.CONFLICT);
                }

                this.setLabels(processForm, langCode);
                form = KieApiUtil.createForm(processForm, this.getI18nManager(), langCode, this.getFormOverridesMap(containerId, processId, overrideList));
                form.setProcessId(processId);
                form.setContainerId(containerId);
            }

        }

        return form;
    }

    @Override
    public void postBpmForm(final KieApiInputForm form) throws ApiException, ApsSystemException {
        if (null == form) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Form null", Response.Status.CONFLICT);
        }
        KieProcessFormQueryResult kieForm = this.getKieFormManager().getProcessForm(form.getContainerId(), form.getProcessId());
        if (null == kieForm) {
            String msg = String.format("No form found with container_id '%s' and process_id '%s'", form.getContainerId(), form.getProcessId());
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, msg, Response.Status.CONFLICT);
        }
        List<ApiError> validationResult = new ArrayList<>();
        Map<String, Object> toBpm = null;
        try {
            toBpm = validateForm(form.getParamsMap(), kieForm, validationResult);
        } catch (Throwable throwable) {
            logger.error("Failed to post bpm form ",throwable);
        }
        if (!validationResult.isEmpty()) {
            throw new ApiException(validationResult);
        }
        this.getKieFormManager().startProcessSubmittingForm(kieForm, form.getContainerId(), form.getProcessId(), toBpm);

    }

    @Override
    public List<KieProcessInstance> getInstanceProcessesList(Properties properties) throws Throwable {
        String processId = properties.getProperty("processId");

        try {
            List<KieProcessInstance> list = this.getKieFormManager().getProcessInstancesList(processId, 0, 5000);
            return list;
        } catch (ApsSystemException t) {
            String msg = String.format("No error getting the list of processes of type '{}'", processId);
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, msg, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<KieProcessInstance> processList(Properties properties) throws Throwable {
        final String page = properties.getProperty("page");
        final String pageSize = properties.getProperty("pageSize");
        Map<String, String> opt = new HashMap<String, String>();

        try {
            if (StringUtils.isNotBlank(page)) {
                opt.put("page", page);
            }
            if (StringUtils.isNotBlank(pageSize)) {
                opt.put("pageSize", pageSize);
            }
            List<KieProcessInstance> list
                    = this.getKieFormManager().getAllProcessInstancesList(opt);
            return list;
        } catch (ApsSystemException t) {
            logger.error("Failed to get process list in kie ",t);
            String msg = String.format("No error getting the list of processes");
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, msg, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JAXBProcessInstanceList processInstancesDataTable(Properties properties) throws Throwable {
        final String pageString = properties.getProperty("page");
        final String pageSizeString = properties.getProperty("pageSize");
        final String configId = properties.getProperty("configId");
        int page = 0, pageSize = 5000;
        try {
            if (StringUtils.isNotBlank(pageString)) {
                page = Integer.valueOf(pageString);
            }
            if (StringUtils.isNotBlank(pageSizeString)) {
                pageSize = Integer.valueOf(pageSizeString);
            }

            if (null != configId) {
                try {
                    final BpmWidgetInfo bpmWidgetInfo = bpmWidgetInfoManager.getBpmWidgetInfo(Integer.parseInt(configId));
                    final String information = bpmWidgetInfo.getInformationDraft();
                    if (StringUtils.isNotEmpty(information)) {
                        final ApsProperties config = new ApsProperties();
                        try {
                            config.loadFromXml(information);
                        } catch (IOException e) {
                            logger.error("Error load configuration  {} ", e.getMessage());
                        }
                        final JAXBProcessInstanceList processList = new JAXBProcessInstanceList();
                        this.setElementDatatableFieldDefinition(config, processList);
                        this.setElementList(config, processList);
                        processList.setContainerId(config.getProperty("containerId"));
                        processList.setProcessId(config.getProperty("processId"));
                        return processList;
                    }
                } catch (ApsSystemException e) {
                    logger.error("Error {}", e);

                }
            }
            return null;
        } catch (Throwable t) {
            logger.error("Failed to get process instances data table ",t);
            String msg = String.format("No error getting the list of processes");
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, msg, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private void setElementDatatableFieldDefinition(final ApsProperties config, final JAXBProcessInstanceList processList) {
        final String PREFIX_FIELD = "field_";
        final String PREFIX_VISIBLE = "visible_";
        final String PREFIX_OVERRIDE = "override_";
        final String PREFIX_POSITION = "position_";

        initFieldMandatory();
        for (final Map.Entry entry : config.entrySet()) {
            final boolean isPropertyField = ((String) entry.getKey()).startsWith(PREFIX_FIELD);
            if (isPropertyField) {
                final String fieldName = ((String) entry.getKey()).replace(PREFIX_FIELD, "").trim();
                final boolean propertyIsVisible = Boolean.valueOf(config.getProperty(PREFIX_VISIBLE + fieldName));
                if (propertyIsVisible) {
                    final DatatableFieldDefinition.Field field = new DatatableFieldDefinition.Field();
                    String title = config.getProperty(PREFIX_OVERRIDE + fieldName) == null ? fieldName : config.getProperty(PREFIX_OVERRIDE + fieldName);
                    field.setTitle(title);
                    field.setData(fieldName);
                    field.setVisible(Boolean.valueOf(true));
                    field.setPosition(Byte.parseByte(config.getProperty(PREFIX_POSITION + fieldName)));
                    processList.getDatatableFieldDefinition().addField(field);

                } else if (isMandatory(fieldName)) {
                    final DatatableFieldDefinition.Field field = new DatatableFieldDefinition.Field();
                    field.setTitle(fieldName);
                    field.setData(fieldName);
                    field.setVisible(Boolean.valueOf(false));
                    field.setPosition(Byte.parseByte(config.getProperty(PREFIX_POSITION + fieldName)));
                    processList.getDatatableFieldDefinition().addField(field);
                }
            }
        }
        Collections.sort(processList.getDatatableFieldDefinition().getFields(), new Comparator<DatatableFieldDefinition.Field>() {
            @Override
            public int compare(DatatableFieldDefinition.Field o1, DatatableFieldDefinition.Field o2) {
                return o1.getPosition().compareTo(o2.getPosition());
            }
        });
    }

    private boolean isMandatory(String fieldName) {
        return fieldMandatory.containsKey(fieldName);
    }

    private void setElementList(final ApsProperties config, final JAXBProcessInstanceList processList) throws ApsSystemException {
        final String groups = config.getProperty("groups") != null ? config.getProperty("groups").replace(" ", "") : "";
        final String processId = config.getProperty("processId");
        final List<JAXBProcessInstance> list = new ArrayList<>();
        final List<KieProcessInstance> rawList = this.getKieFormManager().getProcessInstancesList(processId, 0, 5000);
        for (final KieProcessInstance process : rawList) {
            list.add(new JAXBProcessInstance(process));
        }
        processList.setList(list);
    }

    public JAXBProcessInstanceListPlus processInstancesDataTablePlus(Properties properties) throws Throwable {
        final String pageString = properties.getProperty("page");
        final String pageSizeString = properties.getProperty("pageSize");
        final String configId = properties.getProperty("configId");
        final String procIstId = properties.getProperty("processInstanceId");

        Map<String, String> opt = new HashMap();

        try {
            if (StringUtils.isNotBlank(pageString)) {
                opt.put("page", pageString);
            }
            if (StringUtils.isNotBlank(pageSizeString)) {
                opt.put("pageSize", pageSizeString);
            } else {
                opt.put("pageSize", "5000");
            }
            BpmWidgetInfo bpmWidgetInfo = null;
            if (null != configId) {
                bpmWidgetInfo = bpmWidgetInfoManager.getBpmWidgetInfo(Integer.parseInt(configId));
            } else {
                int id = bpmWidgetInfoManager.getBpmWidgetInfos().get(0);
                if (id > 0) {
                    bpmWidgetInfo = bpmWidgetInfoManager.getBpmWidgetInfo(id);
                } else {
                    return null;
                }
            }
            try {

                final String information = bpmWidgetInfo.getInformationDraft();
                if (StringUtils.isNotEmpty(information)) {
                    final ApsProperties config = new ApsProperties();
                    try {
                        config.loadFromXml(information);
                    } catch (IOException e) {
                        logger.error("Error load configuration  {} ", e.getMessage());
                    }
                    final JAXBProcessInstanceListPlus processList = new JAXBProcessInstanceListPlus();
                    this.setElementDatatableFieldDefinition(config, processList);
                    this.setElementList(config, processList, procIstId, opt);
                    processList.setContainerId(config.getProperty("containerId"));
                    processList.setProcessId(config.getProperty("processId"));
                    return processList;
                }
            } catch (Exception e) {
                logger.error("Error {}", e);

            }
            return null;
        } catch (Throwable t) {
            logger.error("Failed to get processInstancesDataTablePlus ",t);
            String msg = String.format("No error getting the list of processes");
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, msg, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private void setElementDatatableFieldDefinition(ApsProperties config, JAXBProcessInstanceListPlus processList) {
        final String PREFIX_FIELD = "field_";
        final String PREFIX_VISIBLE = "visible_";
        final String PREFIX_OVERRIDE = "override_";
        final String PREFIX_POSITION = "position_";

        initFieldMandatory();
        for (final Map.Entry entry : config.entrySet()) {
            final boolean isPropertyField = ((String) entry.getKey()).startsWith(PREFIX_FIELD);
            if (isPropertyField) {
                final String fieldName = ((String) entry.getKey()).replace(PREFIX_FIELD, "").trim();
                final boolean propertyIsVisible = Boolean.valueOf(config.getProperty(PREFIX_VISIBLE + fieldName));
                if (propertyIsVisible) {
                    final DatatableFieldDefinition.Field field = new DatatableFieldDefinition.Field();
                    String title = config.getProperty(PREFIX_OVERRIDE + fieldName) == null ? fieldName : config.getProperty(PREFIX_OVERRIDE + fieldName);
                    field.setTitle(title);
                    field.setData(fieldName);
                    field.setVisible(Boolean.valueOf(true));
                    field.setPosition(Byte.parseByte(config.getProperty(PREFIX_POSITION + fieldName)));
                    processList.getDatatableFieldDefinition().addField(field);

                } else if (isMandatory(fieldName)) {
                    final DatatableFieldDefinition.Field field = new DatatableFieldDefinition.Field();
                    field.setTitle(fieldName);
                    field.setData(fieldName);
                    field.setVisible(Boolean.valueOf(false));
                    field.setPosition(Byte.parseByte(config.getProperty(PREFIX_POSITION + fieldName)));
                    processList.getDatatableFieldDefinition().addField(field);
                }
            }
        }
        Collections.sort(processList.getDatatableFieldDefinition().getFields(), new Comparator<DatatableFieldDefinition.Field>() {
            @Override
            public int compare(DatatableFieldDefinition.Field o1, DatatableFieldDefinition.Field o2) {
                return o1.getPosition().compareTo(o2.getPosition());
            }
        });
    }

    private void setElementList(ApsProperties config, JAXBProcessInstanceListPlus processList, String procIstId, Map<String, String> opt) {
        try {
            final String processId = config.getProperty("processId");
            final List<JAXBProcessInstancePlus> list = new ArrayList<>();
            Map<String, String> input = null;
            if (procIstId != null) {
                input = new HashMap<>();
                input.put("processInstanceId", procIstId);
            }
            final List<KieProcessInstance> rawList = this.getKieFormManager().getProcessInstancesWithClientData(input, opt).getInstances();
            for (final KieProcessInstance process : rawList) {
                list.add(new JAXBProcessInstancePlus(process));
            }
            processList.setList(list);
        } catch (Throwable ex) {
            java.util.logging.Logger.getLogger(KieApiManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processField(final KieProcessFormField field, final String langCode) throws ApsSystemException {
        final String bpmLabel = KieApiUtil.getFieldProperty(field, "label");
        if (StringUtils.isNotBlank(bpmLabel)) {
            final String fieldName = KieApiUtil.getI18nLabelProperty(field);
            if (null == this.getI18nManager().getLabel(fieldName, langCode)) {
                this.saveEntandoLabel(fieldName, bpmLabel);
            }
        }
    }

    protected void setLabels(final KieProcessFormQueryResult form, final String langCode) throws ApsSystemException {
        if (null != form && null != form.getFields()) {
            for (final KieProcessFormField field : form.getFields()) {
                processField(field, langCode);
            }
            if (null != form.getNestedForms()) {
                for (KieProcessFormQueryResult subForm : form.getNestedForms()) {
                    this.setLabels(subForm, langCode);
                }
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

    protected Map<String, Object> validateForm(Map<String, String> params, KieProcessFormQueryResult kieForm, List<ApiError> validationResult) throws Throwable {
        Map<String, Object> toBpm = new HashMap<>();
        for (Map.Entry<String, String> ff : params.entrySet()) {
            String key = ff.getKey();
            String value = ff.getValue();
            Object obj = FormToBpmHelper.validateField(kieForm, key, value);
            if (null != obj) {
                if (obj instanceof NullFormField) {
                    logger.info("the field '{}' is null, but is not mandatory: ignoring", key);
                } else {
                    toBpm.put(key, obj);
                }
            } else {
                String msg = String.format("Invalid input '%s' on field '%s'", value, key);
                validationResult.add(new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, msg, Response.Status.CONFLICT));
            }
        }
        return toBpm;
    }

    @Override
    public void postSignal(KieApiSignal signalObj) throws Throwable {
        this.getKieFormManager().sendSignal(signalObj.getContainerId(), signalObj.getProcessId(),
                signalObj.getSignal(), signalObj.getAccountId(), null);
    }

    @Override
    public void startNewProcess(KieApiProcessStart process) throws Throwable {
        this.getKieFormManager().startNewProcess(process, null);
    }

    protected II18nManager getI18nManager() {
        return i18nManager;
    }

    public void setI18nManager(II18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    protected ILangManager getLangManager() {
        return langManager;
    }

    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }

    protected IKieFormManager getKieFormManager() {
        return kieFormManager;
    }

    public void setKieFormManager(IKieFormManager kieFormManager) {
        this.kieFormManager = kieFormManager;
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

    private HashMap<String, Boolean> fieldMandatory;
    private II18nManager i18nManager;
    private ILangManager langManager;
    private IKieFormManager kieFormManager;
    private IKieFormOverrideManager kieFormOverrideManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;

}
