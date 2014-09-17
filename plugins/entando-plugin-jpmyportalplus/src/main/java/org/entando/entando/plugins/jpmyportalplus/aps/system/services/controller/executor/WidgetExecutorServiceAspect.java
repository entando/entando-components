/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpmyportalplus.aps.system.services.controller.executor;

import java.util.List;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.tags.util.IFrameDecoratorContainer;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.PageUserConfigBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import org.entando.entando.aps.system.services.controller.executor.AbstractWidgetExecutorService;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
@Aspect
public class WidgetExecutorServiceAspect extends AbstractWidgetExecutorService {
	
	private static final Logger _logger = LoggerFactory.getLogger(WidgetExecutorServiceAspect.class);
	
	@After("execution(* org.entando.entando.aps.system.services.controller.executor.WidgetExecutorService.service(..)) && args(reqCtx,..)")
    public void injectMyPortalBean(RequestContext reqCtx) {
        HttpServletRequest req = reqCtx.getRequest();
		try {
			HttpSession session = req.getSession();
			Lang lang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			req.getSession().setAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_LANG, lang);
			UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (!currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
				PageUserConfigBean userConfigBean = (PageUserConfigBean) session.getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG);
				if (null == userConfigBean || !currentUser.getUsername().equals(userConfigBean.getUsername())) {
					IPageUserConfigManager pageUserConfigManager = (IPageUserConfigManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.PAGE_USER_CONFIG_MANAGER, req);
					userConfigBean = pageUserConfigManager.getUserConfig(currentUser);
					if (null != userConfigBean) {
						session.setAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG, userConfigBean);
					} else {
						session.removeAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG);
					}
				}
			} else {
				session.removeAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG);
			}
			String[] widgetOutput = (String[]) reqCtx.getExtraParam("ShowletOutput");
			IPage currentPage = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			this.buildWidgetOutput(currentPage, widgetOutput, reqCtx);
		} catch (Throwable t) {
			String msg = "Error detected during preprocessing doStartTag";
			_logger.error("Error in doStartTag", t);
			throw new RuntimeException(msg, t);
		}
    }
	
	protected void buildWidgetOutput(IPage page, String[] widgetOutput, RequestContext reqCtx) throws ApsSystemException {
		HttpServletRequest req = reqCtx.getRequest();
		try {
			IPageUserConfigManager pageUserConfigManager = (IPageUserConfigManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.PAGE_USER_CONFIG_MANAGER, req);
			CustomPageConfig customPageConfig = null;
			UserDetails currentUser = (UserDetails) req.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
				customPageConfig = pageUserConfigManager.getGuestPageConfig(page, req);
			} else {
				PageUserConfigBean userConfigBean = (PageUserConfigBean) req.getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG);
				if (null != userConfigBean) {
					customPageConfig = userConfigBean.getConfig().get(page.getCode());
				}
			}
			if (null == customPageConfig || customPageConfig.getConfig() == null || !customPageConfig.getPageCode().equals(page.getCode())) {
				req.getSession().removeAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG);
				return;
			}
			req.getSession().setAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG, customPageConfig);
			Widget[] customWidgets = customPageConfig.getConfig();
			List<IFrameDecoratorContainer> decorators = this.extractDecorators(reqCtx);
			for (int frame = 0; frame < customWidgets.length; frame++) {
				Widget widget = customWidgets[frame];
				if (null != widget && null != widget.getType()) {
					IWidgetTypeManager widgetTypeManager = (IWidgetTypeManager) ApsWebApplicationUtils.getBean(SystemConstants.WIDGET_TYPE_MANAGER, req);
					if (null != widgetTypeManager.getWidgetType(widget.getType().getCode())) {
						reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME, new Integer(frame));
						widgetOutput[frame] = this.buildWidgetOutput(reqCtx, widget, decorators);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error detected preprocessing widget", t);
			String msg = "Error detected preprocessing widget";
			throw new ApsSystemException(msg, t);
		}
	}
	
}
