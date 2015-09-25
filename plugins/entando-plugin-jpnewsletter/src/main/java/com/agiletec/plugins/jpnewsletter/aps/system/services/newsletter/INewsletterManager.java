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

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentReportVO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterSearchBean;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.Subscriber;

import java.util.Collection;
import java.util.List;

/**
 * @author E.Santoboni, A.Turrini
 */
public interface INewsletterManager {
	
	public NewsletterConfig getNewsletterConfig();
	
	public void updateNewsletterConfig(NewsletterConfig config) throws ApsSystemException;
	
	public void addContentToQueue(String contentId) throws ApsSystemException;
	
	public void removeContentFromQueue(String contentId) throws ApsSystemException;
	
	public List<String> getContentQueue() throws ApsSystemException;
	
	public boolean existsContentReport(String contentId) throws ApsSystemException;
	
	public List<String> getSentContentIds() throws ApsSystemException;
	
	public NewsletterContentReportVO getContentReport(String contentId) throws ApsSystemException;
	
	public List<String> loadNewsletterContentIds(EntitySearchFilter[] filters, 
			Collection<String> userGroupCodes, NewsletterSearchBean searchBean) throws ApsSystemException;
	
	public void sendNewsletter() throws ApsSystemException;
	
	public String buildMailBody(Content content, boolean html) throws ApsSystemException;
	
	public List<Subscriber> loadSubscribers() throws ApsSystemException;
	
	public List<Subscriber> searchSubscribers(String mailAddress, Boolean active) throws ApsSystemException;
	
	public Subscriber loadSubscriber(String mailAddress) throws ApsSystemException;
	
	public void addSubscriber(String mailAddress) throws ApsSystemException;
	
	public void resetSubscriber(String mailAddress) throws ApsSystemException;
	
	public void deleteSubscriber(String mailAddress) throws ApsSystemException;
	
	public void activateSubscriber(String mailAddress, String token) throws ApsSystemException;
	
	public String getAddressFromToken(String token) throws ApsSystemException;
	
	public void cleanOldSubscribers() throws ApsSystemException;
	
	public Boolean isAlreadyAnUser(String mailAddress) throws ApsSystemException;
	
}