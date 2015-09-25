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
