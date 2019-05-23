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
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.i18n.I18nManagerWrapper;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityAction;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class BpmFormCompleteTaskAction extends BpmFormActionBase {

    private static String FORM_ACTION = "/ExtStr2/do/bpm/FrontEnd/DataTypeTaskForm/save";

    private static final Logger logger = LoggerFactory.getLogger(BpmFormCompleteTaskAction.class);


    private Long taskId;

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

    @Override
    public String view() {
        //Operation not allowed
        return null;
    }

    @Override
    public String createNew() {
        try {
            logger.debug("createNew taskID {}", taskId);
            kpfr = null;
            setDataObjectOnSession(null);
            setWidgetInitParameters();
            kpfr = this.getFormManager().getTaskForm(getConfig(), getContainerId(), taskId);

            if (null != taskId) {
                JSONObject taskData = null;
                    taskData = this.getFormManager().getTaskFormData(getConfig(), getContainerId(), taskId, null);

                logger.debug("taskData {}", taskData);
                JSONObject inputData = taskData.getJSONObject("task-input-data");
                logger.debug("inputData {}", inputData);
                setValuesMap(this.getValuesMap(inputData, null, null, kpfr));
            }
            setFormAction(FORM_ACTION);
            generateForm();
    } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "createNew");
            return FAILURE;
        }
        return SUCCESS;
    }


    @Override
    public String save() {
        try {
            DataObject dataObject = this.getDataObject();
            if (dataObject == null) {
                return "expiredMessage";
            }

            setWidgetInitParameters();
            //KieProcessFormQueryResult kieForm = this.getFormManager().getProcessForm(config, containerId, processId);
            logger.debug("formManager() {}", getFormManager());
            logger.debug("containerId {}", getContainerId());
            logger.debug("config {}", getConfig());
            logger.debug("taskId() {}", taskId);
            //kpfr = getFormManager().getTaskForm(getConfig(), getContainerId(), taskId);

            String username = this.getCurrentUser().getUsername();
            dataObject.setDescription("New Bpm task instance - " + dataObject.getTypeCode());
            dataObject.setDefaultLang(this.getCurrentLang().getCode());
            dataObject.setMainGroup(Group.FREE_GROUP_NAME);
            dataObject.setLastEditor(username);
            dataObject.setCreated(new Date());

            Map<String, Object> toBpm = new HashMap<String, Object>();
            EntityAttributeIterator iterator = new EntityAttributeIterator(dataObject);

            while (iterator.hasNext()) {
                AttributeInterface attribute = (AttributeInterface) iterator.next();
                logger.debug("SAVE attribute.getName() {}", attribute.getName());
                if (null != attribute.getType()) {
                    final String value = attribute.getName();
                    String originalNameField = attribute.getName().replaceAll(REPLACE_SPACE_STRING, " ");
                    logger.debug("SAVE attribute.getType() {}:{}", attribute.getType(), value);
                    toBpm.put(originalNameField, this.getRequest().getParameter(attribute.getType() + ":" + value));
                } else {
                    logger.debug("SAVE attribute.getType() NULL");
                }
            }
            this.validateForm(toBpm, kpfr);
            if (this.hasFieldErrors()) {
                return INPUT;
            }

            Map<String, String> toBpmStrings = toBpm.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Optional.ofNullable((String) e.getValue()).orElse("")));

            final String result = this.getFormManager().completeHumanFormTask(getConfig(), getContainerId(), getProcessId(), Long.valueOf(taskId), toBpmStrings);
            logger.debug("Result {} ", result);
            this.setDataObjectOnSession(null);
            this.addActionMessage(this.getText("message.success"));
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "save");
            return FAILURE;
        }
        return SUCCESS;
    }


    @Override
    protected void generateForm() throws Exception {
        setTaskId(taskId);
        super.generateForm();
    }


    @Override
    protected Map<String, Object> getValuesMap(JSONObject inputData, String parentKey, String oldParentKey, final KieProcessFormQueryResult form) {
        logger.debug("getValuesMap parentKey {} oldParentKey {}", parentKey, oldParentKey);
        Map<String, Object> map = new HashMap<>();
        try {
            inputData.keySet().forEach(f -> {
                String fieldNameMapped;
                KieDataHolder dataHolder = null;
                if (inputData.get(f) instanceof JSONObject) {
                    for (KieDataHolder fh : form.getHolders()) {
                        if (null != fh.getValue()) {
                            logger.debug("dataHolder value  {} ", fh.getValue());
                            if (fh.getValue().equals(f)) {
                                dataHolder = fh;
                                logger.debug("      dataHolder found {} ", f);
                                logger.debug("      dataHolder.getId() {} ", dataHolder.getId());
                                logger.debug("      dataHolder.getName() {} ", dataHolder.getName());
                                logger.debug("      dataHolder.getValue() {} ", dataHolder.getValue());
                                logger.debug("      dataHolder.getType() {} ", dataHolder.getType());
                                logger.debug("      dataHolder.getOutId() {} ", dataHolder.getOutId());
                                break;
                            }
                        }
                    }

                    if (null != dataHolder) {
                        String[] splitHolderName = dataHolder.getValue().split("\\.");
                        String holderName;
                        holderName = dataHolder.getName();
                        if (dataHolder.getType().equals("BASE")) {
                            fieldNameMapped = searchFieldInTheForm(holderName + f + "_" + f, form);
                            if (splitHolderName.length > 0) {
                                if (null != fieldNameMapped) {
                                    map.put(fieldNameMapped, String.valueOf(inputData.get(f)));
                                } else {
                                    map.put(holderName + f, String.valueOf(inputData.get(f)));
                                }
                            } else {
                                map.put(dataHolder.getName() + f, String.valueOf(inputData.get(f)));
                            }

                        } else {
                            map.putAll(getValuesMap((JSONObject) inputData.get(f), holderName, parentKey, form));
                        }
                    } else {
                        JSONObject jsonObject = (JSONObject) inputData.get(f);


                        if (jsonObject.has("java.util.Date")) {
                            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
                            String strDate = dateFormat.format(jsonObject.get("java.util.Date"));
                            logger.debug("java.util.Date found in jsonObject - Converted String is : {}", strDate);
                            map.put(oldParentKey + "_" + f, strDate);
                        } else {
                            map.putAll(getValuesMap((JSONObject) inputData.get(f), f, parentKey, form));
                        }
                    }
                } else {
                    if (null != parentKey) {
                        String[] splitParentKey = parentKey.split("\\.");
                        String parentKeyName = "";
                        parentKeyName = splitParentKey[splitParentKey.length - 1];

                        if (!(parentKeyName.equalsIgnoreCase(oldParentKey))) {

                            String fieldNameMappedParent = searchFieldInTheForm(oldParentKey + "_" + f, form);

                            if (fieldNameMappedParent == null) {
                                logger.debug("fieldNameMappedParent == null ");
                                fieldNameMapped = searchFieldInTheForm(parentKeyName + "_" + f, form);
                            } else {
                                logger.debug("fieldNameMappedParent != null ");
                                fieldNameMapped = fieldNameMappedParent;
                            }
                        } else {
                            logger.debug("(parentKeyName.equalsIgnoreCase(oldParentKey))) ");
                            fieldNameMapped = searchFieldInTheForm(parentKeyName + "_" + f, form);
                        }

                        logger.debug("fieldNameMapped {}", fieldNameMapped);

                        if (null != fieldNameMapped) {
                            map.put(fieldNameMapped, String.valueOf(inputData.get(f)));
                        } else {
                            if (parentKeyName.startsWith("taskInput")) {
                                String lowerFirstChar = Character.toLowerCase(parentKeyName.charAt(9)) + parentKeyName.substring(10);
                                map.put(lowerFirstChar + "_" + f, String.valueOf(inputData.get(f)));
                            } else {
                                if (inputData.has(parentKeyName + "_" + f)) {
                                    map.put(parentKeyName + "_" + f, inputData.get(parentKeyName + "_" + f));
                                } else {
                                    map.put(parentKeyName + "_" + f, null);
                                }
                            }
                        }

                    } else {
                        map.put(f, inputData.get(f));
                    }
                }

            });
        } catch (Exception ex) {
            logger.error("GetValuesMap error : {}", ex);
        }

        Map<String, Object> newMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            newMap.put(entry.getKey().replaceAll(" ", REPLACE_SPACE_STRING), entry.getValue());
        }
        return newMap;

    }

    private String searchFieldInTheForm(String fieldName, KieProcessFormQueryResult form) {
        logger.debug("Search input for fieldName() {}", fieldName);

        String fieldNameMapped;
        for (KieProcessFormField field : form.getFields()) {
            //TODO FIX THIS using the correct form property instead of ignorecase
            if (field.getName().equalsIgnoreCase(fieldName)) {
                fieldNameMapped = field.getName();
                logger.debug("fieldNameMappedFound {}", fieldNameMapped);
                return fieldNameMapped;
            }
        }

        if (null != form.getNestedForms()) {
            for (KieProcessFormQueryResult subForm : form.getNestedForms()) {
                fieldNameMapped = this.searchFieldInTheForm(fieldName, subForm);
                if (null != fieldNameMapped) {
                    return fieldNameMapped;
                }

            }
        }
        return null;
    }
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

}
