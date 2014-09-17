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
package com.agiletec.plugins.jpcasclient.aps.system.services.config;

/*
 * 
<?xml version="1.0" encoding="UTF-8"?>
<casclientConfig>
	<active>false</active>
	<casLoginURL>http://japs.intranet:8080/cas/login</casLoginURL>
	<casLogoutURL>http://japs.intranet:8080/cas/logout</casLogoutURL>
	<casValidateURL>http://japs.intranet:8080/cas/validate</casValidateURL>
	<serverBaseURL>http://japs.intranet:8080</serverBaseURL>
	<notAuthPage>notauth</notAuthPage>
	<realm>demo.entando.com</realm>
</casclientConfig>
* */
public class CasClientConfig {

	
	public boolean isActive() {
		return _active;
	}
	public void setActive(boolean active) {
		this._active = active;
	}

	public String getCasLoginURL() {
		return _casLoginURL;
	}
	public void setCasLoginURL(String casLoginURL) {
		this._casLoginURL = casLoginURL;
	}

	public String getCasLogoutURL() {
		return _casLogoutURL;
	}
	public void setCasLogoutURL(String casLogoutURL) {
		this._casLogoutURL = casLogoutURL;
	}

	public String getCasValidateURL() {
		return _casValidateURL;
	}
	public void setCasValidateURL(String casValidateURL) {
		this._casValidateURL = casValidateURL;
	}

	public String getServerBaseURL() {
		return _serverBaseURL;
	}
	public void setServerBaseURL(String serverBaseURL) {
		this._serverBaseURL = serverBaseURL;
	}

	public String getNotAuthPage() {
		return _notAuthPage;
	}
	public void setNotAuthPage(String notAuthPage) {
		this._notAuthPage = notAuthPage;
	}

	public String getRealm() {
		return _realm;
	}
	public void setRealm(String realm) {
		this._realm = realm;
	}
	
	private boolean _active;
	private String _casLoginURL;
	private String _casLogoutURL;
	private String _casValidateURL;
	private String _serverBaseURL;
	private String _notAuthPage;
	private String _realm;

}