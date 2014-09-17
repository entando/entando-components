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
package com.agiletec.plugins.jprss.aps.system.services.rss;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jprss.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jprss.aps.system.services.JpRssSystemConstants;
import com.agiletec.plugins.jprss.aps.system.services.rss.Channel;
import com.agiletec.plugins.jprss.aps.system.services.rss.IRssManager;

public class TestRssManager extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetAvailableContentTypes() {
		Map<String, String> availableContentTypes = this.getRssManager().getAvailableContentTypes();
		assertNotNull(availableContentTypes);
		assertEquals(1, availableContentTypes.size());
		String key = "ART";
		String descr = availableContentTypes.get(key);
		assertEquals("Articolo rassegna stampa", descr);
	}
	
	public void testCRUDChannel() throws Throwable {
		Channel testChannel = this.createTestChannel();
		//ADD CHANNEL
		this.getRssManager().addChannel(testChannel);
		assertTrue(testChannel.getId() > 0);
		
		//GET LIST
		List<Channel> activeList = this.getRssManager().getChannels(Channel.STATUS_ACTIVE);
		List<Channel> notActiveList = this.getRssManager().getChannels(Channel.STATUS_NOT_ACTIVE);
		List<Channel> fullList = this.getRssManager().getChannels(Channel.STATUS_ALL);
		
		assertNotNull(activeList);
		assertEquals(1, activeList.size());
		
		assertNotNull(notActiveList);
		assertEquals(0, notActiveList.size());
		
		assertNotNull(fullList);
		assertEquals(1, fullList.size());
		
		//GET ONE
		Channel channel = activeList.get(0);
		assertEquals(channel.getCategory(), testChannel.getCategory());
		assertEquals(channel.getContentType(), testChannel.getContentType());
		assertEquals(channel.getDescription(), testChannel.getDescription());
		assertEquals(channel.getFeedType(), testChannel.getFeedType());
		assertEquals(channel.getId(), testChannel.getId());
		assertEquals(channel.getTitle(), testChannel.getTitle());
		assertEquals(channel.isActive(), testChannel.isActive());
		
		channel.setCategory("evento");
		channel.setDescription("updated_test channel");
		channel.setTitle("updated_test title");
		channel.setFeedType("rss_1.0");
		channel.setActive(false);
		this.getRssManager().updateChannel(channel);
		
		Channel updated = this.getRssManager().getChannel(channel.getId());
		assertEquals(updated.getCategory(), channel.getCategory());
		assertEquals(updated.getContentType(), channel.getContentType());
		assertEquals(updated.getDescription(), channel.getDescription());
		assertEquals(updated.getFeedType(), channel.getFeedType());
		assertEquals(updated.getId(), channel.getId());
		assertEquals(updated.getTitle(), channel.getTitle());		
		assertEquals(updated.isActive(), channel.isActive());
		
		notActiveList = this.getRssManager().getChannels(Channel.STATUS_NOT_ACTIVE);
		activeList = this.getRssManager().getChannels(Channel.STATUS_ACTIVE);
		
		assertTrue(notActiveList.size() == 1);
		assertTrue(activeList.isEmpty());
		
		this.getRssManager().deleteChannel(updated.getId());
		
		activeList = this.getRssManager().getChannels(Channel.STATUS_ACTIVE);
		notActiveList = this.getRssManager().getChannels(Channel.STATUS_NOT_ACTIVE);
		fullList = this.getRssManager().getChannels(Channel.STATUS_ALL);

		assertTrue(activeList.isEmpty());
		assertTrue(notActiveList.isEmpty());
		assertTrue(fullList.isEmpty());
	}
	
	private Channel createTestChannel() {
		Channel channel = new Channel();
		channel.setActive(true);
		channel.setCategory("cat1");
		channel.setContentType("ART");
		channel.setDescription("test channel");
		channel.setFeedType("rss_2.0");
		channel.setTitle("test title");
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
			List<Channel> fullList = this.getRssManager().getChannels(Channel.STATUS_ALL);
			Iterator<Channel> it = fullList.iterator();
			while (it.hasNext()) {
				Channel current = it.next();
				int id = current.getId();
				this.getRssManager().deleteChannel(id);
			}
			fullList = this.getRssManager().getChannels(Channel.STATUS_ALL);
			assertTrue(fullList.isEmpty());
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
}
