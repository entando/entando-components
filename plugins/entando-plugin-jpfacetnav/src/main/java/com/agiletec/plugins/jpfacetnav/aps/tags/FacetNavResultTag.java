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
import com.agiletec.plugins.jpfacetnav.aps.system.services.content.widget.IFacetNavHelper;

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
