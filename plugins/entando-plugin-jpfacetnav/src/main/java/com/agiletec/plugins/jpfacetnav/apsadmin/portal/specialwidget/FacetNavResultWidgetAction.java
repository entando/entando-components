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
public class FacetNavResultWidgetAction extends SimpleWidgetConfigAction implements IFacetNavResultWidgetAction {
	
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
				String fieldName = JpFacetNavSystemConstants.CONTENT_TYPES_FILTER_SHOWLET_PARAM_NAME;
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
	 * Join content type
	 */
	@Override
	public String joinContentType() {
		try {
			this.createValuedShowlet();
			List<String> contentTypes = this.getContentTypeCodes();
			String contentTypeCode = this.getContentTypeCode();
			if (contentTypeCode!=null && contentTypeCode.length()>0 && !contentTypes.contains(contentTypeCode) && this.getContentType(contentTypeCode)!=null) {
				contentTypes.add(contentTypeCode);
				String contentTypesFilter = FacetNavWidgetHelper.concatStrings(contentTypes, ",");
				String configParamName = JpFacetNavSystemConstants.CONTENT_TYPES_FILTER_SHOWLET_PARAM_NAME;
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
	 * Remove content type
	 */
	@Override
	public String removeContentType() {
		try {
			this.createValuedShowlet();
			List<String> contentTypes = this.getContentTypeCodes();
			String contentTypeCode = this.getContentTypeCode();
			if (contentTypeCode != null) {
				contentTypes.remove(contentTypeCode);
				String contentTypesFilter = FacetNavWidgetHelper.concatStrings(contentTypes, ",");
				String configParamName = JpFacetNavSystemConstants.CONTENT_TYPES_FILTER_SHOWLET_PARAM_NAME;
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
	 * Prepare action with the parameters contained in showlet.
	 */
	protected void initSpecialParams() {
		if (null != this.getWidget().getConfig()) {
			String paramName = JpFacetNavSystemConstants.CONTENT_TYPES_FILTER_SHOWLET_PARAM_NAME;
			String configParamName = this.getWidget().getConfig().getProperty(paramName);
			this.setContentTypesFilter(configParamName);
		}
	}
	
	/**
	 * Returns showlet type parameter
	 * @param paramName
	 * @return com.agiletec.aps.system.services.page.Widget type parameter
	 */
	public WidgetTypeParameter getShowletTypeParameter(String paramName) {
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