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
package com.agiletec.plugins.jpwebmail.aps.system.services.addressbook.model;

/**
 * @author E.Santoboni
 */
public class AddressBookContact {
	
	public String getFullName() {
		return _fullName;
	}
	public void setFullName(String fullName) {
		this._fullName = fullName;
	}
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	public String getEmailAddress() {
		return _emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this._emailAddress = emailAddress;
	}
	
	private String _fullName;
	private String _username;
	private String _emailAddress;
	
}