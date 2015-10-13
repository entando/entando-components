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

import com.agiletec.aps.system.services.category.Category;

/**
 * This class is the feed configuration stored in the database.
 * @author S.Puddu
 */
public class Channel {
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	
	/**
	 * The title of the Channel.
	 * This string is used as the title of the feed
	 * @return the title of the channel
	 */
	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
	}
	
	/**
	 * The description of the channel
	 * This string is used as the description of the feed
	 * @return the description of the channel
	 */
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
	
	/**
	 * When a channel is active it can be invoked by the {@link JpRssServlet}.
	 * @return the status of this channel.
	 */
	public boolean isActive() {
		return _active;
	}
	public void setActive(boolean active) {
		this._active = active;
	}
	
	/**
	 * ContentType defined for this channel
	 * @return the contentType configured for this channel
	 */
	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	/**
	 * Returns the filters configured for this channel
	 * @return the filters configured for this channel
	 */
	public String getFilters() {
		return _filters;
	}
	public void setFilters(String filters) {
		this._filters = filters;
	}
	
	/**
	 * Is the feedType (es rss_2.0)
	 * @return the code of the feed type
	 */
	public String getFeedType() {
		return _feedType;
	}
	public void setFeedType(String feedType) {
		this._feedType = feedType;
	}
	
	/**
	 * The {@link Category} associated. It can be null.
	 * If not the contents well be filtered by this category
	 * @return the category configured for this channel
	 */
	public String getCategory() {
		return _category;
	}
	public void setCategory(String _category) {
		this._category = _category;
	}
	
	public int getMaxContentsSize() {
		return _maxContentsSize;
	}
	public void setMaxContentsSize(int maxContentsSize) {
		this._maxContentsSize = maxContentsSize;
	}
	
	private int _id;
	private String _title;
	private String _description;
	private boolean _active;
	private String _contentType;
	private String _category;
	private String _filters;
	private String _feedType;
	private int _maxContentsSize = -1;
	
	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_NOT_ACTIVE = 2;
	public static final int STATUS_ALL = 3;
	
}