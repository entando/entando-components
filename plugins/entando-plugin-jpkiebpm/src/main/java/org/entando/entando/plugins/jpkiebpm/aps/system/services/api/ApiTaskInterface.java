/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.api;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsProperties;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.api.model.DatatableFieldDefinition;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.api.model.JAXBTask;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.api.model.JAXBTaskList;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.KieApiManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiForm;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiInputFormTask;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiProcessStart;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.task.KiaApiTaskDoc;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.task.KiaApiTaskState;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.util.KieApiUtil;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FSIDemoHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTask;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTaskDetail;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

/**
 * @author E.Santoboni
 */
public class ApiTaskInterface extends KieApiManager {

    private static final Logger logger = LoggerFactory.getLogger(ApiTaskInterface.class);

    private static boolean busy = false;

    private IBpmWidgetInfoManager bpmWidgetInfoManager;

    private HashMap<String, Boolean> fieldMandatory;

    private void initFieldMandatory() {
        fieldMandatory = new HashMap<>();
        fieldMandatory.put("processInstanceId", Boolean.TRUE);
        fieldMandatory.put("id", Boolean.TRUE);
        fieldMandatory.put("processDefinitionId", Boolean.TRUE);
    }

    public JAXBTask getTask(Properties properties) throws Throwable {
        JAXBTask resTask = null;
        final String idString = properties.getProperty("id");
        final String page = properties.getProperty("page");
        final String pageSize = properties.getProperty("pageSize");
        final String user = properties.getProperty("user");
        HashMap<String, String> opt = new HashMap<>();
        int id; // parameter appended to the original payload

        try {
            id = Integer.parseInt(idString);
            if (StringUtils.isNotBlank("page")) {
                opt.put("page", page);
            }
            if (StringUtils.isNotBlank("pageSize")) {
                opt.put("pageSize", pageSize);
            }
            if (StringUtils.isNotBlank("user")) {
                opt.put("user", user);
            }
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid number format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
        }
        
        try {
	        List<KieTask> rawList = this.getKieFormManager().getHumanTaskList("", opt);
	        for (KieTask task : rawList) {
	            if (id == task.getId()) {
	                resTask = new JAXBTask(task);
	                break;
	            }
	        }
	        if (null == resTask) {
	            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Task with id '" + idString + "' does not exist", Response.Status.CONFLICT);
	        }
        } catch (Exception ex) {
        		logger.error("Error in getTask", ex);
        		throw ex;
        }
        return resTask;
    }

