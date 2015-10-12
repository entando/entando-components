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
package com.agiletec.plugins.jpfacetnav.apsadmin.portal.specialwidget;

import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpfacetnav.aps.system.JpFacetNavSystemConstants;
import com.agiletec.plugins.jpfacetnav.apsadmin.portal.specialwidget.util.FacetNavWidgetHelper;

/**
 * @author E.Santoboni
 */
public class FacetNavResultWidgetAction extends SimpleWidgetConfigAction {
	
	@Override
	public void validate() {
		super.validate();
		try {
			this.createValuedShowlet();
			this.validateContentTypes();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "validate");
		}
	}
	
	protected void validateContentTypes() {
		List<String> contentTypes = this.getContentTypeCodes();
		Map<String, SmallContentType> smallContentTypes = this.getContentManager().getSmallContentTypesMap();
		for (String contentTypeCode : contentTypes) {
			if (!smallContentTypes.containsKey(contentTypeCode)) {
				String[] args = { contentTypeCode };
				String fieldName = JpFacetNavSystemConstants.CONTENT_TYPES_FILTER_WIDGET_PARAM_NAME;
				this.addFieldError(fieldName, this.getText("message.facetNavWidget.contentTypesFilter.notValid", args));
			}
		}
	}
	
	@Override
	public String init() {
		String result = super.init();
		try {
			if (result.equals(SUCCESS)) {
				this.initSpecialParams();
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "init");
			return FAILURE;
		}
		return result;
	}
	
	/**
	 * Add a content type to the associated content types
	 * @return The code describing the result of the operation.
	 */
	public String joinContentType() {
		try {
			this.createValuedShowlet();
			List<String> contentTypes = this.getContentTypeCodes();
			String contentTypeCode = this.getContentTypeCode();
			if (contentTypeCode != null && contentTypeCode.length()>0 && !contentTypes.contains(contentTypeCode) && this.getContentType(contentTypeCode)!=null) {
				contentTypes.add(contentTypeCode);
				String contentTypesFilter = FacetNavWidgetHelper.concatStrings(contentTypes, ",");
				String configParamName = JpFacetNavSystemConstants.CONTENT_TYPES_FILTER_WIDGET_PARAM_NAME;
				this.getWidget().getConfig().setProperty(configParamName, contentTypesFilter);
				this.setContentTypesFilter(contentTypesFilter);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "joinContentType");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Remove a content type from the associated content types
	 * @return The code describing the result of the operation.
	 */
	public String removeContentType() {
		try {
			this.createValuedShowlet();
			List<String> contentTypes = this.getContentTypeCodes();
			String contentTypeCode = this.getContentTypeCode();
			if (contentTypeCode != null) {
				contentTypes.remove(contentTypeCode);
				String contentTypesFilter = FacetNavWidgetHelper.concatStrings(contentTypes, ",");
				String configParamName = JpFacetNavSystemConstants.CONTENT_TYPES_FILTER_WIDGET_PARAM_NAME;
				this.getWidget().getConfig().setProperty(configParamName, contentTypesFilter);
				this.setContentTypesFilter(contentTypesFilter);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeContentType");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Prepare action with the parameters contained into widget.
	 */
	protected void initSpecialParams() {
		if (null != this.getWidget().getConfig()) {
			String paramName = JpFacetNavSystemConstants.CONTENT_TYPES_FILTER_WIDGET_PARAM_NAME;
			String configParamName = this.getWidget().getConfig().getProperty(paramName);
			this.setContentTypesFilter(configParamName);
		}
	}
	
	/**
	 * Returns widget type parameter
	 * @param paramName
	 * @return the Widget type parameter
	 * @deprecated use getWidgetTypeParameter(String)
	 */
	public WidgetTypeParameter getShowletTypeParameter(String paramName) {
		return this.getWidgetTypeParameter(paramName);
	}
	
	/**
	 * Returns widget type parameter
	 * @param paramName
	 * @return the Widget type parameter
	 */
	public WidgetTypeParameter getWidgetTypeParameter(String paramName) {
		List<WidgetTypeParameter> parameters = this.getWidget().getType().getTypeParameters();
		for (WidgetTypeParameter param : parameters) {
			if (param.getName().equals(paramName)) {
				return param;
			}
		}
		return null;
	}

	public List<SmallContentType> getContentTypes() {
		return this.getContentManager().getSmallContentTypes();
	}

	public SmallContentType getContentType(String contentTypeCode) {
		return (SmallContentType) this.getContentManager().getSmallContentTypesMap().get(contentTypeCode);
	}

	public String getContentTypeCode() {
		return _contentTypeCode;
	}
	public void setContentTypeCode(String contentTypeCode) {
		this._contentTypeCode = contentTypeCode;
	}

	public List<String> getContentTypeCodes() {
		String contentTypesParam = this.getContentTypesFilter();
		List<String> contentTypes = FacetNavWidgetHelper.splitValues(contentTypesParam, ",");
		return contentTypes;
	}

	public void setContentTypesFilter(String contentTypesFilter) {
		this._contentTypesFilter = contentTypesFilter;
	}
	public String getContentTypesFilter() {
		return _contentTypesFilter;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}

	private String _contentTypeCode;
	private String _contentTypesFilter;

	private IContentManager _contentManager;

}