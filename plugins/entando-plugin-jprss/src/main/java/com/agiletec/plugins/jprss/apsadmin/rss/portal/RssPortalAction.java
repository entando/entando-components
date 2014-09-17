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
package com.agiletec.plugins.jprss.apsadmin.rss.portal;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jprss.aps.system.services.rss.Channel;
import com.agiletec.plugins.jprss.aps.system.services.rss.IRssManager;
import com.sun.syndication.feed.synd.SyndFeed;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;

import org.slf4j.Logger;

public class RssPortalAction extends BaseAction implements ServletResponseAware, IRssPortalAction {
	
	@Override
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
