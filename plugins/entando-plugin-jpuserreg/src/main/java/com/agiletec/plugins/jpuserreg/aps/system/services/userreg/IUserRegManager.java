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