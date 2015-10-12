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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.apsadmin.system.ITreeAction;
import com.agiletec.apsadmin.system.ITreeNodeBaseActionHelper;
import com.agiletec.plugins.jpfacetnav.aps.system.JpFacetNavSystemConstants;
import com.agiletec.plugins.jpfacetnav.apsadmin.portal.specialwidget.util.FacetNavWidgetHelper;

/**
 * @author E.Santoboni
 */
public class FacetNavTreeWidgetAction extends FacetNavResultWidgetAction implements ITreeAction {
	
	@Override
	public void validate() {
		super.validate();
		try {
			this.validateFacets();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "validate");
		}
	}
	
	protected void validateFacets() {
		List<String> facetCodes = this.getFacetRootCodes();
		for (String facetCode : facetCodes) {
			if (null == this.getFacet(facetCode)) {
				String[] args = { facetCode };
				String fieldName = JpFacetNavSystemConstants.FACET_ROOTS_WIDGET_PARAM_NAME;
				this.addFieldError(fieldName, this.getText("message.facetNavWidget.facets.notValid", args));
			}
		}
	}
	
	/**
	 * Prepare action with the parameters contained in showlet.
	 */
	@Override
	protected void initSpecialParams() {
		super.initSpecialParams();
		if (null != this.getWidget().getConfig()) {
			String configParamName = JpFacetNavSystemConstants.FACET_ROOTS_WIDGET_PARAM_NAME;
			String facetsParam = this.getWidget().getConfig().getProperty(configParamName);
			this.setFacetRootNodes(facetsParam);
		}
	}
	
	/**
	 * Add a facet to the associated facet nodes
	 * @return The code describing the result of the operation.
	 */
	public String joinFacet() {
		try {
			this.createValuedShowlet();
			if (this.isValidFacet()) {
				String facetCode = this.getFacetCode();
				List<String> facetCodes = this.getFacetRootCodes();
				ITreeNode facet = this.getTreeNodeManager().getNode(this.getFacetCode());
				if (facet != null && !facet.getCode().equals(facet.getParent().getCode()) && !facetCodes.contains(facetCode)) {//se esiste, non è la Home e non è 
					facetCodes.add(facetCode);
					String facetsFilter = FacetNavWidgetHelper.concatStrings(facetCodes, ",");
					String configParamName = JpFacetNavSystemConstants.FACET_ROOTS_WIDGET_PARAM_NAME;
					this.getWidget().getConfig().setProperty(configParamName, facetsFilter);
					this.setFacetRootNodes(facetsFilter);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "joinFacet");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Remove a facet from the associated facet nodes
	 * @return The code describing the result of the operation.
	 */
	public String removeFacet() {
		try {
			this.createValuedShowlet();
			String facetCode = this.getFacetCode();
			List<String> facetCodes = this.getFacetRootCodes();
			if (facetCode != null) {
				facetCodes.remove(facetCode);
				String facetsFilter = FacetNavWidgetHelper.concatStrings(facetCodes, ",");
				String configParamName = JpFacetNavSystemConstants.FACET_ROOTS_WIDGET_PARAM_NAME;
				this.getWidget().getConfig().setProperty(configParamName, facetsFilter);
				this.setFacetRootNodes(facetsFilter);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeFacet");
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Returns true if the facet is valid
	 * @return true if the facet is valid
	 */
	private boolean isValidFacet() {
		String facetCode = this.getFacetCode();
		return (facetCode != null && this.getFacet(facetCode) != null);
	}

	public ITreeNode getFacetRoot() {
		return this.getTreeNodeManager().getRoot();
	}

	public ITreeNode getFacet(String facetCode) {
		return this.getTreeNodeManager().getNode(facetCode);
	}

	public String getFacetCode() {
		return _facetCode;
	}
	public void setFacetCode(String facetCode) {
		this._facetCode = facetCode;
	}

	public List<String> getFacetRootCodes() {
		String facetsParam = this.getFacetRootNodes();
		List<String> facetCodes = FacetNavWidgetHelper.splitValues(facetsParam, ",");
		return facetCodes;
	}

	public String getFacetRootNodes() {
		return _facetRootNodes;
	}
	public void setFacetRootNodes(String facetRootNodes) {
		this._facetRootNodes = facetRootNodes;
	}
	
	@Override
	public String buildTree() {
		Set<String> targets = this.getTreeNodesToOpen();
		try {
			this.createValuedShowlet();
			String marker = this.getTreeNodeActionMarkerCode();
			if (null != marker) {
				if (marker.equalsIgnoreCase(ACTION_MARKER_OPEN)) {
					targets = this.getTreeHelper().checkTargetNodes(this.getTargetNode(), targets, this.getNodeGroupCodes());
				} else if (marker.equalsIgnoreCase(ACTION_MARKER_CLOSE)) {
					targets = this.getTreeHelper().checkTargetNodesOnClosing(this.getTargetNode(), targets, this.getNodeGroupCodes());
				}
			}
			this.setTreeNodesToOpen(targets);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "buildTree");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public ITreeNode getShowableTree() {
		ITreeNode node = null;
		try {
			ITreeNode allowedTree = this.getAllowedTreeRootNode();
			node = this.getTreeHelper().getShowableTree(this.getTreeNodesToOpen(), allowedTree, this.getNodeGroupCodes());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getShowableTree");
		}
		return node;
	}
	
	@Override
	public ITreeNode getAllowedTreeRootNode() {
		ITreeNode node = null;
		try {
			node = this.getTreeHelper().getAllowedTreeRoot(this.getNodeGroupCodes());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAllowedTreeRootNode");
		}
		return node;
	}
	
	/**
	 * Return the allowed codes of the group of the nodes to manage.
	 * This method has to be extended if the helper manage tree nodes with authority.
	 * @return The allowed group codes.
	 */
	protected Collection<String> getNodeGroupCodes() {
		return null;
	}
	
	public String getTargetNode() {
		return _targetNode;
	}
	public void setTargetNode(String targetNode) {
		this._targetNode = targetNode;
	}
	
	public Set<String> getTreeNodesToOpen() {
		return _treeNodesToOpen;
	}
	public void setTreeNodesToOpen(Set<String> treeNodesToOpen) {
		this._treeNodesToOpen = treeNodesToOpen;
	}
	
	public String getTreeNodeActionMarkerCode() {
		return _treeNodeActionMarkerCode;
	}
	public void setTreeNodeActionMarkerCode(String treeNodeActionMarkerCode) {
		this._treeNodeActionMarkerCode = treeNodeActionMarkerCode;
	}
	
	protected ITreeNodeManager getTreeNodeManager() {
		return _treeNodeManager;
	}
	public void setTreeNodeManager(ITreeNodeManager treeNodeManager) {
		this._treeNodeManager = treeNodeManager;
	}
	
	protected ITreeNodeBaseActionHelper getTreeHelper() {
		return _treeHelper;
	}
	public void setTreeHelper(ITreeNodeBaseActionHelper treeHelper) {
		this._treeHelper = treeHelper;
	}
	
	private String _facetCode;
	private String _facetRootNodes;
	
	private String _targetNode;
	private Set<String> _treeNodesToOpen = new HashSet<String>();
	
	private String _treeNodeActionMarkerCode;

	private ITreeNodeManager _treeNodeManager;
	
	private ITreeNodeBaseActionHelper _treeHelper;

}