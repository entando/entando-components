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
package com.agiletec.plugins.jpcontentfeedback.apsadmin.portal.specialwidget;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.viewer.ContentViewerWidgetAction;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.IContentFeedbackConfig;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.IContentFeedbackManager;
import org.slf4j.Logger;


/**
 * Action per la gestione della configurazione della showlet erogatore contenuto singolo,
 * e del blocco relativo agli elementi del contentFeedback: commenti e rating
 * @author D.Cherchi
 *
 */
public class ContentFeedbackWidgetAction  extends ContentViewerWidgetAction implements IContentFeedbackWidgetAction {

	@Override
	public String init() {
		this.getRequest().getSession().removeAttribute(SESSION_PARAM_STORE_CONFIG);
		return super.init();
	}
	
	@Override
	protected String extractInitConfig() {
		if (null != this.getWidget()) return SUCCESS;
		Widget showlet = this.getCurrentPage().getWidgets()[this.getFrame()];
		Logger log = ApsSystemUtils.getLogger();
		if (null == showlet) {
			try {
				showlet = this.createNewShowlet();
				//for ContentFeedbackWidgetAction
				IContentFeedbackConfig systemConfig = this.getContentFeedbackManager().getConfig();
				String value = systemConfig.getComment();
				if (null != value && value.equalsIgnoreCase("true")) showlet.getConfig().setProperty(WIDGET_PARAM_COMMENT_ACTIVE, value);

				value = systemConfig.getAnonymousComment();
				if (null != value && value.equalsIgnoreCase("true")) showlet.getConfig().setProperty(WIDGET_PARAM_COMMENT_ANONYMOUS, value);

				value = systemConfig.getModeratedComment();
				if (null != value && value.equalsIgnoreCase("true")) showlet.getConfig().setProperty(WIDGET_PARAM_COMMENT_MODERATED, value);

				value = systemConfig.getRateContent();
				if (null != value && value.equalsIgnoreCase("true")) showlet.getConfig().setProperty(WIDGET_PARAM_RATE_CONTENT, value);

				value = systemConfig.getRateComment();
				if (null != value && value.equalsIgnoreCase("true")) showlet.getConfig().setProperty(WIDGET_PARAM_RATE_COMMENT, value);
				//---

			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, NONE);
				//TODO METTI MESSAGGIO DI ERRORE NON PREVISO... Vai in pageTree con messaggio di errore Azione non prevista o cosa del genere
				this.addActionError(this.getText("Message.userNotAllowed"));
				return "pageTree";
			}
			log.info("Configurating new Widget " + this.getShowletTypeCode() + " - Page " + this.getPageCode() + " - Frame " + this.getFrame());
		} else {
			log.info("Edit widget config " + showlet.getType().getCode() + " - Page " + this.getPageCode() + " - Frame " + this.getFrame());
			showlet = this.createCloneFrom(showlet);
		}
		this.setShowlet(showlet);
		return SUCCESS;
	}

	public String storeSessionParams() {
		Map<String, String> sessionParams = new HashMap<String, String>();
		sessionParams.put(WIDGET_PARAM_COMMENT_ACTIVE, this.getUsedComment());
		sessionParams.put(WIDGET_PARAM_COMMENT_MODERATED, this.getCommentValidation());
		sessionParams.put(WIDGET_PARAM_RATE_CONTENT, this.getUsedContentRating());
		sessionParams.put(WIDGET_PARAM_RATE_COMMENT, this.getUsedCommentWithRating());
		sessionParams.put(WIDGET_PARAM_COMMENT_ANONYMOUS, this.getAnonymousComment());

		this.getRequest().getSession().setAttribute(SESSION_PARAM_STORE_CONFIG, sessionParams);
		return SUCCESS;
	}

	public void restoreSessionParams() {
		Map<String, String> sessionParams = (Map<String, String>) this.getRequest().getSession().getAttribute(SESSION_PARAM_STORE_CONFIG);
		if (null != sessionParams) {
			Iterator<String> it = sessionParams.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (null != sessionParams.get(key) && sessionParams.get(key).equalsIgnoreCase("true")) {
					this.getWidget().getConfig().setProperty(key, "true");
				}
			}
		}
		this.getRequest().getSession().removeAttribute(SESSION_PARAM_STORE_CONFIG);
	}


	@Override
	public void validate() {
		super.validate();
		if (this.getFieldErrors().size()==0) {
			try {
				if (this.getContentId()!=null){
					Content publishingContent = this.getContentManager().loadContent(this.getContentId(), true);
					if (null == publishingContent) {
						this.addFieldError("contentId", this.getText("Page.specialShowlet.viewer.nullContent"));
					} else {
						IPage currentPage = this.getCurrentPage();
						String mainGroup = currentPage.getGroup();
						if (!publishingContent.getMainGroup().equals(Group.FREE_GROUP_NAME) && !publishingContent.getGroups().contains(Group.FREE_GROUP_NAME) &&
								!publishingContent.getMainGroup().equals(mainGroup) && !publishingContent.getGroups().contains(mainGroup) &&
								!Group.ADMINS_GROUP_NAME.equals(mainGroup)) {
							this.addFieldError("contentId", this.getText("Page.specialShowlet.viewer.invalidContent"));
						}
					}
				}
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "validate", "Errore in validazione contenuto con id " + this.getContentId());
				throw new RuntimeException("Errore in validazione contenuto con id " + this.getContentId(), t);
			}
		}
		if (this.getFieldErrors().size()>0) {
			try {
				this.createValuedShowlet();
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "validate", "Errore in creazione showlet valorizzata");
				throw new RuntimeException("Errore in creazione showlet valorizzata", t);
			}
		}
	}

	@Override
	public String joinContent() {
		try {
			this.createValuedShowlet();
			this.restoreSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "joinContent");
			throw new RuntimeException("Errore in associazione contenuto", t);
		}
		return SUCCESS;
	}

	/**
	 * Restituisce il contenuto vo in base all'identificativo.
	 * @param contentId L'identificativo del contenuto.
	 * @return Il contenuto vo cercato.
	 */
	public ContentRecordVO getContentVo(String contentId) {
		ContentRecordVO contentVo = null;
		try {
			contentVo = this.getContentManager().loadContentVO(contentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentVo");
			throw new RuntimeException("Errore in caricamento contenuto vo", t);
		}
		return contentVo;
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

	public static final String WIDGET_PARAM_COMMENT_ACTIVE = "usedComment";
	public static final String WIDGET_PARAM_COMMENT_MODERATED = "commentValidation";
	public static final String WIDGET_PARAM_RATE_CONTENT = "usedContentRating";
	public static final String WIDGET_PARAM_RATE_COMMENT = "usedCommentWithRating";
	public static final String WIDGET_PARAM_COMMENT_ANONYMOUS = "anonymousComment";
	public static final String SESSION_PARAM_STORE_CONFIG = "ContentFeedbackShowletAction_params_store";

	/**
	 * @deprecated Use {@link #WIDGET_PARAM_COMMENT_ACTIVE} instead
	 */
	public static final String SHOWLET_PARAM_COMMENT_ACTIVE = WIDGET_PARAM_COMMENT_ACTIVE;

	/**
	 * @deprecated Use {@link #WIDGET_PARAM_COMMENT_MODERATED} instead
	 */
	public static final String SHOWLET_PARAM_COMMENT_MODERATED = WIDGET_PARAM_COMMENT_MODERATED;

	/**
	 * @deprecated Use {@link #WIDGET_PARAM_RATE_CONTENT} instead
	 */
	public static final String SHOWLET_PARAM_RATE_CONTENT = WIDGET_PARAM_RATE_CONTENT;

	/**
	 * @deprecated Use {@link #WIDGET_PARAM_RATE_COMMENT} instead
	 */
	public static final String SHOWLET_PARAM_RATE_COMMENT = WIDGET_PARAM_RATE_COMMENT;


	/**
	 * @deprecated Use {@link #WIDGET_PARAM_COMMENT_ANONYMOUS} instead
	 */
	public static final String SHOWLET_PARAM_COMMENT_ANONYMOUS = WIDGET_PARAM_COMMENT_ANONYMOUS;

}