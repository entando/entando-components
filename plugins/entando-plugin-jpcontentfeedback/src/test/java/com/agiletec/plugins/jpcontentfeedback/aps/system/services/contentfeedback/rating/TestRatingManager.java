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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating;

import java.util.List;

import com.agiletec.plugins.jpcontentfeedback.aps.JpcontentfeedbackBaseTestCase;

import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcontentfeedback.aps.system.JpcontentfeedbackSystemConstants;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.ICommentManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.Comment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRating;

public class TestRatingManager extends JpcontentfeedbackBaseTestCase{
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testAddRatingToContent() throws Throwable{
		IRating rating = null;
		try {
			Content content = _contentManager.loadContent(CONTENT_ID, true);
			if (content==null){
				fail();
			}
			this._retingManager.addRatingToContent(CONTENT_ID, 1);
			rating = this._retingManager.getContentRating(CONTENT_ID);
			assertNotNull(rating);
			assertEquals(1, rating.getVoters());
			assertEquals(1, rating.getSumvote());
			//Inserimento di altri 3 voti:
			this._retingManager.addRatingToContent(CONTENT_ID, 1);
			this._retingManager.addRatingToContent(CONTENT_ID, 2);
			this._retingManager.addRatingToContent(CONTENT_ID, 4);
			rating = this._retingManager.getContentRating(CONTENT_ID);
			assertNotNull(rating);
			assertEquals(4, rating.getVoters());
			assertEquals(8, rating.getSumvote());
		} catch (Throwable t) {
			throw t;
		} finally {
			if(rating != null) {
				((RatingDAO)((RatingManager)this._retingManager).getRatingDAO()).removeContentRating(rating.getContentId());
			}
		}

	}



	public void testAddRatingToComment() throws Throwable{
		IRating rating = null;
		Comment comment = null;
		int COMMENT_ID = -1;
		try {
			comment = createNewComment();
			comment.setContentId(CONTENT_ID);
			this._commentManager.addComment(comment);
			List<String> listaCommenti = this._commentManager.searchCommentIds(null);
			assertEquals(1, listaCommenti.size());
			COMMENT_ID = Integer.parseInt(listaCommenti.get(0));
			this._retingManager.addRatingToComment(COMMENT_ID,1);
			rating = this._retingManager.getCommentRating(COMMENT_ID);
			assertNotNull(rating);
			assertEquals(1, rating.getVoters());
			assertEquals(1, rating.getSumvote());
			//Inserimento di altri 3 voti:
			this._retingManager.addRatingToComment(COMMENT_ID, 1);
			this._retingManager.addRatingToComment(COMMENT_ID, 2);
			this._retingManager.addRatingToComment(COMMENT_ID, 4);
			rating = this._retingManager.getCommentRating(COMMENT_ID);
			assertNotNull(rating);
			assertEquals(4, rating.getVoters());
			assertEquals(8, rating.getSumvote());
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		} finally {
			if (comment!=null){
				this._commentManager.deleteComment(comment.getId());
			}
		}

	}


	private Comment createNewComment(){
		Comment comment = new Comment();
		comment.setComment("Testo del commento");
		comment.setStatus(Comment.STATUS_APPROVED);
		comment.setUsername("username");
		return comment;

	}

	private void init() {
		this._retingManager = (IRatingManager) this.getService(JpcontentfeedbackSystemConstants.RATING_MANAGER);
		_contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
		this._commentManager = (ICommentManager) this.getService(JpcontentfeedbackSystemConstants.COMMENTS_MANAGER);

	}

	private IRatingManager _retingManager;
	private IContentManager _contentManager;
	private String CONTENT_ID = "ART102";
	private ICommentManager _commentManager;
}
