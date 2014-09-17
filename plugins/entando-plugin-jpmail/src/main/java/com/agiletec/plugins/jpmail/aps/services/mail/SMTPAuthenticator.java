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
package com.agiletec.plugins.jpmail.aps.services.mail;

import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends javax.mail.Authenticator {
	
	public SMTPAuthenticator(MailConfig config) {
		if (null != config) {
			_user = config.getSmtpUserName();
			_pwd = config.getSmtpPassword();
		}
	}
	
	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(_user, _pwd);
	}
	
	private String _user;
	private String _pwd;
	
}
