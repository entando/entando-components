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
package com.agiletec.plugins.jpcrowdsourcing.apsadmin.comment;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment.IIdeaComment;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment.IIdeaCommentManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;

public class IdeaCommentFinderAction extends BaseAction implements IIdeaCommentFinderAction {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaCommentFinderAction.class);

	@Override
	public List<Integer> getComments() {
		List<Integer> list = new ArrayList<Integer>();
		try {
			list = this.getIdeaCommentManager().searchComments(this.getSearchStatus(), this.getCommentText(), this.getIdeaId());
		} catch (Throwable t) {
			_logger.error("error in getComments", t);
			throw new RuntimeException("Errore in getComments");
		}
		return list;
	}

	@Override
	public IIdeaComment getComment(int id) {
		IIdeaComment comment = null;
		try {
			comment = this.getIdeaCommentManager().getComment(id);
		} catch (Throwable t) {
			_logger.error("error in getComment", t);
			throw new RuntimeException("Errore in getComment con id " + id);
		}
		return comment;
	}

	@Override
	public IIdea getIdea(String ideaId) {
		IIdea idea = null;
		try {
			idea = this.getIdeaManager().getIdea(ideaId);
		} catch (Throwable t) {
			_logger.error("Error in getIdea", t);
			throw new RuntimeException("Errore in getIdea con id " + ideaId);
		}
		return idea;
	}

	@Override
	public String view() {
		try {
			IIdeaComment comment = this.getIdeaCommentManager().getComment(this.getCommentId());
			if (null == comment) {
				this.addActionError(this.getText("jpcrowdsourcing.comment.null"));
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String changeStatus() {
		try {
			IIdeaComment comment = this.getIdeaCommentManager().getComment(this.getCommentId());
			if (null == comment) {
				this.addActionError(this.getText("jpcrowdsourcing.comment.null"));
				return INPUT;
			}
			comment.setStatus(this.getStatus());
			this.getIdeaCommentManager().updateComment(comment);
		} catch (Throwable t) {
			_logger.error("error in changeStatus", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String delete() {
		try {
			IIdeaComment comment = this.getIdeaCommentManager().getComment(this.getCommentId());
			if (null == comment) {
				this.addActionError(this.getText("jpcrowdsourcing.comment.null"));
				return INPUT;
			}
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getIdeaCommentManager().deleteComment(comment.getId());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String trash() {
		try {
			IIdeaComment comment = this.getIdeaCommentManager().getComment(this.getCommentId());
			if (null == comment) {
				this.addActionError(this.getText("jpcrowdsourcing.comment.null"));
				return INPUT;
			}
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}


	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public void setCommentText(String commentText) {
		this._commentText = commentText;
	}
	public String getCommentText() {
		return _commentText;
	}

	public Integer getSearchStatus() {
		return _searchStatus;
	}
	public void setSearchStatus(Integer searchStatus) {
		this._searchStatus = searchStatus;
	}

	public Integer getStatus() {
		return _status;
	}
	public void setStatus(Integer status) {
		this._status = status;
	}

	public int getCommentId() {
		return _commentId;
	}
	public void setCommentId(int commentId) {
		this._commentId = commentId;
	}

	public void setIdeaId(String ideaId) {
		this._ideaId = ideaId;
	}
	public String getIdeaId() {
		return _ideaId;
	}
	
	protected IIdeaManager getIdeaManager() {
		return _ideaManager;
	}
	public void setIdeaManager(IIdeaManager ideaManager) {
		this._ideaManager = ideaManager;
	}

	protected IIdeaCommentManager getIdeaCommentManager() {
		return _ideaCommentManager;
	}
	public void setIdeaCommentManager(IIdeaCommentManager ideaCommentManager) {
		this._ideaCommentManager = ideaCommentManager;
	}

	private int _strutsAction;
	private String _commentText;
	private Integer _searchStatus;
	private Integer _status;
	private int _commentId;
	private String _ideaId;

	private IIdeaManager _ideaManager;
	private IIdeaCommentManager _ideaCommentManager;

}
