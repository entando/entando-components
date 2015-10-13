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

import java.util.Iterator;
import java.util.List;

import org.entando.entando.plugins.jprss.apsadmin.ApsAdminPluginBaseTestCase;
import org.entando.entando.plugins.jprss.aps.system.services.JpRssSystemConstants;
import org.entando.entando.plugins.jprss.aps.system.services.rss.Channel;
import org.entando.entando.plugins.jprss.aps.system.services.rss.IRssManager;
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
