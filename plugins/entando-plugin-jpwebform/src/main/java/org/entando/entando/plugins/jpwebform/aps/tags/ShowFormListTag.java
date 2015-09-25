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

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 *
 * @author S.Loru
 */
public class ShowFormListTag extends TagSupport {
	
	private static final Logger _logger =  LoggerFactory.getLogger(ShowFormListTag.class);

	public ShowFormListTag(){
		super();
		this.release();
	}
	
	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			this.extractExtraShowletParameters(reqCtx);
			List<String> formMessages = this.getFormMessageId(reqCtx);
			this.pageContext.setAttribute(this.getListName(), formMessages);
		} catch (Throwable t) {
			_logger.error("error in doEndTag", t);
			throw new JspException("Error detected while finalising the tag", t);
		}
		this.release();
		return EVAL_PAGE;
	}
	
	private void extractExtraShowletParameters(RequestContext reqCtx) {
		try {
			Widget showlet = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
			config = showlet.getConfig();
			if (null != config) {
				Lang currentLang = (Lang) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_LANG));
				this.addMultilanguageShowletParameter(config, JpwebformSystemConstants.WIDGET_PARAM_TITLE, currentLang, this.getTitleVar());
				this.addMultilanguageShowletParameter(config, JpwebformSystemConstants.WIDGET_PARAM_PAGE_LINK_DESCR, currentLang, this.getPageLinkDescriptionVar());
				if (null != this.getPageLinkVar()) {
					String pageLink = config.getProperty(JpwebformSystemConstants.WIDGET_PARAM_PAGE_LINK);
					if (null != pageLink) {
						this.pageContext.setAttribute(this.getPageLinkVar(), pageLink);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting extra parameters", t);
		}
	}
	
	
	protected List<String> getFormMessageId(RequestContext reqCtx) throws Throwable {
		List<String> formMessageId = null;
		try {
			HttpSession session = this.pageContext.getSession();
			UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			IFormManager formManager = (IFormManager) ApsWebApplicationUtils.getBean(JpwebformSystemConstants.FORM_MANAGER, this.pageContext);
			EntitySearchFilter filterUser = new EntitySearchFilter(IFormManager.FILTER_KEY_USERNAME, false, currentUser.getUsername(), false);
			EntitySearchFilter filterSendDate = new EntitySearchFilter(IFormManager.FILTER_KEY_SEND_DATE, false);
			filterSendDate.setOrder(EntitySearchFilter.ASC_ORDER);
			EntitySearchFilter filterCreationDate = new EntitySearchFilter(IFormManager.FILTER_KEY_CREATION_DATE, false);
			String status = null;
			if(config != null){
				status = config.getProperty(JpwebformSystemConstants.WIDGET_PARAM_STATUS);
			}
			EntitySearchFilter[] entitySearch = null;
			if(null == status || "".equals(status)){
				entitySearch = new EntitySearchFilter[]{filterSendDate, filterCreationDate, filterUser};
			} else {
				EntitySearchFilter statusFilter = null;
				if("true".equals(status)){
					statusFilter = new EntitySearchFilter(IFormManager.FILTER_KEY_DONE, false, new Integer(1) , false);
				} else if("false".equals(status)) {
					statusFilter = new EntitySearchFilter(IFormManager.FILTER_KEY_DONE, false, new Integer(0) , false);
				}
				entitySearch = new EntitySearchFilter[]{filterSendDate, filterCreationDate, filterUser, statusFilter};
			}
			formMessageId = formManager.loadMessagesId(entitySearch);
		} catch (Throwable t) {
			_logger.error("Error extracting contents id", t);
			throw new ApsSystemException("Error extracting contents id", t);
		}
		return formMessageId;
	}
	
	protected void addMultilanguageShowletParameter(ApsProperties config, String showletParamPrefix, Lang currentLang, String var) {
		if (null == var) return;
		String paramValue = config.getProperty(showletParamPrefix + "_" + currentLang.getCode());
		if (null == paramValue) {
			ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
			Lang defaultLang = langManager.getDefaultLang();
			paramValue = config.getProperty(showletParamPrefix + "_" + defaultLang.getCode());
		}
		if (null != paramValue) {
			this.pageContext.setAttribute(var, paramValue);
		}
	}
	
	@Override
	public void release() {
		this._listName = null;
		this.setTitleVar(null);
		this.setPageLinkVar(null);
		this.setPageLinkDescriptionVar(null);
	}
	
	

	public String getTitleVar() {
		return _titleVar;
	}

	public void setTitleVar(String titleVar) {
		this._titleVar = titleVar;
	}

	public String getPageLinkVar() {
		return _pageLinkVar;
	}

	public void setPageLinkVar(String pageLinkVar) {
		this._pageLinkVar = pageLinkVar;
	}

	public String getPageLinkDescriptionVar() {
		return _pageLinkDescriptionVar;
	}

	public void setPageLinkDescriptionVar(String pageLinkDescriptionVar) {
		this._pageLinkDescriptionVar = pageLinkDescriptionVar;
	}

	public String getListName() {
		return _listName;
	}

	public void setListName(String listaName) {
		this._listName = listaName;
	}
	
	private String _listName;
	private String _titleVar;
	private String _pageLinkVar;
	private String _pageLinkDescriptionVar;
	private ApsProperties config;
	
	
}
