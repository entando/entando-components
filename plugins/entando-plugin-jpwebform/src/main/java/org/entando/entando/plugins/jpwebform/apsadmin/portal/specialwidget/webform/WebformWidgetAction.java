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
package org.entando.entando.plugins.jpwebform.apsadmin.portal.specialwidget.webform;

import java.util.List;

import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.SmallMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;

/**
 * @author E.Santoboni
 */
public class WebformWidgetAction extends SimpleWidgetConfigAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(WebformWidgetAction.class);

	@Override
	public void validate() {
		super.validate();
		try {
			String typeCode = this.getTypeCode();
			if (typeCode == null || this.getMessageManager().getSmallMessageTypesMap().get(typeCode) == null) {
				this.addFieldError("typeCode", this.getText("error.typeCode.required"));
			}
		} catch (Throwable t) {
			_logger.error("error in validate", t);
		}
	}
	
	@Override
	public String init() {
		String result = super.init();
		try {
			if (result.equals(SUCCESS)) {
				String paramName = JpwebformSystemConstants.WIDGET_PARAM_TYPECODE;
				String typeCode = this.getWidget().getConfig().getProperty(paramName);
				this.setTypeCode(typeCode);
			}
		} catch (Throwable t) {
			_logger.error("error in init", t);
			return FAILURE;
		}
		return result;
	}
	
	public List<SmallMessageType> getMessageTypes() {
		try {
			return this.getMessageManager().getSmallMessageTypes();
		} catch (Throwable t) {
			_logger.error("Error searching message types", t);
			throw new RuntimeException("Error searching message types", t);
		}
	}
	
	public String getTypeCode() {
		return _typeCode;
	}
	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}
	
	protected IFormManager getMessageManager() {
		return _messageManager;
	}
	public void setMessageManager(IFormManager messageManager) {
		this._messageManager = messageManager;
	}
	
	private String _typeCode;
	
	private IFormManager _messageManager;
	
}