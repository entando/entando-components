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
package com.agiletec.plugins.jpcasclient.aps.system.common;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.ICasClientConfigManager;

/**
 * 
 * Some utilities for manage Principal/Realm Names
 * 
 * @author G.Cocco
 * */
public class AuthCommon {
	
	/**
	 * Extract username from full principal name
	 * Example admin from admin@MYDOMAIN.LOCAL
	 * */
	public String getUsernameFromPrincipal(String remoteUser) {

    	int lenght = remoteUser.length();
    	int at = remoteUser.indexOf('@');
    	
    	String realmDomain = remoteUser.substring(at, lenght);
    	if (this.verifyRealm(realmDomain)) {
    		String temp = remoteUser.substring(0, at);
    		ApsSystemUtils.getLogger().trace(" Username extracted from remoteUser: " + remoteUser + " username: " + temp);
    		return temp;
    	}
    	ApsSystemUtils.getLogger().trace(" NO Username extracted from remoteUser: " + remoteUser);
		return null;
	}

	/**
	 * Return true if the suplied name contains @,
	 * so it has principal format
	 * */
	public boolean hasRealmDomainInformation(String remoteUser) {
		int at = remoteUser.indexOf('@');
		if (at == -1 ) {
			return false;
		}
		return true;
	}

	private boolean verifyRealm(String realmDomain) {
		
		String realm = this.getCasConfigManager().getClientConfig().getRealm();
		this.setRealmDomain(realm);
		ApsSystemUtils.getLogger().trace(" verifyRealm with Realm Domain conf: " + this.getRealmDomain() + " for Realm: " + realmDomain);
		if (realmDomain.equals('@' + this.getRealmDomain())) {
			return true;
		}
		return false;
	}
	
	public void setRealmDomain(String realmDomain) {
		_realmDomain = realmDomain;
	}
	public String getRealmDomain() {
		return _realmDomain;
	}


	public ICasClientConfigManager getCasConfigManager() {
		return _casConfigManager;
	}
	public void setCasConfigManager(ICasClientConfigManager casConfigManager) {
		this._casConfigManager = casConfigManager;
	}


	private String _realmDomain;
	private ICasClientConfigManager _casConfigManager;
	
}
