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
package com.agiletec.plugins.jpnewsletter.aps;

import java.util.Iterator;

import javax.sql.DataSource;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailManager;
import com.agiletec.plugins.jpnewsletter.PluginConfigTestUtils;
import com.agiletec.plugins.jpnewsletter.aps.system.JpnewsletterSystemConstants;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentType;
import com.agiletec.plugins.jpnewsletter.util.JpnewsletterTestHelper;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;

public class ApsPluginBaseTestCase extends BaseTestCase {
	
	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
        this.activeMailManager(false);
    }
	
	public NewsletterConfig createNewsletterConfig() {
		NewsletterConfig config = new NewsletterConfig();
		config.setSenderCode("senderCode");
		config.setAlsoHtml(false);
		config.setUnsubscriptionPageCode("newsletter_unsubscribe");
		config.setSubject("subject");
		config.setHtmlHeader("htmlHeader");
		config.setHtmlFooter("htmlFooter");
		config.setHtmlSeparator("htmlSeparator");
		config.setTextHeader("textHeader");
		config.setTextFooter("textFooter");
		config.setTextSeparator("textSeparator");
		config.setSubscribersHtmlFooter("Clicca sul link per cancellare la sottoscrizione <a href=\"{unsubscribeLink}\" >CONFERMA</a></body></html>");
		config.setSubscribersTextFooter("Clicca sul link {unsubscribeLink} per cancellare la sottoscrizione");
		
		config.setSubscriptionPageCode("newsletter_terminatereg");
		config.setSubscriptionTokenValidityDays(90);
		config.setSubscriptionSubject("Conferma la sottoscrizione al servizio di Newsletter");
		config.setSubscriptionHtmlBody("<p>Clicca sul link per confermare <a href=\"{subscribeLink}\" >***CONFERMA***</a></p>");
		config.setSubscriptionTextBody("Clicca sul link {subscribeLink} per confermare");
		
		config.setAllContentsAttributeName("allContentsSubscription");
		config.addSubscription("Attach", "attachAttribute");
		config.addSubscription("Image", "imageAttribute");
		NewsletterContentType contentType = new NewsletterContentType();
		contentType.setContentTypeCode("EVN");
		contentType.setHtmlModel(1);
		contentType.setSimpleTextModel(2);
		config.addContentType(contentType);
		contentType = new NewsletterContentType();
		contentType.setContentTypeCode("ART");
		contentType.setHtmlModel(1);
		contentType.setSimpleTextModel(2);
		config.addContentType(contentType);
		return config;
	}
	
	public void compareConfigs(NewsletterConfig conf1, NewsletterConfig conf2) {
		assertEquals(conf1.getSenderCode(), conf2.getSenderCode());
		assertEquals(conf1.isAlsoHtml(), conf2.isAlsoHtml());
		assertEquals(conf1.getUnsubscriptionPageCode(), conf2.getUnsubscriptionPageCode());
		assertEquals(conf1.getSubject(), conf2.getSubject());
		assertEquals(conf1.getHtmlHeader(), conf2.getHtmlHeader());
		assertEquals(conf1.getHtmlFooter(), conf2.getHtmlFooter());
		assertEquals(conf1.getHtmlSeparator(), conf2.getHtmlSeparator());
		assertEquals(conf1.getTextHeader(), conf2.getTextHeader());
		assertEquals(conf1.getTextFooter(), conf2.getTextFooter());
		assertEquals(conf1.getTextSeparator(), conf2.getTextSeparator());
		assertEquals(conf1.getSubscribersTextFooter(), conf2.getSubscribersTextFooter());
		assertEquals(conf1.getSubscribersHtmlFooter(), conf2.getSubscribersHtmlFooter());

		assertEquals(conf1.getSubscriptionPageCode(), conf2.getSubscriptionPageCode());
		assertEquals(conf1.getSubscriptionTokenValidityDays(), conf2.getSubscriptionTokenValidityDays());
		assertEquals(conf1.getSubscriptionSubject(), conf2.getSubscriptionSubject());
		assertEquals(conf1.getSubscriptionHtmlBody(), conf2.getSubscriptionHtmlBody());
		assertEquals(conf1.getSubscriptionTextBody(), conf2.getSubscriptionTextBody());
		
		assertEquals(conf1.getAllContentsAttributeName(), conf2.getAllContentsAttributeName());
		assertEquals(conf1.getSubscriptions().size(), conf2.getSubscriptions().size());
		Iterator subscriptions = conf1.getSubscriptions().keySet().iterator();
		while (subscriptions.hasNext()) {
			String category = (String) subscriptions.next();
			assertEquals(conf1.getSubscriptions().get(category), conf2.getSubscriptions().get(category));
		}
		
		assertEquals(conf1.getContentTypes().size(), conf2.getContentTypes().size());
		Iterator contentTypes = conf1.getContentTypes().keySet().iterator();
		while (contentTypes.hasNext()) {
			String contentTypeCode = (String) contentTypes.next();
			NewsletterContentType ct1 = conf1.getContentType(contentTypeCode);
			NewsletterContentType ct2 = conf2.getContentType(contentTypeCode);
			
			assertEquals(ct1.getContentTypeCode(), ct2.getContentTypeCode());
			assertEquals(ct1.getSimpleTextModel(), ct2.getSimpleTextModel());
			assertEquals(ct1.getHtmlModel(), ct2.getHtmlModel());
		}
	}
	
	protected void checkOriginaryConfig(NewsletterConfig config) {
		assertEquals(2, config.getSubscriptions().size());
		assertNull(config.getAllContentsAttributeName());
		assertEquals("boolean1", config.getSubscriptions().getProperty("cat1"));
		assertEquals("boolean2", config.getSubscriptions().getProperty("evento"));
		
		assertEquals(1, config.getContentTypes().size());
		NewsletterContentType contentType = config.getContentType("ART");
		assertEquals("ART", contentType.getContentTypeCode());
		assertEquals(2, contentType.getSimpleTextModel());
		assertEquals(3, contentType.getHtmlModel());
		
		assertEquals("CODE1", config.getSenderCode());
		assertEquals(true, config.isAlsoHtml());
		assertEquals("Oggetto della mail", config.getSubject());
		
		assertEquals("<strong>Header html della mail</strong>", config.getHtmlHeader());
		assertEquals("<strong>Footer html della mail</strong>", config.getHtmlFooter());
		assertEquals("Separatore html della mail", config.getHtmlSeparator());
		assertEquals("Header text della mail", config.getTextHeader());
		assertEquals("Footer text della mail", config.getTextFooter());
		assertEquals("Separatore text della mail", config.getTextSeparator());
	}
	
    protected void init() throws Exception {
    	try {
    		IUserManager userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
    		IUserProfileManager profileManager = (IUserProfileManager) this.getService(SystemConstants.USER_PROFILE_MANAGER);
    		ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
    		this._helper = new JpnewsletterTestHelper(userManager, profileManager, configManager);
    		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
    		this._helper.setDataSource(dataSource);
		} catch (Exception e) {
			throw e;
		}
    }
    
	@Override
	protected void tearDown() throws Exception {
		this.waitForSentNewsletter();
		this.activeMailManager(true);
		super.tearDown();
	}
	
	protected void waitForSentNewsletter() throws Exception {
		Thread[] threads = new Thread[40];
	    Thread.enumerate(threads);
	    for (int i=0; i<threads.length; i++) {
	    	Thread currentThread = threads[i];
	    	if (currentThread != null && 
	    			(currentThread.getName().startsWith(JpnewsletterSystemConstants.NEWSLETTER_SENDER_THREAD_NAME)
	    					|| currentThread.getName().startsWith(JpnewsletterSystemConstants.EMAIL_SENDER_NAME_THREAD_PREFIX))
	    			) {
	    		currentThread.join();
	    	}
	    }
	}
	
    private void activeMailManager(boolean active) {
		IMailManager mailManager = (IMailManager) this.getService(JpmailSystemConstants.MAIL_MANAGER);
		if (mailManager instanceof MailManager) {
			((MailManager) mailManager).setActive(active);
		}
	}
    
	protected JpnewsletterTestHelper _helper;
	
}