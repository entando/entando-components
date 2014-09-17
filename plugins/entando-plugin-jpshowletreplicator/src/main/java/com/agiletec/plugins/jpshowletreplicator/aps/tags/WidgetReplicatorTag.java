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
package com.agiletec.plugins.jpshowletreplicator.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.aps.system.services.widgettype.WidgetType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.entando.entando.aps.system.services.controller.executor.AbstractWidgetExecutorService;
import org.entando.entando.aps.system.services.controller.executor.WidgetExecutorService;

/**
 * @author E.Santoboni
 */
public class WidgetReplicatorTag extends TagSupport {
	
	private static final Logger _logger = LoggerFactory.getLogger(WidgetReplicatorTag.class);
	
	@Override
	public int doEndTag() throws JspException {
		ServletRequest req =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		try {
			Widget currentWidget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			String pageCode = currentWidget.getConfig().getProperty("pageCodeParam");
			IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.pageContext);
			IPage targetPage = pageManager.getPage(pageCode);
			if (null != targetPage) {
				String frameIdString = currentWidget.getConfig().getProperty("frameIdParam");
				int frameId = Integer.parseInt(frameIdString);
				Widget[] widgets = targetPage.getWidgets();
				if (widgets.length >= frameId) {
					Widget targetWidget = targetPage.getWidgets()[frameId];
					if (null != targetWidget) {
						reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET, targetWidget);
						WidgetType widgetType = targetWidget.getType();
						String jspPath = widgetType.getJspPath();
						/*
						if (widgetType.isLogic()) {
							widgetType = widgetType.getParentType();
						}
						String pluginCode = widgetType.getPluginCode();
						boolean isPluginShowlet = (null != pluginCode && pluginCode.trim().length()>0);
						StringBuilder jspPath = new StringBuilder("/WEB-INF/");
						if (isPluginShowlet) {
							jspPath.append("plugins/").append(pluginCode.trim()).append("/");
						}
						jspPath.append(WIDGET_LOCATION).append(widgetType.getCode()).append(".jsp");
						*/
						this.pageContext.include(jspPath);
					}
				}
			}
		} catch (Throwable t) {
			String msg = "Errore in preelaborazione widgets";
			_logger.error("error in doEndTag", t);
			throw new JspException(msg, t);
		}
		return EVAL_PAGE;
	}
	
}