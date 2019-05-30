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
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BpmFormStartNewProcessAction extends BpmFormActionBase{
    private static final Logger logger = LoggerFactory.getLogger(BpmFormStartNewProcessAction.class);

    private static String FORM_ACTION = "/ExtStr2/do/bpm/FrontEnd/StartNewProcessForm/save";

    @Override
    public String createNew() {
        try {
            logger.debug("createNew processId {}", getProcessId());
            kpfr = null;
            setDataObjectOnSession(null);
            setWidgetInitParameters();
            kpfr = this.getFormManager().getProcessForm(getConfig(), getContainerId(), getProcessId());
            setValuesMap(new HashMap<>());

/*
            if (null != processId) {
                JSONObject taskData = null;
                taskData = this.getFormManager().getProcessForm(getConfig(), getContainerId(), processId, null);

                logger.debug("taskData {}", taskData);
                JSONObject inputData = taskData.getJSONObject("task-input-data");
                logger.debug("inputData {}", inputData);
                setValuesMap(this.getValuesMap(inputData, null, null, kpfr));
            }
*/
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
            String widgetInfoId = this.extractWidgetConfig(KieBpmSystemConstants.WIDGET_PARAM_INFO_ID);
            BpmWidgetInfo widgetInfo = this.getBpmWidgetInfoManager().getBpmWidgetInfo(Integer.valueOf(widgetInfoId));
            ApsProperties configOnline = widgetInfo.getConfigOnline();
            String processId = configOnline.getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_PROCESS_ID);
            String containerId = configOnline.getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_CONTAINER_ID);
            String kieSourceId = configOnline.getProperty(KieBpmSystemConstants.WIDGET_INFO_PROP_KIE_SOURCE_ID);

            KieBpmConfig config = getFormManager().getKieServerConfigurations().get(kieSourceId);
            KieProcessFormQueryResult kieForm = this.getFormManager().getProcessForm(config, containerId, processId);

            String username = this.getCurrentUser().getUsername();
            dataObject.setDescription("New Bpm process instance - " + dataObject.getTypeCode());
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
                    logger.debug("SAVE attribute.getType() {}:{}", attribute.getType(), value);

                    toBpm.put(attribute.getName(), this.getRequest().getParameter(attribute.getType() + ":" + value));
                } else {
                    logger.debug("SAVE attribute.getType() NULL");
                }
            }
            this.validateForm(toBpm, kieForm);
            if (this.hasFieldErrors()) {
                return INPUT;
            }
            String procId = this.getFormManager().startNewProcess(config, containerId, processId, toBpm);
            logger.info("NEW PROCESS ID: {}", procId);
            List<String> args = new ArrayList<>();
            args.add(procId);
            args.add(processId.substring(processId.lastIndexOf(".") + 1));
            this.setDataObjectOnSession(null);
            this.addActionMessage(this.getText("message.success",args));
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "save");
            return FAILURE;
        }
        return SUCCESS;
    }

    private void generateForm() throws Exception {
        String modelDescription  = "Model for Start process "+ getProcessId() + " of container "+getContainerId() ;
        String typeDescription = getProcessId() + "_" + getContainerId();
        String urlParameters = "";
        super.generateForm(modelDescription, typeDescription, urlParameters);
    }

}