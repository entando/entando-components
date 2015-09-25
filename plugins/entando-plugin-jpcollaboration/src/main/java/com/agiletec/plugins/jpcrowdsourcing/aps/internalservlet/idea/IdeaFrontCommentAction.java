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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment.IIdeaComment;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment.IIdeaCommentManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment.IdeaComment;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.Idea;

public class IdeaFrontCommentAction extends BaseAction implements IIdeaFrontCommentAction {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaFrontCommentAction.class);
	
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

	@Override
	public IIdeaComment getComment(int id) {
		IIdeaComment comment = null;
		try {
			comment = this.getIdeaCommentManager().getComment(id);
			if (null != comment && comment.getStatus() != IIdea.STATUS_APPROVED) return null;
		} catch (Throwable t) {
			_logger.error("error in getComment", t);
			throw new RuntimeException("Errore in caricamento commento " + id);
		}
		return comment;
	}

	@Override
	public String saveComment() {
		try {
			IdeaComment ideaComment = (IdeaComment) this.getIdeaComment();
			ideaComment.setUsername(this.getCurrentUser().getUsername());
			this.getIdeaCommentManager().addComment(ideaComment);
			if (this.getIdeaManager().getConfig().isModerateComments()) {
				this.addActionMessage(this.getText("jpcrowdsourcing.message.comment.inserted.moderated"));
			} else {
				this.addActionMessage(this.getText("jpcrowdsourcing.message.comment.inserted.ok"));
			}
		} catch (Throwable t) {
			_logger.error("error in saveComment", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String entryComment() {
		try {
			this.setIdeaComment(null);
		} catch (Throwable t) {
			_logger.error("error in entryComment", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Restituisce solamente le categorie figlie del nodo principale associato la servizio
	 * I nodi speciali sono definiti nel wrapper
	 */
	public List<Category> getSpecialCategories(Idea idea) {
		List<Category> categories = new ArrayList<Category>();
		List<String> commentCategories= idea.getTags();
		if (null != commentCategories) {
			Iterator<String> it = commentCategories.iterator();
			while (it.hasNext()) {
				Category cat = this.getCategoryManager().getCategory(it.next());
				if (null != cat) {
					String rootCode = this.getIdeaManager().getCategoryRoot();
					if (cat.isChildOf(rootCode) && (null == cat.getChildren() || cat.getChildren().length == 0)) {
						categories.add(cat);
					}
				}
			}
		}
		return categories;
	}

	public String getIdeaId() {
		return ideaId;
	}
	public void setIdeaId(String ideaId) {
		this.ideaId = ideaId;
	}

	public void setIdeaComment(IdeaComment ideaComment) {
		this._ideaComment = ideaComment;
	}
	public IdeaComment getIdeaComment() {
		return _ideaComment;
	}

	protected IIdeaManager getIdeaManager() {
		return ideaManager;
	}
	public void setIdeaManager(IIdeaManager ideaManager) {
		this.ideaManager = ideaManager;
	}

	protected IIdeaCommentManager getIdeaCommentManager() {
		return ideaCommentManager;
	}
	public void setIdeaCommentManager(IIdeaCommentManager ideaCommentManager) {
		this.ideaCommentManager = ideaCommentManager;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	public Category getCategory(String code) {
		return (Category) this.getCategoryManager().getCategory(code);
	}

	private IIdeaManager ideaManager;
	private IIdeaCommentManager ideaCommentManager;
	private String ideaId;
	private IdeaComment _ideaComment;
	private ICategoryManager _categoryManager;
}