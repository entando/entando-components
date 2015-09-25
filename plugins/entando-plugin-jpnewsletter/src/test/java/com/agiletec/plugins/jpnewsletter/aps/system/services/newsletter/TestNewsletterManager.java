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
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import com.agiletec.plugins.jpnewsletter.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpnewsletter.util.JpnewsletterTestHelper;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpnewsletter.aps.system.JpnewsletterSystemConstants;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterDAO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.NewsletterDAO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.ContentReport;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentReportVO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterReport;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterSearchBean;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.Subscriber;

public class TestNewsletterManager extends ApsPluginBaseTestCase {
	
	public void testGetUpdateNewsletterConfig() throws Throwable {
		NewsletterConfig originaryConfig = this._newsletterManager.getNewsletterConfig();
		this.checkOriginaryConfig(originaryConfig);
		try {
			NewsletterConfig newConfig = this.createNewsletterConfig();
    		this._newsletterManager.updateNewsletterConfig(newConfig);
    		NewsletterConfig updatedConfig = this._newsletterManager.getNewsletterConfig();
    		this.compareConfigs(newConfig, updatedConfig);
    		
    		this._newsletterManager.updateNewsletterConfig(originaryConfig);
    		updatedConfig = this._newsletterManager.getNewsletterConfig();
    		this.compareConfigs(originaryConfig, updatedConfig);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.resetConfig();
		}
	}
	
