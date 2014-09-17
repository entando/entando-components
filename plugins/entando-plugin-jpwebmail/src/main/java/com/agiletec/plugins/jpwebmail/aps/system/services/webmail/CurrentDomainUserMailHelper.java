/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebmail.aps.system.services.webmail;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Default Helper class of the current user.
 * The helper class uses the WebmailManager for the construction of the current email address; 
 * email address is constructed as a concatenation of the username and domain name (returned by WebMail Manager).
 * @version 1.0
 * @author E.Santoboni
 */
public class CurrentDomainUserMailHelper implements IUserMailHelper {
	
	@Override
	public String getEmailAddress(UserDetails currentUser) throws ApsSystemException {
		StringBuffer buffer = new StringBuffer();
		buffer.append(currentUser.toString());
		buffer.append(" <").append(currentUser.getUsername()).append("@").append(this.getWebMailManager().getDomainName()).append(">");
		return buffer.toString();
	}
	
	protected IWebMailManager getWebMailManager() {
		return _webMailManager;
	}
	public void setWebMailManager(IWebMailManager webMailManager) {
		this._webMailManager = webMailManager;
	}
	
	private IWebMailManager _webMailManager;
	
}