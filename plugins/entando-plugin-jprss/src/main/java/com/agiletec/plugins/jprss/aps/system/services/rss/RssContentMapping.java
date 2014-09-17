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
package com.agiletec.plugins.jprss.aps.system.services.rss;

/**
 * This object holds the configuration for mapping a feed with a content type
 */
public class RssContentMapping {
	
	@Override
	public RssContentMapping clone() {
		RssContentMapping clone = new RssContentMapping();
		clone.setContentType(this.getContentType());
		clone.setDescriptionAttributeName(this.getDescriptionAttributeName());
		clone.setTitleAttributeName(this.getTitleAttributeName());
		return clone;
	}
	
	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	public String getTitleAttributeName() {
		return _titleAttributeName;
	}
	public void setTitleAttributeName(String titleAttributeName) {
		this._titleAttributeName = titleAttributeName;
	}
	
	public String getDescriptionAttributeName() {
		return _descriptionAttributeName;
	}
	public void setDescriptionAttributeName(String descriptionAttributeName) {
		this._descriptionAttributeName = descriptionAttributeName;
	}
	
	private String _contentType;
	private String _titleAttributeName;
	private String _descriptionAttributeName;
	
}
