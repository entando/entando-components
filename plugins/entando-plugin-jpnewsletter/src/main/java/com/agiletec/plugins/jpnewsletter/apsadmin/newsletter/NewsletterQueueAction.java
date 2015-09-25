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
package com.agiletec.plugins.jpnewsletter.apsadmin.newsletter;

import java.util.Date;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;

/**
 * @author E.Santoboni
 */
public class NewsletterQueueAction extends BaseAction {
	
	public List<String> getContentIds() {
		if (this._contentIds == null) {
			try {
				this._contentIds = this.getNewsletterManager().getContentQueue();
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getContents");
				throw new RuntimeException("Error loading contents", t);
			}
		}
		return this._contentIds;
	}
	
	public String removeFromQueue() {
		try {
			INewsletterManager newsletterManager = this.getNewsletterManager();
			String contentId = this.getContentId();
			if (contentId != null) {
				newsletterManager.removeContentFromQueue(contentId);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeFromQueue");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String send() {
		try {
			this.getNewsletterManager().sendNewsletter();
			this.addActionMessage(this.getText("Messages.newsletter.startedSending"));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "send");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public boolean existsContentReport(String contentId) {
		try {
			return this.getNewsletterManager().existsContentReport(contentId);
		} catch (Throwable t) {
			throw new RuntimeException("Error verifying content report existence", t);
		}
	}
	
	public ContentRecordVO getContentVo(String contentId) {
		ContentRecordVO contentVo = null;
		try {
			contentVo = this.getContentManager().loadContentVO(contentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentVo");
			throw new RuntimeException("Error loading contentvo", t);
		}
		return contentVo;
	}
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	public NewsletterConfig getNewsletterConfig() {
		return this.getNewsletterManager().getNewsletterConfig();
	}
	
	public long getHoursDelay() {
		long delay = this.getStartTimeDelay();
		return delay/3600000;
	}
	
	public long getMinutesDelay() {
		long delay = this.getStartTimeDelay();
		return (delay%3600000) / 60000;
	}
	
	public long getStartTimeDelay() {
		if (this._startTimeDelay==-1) {
			NewsletterConfig config = this.getNewsletterManager().getNewsletterConfig();
			this._startTimeDelay = config.getNextTaskTime().getTime() - new Date().getTime();
		}
		return this._startTimeDelay;
	}
	
	protected INewsletterManager getNewsletterManager() {
		return _newsletterManager;
	}
	public void setNewsletterManager(INewsletterManager newsletterManager) {
		this._newsletterManager = newsletterManager;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected List<String> _contentIds;
	private String _contentId;
	private long _startTimeDelay = -1;
	
	private INewsletterManager _newsletterManager;
	private IContentManager _contentManager;
	
}