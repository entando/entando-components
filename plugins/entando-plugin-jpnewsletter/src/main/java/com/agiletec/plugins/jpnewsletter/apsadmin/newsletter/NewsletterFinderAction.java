/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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

public class NewsletterFinderAction extends ContentFinderAction implements INewsletterFinderAction {
	
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
	
	@Override
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
	
	@Override
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
	
	/**
	 * @return il valore corrispondente allo stato "onLine"
	 */
	@Override
	public String getOnLineState() {
		return "yes";
	}
	
	public boolean isContentInQueue(String contentId) {
		return this.getContentQueue().contains(contentId);
	}
	
	/**
	 * Restituisce il riepilogo della newsletter per il contenuto di id dato.
	 * @param contentId L'id del contenuto per cui recuperare l'invio della newsletter.
	 * @return Il riepilogo della newsletter per il contenuto di id dato.
	 */
	public NewsletterContentReportVO getContentReport(String contentId) {
		try {
			return this.getNewsletterManager().getContentReport(contentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentReport");
			throw new RuntimeException("Error loading content report by id " + contentId, t);
		}
	}
	
	/**
	 * Restituisce i tipi di contenuto per cui è possibile inviare una newsletter.
	 * @return La lista dei tipi di contenuto.
	 */
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
	
	/**
	 * Verifica l'accesso al contenuto dato.
	 * @param content Il contenuto per cui si vuole gestire la newsletter.
	 * @return true se l'accesso è consentito, false in caso contrario.
	 * @throws ApsSystemException
	 */
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
	
	/**
	 * Verifica l'accesso al contenuto dato.
	 * @param content Il contenuto per cui si vuole gestire la newsletter.
	 * @return true se l'accesso è consentito, false in caso contrario.
	 * @throws ApsSystemException
	 */
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