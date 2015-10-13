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
package org.entando.entando.plugins.jprss.aps.system.services.rss;

import java.util.List;

/**
 * Interface for the Data Access Objet for the Channel object *
 * @author S.Puddu
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
	 * @throws Throwable in case of error
	 */
	public List<Channel> getChannels(int status) throws Throwable;
	
	/**
	 * Returns a channel with the specified id.
	 * @param id the id of the channel
	 * @return a channel
	 */
	public Channel getChannel(int id);
	
}