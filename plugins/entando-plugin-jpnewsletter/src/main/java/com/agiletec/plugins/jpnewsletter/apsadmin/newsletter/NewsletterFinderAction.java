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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.apsadmin.content.ContentFinderAction;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentReportVO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentType;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterSearchBean;

/**
 * @author E.Santoboni
 */
public class NewsletterFinderAction extends ContentFinderAction {
	
	@Override
	public List<String> getContents() {
		List<String> result = new ArrayList<String>();
		try {
			List<String> allowedGroups = this.getContentGroupCodes();
			EntitySearchFilter[] filters = this.createFilters();
			NewsletterSearchBean searchBean = this.prepareSearchBean();
			result = this.getNewsletterManager().loadNewsletterContentIds(filters, allowedGroups, searchBean);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContents");
			throw new RuntimeException("Error loading contents", t);
		}
		return result;
	}
	
	private NewsletterSearchBean prepareSearchBean() {
		NewsletterSearchBean searchBean = new NewsletterSearchBean();
		Boolean isInQueue = null;
		Integer inQueue = this.getInQueue();
		if (inQueue!=null && inQueue.intValue()!=0) {
			isInQueue = new Boolean(inQueue.intValue()==1);
		}
		searchBean.setInQueue(isInQueue);
		
		Integer sent = this.getSent();
		Boolean isSent = null;
		if (sent!=null && sent.intValue()!=0) {
			isSent = new Boolean(sent.intValue()==1);
		}
		searchBean.setSent(isSent);
		return searchBean;
	}
	
	public String addToQueue() {
		try {
			INewsletterManager newsletterManager = this.getNewsletterManager();
			Set<String> contentIds = this.getContentIds();
			if (contentIds!=null && contentIds.size()>0) {
				List<String> newsletterQueue = newsletterManager.getContentQueue();
				for (String contentId : contentIds) {
					Content content = this.getContentManager().loadContent(contentId, true);
					if (this.checkContent(content)) {
						if (newsletterQueue.contains(contentId)) {
							this.addActionError(this.getText("Errors.newsletter.contentAlreadyAdded", new String[] { content.getDescr() }));
						} else {
							newsletterManager.addContentToQueue(contentId);
							this.addActionMessage(this.getText("Messages.newsletter.addedContent", new String[] { content.getDescr() }));
						}
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addToQueue");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String removeFromQueue() {
		try {
			INewsletterManager newsletterManager = this.getNewsletterManager();
			Set<String> contentIds = this.getContentIds();
			if (contentIds!=null && contentIds.size()>0) {
				for (String contentId : contentIds) {
					newsletterManager.removeContentFromQueue(contentId);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeFromQueue");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String getOnLineState() {
		return "yes";
	}
	
	public boolean isContentInQueue(String contentId) {
		return this.getContentQueue().contains(contentId);
	}
	
	public NewsletterContentReportVO getContentReport(String contentId) {
		try {
			return this.getNewsletterManager().getContentReport(contentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentReport");
			throw new RuntimeException("Error loading content report by id " + contentId, t);
		}
	}
	
	@Override
	public List<SmallContentType> getContentTypes() {
		List<SmallContentType> contentTypes = new ArrayList<SmallContentType>();
		List<SmallContentType> systemContentTypes = super.getContentTypes();
		Collection<String> nwlContentTypes = this.getNewsletterManager().getNewsletterConfig().getContentTypes().keySet();
		for (SmallContentType contentType : systemContentTypes) {
			if (nwlContentTypes.contains(contentType.getCode())) {
				contentTypes.add(contentType);
			}
		}
		return contentTypes;
	}
	
	private boolean checkContent(Content content) throws ApsSystemException {
		boolean allowed = false;
		if (content!=null) {
			if (!this.isUserAllowed(content)) {
				this.addActionError(this.getText("Errors.newsletter.userNotAllowed", new String[] { content.getDescr() }));
			} else if (!this.isContentAllowed(content)) {
				this.addActionError(this.getText("Errors.newsletter.contentNotAllowed", new String[] { content.getDescr() }));
			} else {
				allowed = true;
			}
		}
		return allowed;
	}
	
	private boolean isContentAllowed(Content content) throws ApsSystemException {
		NewsletterConfig newsletterConfig = this.getNewsletterManager().getNewsletterConfig();
		NewsletterContentType contentType = newsletterConfig.getContentType(content.getTypeCode());
		if (contentType != null) {
			if (this.getNewsletterManager().getNewsletterConfig().getAllContentsAttributeName() != null) {
				return true;
			}
			Set<Object> categories = newsletterConfig.getSubscriptions().keySet();
			for (Category category : content.getCategories()) {
				if (this.isContained(category, categories)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isContained(Category category, Set<Object> categories) {
		boolean contain = false;
		Category categoryTemp = category;
		while (!contain && !categoryTemp.getCode().equals(categoryTemp.getParentCode())) {
			contain = (categories.contains(categoryTemp.getCode()));
			categoryTemp = categoryTemp.getParent();
		}
		return contain;
	}
	
	public List<String> getContentQueue() {
		if (this._contentQueue == null) {
			try {
				this._contentQueue = this.getNewsletterManager().getContentQueue();
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getContentQueue");
				throw new RuntimeException("Error loading content queue", t);
			}
		}
		return this._contentQueue;
	}
	
	public Integer getInQueue() {
		return _inQueue;
	}
	public void setInQueue(Integer inQueue) {
		this._inQueue = inQueue;
	}
	
	public Integer getSent() {
		return _sent;
	}
	public void setSent(Integer sent) {
		this._sent = sent;
	}
	
	protected INewsletterManager getNewsletterManager() {
		return _newsletterManager;
	}
	public void setNewsletterManager(INewsletterManager newsletterManager) {
		this._newsletterManager = newsletterManager;
	}
	
	private List<String> _contentQueue;
	
	private Integer _inQueue;
	private Integer _sent;
	
	private INewsletterManager _newsletterManager;
	
}