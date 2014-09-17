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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment;

import java.util.Date;
import java.util.List;

import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcontentfeedback.aps.JpcontentfeedbackBaseTestCase;
import com.agiletec.plugins.jpcontentfeedback.aps.system.JpcontentfeedbackSystemConstants;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.Comment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.CommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.IComment;

public class TestCommentManager extends JpcontentfeedbackBaseTestCase{

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testAddGetComment() throws Throwable{
		Comment comment = null;
		try {

			IContentManager contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
			Content content = contentManager.loadContent("ART1", true);
			if (content==null){
				fail();
			}
			List<String> listaIds = this._commentManager.searchCommentIds(null);
			assertEquals(0,listaIds.size());
			comment = createNewComment();
			comment.setContentId(content.getId());
			this._commentManager.addComment(comment);
			assertNotNull(comment.getId());
			IComment commentRead = this._commentManager.getComment(comment.getId());
			assertEquals(comment.getComment(), commentRead.getComment());
			assertEquals(comment.getStatus(), commentRead.getStatus());
			assertEquals(comment.getUsername(), commentRead.getUsername());
			assertEquals(comment.getId(), commentRead.getId());
			assertEquals(Comment.STATUS_TO_APPROVE, commentRead.getStatus());

			CommentSearchBean searchBean = new CommentSearchBean();
			searchBean.setComment("Testo ");
			listaIds = this._commentManager.searchCommentIds(searchBean);
			assertEquals(1, listaIds.size());

		// Ricerca per data successiva a publicazione
			searchBean = new CommentSearchBean();
			searchBean.setComment("Testo ");
			searchBean.setCreationFROMDate(new Date(System.currentTimeMillis()+24*60*60*1000));
			searchBean.setStatus(0);
			searchBean.setUsername("username");
			listaIds = this._commentManager.searchCommentIds(searchBean);
			assertEquals(0, listaIds.size());

		// Ricerca elemento esatto con qualsiasi stato
			searchBean = new CommentSearchBean();
			searchBean.setComment("Testo ");
			searchBean.setCreationFROMDate(new Date(System.currentTimeMillis()));
			searchBean.setStatus(0);
			searchBean.setUsername("username");
			listaIds = this._commentManager.searchCommentIds(searchBean);
			assertEquals(1, listaIds.size());

		// Ricerca per commento errato
			searchBean = new CommentSearchBean();
			searchBean.setComment("ciccio");
			listaIds = this._commentManager.searchCommentIds(searchBean);
			assertEquals(0, listaIds.size());

		// Ricerca per titolo errato
			searchBean = new CommentSearchBean();
			searchBean.setComment("ciccio");
			searchBean.setStatus(Comment.STATUS_NOT_APPROVED);
			listaIds = this._commentManager.searchCommentIds(searchBean);
			assertEquals(0, listaIds.size());
			// Ricerca per titolo errato
			searchBean = new CommentSearchBean();
			searchBean.setStatus(Comment.STATUS_TO_APPROVE);
			listaIds = this._commentManager.searchCommentIds(searchBean);
			assertEquals(1, listaIds.size());

		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		} finally {
			if (comment != null ){
				this._commentManager.deleteComment(comment.getId());
			}
		}
	}

	public void testAddUpdateDeleteComment() throws Throwable{
		Comment comment = null;
		try {
			IContentManager contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
			Content content = contentManager.loadContent("ART1", true);
			if (content==null){
				fail();
			}
			List<String> listaIds = this._commentManager.searchCommentIds(null);
			assertEquals(0,listaIds.size());
			comment = createNewComment();
			comment.setContentId(content.getId());
			this._commentManager.addComment(comment);
			assertNotNull(comment.getId());
			IComment commentRead = this._commentManager.getComment(comment.getId());
			assertEquals(comment.getComment(), commentRead.getComment());
			assertEquals(comment.getStatus(), commentRead.getStatus());
			assertEquals(comment.getUsername(), commentRead.getUsername());
			assertEquals(comment.getId(), commentRead.getId());

			this._commentManager.updateCommentStatus(comment.getId(), Comment.STATUS_NOT_APPROVED);
			commentRead = this._commentManager.getComment(comment.getId());
			assertEquals(Comment.STATUS_NOT_APPROVED, commentRead.getStatus());

			this._commentManager.deleteComment(comment.getId());
			assertNull(this._commentManager.getComment(comment.getId()));

		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		} finally {
			if (comment != null ){
				this._commentManager.deleteComment(comment.getId());
			}
		}
	}

	private Comment createNewComment(){
		Comment comment = new Comment();
		comment.setComment("Testo del commento");
		comment.setStatus(Comment.STATUS_TO_APPROVE);
		comment.setUsername("username");
		return comment;

	}
	private void init() {
		this._commentManager = (ICommentManager) this.getService(JpcontentfeedbackSystemConstants.COMMENTS_MANAGER);

	}

	private ICommentManager _commentManager;
}
