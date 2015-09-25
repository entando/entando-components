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
package com.agiletec.plugins.jpnewsletter.apsadmin.config;

import java.util.List;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;

/**
 * @author E.Santoboni
 */
public class AbstractNewsletterConfigAction extends BaseAction {
	
	public NewsletterConfig getNewsletterConfig() {
		return (NewsletterConfig) this.getRequest().getSession().getAttribute(NEWSLETTER_CONFIG_SESSION_PARAM);
	}
	public void setNewsletterConfig(NewsletterConfig newsletterConfig) {
		this.getRequest().getSession().setAttribute(NEWSLETTER_CONFIG_SESSION_PARAM, newsletterConfig);
	}
	
	public SmallContentType getSmallContentType(String typeCode) {
		return this.getContentManager().getSmallContentTypesMap().get(typeCode);
	}
	
	public ContentModel getContentModel(int modelId) {
		return this.getContentModelManager().getContentModel(modelId);
	}
	
	public List<ContentModel> getContentModelByType(String typeCode) {
		return this.getContentModelManager().getModelsForContentType(typeCode);
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
	
	protected IContentModelManager getContentModelManager() {
		return _contentModelManager;
	}
	public void setContentModelManager(IContentModelManager contentModelManager) {
		this._contentModelManager = contentModelManager;
	}
	
	private INewsletterManager _newsletterManager;
	private IContentManager _contentManager;
	private IContentModelManager _contentModelManager;
	
	public static String NEWSLETTER_CONFIG_SESSION_PARAM = "newsletterConfig_sessionParam";
	
}