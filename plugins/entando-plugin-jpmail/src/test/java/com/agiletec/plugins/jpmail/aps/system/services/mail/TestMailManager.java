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
package com.agiletec.plugins.jpmail.aps.system.services.mail;

import java.util.List;
import java.util.Properties;

import org.subethamail.smtp.server.SMTPServer;

import com.agiletec.plugins.jpmail.mock.authn.MockAuthenticationHandlerFactory;
import com.agiletec.plugins.jpmail.mock.messages.MockMailMessage;
import com.agiletec.plugins.jpmail.mock.messages.MockMessageHandler;
import com.agiletec.plugins.jpmail.mock.messages.MockMessageHandlerFactory;
import com.agiletec.plugins.jpmail.util.JpmailTestHelper;

import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailConfig;

public class TestMailManager extends AbstractMailConfigTestCase {
	
	public void testGetConfig() throws Throwable {
		MailConfig mailConfig = this._mailManager.getMailConfig();
		this.checkOriginaryConfig(mailConfig);
	}
	
	public void testUpdateConfig() throws Throwable {
		MailConfig originaryConfig = this._mailManager.getMailConfig();
		try {
			MailConfig config = this.createMailConfig();
			this._mailManager.updateMailConfig(config);
			MailConfig updatedConfig = this._mailManager.getMailConfig();
			this.compareConfigs(config, updatedConfig);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._mailManager.updateMailConfig(originaryConfig);
			MailConfig updatedConfig = this._mailManager.getMailConfig();
			this.compareConfigs(originaryConfig, updatedConfig);
		}
	}
	
	public void testSendMail() throws Throwable {
		String[] mailAddresses = JpmailTestHelper.MAIL_ADDRESSES;
		this._mailManager.sendMail(MAIL_TEXT, "Mail semplice", mailAddresses, mailAddresses, mailAddresses, SENDER_CODE);
		
		MockMessageHandler messageHandler = this._myFactory.getMessageHandler();
		List<MockMailMessage> messages = messageHandler.getMessages();
		
		assertNotNull(messages);
		assertEquals(1, messages.size());
		
	}
	
	public void testSendMailWithChangedPort() throws Throwable {
		
		MockMessageHandlerFactory myFactory2 = new MockMessageHandlerFactory();
		MockAuthenticationHandlerFactory myAuthFactory2 = new MockAuthenticationHandlerFactory();
		SMTPServer smtpServer2 = new SMTPServer(myFactory2, myAuthFactory2);
        smtpServer2.setPort(25001);
        smtpServer2.start();
		
		MailConfig originaryConfig = this._mailManager.getMailConfig();
		try {
			MailConfig config = this._mailManager.getMailConfig();
			config.setSmtpPort(25001);
			this._mailManager.updateMailConfig(config);
			String[] mailAddresses = JpmailTestHelper.MAIL_ADDRESSES;
			this._mailManager.sendMail(MAIL_TEXT, "Mail semplice", mailAddresses, mailAddresses, mailAddresses, SENDER_CODE);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._mailManager.updateMailConfig(originaryConfig);
			this.checkOriginaryConfig(originaryConfig);
		}
		
		myFactory2 = null;
		myAuthFactory2 = null;
		smtpServer2.stop();
		smtpServer2 = null;
	}

	
	
	public void testSendMailWithContentType() throws Throwable {
		this._mailManager.sendMail(MAIL_TEXT, "Mail with contentType text/html", 
				JpmailTestHelper.MAIL_ADDRESSES, null, null, SENDER_CODE, IMailManager.CONTENTTYPE_TEXT_HTML);
		
		MockMessageHandler messageHandler = this._myFactory.getMessageHandler();
		List<MockMailMessage> messages = messageHandler.getMessages();
		
		assertNotNull(messages);
		assertEquals(1, messages.size());
		
		this._mailManager.sendMail(MAIL_TEXT, "Mail with contentType text/plain", 
				null, JpmailTestHelper.MAIL_ADDRESSES, null, SENDER_CODE, IMailManager.CONTENTTYPE_TEXT_PLAIN);
		
		messages = messageHandler.getMessages();
		
		assertNotNull(messages);
		assertEquals(2, messages.size());
	}
	
	public void testSendMailWithAttachment() throws Throwable {
		Properties attachments = new Properties();
		attachments.setProperty("ALLEGATO", "target/test/entando_logo.jpg");
		this._mailManager.sendMail(MAIL_TEXT, "Mail with attachment & text html", 
				IMailManager.CONTENTTYPE_TEXT_HTML, attachments, JpmailTestHelper.MAIL_ADDRESSES, null, null, SENDER_CODE);
		
		MockMessageHandler messageHandler = this._myFactory.getMessageHandler();
		List<MockMailMessage> messages = messageHandler.getMessages();
		
		assertNotNull(messages);
		assertEquals(1, messages.size());
		
		this._mailManager.sendMail(MAIL_TEXT, "Mail with void attachment & text plain", 
				IMailManager.CONTENTTYPE_TEXT_PLAIN, null, null, null, JpmailTestHelper.MAIL_ADDRESSES, SENDER_CODE);
		
		messages = messageHandler.getMessages();
		
		assertNotNull(messages);
		assertEquals(2, messages.size());
	}
	
	public void testSendMixedMail() throws Throwable {
		Properties attachments = new Properties();
		attachments.setProperty("ALLEGATO", "target/test/entando_logo.jpg");
		String[] mailAddresses = JpmailTestHelper.MAIL_ADDRESSES;
		this._mailManager.sendMixedMail("AAAAAAAAAMixed"+MAIL_TEXT, "Mixed"+MAIL_TEXT, "Oggetto Mail composta", 
				attachments, mailAddresses, mailAddresses, mailAddresses, SENDER_CODE);
		
		MockMessageHandler messageHandler = this._myFactory.getMessageHandler();
		List<MockMailMessage> messages = messageHandler.getMessages();
		
		assertNotNull(messages);
		assertEquals(1, messages.size());
	}
	
	@Override
	protected void setUp() throws Exception {
		this._myFactory = new MockMessageHandlerFactory();
		this._myAuthFactory = new MockAuthenticationHandlerFactory();
		this._smtpServer = new SMTPServer(this._myFactory, this._myAuthFactory);
		this._smtpServer.setPort(25000);
		this._smtpServer.start();
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		this._myFactory = null;
		this._myAuthFactory = null;
		this._smtpServer.stop();
		this._smtpServer = null;
		super.tearDown();
	}
	
	private final String MAIL_TEXT = "<a href=\"http://www.japsportal.org/\" >Test Mail di prova</a>";
	private final String SENDER_CODE = "CODE1";
	
	private SMTPServer _smtpServer;
	private MockMessageHandlerFactory _myFactory;
	private MockAuthenticationHandlerFactory _myAuthFactory;
	
}