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
package com.agiletec.plugins.jpuserreg.aps.system.services.userreg;

import java.util.Collection;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model.IUserRegConfig;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * Basic interface that provides the Account Registration functionalities
 * 
 * @author S.Puddu
 * @author E.Mezzano
 * @author G.Cocco
 */
public interface IUserRegManager {
	
	/**
	 * Returns the plugin configuration.
	 * @return The plugin configuration.
	 */
	public IUserRegConfig getUserRegConfig();
	
	/**
	 * Save the plugin configuration.
	 * @param config The plugin configuration to save.
	 * @throws ApsSystemException If an error happens.
	 */
	public void saveUserRegConfig(IUserRegConfig config) throws ApsSystemException;
	
	/**
	 * Add an inactive user waiting for complete registration.
	 * @param userProfile The user profile.
	 * @throws ApsSystemException In an error happens.
	 */
	public void regAccount(IUserProfile userProfile) throws ApsSystemException;
	
	/**
	 * Activate user identified by username with password provided
	 * @param username
	 * @param password
	 * @param token 
	 * @throws ApsSystemException
	 */
	public void activateUser(String username, String password, String token) throws ApsSystemException;
	
	/**
	 * Reactivate user identified by username with password provided
	 * 
	 * @param username
	 * @param password
	 * @param token 
	 * @throws ApsSystemException
	 */
	public void reactivateUser(String username, String password, String token) throws ApsSystemException;
	
	/**
	 * Load username from associated ticket if exist
	 * @param token
	 */
	public String getUsernameFromToken(String token);
	
	public Collection<String> getUsernamesByEmail(String email) throws ApsSystemException;
	
	/**
	 * Manage reactivation request using user id
	 * 
	 * @param username
	 * */
	public void reactivationByUserName(String username) throws ApsSystemException;
	
	/**
	 * Manage reactivation request using user email
	 * 
	 * @param email 
	 * */
	public void reactivationByEmail(String email) throws ApsSystemException;
	
	/**
	 * Deactivate user
	 * 
	 * @param user
	 * */
	public void deactivateUser(UserDetails user) throws ApsSystemException;
	
	/**
	 * Remove tickets and associated disabled accounts if expired
	 * */
	void clearOldAccountRequests() throws ApsSystemException;
	
}