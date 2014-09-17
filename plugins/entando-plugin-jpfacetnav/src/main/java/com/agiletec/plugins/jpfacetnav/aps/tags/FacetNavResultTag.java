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
package com.agiletec.plugins.jpfacetnav.aps.tags;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpfacetnav.aps.system.JpFacetNavSystemConstants;
import com.agiletec.plugins.jpfacetnav.aps.system.services.content.showlet.IFacetNavHelper;

/**
 * 
 * @author E.Santoboni
 */
public class FacetNavResultTag extends AbstractFacetNavTag {

	private static final Logger _logger = LoggerFactory.getLogger(FacetNavResultTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			List<String> requiredFacets = null;
			if (this.isExecuteExtractRequiredFacets()) {
				requiredFacets = this.getRequiredFacets();
				this.pageContext.setAttribute(this.getRequiredFacetsParamName(), requiredFacets);
			} else {
				requiredFacets = (List<String>) request.getAttribute(this.getRequiredFacetsParamName());
				if (requiredFacets == null) requiredFacets = new ArrayList<String>();
			}
			
			IFacetNavHelper facetNavHelper = (IFacetNavHelper) ApsWebApplicationUtils.getBean(JpFacetNavSystemConstants.CONTENT_FACET_NAV_HELPER, this.pageContext);
			List<String> result = facetNavHelper.getSearchResult(requiredFacets, reqCtx);
			this.pageContext.setAttribute(this.getResultParamName(), result);
			if (null != this.getBreadCrumbsParamName()) {
				this.pageContext.setAttribute(this.getBreadCrumbsParamName(), super.getBreadCrumbs(requiredFacets, reqCtx));
			}
		} catch (Throwable t) {
			_logger.error("error in startTag", t);
			throw new JspException("error in startTag", t);
		}
		return super.doStartTag();
	}
	
	
	public String getResultParamName() {
		return _resultParamName;
	}
	public void setResultParamName(String resultParamName) {
		this._resultParamName = resultParamName;
	}
	
	public boolean isExecuteExtractRequiredFacets() {
		return _executeExtractRequiredFacets;
	}
	public void setExecuteExtractRequiredFacets(boolean executeExtractRequiredFacets) {
		this._executeExtractRequiredFacets = executeExtractRequiredFacets;
	}

	public String getBreadCrumbsParamName() {
		return _breadCrumbsParamName;
	}
	public void setBreadCrumbsParamName(String breadCrumbsParamName) {
		this._breadCrumbsParamName = breadCrumbsParamName;
	}
	
	private String _resultParamName;
	private boolean _executeExtractRequiredFacets = true;
	private String _breadCrumbsParamName;
	
}
