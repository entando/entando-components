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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services;

import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment.IdeaComment;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.Idea;

public class JpcrowdsourcingTestHelper {
	
	public Idea getIdea(String instance, String title, String descr, String username) {
		Idea idea = new Idea();
		idea.setDescr(descr);
		idea.setUsername(username);
		idea.setTitle(title);
		idea.setInstanceCode(instance);
		return idea;
	}
	
	public IdeaComment getComment(Idea idea, String comment, String username) {
		IdeaComment ideaComment = new IdeaComment();
		ideaComment.setComment(comment);
		ideaComment.setIdeaId(idea.getId());
		ideaComment.setStatus(IIdea.STATUS_APPROVED);
		ideaComment.setUsername(username);
		return ideaComment;
	}
	
}