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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FormToBpmHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.BaseAction;

public class BpmFormAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(BpmFormAction.class);
	private static final String PROP_NAME_PROCESS_ID = "processId";
	private static final String PROP_NAME_CONTAINER_ID = "containerId";

	public String viewForm() {
		try {
			this.applyConfigParams();
			KieProcessFormQueryResult form = this._formManager.getProcessForm(this.getContainerId(), this.getProcessId());
			this.setForm(form);
			this.setLabels(form);
		} catch (Throwable t) {
			_logger.error("error in view form", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Validate input
	 *
	 * @return
	 */
	private Map<String, Object> validateForm() {
		Map<String, Object> toBpm = new HashMap<>();
		Map<String, String> params = this.getFormParams();

		try {
			for (Map.Entry<String, String> ff : params.entrySet()) {
				String key = ff.getKey();
				String value = ff.getValue();

				Object obj = FormToBpmHelper.validateField(this.getForm(), key, value);
				if (null != obj) {
					toBpm.put(key, obj);
					//                    System.out.println("ADDING " +
					//                            key + ":" +
					//                            obj + " of class " +
					//                            obj.getClass().getCanonicalName());
				} else {
					//                    System.out.println("could not validate " +
					//                            key +":" + value);
					this.addFieldError(key, this.getText("jpkiebpm.input.invalid", new String[]{value}));
				}

			}
		} catch (Throwable t) {
			_logger.error("error validating form!", t);
		}
		return toBpm;
	}

	public String postForm() {
		try {
			// reload the form
			KieProcessFormQueryResult form = this._formManager.getProcessForm(this.getContainerId(), this.getProcessId());
			this.setForm(form);
			// validate
			Map<String, Object> toBpm = validateForm();

			if (!this.getFieldErrors().isEmpty()) {
				return INPUT;
			}
			// submit form
			String result = this.getFormManager().startProcessSubmittingForm(this.getForm(), this.getContainerId(), this.getProcessId(), toBpm);


		} catch (Throwable t) {
			_logger.error("error in post form", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String getFieldProperty(KieProcessFormField field, String property) {
		if (null != field) {
			for (KieProcessProperty prop : field.getProperties()) {
				if (prop.getName().equals(property)) {
					return prop.getValue();
				}
			}
		}
		return null;
	}

	//XXX ovviamente solo per la demo
	private void setLabels(KieProcessFormQueryResult form) throws ApsSystemException {
		if (null != form && null != form.getFields()) {
			for (KieProcessFormField field : form.getFields()) {
				String bpmLabel = this.getFieldProperty(field, "label");
				if (StringUtils.isNotBlank(bpmLabel)) {
					String fieldName = field.getName();
					if (null == this.getI18nManager().getLabel(fieldName, this.getCurrentLang().getCode())) {
						this.saveEntandoLabel(bpmLabel, fieldName);
					}
				}
			}
		}
	}

	private void saveEntandoLabel(String bpmLabel, String fieldName) throws ApsSystemException {
		ApsProperties apsLabels = new ApsProperties();
		Iterator<Lang> it = this.getLangManager().getLangs().iterator();
		while (it.hasNext()) {
			apsLabels.put(it.next().getCode(), bpmLabel);
		}
		this.getI18nManager().addLabelGroup(fieldName, apsLabels);
	}

	protected void applyConfigParams() {
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		Widget currentWidget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
		if (null != currentWidget && null != currentWidget.getConfig()) {
			String processId = currentWidget.getConfig().getProperty(PROP_NAME_PROCESS_ID);
			String containerId = currentWidget.getConfig().getProperty(PROP_NAME_CONTAINER_ID);
			if (StringUtils.isNotBlank(processId) && StringUtils.isNotBlank(containerId)) {
				this.setProcessId(processId);
				this.setContainerId(containerId);
			} else {
				//TODO add better log
				_logger.warn("Found an empty widget configuration");
			}
		} else {
			//TODO add better log
			_logger.warn("No configuration found for widget");
		}
	}

	protected IKieFormManager getFormManager() {
		return _formManager;
	}

	public void setFormManager(IKieFormManager formManager) {
		this._formManager = formManager;
	}

	public String getContainerId() {
		return _containerId;
	}

	public void setContainerId(String containerId) {
		this._containerId = containerId;
	}

	public String getProcessId() {
		return _processId;
	}

	public void setProcessId(String processId) {
		this._processId = processId;
	}

	public KieProcessFormQueryResult getForm() {
		return form;
	}

	public void setForm(KieProcessFormQueryResult form) {
		this.form = form;
	}

	public Map<String, String> getFormParams() {
		return formParams;
	}

	public void setFormParams(Map<String, String> formParams) {
		this.formParams = formParams;
	}

	public II18nManager getI18nManager() {
		return i18nManager;
	}

	public void setI18nManager(II18nManager i18nManager) {
		this.i18nManager = i18nManager;
	}

	private IKieFormManager _formManager;
	private II18nManager i18nManager;

	private String _containerId;
	private String _processId;

	private KieProcessFormQueryResult form;

	private Map<String, String> formParams = new HashMap<>();
}
