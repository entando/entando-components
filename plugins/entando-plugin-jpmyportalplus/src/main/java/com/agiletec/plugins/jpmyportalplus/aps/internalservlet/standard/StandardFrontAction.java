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
package com.agiletec.plugins.jpmyportalplus.aps.internalservlet.standard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.Frame;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpmyportalplus.aps.internalservlet.AbstractFrontAction;
import com.agiletec.plugins.jpmyportalplus.aps.internalservlet.IFrontAction;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.WidgetUpdateInfoBean;

import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalFrameConfig;

/**
 * @author E.Santoboni
 */
public class StandardFrontAction extends AbstractFrontAction implements IFrontAction {

	@Override
	public String swapFrames() {
		//System.out.println("Partenza " + this.getStartFramePos() +
		//		" - ARRIVO  " + this.getTargetFramePos());
		try {
			Widget[] customWidgets = super.getCustomWidgetConfig();
			IPage currentPage = this.getCurrentPage();
			Widget[] widgetsToRender = this.getPageUserConfigManager().getShowletsToRender(currentPage, customWidgets);
			
			Widget movedWidget = widgetsToRender[this.getStartFramePos()];
			Integer movedWidgetStatusInteger = super.getCustomShowletStatus() != null ? super.getCustomShowletStatus()[this.getStartFramePos()] : null;
			int movedWidgetStatus = (movedWidgetStatusInteger == null) ? 0 : movedWidgetStatusInteger;
			WidgetUpdateInfoBean movedWidgetUpdateInfo = new WidgetUpdateInfoBean(this.getTargetFramePos(), movedWidget, movedWidgetStatus);
			this.addUpdateInfoBean(movedWidgetUpdateInfo);

			Widget widgetToMove = widgetsToRender[this.getTargetFramePos()];
			Integer widgetToMoveStatusInteger = super.getCustomShowletStatus() != null ? super.getCustomShowletStatus()[this.getTargetFramePos()] : null;
			int widgetToMoveStatus = (widgetToMoveStatusInteger == null) ? 0 : widgetToMoveStatusInteger;
			WidgetUpdateInfoBean widgetToMoveUpdateInfo = new WidgetUpdateInfoBean(this.getStartFramePos(), widgetToMove, widgetToMoveStatus);
			this.addUpdateInfoBean(widgetToMoveUpdateInfo);

			super.executeUpdateUserConfig(currentPage);
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "swapFrames", "Error on swapFrame");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String removeFrame() {
		try {
			this.executeResetFrame();
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeFrame", "Error on removeFrame");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String addWidgets() {
		try {
			Widget[] customWidgets = super.getCustomWidgetConfig();
			IPage currentPage = this.getCurrentPage();
			Widget[] widgetsToRender = this.getPageUserConfigManager().getWidgetsToRender(currentPage, customWidgets);

			//for (int i = 0; i < this.getShowletToShow().size(); i++) {
			//	System.out.println("DA MOSTRARE " + this.getShowletToShow().get(i));
			//}
			List<Integer> framesToFlow = this.getFramesToFlow(widgetsToRender, currentPage);
			//for (int i = 0; i < framesToFlow.size(); i++) {
			//	System.out.println("DA ELIMINARE - " + framesToFlow.get(i));
			//}
			List<String> widgetsToAdd = this.getShowletsToAdd(widgetsToRender, currentPage);
			//for (int i = 0; i < showletsToAdd.size(); i++) {
			//	System.out.println("DA AGGIUNGERE + " + showletsToAdd.get(i));
			//}
			PageModel pageModel = currentPage.getModel();
			Map<Integer, MyPortalFrameConfig> myPortalModel = super.getMyPortalModelConfig(pageModel.getCode());
			String voidWidgetCode = this.getPageUserConfigManager().getVoidShowlet().getCode();
			Frame[] frames = pageModel.getConfiguration();
			for (int i = 0; i < frames.length; i++) {
				//Frame frame = frames[i];
				MyPortalFrameConfig frameConfig = (null != myPortalModel) ? myPortalModel.get(i) : null;
				if (null != frameConfig && !frameConfig.isLocked()) {
					boolean isFrameToFlow = framesToFlow.contains(i);
					if (isFrameToFlow) {
						if (widgetsToAdd.size()>0) {
							this.addNewWidgetUpdateInfo(widgetsToAdd, i, isFrameToFlow);
						} else {
							Widget showletToInsert = this.getWidgetVoid();
							WidgetUpdateInfoBean infoBean = new WidgetUpdateInfoBean(i, showletToInsert, IPageUserConfigManager.STATUS_OPEN);
							this.addUpdateInfoBean(infoBean);
						}
					} else {
						Widget showlet = widgetsToRender[i];
						if ((null == showlet || (showlet.getType().getCode().equals(voidWidgetCode))) && widgetsToAdd.size()>0) {
							this.addNewWidgetUpdateInfo(widgetsToAdd, i, isFrameToFlow);
						}
					}
				}
			}
			this.executeUpdateUserConfig(currentPage);
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addWidgets", "Error on addWidgets");
			return FAILURE;
		}
		return SUCCESS;
	}

	protected List<Integer> getFramesToFlow(Widget[] widgetsToRender, IPage currentPage) throws Throwable {
		List<Integer> framesToFlow = new ArrayList<Integer>();
		try {
			PageModel pageModel = currentPage.getModel();
			Map<Integer, MyPortalFrameConfig> myPortalModel = super.getMyPortalModelConfig(pageModel.getCode());
			String voidWidgetCode = this.getPageUserConfigManager().getVoidWidget().getCode();
			Frame[] frames = pageModel.getConfiguration();
			for (int i = 0; i < frames.length; i++) {
				//Frame frame = frames[i];
				MyPortalFrameConfig frameConfig = (null != myPortalModel) ? myPortalModel.get(i) : null;
				if (null != frameConfig && !frameConfig.isLocked()) {
				//if (!frame.isLocked()) {
					Widget widget = widgetsToRender[i];
					if (null != widget && !widget.getType().getCode().equals(voidWidgetCode) &&
							(null == this.getWidgetToShow() || !this.getWidgetToShow().contains(widget.getType().getCode()))) {
						framesToFlow.add(i);
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getFramesToFlow", "Error on extracting frames to flow");
			throw new ApsSystemException("Error on extracting frames to flow", t);
		}
		return framesToFlow;
	}
	
	@Deprecated
	protected List<String> getShowletsToAdd(Widget[] showletsToRender, IPage currentPage) throws Throwable {
		return this.getWidgetsToAdd(showletsToRender, currentPage);
	}
	
	protected List<String> getWidgetsToAdd(Widget[] widgetsToRender, IPage currentPage) throws Throwable {
		Set<String> widgetsToAdd = new HashSet<String>();
		try {
			if (null != this.getWidgetToShow()) {
				widgetsToAdd.addAll(this.getWidgetToShow());
			}
			PageModel pageModel = currentPage.getModel();
			Map<Integer, MyPortalFrameConfig> myPortalModel = super.getMyPortalModelConfig(pageModel.getCode());
			Frame[] frames = pageModel.getConfiguration();
			for (int i = 0; i < frames.length; i++) {
				//Frame frame = frames[i];
				MyPortalFrameConfig frameConfig = (null != myPortalModel) ? myPortalModel.get(i) : null;
				if (null != frameConfig && !frameConfig.isLocked()) {
				//if (!frame.isLocked()) {
					Widget widget = widgetsToRender[i];
					if (null != widget) {
						widgetsToAdd.remove(widget.getType().getCode());
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getWidgetsToAdd", "Error on extracting widget to add");
			throw new ApsSystemException("Error on extracting widget to add", t);
		}
		List<String> codes = new ArrayList<String>(widgetsToAdd);
		Collections.sort(codes);
		return codes;
	}

	protected void addNewWidgetUpdateInfo(List<String> widgetsToAdd, int framePos, boolean frameToFlow) {
		WidgetUpdateInfoBean infoBean = null;
		Widget widgetToInsert = null;
		String typeCode = widgetsToAdd.get(0);
		WidgetType type = this.getWidgetTypeManager().getWidgetType(typeCode);
		if (null != type) {
			widgetsToAdd.remove(typeCode);
			widgetToInsert = new Widget();
			widgetToInsert.setType(type);
		}
		if (null != widgetToInsert) {
			infoBean = new WidgetUpdateInfoBean(framePos, widgetToInsert, IPageUserConfigManager.STATUS_OPEN);
			this.addUpdateInfoBean(infoBean);
		} else if (frameToFlow) {
			infoBean = new WidgetUpdateInfoBean(framePos, this.getWidgetVoid(), IPageUserConfigManager.STATUS_OPEN);
			this.addUpdateInfoBean(infoBean);
		}
	}

	@Override
	public String resetFrames() {
		try {
			IPage currentPage = this.getCurrentPage();
			UserDetails currentUser = super.getCurrentUser();
			if (currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
				this.getPageUserConfigManager().removeGuestPageConfig(currentPage, this.getRequest(), this.getResponse());
			} else {
				this.getPageUserConfigManager().removeUserPageConfig(currentUser.getUsername(), currentPage);
			}
			this.getRequest().getSession().removeAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG);
			this.getRequest().getSession().removeAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "resetFrames", "Error on resetFrames");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String closeFrame() {
		//System.out.println("Frame da Chiudere " + this.getFrameToResize());
		try {
			this.executeCloseFrame();
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "closeFrame", "Error on closeFrame");
		}
		return SUCCESS;
	}

	@Override
	public String openFrame() {
		//System.out.println("Frame da Aprire " + this.getFrameToResize());
		try {
			this.executeOpenFrame();
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "openFrame", "Error on openFrame");
		}
		return SUCCESS;
	}

	public String getDestForwardPath() {
		String pathDest = null;
		try {
			Lang currentLanguage = this.getCurrentSessionLang();
			if (null == currentLanguage) {
				currentLanguage = this.getLangManager().getDefaultLang();
			}
			IPage currentPage = this.getCurrentPage();
			String pathDestFirst = this.getUrlManager().createUrl(currentPage, currentLanguage, null);
			pathDest = this.getResponse().encodeURL(pathDestFirst);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getDestForwardPath", "Error on extracting destination forward Path");
			throw new RuntimeException("Error on extracting destination forward Path", t);
		}
		return pathDest;
	}

	@Override
	public String openSwapSection() {
		throw new RuntimeException("ACTION NOT SUPPORTED - openSwapSection");
	}
	
	@Deprecated
	public List<String> getShowletToShow() {
		return this.getWidgetToShow();
	}
	@Deprecated
	public void setShowletToShow(List<String> showletToShow) {
		this.setWidgetToShow(showletToShow);
	}
	
	public List<String> getWidgetToShow() {
		return _widgetToShow;
	}
	public void setWidgetToShow(List<String> widgetToShow) {
		this._widgetToShow = widgetToShow;
	}
	
	protected IURLManager getUrlManager() {
		return _urlManager;
	}
	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}
	
	private List<String> _widgetToShow;

	private IURLManager _urlManager;

}
