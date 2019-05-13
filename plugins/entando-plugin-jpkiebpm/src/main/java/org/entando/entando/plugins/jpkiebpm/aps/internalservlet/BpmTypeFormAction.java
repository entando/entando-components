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

import com.agiletec.aps.system.*;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityAction;
import org.apache.struts2.ServletActionContext;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobjectdispenser.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FormToBpmHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.*;
import org.slf4j.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class BpmTypeFormAction extends AbstractApsEntityAction {

    private static final Logger _logger = LoggerFactory.getLogger(BpmTypeFormAction.class);

    private String typeCode;
    private Lang _currentLang;
    private DataObject dataObject;

    private IKieFormManager formManager;
    private IKieFormOverrideManager formOverrideManager;
    private II18nManager i18nManager;
    private IDataObjectManager dataObjectManager;
    private IDataObjectDispenser dataObjectDispenser;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;

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
            String typeCode = this.getTypeCode();
            DataObject dataObject = null;
            if (typeCode != null && typeCode.length() > 0) {
                dataObject = this.getDataObjectManager().createDataObject(typeCode);
            }
            if (dataObject == null) {
                return "voidTypeCode";
            }
            this.setDataObjectOnSession(dataObject);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "createNew");
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    public String edit() {
        // Operation Not Allowed
        return null;
    }

    public String entryDataObject() {
        DataObject dataObject = this.getDataObject();
        if (dataObject == null) {
            return "expiredMessage";
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

            KieBpmConfig config = formManager.getKieServerConfigurations().get(kieSourceId);
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
                _logger.debug("SAVE attribute.getName() {}", attribute.getName());
                if (null != attribute.getType()) {
                    final String value = attribute.getName();
                    _logger.debug("SAVE attribute.getType() {}:{}", attribute.getType(), value);

                    toBpm.put(attribute.getName(), this.getRequest().getParameter(attribute.getType() + ":" + value));
                } else {
                    _logger.debug("SAVE attribute.getType() NULL");
                }
            }
            this.validateForm(toBpm, kieForm);
            if (this.hasFieldErrors()) {
                return INPUT;
            }
            //message.setLangCode(this.getCurrentLang().getCode());
            //this.getDataObjectManager().insertOnLineDataObject(dataObject);
            String procId = this.getFormManager().startNewProcess(config, containerId, processId, toBpm);
            _logger.info("NEW PROCCESS ID: {}", procId);
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

    protected void validateForm(Map<String, Object> params, KieProcessFormQueryResult kieForm) throws Throwable {
        for (Map.Entry<String, Object> ff : params.entrySet()) {
            String key = ff.getKey();
            String value = null;
            if (ff.getValue() != null) {
                value = ff.getValue().toString();
            }

            _logger.debug("******* field '{}' value {}", key, value);
            Object obj = FormToBpmHelper.validateField(kieForm, key, value);
            if (null != obj) {
                if (obj instanceof NullFormField) {
                    _logger.debug("the field '{}' is null (NullFormField), but is not mandatory: ignoring", key);
                } else {
                    _logger.debug("the field '{}' is  NOT null, but is not NullFormField", key);
                    if (value.isEmpty()) {
                        _logger.debug("Invalid input '{}' on field '{}'", value, key);
                        String msg = String.format("Invalid input '%s' on field '%s'", value, key);
                        this.addFieldError(key, msg);
                    }
                }
            } else {
                _logger.debug("Invalid input '{}' on field '{}'", value, key);
                String msg = String.format("Invalid input '%s' on field '%s'", value, key);
                this.addFieldError(key, msg);

                //validationResult.add(new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, msg, Response.Status.CONFLICT));
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
                _logger.error("getDataObject", t);
                throw new RuntimeException("Error finding DataObject", t);
            }
        }
        return dataObject;
    }

    public String getRenderedForm() {
        String output = null;
        try {
            DataObject dataObject = this.getDataObject();
            if (dataObject == null) {
                return "Data object null";
            }
            RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
            Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
            String modelId = this.extractWidgetConfig(KieBpmSystemConstants.WIDGET_PARAM_DATA_UX_ID);
            DataObjectRenderizationInfo renderInfo = this.getDataObjectDispenser().getBaseRenderizationInfo(dataObject, Long.parseLong(modelId), currentLang.getCode(), reqCtx);
            output = renderInfo.getRenderedDataobject();
        } catch (Throwable t) {
            _logger.error("getDataObject", t);
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

}
