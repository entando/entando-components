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
package com.agiletec.plugins.jpcrowdsourcing.aps.internalservlet.idea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcrowdsourcing.aps.internalservlet.idea.util.CookieUtil;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.Idea;
import com.agiletec.plugins.jpcrowdsourcing.apsadmin.portal.specialwidget.IdeaInstanceWidgetAction;

public class ListIdeaFrontAction extends BaseAction implements IListIdeaFrontAction, ServletResponseAware {

	private static final Logger _logger =  LoggerFactory.getLogger(ListIdeaFrontAction.class);
	
	public String list() {
		String newIdeaParam = this.getRequest().getParameter("newIdea");
		if (null != newIdeaParam && Boolean.parseBoolean(newIdeaParam)) {
			if (this.getIdeaManager().getConfig().isModerateEntries()) {
				this.addActionMessage(this.getText("jpcrowdsourcing.message.idea.inserted.moderated"));
			} else {
				this.addActionMessage(this.getText("jpcrowdsourcing.message.idea.inserted.ok"));
			}
		}
		return SUCCESS;
	}
	
	@Override
	public UserDetails getCurrentUser() {
		return super.getCurrentUser();
	}

	@Override
	public String ideaLike() {
		try {
			IIdea idea = this.getIdeaManager().getIdea(this.getIdeaId());
			if (null == idea) {
				this.addActionError(this.getText("jpcrowdsourcing.error.idea.null"));
				return INPUT;
			}
			if (idea.getStatus() != IIdea.STATUS_APPROVED) {
				this.addActionError(this.getText("jpcrowdsourcing.error.idea.null"));
				return INPUT;
			}
			boolean alreadyVoted = CookieUtil.isIdeaVoted("RATE_"+this.getIdeaId(), this.getRequest());
			if (alreadyVoted) {
				this.addActionError(this.getText("jpcrowdsourcing.message.alreadyVoted"));
				return "alreadyVoted";
			}
			((Idea)idea).setVotePositive(idea.getVotePositive() + 1);
			this.getIdeaManager().updateIdea(idea);
			this.addCookie("RATE_" + this.getIdeaId());
		} catch (Throwable t) {
			_logger.error("error in ideaLike", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String ideaUnlike() {
		try {
			IIdea idea = this.getIdeaManager().getIdea(this.getIdeaId());
			if (null == idea) {
				this.addActionError(this.getText("jpcrowdsourcing.error.idea.null"));
				return INPUT;
			}
			if (idea.getStatus() != IIdea.STATUS_APPROVED) {
				this.addActionError(this.getText("jpcrowdsourcing.error.idea.null"));
				return INPUT;
			}
			boolean alreadyVoted = CookieUtil.isIdeaVoted("RATE_" + this.getIdeaId(), this.getRequest());
			if (alreadyVoted) {
				this.addActionError(this.getText("jpcrowdsourcing.message.alreadyVoted"));
				return "alreadyVoted";
			}

			((Idea)idea).setVoteNegative(idea.getVoteNegative() + 1);
			this.getIdeaManager().updateIdea(idea);
			this.addCookie("RATE_" + this.getIdeaId());
		} catch (Throwable t) {
			_logger.error("error in ideaUnlike", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public List<String> getIdeas(Integer order) {
		List<String> list = new ArrayList<String>();
		try {
			String currentInstance = this.getInstanceCode();
			list = this.getIdeaManager().searchIdeas(currentInstance, IIdea.STATUS_APPROVED, this.getIdeaText(), this.getIdeaTag(), order);
		} catch (Throwable t) {
			_logger.error("error in getIdeas", t);
			throw new RuntimeException("Errore in caricamento lista idee");
		}
		return list;
	}

	@Override
	public IIdea getIdea(String code) {
		IIdea idea = null;
		try {
			idea = this.getIdeaManager().getIdea(code);
			if (null != idea && idea.getStatus() != IIdea.STATUS_APPROVED) return null;
		} catch (Throwable t) {
			_logger.error("error in getIdea", t);
			throw new RuntimeException("Errore in caricamento idea " + code);
		}
		return idea;
	}



	public List<Category> getIdeaTags(Idea idea) {
		List<Category> categories = new ArrayList<Category>();
		List<String> tags = idea.getTags();
		if (null != tags) {
			Iterator<String> it = tags.iterator();
			while (it.hasNext()) {
				Category cat = this.getCategoryManager().getCategory(it.next());
				if (null != cat) {
					categories.add(cat);
				}
			}
		}
		return categories;
	}
	
	protected void addCookie(String ideaId) {
		UserDetails currentUser = this.getCurrentUser();
		String cookieName = CookieUtil.getCookieName(currentUser.getUsername(), ideaId);
		String cookieValue = CookieUtil.getCookieValue(currentUser.getUsername(), ideaId);
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(365*24*60*60);//one year
		this.getServletResponse().addCookie(cookie);
	}
	
	
	protected String extractInstanceCode() {
		String code = null;
		//if (null == code || code.trim().length() == 0) {
			RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
			Widget currentShowlet = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			if (null != currentShowlet.getConfig() && currentShowlet.getConfig().getProperty(IdeaInstanceWidgetAction.WIDGET_PARAM_IDEA_INSTANCE) != null && currentShowlet.getConfig().getProperty(IdeaInstanceWidgetAction.WIDGET_PARAM_IDEA_INSTANCE).length() > 0) {
				code = currentShowlet.getConfig().getProperty(IdeaInstanceWidgetAction.WIDGET_PARAM_IDEA_INSTANCE);
			} else {
				//code = (String) this.getRequest().getAttribute("currentContentId");
			}
		//}
		return code;
	}

	public String getInstanceCode() {
		return this.extractInstanceCode();
	}
	
	public void setIdeaId(String ideaId) {
		this._ideaId = ideaId;
	}
	public String getIdeaId() {
		return _ideaId;
	}

	public void setIdeaManager(IIdeaManager ideaManager) {
		this._ideaManager = ideaManager;
	}
	protected IIdeaManager getIdeaManager() {
		return _ideaManager;
	}

	public void setIdeaText(String ideaText) {
		this._ideaText = ideaText;
	}
	public String getIdeaText() {
		return _ideaText;
	}

	public void setIdeaTag(String ideaTag) {
		this._ideaTag = ideaTag;
	}
	public String getIdeaTag() {
		return _ideaTag;
	}

	public HttpServletResponse getServletResponse() {
		return _servletResponse;
	}
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this._servletResponse = servletResponse;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	public ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	private String _ideaId;
	private IIdeaManager _ideaManager;
	private String _ideaText;
	private String _ideaTag;
	private HttpServletResponse _servletResponse;
	private ICategoryManager _categoryManager;
	
}
