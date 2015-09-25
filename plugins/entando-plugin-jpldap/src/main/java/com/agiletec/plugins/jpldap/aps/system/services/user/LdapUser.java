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

import javax.naming.directory.Attributes;

import com.agiletec.aps.system.services.user.AbstractUser;

/**
 * A UserDetails implementation which is used internally by the Ldap services. 
 * It contains a set of attributes that have been retrieved from the Ldap server.
 * @author E.Santoboni
 */
public class LdapUser extends AbstractUser {
    
    public LdapUser() {}
    
	@Override
	public boolean isEntandoUser() {
		return false;
	}
    
    @Override
	@Deprecated
    public boolean isJapsUser() {
        return this.isEntandoUser();
    }
    
    /**
     * Crea una copia dell'oggetto user e lo restituisce.
     * @return Oggetto di tipo User clonato.
     */
	@Override
    public Object clone() {
        LdapUser cl = new LdapUser();
        cl.setUsername(this.getUsername());
        cl.setPassword("");
        cl.setAuthorizations(this.getAuthorizations());
        cl.setAttributes(this.getAttributes());
        return cl;
    }
    
    /**
     * Return the attributes that have been retrieved from the Ldap server.
     * @return The ldap Attributes.
     */
    public Attributes getAttributes() {
        return _attributes;
    }

    /**
     * Set the attributes that have been retrieved from the Ldap server.
     * @param attributes The ldap Attributes.
     */
    protected void setAttributes(Attributes attributes) {
        this._attributes = attributes;
    }
    
    private Attributes _attributes;
	
}