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

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.ITreeNodeManager;

/**
 * 
 * @author E.Santoboni
 */
public class HasToOpenFacetNodeTag extends AbstractFacetNavTag {

	private static final Logger _logger = LoggerFactory.getLogger(HasToOpenFacetNodeTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		try {
			boolean hasToOpen = 
				((this.getRequiredFacets().contains(this.getFacetNodeCode())) || this.isSelectedOneChild()) 
				&& this.hasChildrenOccurrences(); 
			if (hasToOpen) {
				return EVAL_BODY_INCLUDE;
			} else {
				return super.doStartTag();
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error doStartTag", t);
		}
	}

	/**
	 * Returns true if one child is selected
	 * @return True if one child is selected
	 */
	private boolean isSelectedOneChild() {
		ITreeNodeManager facetManager = this.getFacetManager();
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
	 * Returns true if a child is selected.
	 * @param facet
	 * @param codeForCheck 
	 * @return true if a child is selected
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
	 * Returns true if there are children occurrences.
	 * @return true if there are children occurrences
	 */
	private boolean hasChildrenOccurrences() {
		ITreeNodeManager facetManager = this.getFacetManager();
		ITreeNode facet = facetManager.getNode(this.getFacetNodeCode());
		for (int i=0; i<facet.getChildren().length; i++) {
			ITreeNode child = facet.getChildren()[i];
			Integer occurrence = this.getOccurrences().get(child.getCode());
			if (null != occurrence && occurrence.intValue()>0) {
				return true;
			}
		}
		return false;
	}

	public String getFacetNodeCode() {
		return _facetNodeCode;
	}
	public void setFacetNodeCode(String facetNodeCode) {
		this._facetNodeCode = facetNodeCode;
	}
	public List<String> getRequiredFacets() {
		return _requiredFacets;
	}
	public void setRequiredFacets(List<String> requiredFacets) {
		this._requiredFacets = requiredFacets;
	}
	public Map<String, Integer> getOccurrences() {
		return _occurrences;
	}
	public void setOccurrences(Map<String, Integer> occurrences) {
		this._occurrences = occurrences;
	}

	private String _facetNodeCode;//="${facetNode.code}"
	private List<String> _requiredFacets;//="requiredFacets" 
	private Map<String, Integer> _occurrences; //="${occurrences}"

}
