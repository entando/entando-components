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
package com.agiletec.plugins.jpfastcontentedit.aps.internalservlet.content.attribute.action.link;

import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.helper.ILinkAttributeActionHelper;
import javax.servlet.http.HttpSession;

/**
 * @author E.Santoboni
 */
public class UrlLinkAction extends com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.UrlLinkAction {
	
	@Override
	public String joinUrlLink() {
		HttpSession session = this.getRequest().getSession();
		this.setLangCode((String) session.getAttribute(ILinkAttributeActionHelper.LINK_LANG_CODE_SESSION_PARAM));
		return super.joinUrlLink();
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