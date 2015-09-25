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
package com.agiletec.plugins.jpblog.apsadmin.portal.specialwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListWidgetHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.UserFilterOptionBean;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.ContentListViewerWidgetAction;
import com.agiletec.plugins.jpblog.aps.system.JpblogSystemConstants;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.IContentFeedbackConfig;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.IContentFeedbackManager;
import com.agiletec.plugins.jpcontentfeedback.apsadmin.portal.specialwidget.ContentFeedbackWidgetAction;

public class BlogListViewerWidgetAction extends ContentListViewerWidgetAction {

	private static final Logger _logger =  LoggerFactory.getLogger(BlogListViewerWidgetAction.class);

	@Override
	public String init() {
		this.getRequest().getSession().removeAttribute(ContentFeedbackWidgetAction.SESSION_PARAM_STORE_CONFIG);
		return super.init();
	}

	@Override
	public String configContentType() {
		String result = super.configContentType();
		if (result.equalsIgnoreCase(SUCCESS)) {
			//for ContentFeedbackWidgetAction
			IContentFeedbackConfig systemConfig = this.getContentFeedbackManager().getConfig();
			String value = systemConfig.getComment();
			if (null != value && value.equalsIgnoreCase("true")) this.getWidget().getConfig().setProperty(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_ACTIVE, value);

			value = systemConfig.getAnonymousComment();
			if (null != value && value.equalsIgnoreCase("true")) this.getWidget().getConfig().setProperty(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_ANONYMOUS, value);

			value = systemConfig.getModeratedComment();
			if (null != value && value.equalsIgnoreCase("true")) this.getWidget().getConfig().setProperty(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_MODERATED, value);

			value = systemConfig.getRateContent();
			if (null != value && value.equalsIgnoreCase("true")) this.getWidget().getConfig().setProperty(ContentFeedbackWidgetAction.WIDGET_PARAM_RATE_CONTENT, value);

			value = systemConfig.getRateComment();
			if (null != value && value.equalsIgnoreCase("true")) this.getWidget().getConfig().setProperty(ContentFeedbackWidgetAction.WIDGET_PARAM_RATE_COMMENT, value);
			//---
		}
		return result;
	}


	protected String extractInitConfig() {
		if (null != this.getWidget()) return SUCCESS;
		Widget showlet = this.getCurrentPage().getWidgets()[this.getFrame()];
		if (null == showlet) {
			try {
				showlet = this.createNewShowlet();
			} catch (Exception e) {
				_logger.error("error in createNewShowlet", e);
				//TODO METTI MESSAGGIO DI ERRORE NON PREVISO... Vai in pageTree con messaggio di errore Azione non prevista o cosa del genere
				this.addActionError(this.getText("Message.userNotAllowed"));
				return "pageTree";
			}
			_logger.debug("Configurating new Widget {} - Page {} - Frame {}", this.getWidgetTypeCode(), this.getPageCode(), this.getFrame());
		} else {
			_logger.debug("Edit Widget config {} - Page {} - Frame {}",showlet.getType().getCode(), this.getPageCode(),this.getFrame());
			showlet = this.createCloneFrom(showlet);
		}
		this.setShowlet(showlet);
		return SUCCESS;
	}

	@Override
	public String addFilter() {
		String result =  super.addFilter();
		if (null != this.getWidget()) {
			this.restoreSessionParams();
		}
		return result;
	}

