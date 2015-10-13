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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.rometools.rome.feed.synd.SyndFeed;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class manages all the CRUD operations on the {@link Channel} object and
 * generates the xml for the rss.
 * @author S.Puddu
 */
public interface IRssManager {

	/**
	 * Add a new Channel object
	 * @param channel the channel to insert.
	 * @throws ApsSystemException if an error occurs
	 */
	public void addChannel(Channel channel) throws ApsSystemException;

	/**
	 * Delete a channel.
	 * @param id the code of the channel do delete.
	 * @throws ApsSystemException if an error occurs
	 */
	public void deleteChannel(int id) throws ApsSystemException;

	/**
	 * Update a channel object
	 * @param channel the object to update
	 * @throws ApsSystemException if an error occurs
	 */
	public void updateChannel(Channel channel) throws ApsSystemException;

	/**
	 * Load the entire list o channels.
	 * @param status value used to filter the list. The values for the status 
	 * are: 1:active 2:not active 3:both
	 * @return the list of the channel filtered by the status.
	 * @throws ApsSystemException if an error occurs
	 */
	public List<Channel> getChannels(int status) throws ApsSystemException;

	/**
	 * Returns a Map [code, descr] of all the contentTypes configured to by
	 * exported in rss format
	 * @return a Map [code, descr] of all the contentTypes
	 */
	public Map<String, String> getAvailableContentTypes();

	/**
	 * Returns a map with all the feedTypes that con be used. these values are
	 * stored in the spring definition of this service
	 * @return Returns a map with all the feedTypes that con be used
	 */
	public Map<String, String> getAvailableFeedTypes();

	public RssContentMapping getContentMapping(String typeCode);

	/**
	 * Returns a channel.
	 * @param id the code of the channel
	 * @return the channel object identified by the provided id.
	 * @throws ApsSystemException if an error occurs
	 */
	public Channel getChannel(int id) throws ApsSystemException;

	/**
	 * Build {@link SyndFeed} according to the params provided. This object is
	 * the one the the rssServlet uses to print data in response.
	 * @param channel The target channel object.
	 * @param lang the code of the lang that will be used retrieve the contents
	 * @param feedLink il link completo, generato dalla sevlet dell'oggetto syndFeed.
	 * @param req the request
	 * @param resp the response
	 * @return a SyndFeed according to the params provided
	 * @throws ApsSystemException if an error occurs
	 */
	public SyndFeed getSyndFeed(Channel channel, String lang, String feedLink, HttpServletRequest req, HttpServletResponse resp) throws ApsSystemException;
	
}
