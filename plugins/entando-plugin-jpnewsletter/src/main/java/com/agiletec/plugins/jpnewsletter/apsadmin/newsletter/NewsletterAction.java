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
public class NewsletterAction extends BaseAction {
	
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