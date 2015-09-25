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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import com.agiletec.plugins.jpnewsletter.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpnewsletter.aps.system.JpnewsletterSystemConstants;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterDAO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.NewsletterDAO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.ContentReport;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentReportVO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterReport;

public class TestNewsletterDAO extends ApsPluginBaseTestCase {
	
	public void testAddLoadDeleteCleanContentQueue() throws Throwable {
		try {
			this._helper.setNewsletterManagerThreadDelay(10000, false, null, this._newsletterManager);
			
			List<String> queue = this._newsletterDAO.loadContentQueue();
			assertEquals(0, queue.size());
			this._newsletterDAO.addContentToQueue("id1");
			this._newsletterDAO.addContentToQueue("id2");
			this._newsletterDAO.addContentToQueue("id3");
			this._newsletterDAO.addContentToQueue("id4");
			queue = this._newsletterDAO.loadContentQueue();
			assertEquals(4, queue.size());
			queue.contains("id1");
			queue.contains("id2");
			queue.contains("id3");
			queue.contains("id4");
			
			this._newsletterDAO.deleteContentFromQueue("id1");
			this._newsletterDAO.deleteContentFromQueue("id3");
			queue = this._newsletterDAO.loadContentQueue();
			assertEquals(2, queue.size());
			queue.contains("id2");
			queue.contains("id4");
			
			this._newsletterDAO.addContentToQueue("id5");
			this._newsletterDAO.cleanContentQueue(queue);
			queue = this._newsletterDAO.loadContentQueue();
			assertEquals(1, queue.size());
			queue.contains("id5");
			
			this._newsletterDAO.deleteContentFromQueue("id5");
			queue = this._newsletterDAO.loadContentQueue();
			assertEquals(0, queue.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteNewsletters();
			this._helper.resetConfig();
		}
	}
	
	public void testAddNewsletterReport() throws Throwable {
		Date date = new Date();
		NewsletterReport newsletterReport1 = this._helper.createNewsletterReport(1, new Date(), "subject");
		ContentReport contentReport1 = this._helper.createContentReport(1, newsletterReport1.getId(), 
				"ART1", "textBody1", "htmlBody1");
		contentReport1.addRecipient("user1", "mail1@address.it");
		contentReport1.addRecipient("user2", "mail2@address.it");
		newsletterReport1.addContentReport(contentReport1);
		ContentReport contentReport2 = this._helper.createContentReport(2, newsletterReport1.getId(), 
				"ART102", "textBody2", "htmlBody2");
		contentReport2.addRecipient("user1", "mail1@address.it");
		newsletterReport1.addContentReport(contentReport2);
		
		NewsletterReport newsletterReport2 = this._helper.createNewsletterReport(2, new Date(date.getTime()+100), "subject");
		ContentReport contentReport3 = this._helper.createContentReport(3, newsletterReport2.getId(), 
				"ART1", "textBody1", "htmlBody1");
		newsletterReport2.addContentReport(contentReport3);
		ContentReport contentReport4 = this._helper.createContentReport(4, newsletterReport2.getId(), 
				"ART102", "textBody2", "htmlBody2");
		newsletterReport2.addContentReport(contentReport4);
		
		try {
			this._newsletterDAO.addNewsletterReport(newsletterReport1);
			NewsletterContentReportVO report1 = this._newsletterDAO.loadContentReport("ART1");
			this.compareContentReports(newsletterReport1, contentReport1, report1);
			NewsletterContentReportVO report2 = this._newsletterDAO.loadContentReport("ART102");
			this.compareContentReports(newsletterReport1, contentReport2, report2);
			
			this._newsletterDAO.addNewsletterReport(newsletterReport2);
			NewsletterContentReportVO report3 = this._newsletterDAO.loadContentReport("ART1");
			this.compareContentReports(newsletterReport2, contentReport3, report3);
			NewsletterContentReportVO report4 = this._newsletterDAO.loadContentReport("ART102");
			this.compareContentReports(newsletterReport2, contentReport4, report4);
			
			List<String> contentIds = this._newsletterDAO.loadSentContentIds();
			this.compareIds(contentIds, new String[] { "ART1", "ART102" });

			assertTrue(this._newsletterDAO.existsContentReport("ART1"));
			assertTrue(this._newsletterDAO.existsContentReport("ART102"));
			assertFalse(this._newsletterDAO.existsContentReport("ART104"));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteNewsletters();
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
    		NewsletterDAO newsletterDAO = new NewsletterDAO();
    		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
    		newsletterDAO.setDataSource(dataSource);
    		this._newsletterDAO = newsletterDAO;
    		this._newsletterManager = (INewsletterManager) this.getService(JpnewsletterSystemConstants.NEWSLETTER_MANAGER);
		} catch (Exception e) {
			throw e;
		}
    }
    
	private INewsletterDAO _newsletterDAO;
	private INewsletterManager _newsletterManager;
	
}