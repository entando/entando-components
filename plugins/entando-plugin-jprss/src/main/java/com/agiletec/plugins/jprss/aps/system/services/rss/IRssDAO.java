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
package com.agiletec.plugins.jprss.aps.system.services.rss;

import java.util.List;

/**
 * Interface for the Data Access Objet for the Channel object *
 */
public interface IRssDAO {

	/**
	 * Inserts a new channel into the database
	 * @param channel the channel to insert
	 */
	public void addChannel(Channel channel);

	/**
	 * Update a channel
	 * @param channel the channel to update
	 */
	public void updateChannel(Channel channel);

	/**
	 * Deletes a channel. 
	 * Cannot be reverted
	 * @param id the id of the channel
	 */
	public void deleteChannel(int id);

	/**
	 * Returns a list of all the channels having the specified status
	 * @param status the status of the Channel
	 * @return returns the list of the channels whith the status provided
	 */
	public List<Channel> getChannels(int status) throws Throwable;

	/**
	 * Returns a channel with the specified id.
	 * @param id the id of the channel
	 * @return a channel
	 */
	public Channel getChannel(int id);

}