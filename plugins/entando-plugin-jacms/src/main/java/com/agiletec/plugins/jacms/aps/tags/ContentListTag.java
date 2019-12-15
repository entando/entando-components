/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.aps.tags;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListTagBean;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListWidgetHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.UserFilterOptionBean;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Loads a list of contents IDs by applying the filters (if any).
 * @author E.Santoboni
 */
public class ContentListTag extends TagSupport implements IContentListTagBean {

	private static final Logger _logger = LoggerFactory.getLogger(ContentListTag.class);
	
	private String listName;
	private String contentType;
	private String[] categories = new String[0];
	private EntitySearchFilter[] filters = new EntitySearchFilter[0];
	
	private boolean cacheable = true;
	
	private List<UserFilterOptionBean> userFilterOptions;
	
	@Deprecated
	private boolean listEvaluated;
	
	private String titleVar;
	private String pageLinkVar;
	private String pageLinkDescriptionVar;
	private String userFilterOptionsVar;
	
	public ContentListTag() {
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
			IContentListWidgetHelper helper = (IContentListWidgetHelper) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_LIST_HELPER, this.pageContext);
			List<UserFilterOptionBean> defaultUserFilterOptions = helper.getConfiguredUserFilters(this, reqCtx);
			this.addUserFilterOptions(defaultUserFilterOptions);
			this.extractExtraWidgetParameters(reqCtx);
			if (null != this.getUserFilterOptions() && null != this.getUserFilterOptionsVar()) {
				this.pageContext.setAttribute(this.getUserFilterOptionsVar(), this.getUserFilterOptions());
			}
			List<String> contents = this.getContentsId(helper, reqCtx);
			this.pageContext.setAttribute(this.getListName(), contents);
		} catch (Throwable t) {
			_logger.error("error in end tag", t);
			throw new JspException("Error detected while finalising the tag", t);
		}
		this.release();
		return EVAL_PAGE;
	}
	
	protected List<String> getContentsId(IContentListWidgetHelper helper, RequestContext reqCtx) throws ApsSystemException {
		List<String> contents = null;
		try {
			contents = helper.getContentsId(this, reqCtx);
			Widget currentWidget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			Integer maxElements = null;
			if (null != currentWidget && null != currentWidget.getConfig()) {
				ApsProperties properties = currentWidget.getConfig();
				String maxElementsString = properties.getProperty("maxElements");
				try {
					maxElements = Integer.parseInt(maxElementsString);
				} catch (Exception e) {
					//nothing to catch
				}	
			}
			if (null != maxElements && contents != null && contents.size() > maxElements) {
				contents = contents.subList(0, maxElements);
			}
		} catch (Throwable t) {
			_logger.error("Error extracting content ids", t);
			throw new ApsSystemException("Error extracting content ids", t);
		}
		return contents;
	}
	
	protected void extractExtraWidgetParameters(RequestContext reqCtx) {
		try {
			Widget currentWidget = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
			ApsProperties config = (null != currentWidget) ? currentWidget.getConfig() : null;
			if (null != config) {
				Lang currentLang = (Lang) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_LANG));
				this.addMultilanguageWidgetParameter(config, IContentListWidgetHelper.WIDGET_PARAM_TITLE, currentLang, this.getTitleVar());
				this.addMultilanguageWidgetParameter(config, IContentListWidgetHelper.WIDGET_PARAM_PAGE_LINK_DESCR, currentLang, this.getPageLinkDescriptionVar());
				if (null != this.getPageLinkVar()) {
					String pageLink = config.getProperty(IContentListWidgetHelper.WIDGET_PARAM_PAGE_LINK);
					if (null != pageLink) {
						this.pageContext.setAttribute(this.getPageLinkVar(), pageLink);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting extra parameters", t);
		}
	}
	
	@Deprecated
	protected void addMultilanguageShowletParameter(ApsProperties config, String widgetParamPrefix, Lang currentLang, String var) {
		this.addMultilanguageWidgetParameter(config, widgetParamPrefix, currentLang, var);
	}
	
	protected void addMultilanguageWidgetParameter(ApsProperties config, String widgetParamPrefix, Lang currentLang, String var) {
		if (null == var || null == config) return;
		String paramValue = config.getProperty(widgetParamPrefix + "_" + currentLang.getCode());
		if (null == paramValue) {
			ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
			Lang defaultLang = langManager.getDefaultLang();
			paramValue = config.getProperty(widgetParamPrefix + "_" + defaultLang.getCode());
		}
		if (null != paramValue) {
			this.pageContext.setAttribute(var, paramValue);
		}
	}
	
	@Override
	public void release() {
		this.listName = null;
		this.contentType = null;
		this.categories = new String[0];
		this.filters = new EntitySearchFilter[0];
		this.listEvaluated = false;
		this.cacheable = true;
		this.setUserFilterOptions(null);
		this.setTitleVar(null);
		this.setPageLinkVar(null);
		this.setPageLinkDescriptionVar(null);
		this.setUserFilterOptionsVar(null);
	}
	
	@Override
	public void addFilter(EntitySearchFilter filter) {
        this.filters = ArrayUtils.add(this.filters, filter);
	}
	
	protected void addUserFilterOptions(List<UserFilterOptionBean> userFilterOptions) {
		if (null == userFilterOptions) return;
		for (int i = 0; i < userFilterOptions.size(); i++) {
			this.addUserFilterOption(userFilterOptions.get(i));
		}
	}
	
	@Override
	public void addUserFilterOption(UserFilterOptionBean userFilterOption) {
		if (null == userFilterOption) return;
		if (null == this.getUserFilterOptions()) {
			this.setUserFilterOptions(new ArrayList<>());
		}
		this.getUserFilterOptions().add(userFilterOption);
	}
	
	@Override
	public EntitySearchFilter[] getFilters() {
		return this.filters;
	}
	
	/**
	 * Get the name of the variable in the page context that holds the list of the IDs found.
	 * @return Returns the name of the list.
	 */
	@Override
	public String getListName() {
		return listName;
	}

	/**
	 * Set the name of the variable in the page context that holds the list of the IDs found.
	 * @param listName The listName to set.
	 */
	public void setListName(String listName) {
		this.listName = listName;
	}

	/**
	 * Get the code of the content types to search.
	 * @return The code of the content type.
	 */
	@Override
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * Set the code of the content types to search.
	 * @param contentType The code of the content type.
	 */
	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	/**
	 * Return the identifier string of the category of the Content to search.
	 * @return The category code.
	 */
	@Override
	@Deprecated
	public String getCategory() {
		return null;
	}
	
	/**
	 * Set the identifier string of the category of the Content to search.
	 * @param category The category code.
	 */
	@Override
	public void setCategory(String category) {
		this.addCategory(category);
	}
	
	@Override
	public String[] getCategories() {
		return this.categories;
	}
	
	@Override
	public void addCategory(String category) {
		if (null == category) return;
        this.categories = ArrayUtils.add(this.categories, category);
	}
    
    @Override
    public boolean isOrClauseCategoryFilter() {
        return false;
    }
    
	/**
	 * Checks if the list if the list has been previously stored in the startTag method.
	 * @return true if the list wad evalued into start tag
	 * @deprecated the startTag method isn't extended
	 */
	protected boolean isListEvaluated() {
		return listEvaluated;
	}
	
	/**
	 * Set if the list if the list has been previously stored in the startTag method.
	 * @param listEvaluated true if the list wad evalued into start tag
	 * @deprecated the startTag method isn't extended
	 */
	protected void setListEvaluated(boolean listEvaluated) {
		this.listEvaluated = listEvaluated;
	}
	
	/**
	 * Return true if the system caching must involved in the search process.
	 * @return true if the system caching must involved
	 */
	@Override
	public boolean isCacheable() {
		return cacheable;
	}
	
	/**
	 * Toggles the system caching usage when retrieving the list.
	 * Admitted values (true|false), default "true".
	 * @param cacheable
	 */
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}
	
	@Override
	public List<UserFilterOptionBean> getUserFilterOptions() {
		return userFilterOptions;
	}
	protected void setUserFilterOptions(List<UserFilterOptionBean> userFilterOptions) {
		this.userFilterOptions = userFilterOptions;
	}
	
	public String getTitleVar() {
		return titleVar;
	}
	public void setTitleVar(String titleVar) {
		this.titleVar = titleVar;
	}
	
	public String getPageLinkVar() {
		return pageLinkVar;
	}
	public void setPageLinkVar(String pageLinkVar) {
		this.pageLinkVar = pageLinkVar;
	}
	
	public String getPageLinkDescriptionVar() {
		return pageLinkDescriptionVar;
	}
	public void setPageLinkDescriptionVar(String pageLinkDescriptionVar) {
		this.pageLinkDescriptionVar = pageLinkDescriptionVar;
	}
	
	public String getUserFilterOptionsVar() {
		return userFilterOptionsVar;
	}
	public void setUserFilterOptionsVar(String userFilterOptionsVar) {
		this.userFilterOptionsVar = userFilterOptionsVar;
	}
	
}