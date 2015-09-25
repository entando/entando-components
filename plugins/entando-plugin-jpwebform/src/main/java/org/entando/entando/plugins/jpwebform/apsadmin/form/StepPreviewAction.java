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
package org.entando.entando.plugins.jpwebform.apsadmin.form;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IGuiGeneratorManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepGuiConfig;
import org.entando.entando.plugins.jpwebform.apsadmin.AbstractConfigAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;

/**
 * @author S.Loru
 */
public class StepPreviewAction extends AbstractConfigAction implements ServletResponseAware, SessionAware {

	private static final Logger _logger =  LoggerFactory.getLogger(StepPreviewAction.class);

	public String preview() {
		try {
			StepGuiConfig stepGuiConfig = new StepGuiConfig();
			stepGuiConfig.setFormTypeCode(this.getEntityTypeCode());
			stepGuiConfig.setStepCode(this.getStepCode());
			stepGuiConfig.setUserCss(this.getUserCss());
			stepGuiConfig.setUserGui(this.getUserGui());
			this.setVersionType(0);
			this.getGuiGeneratorManager().generatePreview(stepGuiConfig);
			String previewPageCode = this.getRequest().getParameter(PAGE_CODE_PARAM_PREFIX);

			if(this.getRootStepCode() == null){
				this.setRootStepCode(this.getStepCode());
			}
			this.setPreviewPageCode(previewPageCode);
		} catch (Throwable t) {
			_logger.error("error in preview", t);
			releaseSession();
			return FAILURE;
		}
		return SUCCESS;
	}

	public String previewOnPublish() {
		try {
			if(!this.getFormManager().checkStepGui(this.getEntityTypeCode())){
				return FAILURE;
			}
			this.setVersionType(0);
			this.getGuiGeneratorManager().generatePreview(this.getEntityTypeCode());
			String previewPageCode = this.getRequest().getParameter(PAGE_CODE_PARAM_PREFIX);
			if(this.getRootStepCode() == null){
				this.setRootStepCode(this.getStepCode());
			}
			this.setPreviewPageCode(previewPageCode);
		} catch (Throwable t) {
			_logger.error("error in previewOnPublish", t);
			releaseSession();
			return FAILURE;
		}
		return SUCCESS;
	}


	public String executePreview() {
		try {
			if(!this.getFormManager().checkStepGui(this.getEntityTypeCode())){
				return FAILURE;
			}
			String pageDestCode = this.getCheckPageDestinationCode();
			if (null == pageDestCode) return INPUT;
			this.prepareForwardParams(pageDestCode);
			this.getRequest().setCharacterEncoding("UTF-8");
		} catch (Throwable t) {
			String message = "Error";
			_logger.error("error in executePreview", t);
			releaseSession();
			throw new RuntimeException(message, t);
		}
		return SUCCESS;
	}

	public String showPreview(){
		Step nextStep = this.getStepsConfig().getNextStep(this.getStepCode());
		this.setNextStepCode((nextStep!=null)?nextStep.getCode():"");
		Step previousStep = this.getStepsConfig().getPreviousStep(this.getStepCode());
		this.setPrevStepCode((previousStep!=null)?previousStep.getCode():"");
		return SUCCESS;
	}

	protected String getCheckPageDestinationCode() {
		IPageManager pageManager = this.getPageManager();
		String pageDestCode = this.getPreviewPageCode();
		if (null == pageDestCode || pageDestCode.trim().length() == 0) {
			if (null == pageDestCode || null == pageManager.getPage(pageDestCode)) {
				String[] args = {pageDestCode};
				this.addFieldError("previewPageCode", this.getText("error.content.preview.pageNotValid", args));
				return null;
			}
		}
		if (null == pageManager.getPage(pageDestCode)) {
			String[] args = {pageDestCode};
			this.addFieldError("previewPageCode", this.getText("error.content.preview.pageNotFound", args));
			releaseSession();
			return null;
		}
		return pageDestCode;
	}

