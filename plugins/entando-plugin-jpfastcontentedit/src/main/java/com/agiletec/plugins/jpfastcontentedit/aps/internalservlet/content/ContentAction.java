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
package com.agiletec.plugins.jpfastcontentedit.aps.internalservlet.content;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.notify.NotifyManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.url.IURLManager;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpfastcontentedit.aps.internalservlet.content.helper.IContentActionHelper;
import com.agiletec.plugins.jpfastcontentedit.aps.system.JpFastContentEditSystemConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe action delegata alla redazione del contenuto in Front-End.
 * La classe estende la classe action base della gestione contenuti con l'aggiunta della
 * funzione per la redirezione alla pagina di partenza.
 * @author E.Santoboni
 */
public class ContentAction extends com.agiletec.plugins.jacms.apsadmin.content.ContentAction implements ServletResponseAware {
	
	private static final Logger _logger = LoggerFactory.getLogger(ContentAction.class);
	
	@Override
	public String edit() {
		String result = super.edit();
		if (SUCCESS.equals(result)) {
			IContentActionHelper helper = (IContentActionHelper) this.getContentActionHelper();
			helper.checkTypeLabels(this.getContent());
			this.disableAttributes(this.getContent());
		} else {
			ApsSystemUtils.getLogger().error("Error on super.edit - result " + result);
		}
		return result;
	}

	private void disableAttributes(Content content) {
		content.disableAttributes(JpFastContentEditSystemConstants.CONTENT_DISABLING_CODE);
	}

	@Override
	protected String saveContent(boolean approve) {
		try {
			String contentId = this.getContent().getId();
			String result = super.saveContent(approve);
			if (!result.equals(SUCCESS)) {
				return result;
			}
			this.waitNotifyingThread();
			this.getResponse().sendRedirect(this.getDestForwardPath(contentId));
		} catch (Throwable t) {
			String message = "Errore in forward a pagina iniziale '" + this.getDestForwardPath() + "'";
			ApsSystemUtils.logThrowable(t, this, "saveContent", message);
			throw new RuntimeException(message, t);
		}
		return null;
	}
	
	/**
	 * Delete a content created from current user.
	 * @return The result of the action.
	 */
	public String delete() {
		Logger log = ApsSystemUtils.getLogger();
		try {
			String contentId = this.getContentId();
			Content contentToSuspend = this.getContentManager().loadContent(contentId, false);
			if (null == contentToSuspend) {
				log.info("fastContentEdit: contenuto da eliminare nullo");
				return FAILURE;
			}
			if (!this.isUserAllowed(contentToSuspend)) {
				log.info("fastContentEdit: utente non abilitato all'eliminazione del contenuto " + contentId);
				return "userNotAllowed";
			}
			Map references = this.getContentActionHelper().getReferencingObjects(contentToSuspend, this.getRequest());
			if (references.size()>0) {
				this.addActionError(this.getText("Errors.fastContentEdit.referencedContent"));
				log.info("fastContentEdit: il contenuto da eliminare " + contentId + " presenta delle referenze");
				return "userNotAllowed";
			}
			Content content = this.getContentManager().loadContent(contentId, true);
			this.getContentManager().removeOnLineContent(content);
			this.getContentManager().deleteContent(content);
			this.waitNotifyingThread();
			this.getResponse().sendRedirect(this.getDestForwardPath());
		} catch (Throwable t) {
			String message = "Errore in forward a pagina iniziale '" + this.getDestForwardPath() + "'";
			ApsSystemUtils.logThrowable(t, this, "deleteContent", message);
			return FAILURE;
		}
		return null;
	}

	/**
	 * Attende che il thread in esecuzione riguardante la notifica di eventi terminino.
	 * @throws InterruptedException In caso di errore.
	 */
	protected void waitNotifyingThread() throws InterruptedException {
		Thread[] threads = new Thread[100];
	    Thread.enumerate(threads);
	    for (int i=0; i<threads.length; i++) {
	    	Thread currentThread = threads[i];
	    	if (currentThread == null) {
	    		break;
	    	} else if (currentThread.getName().startsWith(NotifyManager.NOTIFYING_THREAD_NAME)) {
	    		currentThread.join();
	    	}
	    }
	}

