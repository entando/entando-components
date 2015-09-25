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