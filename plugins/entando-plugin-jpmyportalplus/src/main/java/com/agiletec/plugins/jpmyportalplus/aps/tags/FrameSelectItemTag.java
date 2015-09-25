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
package com.agiletec.plugins.jpmyportalplus.aps.tags;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;
import com.agiletec.plugins.jpmyportalplus.aps.tags.util.FrameSelectItem;

import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.IMyPortalPageModelManager;
import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalFrameConfig;

/**
 * Returns the list of select items to use in the select inside the frame swap function of each widget
 * @author E.Santoboni
 */
public class FrameSelectItemTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(FrameSelectItemTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		RequestContext reqCtx = (RequestContext) this.pageContext.getRequest().getAttribute(RequestContext.REQCTX);
		List<FrameSelectItem> selectItems = new ArrayList<FrameSelectItem>();
		IPageUserConfigManager pageUserConfigManager = (IPageUserConfigManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.PAGE_USER_CONFIG_MANAGER, pageContext);
		IMyPortalPageModelManager myportalModelConfigManager = 
						(IMyPortalPageModelManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.MYPORTAL_MODEL_CONFIG_MANAGER, this.pageContext);
		try {
			Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
			IPage currentPage = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			PageModel pageModel = currentPage.getModel();
			Map<Integer, MyPortalFrameConfig> modelConfig = myportalModelConfigManager.getPageModelConfig(pageModel.getCode());
			MyPortalFrameConfig currentFrameConfig = (null != modelConfig) ? modelConfig.get(currentFrame) : null;
			Integer currentColumnId = (null != currentFrameConfig) ? currentFrameConfig.getColumn() : null;
			if (null == currentColumnId) {
				return super.doStartTag();
			}
			Lang currentLang = (Lang) this.pageContext.getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_LANG);
			Widget[] customShowletConfig = this.getCustomShowletConfig(currentPage, pageUserConfigManager);
			Widget[] showletsToRender = pageUserConfigManager.getShowletsToRender(currentPage, customShowletConfig);
			String voidShowletCode = pageUserConfigManager.getVoidShowlet().getCode();
			for (int i = 0; i < showletsToRender.length; i++) {
				//Frame frame = pageModel.getConfiguration()[i];
				MyPortalFrameConfig frameConfig = (null != modelConfig) ? modelConfig.get(i) : null;
				//Integer columnId = frame.getColumn();
				if (null == frameConfig || frameConfig.isLocked() || null == frameConfig.getColumn() || i == currentFrame.intValue()) continue;
				Integer columnId = frameConfig.getColumn();
				Widget showlet = showletsToRender[i];
				if (columnId.equals(currentColumnId)) {
					if (showlet != null && !showlet.getType().getCode().equals(voidShowletCode)) {
						FrameSelectItem item = new FrameSelectItem(currentColumnId, columnId,
								showlet, i, currentLang);
						selectItems.add(item);
					}
				} else {
					if (showlet == null || showlet.getType().getCode().equals(voidShowletCode)) {
						boolean check = this.check(selectItems, columnId);
						if (!check) {
							FrameSelectItem item = new FrameSelectItem(currentColumnId, columnId,
									null, i, currentLang);
							selectItems.add(item);
						}
					}
				}
			}
			this.pageContext.setAttribute(this.getVar(), selectItems);
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("error in doStartTag", t);
		}
		return super.doStartTag();
	}

	private boolean check(List<FrameSelectItem> selectItems, Integer columnId) {
		for (Iterator iterator = selectItems.iterator(); iterator.hasNext();) {
			FrameSelectItem frameSelectItem = (FrameSelectItem) iterator.next();
			if (columnId.equals(frameSelectItem.getColumnIdDest())) {
				return true;
			}
		}
		return false;
	}

	protected Widget[] getCustomShowletConfig(IPage currentPage, IPageUserConfigManager pageUserConfigManager) throws Throwable {
		Widget[] customShowlets = null;
		try {
			CustomPageConfig customPageConfig =
				(CustomPageConfig) this.pageContext.getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG);
			if (customPageConfig != null && !customPageConfig.getPageCode().equals(currentPage.getCode())) {
				//throw new RuntimeException("Current page '" + currentPage.getCode()
				//		+ "' not equals then pageCode of custom config param '" + customPageConfig.getPageCode() + "'");
				_logger.error("Current page '{}' not equals then pageCode of custom config param '{}'",currentPage.getCode(), customPageConfig.getPageCode());
				return null;
			}
			if (null != customPageConfig) {
				customShowlets = customPageConfig.getConfig();
			}
		} catch (Throwable t) {
			_logger.error("Error loading custom widgets config", t);
			String message = "Error loading custom widgets config";
			throw new ApsSystemException(message, t);
		}
		return customShowlets;
	}

	public String getVar() {
		return var;
	}
	public void setVar(String var) {
		this.var = var;
	}

	private String var;

}
