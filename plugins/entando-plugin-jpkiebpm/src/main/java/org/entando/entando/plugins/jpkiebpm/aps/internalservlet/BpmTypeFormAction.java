/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityAction;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobjectdispenser.DataObjectRenderizationInfo;
import org.entando.entando.aps.system.services.dataobjectdispenser.IDataObjectDispenser;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FormToBpmHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.NullFormField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation for action managing DataObject entity editing operations.
 *
 * @author E.Santoboni
 */
public class BpmTypeFormAction extends AbstractApsEntityAction {

	private static final Logger _logger = LoggerFactory.getLogger(BpmTypeFormAction.class);

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
			KieProcessFormQueryResult kieForm = this.getFormManager().getProcessForm(containerId, processId);

			String username = this.getCurrentUser().getUsername();
			dataObject.setDescription("New Bpm process instance - " + dataObject.getTypeCode());
			dataObject.setDefaultLang(this.getCurrentLang().getCode());
			dataObject.setMainGroup(Group.FREE_GROUP_NAME);
			dataObject.setLastEditor(username);
			dataObject.setCreated(new Date());

			Map<String, Object> toBpm = new HashMap<String, Object>();
			EntityAttributeIterator iterator = new EntityAttributeIterator(dataObject);
			Enumeration<String> e = this.getRequest().getParameterNames();

			while (iterator.hasNext()) {
				AttributeInterface attribute = (AttributeInterface) iterator.next();
				if (null != attribute.getType()) {
				final String value = attribute.getName();

					toBpm.put(attribute.getName(), this.getRequest().getParameter(attribute.getType()+":"+value));
				}
			}
			this.validateForm(toBpm, kieForm);
			if (this.hasFieldErrors()) {
				return INPUT;
			}
			//message.setLangCode(this.getCurrentLang().getCode());
			//this.getDataObjectManager().insertOnLineDataObject(dataObject);
			this.getFormManager().startProcessSubmittingForm(kieForm, containerId, processId, toBpm);
			this.setDataObjectOnSession(null);
            this.addActionMessage(this.getText("message.success"));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}

	protected void validateForm(Map<String, Object> params, KieProcessFormQueryResult kieForm) throws Throwable {
		for (Map.Entry<String, Object> ff : params.entrySet()) {
			String key = ff.getKey();
			String value = ff.getValue().toString();
			Object obj = FormToBpmHelper.validateField(kieForm, key, value);
			if (null != obj) {
				if (obj instanceof NullFormField) {
					_logger.info("the field '{}' is null, but is not mandatory: ignoring", key);
				}
			} else {
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
		if (this._dataObject == null) {
			try {
				String sessionParamName = this.getSessionParamName();
				HttpServletRequest request = (null != this.getRequest()) ? this.getRequest() : ServletActionContext.getRequest();
				this._dataObject = (DataObject) request.getSession().getAttribute(sessionParamName);
			} catch (Throwable t) {
				_logger.error("getDataObject", t);
				throw new RuntimeException("Error finding DataObject", t);
			}
		}
		return _dataObject;
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
		this._dataObject = dataObject;
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
		if (null == this._typeCode) {
			this._typeCode = this.extractTypeCode();
		}
		return _typeCode;
	}

	/**
	 * Sets the DataObject type search filter.
	 *
	 * @param typeCode The message type search filter.
	 */
	public void setTypeCode(String typeCode) {
		String extractedTypeCode = this.extractTypeCode();
		this._typeCode = (null == extractedTypeCode) ? typeCode : extractedTypeCode;
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
		return _formManager;
	}

	public void setFormManager(IKieFormManager formManager) {
		this._formManager = formManager;
	}

	protected II18nManager getI18nManager() {
		return _i18nManager;
	}

	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
	}

	protected IDataObjectManager getDataObjectManager() {
		return _dataObjectManager;
	}

	public void setDataObjectManager(IDataObjectManager dataObjectManager) {
		this._dataObjectManager = dataObjectManager;
	}

	public IDataObjectDispenser getDataObjectDispenser() {
		return _dataObjectDispenser;
	}

	public void setDataObjectDispenser(IDataObjectDispenser dataObjectDispenser) {
		this._dataObjectDispenser = dataObjectDispenser;
	}

	public IBpmWidgetInfoManager getBpmWidgetInfoManager() {
		return bpmWidgetInfoManager;
	}

	public void setBpmWidgetInfoManager(IBpmWidgetInfoManager bpmWidgetInfoManager) {
		this.bpmWidgetInfoManager = bpmWidgetInfoManager;
	}
	private String _typeCode;
	private Lang _currentLang;
	private DataObject _dataObject;

	private IKieFormManager _formManager;
	private II18nManager _i18nManager;
	private IDataObjectManager _dataObjectManager;
	private IDataObjectDispenser _dataObjectDispenser;
	private IBpmWidgetInfoManager bpmWidgetInfoManager;

}
