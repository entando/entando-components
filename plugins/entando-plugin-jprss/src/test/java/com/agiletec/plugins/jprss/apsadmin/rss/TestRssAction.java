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
package com.agiletec.plugins.jprss.apsadmin.rss;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jprss.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jprss.aps.system.services.JpRssSystemConstants;
import com.agiletec.plugins.jprss.aps.system.services.rss.Channel;
import com.agiletec.plugins.jprss.aps.system.services.rss.IRssManager;
import com.agiletec.plugins.jprss.apsadmin.rss.RssAction;
import com.opensymphony.xwork2.Action;

public class TestRssAction extends ApsAdminPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testNew() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NAMESPACE, "new");
		
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		RssAction action = (RssAction) this.getAction();
		assertEquals(1, action.getStrutsAction());
		Map<String, String> availableCntTypes = action.getAvailableContentTypes();
		assertTrue(availableCntTypes.containsKey("ART"));
		assertEquals(1, availableCntTypes.size());
	}
	
	@SuppressWarnings("unchecked")
	public void testSelectContentType() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NAMESPACE, "selectContentType");
		this.addParameter("strutsAction", "1");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		
		Map fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertTrue(fieldErrors.containsKey("contentType"));
		
		this.initAction(NAMESPACE, "selectContentType");
		this.addParameter("strutsAction", "1");
		this.addParameter("contentType", "ART");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		
		RssAction action = (RssAction) this.getAction();
		Map<String, String> feedTypes = action.getAvailableFeedTypes();
		assertNotNull(feedTypes);
		assertEquals(8, feedTypes.size());
	}
	
	@SuppressWarnings("unchecked")
	public void testSaveNew() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NAMESPACE, "save");
		this.addParameter("strutsAction", "1");
		this.addParameter("contentType", "ART");
		this.addParameter("filters", "");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map fieldErrors = this.getAction().getFieldErrors();
		assertEquals(3, fieldErrors.size());
		assertTrue(fieldErrors.containsKey("title"));
		assertTrue(fieldErrors.containsKey("description"));
		assertTrue(fieldErrors.containsKey("feedType"));
		
		this.initAction(NAMESPACE, "save");
		this.addParameter("strutsAction", "1");
		this.addParameter("contentType", "ART");
		this.addParameter("title", "test rss title");
		this.addParameter("description", "test rss descr");
		this.addParameter("feedType", JpRssSystemConstants.FEED_TYPE_RSS_20);
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		
		List<Channel> channels = this.getRssManager().getChannels(Channel.STATUS_ALL);
		assertTrue(channels.size() == 1);
		Channel current = channels.get(0);
		assertTrue(!current.isActive());
		assertEquals("ART", current.getContentType());
		assertEquals("test rss title", current.getTitle());
		assertEquals("test rss descr", current.getDescription());
		assertEquals(JpRssSystemConstants.FEED_TYPE_RSS_20, current.getFeedType());
		assertNull(current.getFilters());
	}
	
	public void testAddFilter() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NAMESPACE, "addFilter");
		this.addParameter("strutsAction", "1");
		this.addParameter("contentType", "ART");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testList() throws Throwable {
		Channel channel1 = this.createTestChannel("t1", "d1", true);
		Channel channel2 = this.createTestChannel("t2", "d2", true);
		Channel channel3 = this.createTestChannel("t3", "d3", false);
		Channel channel4 = this.createTestChannel("t4", "d4", false);
		this.getRssManager().addChannel(channel1);
		this.getRssManager().addChannel(channel2);
		this.getRssManager().addChannel(channel3);
		this.getRssManager().addChannel(channel4);
		
		this.setUserOnSession("admin");
		this.initAction(NAMESPACE, "list");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		RssAction action = (RssAction) this.getAction();
		List<Channel> channels = action.getChannels();
		assertEquals(4, channels.size());
	}
	
	public void testEdit() throws Throwable {
		Channel channel1 = this.createTestChannel("t1", "d1", true);
		this.getRssManager().addChannel(channel1);
		
		this.setUserOnSession("admin");
		this.initAction(NAMESPACE, "edit");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		assertTrue(this.getAction().getActionErrors().size() == 1);
		
		this.initAction(NAMESPACE, "edit");
		String id = new Integer(channel1.getId()).toString();
		this.addParameter("id", id);
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		RssAction action = (RssAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
		assertEquals("t1", action.getTitle());
		assertEquals("d1", action.getDescription());
		assertTrue(action.isActive());
		assertEquals("cat1", action.getCategory());
		assertEquals("rss_2.0", action.getFeedType());
	}
	
	public void testDelete() throws Throwable {
		Channel channel1 = this.createTestChannel("t1", "d1", true);
		this.getRssManager().addChannel(channel1);
		
		this.setUserOnSession("admin");
		this.initAction(NAMESPACE, "delete");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		assertTrue(this.getAction().getActionErrors().size() == 1);
		
		this.initAction(NAMESPACE, "delete");
		String id = new Integer(channel1.getId()).toString();
		this.addParameter("id", id);
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		List<Channel> channels = this.getRssManager().getChannels(Channel.STATUS_ALL);
		assertTrue(channels.isEmpty());
	}
	
	public void testSaveUpdate() throws Throwable {
		Channel channel1 = this.createTestChannel("t1", "d1", true);
		this.getRssManager().addChannel(channel1);
		
		
		this.setUserOnSession("admin");
		this.initAction(NAMESPACE, "save");
		this.addParameter("id", new Integer(channel1.getId()).intValue());
		this.addParameter("strutsAction", "2");
		this.addParameter("contentType", "ART");
		this.addParameter("title", "test rss title");
		this.addParameter("description", "test rss descr");
		this.addParameter("feedType", JpRssSystemConstants.FEED_TYPE_RSS_20);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		
		Channel upadated = this.getRssManager().getChannel(channel1.getId());
		assertFalse(upadated.isActive());
		assertEquals("test rss title", upadated.getTitle());
		assertEquals("test rss descr", upadated.getDescription());
	}
	
	public void testFilters() throws Throwable {
		//TODO da fare
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
	
	private void init() throws Exception {
		try {
			this._rssManager = (IRssManager) this.getService(JpRssSystemConstants.RSS_MANAGER);
			IContentManager contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
			Content contentType = contentManager.createContentType("ART");
			AttributeInterface titolo = (AttributeInterface) contentType.getAttribute("Titolo");
			String[] roles = {JpRssSystemConstants.ATTRIBUTE_ROLE_RSS_CONTENT_TITLE};
			titolo.setRoles(roles);
			((IEntityTypesConfigurer) contentManager).updateEntityPrototype(contentType);
			this.waitNotifyingThread();
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}
	
	@Override
	protected void tearDown() throws Exception {
		try {
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
			IContentManager contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
			Content contentType = contentManager.createContentType("ART");
			AttributeInterface titolo = (AttributeInterface) contentType.getAttribute("Titolo");
			String[] roles = {JpRssSystemConstants.ATTRIBUTE_ROLE_RSS_CONTENT_TITLE};
			titolo.setRoles(roles);
			((IEntityTypesConfigurer) contentManager).updateEntityPrototype(contentType);
			this.waitNotifyingThread();
		} catch (Throwable t) {
			throw new Exception(t);
		} finally {
			super.tearDown();
		}
	}
	
	public IRssManager getRssManager() {
		return _rssManager;
	}

	private IRssManager _rssManager;
	private static final String NAMESPACE = "/do/jprss/Rss";

}