    public JAXBTaskList getUserTask(Properties properties) throws Throwable {

        final String user = properties.getProperty("user");
        HashMap<String, String> opt = new HashMap<>();
        int id; // parameter appended to the original payload

        if (StringUtils.isNotBlank("user")) {
            opt.put("user", user);
        }

        List<KieTask> rawList = this.getKieFormManager().getHumanTaskList("", opt);
        final JAXBTaskList taskList = new JAXBTaskList();
        List<JAXBTask> list = new ArrayList<>();
        for (KieTask raw : rawList) {
            JAXBTask task = new JAXBTask(raw);
            list.add(task);
            taskList.setContainerId(task.getContainerId());
            taskList.setOwner(user);
            taskList.setProcessId(task.getProcessDefinitionId());
        }
        taskList.setList(list);
        this.startTasks(rawList, opt);
        if (taskList.getList().isEmpty()) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Tasks for user '" + user + "' does not exist", Response.Status.CONFLICT);
        }
        return taskList;
    }

    public JAXBTaskList getLegalWorkerTask(Properties properties) throws Throwable {
        HashMap<String, String> opt = new HashMap<>();
        final String pageSize = properties.getProperty("pageSize");
        final String page = properties.getProperty("page");
        final String user = properties.getProperty("user");
        int id; // parameter appended to the original payload

        if (StringUtils.isNotBlank(page)) {
            opt.put("page", page);
        }
        if (StringUtils.isNotBlank(pageSize)) {
            opt.put("pageSize", pageSize);
        } else {
            opt.put("pageSize", "5000");
        }

        List<KieTask> rawList = this.getKieFormManager().getLegalWorkerTaskList(opt);
        final JAXBTaskList taskList = new JAXBTaskList();
        List<JAXBTask> list = new ArrayList<>();
        if (null != rawList
                && !rawList.isEmpty()) {
            for (KieTask raw : rawList) {
                JAXBTask task = new JAXBTask(raw);
                list.add(task);
                taskList.setContainerId(task.getContainerId());
                taskList.setOwner(user);
                taskList.setProcessId(task.getProcessDefinitionId());
            }
        }
        taskList.setList(list);
        return taskList;
    }

    public JAXBTaskList getKnowledgeWorkerTask(Properties properties) throws Throwable {
        HashMap<String, String> opt = new HashMap<>();
        final String pageSize = properties.getProperty("pageSize");
        final String page = properties.getProperty("page");
        final String user = properties.getProperty("user");
        int id; // parameter appended to the original payload

        if (StringUtils.isNotBlank(page)) {
            opt.put("page", page);
        }
        if (StringUtils.isNotBlank(pageSize)) {
            opt.put("pageSize", pageSize);
        } else {
            opt.put("pageSize", "5000");
        }

        List<KieTask> rawList = this.getKieFormManager().getKnowledgeWorkerTaskList(opt);
        final JAXBTaskList taskList = new JAXBTaskList();
        List<JAXBTask> list = new ArrayList<>();
        if (null != rawList
                && !rawList.isEmpty()) {
            for (KieTask raw : rawList) {
                JAXBTask task = new JAXBTask(raw);
                list.add(task);
                taskList.setContainerId(task.getContainerId());
                taskList.setOwner(user);
                taskList.setProcessId(task.getProcessDefinitionId());
            }
        }
        taskList.setList(list);
        return taskList;
    }

    public String getDiagram(Properties properties) {
        final String configId = properties.getProperty("configId");
        if (null != configId) {
            try {
                final BpmWidgetInfo bpmWidgetInfo = bpmWidgetInfoManager.getBpmWidgetInfo(Integer.parseInt(configId));
                final String information = bpmWidgetInfo.getInformationDraft();
                if (StringUtils.isNotEmpty(information)) {
                    final ApsProperties config = new ApsProperties();
                    config.loadFromXml(information);
                    final String containerId = config.getProperty("containerId");
                    final String processInstanceId = properties.getProperty("processInstanceId");
                    return this.getKieFormManager().getProcInstDiagramImage(containerId, processInstanceId);
                }
            } catch (ApsSystemException e) {
                logger.error("Error {}", e.getMessage());

            } catch (IOException e) {
                logger.error("Error load configuration  {} ", e.getMessage());
            }
        }
        return "";

    }

    public JAXBTaskList getTasks(Properties properties) {
        final String configId = properties.getProperty("configId");

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
                    final JAXBTaskList taskList = new JAXBTaskList();
                    this.setElementDatatableFieldDefinition(config, taskList);
                    this.setElementList(config, taskList);
                    taskList.setContainerId(config.getProperty("containerId"));
                    taskList.setProcessId(config.getProperty("processId"));
                    return taskList;
                }
            } catch (ApsSystemException e) {
                logger.error("Error {}", e);

            }
        }
        return null;
    }

    private void setElementDatatableFieldDefinition(final ApsProperties config, final JAXBTaskList taskList) {
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
                    taskList.getDatatableFieldDefinition().addField(field);

                } else if (isMandatory(fieldName)) {
                    final DatatableFieldDefinition.Field field = new DatatableFieldDefinition.Field();
                    field.setTitle(fieldName);
                    field.setData(fieldName);
                    field.setVisible(Boolean.valueOf(false));
                    field.setPosition(Byte.parseByte(config.getProperty(PREFIX_POSITION + fieldName)));
                    taskList.getDatatableFieldDefinition().addField(field);
                }
            }
        }
        Collections.sort(taskList.getDatatableFieldDefinition().getFields(), new Comparator<DatatableFieldDefinition.Field>() {
            @Override
            public int compare(DatatableFieldDefinition.Field o1, DatatableFieldDefinition.Field o2) {
                return o1.getPosition().compareTo(o2.getPosition());
            }
        });
    }

    private boolean isMandatory(String fieldName) {
        return fieldMandatory.containsKey(fieldName);
    }

    private void setElementList(final ApsProperties config, final JAXBTaskList taskList) throws ApsSystemException {
        final String groups = config.getProperty("groups").replace(" ", "");
        final List<JAXBTask> list = new ArrayList<>();
        final List<KieTask> rawList = this.getKieFormManager().getHumanTaskList(groups, null);
        for (final KieTask task : rawList) {
            list.add(new JAXBTask(task));
        }
        taskList.setList(list);
    }

    public KieTaskDetail getTaskDetail(Properties properties) throws Throwable {
        String containerId = properties.getProperty("containerId");
        String taskIdString = properties.getProperty("taskId");
        String user = properties.getProperty("user");
        Map<String, String> opt = null;
        if (StringUtils.isNotBlank(user)) {
            opt = new HashMap<>();
            opt.put("user", user);
        }
        KieTaskDetail taskDetail = this.getKieFormManager().getTaskDetail(containerId, Long.valueOf(taskIdString), opt);
        if (null == taskDetail) {
            String msg = String.format("No form found with containerId %s and taskId %s does not exist", containerId, taskIdString);
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, msg, Response.Status.CONFLICT);
        }
        return taskDetail;
    }

    public KieApiForm getTaskForm(Properties properties) throws Throwable {
        String containerId = properties.getProperty("containerId");
        String taskIdString = properties.getProperty("taskId");
        String langCode = properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER);
        KieApiForm form = null;
        KieProcessFormQueryResult processForm = this.getKieFormManager().getTaskForm(containerId, Long.valueOf(taskIdString));

        JSONObject taskData = this.getKieFormManager().getTaskFormData(containerId, Long.valueOf(taskIdString), null);

        mergeTaskData(taskData, processForm);

        if (null == processForm) {
            String msg = String.format("No form found with containerId %s and taskId %s does not exist", containerId, taskIdString);
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, msg, Response.Status.CONFLICT);
        }
        String processId = processForm.getHolders().get(0).getValue();
        try {
            this.setLabels(processForm, langCode);
            form = KieApiUtil.createForm(processForm, this.getI18nManager(), langCode, this.getFormOverridesMap(containerId, processId, null));
            form.setTaskId(taskIdString);
            form.setContainerId(containerId);
            form.setProcessId(processId);
        } catch (Exception e) {
            logger.error("Failed to create kie form ",e);
        }
        return form;
    }

    public KieApiProcessStart getTaskInputOutput(Properties properties) {
        try {
            String containerId = properties.getProperty("containerId");
            String taskIdString = properties.getProperty("taskId");
            String user = properties.getProperty("user");
            Map<String, String> opt = null;
            if (StringUtils.isNotBlank(user)) {
                opt = new HashMap<>();
                opt.put("user", user);
            }
            JSONObject processForm = this.getKieFormManager().getTaskFormData(containerId, Long.valueOf(taskIdString), opt);
            KieApiProcessStart form = new KieApiProcessStart();
            form = FSIDemoHelper.replaceValuesFromJson(processForm, form);
            return form;
        } catch (ApsSystemException ex) {
            logger.error("Failed to get task input output ",ex);
        }
        return null;
    }

    public void postTaskForm(final KieApiInputFormTask form) throws Throwable {
        if (null == form) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Form null", Response.Status.CONFLICT);
        }
        String containerId = null;
        String taskId = null;
        String processId = null;
        Map<String, String> input = new HashMap<>();
        for (KieApiInputFormTask.Field field : form.getFields()) {
        		logger.info(">>> KieApiInputFormTask field {} = {}", field.getName(), field.getValue());
            if (field.getName().equalsIgnoreCase("containerId")) {
                containerId = field.getValue();
            }
            if (field.getName().equalsIgnoreCase("taskId")) {
                taskId = field.getValue();
            }
            if (field.getName().equalsIgnoreCase("processId")) {
            		processId = field.getValue();
            }
            input.put(field.getName().replace(KieApiField.FIELD_NAME_PREFIX, ""), field.getValue());
        }
        final String result = this.getKieFormManager().completeHumanFormTask(containerId, processId, Long.valueOf(taskId), input);
        logger.info("Result {} ", result);
    }

    public void setTaskState(final KiaApiTaskState state) throws Throwable {
        try {
            Map<String, String> opt = new HashMap<>();
            opt.put("user", state.getUser());
            KieFormManager.TASK_STATES enumState = KieFormManager.TASK_STATES.COMPLETED;
            if (StringUtils.isNotBlank(state.getState())) {
                enumState = KieFormManager.TASK_STATES.valueOf(state.getState().toUpperCase());
            }
            Map<String, Object> input = this.getInputForTaskState(state);
            this.getKieFormManager().setTaskState(state.getContainerId(), state.getTaskId(), enumState, input, opt);
            List<KieTask> tasks = this.getKieFormManager().getHumanTaskList(null, opt);
            if (!tasks.isEmpty()) {
                for (KieTask task : tasks) {
                    this.getKieFormManager().setTaskState(state.getContainerId(),
                            String.valueOf(task.getId()), KieFormManager.TASK_STATES.STARTED, input, opt);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to set task state ",e);
        }
    }

    public void putTaskDoc(final KiaApiTaskDoc doc) throws Throwable {
        try {
            logger.info("putTaskDoc for task id-{} and resource {}busy", doc.getTaskId(), busy ? "" : "not ");
            while (busy) {
                logger.info("putTaskDoc for task id-{} waiting 500 mils");
                Thread.sleep(500);
            }
            busy = true;
            Map<String, String> queryStringParam = new HashMap<>();
            queryStringParam.put("user", doc.getUser());
            Map<String, Object> input = new HashMap<>();
            input.put("content", doc.getContent());
            input.put("identifier", doc.getIdentifier());
            input.put("lastModified", Long.valueOf(doc.getLastModified()));
            input.put("size", Long.valueOf(doc.getSize()));
            input.put("link", doc.getLink());
            input.put("name", doc.getName());
            this.getKieFormManager().submitHumanFormTask(doc.getContainerId(), doc.getTaskId(), KieFormManager.TASK_STATES.COMPLETED, queryStringParam, input);
        } finally {
            busy = false;
        }
    }

    @Override
    public IBpmWidgetInfoManager getBpmWidgetInfoManager() {
        return bpmWidgetInfoManager;
    }

    @Override
    public void setBpmWidgetInfoManager(IBpmWidgetInfoManager bpmWidgetInfoManager) {
        this.bpmWidgetInfoManager = bpmWidgetInfoManager;
    }

    private void startTasks(List<KieTask> list, HashMap<String, String> opt) {
        for (KieTask cur : list) {
            if (!cur.getStatus().equalsIgnoreCase("inprogress")) {
                try {
                    this.getKieFormManager().setTaskState(cur.getContainerId(), String.valueOf(cur.getId()), KieFormManager.TASK_STATES.STARTED, null, opt);
                } catch (Throwable throwable) {
                    logger.error("failed to start tasks ", throwable);
                }
            }
        }
    }

    private Map<String, Object> getInputForTaskState(KiaApiTaskState state) {
        Properties properties = new Properties();
        properties.put("containerId", state.getContainerId());
        properties.put("taskId", state.getTaskId());
        properties.put("user", state.getUser());
        KieApiProcessStart taskInOut = this.getTaskInputOutput(properties);
        Map<String, Object> input = new HashMap<>();
        input.put("accountManager", taskInOut.getAccountManager());
        input.put("bic", state.getBic());
        input.put("name", state.getName());
        input.put("country", taskInOut.getCountry());
        input.put("street", state.getStreet());
        input.put("state", state.getUsstate());
        input.put("zipcode", state.getZipcode());
        input.put("dateOfBirth", Long.valueOf(taskInOut.getPdateOfBirth()));
        input.put("email", taskInOut.getPemail());
        input.put("party_name", taskInOut.getPname());
        input.put("surname", taskInOut.getPsurname());
        input.put("address_country", state.getCountry());
        input.put("type", taskInOut.getType());
        input.put("relationship", taskInOut.getPrelationship());
        input.put("ssn", "987654321");

        return input;
    }

    public void mergeTaskData(JSONObject taskData, KieProcessFormQueryResult form) {

        logger.info("Task data "+taskData.toString());

        String holderType = form.getHolders().get(0).getValue();
        String formName = form.getHolders().get(0).getId();

        Set<String> children = taskData.keySet();

        JSONObject formChild = null;
        for(String child : children) {
            if(child.equals(holderType)){
                formChild = taskData.getJSONObject(child);
                break;
            }
        }

        if(formChild != null) {
            mergeForm(formChild, form);
        }else {
            for(String child : children) {
                if(taskData.get(child) instanceof JSONObject) {
                    mergeTaskData(taskData.getJSONObject(child), form);
                }
            }
        }
    }

    private void mergeForm(JSONObject taskInputData, KieProcessFormQueryResult form) {

        List<KieProcessFormField> fields = form.getFields();
        Map<String, KieProcessFormField> fieldMap  = new HashMap<>();
        for(KieProcessFormField field : fields) {
            if(field.getName().contains("_")){
                fieldMap.put(field.getName().split("_")[1], field);
            }else{
                fieldMap.put(field.getName(), field);
            }
        }

        Set<String> children = taskInputData.keySet();
        for(String child : children) {

            if(fieldMap.containsKey(child)) {
                fieldMap.get(child).addProperty("inputBinding", taskInputData.get(child));
            }
        }

        List<KieProcessFormQueryResult> nested = form.getNestedForms();

        if(nested!=null) {
            for (KieProcessFormQueryResult subForm : nested) {
                mergeTaskData(taskInputData, subForm);
            }
        }
    }
}
