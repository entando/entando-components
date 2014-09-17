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
package com.agiletec.plugins.jpnewsletter.apsadmin.config;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentType;

/**
 * @author E.Santoboni
 */
public class ContentTypeConfigAction extends AbstractNewsletterConfigAction implements IContentTypeConfigAction {
	
	@Override
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