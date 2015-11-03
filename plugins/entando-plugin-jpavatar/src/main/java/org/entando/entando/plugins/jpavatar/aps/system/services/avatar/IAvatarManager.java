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
package org.entando.entando.plugins.jpavatar.aps.system.services.avatar;

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
