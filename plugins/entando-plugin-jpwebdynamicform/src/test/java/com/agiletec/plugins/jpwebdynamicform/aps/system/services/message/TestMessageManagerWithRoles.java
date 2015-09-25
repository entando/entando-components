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
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailManager;
import com.agiletec.plugins.jpwebdynamicform.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;
import com.agiletec.plugins.jpwebdynamicform.util.JpwebdynamicformTestHelper;

public class TestMessageManagerWithRoles extends ApsPluginBaseTestCase {
	
	public void testDeleteMessage() throws Throwable {
		String messageId = null;
		try {
			Message message = this._helper.createMessage(null, null, "it", new Date(), "company", "address", JpwebdynamicformTestHelper.EMAIL, "note");
			this._messageManager.addMessage(message);
			messageId = message.getId();
			assertNotNull(this._messageManager.getMessage(messageId));
			
			Answer answer1 = this._helper.createAnswer(null, messageId, "admin", new Date(), "text1");
			this._helper.addAnswer(answer1);
			Answer answer2 = this._helper.createAnswer(null, messageId, "admin", new Date(), "text2");
			this._helper.addAnswer(answer2);
			List<Answer> answers = this._messageManager.getAnswers(messageId);
			assertEquals(2, answers.size());
			
			this._messageManager.deleteMessage(messageId);
			assertNull(this._messageManager.getMessage(messageId));
			answers = this._messageManager.getAnswers(messageId);
			assertEquals(0, answers.size());
			
		} finally {
			if (StringUtils.isNotBlank(messageId)) {
				this._messageManager.deleteMessage(messageId);
			}
		}
	}
	


	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.activeMailManager(false);
		((MessageManager)this._messageManager).getNotifierConfig("COM").setStore(true);
	}
	
	@Override
	protected void tearDown() throws Exception {
		((MessageManager)this._messageManager).getNotifierConfig("COM").setStore(false);
		this.activeMailManager(true);
		super.tearDown();
	}
	
	private void activeMailManager(boolean active) {
		IMailManager mailManager = (IMailManager) this.getService(JpmailSystemConstants.MAIL_MANAGER);
		if (mailManager instanceof MailManager) {
			((MailManager) mailManager).setActive(active);
		}
	}
	
}