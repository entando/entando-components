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
package com.agiletec.plugins.jpmyportalplus.aps.tags.util;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.tags.util.BaseFrameDecoratorContainer;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.IMyPortalConfigManager;

import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.IMyPortalPageModelManager;
import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalFrameConfig;

/**
 * @author E.Santoboni
 */
public class MyPortalWidgetDecoratorContainer extends BaseFrameDecoratorContainer {
	
	private static final Logger _logger = LoggerFactory.getLogger(MyPortalWidgetDecoratorContainer.class);
	
	@Override
	public boolean needsDecoration(Widget widget, RequestContext reqCtx) {
		try {
			IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
			IMyPortalConfigManager myportalConfigManager =
					(IMyPortalConfigManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.MYPORTAL_CONFIG_MANAGER, reqCtx.getRequest());
			IMyPortalPageModelManager myportalModelConfigManager =
					(IMyPortalPageModelManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.MYPORTAL_MODEL_CONFIG_MANAGER, reqCtx.getRequest());
			Set<String> allowedWidgets = myportalConfigManager.getConfig().getAllowedWidgets();
			PageModel model = page.getModel();
			Map<Integer, MyPortalFrameConfig> modelConfig = myportalModelConfigManager.getPageModelConfig(model.getCode());
			MyPortalFrameConfig frameConfig = (null != modelConfig) ? modelConfig.get(currentFrame) : null;
			if (null == frameConfig) return false;
			return (!frameConfig.isLocked() && allowedWidgets.contains(widget.getType().getCode()));
		} catch (Throwable t) {
			_logger.error("Error checking widget decorators", t);
			throw new RuntimeException("Error checking widget decorators", t);
		}
	}
	
	@Override
	public boolean isWidgetDecorator() {
		return true;
	}
	
}