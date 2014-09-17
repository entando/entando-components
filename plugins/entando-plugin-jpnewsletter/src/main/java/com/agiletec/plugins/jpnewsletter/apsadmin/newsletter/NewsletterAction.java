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

import java.util.Set;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentReportVO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentType;

/**
 * @author E.Santoboni
 */
public class NewsletterAction extends BaseAction implements INewsletterAction {
	
	@Override
	public String entry() {
		try {
			Content content = this.getContent();
			if (!this.checkContent(content)) {
				return "userNotAllowed";
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "entry");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Verifica l'accesso al contenuto dato.
	 * @param content Il contenuto per cui si vuole gestire la newsletter.
	 * @return true se l'accesso è consentito, false in caso contrario.
	 * @throws ApsSystemException
	 */
	private boolean checkContent(Content content) throws ApsSystemException {
		boolean allowed = false;
		if (content != null) {
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
	
	/**
	 * Verifica se l'utente corrente è abilitato all'accesso 
	 * del contenuto specificato.
	 * @param content Il contenuto su cui verificare il permesso di accesso.
	 * @return True se l'utente corrente è abilitato all'eccesso al contenuto,
	 * false in caso contrario.
	 */
	protected boolean isUserAllowed(Content content) {
		String group = content.getMainGroup();
		return this.isCurrentUserMemberOf(group);
	}
	
	public NewsletterContentReportVO getContentReport() {
		if (this._contentReport==null) {
			try {
				this._contentReport = this.getNewsletterManager().getContentReport(this.getContentId());
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getContentReport");
				throw new RuntimeException("Error loading content report", t);
			}
		}
		return _contentReport;
	}
	
	public Content getContent() {
		if (this._content==null) {
			try {
				this._content = (Content) this.getContentManager().loadContent(this.getContentId(), true);
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getContent");
			}
		}
		return _content;
	}
	
	public ContentRecordVO getContentVo() {
		if (this._contentVo==null) {
			try {
				this._contentVo = this.getContentManager().loadContentVO(this.getContentId());
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getContentVo");
			}
		}
		return _contentVo;
	}
	
	public String getBodyPreview(boolean html) {
		String mailBody = null;
		try {
			Content content = this.getContent();
			mailBody = this.getNewsletterManager().buildMailBody(content, html);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getBodyPreview");
		}
		return mailBody;
	}
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	public boolean isAlsoHtml() {
		return _alsoHtml;
	}
	public void setAlsoHtml(boolean alsoHtml) {
		this._alsoHtml = alsoHtml;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected INewsletterManager getNewsletterManager() {
		return _newsletterManager;
	}
	public void setNewsletterManager(INewsletterManager newsletterManager) {
		this._newsletterManager = newsletterManager;
	}
	
	private String _contentId;
	private boolean _alsoHtml;
	
	private NewsletterContentReportVO _contentReport;
	private Content _content;
	private ContentRecordVO _contentVo;
	
	private IContentManager _contentManager;
	private INewsletterManager _newsletterManager;
	
}