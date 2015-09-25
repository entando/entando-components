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
