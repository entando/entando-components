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
package org.entando.entando.plugins.jpwidgetutils.aps.tags;

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
						String output = AbstractWidgetExecutorService.extractWidgetOutput(reqCtx, widgetType);
						this.pageContext.getOut().print(output);
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