	public void testAddGetRemoveContentFromQueue(String contentId) throws Throwable {
		try {
			this._helper.setNewsletterManagerThreadDelay(10000, false, null, this._newsletterManager);
			
			List<String> queue = this._newsletterManager.getContentQueue();
			assertEquals(0, queue.size());
			this._newsletterManager.addContentToQueue("id1");
			this._newsletterManager.addContentToQueue("id2");
			this._newsletterManager.addContentToQueue("id3");
			this._newsletterManager.addContentToQueue("id4");
			queue = this._newsletterManager.getContentQueue();
			assertEquals(4, queue.size());
			queue.contains("id1");
			queue.contains("id2");
			queue.contains("id3");
			queue.contains("id4");
			
			this._newsletterManager.removeContentFromQueue("id1");
			this._newsletterManager.removeContentFromQueue("id3");
			queue = this._newsletterManager.getContentQueue();
			assertEquals(2, queue.size());
			queue.contains("id2");
			queue.contains("id4");
			
			this._newsletterManager.addContentToQueue("id5");
			queue = this._newsletterManager.getContentQueue();
			assertEquals(3, queue.size());
			queue.contains("id2");
			queue.contains("id4");
			queue.contains("id5");

			this._newsletterManager.removeContentFromQueue("id2");
			this._newsletterManager.removeContentFromQueue("id4");
			this._newsletterManager.removeContentFromQueue("id5");
			queue = this._newsletterManager.getContentQueue();
			assertEquals(0, queue.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteNewsletters();
			this._helper.resetConfig();
		}
	}
	
	public void testGetContentReport() throws Throwable {
		Date date = new Date();
		NewsletterReport newsletterReport1 = this._helper.createNewsletterReport(1, new Date(), "subject1");
		ContentReport contentReport1 = this._helper.createContentReport(1, newsletterReport1.getId(), "ART1", "textBody1", "htmlBody1");
		contentReport1.addRecipient("user1", "mail1@address.it");
		contentReport1.addRecipient("user2", "mail2@address.it");
		newsletterReport1.addContentReport(contentReport1);
		ContentReport contentReport2 = this._helper.createContentReport(2, newsletterReport1.getId(), "ART102", "textBody2", "htmlBody2");
		contentReport2.addRecipient("user1", "mail1@address.it");
		newsletterReport1.addContentReport(contentReport2);
		NewsletterReport newsletterReport2 = this._helper.createNewsletterReport(2, new Date(date.getTime()+100), "subject2");
		ContentReport contentReport3 = this._helper.createContentReport(3, newsletterReport2.getId(), "ART1", "textBody1", "htmlBody1");
		newsletterReport2.addContentReport(contentReport3);
		ContentReport contentReport4 = this._helper.createContentReport(4, newsletterReport2.getId(), "ART102", "textBody2", "htmlBody2");
		newsletterReport2.addContentReport(contentReport4);
		try {
			this._newsletterDAO.addNewsletterReport(newsletterReport1);
			NewsletterContentReportVO report1 = this._newsletterManager.getContentReport("ART1");
			this.compareContentReports(newsletterReport1, contentReport1, report1);
			NewsletterContentReportVO report2 = this._newsletterManager.getContentReport("ART102");
			this.compareContentReports(newsletterReport1, contentReport2, report2);
			
			this._newsletterDAO.addNewsletterReport(newsletterReport2);
			NewsletterContentReportVO report3 = this._newsletterManager.getContentReport("ART1");
			this.compareContentReports(newsletterReport2, contentReport3, report3);
			NewsletterContentReportVO report4 = this._newsletterManager.getContentReport("ART102");
			this.compareContentReports(newsletterReport2, contentReport4, report4);
			
			List<String> contentIds = this._newsletterManager.getSentContentIds();
			this.compareIds(contentIds, new String[] { "ART1", "ART102" });

			assertTrue(this._newsletterManager.existsContentReport("ART1"));
			assertTrue(this._newsletterManager.existsContentReport("ART102"));
			assertFalse(this._newsletterManager.existsContentReport("ART104"));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteNewsletters();
		}
	}
	
	public void testLoadNewsletterContentIds() throws Throwable {
		EntitySearchFilter[] filters = { };
		List<String> userGroupCodes = new ArrayList<String>();
		userGroupCodes.add(Group.FREE_GROUP_NAME);
		NewsletterSearchBean searchBean = new NewsletterSearchBean();
		List<String> contentIds = this._newsletterManager.loadNewsletterContentIds(filters, userGroupCodes, searchBean);
		this.compareIds(contentIds, new String[] { "ART180" });
		try {
			this._newsletterManager.addContentToQueue("ART180");
			searchBean.setInQueue(new Boolean(false));
			contentIds = this._newsletterManager.loadNewsletterContentIds(filters, userGroupCodes, searchBean);
			this.compareIds(contentIds, new String[] { });
			
			searchBean.setInQueue(new Boolean(true));
			contentIds = this._newsletterManager.loadNewsletterContentIds(filters, userGroupCodes, searchBean);
			this.compareIds(contentIds, new String[] { "ART180" });
			
			NewsletterReport newsletterReport = this._helper.createNewsletterReport(1, new Date(), "subject");
			ContentReport contentReport = this._helper.createContentReport(1, newsletterReport.getId(), 
					"ART180", "textBody1", "htmlBody1");
			newsletterReport.addContentReport(contentReport);
			this._newsletterDAO.addNewsletterReport(newsletterReport);
			searchBean.setSent(new Boolean(true));
			contentIds = this._newsletterManager.loadNewsletterContentIds(filters, userGroupCodes, searchBean);
			this.compareIds(contentIds, new String[] { "ART180" });
			
			searchBean.setSent(new Boolean(false));
			contentIds = this._newsletterManager.loadNewsletterContentIds(filters, userGroupCodes, searchBean);
			this.compareIds(contentIds, new String[] { });
			
			searchBean.setInQueue(new Boolean(false));
			searchBean.setSent(new Boolean(true));
			contentIds = this._newsletterManager.loadNewsletterContentIds(filters, userGroupCodes, searchBean);
			this.compareIds(contentIds, new String[] { });
			
			searchBean.setInQueue(null);
			searchBean.setSent(new Boolean(true));
			contentIds = this._newsletterManager.loadNewsletterContentIds(filters, userGroupCodes, searchBean);
			this.compareIds(contentIds, new String[] { "ART180" });
			
			searchBean.setSent(new Boolean(false));
			contentIds = this._newsletterManager.loadNewsletterContentIds(filters, userGroupCodes, searchBean);
			this.compareIds(contentIds, new String[] { });
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteNewsletters();
		}
	}
	
	public void testLoadAllNewsletterContentIds() throws Throwable {
		EntitySearchFilter[] filters = { };
		List<String> userGroupCodes = new ArrayList<String>();
		userGroupCodes.add(Group.FREE_GROUP_NAME);
		NewsletterSearchBean searchBean = new NewsletterSearchBean();
		List<String> contentIds = this._newsletterManager.loadNewsletterContentIds(filters, userGroupCodes, searchBean);
		this.compareIds(contentIds, new String[] { "ART180" });
		// imposta l'attributo per tutti i contenuti, quindi cercherà su tutti e non più per categoria
		NewsletterConfig config = this._newsletterManager.getNewsletterConfig();
		try {
			config.setAllContentsAttributeName("allContents");
			config.setActive(false);
			this._newsletterManager.updateNewsletterConfig(config);
			userGroupCodes.add("customers");
			contentIds = this._newsletterManager.loadNewsletterContentIds(filters, userGroupCodes, searchBean);
			this.compareIds(contentIds, new String[] { "ART1", "ART112", "ART102", "ART111", "ART121", "ART122", "ART179", "ART180", "ART187" });
			
			userGroupCodes.add(Group.ADMINS_GROUP_NAME);
			contentIds = this._newsletterManager.loadNewsletterContentIds(filters, userGroupCodes, searchBean);
			this.compareIds(contentIds, new String[] { "ART1", "ART112", "ART102", "ART104", "ART111", "ART120", "ART121", "ART122", "ART179", "ART180", "ART187" });
			userGroupCodes.clear();
			userGroupCodes.add(Group.FREE_GROUP_NAME);
			this._newsletterManager.addContentToQueue("ART180");
			searchBean.setInQueue(new Boolean(false));
			contentIds = this._newsletterManager.loadNewsletterContentIds(filters, userGroupCodes, searchBean);
			this.compareIds(contentIds, new String[] { "ART1", "ART121", "ART179", "ART187" });
			
			searchBean.setInQueue(new Boolean(true));
			contentIds = this._newsletterManager.loadNewsletterContentIds(filters, userGroupCodes, searchBean);
			this.compareIds(contentIds, new String[] { "ART180" });
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteNewsletters();
			this._helper.resetConfig();
		}
	}
	
	public void testBuildMailBody() throws Throwable {
		IContentManager contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
		Content content = contentManager.loadContent("ART180", true);
		String textBody = this._newsletterManager.buildMailBody(content, false);
		assertNotNull(textBody);
		String htmlBody = this._newsletterManager.buildMailBody(content, true);
		assertNotNull(htmlBody);
	}
	
	public void testSendNewsletters() throws Throwable {
		try {
			String email = JpnewsletterTestHelper.MAIL_ADDRESS;
			this._helper.addUser("editorCoach_temp", "aaa", "bbb", email, true, true);
			this._helper.addUser("editorCustomers_temp", "aaa", "bbb", email, true, true);
			NewsletterConfig config = this._newsletterManager.getNewsletterConfig();
			config.setActive(true);
			config.setHoursDelay(1);
			config.setStartScheduler(new Date(new Date().getTime()+2000));
			_newsletterManager.updateNewsletterConfig(config);
			this._newsletterManager.addContentToQueue("ART1");
			this._newsletterManager.addContentToQueue("ART102");
			this._newsletterManager.addContentToQueue("ART180");
			assertNull(this._newsletterManager.getContentReport("ART1"));
			assertNull(this._newsletterManager.getContentReport("ART102"));
			assertNull(this._newsletterManager.getContentReport("ART180"));
			
			this._newsletterManager.sendNewsletter();
			this.waitForSentNewsletter();
			
			NewsletterContentReportVO contentReport1 = this._newsletterManager.getContentReport("ART1");
			assertNotNull(contentReport1);
			assertEquals(0, contentReport1.getRecipients().size());
			NewsletterContentReportVO contentReport2 = this._newsletterManager.getContentReport("ART102");
			assertNotNull(contentReport2);
			assertEquals(0, contentReport2.getRecipients().size());
			NewsletterContentReportVO contentReport3 = this._newsletterManager.getContentReport("ART180");
			assertEquals("ART180", contentReport3.getContentId());
			assertTrue(contentReport3.getSendDate().compareTo(new Date())<0);
			assertEquals("ART180", contentReport3.getContentId());
			assertEquals("Oggetto della mail", contentReport3.getSubject());
			String textBody = contentReport3.getTextBody();
			assertFalse(textBody.contains("Header text"));
			assertFalse(textBody.contains("Footer text"));
			String htmlBody = contentReport3.getHtmlBody();
			assertFalse(htmlBody.contains("Header html"));
			assertFalse(htmlBody.contains("Footer html"));
			assertEquals(2, contentReport3.getRecipients().size());
			assertEquals(email, contentReport3.getRecipients().get("editorCoach_temp"));
			assertEquals(email, contentReport3.getRecipients().get("editorCustomers_temp"));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteUser("editorCoach_temp");
			this._helper.deleteUser("editorCustomers_temp");
			this._helper.deleteNewsletters();
			this._helper.resetConfig();
		}
	}
    
	public void testAddSubscriber() throws Throwable{
		String mail = "inesistente@inesistente.in";
		List<Subscriber> subscribers = new ArrayList<Subscriber>();
		try {
			this._newsletterManager.addSubscriber(mail);
			subscribers = this._newsletterManager.loadSubscribers();
			String mailExpected = "inesistente@inesistente.in";
			String mailActual = subscribers.get(0).getMailAddress();
			assertEquals(mailExpected, mailActual);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._newsletterManager.deleteSubscriber(mail);
		}
	}
	
	public void testActivateSubscriber() throws Throwable{
		String mail = "inesistente@inesistente.in";
		List<Subscriber> subscribers = new ArrayList<Subscriber>();
		try {
			this._newsletterManager.activateSubscriber(mail, null);// TODO token
			this._newsletterManager.deleteSubscriber(mail);
			subscribers = this._newsletterManager.loadSubscribers();
			Integer subscribersSizeExpected = 0;
			Integer subscribersSizeActual = subscribers.size();
			assertEquals(subscribersSizeExpected, subscribersSizeActual);
		} catch (Throwable t) {
			this._newsletterManager.deleteSubscriber(mail);
			throw t;
		}
	}
	
	private void compareContentReports(NewsletterReport newsletterReport, ContentReport expected, NewsletterContentReportVO received) {
		assertEquals(expected.getId(), received.getId());
		assertEquals(expected.getContentId(), received.getContentId());
		assertEquals(expected.getTextBody(), received.getTextBody());
		assertEquals(expected.getHtmlBody(), received.getHtmlBody());
		
		assertEquals(newsletterReport.getId(), received.getNewsletterId());
		assertEquals(newsletterReport.getSubject(), received.getSubject());
		assertEquals(DateConverter.getFormattedDate(newsletterReport.getSendDate(), "ddMMyyyyHHmmss"), 
				DateConverter.getFormattedDate(received.getSendDate(), "ddMMyyyyHHmmss"));
		
		Map<String, String> expectedRecipients = expected.getRecipients();
		Map<String, String> receivedRecipients = received.getRecipients();
		assertEquals(expectedRecipients.size(), receivedRecipients.size());
		for (Entry<String, String> entry : expectedRecipients.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			assertEquals(value, receivedRecipients.get(key));
		}
	}
	
	private void compareIds(List<String> received, String[] expected) {
		assertEquals(expected.length, received.size());
		for (String id : expected) {
			assertTrue(received.contains(id));
		}
	}
	
	@Override
    protected void init() throws Exception {
    	super.init();
    	try {
    		this._newsletterManager = (INewsletterManager) this.getService(JpnewsletterSystemConstants.NEWSLETTER_MANAGER);
    		NewsletterDAO newsletterDAO = new NewsletterDAO();
    		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
    		newsletterDAO.setDataSource(dataSource);
    		this._newsletterDAO = newsletterDAO;
		} catch (Exception e) {
			throw e;
		}
    }
	
	private INewsletterManager _newsletterManager;
	private INewsletterDAO _newsletterDAO;
	
}