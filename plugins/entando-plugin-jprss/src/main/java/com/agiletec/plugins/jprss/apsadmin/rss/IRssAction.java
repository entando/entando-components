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
package com.agiletec.plugins.jprss.apsadmin.rss;

import java.util.List;

import com.agiletec.plugins.jprss.aps.system.services.rss.Channel;

/**
 * Interface for the actions that handles the operations on the Channels configuration
 */
public interface IRssAction {
	
	/**
	 * Returns the list of Channels storend in the database
	 * @return a list of all the channels
	 */
	public List<Channel> getChannels();
	
	/**
	 * Prepare the action for a new entry
	 * @return success when no error occurs
	 */
	public String newChannel();
	
	/**
	 * Save or update a channel
	 * @return success when no error occurs
	 */
	public String save();

	/**
	 * Delete a channel
	 * @return success when no error occurs
	 */
	public String delete();

	/**
	 * Prepare the form for an update operation
	 * @return success when no error occurs
	 */
	public String edit();
	
	public String trash();
	
	public static final String MOVEMENT_UP_CODE ="UP";
	public static final String MOVEMENT_DOWN_CODE ="DOWN";
	
}
