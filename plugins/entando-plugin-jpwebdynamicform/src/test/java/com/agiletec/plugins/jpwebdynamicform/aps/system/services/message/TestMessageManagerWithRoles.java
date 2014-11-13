/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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