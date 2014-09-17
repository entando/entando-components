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

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.ITreeNodeManager;

/**
 * 
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
				|| (this.isParentSeleted() || this.isSelectedOneChild()) 
				//&& !this.hasBrotherSelected() //&& NON HA FRATELLI SELEZIONATI 
				//&& !this.hasBrotherWithChildSelected();//&& NON HA FRATELLI CON FIGLI SELEZIONATI
				;
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

	/*
	private boolean hasBrotherSelected() {
		ITreeNodeManager facetManager = super.getFacetManager();
		ITreeNode parent = facetManager.getNode(this.getFacetNodeCode()).getParent();
		for (int i=0; i<parent.getChildren().length; i++) {
			ITreeNode node = parent.getChildren()[i];
			if (!node.getCode().equals(this.getFacetNodeCode()) && this.getRequiredFacets().contains(node.getCode())) return true;
		}
		return false;
	}

	private boolean hasBrotherWithChildSelected() {
		ITreeNodeManager facetManager = super.getFacetManager();
		ITreeNode parent = facetManager.getNode(this.getFacetNodeCode()).getParent();
		for (int i=0; i<parent.getChildren().length; i++) {
			ITreeNode node = parent.getChildren()[i];
			if (!node.getCode().equals(this.getFacetNodeCode())) {
				boolean check = this.checkSelectChild(node, this.getFacetNodeCode());
				if (check) return true;
			}
		}
		return false;
	}
	 */

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

	public List<String> getRequiredFacets() {
		return _requiredFacets;
	}
	public void setRequiredFacets(List<String> requiredFacets) {
		this._requiredFacets = requiredFacets;
	}

	private String _facetNodeCode;//="${facetNode.code}"
	private List<String> _requiredFacets;//="requiredFacets" 

}
