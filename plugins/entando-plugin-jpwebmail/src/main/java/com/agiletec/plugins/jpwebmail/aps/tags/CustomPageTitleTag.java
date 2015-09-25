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
package com.agiletec.plugins.jpwebmail.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Create and insert a custom page title
 * @author E.Santoboni
 */
public class CustomPageTitleTag extends TagSupport {
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			if (null != this.getTitle() && this.getTitle().trim().length() > 0) {
				reqCtx.addExtraParam(SystemConstants.EXTRAPAR_EXTRA_PAGE_TITLES, this.getTitle());
			} else if (null != this.getLabelKey() && this.getLabelKey().trim().length() > 0) {
				II18nManager i18nManager = (II18nManager) ApsWebApplicationUtils.getBean(SystemConstants.I18N_MANAGER, this.pageContext);
				ApsProperties titles = i18nManager.getLabelGroups().get(this.getLabelKey());
				if (null != titles) {
					reqCtx.addExtraParam(SystemConstants.EXTRAPAR_EXTRA_PAGE_TITLES, titles);
				} else {
					reqCtx.addExtraParam(SystemConstants.EXTRAPAR_EXTRA_PAGE_TITLES, this.getLabelKey());
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error during tag initialization", t);
		}
		return super.doStartTag();
	}
	
	@Override
	public void release() {
		this.setLabelKey(null);
		this.setTitle(null);
	}
	
	public String getLabelKey() {
		return _labelKey;
	}
	public void setLabelKey(String labelKey) {
		this._labelKey = labelKey;
	}
	
	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
	}
	
	private String _labelKey;
	private String _title;
	
}