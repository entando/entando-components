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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.Frame;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;
import com.agiletec.plugins.jpmyportalplus.aps.tags.util.WidgetCheckInfo;

import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.IMyPortalPageModelManager;
import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalFrameConfig;

/**
 * Returns the list of widget (in form of {@link WidgetCheckInfo}) to use into the function of page configuration.
 * @author E.Santoboni
 */
public class CustomizableWidgetsTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(CustomizableWidgetsTag.class);
	
	@Override
    public int doStartTag() throws JspException {
        RequestContext reqCtx = (RequestContext) this.pageContext.getRequest().getAttribute(RequestContext.REQCTX);
        List<WidgetCheckInfo> checkInfos = new ArrayList<WidgetCheckInfo>();
        IPageUserConfigManager pageUserConfigManager = (IPageUserConfigManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.PAGE_USER_CONFIG_MANAGER, pageContext);
        IMyPortalPageModelManager myportalModelConfigManager = 
						(IMyPortalPageModelManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.MYPORTAL_MODEL_CONFIG_MANAGER, this.pageContext);
		try {
            Lang currentLang = (Lang) this.pageContext.getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_LANG);
            IPage currentPage = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
            Widget[] customShowletConfig = this.getCustomShowletConfig(currentPage);
            Widget[] showletsToRender = pageUserConfigManager.getShowletsToRender(currentPage, customShowletConfig);
            List<String> allowedShowlets = new ArrayList<String>();
            Map<String, WidgetType> customizableShowlets = this.getCustomizableShowlets(pageUserConfigManager);
            allowedShowlets.addAll(customizableShowlets.keySet());
			Map<Integer, MyPortalFrameConfig> modelConfig = myportalModelConfigManager.getPageModelConfig(currentPage.getModel().getCode());
            Frame[] frames = currentPage.getModel().getConfiguration();
            for (int i = 0; i < frames.length; i++) {
                //Frame frame = frames[i];
				MyPortalFrameConfig frameConfig = (null != modelConfig) ? modelConfig.get(i) : null;
                if (null != frameConfig && !frameConfig.isLocked()) {
                    Widget showlet = showletsToRender[i];
                    if (null != showlet && allowedShowlets.contains(showlet.getType().getCode())) {
                        WidgetCheckInfo info = new WidgetCheckInfo(showlet.getType(), true, currentLang);
                        allowedShowlets.remove(showlet.getType().getCode());
                        checkInfos.add(info);
                    }
                }
            }
            for (int i = 0; i < allowedShowlets.size(); i++) {
                String code = allowedShowlets.get(i);
                WidgetType type = customizableShowlets.get(code);
                WidgetCheckInfo info = new WidgetCheckInfo(type, false, currentLang);
                checkInfos.add(info);
            }
            BeanComparator comparator = new BeanComparator("title");
            Collections.sort(checkInfos, comparator);
            this.pageContext.setAttribute(this.getVar(), checkInfos);
        } catch (Throwable t) {
        	_logger.error("error in doStartTag", t);
            throw new JspException("Error on doStartTag", t);
        }
        return super.doStartTag();
    }

    protected Widget[] getCustomShowletConfig(IPage currentPage) throws Throwable {
        Widget[] customShowlets = null;
        try {
            CustomPageConfig customPageConfig =
                    (CustomPageConfig) this.pageContext.getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG);
            if (customPageConfig != null && !customPageConfig.getPageCode().equals(currentPage.getCode())) {
                throw new RuntimeException("Current page '" + currentPage.getCode()
                        + "' not equals then pageCode of custom config param '" + customPageConfig.getPageCode() + "'");
            }
            if (null != customPageConfig) {
                customShowlets = customPageConfig.getConfig();
            }
        } catch (Throwable t) {
        	_logger.error("Error extracting custom widgets", t);
            String message = "Error extracting custom showlets";
            throw new ApsSystemException(message, t);
        }
        return customShowlets;
    }

    private Map<String, WidgetType> getCustomizableShowlets(IPageUserConfigManager pageUserConfigManager) throws ApsSystemException {
        Map<String, WidgetType> map = new HashMap<String, WidgetType>();
        UserDetails currentUser = (UserDetails) this.pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
        try {
            List<WidgetType> list = pageUserConfigManager.getCustomizableWidgets(currentUser);
            for (int i = 0; i < list.size(); i++) {
                WidgetType type = list.get(i);
                map.put(type.getCode(), type);
            }
        } catch (Throwable t) {
        	_logger.error("Error extracting customizable Showlets by user {}", currentUser.getUsername(), t);
            String message = "Error extracting customizable Showlets by user '" + currentUser.getUsername() + "'";
            throw new ApsSystemException(message, t);
        }
        return map;
    }

    public String getVar() {
        return var;
    }
    public void setVar(String var) {
        this.var = var;
    }

    private String var;

}