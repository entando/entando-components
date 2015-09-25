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