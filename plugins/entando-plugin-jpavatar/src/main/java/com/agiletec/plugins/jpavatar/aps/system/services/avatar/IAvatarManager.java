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
package com.agiletec.plugins.jpavatar.aps.system.services.avatar;

import java.io.File;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interface for the service that manages the avatars
 * @author S.Puddu
 */
public interface IAvatarManager {

	/**
	 * Saves an avatar. The name of the file will be the username, while the file extension is preserved
	 * The file will be stored in the following path: value of the param "avatarDiskFolder" + /avatar 
	 * @param username String with the value of the name of the current user
	 * @param file the file to be saved
	 * @throws ApsSystemException in an error occurs
	 */
	public void saveAvatar(String username, File file, String filename) throws ApsSystemException;
	
	/**
	 * Deletes an avatar from the filesystem
	 * @param user can be a String or a UserDetails instance
	 * @throws ApsSystemException if an error occurs
	 */
	public void removeAvatar(Object user) throws ApsSystemException;

	/**
	 * Gets a string containing the avatar directory (without /avatar)
	 * @return the avatar directory 
	 */
	public String getAvatarDiskFolder();
	
	@Deprecated
	public String getAvatar(String username) throws ApsSystemException;
	
	/**
	 * Returns the url of the file associated with the username
	 * @param username the username
	 * @return the url of the file associated with the user
	 * @throws ApsSystemException if an error occurs
	 */
	public String getAvatarUrl(String username) throws ApsSystemException;
	
	/**
	 * Returns the avatar File
	 * @param username the username
	 * @return the avatar File
	 * @throws ApsSystemException if an error occurs
	 */
	public File getAvatarResource(String username) throws ApsSystemException;
	
	/**
	 * Return the service configuration bean
	 * @return
	 */
	public AvatarConfig getConfig();

	/**
	 * Update the service configuration
	 * @param config
	 * @throws ApsSystemException
	 */
	public void updateConfig(AvatarConfig config) throws ApsSystemException;

}
