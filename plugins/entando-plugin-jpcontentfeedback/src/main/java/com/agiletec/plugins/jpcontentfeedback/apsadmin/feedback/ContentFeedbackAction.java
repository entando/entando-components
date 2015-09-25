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
package com.agiletec.plugins.jpcontentfeedback.apsadmin.feedback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.ICommentManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.CommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.IComment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.ICommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.IRatingManager;

/**
 * @author D.Cherchi
 */
public class ContentFeedbackAction extends AbstractContentFeedbackAction implements IContentFeedbackAction{

	@Override
	public String search() {
		try {
			ICommentSearchBean searchBean = this.prepareSearchBean();
			this.setSearchBean(searchBean);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "search");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public List<String> getCommentIds() {
		List<String> comments = new ArrayList<String>();
		try {
			CommentSearchBean searchBean = (CommentSearchBean) this.getSearchBean();
			searchBean.setSort(ICommentSearchBean.SORT_DESC);
			comments = this.getCommentManager().searchCommentIds(searchBean);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getCommentIds");
		}
		return comments;
	}

	@Override
	public IComment getComment(int id){
		IComment comment = null;
		try {
			comment = this.getCommentManager().getComment(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getComment");
		}
		return comment;
	}

	@Override
	public String view() {
		try {
			IComment comment = this.getComment(this.getSelectedComment());
			this.setComment(comment);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "view");
			return FAILURE;
		}
		return SUCCESS;
	}


	public String trash() {
		try {
			IComment comment = this.getComment(this.getSelectedComment());
			if (null == comment) {
				return INPUT;
			}
			this.setComment(comment);
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "trash");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String delete(){
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getCommentManager().deleteComment(this.getSelectedComment());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "delete");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String updateStatus() {
		try {
			this.getCommentManager().updateCommentStatus(this.getSelectedComment(), this.getStatus());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateStatus");
			return FAILURE;
		}
		return SUCCESS;
	}

	private ICommentSearchBean prepareSearchBean() {
		CommentSearchBean searchBean = new CommentSearchBean();
		searchBean.setComment(this.getText());
		searchBean.setUsername(this.getAuthor());
		searchBean.setCreationFROMDate(this.getFrom());
		searchBean.setCreationTODate(this.getTo());
		searchBean.setStatus(this.getStatus());
		return searchBean;
	}

	public void setCommentManager(ICommentManager commentManager) {
		this._commentManager = commentManager;
	}
	public ICommentManager getCommentManager() {
		return _commentManager;
	}

	public String getText() {
		return _commentText;
	}
	public void setText(String text) {
		this._commentText = text;
	}

	public String getAuthor() {
		return _author;
	}
	public void setAuthor(String author) {
		this._author = author;
	}

	public Date getFrom() {
		return _from;
	}
	public void setFrom(Date from) {
		this._from = from;
	}

	public Date getTo() {
		return _to;
	}
	public void setTo(Date to) {
		this._to = to;
	}

	public void setSearchBean(ICommentSearchBean searchBean) {
		this._searchBean = searchBean;
	}
	public ICommentSearchBean getSearchBean() {
		return _searchBean;
	}

	public void setSelectedComment(int selectedComment) {
		this._selectedComment = selectedComment;
	}
	public int getSelectedComment() {
		return _selectedComment;
	}

	public void setComment(IComment comment) {
		this._comment = comment;
	}
	public IComment getComment() {
		return _comment;
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
	protected IRatingManager getRatingManager() {
		return _ratingManager;
	}

	public void setVotes(Map<String, Integer> votes) {
		this._votes = votes;
	}
	public Map<String, Integer> getVotes() {
		return _votes;
	}

	public void setVote(int vote) {
		this._vote = vote;
	}
	public int getVote() {
		return _vote;
	}

	public void setStatus(int status) {
		this._status = status;
	}
	public int getStatus() {
		return _status;
	}

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	private int _strutsAction;
	private String _commentText;
	private String _author;
	private Date _from;
	private Date _to;
	private ICommentManager _commentManager;
	private ICommentSearchBean _searchBean;
	private int _selectedComment;
	private IComment _comment;
	private Map<String, Integer>  _votes;
	private IRatingManager _ratingManager;
	private int _status;
	private int _vote;
	private String _selectedContent;

}