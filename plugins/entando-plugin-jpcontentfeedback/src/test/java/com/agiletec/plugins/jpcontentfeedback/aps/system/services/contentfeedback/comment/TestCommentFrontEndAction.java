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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment;

import java.util.HashMap;
import java.util.List;

import org.apache.struts2.util.TokenHelper;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.cache.ICacheManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.PublicContentAuthorizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcontentfeedback.aps.internalservlet.feedback.ContentFeedbackAction;
import com.agiletec.plugins.jpcontentfeedback.aps.internalservlet.system.TokenInterceptor;
import com.agiletec.plugins.jpcontentfeedback.aps.system.JpcontentfeedbackSystemConstants;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.CommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.ICommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.IRatingManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.RatingDAO;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.RatingManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRating;
import com.agiletec.plugins.jpcontentfeedback.apsadmin.JpContentFeedbackApsAdminBaseTestCase;
import com.agiletec.plugins.jpcontentfeedback.apsadmin.portal.specialwidget.ContentFeedbackWidgetAction;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;


public class TestCommentFrontEndAction extends JpContentFeedbackApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

	public void testAddDeleteCommentByWidgetConfig()throws Throwable{
		String contentId = "ART1";
		try{
			Content content = this._contentManager.loadContent(contentId, true);
			ICacheManager cacheManager = (ICacheManager) ApsWebApplicationUtils.getBean(SystemConstants.CACHE_MANAGER, this.getRequest());
			PublicContentAuthorizationInfo authInfo = new PublicContentAuthorizationInfo(content);
			cacheManager.putInCache(JacmsSystemConstants.CONTENT_AUTH_INFO_CACHE_PREFIX + contentId, authInfo);
			this.setUserOnSession("admin");
			Widget widget = new Widget();
            IWidgetTypeManager widgetTypeMan =
            	(IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
            WidgetType widgetType = widgetTypeMan.getWidgetType("content_feedback_viewer");
            widget.setType(widgetType);
            ApsProperties prop = new ApsProperties();
            prop.put("contentId", contentId);
            prop.put(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_ACTIVE, "true");
            prop.put(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_MODERATED, "false");
            prop.put(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_ANONYMOUS, "false");
            widget.setConfig(prop);

            List<String> listaIds = this._commentManager.searchCommentIds(null);
			assertEquals(0, listaIds.size());

            RequestContext e = new RequestContext();
            e.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET, widget);
            Lang lang = new Lang();
    		lang.setCode("en");
            e.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, lang);
            this.getRequest().setAttribute(RequestContext.REQCTX, e);

			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "insert");
			this.addParameter("formContentId", contentId);
			this.addParameter("commentText", "testComment");

			this.setToken();

			String result1 = this.executeAction();
			assertEquals(Action.SUCCESS, result1);

			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "insert");
			assertEquals(TokenInterceptor.INVALID_TOKEN_CODE, this.executeAction());

			CommentSearchBean searchBean = new CommentSearchBean();
			searchBean.setComment("Testo ");
			listaIds = this._commentManager.searchCommentIds(null);
			assertEquals(1, listaIds.size());

			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "delete");
			this.addParameter("contentId", listaIds.get(0));
		} catch (Throwable t) {
			throw t;
		} finally{
			List<String> listaIds = this._commentManager.searchCommentIds(null);
			for (int i=0; i< listaIds.size(); i++){
				this._commentManager.deleteComment(Integer.parseInt(listaIds.get(i)));
			}
		}
	}

	private void setToken() {
		String token = TokenHelper.generateGUID();
		ActionContext.getContext().setSession(new HashMap<String, Object>());
		String tokenName = "test_tokenName";
		String tokenSessionAttributeName = TokenHelper.buildTokenSessionAttributeName(tokenName);
		ActionContext.getContext().getSession().put(tokenSessionAttributeName, token);
		this.addParameter(TokenHelper.TOKEN_NAME_FIELD, new String[]{tokenName});
		this.addParameter(tokenName, new String[]{token});
	}

	public void testAddContentRatingByShowletConfig()throws Throwable {
		String contentId = "ART1";
		try {
			Content content = this._contentManager.loadContent(contentId, true);
			ICacheManager cacheManager = (ICacheManager) ApsWebApplicationUtils.getBean(SystemConstants.CACHE_MANAGER, this.getRequest());
			PublicContentAuthorizationInfo authInfo = new PublicContentAuthorizationInfo(content);
			cacheManager.putInCache(JacmsSystemConstants.CONTENT_AUTH_INFO_CACHE_PREFIX + contentId, authInfo);
			this.setUserOnSession("admin");
			Widget widget = new Widget();
            IWidgetTypeManager showletTypeMan =
            	(IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
            WidgetType widgetType = showletTypeMan.getWidgetType("content_feedback_viewer");
            widget.setType(widgetType);
            ApsProperties prop = new ApsProperties();
            prop.put("contentId", contentId);
            prop.put(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_ACTIVE, "true");
            prop.put(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_MODERATED, "false");
            prop.put(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_ANONYMOUS, "false");
            prop.put(ContentFeedbackWidgetAction.WIDGET_PARAM_RATE_COMMENT, "true");
            prop.put(ContentFeedbackWidgetAction.WIDGET_PARAM_RATE_CONTENT, "true");
            widget.setConfig(prop);

            RequestContext e = new RequestContext();
            e.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET, widget);
            Lang lang = new Lang();
    		lang.setCode("en");
            e.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, lang);
            this.getRequest().setAttribute(RequestContext.REQCTX, e);

            List<String> listaIds = this._commentManager.searchCommentIds(null);
			assertEquals(0, listaIds.size());

			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "insert");
			this.setToken();
			this.addParameter("formContentId", "ART1");
			this.addParameter("title", "testTitle");
			this.addParameter("commentText", "testComment");
			String result1 = this.executeAction();
			assertEquals(Action.SUCCESS, result1);

			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "insert");
			this.setToken();
			this.addParameter("formContentId", contentId);
			this.addParameter("title", "testTitle");
			this.addParameter("commentText", "testComment");
			String result2 = this.executeAction();
			assertEquals(Action.SUCCESS, result2);

			CommentSearchBean searchBean = new CommentSearchBean();
			searchBean.setComment("Testo ");

			CommentSearchBean sb = new CommentSearchBean();
			sb.setSort(ICommentSearchBean.SORT_ASC);
			listaIds = this._commentManager.searchCommentIds(sb);
			assertEquals(2, listaIds.size());

			// Inserimento votazione su commento 1
			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "insertCommentVote");
			this.setToken();
			this.addParameter("formContentId", contentId);
			this.addParameter("selectedComment", listaIds.get(0));
			this.addParameter("vote", 2);
			result2 = this.executeAction();
			assertEquals(Action.SUCCESS, result2);

			ContentFeedbackAction action = (ContentFeedbackAction)this.getAction();
			IRating ratingConten_0 = action.getCommentRating(Integer.parseInt(listaIds.get(0)));
			assertNotNull(ratingConten_0);
			assertEquals(1, ratingConten_0.getVoters());
			assertEquals(2, ratingConten_0.getSumvote());

			IRating ratingConten_1 = action.getCommentRating(Integer.parseInt(listaIds.get(1)));
			assertNull(ratingConten_1);

			IRating ratingConten = action.getContentRating();
			assertNull(ratingConten);

		// Inserimento votazione su contenuto
			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "insertVote");
			this.setToken();
			this.addParameter("formContentId", contentId);
			this.addParameter("vote", 4);
			result2 = this.executeAction();
			assertEquals(Action.SUCCESS, result2);

			ContentFeedbackAction action_1 = (ContentFeedbackAction)this.getAction();
			ratingConten_0 = action_1.getCommentRating(Integer.parseInt(listaIds.get(0)));
			assertNotNull(ratingConten_0);
			assertEquals(1, ratingConten_0.getVoters());
			assertEquals(2, ratingConten_0.getSumvote());

