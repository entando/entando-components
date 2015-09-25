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
package com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * Transfer Object from Struts action to Manager
 * @author G.Cocco
 */
public class UserRegBean {
	// TODO Questa classe dovrà morire quanto prima, non appena potrà essere rimosso IUserRegManager.regAccount(UserRegBean regAccountBean)
	
	public void setUsername(String username) {
		this._username = username;
	}
	public String getUsername() {
		return _username;
	}
	
	public void setUserProfile(IUserProfile userProfile) {
		this._userProfile = userProfile;
	}
	public IUserProfile getUserProfile() {
		return _userProfile;
	}
	
	private String _username;
	private IUserProfile _userProfile;
	
}