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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentReportVO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterReport;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.Subscriber;

/**
 * @author E.Mezzano, A.Turrini
 */
public interface INewsletterDAO {
	
	public void addContentToQueue(String contentId);
	
	public void deleteContentFromQueue(String contentId);
	
	public List<String> loadContentQueue();
	
	public void cleanContentQueue(List<String> queue);
	
	public void addNewsletterReport(NewsletterReport newsletterReport);
	
	public NewsletterContentReportVO loadContentReport(String contentId);
	
	public List<String> loadSentContentIds();
	
	public boolean existsContentReport(String contentId);
	
	public List<Subscriber> loadSubscribers() throws ApsSystemException;
	
	public List<Subscriber> searchSubscribers(String mailAddress, Boolean active) throws ApsSystemException;
	
	public Subscriber loadSubscriber(String mailAddress) throws ApsSystemException;
	
	public void addSubscriber(Subscriber subscriber, String token) throws ApsSystemException;
	
	public void updateSubscriber(Subscriber subscriber, String token) throws ApsSystemException;
	
	public void deleteSubscriber(String mailAddress) throws ApsSystemException;
	
	public void activateSubscriber(String mailAddress) throws ApsSystemException;
	
	public String getAddressFromToken(String token);
	
	public void removeToken(String token);
	
	public void cleanOldSubscribers(Date date);
	
}