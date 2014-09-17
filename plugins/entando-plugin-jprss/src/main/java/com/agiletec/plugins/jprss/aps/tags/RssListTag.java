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
package com.agiletec.plugins.jprss.aps.tags;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jprss.aps.system.services.JpRssSystemConstants;
import com.agiletec.plugins.jprss.aps.system.services.rss.Channel;
import com.agiletec.plugins.jprss.aps.system.services.rss.IRssManager;

/**
 * This tag puts in the pageContext a list of all the active channels.
 */
public class RssListTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(RssListTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		IRssManager rssManager = (IRssManager) ApsWebApplicationUtils.getBean(JpRssSystemConstants.RSS_MANAGER, this.pageContext);
		try {
			List<Channel> activeChannels = rssManager.getChannels(Channel.STATUS_ACTIVE);
			this.pageContext.setAttribute(this.getListName(), activeChannels);
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error in RssListTag - doStartTag", t);
		}
		return super.doStartTag();
	}
	
	@Override
	public void release() {
		super.release();
		this.setListName(null);
	}
	
	public String getListName() {
		return _listName;
	}
	public void setListName(String listName) {
		this._listName = listName;
	}
	
	private String _listName;
	
}