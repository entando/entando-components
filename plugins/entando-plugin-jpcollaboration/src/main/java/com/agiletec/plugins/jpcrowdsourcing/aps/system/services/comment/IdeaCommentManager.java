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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.CrowdSourcingConfig;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;

public class IdeaCommentManager extends AbstractService implements IIdeaCommentManager {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaCommentManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getSimpleName());
	}

	@Override
	public void addComment(IIdeaComment ideaComment) throws ApsSystemException {
		try {
			CrowdSourcingConfig config = this.getIdeaManager().getConfig();
			if (config.isModerateComments()) {
				ideaComment.setStatus(IIdea.STATUS_TO_APPROVE);
			} else {
				ideaComment.setStatus(IIdea.STATUS_APPROVED);
			}
			
			int id = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			ideaComment.setCreationDate(new Date());
			ideaComment.setId(id);
			this.getIdeaCommentDAO().insertComment(ideaComment);
		} catch (Throwable t) {
			_logger.error("error in addcomment", t);
			throw new ApsSystemException("errore in addcomment", t);
		}		
	}

	@Override
	public void updateComment(IIdeaComment ideaComment) throws ApsSystemException {
		try {
			this.getIdeaCommentDAO().updateComment(ideaComment);
		} catch (Throwable t) {
			_logger.error("errore in updateComment {}", ideaComment, t);
			throw new ApsSystemException("errore in updateComment " + ideaComment, t);
		}		
	}

	@Override
	public void deleteComment(int id) throws ApsSystemException {
		try {
			this.getIdeaCommentDAO().removeComment(id);
		} catch (Throwable t) {
			_logger.error("errore in eliminazione commento idea {}", id, t);
			throw new ApsSystemException("errore in eliminazione commento idea " + id, t);
		}
	}

	@Override
	public IIdeaComment getComment(int id) throws ApsSystemException {
		IIdeaComment comment = null;
		try {
			comment = this.getIdeaCommentDAO().loadComment(id);
		} catch (Throwable t) {
			_logger.error("errore in recupero commento idea {}", id, t);
			throw new ApsSystemException("errore in recupero commento idea " + id, t);
		}		
		return comment;
	}

	@Override
	public List<Integer> searchComments(Integer searchStatus, String commentText, String ideaId) throws ApsSystemException {
		List<Integer> list = new ArrayList<Integer>();
		try {
			list = this.getIdeaCommentDAO().searchComments(searchStatus, commentText, ideaId);
		} catch (Throwable t) {
			_logger.error("Errore in ricerca commenti", t);
			throw new ApsSystemException("Errore in ricerca commenti");
		}
		return list;
	}

	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}
	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}

	public void setIdeaCommentDAO(IIdeaCommentDAO ideaCommentDAO) {
		this._ideaCommentDAO = ideaCommentDAO;
	}
	protected IIdeaCommentDAO getIdeaCommentDAO() {
		return _ideaCommentDAO;
	}

	protected IIdeaManager getIdeaManager() {
		return _ideaManager;
	}
	public void setIdeaManager(IIdeaManager ideaManager) {
		this._ideaManager = ideaManager;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private IIdeaCommentDAO _ideaCommentDAO;
	private IIdeaManager _ideaManager;
}
