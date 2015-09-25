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

import java.util.Iterator;
import java.util.List;

import com.agiletec.plugins.jpcrowdsourcing.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.JpCrowdSourcingSystemConstants;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.JpcrowdsourcingTestHelper;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.Idea;

public class TestIdeaCommentManager extends ApsPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testAddDeleteComment() throws Throwable {
		String title = "test_title";
		String descr = "test_descr";
		String username = "test_bot";
		Idea testIdea = this._testHelper.getIdea(INSTANCE, title, descr, username);
		this._ideaManager.addIdea(testIdea);
		
		IdeaComment testComment = this._testHelper.getComment(testIdea, "test_comment", username);
		this._ideaCommentManager.addComment(testComment);
		Idea idea = (Idea) this._ideaManager.getIdea(testIdea.getId());
		assertNotNull(idea);
		assertEquals(1, idea.getComments().size());
		IdeaComment comment = (IdeaComment) this._ideaCommentManager.getComment(idea.getComments().get(IIdea.STATUS_APPROVED).get(0));
		assertNotNull(comment);
		assertEquals(username, comment.getUsername());
		assertEquals("test_comment", comment.getComment());
		assertEquals(IIdea.STATUS_APPROVED, comment.getStatus());
		assertEquals(idea.getId(), comment.getIdeaId());
	
		this._ideaCommentManager.deleteComment(comment.getId());
		idea = (Idea) this._ideaManager.getIdea(testIdea.getId());
		assertNotNull(idea);
		assertEquals(0, idea.getComments().size());
		
		this._ideaManager.deleteIdea(idea.getId());
		assertNull(this._ideaManager.getIdea(idea.getId()));
	}

	public void testAddDeleteComment2() throws Throwable {
		String title = "test_title";
		String descr = "test_descr";
		String username = "test_bot";
		Idea testIdea = this._testHelper.getIdea(INSTANCE, title, descr, username);
		this._ideaManager.addIdea(testIdea);
		
		IdeaComment testComment = this._testHelper.getComment(testIdea, " test_comment", username);
		this._ideaCommentManager.addComment(testComment);
		
		this._ideaManager.deleteIdea(testIdea.getId());
		assertNull(this._ideaManager.getIdea(testIdea.getId()));
		assertNull(this._ideaCommentManager.getComment(testComment.getId()));
	}

	
	@Override
	protected void tearDown() throws Exception {
		List<String> list = this._ideaManager.searchIdeas(INSTANCE, null, "test_", null, null);
		if (null != list) {
			Iterator<String> it = list.iterator();
			while (it.hasNext()) {
				String ideaId = it.next();
				this._ideaManager.deleteIdea(ideaId);
				System.out.println("Eliminata idea di test " + ideaId);
			}
		}

		super.tearDown();
	}


	private void init() {
		_ideaCommentManager = (IIdeaCommentManager) this.getService(JpCrowdSourcingSystemConstants.IDEA_COMMENT_MANAGER);
		this._ideaManager = (IIdeaManager) this.getService(JpCrowdSourcingSystemConstants.IDEA_MANAGER);
		this._testHelper = new JpcrowdsourcingTestHelper();
	}
	
	private IIdeaCommentManager _ideaCommentManager;
	private IIdeaManager _ideaManager;
	private JpcrowdsourcingTestHelper _testHelper;
	private String INSTANCE = "default";
}
