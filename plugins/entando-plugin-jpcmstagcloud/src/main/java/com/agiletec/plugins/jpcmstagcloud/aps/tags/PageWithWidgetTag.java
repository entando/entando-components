/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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