	private void prepareForwardParams(String pageDestCode) {
		HttpServletRequest request = this.getRequest();
		RequestContext reqCtx = new RequestContext();
		reqCtx.setRequest(request);
		reqCtx.setResponse(this.getServletResponse());
		Lang currentLang = this.getLangManager().getLang(this.getPreviewLangCode());
		if (null == currentLang) {
			currentLang = this.getLangManager().getDefaultLang();
		}
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, currentLang);
		IPageManager pageManager = this.getPageManager();
		IPage pageDest = null;
		if(pageDestCode != null || pageDestCode.isEmpty()){
			pageDest = pageManager.getPage(pageDestCode);
		} else {
			//TODO ottenere una nuova pagina
			pageDest = new Page();
		}
		reqCtx.addExtraParam(JpwebformSystemConstants.PREVIEW_PARAM_TYPE, this.getEntityTypeCode());
		reqCtx.addExtraParam(JpwebformSystemConstants.PREVIEW_PARAM_STEP, this.getStepCode());
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE, pageDest);
		request.setAttribute(RequestContext.REQCTX, reqCtx);
	}

	private void releaseSession(){
		this.getSession().remove(JpwebformSystemConstants.PREVIEW_USER_GUI);
		this.getSession().remove(JpwebformSystemConstants.PREVIEW_CSS_GUI);
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this._response = response;
	}
	public HttpServletResponse getServletResponse() {
		return _response;
	}

	public String getPreviewPageCode() {
		return _previewPageCode;
	}
	public void setPreviewPageCode(String previewPageCode) {
		this._previewPageCode = previewPageCode;
	}

	public String getPreviewLangCode() {
		return _previewLangCode;
	}
	public void setPreviewLangCode(String previewLangCode) {
		this._previewLangCode = previewLangCode;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}


	public String getUserCss() {
		return (String) this.getSession().get(JpwebformSystemConstants.PREVIEW_CSS_GUI);
	}
	public void setUserCss(String userCss) {
		this.getSession().put(JpwebformSystemConstants.PREVIEW_CSS_GUI, userCss);
	}

	public String getUserGui() {
		return (String) this.getSession().get(JpwebformSystemConstants.PREVIEW_USER_GUI);
	}
	public void setUserGui(String userGui) {
		this.getSession().put(JpwebformSystemConstants.PREVIEW_USER_GUI, userGui);
	}

	public IGuiGeneratorManager getGuiGeneratorManager() {
		return _guiGeneratorManager;
	}

	public void setGuiGeneratorManager(IGuiGeneratorManager guiGeneratorManager) {
		this._guiGeneratorManager = guiGeneratorManager;
	}

	public int getVersionType() {
		return _versionType;
	}

	public void setVersionType(int versionType) {
		this._versionType = versionType;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this._session = session;
	}

	public Map<String, Object> getSession(){
		return _session;
	}

	public String getNextStepCode() {
		return _nextStepCode;
	}

	public void setNextStepCode(String nextStepCode) {
		this._nextStepCode = nextStepCode;
	}

	public String getPrevStepCode() {
		return _prevStepCode;
	}

	public void setPrevStepCode(String prevStepCode) {
		this._prevStepCode = prevStepCode;
	}

	public String getRootStepCode() {
		return _rootStepCode;
	}

	public void setRootStepCode(String rootStepCode) {
		this._rootStepCode = rootStepCode;
	}

	public String getActionName(){
		if((null == this.getUserGui() || this.getUserGui().isEmpty()) && (null == this.getUserCss() || this.getUserCss().isEmpty())){
			return "executePreviewOnPublish";
		} else {
			return "executePreview";
		}
	}

	private int _versionType;
	private IGuiGeneratorManager _guiGeneratorManager;
	private String _userGui;
	private String _userCss;

	private HttpServletResponse _response;

	private String _previewPageCode;
	private String _previewLangCode;

	private IPageManager _pageManager;

	private Map<String, Object> _session;

	private static String PAGE_CODE_PARAM_PREFIX = "jpwebformStepPreviewActionPageCode";

	private String _nextStepCode;
	private String _prevStepCode;

	private String _rootStepCode;


}
