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
package com.agiletec.plugins.jpmyportalplus.aps.internalservlet;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.PageUserConfigBean;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.WidgetUpdateInfoBean;

import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.IMyPortalPageModelManager;
import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalFrameConfig;

/**
 * @author E.Santoboni
 */
public abstract class AbstractFrontAction extends BaseAction implements IFrontAction, ServletResponseAware {

	@Override
	public String swapFrames() {
		try {
			IPage currentPage = this.getCurrentPage();
			CustomPageConfig config = this.getCustomPageConfig();
			Widget[] customWidgets = (null == config || config.getConfig() == null) ? null : config.getConfig();
			Widget[] widgetsToRender = this.getPageUserConfigManager().getWidgetsToRender(currentPage, customWidgets);
			Widget widgetToMove = widgetsToRender[this.getStartFramePos()];
			Integer statusWidgetToMoveInteger = this.getCustomWidgetStatus() != null ? this.getCustomWidgetStatus()[this.getStartFramePos()] : null;
			int statusWidgetToMove = (statusWidgetToMoveInteger == null) ? 0 : statusWidgetToMoveInteger;
			WidgetUpdateInfoBean frameTargetUpdate =
				new WidgetUpdateInfoBean(this.getTargetFramePos(), widgetToMove, statusWidgetToMove);
			this.addUpdateInfoBean(frameTargetUpdate);
			Widget widgetOnFrameDest = widgetsToRender[this.getTargetFramePos()];
			Integer statusWidgetOnFrameDestInteger = this.getCustomWidgetStatus() != null ? this.getCustomWidgetStatus()[this.getTargetFramePos()] : null;
			int statusWidgetOnFrameDest = (statusWidgetOnFrameDestInteger == null) ? 0 : statusWidgetOnFrameDestInteger;
			WidgetUpdateInfoBean frameStartUpdate = 
					new WidgetUpdateInfoBean(this.getStartFramePos(), widgetOnFrameDest, statusWidgetOnFrameDest);
			this.addUpdateInfoBean(frameStartUpdate);
			this.executeUpdateUserConfig(currentPage);
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "swapFrames", "Error on swapFrames");
			return FAILURE;
		}
		return SUCCESS;
	}

	protected boolean executeResetFrame() throws ApsSystemException {
		try {
			IPage currentPage = this.getCurrentPage();
			WidgetUpdateInfoBean resetFrame =
				new WidgetUpdateInfoBean(this.getFrameToEmpty(), this.getWidgetVoid(), IPageUserConfigManager.STATUS_OPEN);
			this.addUpdateInfoBean(resetFrame);
			this.executeUpdateUserConfig(currentPage);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "executeResetFrame", "Error on executeResetFrame");
			return false;
		}
		return true;
	}

	protected boolean executeCloseFrame() {
		return this.executeResizeFrame(IPageUserConfigManager.STATUS_CLOSE);
	}

	protected boolean executeOpenFrame() {
		return this.executeResizeFrame(IPageUserConfigManager.STATUS_OPEN);
	}

	protected boolean executeResizeFrame(int status) {
		try {
			IPage currentPage = this.getCurrentPage();
			CustomPageConfig config = this.getCustomPageConfig();
			Widget[] customWidgets = (null == config || config.getConfig() == null) ? null : config.getConfig();
			Widget[] widgetsToRender = this.getPageUserConfigManager().getWidgetsToRender(currentPage, customWidgets);
			Widget widget = widgetsToRender[this.getFrameToResize()];
			if (null == widget) return true;
			WidgetUpdateInfoBean resizingFrame =
				new WidgetUpdateInfoBean(this.getFrameToResize(), widget, status);
			this.addUpdateInfoBean(resizingFrame);
			this.executeUpdateUserConfig(currentPage);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "executeResizeFrame", "Error on resize frame");
			return false;
		}
		return true;
	}

	protected void updateSessionParams() throws Throwable {
		WidgetUpdateInfoBean[] infos = this.getUpdateInfos();
		if (null == infos || infos.length == 0) {
			return;
		}
		try {
			//AGGIORNARE SE L'UTENTE CORRENTE è DIVERSO DA GUEST
			//public static final String SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG = "jpmyportalplus_currentCustomUserPageConfig";
			//AGGIORARE SEMPRE
			//public static final String SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG = "jpmyportalplus_currentCustomPageConfig";
			IPage currentPage = this.getCurrentPage();
			UserDetails currentUser = super.getCurrentUser();
			if (null == currentUser) {
				currentUser = this.getUserManager().getGuestUser();
				this.getRequest().getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, currentUser);
			}
			if (!currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
				CustomPageConfig customUserPageConfig = null;
				PageUserConfigBean pageUserConfigBean =
					(PageUserConfigBean) this.getRequest().getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG);
				if (null == pageUserConfigBean) {
					ApsSystemUtils.getLogger().info("No Page User Config by user " + currentUser.getUsername());
					pageUserConfigBean = this.createNewPageUserConfig(infos, currentUser, currentPage);
					this.getRequest().getSession().setAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG, pageUserConfigBean);
					customUserPageConfig = pageUserConfigBean.getConfig().get(currentPage.getCode());
				} else {
					customUserPageConfig = pageUserConfigBean.getConfig().get(currentPage.getCode());
					if (null == customUserPageConfig) {
						customUserPageConfig = this.createNewPageConfig(infos, currentPage);
						pageUserConfigBean.getConfig().put(currentPage.getCode(), customUserPageConfig);
					} else {
						this.updatePageConfig(customUserPageConfig, infos);
					}
				}
				this.getRequest().getSession().setAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG, customUserPageConfig);
			} else {
				CustomPageConfig customGuestPageConfig =
					(CustomPageConfig) this.getRequest().getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG);
				if (null != customGuestPageConfig) {
					this.updatePageConfig(customGuestPageConfig, infos);
				} else {
					customGuestPageConfig = this.createNewPageConfig(infos, currentPage);
					this.getRequest().getSession().setAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG, customGuestPageConfig);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateSessionParams", "Error on updateSessionParams");
			throw new ApsSystemException("Error on updating session params", t);
		}
	}
	
	@Deprecated
	protected Widget getShowletVoid() {
		return this.getWidgetVoid();
	}
	
	protected Widget getWidgetVoid() {
		Widget voidWidget = new Widget();
		voidWidget.setType(this.getPageUserConfigManager().getVoidWidget());
		voidWidget.setConfig(new ApsProperties());
		return voidWidget;
	}
	
	protected boolean executeUpdateUserConfig(IPage currentPage) throws ApsSystemException {
		try {
			UserDetails currentUser = super.getCurrentUser();
			if (currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
				this.getPageUserConfigManager().updateGuestPageConfig(currentPage, this.getUpdateInfos(), this.getRequest(), this.getResponse());
			} else {
				this.getPageUserConfigManager().updateUserPageConfig(currentUser.getUsername(), currentPage, this.getUpdateInfos());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "executeUpdateUserConfig", "Error on executeUpdateUserConfig");
			return false;
		}
		return true;
	}
	
	private PageUserConfigBean createNewPageUserConfig(WidgetUpdateInfoBean[] infos, UserDetails currentUser, IPage currentPage) {
		PageUserConfigBean bean = new PageUserConfigBean(currentUser.getUsername());
		CustomPageConfig pageConfig = this.createNewPageConfig(infos, currentPage);
		bean.getConfig().put(currentPage.getCode(), pageConfig);
		return bean;
	}
	
	private void updatePageConfig(CustomPageConfig customUserPageConfig, WidgetUpdateInfoBean[] infos) {
		for (int i = 0; i < infos.length; i++) {
			WidgetUpdateInfoBean updateInfo = infos[i];
			customUserPageConfig.getConfig()[updateInfo.getFramePos()] = updateInfo.getShowlet();
			customUserPageConfig.getStatus()[updateInfo.getFramePos()] = updateInfo.getStatus();
		}
	}

	private CustomPageConfig createNewPageConfig(WidgetUpdateInfoBean[] infos, IPage currentPage) {
		CustomPageConfig pageConfig = new CustomPageConfig(currentPage.getCode(), currentPage.getModel().getFrames().length);
		this.updatePageConfig(pageConfig, infos);
		return pageConfig;
	}
	
	@Deprecated
	protected Widget[] getCustomShowletConfig() throws Throwable {
		return this.getCustomWidgetConfig();
	}
	
	protected Widget[] getCustomWidgetConfig() throws Throwable {
		Widget[] customWidgets = null;
		try {
			CustomPageConfig customPageConfig = this.getCustomPageConfig();
			if (null != customPageConfig) {
				customWidgets = customPageConfig.getConfig();
			}
		} catch (Throwable t) {
			String message = "Errore in estrazione custom widgets";
			ApsSystemUtils.logThrowable(t, this, "getCustomWidgetConfig", message);
			throw new ApsSystemException(message, t);
		}
		return customWidgets;
	}
	
	@Deprecated
	protected Integer[] getCustomShowletStatus() throws Throwable {
		return this.getCustomWidgetStatus();
	}
	
	protected Integer[] getCustomWidgetStatus() throws Throwable {
		Integer[] customWidgetStatus = null;
		try {
			CustomPageConfig customPageConfig = this.getCustomPageConfig();
			if (null != customPageConfig) {
				customWidgetStatus = customPageConfig.getStatus();
			}
		} catch (Throwable t) {
			String message = "Errore in estrazione custom widget status";
			ApsSystemUtils.logThrowable(t, this, "getCustomWidgetStatus", message);
			throw new ApsSystemException(message, t);
		}
		return customWidgetStatus;
	}
	
	protected CustomPageConfig getCustomPageConfig() {
		IPage currentPage = this.getCurrentPage();
		CustomPageConfig config = (CustomPageConfig) this.getRequest().getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG);
		if (config != null && !config.getPageCode().equals(currentPage.getCode())) {
			ApsSystemUtils.getLogger().error("Current page '" + currentPage
					+ "' not equals then pageCode of custom config param '" + config.getPageCode() + "'");
			return null;
		}
		return config;
	}

	protected IPage getCurrentPage() {
		return this.getPageManager().getPage(this.getCurrentPageCode());
	}

	protected Lang getCurrentSessionLang() {
		return (Lang) this.getRequest().getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_LANG);
	}

	protected void addUpdateInfoBean(WidgetUpdateInfoBean toAdd) {
		WidgetUpdateInfoBean[] infos = this.getUpdateInfos();
		int len = infos.length;
		WidgetUpdateInfoBean[] newInfos = new WidgetUpdateInfoBean[len + 1];
		for (int i = 0; i < len; i++){
			newInfos[i] = infos[i];
		}
		newInfos[len] = toAdd;
		this.setUpdateInfos(newInfos);
	}
	
	protected Map<Integer, MyPortalFrameConfig> getMyPortalModelConfig(String modelCode) {
		return this.getMyPortalPageModelManager().getPageModelConfig(modelCode);
	}

	public String getCurrentPageCode() {
		return _currentPageCode;
	}
	public void setCurrentPageCode(String currentPageCode) {
		this._currentPageCode = currentPageCode;
	}

	public Integer getStartFramePos() {
		return _startFramePos;
	}
	public void setStartFramePos(Integer startFramePos) {
		this._startFramePos = startFramePos;
	}

	public Integer getTargetFramePos() {
		return _targetFramePos;
	}
	public void setTargetFramePos(Integer targetFramePos) {
		this._targetFramePos = targetFramePos;
	}

	public Integer getFrameToEmpty() {
		return _frameToEmpty;
	}
	public void setFrameToEmpty(Integer frameToEmpty) {
		this._frameToEmpty = frameToEmpty;
	}

	public Integer getFrameToResize() {
		return _frameToResize;
	}
	public void setFrameToResize(Integer frameToResize) {
		this._frameToResize = frameToResize;
	}

	protected WidgetUpdateInfoBean[] getUpdateInfos() {
		return _updateInfos;
	}
	protected void setUpdateInfos(WidgetUpdateInfoBean[] updateInfos) {
		this._updateInfos = updateInfos;
	}

	protected HttpServletResponse getResponse() {
		return _servletResponse;
	}
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this._servletResponse = servletResponse;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	protected IPageUserConfigManager getPageUserConfigManager() {
		return _pageUserConfigManager;
	}
	public void setPageUserConfigManager(IPageUserConfigManager pageUserConfigManager) {
		this._pageUserConfigManager = pageUserConfigManager;
	}

	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	protected IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}
	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}
	
	protected IMyPortalPageModelManager getMyPortalPageModelManager() {
		return _myPortalPageModelManager;
	}
	public void setMyPortalPageModelManager(IMyPortalPageModelManager myPortalPageModelManager) {
		this._myPortalPageModelManager = myPortalPageModelManager;
	}
	
	private String _currentPageCode;

	private Integer _startFramePos;
	private Integer _targetFramePos;
	private Integer _frameToEmpty;
	private Integer _frameToResize;

	private WidgetUpdateInfoBean[] _updateInfos = new WidgetUpdateInfoBean[0];
	
	private HttpServletResponse _servletResponse;
	
	private IPageManager _pageManager;
	private IPageUserConfigManager _pageUserConfigManager;
	private IUserManager _userManager;
	private IWidgetTypeManager _widgetTypeManager;
	private IMyPortalPageModelManager _myPortalPageModelManager;
	
}