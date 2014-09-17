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
        cl.setAuthorities(this.getAuthorities());
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