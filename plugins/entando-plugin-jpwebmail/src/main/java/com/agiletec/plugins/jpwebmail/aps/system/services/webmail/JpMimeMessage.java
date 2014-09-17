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

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * Wrapper of MimeMessage
 * @author E.Santoboni
 */
public class JpMimeMessage extends MimeMessage {
	
	protected JpMimeMessage(Session session) {
		super(session);
		this._session = session;
	}
	
	protected Session getSession() {
		return _session;
	}
	
	private Session _session;
	
}
