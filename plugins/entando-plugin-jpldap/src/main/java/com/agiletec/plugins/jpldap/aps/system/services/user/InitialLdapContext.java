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
