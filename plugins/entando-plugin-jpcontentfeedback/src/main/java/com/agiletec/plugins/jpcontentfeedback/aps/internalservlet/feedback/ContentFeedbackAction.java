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
package com.agiletec.plugins.jpcontentfeedback.aps.internalservlet.feedback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.apsadmin.content.helper.ContentActionHelper;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.CheckVotingUtil;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.IContentFeedbackManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.ICommentManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.Comment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.CommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.IComment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.IRatingManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRating;
import com.agiletec.plugins.jpcontentfeedback.aps.tags.FeedbackIntroTag;
import com.agiletec.plugins.jpcontentfeedback.apsadmin.feedback.AbstractContentFeedbackAction;
import com.agiletec.plugins.jpcontentfeedback.apsadmin.portal.specialwidget.ContentFeedbackWidgetAction;

/**
 * @author D.Cherchi
 */
public class ContentFeedbackAction extends AbstractContentFeedbackAction implements IContentFeedbackAction, ServletResponseAware, ServletRequestAware {


	public String getRedirectParams() {
		String params = null;
		try {
			StringBuffer result = new StringBuffer();
				Iterator<String> it = super.getRequest().getParameterMap().keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					String value = null;
					if (null != key && key.startsWith("entaredir_")) {
						value = super.getParameter(key);
						if (null != value && value.trim().length() > 0) {
							if (result.length() == 0) {
								result.append("&");
							} else {
								result.append("&");
							}
							result.append(key.split("entaredir_")[1]).append("=").append(value);
						}
					}
				}

			if (StringUtils.isNotBlank(result.toString())) {
				//System.out.println("ENCODED: " + result.toString());
				params = result.toString(); // = URLEncoder.encode(result.toString(), "UTF-8");
			}

		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getRedirectParams");
		}
		//return null;
		return params;
	}

	public String getShowletParam(String param) {
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		Widget currentShowlet = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
		return currentShowlet.getConfig().getProperty(param);
	}

	@Override
	public String addComment() {
		try {
			String commentsActive = this.getShowletParam(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_ACTIVE);
			boolean commentsAllowed = null != commentsActive && commentsActive.equalsIgnoreCase("true");
			if (!commentsAllowed) {
				ApsSystemUtils.getLogger().info("comments not allowed");
				return BaseAction.USER_NOT_ALLOWED;
			}

			String anonComments = this.getShowletParam(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_ANONYMOUS);
			boolean anomymousCommentsAllowed = null != anonComments && anonComments.equalsIgnoreCase("true");
			if (this.getCurrentUser().getUsername().equalsIgnoreCase(SystemConstants.GUEST_USER_NAME) && !anomymousCommentsAllowed) {
				ApsSystemUtils.getLogger().info("anomymous comments not allowed");
				return BaseAction.USER_NOT_ALLOWED;
			}

			Comment comment = new Comment();
			String contentId = this.getCurrentContentId();
			if (this.isAuth(contentId)) {
				String commentsModeration = this.getShowletParam(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_MODERATED);
				boolean moderation = null != commentsModeration && commentsModeration.equalsIgnoreCase("true");
				if (moderation) {
					comment.setStatus(Comment.STATUS_TO_APPROVE);
					this.addActionMessage(this.getText("jpcontentfeedback_MESSAGE_TO_APPROVED"));
				} else {
					comment.setStatus(Comment.STATUS_APPROVED);
				}
				comment.setComment(this.getCommentText());
				comment.setContentId(contentId);
				comment.setUsername(this.getCurrentUser().getUsername());
				this.getCommentManager().addComment(comment);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addComment");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String insertVote() {
		try {
			String contentRatingActive = this.getShowletParam(ContentFeedbackWidgetAction.WIDGET_PARAM_RATE_CONTENT);
			boolean contentRatingAllowed = null != contentRatingActive && contentRatingActive.equalsIgnoreCase("true");
			if (!contentRatingAllowed) {
				ApsSystemUtils.getLogger().info("ContentRating not allowed");
				return BaseAction.USER_NOT_ALLOWED;
			}
			String contentId = this.getCurrentContentId();
			if (this.isAuth(contentId)) {
				if (!this.isValidVote()) {
					this.addActionError(this.getText("Message.invalidVote"));
					return INPUT;
				}
				boolean alreadyVoted = CheckVotingUtil.isContentVoted(contentId, this.getRequest());
				if (alreadyVoted){
					this.addActionError(this.getText("Message.alreadyVoted"));
					return INPUT;
				}
				this.getRatingManager().addRatingToContent(contentId, this.getVote());
				this.addCookieRating(contentId);

			} else {
				ApsSystemUtils.getLogger().info("not auth to insert vote on content " + contentId);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "insertVote");
			return FAILURE;
		}
		return SUCCESS;
	}


	public String insertCommentVote() {
		try {
			String commentRatingActive = this.getShowletParam(ContentFeedbackWidgetAction.WIDGET_PARAM_RATE_COMMENT);
			boolean commentRatingAllowed = null != commentRatingActive && commentRatingActive.equalsIgnoreCase("true");
			if (!commentRatingAllowed) {
				ApsSystemUtils.getLogger().info("Comment Rating not allowed");
				return BaseAction.USER_NOT_ALLOWED;
			}

			String contentId = this.getCurrentContentId();
			IComment comment = this.getCommentManager().getComment(this.getSelectedComment());
			if (null == comment || !comment.getContentId().equalsIgnoreCase(contentId)) {
				this.addActionError(this.getText("Message.invalidComment"));
				return INPUT;
			}

			if (this.isAuth(contentId)) {
				if (!this.isValidVote()) {
					this.addActionError(this.getText("Message.invalidVote"));
					return INPUT;
				}
				boolean alreadyVoted = CheckVotingUtil.isCommentVoted(this.getSelectedComment(), this.getRequest());
				if (alreadyVoted){
					this.addActionError(this.getText("Message.alreadyVoted"));
					return INPUT;
				}
				this.getRatingManager().addRatingToComment(this.getSelectedComment(), this.getVote());
				this.addCookieRating(this.getSelectedComment());

			} else {
				ApsSystemUtils.getLogger().info("not auth to insert vote on content " + contentId);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "insertVote");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public List<String> getContentCommentIds() {
		List<String> commentIds = new ArrayList<String>();
		try {
			String contentId = this.getCurrentContentId();
			if (StringUtils.isBlank(contentId)) {
				ApsSystemUtils.getLogger().error("Content id null");
				return commentIds;
			}
			if (this.isAuth(contentId)) {
				CommentSearchBean searchBean = new CommentSearchBean();
				searchBean.setContentId(contentId);
				searchBean.setStatus(Comment.STATUS_APPROVED);
				commentIds = this.getCommentManager().searchCommentIds(searchBean);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentCommentIds");
			throw new RuntimeException("Error", t);
		}
		return commentIds;
	}

	@Override
	public boolean isAuthorizedToDelete(String username){
		return username.equals(this.getCurrentUser().getUsername());
	}

	@Override
	public String delete() {
		try {
			if (!this.getCurrentUser().getUsername().equals(SystemConstants.GUEST_USER_NAME)
					&& this.isAuthorizedToDelete(this.getCurrentUser().getUsername())) {
				if (this.isAuth(this.getCurrentContentId())){
					this.getCommentManager().deleteComment(this.getSelectedComment());
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "delete");
			return FAILURE;
		}
		return SUCCESS;
	}



	private boolean isAuth(String contentId) {

		/* se anche guest puo aggiungere commenti....
		IContentDispenser contentDispenser = (IContentDispenser) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_DISPENSER_MANAGER, this.getRequest());
		ContentAuthorizationInfo authInfo = contentDispenser.getAuthorizationInfo(contentId);
		if (null == authInfo) {
			return false;
		}
		IAuthorizationManager authManager = (IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, this.getRequest());
		List<Group> userGroups = authManager.getUserGroups(this.getCurrentUser());
		return authInfo.isUserAllowed(userGroups);
		 */
		return true;
	}

	@Override
	public IComment getComment(Integer commentId) {
		IComment comment = null;
		try {
			comment = this.getCommentManager().getComment(commentId.intValue());
		} catch (ApsSystemException e) {
			ApsSystemUtils.logThrowable(e, this, "getComment");
			throw new RuntimeException("Error", e);
		}
		return comment;
	}

	@Override
	public IRating getContentRating() {
		IRating rating = null;
		try {
			String contentId = this.getCurrentContentId();
			if (StringUtils.isBlank(contentId)) {
				ApsSystemUtils.getLogger().error("Content id null");
				return null;
			}
			rating = this.getRatingManager().getContentRating(contentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentRating");
			throw new RuntimeException("Error", t);
		}
		return rating;
	}

	@Override
	public IRating getCommentRating(int commentId){
		IRating rating = null;
		try {
			if (commentId == 0) {
				ApsSystemUtils.getLogger().error("Content id null");
				return null;
			}
			rating = this.getRatingManager().getCommentRating(commentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getCommentRating");
			throw new RuntimeException("Error", t);
		}
		return rating;
	}

	protected boolean isValidVote() {
		return (null != this.getVote() && this.getVotes().values().contains(this.getVote()));
	}

	protected void addCookieRating(String contentId) {
		UserDetails currentUser = this.getCurrentUser();
		String cookieName = CheckVotingUtil.getCookieName(currentUser.getUsername(), contentId);
		String cookieValue = CheckVotingUtil.getCookieValue(currentUser.getUsername(), contentId);
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(365*24*60*60);//one year
		this.getResponse().addCookie(cookie);
	}

	protected void addCookieRating(int commentId) {
		UserDetails currentUser = this.getCurrentUser();
		String cookieName = CheckVotingUtil.getCookieName(currentUser.getUsername(), commentId);
		String cookieValue = CheckVotingUtil.getCookieValue(currentUser.getUsername(), commentId);
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(365*24*60*60);//one year
		this.getResponse().addCookie(cookie);
	}

	protected HttpServletResponse getResponse() {
		return this._response;
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this._response = response;
	}

	public void setSelectedContent(String selectedContent) {
		this._selectedContent = selectedContent;
	}
	public String getSelectedContent() {
		return _selectedContent;
	}

	public void setRatingManager(IRatingManager ratingManager) {
		this._ratingManager = ratingManager;
	}
	public IRatingManager getRatingManager() {
		return _ratingManager;
	}

	public void setVotes(Map<String, Integer> votes) {
		this._votes = votes;
	}
	public Map<String, Integer> getVotes() {
		if (null != this.getReverseVotes() && this.getReverseVotes().booleanValue()) {
			TreeMap<String, Integer> t = new TreeMap<String, Integer>(this._votes);
			NavigableMap<String, Integer> x = t.descendingMap();
			return  x;
			//return t.descendingMap();
		}
		return _votes;
	}

	public void setVote(Integer vote) {
		this._vote = vote;
	}
	public Integer getVote() {
		return _vote;
	}

	protected ICommentManager getCommentManager() {
		return _commentManager;
	}
	public void setCommentManager(ICommentManager commentManager) {
		this._commentManager = commentManager;
	}

	public String getCommentText() {
		return _commentText;
	}
	public void setCommentText(String commentText) {
		this._commentText = commentText;
	}

	public int getSelectedComment() {
		return _selectedComment;
	}
	public void setSelectedComment(int selectedComment) {
		this._selectedComment = selectedComment;
	}

	public String extractContentId() {
		String contentId = null;
		if (null == contentId || contentId.trim().length() == 0) {
			RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
			Widget currentShowlet = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			if (null != currentShowlet.getConfig() && currentShowlet.getConfig().getProperty("contentId") != null && currentShowlet.getConfig().getProperty("contentId").length() > 0) {
				contentId = currentShowlet.getConfig().getProperty("contentId");
			} else {
				contentId = (String) this.getRequest().getAttribute("currentContentId");
			}
		}
		return contentId;
	}


	protected ContentActionHelper getContentActionHelper() {
		return _contentActionHelper;
	}
	public void setContentActionHelper(ContentActionHelper contentActionHelper) {
		this._contentActionHelper = contentActionHelper;
	}

	protected IContentFeedbackManager getContentFeedbackManager() {
		return _contentFeedbackManager;
	}
	public void setContentFeedbackManager(IContentFeedbackManager contentFeedbackManager) {
		this._contentFeedbackManager = contentFeedbackManager;
	}

	public Boolean getReverseVotes() {
		return _reverseVotes;
	}
	public void setReverseVotes(Boolean reverseVotes) {
		this._reverseVotes = reverseVotes;
	}

//	public String getFormContentId() {
//		return _formContentId;
//	}
//	public void setFormContentId(String formContentId) {
//		this._formContentId = formContentId;
//	}


	public Map<String, String> getRedirectParamNames() {
		return redirectParamNames;
	}
	public void setRedirectParamNames(Map<String, String> redirectParamNames) {
		this.redirectParamNames = redirectParamNames;
	}


	/**
	 * Lista di parametri, settati dall'utente in {@link FeedbackIntroTag}.
	 * Se in request è presente uno di questi parametri, nei from verrà generato un hidden.
	 * Al submit, i valore di questi hidden concorreranno a valorizzare la mappa redirectParamNames.
	 * Questa mappa serve per appendere i parametri ad ogni azione (che è sempre redirectAction)
	 * @return
	 */
	public String[] getExtraParamNames() {
		if (null == _extraParamNames) {
			_extraParamNames = (String[]) this.getRequest().getAttribute("extraParamNames");
		}
		return _extraParamNames;
	}
	public void setExtraParamNames(String[] extraParamNames) {
		this._extraParamNames = extraParamNames;
	}


	public String getCurrentContentId() {
		String value =  _currentContentId;
		if (StringUtils.isBlank(value)) {
			value = this.extractContentId();
		}
		return value;
	}

	public void setCurrentContentId(String currentContentId) {
		this._currentContentId = currentContentId;
	}


	//private String _formContentId;
	private String _currentContentId;
	private Boolean _reverseVotes;


	private String _commentText;
	private ICommentManager _commentManager;
	private Map<String, Integer>  _votes;
	private IRatingManager _ratingManager;
	private Integer _vote;
	private String _selectedContent;
	private int _selectedComment;
	private HttpServletResponse _response;
	private ContentActionHelper _contentActionHelper;
	private IContentFeedbackManager _contentFeedbackManager;

	private Map<String, String> redirectParamNames = new HashMap<String, String>();
	private String[] _extraParamNames;

}