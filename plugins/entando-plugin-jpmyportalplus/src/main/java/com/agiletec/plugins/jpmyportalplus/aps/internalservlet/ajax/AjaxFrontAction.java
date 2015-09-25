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
package com.agiletec.plugins.jpmyportalplus.aps.internalservlet.ajax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.plugins.jpmyportalplus.aps.internalservlet.AbstractFrontAction;
import com.agiletec.plugins.jpmyportalplus.aps.internalservlet.util.FrameSelectItem;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;

import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalFrameConfig;

/**
 * @author E.Santoboni
 */
public class AjaxFrontAction extends AbstractFrontAction {

	@Override
	public String removeFrame() {
		boolean result = false;
		try {
			result = this.executeResetFrame();
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeFrame", "Error on removeFrame");
		}
		this.setRemoveResult(String.valueOf(result));
		return SUCCESS;
	}

	@Override
	public String addWidgets() {
		throw new RuntimeException("ACTION NOT SUPPORTED - addWidgets");
	}

	@Override
	public String closeFrame() {
		//System.out.println("Frame to resize " + this.getFrameToResize());
		boolean result = false;
		try {
			result = this.executeCloseFrame();
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "closeFrame", "Error on closeFrame");
		}
		this.setResizeResult(String.valueOf(result));
		return SUCCESS;
	}

	@Override
	public String openFrame() {
		//System.out.println("Frame to resize " + this.getFrameToResize());
		boolean result = false;
		try {
			result = this.executeOpenFrame();
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "openFrame", "Error on openFrame");
		}
		this.setResizeResult(String.valueOf(result));
		return SUCCESS;
	}

	@Override
	public String resetFrames() {
		throw new RuntimeException("ACTION NOT SUPPORTED - resetFrames");
	}

	@Override
	public String openSwapSection() {
		//System.out.println("Frame where opens section " + this.getFrameWhereOpenSection());
		List<FrameSelectItem> selectItems = new ArrayList<FrameSelectItem>();
		this.setSelectItems(selectItems);
		try {
			IPage currentPage = this.getCurrentPage();
			PageModel pageModel = currentPage.getModel();
			Map<Integer, MyPortalFrameConfig> modelConfig = super.getMyPortalModelConfig(pageModel.getCode());
			MyPortalFrameConfig currentFrameConfig = (null != modelConfig) ? modelConfig.get(this.getFrameWhereOpenSection()) : null;
			Integer currentColumnId = (null != currentFrameConfig) ? currentFrameConfig.getColumn() : null;
			//Integer currentColumnId = pageModel.getFrameConfigs()[this.getFrameWhereOpenSection()].getColumn();
			if (null == currentColumnId) {
				return SUCCESS;
			}
			CustomPageConfig config = this.getCustomPageConfig();
			Widget[] customShowlets = (null == config || config.getConfig() == null) ? null : config.getConfig();
			Widget[] widgetsToRender = this.getPageUserConfigManager().getWidgetsToRender(currentPage, customShowlets);
			Lang currentLang = this.getCurrentLang();
			String voidWidgetCode = this.getPageUserConfigManager().getVoidWidget().getCode();
			for (int i = 0; i < widgetsToRender.length; i++) {
				//Frame frame = pageModel.getFrameConfigs()[i];
				//Integer columnId = frame.getColumn();
				MyPortalFrameConfig frameConfig = (null != modelConfig) ? modelConfig.get(i) : null;
				if (null == frameConfig || frameConfig.isLocked() || null == frameConfig.getColumn() || i == this.getFrameWhereOpenSection().intValue()) continue;
				Widget widget = widgetsToRender[i];
				Integer columnId = frameConfig.getColumn();
				if (columnId.equals(currentColumnId)) {
					if (widget != null && !widget.getType().getCode().equals(voidWidgetCode)) {
						FrameSelectItem item = new FrameSelectItem(currentColumnId, columnId, widget, i, currentLang);
						selectItems.add(item);
					}
				} else {
					if (widget == null || widget.getType().getCode().equals(voidWidgetCode)) {
						boolean check = this.check(columnId);
						if (!check) {
							FrameSelectItem item = new FrameSelectItem(currentColumnId, columnId, null, i, currentLang);
							selectItems.add(item);
						}
					}
				}
				if (i == this.getFrameWhereOpenSection() && null != widget) {
					this.setShowletCodeOnOpeningSection(widget.getType().getCode());
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "openSwapSection", "Error on opening Swap Section");
		}
		return SUCCESS;
	}

	private boolean check(Integer columnId) {
		for (int i=0; i<this.getSelectItems().size(); i++) {
			FrameSelectItem frameSelectItem = this.getSelectItems().get(i);
			if (columnId.equals(frameSelectItem.getColumnIdDest())) {
				return true;
			}
		}
		return false;
	}

	public String getRemoveResult() {
		return _removeResult;
	}
	protected void setRemoveResult(String removeResult) {
		this._removeResult = removeResult;
	}

	public String getResizeResult() {
		return _resizeResult;
	}
	protected void setResizeResult(String resizeResult) {
		this._resizeResult = resizeResult;
	}

	public Integer getFrameWhereOpenSection() {
		return _frameWhereOpenSection;
	}
	public void setFrameWhereOpenSection(Integer frameWhereOpenSection) {
		this._frameWhereOpenSection = frameWhereOpenSection;
	}

	public List<FrameSelectItem> getSelectItems() {
		return _selectItems;
	}
	protected void setSelectItems(List<FrameSelectItem> selectItems) {
		this._selectItems = selectItems;
	}

	public String getShowletCodeOnOpeningSection() {
		return _showletCodeOnOpeningSection;
	}
	protected void setShowletCodeOnOpeningSection(String showletCodeOnOpeningSection) {
		this._showletCodeOnOpeningSection = showletCodeOnOpeningSection;
	}

	private String _removeResult;

	private String _resizeResult;

	private Integer _frameWhereOpenSection;
	private List<FrameSelectItem> _selectItems;
	private String _showletCodeOnOpeningSection;

}