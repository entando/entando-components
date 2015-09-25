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
package com.agiletec.plugins.jpnewsletter.apsadmin.subscriber;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.Subscriber;

/**
 * @author A.Turrini
 */
public class SubscriberFinderAction extends BaseAction {
	
	public List<Subscriber> getSubscribers() {
		List<Subscriber> subscribers = new ArrayList<Subscriber>();
		try {
			Integer active = this.getInsertedActive();
			Boolean boolActive = active == null ? null : active.intValue()==1;
			subscribers = this.getNewsletterManager().searchSubscribers(this.getInsertedMailAddress(), boolActive);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSubscribers");
		}
		return subscribers;
	}
	
	public String getInsertedMailAddress() {
		return _insertedMailAddress;
	}
	public void setInsertedMailAddress(String insertedMailAddress) {
		_insertedMailAddress = insertedMailAddress;
	}
	
	public Integer getInsertedActive() {
		return _insertedActive;
	}
	public void setInsertedActive(Integer insertedActive) {
		_insertedActive = insertedActive;
	}
	
	protected INewsletterManager getNewsletterManager() {
		return _newsletterManager;
	}
	public void setNewsletterManager(INewsletterManager newsletterManager) {
		_newsletterManager = newsletterManager;
	}
	
	private String _insertedMailAddress;
	private Integer _insertedActive;
	
	private INewsletterManager _newsletterManager;
	
}