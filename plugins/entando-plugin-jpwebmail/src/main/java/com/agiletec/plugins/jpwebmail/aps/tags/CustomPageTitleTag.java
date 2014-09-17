/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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