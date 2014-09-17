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
package com.agiletec.plugins.jpfacetnav.aps.tags.util;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.ITreeNodeManager;

/**
 * Representation of an object "breadcrumb trail" for faceted navigation.
 * The object makes available the list of nodes between a root node and the node selected,
 * way as to allow the construction of a special user interface.
 * @author E.Santoboni
 */
public class FacetBreadCrumbs {

	/**
	 * Object constructor.
	 * @param requiredFacet The code of the node requested.
	 * @param facetRoot Code facet root.
	 * @param facetManager The manager nodes facet.
	 */
	public FacetBreadCrumbs(String requiredFacet, String facetRoot, ITreeNodeManager facetManager) {
		this.setFacetRoot(facetRoot);
		this.setRequiredFacet(requiredFacet);
		ITreeNode requiredNode = facetManager.getNode(requiredFacet);
		this.addBreadCrumbsNode(requiredNode);
	}

	/**
	 * Add bread crumbs node
	 * @param node 
	 */
	private void addBreadCrumbsNode(ITreeNode node) {
		this.getBreadCrumbs().add(0, node.getCode());
		if (node.getCode().equals(this.getFacetRoot())) return;
		ITreeNode parentNode = node.getParent();
		if (null != parentNode && !parentNode.getCode().equals(node.getCode())) {
			this.addBreadCrumbsNode(parentNode);
		}
	}

	/**
	 * Returns the code of node requested.
	 * @return The code of the node requested.
	 */
	public String getRequiredFacet() {
		return _requiredFacet;
	}

	/**
	 * Set the code of the node requested.
	 * @param requiredFacet The code of the node requested.
	 */
	protected void setRequiredFacet(String requiredFacet) {
		this._requiredFacet = requiredFacet;
	}

	/**
	 * Return code facet root.
	 * @return Code facet root.
	 */
	public String getFacetRoot() {
		return _facetRoot;
	}

	/**
	 * Set the code facet root.
	 * @param facetRoot Set the code facet root.
	 */
	protected void setFacetRoot(String facetRoot) {
		this._facetRoot = facetRoot;
	}

	/**
	 * Returns the list of nodes between the facet and the root node requested.
	 * @return The list of nodes between the facet and the root node requested.
	 */
	public List<String> getBreadCrumbs() {
		return _breadCrumbs;
	}

	private String _requiredFacet;
	private String _facetRoot;
	private List<String> _breadCrumbs = new ArrayList<String>();

}
