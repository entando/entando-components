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
package com.agiletec.plugins.jpcontentfeedback.apsadmin.comment;

import java.util.List;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpcontentfeedback.apsadmin.JpContentFeedbackApsAdminBaseTestCase;

import com.agiletec.plugins.jpcontentfeedback.aps.system.JpcontentfeedbackSystemConstants;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.ICommentManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.Comment;
import com.agiletec.plugins.jpcontentfeedback.apsadmin.feedback.ContentFeedbackAction;
import com.opensymphony.xwork2.Action;

public class TestCommentAction extends JpContentFeedbackApsAdminBaseTestCase {

	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

	public void testViewList() throws Throwable{
		try{
			this.setUserOnSession("admin");
			this.initAction("/do/jpcontentfeedback/Comments", "search");
			this.addParameter("status", "1");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			ContentFeedbackAction action = (ContentFeedbackAction)this.getAction();
			List<String> listaIds = action.getCommentIds();
			assertEquals(0, listaIds.size());
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		}
	}

	public void testAddSearchUpdateComment() throws Throwable{
		Comment comment = null;
		try{
			this.setUserOnSession("admin");
			this.initAction("/do/jpcontentfeedback/Comments", "search");
			this.addParameter("status", Comment.STATUS_TO_APPROVE);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			ContentFeedbackAction action = (ContentFeedbackAction)this.getAction();
			List<String> listaIds = action.getCommentIds();
			assertEquals(0, listaIds.size());

			comment = createNewComment();
			this._commentManager.addComment(comment);

			this.initAction("/do/jpcontentfeedback/Comments", "search");
			this.addParameter("status", Comment.STATUS_TO_APPROVE);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			action = (ContentFeedbackAction)this.getAction();
			listaIds = action.getCommentIds();
			assertEquals(1, listaIds.size());

			this.initAction("/do/jpcontentfeedback/Comments", "view");
			this.addParameter("selectedComment", comment.getId());
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			action = (ContentFeedbackAction)this.getAction();
			assertNotNull(action.getComment());

			this.initAction("/do/jpcontentfeedback/Comments", "updateStatus");
			this.addParameter("selectedComment", comment.getId());
			this.addParameter("status", Comment.STATUS_APPROVED);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			this.initAction("/do/jpcontentfeedback/Comments", "search");
			this.addParameter("status", Comment.STATUS_APPROVED);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			action = (ContentFeedbackAction)this.getAction();
			listaIds = action.getCommentIds();
			assertEquals(1, listaIds.size());

			this.initAction("/do/jpcontentfeedback/Comments", "delete");
			this.addParameter("selectedComment", comment.getId());
			this.addParameter("strutsAction", ApsAdminSystemConstants.DELETE);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			this.initAction("/do/jpcontentfeedback/Comments", "search");
			this.addParameter("status", Comment.STATUS_APPROVED);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			action = (ContentFeedbackAction)this.getAction();
			listaIds = action.getCommentIds();
			assertEquals(0, listaIds.size());



		} catch (Throwable t) {
			throw t;
		}finally {
			List<String> listaIds = this._commentManager.searchCommentIds(null);
			for (int i=0; i< listaIds.size(); i++){
				this._commentManager.deleteComment(Integer.parseInt(listaIds.get(i)));
			}
		}
	}

	private Comment createNewComment(){
		Comment comment = new Comment();
		comment.setComment("Testo del commento");
		comment.setContentId("ART1");
		comment.setStatus(Comment.STATUS_TO_APPROVE);
		comment.setUsername("username");
		return comment;

	}

	private void init() throws Exception {
    	try {
    		_commentManager = (ICommentManager) this.getService(JpcontentfeedbackSystemConstants.COMMENTS_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }

	  private ICommentManager _commentManager = null;
}
