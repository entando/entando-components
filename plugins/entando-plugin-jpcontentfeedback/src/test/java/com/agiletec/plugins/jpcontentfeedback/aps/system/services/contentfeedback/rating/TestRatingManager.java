/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
