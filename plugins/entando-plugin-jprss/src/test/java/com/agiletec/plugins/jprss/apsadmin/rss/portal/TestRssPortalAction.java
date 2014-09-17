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

import java.util.Iterator;
import java.util.List;

import com.agiletec.plugins.jprss.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jprss.aps.system.services.JpRssSystemConstants;
import com.agiletec.plugins.jprss.aps.system.services.rss.Channel;
import com.agiletec.plugins.jprss.aps.system.services.rss.IRssManager;
import com.opensymphony.xwork2.Action;

public class TestRssPortalAction extends ApsAdminPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testShowAction() throws Throwable {
		Channel channel = this.createTestChannel("title", "descr", true);
		this.getRssManager().addChannel(channel);
		this.initAction(NAMESPACE, "show");
		this.addParameter("id", channel.getId());
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	private Channel createTestChannel(String title, String descr, boolean active) {
		Channel channel = new Channel();
		channel.setActive(active);
		channel.setCategory("cat1");
		channel.setContentType("ART");
		channel.setDescription(descr);
		channel.setFeedType("rss_2.0");
		channel.setTitle(title);
		return channel;
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		List<Channel> channels = this.getRssManager().getChannels(Channel.STATUS_ALL);
		if (null != channels && !channels.isEmpty()) {
			Iterator<Channel> it = channels.iterator();
			while (it.hasNext()) {
				Channel current = it.next();
				this.getRssManager().deleteChannel(current.getId());
			}
		}
		channels = this.getRssManager().getChannels(Channel.STATUS_ALL);
		assertTrue(channels.isEmpty());
	}

	private void init() {
		_rssManager = (IRssManager) this.getService(JpRssSystemConstants.RSS_MANAGER);
	}
	
	public IRssManager getRssManager() {
		return _rssManager;
	}

	private IRssManager _rssManager;
	private static final String NAMESPACE = "/do/jprss/Rss/Feed";
}
