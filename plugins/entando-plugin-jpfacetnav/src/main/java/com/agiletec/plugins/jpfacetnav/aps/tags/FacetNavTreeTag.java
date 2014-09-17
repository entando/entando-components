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

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpfacetnav.aps.system.JpFacetNavSystemConstants;
import com.agiletec.plugins.jpfacetnav.aps.system.services.content.showlet.IFacetNavHelper;

/**
 *
 * @author E.Santoboni
 */
public class FacetNavTreeTag extends AbstractFacetNavTag {

	private static final Logger _logger = LoggerFactory.getLogger(FacetNavTreeTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			List<String> requiredFacets = this.getRequiredFacets();

			IFacetNavHelper facetNavHelper = (IFacetNavHelper) ApsWebApplicationUtils.getBean(JpFacetNavSystemConstants.CONTENT_FACET_NAV_HELPER, this.pageContext);
			Map<String, Integer> occurrences = facetNavHelper.getOccurences(requiredFacets, reqCtx);

			List<ITreeNode> facetsForTree = this.getFacetRootNodes(reqCtx);
			this.pageContext.setAttribute(this.getFacetsTreeParamName(), facetsForTree);
			this.pageContext.setAttribute("occurrences", occurrences);
			request.setAttribute(this.getRequiredFacetsParamName(), requiredFacets);
		} catch (Throwable t) {
			_logger.error("Error in doStartTag", t);
			throw new JspException("Error initialization tag", t);
		}
		return super.doStartTag();
	}

	protected List<ITreeNode> getFacetRootNodes(RequestContext reqCtx) {
		List<ITreeNode> facets = null;
		Widget currentWidget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
		String configParamName = JpFacetNavSystemConstants.FACET_ROOTS_SHOWLET_PARAM_NAME;
		String facetParamConfig = currentWidget.getConfig().getProperty(configParamName);
		if (null != facetParamConfig && facetParamConfig.trim().length()>0) {
			facets = super.getFacetRoots(facetParamConfig);
		}
		return facets;
	}

	public String getFacetsTreeParamName() {
		return _facetsTreeParamName;
	}
	public void setFacetsTreeParamName(String facetsTreeParamName) {
		this._facetsTreeParamName = facetsTreeParamName;
	}

	private String _facetsTreeParamName;

}
