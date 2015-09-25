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
package org.entando.entando.plugins.jpwidgetutils.apsadmin.portal.specialwidget.replicator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import com.agiletec.apsadmin.system.ITreeAction;

/**
 * @author E.Santoboni
 */
public class ReplicatorWidgetAction extends SimpleWidgetConfigAction implements ITreeAction {

	private static final Logger _logger = LoggerFactory.getLogger(ReplicatorWidgetAction.class);
	
	@Override
	public void validate() {
		super.validate();
		if (this.getFieldErrors().size() > 0 || !this.validatePageFrame()) {
			try {
				this.createValuedShowlet();
			} catch (Throwable t) {
				_logger.error("error in validate", t);
				this.addActionError("error.genericError");
			}
		}
	}

	@Override
	public String init() {
		try {
			String result = super.init();
			return result;
		} catch (Throwable t) {
			_logger.error("error in init", t);
			return FAILURE;
		}
	}
	
	public String resetConfig() {
		try {
			this.createValuedShowlet();
			ApsProperties config = this.getWidget().getConfig();
			config.remove("pageCodeParam");
			config.remove("frameIdParam");
		} catch (Throwable t) {
			_logger.error("error in reset config", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String browseFrames() {
		try {
			this.createValuedShowlet();
			String pageCodeParam = this.getPageCodeParam();
			if (null == pageCodeParam || pageCodeParam.trim().length() == 0) {
				this.addActionError(this.getText("error.noPageSelected"));
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in browseFrames", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String selectFrame() {
		try {
			this.createValuedShowlet();
			if (!this.validatePageFrame()) {
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in selectFrame", t);
		}
		return SUCCESS;
	}

	public IPage getTargetPage() {
		if (this._targetPage == null) {
			this._targetPage = this.getPageManager().getPage(this.getPageCodeParam());
		}
		return this._targetPage;
	}

	protected boolean validatePageFrame() {
		boolean checked = false;
		String selectedNode = this.getPageCodeParam();
		if (null == selectedNode || selectedNode.trim().length() == 0) {
			this.addFieldError("pageCodeParam", this.getText("error.noPageSelected"));
		} else {
			IPage targetPage = this.getPage(selectedNode);
			if (targetPage == null) {
				this.addFieldError("pageCodeParam", this.getText("error.noPageSelected"));
			} else {
				if (this.getFrameIdParam() == null) {
					this.addFieldError("frameIdParam", this.getText("error.invalidWidgetType"));
				} else {
					int frame = this.getFrameIdParam().intValue();
					if (selectedNode.equals(this.getPageCode()) && frame == this.getFrame()) {
						this.addFieldError("frameIdParam", this.getText("error.target.currentFrame"));
					} else {
						Widget[] widgets = targetPage.getWidgets();
						if (widgets.length <= frame) {
							this.addFieldError("frameIdParam", this.getText("error.invalidWidgetType"));
						} else {
							Widget widget = widgets[frame];
							if (widget != null && this.getInvalidShowletTypes().contains(widget.getType().getCode())) {
								this.addFieldError("frameIdParam", this.getText("error.invalidWidgetType"));
							} else {
								checked = true;
							}
						}
					}
				}
			}
		}
		return checked;
	}

	protected boolean validateFrame() {
		boolean checked = false;
		String selectedNode = this.getPageCodeParam();
		if (null == selectedNode || selectedNode.trim().length() == 0) {
			this.addActionError(this.getText("error.noPageSelected"));
		} else {
			IPage targetPage = this.getPage(selectedNode);
			if (targetPage == null) {
				this.addActionError(this.getText("error.noPageSelected"));
			} else {
				Widget targetFrame = targetPage.getWidgets()[this.getFrameIdParam().intValue()];
				if (targetFrame == null || this.getInvalidShowletTypes().contains(targetFrame.getType().getCode())) {
					this.addActionError(this.getText("error.invalidWidgetType"));
				} else {
					checked = true;
				}
			}
		}
		return checked;
	}

	@Override
	public String buildTree() {
		Set<String> targets = this.getTreeNodesToOpen();
		try {
			this.createValuedShowlet();
			String marker = this.getTreeNodeActionMarkerCode();
			if (null != marker) {
				if (marker.equalsIgnoreCase(ACTION_MARKER_OPEN)) {
					targets = this.getPageActionHelper().checkTargetNodes(this.getTargetNode(), targets, this.getNodeGroupCodes());
				} else if (marker.equalsIgnoreCase(ACTION_MARKER_CLOSE)) {
					targets = this.getPageActionHelper().checkTargetNodesOnClosing(this.getTargetNode(), targets, this.getNodeGroupCodes());
				}
			}
			this.setTreeNodesToOpen(targets);
		} catch (Throwable t) {
			_logger.error("error in buildTree", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public ITreeNode getShowableTree() {
		ITreeNode node = null;
		try {
			ITreeNode allowedTree = this.getAllowedTreeRootNode();
			node = this.getPageActionHelper().getShowableTree(this.getTreeNodesToOpen(), allowedTree, this.getNodeGroupCodes());
		} catch (Throwable t) {
			_logger.error("error in getShowableTree", t);
		}
		return node;
	}

	@Override
	public ITreeNode getAllowedTreeRootNode() {
		ITreeNode node = null;
		try {
			node = this.getPageActionHelper().getAllowedTreeRoot(this.getNodeGroupCodes());
		} catch (Throwable t) {
			_logger.error("error in getAllowedTreeRootNode", t);
		}
		return node;
	}

	/**
	 * Return the allowed codes of the group of the nodes to manage.
	 * This method has to be extended if the helper manage tree nodes with authority.
	 * @return The allowed group codes.
	 */
	protected Collection<String> getNodeGroupCodes() {
		IPage page = this.getCurrentPage();
		Set<String> groupCodes = new HashSet<String>();
		groupCodes.add(Group.FREE_GROUP_NAME);
		groupCodes.add(page.getGroup());
		return groupCodes;
	}

	public Integer getFrameIdParam() {
		return _frameIdParam;
	}
	public void setFrameIdParam(Integer frameIdParam) {
		this._frameIdParam = frameIdParam;
	}

	public String getPageCodeParam() {
		return this._pageCodeParam;
	}
	public void setPageCodeParam(String pageCodeParam) {
		this._pageCodeParam = pageCodeParam;
	}

	protected List<String> getInvalidShowletTypes() {
		return _invalidShowletTypes;
	}
	public void setInvalidShowletTypes(List<String> invalidShowletTypes) {
		this._invalidShowletTypes = invalidShowletTypes;
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
	
	private String _pageCodeParam;
	private Integer _frameIdParam;

	private List<String> _invalidShowletTypes;

	private IPage _targetPage;

	private String _targetNode;
	private Set<String> _treeNodesToOpen = new HashSet<String>();

	private String _treeNodeActionMarkerCode;

}