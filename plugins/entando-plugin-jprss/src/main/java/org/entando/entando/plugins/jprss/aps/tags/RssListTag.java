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
package org.entando.entando.plugins.jprss.aps.tags;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.entando.entando.plugins.jprss.aps.system.services.JpRssSystemConstants;
import org.entando.entando.plugins.jprss.aps.system.services.rss.Channel;
import org.entando.entando.plugins.jprss.aps.system.services.rss.IRssManager;

/**
 * This tag puts in the pageContext a list of all the active channels.
 * @author S.Puddu
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