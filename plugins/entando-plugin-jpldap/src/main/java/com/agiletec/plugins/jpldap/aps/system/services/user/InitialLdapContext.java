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
package com.agiletec.plugins.jpldap.aps.system.services.user;

import java.util.Hashtable;
import javax.naming.NamingException;
import javax.naming.ldap.Control;
import javax.naming.ldap.ExtendedRequest;
import javax.naming.ldap.ExtendedResponse;

/**
 * @author E.Santoboni
 */
public class InitialLdapContext extends javax.naming.ldap.InitialLdapContext {
	
	public InitialLdapContext(Hashtable<?, ?> hshtbl, Control[] cntrls) throws NamingException {
		super(hshtbl, cntrls);
	}
	
	@Override
	public ExtendedResponse extendedOperation(ExtendedRequest er) throws NamingException {
		ExtendedResponse extResponse = super.extendedOperation(er);
		this.setExtendedResponse(extResponse);
		return extResponse;
	}
	
	protected ExtendedResponse getExtendedResponse() {
		return _extendedResponse;
	}
	protected void setExtendedResponse(ExtendedResponse extendedResponse) {
		this._extendedResponse = extendedResponse;
	}
	
	private ExtendedResponse _extendedResponse;
}
