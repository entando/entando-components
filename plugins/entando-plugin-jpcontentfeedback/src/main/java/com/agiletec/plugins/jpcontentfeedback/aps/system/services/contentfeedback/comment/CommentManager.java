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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.CommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.IComment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.ICommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.event.ContentFeedbackChangedEvent;

@Aspect
public class CommentManager extends AbstractService implements ICommentManager {

	private static final Logger _logger = LoggerFactory.getLogger(CommentManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getName());
	}
	
	@Override
	public void addComment(IComment comment) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			comment.setId(key);
			comment.setCreationDate(new Date());
			this.getCommentDAO().addComment(comment);
            this.notifyEvent(comment.getContentId(), -1, ContentFeedbackChangedEvent.CONTENT_COMMENT, ContentFeedbackChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("error in add comment", t);
			throw new ApsSystemException("Error add comment", t);
		}
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.deleteContent(..)) && args(content)")
	public void deleteAllComments(Content content) throws ApsSystemException{
		try {
			String contentId = content.getId();
			CommentSearchBean searcherBean = new CommentSearchBean();
			searcherBean.setContentId(contentId);
			List<String> ids = this.searchCommentIds(searcherBean);
			for (int i = 0; i< ids.size();i++){
				this.deleteComment(Integer.parseInt(ids.get(i)));
			}
            this.notifyEvent(contentId, -1, ContentFeedbackChangedEvent.CONTENT_COMMENT, ContentFeedbackChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting all comments", t);
			throw new ApsSystemException("Error deleting all comments", t);
		}
	}
	
	@Override
	public void deleteComment(int id) throws ApsSystemException {
		try {
			this.getCommentDAO().deleteComment(id);
            this.notifyEvent(null, id, ContentFeedbackChangedEvent.CONTENT_COMMENT, ContentFeedbackChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting comment {}", id, t);
			throw new ApsSystemException("Error while remove comment", t);
		}
	}
	
	@Override
	public IComment getComment(int id) throws ApsSystemException {
		IComment comment = null;
		try {
			comment = this.getCommentDAO().getComment(id);
		} catch (Throwable t) {
			_logger.error("error loading comment {}", id, t);
			throw new ApsSystemException("Error while extract comment", t);
		}
		return comment;
	}

	@Override
	public List<String> searchCommentIds(ICommentSearchBean searchBean) throws ApsSystemException {
		List<String> commentIds = new ArrayList<String>();
		try {
			commentIds = this.getCommentDAO().searchCommentsId(searchBean);
		} catch (Throwable t) {
			_logger.error("Error in search comments", t);
			throw new ApsSystemException("Error while search comment", t);
		}
		return commentIds;
	}
	
	@Override
	public void updateCommentStatus(int id, int status) throws ApsSystemException {
		try {
			this.getCommentDAO().updateStatus(id, status);
            this.notifyEvent(null, id, ContentFeedbackChangedEvent.CONTENT_COMMENT, ContentFeedbackChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("error updating comment status to {} for comment {} ", status, id, t);
			throw new ApsSystemException("Error while update comment status", t);
		}
	}
	
	private void notifyEvent(String contentId, int commentId, int objectCode, int operationCode) throws ApsSystemException {
		ContentFeedbackChangedEvent event = new ContentFeedbackChangedEvent();
		event.setContentId(contentId);
		event.setCommentId(commentId);
		event.setObjectCode(objectCode);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}
    
	protected ICommentDAO getCommentDAO() {
		return _commentDAO;
	}
	public void setCommentDAO(ICommentDAO commentDAO) {
		this._commentDAO = commentDAO;
	}
	
	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}
	
	private ICommentDAO _commentDAO;
	private IKeyGeneratorManager _keyGeneratorManager;
	
}