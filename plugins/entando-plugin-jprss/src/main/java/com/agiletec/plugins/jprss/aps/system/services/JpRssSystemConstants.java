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
package com.agiletec.plugins.jprss.aps.system.services;

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
	
	/**
	 * Value of the item column inside the table sysconfig in the datamse  *port
	 * userd to load the configuration patameters.
	 */
	public static final String ITEM_CONFIG_NAME = "jprss_config";
	
	public static final String ATTRIBUTE_ROLE_RSS_CONTENT_TITLE = "jprss:title";
	
	public static final String ATTRIBUTE_ROLE_RSS_CONTENT_DESCRIPTION = "jprss:description";
	
}