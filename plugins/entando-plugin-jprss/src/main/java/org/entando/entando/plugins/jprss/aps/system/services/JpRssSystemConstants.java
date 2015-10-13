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
package org.entando.entando.plugins.jprss.aps.system.services;

public interface JpRssSystemConstants {
	
	/**
	 * Bean name of the service that manage the channel objects
	 */
	public static final String RSS_MANAGER = "jprssRssManager";
	
	public static final String FEED_TYPE_RSS_090 = "rss_0.90";
	public static final String FEED_TYPE_RSS_091 = "rss_0.91";
	public static final String FEED_TYPE_RSS_092 = "rss_0.92";
	public static final String FEED_TYPE_RSS_093 = "rss_0.93";
	public static final String FEED_TYPE_RSS_094 = "rss_0.94";
	public static final String FEED_TYPE_RSS_10 = "rss_1.0";
	public static final String FEED_TYPE_RSS_20 = "rss_2.0";
	public static final String FEED_TYPE_ATOM_03 = "atom_0.3";
	
	public static final String SYNDCONTENT_TYPE_TEXTHTML = "text/html";
	
	public static final String ITEM_CONFIG_NAME = "jprss_config";
	
	public static final String ATTRIBUTE_ROLE_RSS_CONTENT_TITLE = "jprss:title";
	
	public static final String ATTRIBUTE_ROLE_RSS_CONTENT_DESCRIPTION = "jprss:description";
	
}