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
package org.entando.entando.plugins.jprss.apsadmin.portal;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.apsadmin.system.BaseAction;
import org.entando.entando.plugins.jprss.aps.system.services.rss.Channel;
import org.entando.entando.plugins.jprss.aps.system.services.rss.IRssManager;
import com.rometools.rome.feed.synd.SyndFeed;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;

import org.slf4j.Logger;

/**
 * The action that renders the feed
 * @author S.Puddu
 */
public class RssPortalAction extends BaseAction implements ServletResponseAware {
	
	public String show() {
		Logger log = ApsSystemUtils.getLogger();
		try {
			if (null == this.getLang() || null == this.getLangManager().getLang(this.getLang())) {
				this.setLang(this.getLangManager().getDefaultLang().getCode());
				log.info("JpRssPortalAction - Setting default lang.");
			}
			if (null == this.getId() || this.getId().trim().length() == 0) {
				log.info("Channel id was null.");
				return null;
			}
			boolean isnumeric = this.isNumeric(this.getId()); 
			if (!isnumeric) {
				log.info("JpRssPortalAction - Wrong channel id.");
				return null;
			}
			int channelId = new Integer(this.getId()).intValue();
			Channel channel = this.getRssManager().getChannel(channelId);
			if (null == channel || !channel.isActive()) {
				log.info("JpRssPortalAction - Channel " + channelId + " not found");
				return null;
			}
			String feedLink = this.getFeedLink();
			SyndFeed syndFeed = this.getRssManager().getSyndFeed(channel, this.getLang(), feedLink, this.getRequest(), this.getServletResponse());
			this.setSyndFeed(syndFeed);
			this.setFeedType(channel.getFeedType());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "show");
			return FAILURE;
		}
		return SUCCESS;
	}

	private boolean isNumeric(String string) {
		Pattern p = Pattern.compile("([0-9]*)");
		Matcher m = p.matcher(string);
		return m.matches();
	}
	
	private String getFeedLink() {
		String feedLink = "";
		String url = this.getConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL);
		feedLink = url;
		return feedLink + this.getLang() + "/";
	}
	
	public void setId(String id) {
		this._id = id;
	}
	public String getId() {
		return _id;
	}
	
	public void setLang(String lang) {
		this._lang = lang;
	}
	public String getLang() {
		return _lang;
	}
	
	public void setRssManager(IRssManager rssManager) {
		this._rssManager = rssManager;
	}
	protected IRssManager getRssManager() {
		return _rssManager;
	}
	
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this._servletResponse = response;
	}
	public HttpServletResponse getServletResponse() {
		return _servletResponse;
	}
	
	public void setSyndFeed(SyndFeed syndFeed) {
		this._syndFeed = syndFeed;
	}
	public SyndFeed getSyndFeed() {
		return _syndFeed;
	}

	public void setFeedType(String feedType) {
		this._feedType = feedType;
	}
	public String getFeedType() {
		return _feedType;
	}
	
	private String _id;
	private String _lang;
	private IRssManager _rssManager;
	private ConfigInterface _configManager;
	private HttpServletResponse _servletResponse;
	private SyndFeed _syndFeed;
	private String _feedType;
	
}
