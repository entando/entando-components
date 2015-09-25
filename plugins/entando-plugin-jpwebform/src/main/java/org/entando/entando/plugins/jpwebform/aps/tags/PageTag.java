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
package org.entando.entando.plugins.jpwebform.aps.tags;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * @author S.Loru
 */
public class PageTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(PageTag.class);

	public PageTag() {
		super();
		this.release();
	}

	@Override
	public void release() {
		this.setFormId(null);
		this.setVar(null);
	}

	@Override
	public int doStartTag() throws JspException {
		//ServletRequest request =  this.pageContext.getRequest();
		try {
			IFormManager formManager = (IFormManager) ApsWebApplicationUtils.getBean(JpwebformSystemConstants.FORM_MANAGER, this.pageContext);
			IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.pageContext);
			Message message = formManager.getMessage(this.getFormId());
			String typeCode = null;
			if(message != null){
				typeCode = message.getTypeCode();
			}
			//Object formInfo = null;
			List<IPage> showletUtilizers = pageManager.getWidgetUtilizers(JpwebformSystemConstants.WIDGET_NAME_FORM);
			for (int i = 0; i < showletUtilizers.size(); i++) {
				IPage page = showletUtilizers.get(i);
				Widget[] widgets = page.getWidgets();
				for (int j = 0; j < widgets.length; j++) {
					Widget widget = widgets[j];
					if (null != widget && null != widget.getType() && JpwebformSystemConstants.WIDGET_NAME_FORM.equals(widget.getType().getCode())) {
						ApsProperties config = widget.getConfig();
						if(null != typeCode && typeCode.equals(config.getProperty(JpwebformSystemConstants.WIDGET_PARAM_TYPECODE))){
							returnValue(page.getCode());
						}
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error detected while initialising the tag", t);
		}
		return EVAL_PAGE;
	}
	
	private void returnValue(Object formInfo) throws IOException {
		if (null != this.getVar()) {
			this.pageContext.setAttribute(this.getVar(), formInfo);
		} else {
			this.pageContext.getOut().print(formInfo);
		}
	}
	
	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}

	public String getFormId() {
		return _formId;
	}
	public void setFormId(String formId) {
		this._formId = formId;
	}
	
	private String _var;
	private String _formId;
	
}
