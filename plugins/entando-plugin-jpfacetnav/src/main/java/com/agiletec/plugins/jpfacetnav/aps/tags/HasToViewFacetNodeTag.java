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

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.ITreeNodeManager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class HasToViewFacetNodeTag extends AbstractFacetNavTag {

	private static final Logger _logger = LoggerFactory.getLogger(HasToViewFacetNodeTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		List<String> requiredFacets = super.getRequiredFacets();
		this.setRequiredFacets(requiredFacets);
		try {
			boolean hasToView = 
				this.getRequiredFacets().contains(this.getFacetNodeCode()) 
					|| (this.isParentSeleted() || this.isSelectedOneChild());
			if (hasToView) {
				return EVAL_BODY_INCLUDE;
			} else {
				return super.doStartTag();
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error initialization tag", t);
		}
	}
	
	/**
	 * Returns true if a child is selected.
	 * @return true if a child is selected
	 */
	private boolean isSelectedOneChild() {
		ITreeNodeManager facetManager = super.getFacetManager();
		List<String> requiredFacets = this.getRequiredFacets();
		for (int i=0; i<requiredFacets.size(); i++) {
			String requiredFacet = requiredFacets.get(i);
			ITreeNode facet = facetManager.getNode(requiredFacet);
			if (null != facet) {
				boolean check = this.checkSelectChild(facet, this.getFacetNodeCode());
				if (check) return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the selected child is checked
	 * @param facet
	 * @param codeForCheck
	 * @return true if the selected child is selected
	 */
	private boolean checkSelectChild(ITreeNode facet, String codeForCheck) {
		if (facet.getCode().equals(codeForCheck)) {
			return true;
		}
		ITreeNode parentFacet = facet.getParent();
		if (null != parentFacet && !parentFacet.getCode().equals(parentFacet.getParent().getCode())) {
			return this.checkSelectChild(parentFacet, codeForCheck);
		}
		return false;
	}

	/**
	 * Returns true if a parent is selected
	 * @return true if a parent is selected
	 */
	private boolean isParentSeleted() {
		ITreeNodeManager facetManager = super.getFacetManager();
		ITreeNode facet = facetManager.getNode(this.getFacetNodeCode());
		ITreeNode parent = facet.getParent();
		return this.getRequiredFacets().contains(parent.getCode());
	}

	public String getFacetNodeCode() {
		return _facetNodeCode;
	}
	public void setFacetNodeCode(String facetNodeCode) {
		this._facetNodeCode = facetNodeCode;
	}
	
	@Override
	public List<String> getRequiredFacets() {
		if (null == this._facetNodeCode) {
			if (null == this.getRequiredFacetsParamName()) {
				return new ArrayList<String>();
			} else {
				ServletRequest request = this.pageContext.getRequest();
				List<String> list = (List<String>) request.getAttribute(this.getRequiredFacetsParamName());
				if (null == list) {
					return new ArrayList<String>();
				} else {
					return list;
				}
			}
		}
		return _requiredFacets;
	}
	public void setRequiredFacets(List<String> requiredFacets) {
		this._requiredFacets = requiredFacets;
	}

	private String _facetNodeCode;//="${facetNode.code}"
	private List<String> _requiredFacets;//="requiredFacets" 

}
