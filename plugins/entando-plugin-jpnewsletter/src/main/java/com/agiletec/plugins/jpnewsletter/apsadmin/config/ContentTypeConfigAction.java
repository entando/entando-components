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

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentType;

/**
 * @author E.Santoboni
 */
public class ContentTypeConfigAction extends AbstractNewsletterConfigAction {
	
	public String addContentTypeConfig() {
		try {
			NewsletterConfig config = this.getNewsletterConfig();
			config.addContentType(this.getNewsletterContentType());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addContentTypeConfig");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String getContentTypeCode() {
		return _contentTypeCode;
	}
	public void setContentTypeCode(String contentTypeCode) {
		if (null == this.getNewsletterContentType()) {
			NewsletterContentType config = new NewsletterContentType();
			config.setContentTypeCode(contentTypeCode);
			this.setNewsletterContentType(config);
		}
		this._contentTypeCode = contentTypeCode;
	}
	
	public NewsletterContentType getNewsletterContentType() {
		return _newsletterContentType;
	}
	public void setNewsletterContentType(NewsletterContentType newsletterContentType) {
		this._newsletterContentType = newsletterContentType;
	}
	
	protected String _contentTypeCode;
	private NewsletterContentType _newsletterContentType;
	
}