	/**
	 * Costruisce e restituisce il path di redirezione finale.
	 * Il path rappresenta la destinazione alla quale si deve essere redirezionati una volta
	 * terminata la redazione del contenuto.
	 * @return Il path di destinazione finale.
	 */
	protected String getDestForwardPath() {
		return this.getDestForwardPath(null);
	}

	/**
	 * Costruisce e restituisce il path di redirezione finale.
	 * Il path rappresenta la destinazione alla quale si deve essere redirezionati una volta
	 * terminata la redazione del contenuto.
	 * @param contentId The Content id.
	 * @return Il path di destinazione finale.
	 */
	protected String getDestForwardPath(String contentId) {
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		String url = null;
		try {
			Lang currentLang = null;
			if (null != reqCtx) {
				currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			} else {
				currentLang = this.getLangManager().getDefaultLang();
			}
			IPage pageDestCode = null;
			String pageDestCodeString = (String) this.getRequest().getSession().getAttribute(JpFastContentEditSystemConstants.FINAL_DEST_PAGE_PARAM_NAME);
			if (null == pageDestCodeString || null == (pageDestCode = this.getPageManager().getPage(pageDestCodeString))) {
				pageDestCode = this.getPageManager().getRoot();
			}
			this.getRequest().getSession().removeAttribute(JpFastContentEditSystemConstants.FINAL_DEST_PAGE_PARAM_NAME);
			Map<String, String> params = null;
			if (null != contentId) {
				params = new HashMap<String, String>();
				params.put(SystemConstants.K_CONTENT_ID_PARAM, contentId);
			}
			url = this.getUrlManager().createUrl(pageDestCode, currentLang, params, true, this.getRequest());
		} catch (Throwable t) {
			_logger.error("Error creating url destination", t);
			throw new RuntimeException("Error creating url destination", t);
		}
		return url;
	}
	
	@Override
	protected boolean isUserAllowed(Content content) {
		boolean userAllowed = super.isUserAllowed(content);
		if (userAllowed) {
			String typeCode = this.getContentTypeCode();
			if (typeCode == null || typeCode.length() == 0 || content.getTypeCode().equals(typeCode)) {
				String username = this.getCurrentUser().getUsername();
				IContentActionHelper helper = (IContentActionHelper) this.getContentActionHelper();
				String author = helper.getAuthor(content, this.getRequest());
				return (null != author && username.equals(author)) || username.equals(content.getLastEditor());
			}
		}
		return false;
	}

	public String getContentTypeCode() {
		String contentTypeCode = this._contentTypeCode;
		if (null == contentTypeCode) {
			IContentActionHelper helper = (IContentActionHelper) this.getContentActionHelper();
			contentTypeCode = helper.extractShowletParam(this.getRequest(), JpFastContentEditSystemConstants.TYPE_CODE_SHOWLET_PARAM_NAME);
			this._contentTypeCode = contentTypeCode;
		}
		return contentTypeCode;
	}

	public void setContentTypeCode(String contentTypeCode) {
		IContentActionHelper helper = (IContentActionHelper) this.getContentActionHelper();
		String showletTypeCode = helper.extractShowletParam(this.getRequest(), JpFastContentEditSystemConstants.TYPE_CODE_SHOWLET_PARAM_NAME);
		contentTypeCode = (null==showletTypeCode) ? contentTypeCode : showletTypeCode;
		this._contentTypeCode = contentTypeCode;
	}

	protected HttpServletResponse getResponse() {
		return _response;
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this._response = response;
	}

	protected IURLManager getUrlManager() {
		return _urlManager;
	}
	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}

	private String _contentTypeCode;

	private HttpServletResponse _response;
	private IURLManager _urlManager;

}