	/**
	 * No categories for blog user filter options
	 */
	public List<SelectItem> getAllowedUserFilterTypes() throws ApsSystemException {
		List<SelectItem> types = new ArrayList<SelectItem>();
		try {
			types.add(new SelectItem(UserFilterOptionBean.KEY_FULLTEXT, this.getText("label.fulltext")));
			//types.add(new SelectItem(UserFilterOptionBean.KEY_CATEGORY, this.getText("label.category")));
			String contentType = this.getWidget().getConfig().getProperty(IContentListWidgetHelper.WIDGET_PARAM_CONTENT_TYPE);
			Content prototype = this.getContentManager().createContentType(contentType);
			List<AttributeInterface> contentAttributes = prototype.getAttributeList();
			for (int i=0; i<contentAttributes.size(); i++) {
				AttributeInterface attribute = contentAttributes.get(i);
				if (attribute.isSearcheable()) {
					types.add(new SelectItem(UserFilterOptionBean.TYPE_ATTRIBUTE + "_" + attribute.getName(), this.getText("label.attribute", new String[]{attribute.getName()})));
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting allowed user filter types", t);
			throw new ApsSystemException("Error extracting allowed user filter types", t);
		}
		return types;
	}

	/**
	 * Restituisce la lista di contenuti (in forma small) definiti nel sistema.
	 * Il metodo è a servizio delle jsp che richiedono questo dato per fornire
	 * una corretta visualizzazione della pagina.
	 * @return La lista di tipi di contenuto (in forma small) definiti nel sistema.
	 */
	public List<SmallContentType> getContentTypes() {
		List<SmallContentType> types = new ArrayList<SmallContentType>();
		List<SmallContentType> systemTypes = this.getContentManager().getSmallContentTypes();
		
		if (null != systemTypes) {
			for (int i = 0; i < systemTypes.size(); i++) {
				SmallContentType current = systemTypes.get(i);
				Content content = this.getContentManager().createContentType(current.getCode());
				boolean hasAuthor = null != content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_AUTHOR);
				boolean hasBody = null != content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_BODY);
				boolean hasDate= null != content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_DATE);
				boolean hasTitle = null != content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_TITLE);
				if (hasAuthor && hasBody && hasDate && hasTitle) {
					types.add(current);
				}
			}
		}
		return types;
	}
	
	public String storeSessionParams() {
		Map<String, String> sessionParams = new HashMap<String, String>();
		sessionParams.put(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_ACTIVE, this.getUsedComment());
		sessionParams.put(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_MODERATED, this.getCommentValidation());
		sessionParams.put(ContentFeedbackWidgetAction.WIDGET_PARAM_RATE_CONTENT, this.getUsedContentRating());
		sessionParams.put(ContentFeedbackWidgetAction.WIDGET_PARAM_RATE_COMMENT, this.getUsedCommentWithRating());
		sessionParams.put(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_ANONYMOUS, this.getAnonymousComment());

		this.getRequest().getSession().setAttribute(ContentFeedbackWidgetAction.SESSION_PARAM_STORE_CONFIG, sessionParams);
		return SUCCESS;
	}

	public void restoreSessionParams() {
		Map<String, String> sessionParams = (Map<String, String>) this.getRequest().getSession().getAttribute(ContentFeedbackWidgetAction.SESSION_PARAM_STORE_CONFIG);
		if (null != sessionParams) {
			Iterator<String> it = sessionParams.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (null != sessionParams.get(key) && sessionParams.get(key).equalsIgnoreCase("true")) {
					this.getWidget().getConfig().setProperty(key, "true");
				}
			}
		}
		this.getRequest().getSession().removeAttribute(ContentFeedbackWidgetAction.SESSION_PARAM_STORE_CONFIG);
	}

	public String getUsedComment() {
		return _usedComment;
	}
	public void setUsedComment(String usedComment) {
		this._usedComment = usedComment;
	}

	public String getAnonymousComment() {
		return _anonymousComment;
	}
	public void setAnonymousComment(String anonymousComment) {
		this._anonymousComment = anonymousComment;
	}

	public String getCommentValidation() {
		return _commentValidation;
	}
	public void setCommentValidation(String commentValidation) {
		this._commentValidation = commentValidation;
	}

	public String getUsedContentRating() {
		return _usedContentRating;
	}
	public void setUsedContentRating(String usedContentRating) {
		this._usedContentRating = usedContentRating;
	}

	public String getUsedCommentWithRating() {
		return _usedCommentWithRating;
	}
	public void setUsedCommentWithRating(String usedCommentWithRating) {
		this._usedCommentWithRating = usedCommentWithRating;
	}

	protected IContentFeedbackManager getContentFeedbackManager() {
		return _contentFeedbackManager;
	}
	public void setContentFeedbackManager(IContentFeedbackManager contentFeedbackManager) {
		this._contentFeedbackManager = contentFeedbackManager;
	}

	private IContentFeedbackManager _contentFeedbackManager;

	private String _usedComment;
	private String _anonymousComment;
	private String _commentValidation;
	private String _usedContentRating;
	private String _usedCommentWithRating;
}
