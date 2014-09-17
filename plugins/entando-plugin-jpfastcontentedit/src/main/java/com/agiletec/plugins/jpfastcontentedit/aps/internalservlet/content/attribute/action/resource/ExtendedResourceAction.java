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
package com.agiletec.plugins.jpfastcontentedit.aps.internalservlet.content.attribute.action.resource;

import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource.ResourceAttributeActionHelper;
import javax.servlet.http.HttpSession;

/**
 * @author E.Santoboni
 */
public class ExtendedResourceAction extends com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource.ExtendedResourceAction {
	
	@Override
	public String save() {
		HttpSession session = this.getRequest().getSession();
		this.setLangCode((String) session.getAttribute(ResourceAttributeActionHelper.RESOURCE_LANG_CODE_SESSION_PARAM));
		return super.save();
	}
	
	public String getLangTabAnchorDest() {
		return this.getLangCode() + "_tab";
	}
	
	protected String getLangCode() {
		return _langCode;
	}
	
	protected void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	
	private String _langCode;
	
}