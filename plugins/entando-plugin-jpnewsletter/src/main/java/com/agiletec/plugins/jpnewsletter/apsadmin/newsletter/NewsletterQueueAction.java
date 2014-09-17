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

import java.util.Date;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;

public class NewsletterQueueAction extends BaseAction implements INewsletterQueueAction {
	
	@Override
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
	
	@Override
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
	
	@Override
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
	
	/**
	 * Indica se esiste una newsletter per il contenuto di id dato.
	 * @param contentId L'id del contenuto per cui verificare l'invio della newsletter.
	 * @return true se la newsletter risulta inviata, false in caso contrario.
	 */
	public boolean existsContentReport(String contentId) {
		try {
			return this.getNewsletterManager().existsContentReport(contentId);
		} catch (Throwable t) {
			throw new RuntimeException("Error verifying content report existence", t);
		}
	}
	
	/**
	 * Restituisce il contenuto vo in base all'identificativo.
	 * Metodo a servizio dell'interfaccia di gestione 
	 * contenuti singoli ed in lista.
	 * @param contentId L'identificativo del contenuto.
	 * @return Il contenuto vo cercato.
	 */
	public ContentRecordVO getContentVo(String contentId) {
		ContentRecordVO contentVo = null;
		try {
			contentVo = this.getContentManager().loadContentVO(contentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentVo");
			throw new RuntimeException("Errore in caricamento contenuto vo", t);
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