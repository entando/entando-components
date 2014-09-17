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
package com.agiletec.plugins.jpavatar.apsadmin.avatar;

/**
 * Interface for the actions designed to manage the avatar.
 *
 */
public interface IAvatarAction {
	
	/**
	 * Edit the current avatar
	 * @return success
	 */
	public String edit();

	/**
	 * Save an avatar
	 * @return success
	 */
	public String save();

	/**
	 * Ask confirm before delete an avatar
	 * @return success
	 */
	public String bin();
	
	/**
	 * Delete the current avatar from the filesystem
	 * @return success
	 */
	public String delete();

}
