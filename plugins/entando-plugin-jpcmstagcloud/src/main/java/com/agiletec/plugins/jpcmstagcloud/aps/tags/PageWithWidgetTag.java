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
package com.agiletec.plugins.jpcmstagcloud.aps.tags;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Search and return the page (or the list of pages) with the given showlet type.
 * @author E.Santoboni
 */
public class PageWithWidgetTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(PageWithWidgetTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.pageContext);
		try {
			List<IPage> pages = pageManager.getWidgetUtilizers(this.getShowletTypeCode());
			if (this.isListResult()) {
				this.pageContext.setAttribute(this.getVar(), pages);
			} else if (null != pages && pages.size() > 0) {
				this.pageContext.setAttribute(this.getVar(), pages.get(0));
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("doStartTag", t);
		}
		return super.doStartTag();
	}

	@Override
	public void release() {
		super.release();
		this.setShowletTypeCode(null);
		this.setVar(null);
		this.setListResult(false);
	}

	public String getShowletTypeCode() {
		return _showletTypeCode;
	}
	public void setShowletTypeCode(String showletTypeCode) {
		this._showletTypeCode = showletTypeCode;
	}

	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}

	public boolean isListResult() {
		return _listResult;
	}
	public void setListResult(boolean listResult) {
		this._listResult = listResult;
	}

	private String _showletTypeCode;
	private String _var;
	private boolean _listResult;

}