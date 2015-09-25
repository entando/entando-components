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