//			ratingConten_1 = action.getCommentRating(Integer.parseInt(listaIds.get(1)));
//			assertNull(ratingConten_1);
//
//			ratingConten = action.getContentRating();
//			assertEquals(1, ratingConten.getVoters());
//			assertEquals(4, ratingConten.getSumvote());
			
		} catch (Throwable t) {
			throw t;
		} finally{
			List<String> listaIds = this._commentManager.searchCommentIds(null);
			RatingDAO ratingDao = (RatingDAO) ((RatingManager)this._ratingManager).getRatingDAO();
			for (int i=0; i< listaIds.size(); i++){
				IRating rating = this._ratingManager.getCommentRating(Integer.parseInt(listaIds.get(i)));
				if (rating!=null){
					ratingDao.removeRating(rating.getCommentId());
				}
				this._commentManager.deleteComment(Integer.parseInt(listaIds.get(i)));
			}
			IRating rating = this._ratingManager.getContentRating(contentId);
			if (rating!=null){
				((RatingDAO)ratingDao).removeContentRating(rating.getContentId());
			}
		}
	}
	
	public void testViewContentAndAddCommentByRequest()throws Throwable{
		String contentId = "ART1";
		try {
			Content content = this._contentManager.loadContent(contentId, true);
			ICacheManager cacheManager = (ICacheManager) ApsWebApplicationUtils.getBean(SystemConstants.CACHE_MANAGER, this.getRequest());
			PublicContentAuthorizationInfo authInfo = new PublicContentAuthorizationInfo(content);
			cacheManager.putInCache(JacmsSystemConstants.CONTENT_AUTH_INFO_CACHE_PREFIX + contentId, authInfo);
			this.setUserOnSession("admin");
			this._contentManager.loadContent(contentId, true);
			Widget showlet = new Widget();
            IWidgetTypeManager showletTypeMan =
            	(IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
            WidgetType WidgetType = showletTypeMan.getWidgetType("content_feedback_viewer");
            showlet.setType(WidgetType);
            ApsProperties prop = new ApsProperties();
            prop.put("contentId", contentId);
            prop.put(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_ACTIVE, "true");
            prop.put(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_MODERATED, "false");
            prop.put(ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_ANONYMOUS, "false");
            prop.put(ContentFeedbackWidgetAction.WIDGET_PARAM_RATE_COMMENT, "true");
            prop.put(ContentFeedbackWidgetAction.WIDGET_PARAM_RATE_CONTENT, "true");
            showlet.setConfig(prop);

            RequestContext e = new RequestContext();
            e.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET, showlet);
            Lang lang = new Lang();
    		lang.setCode("en");
            e.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, lang);
            this.getRequest().setAttribute(RequestContext.REQCTX, e);

			this.setUserOnSession("admin");
			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "intro");
			this.setToken();
			this.addParameter("formContentId", contentId);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			ContentFeedbackAction action = (ContentFeedbackAction)this.getAction();
			List<String> commentIds = action.getContentCommentIds();
			assertEquals(0, commentIds.size());

			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "insert");
			this.setToken();
			this.addParameter("formContentId", contentId);
			this.addParameter("title", "testTitle");
			this.addParameter("commentText", "testComment");
			this.setToken();
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			this.setUserOnSession("admin");
			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "intro");
			this.setToken();
			this.addParameter("formContentId", contentId);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			action = (ContentFeedbackAction)this.getAction();
			action.setCurrentContentId(contentId);
			commentIds = action.getContentCommentIds();
			assertEquals(1, commentIds.size());

		} catch (Throwable t) {
			throw t;
		} finally{
			List<String> listaIds = this._commentManager.searchCommentIds(null);
			for (int i=0; i< listaIds.size(); i++){
				this._commentManager.deleteComment(Integer.parseInt(listaIds.get(i)));
			}
		}
	}

	private void init() throws Exception {
    	try {
    		this._commentManager = (ICommentManager) this.getService(JpcontentfeedbackSystemConstants.COMMENTS_MANAGER);
    		this._ratingManager = (IRatingManager) this.getService(JpcontentfeedbackSystemConstants.RATING_MANAGER);
    		this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }

	private ICommentManager _commentManager = null;
	private IRatingManager _ratingManager = null;
	private IContentManager _contentManager;

}