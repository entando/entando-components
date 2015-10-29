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
package org.entando.entando.plugins.jprssaggregator.aps.system.services.converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.HypertextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import org.entando.entando.plugins.jprssaggregator.PluginConfigTestUtils;
import org.entando.entando.plugins.jprssaggregator.RssAggregatorTestHelper;
import org.entando.entando.plugins.jprssaggregator.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jprssaggregator.aps.JpRssAggregatorSystemConstants;
import org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator.ApsAggregatorItem;
import org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator.IAggregatorManager;
import org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator.TestAggregatorManagerHelper;
import com.rometools.rome.feed.synd.SyndEntry;

/**
 * Before running this test, copy the file src/main/webapp/resources/plugins/jprssaggregator/test_rss.xml
 * inside an instance of Entando and run the server.
 * You must edit the constant TEST_URL inside com.agiletec.plugins.jprssaggregator.PluginConfigTestUtils
 */
public class TestRssConverterManager extends ApsPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this._testHelper.addContentTypeToConfig();
		List<String> groups = new ArrayList<String>();
		groups.add(Group.FREE_GROUP_NAME);
		EntitySearchFilter[] filters = new EntitySearchFilter[1];
		filters[0] = new EntitySearchFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "RSS", false);
		List<String> contentsId = _contentManager.loadWorkContentsId(filters, groups);
		assertEquals(0, contentsId.size());
	}

	public void testLoadConfig() {
		assertNotNull(_rssConverterManager);
		RssConverterManager converter = (RssConverterManager) _rssConverterManager;
		Map<String, AggregatorConfig> mapping = converter.getMappingMap();
		assertEquals(1, mapping.size());
		assertTrue(mapping.containsKey("RSS"));
	}
	
	public void _testGetRssEntries() throws Throwable {
		List<SyndEntry> entries = _rssConverterManager.getRssEntries(IRssConverterManager.RSS_2_0, PluginConfigTestUtils.TEST_URL);
		assertNotNull(entries);
		assertEquals(3, entries.size());
	}
	
	public void _testGetContents() throws Throwable {
		String url = PluginConfigTestUtils.TEST_URL;
		ApsAggregatorItem item = TestAggregatorManagerHelper.createItem(3600, "dummy_descr", url);
		//_aggregatorManager.addItem(item);
		List<SyndEntry> entries = _rssConverterManager.getRssEntries(IRssConverterManager.RSS_2_0, url);
		assertNotNull(entries);
		List<Content> contents = _rssConverterManager.getContents(item);
		assertNotNull(contents);
		assertEquals(entries.size(), contents.size());
		Content content = contents.get(0);
		assertEquals("Burnout", content.getDescr());
		TextAttribute titleAttr = (TextAttribute) content.getAttribute("Title");
		LinkAttribute  linkAttr = (LinkAttribute) content.getAttribute("Link");
		HypertextAttribute corpoAttr = (HypertextAttribute) content.getAttribute("TextBody");
		assertEquals("Burnout", titleAttr.getText());
		assertEquals("http://www.alistapart.com/articles/burnout/", linkAttr.getSymbolicLink().getUrlDest());
		assertTrue(corpoAttr.getText().startsWith("Does every day feel like a bad day"));

		Content content1 = contents.get(2);
		assertEquals("Descrizione non disponibile", content1.getDescr());
		TextAttribute titleAttr1 = (TextAttribute) content1.getAttribute("Title");
		LinkAttribute  linkAttr1 = (LinkAttribute) content1.getAttribute("Link");
		HypertextAttribute corpoAttr1 = (HypertextAttribute) content1.getAttribute("TextBody");
		assertEquals("Titolo non disponibile", titleAttr1.getText());
		assertEquals("http://www.alistapart.com/articles/taking-the-guesswork-out-of-design/", linkAttr1.getSymbolicLink().getUrlDest());
		assertEquals("Corpo testo non disponibile", corpoAttr1.getText());
	}

	private void init() {
		this._rssConverterManager = (IRssConverterManager) this.getService(JpRssAggregatorSystemConstants.RSS_CONVERTER_MANAGER);
		this._aggregatorManager = (IAggregatorManager) this.getService(JpRssAggregatorSystemConstants.AGGREGATOR_MANAGER);
		this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
		this._configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		this._testHelper = new RssAggregatorTestHelper(this._configManager);
	}
	
	@Override
	protected void tearDown() throws Exception {
		try {
			List<ApsAggregatorItem> items = this._aggregatorManager.getItems();
			Iterator<ApsAggregatorItem> it = items.iterator();
			while (it.hasNext()) {
				ApsAggregatorItem currentItem = it.next();
				this._aggregatorManager.deleteItem(currentItem.getCode());
			}
		} catch (Throwable t) {
			throw new Exception();
		} finally {
			this._testHelper.restoreConfig();
			super.tearDown();
		}
	}
	
	private IRssConverterManager _rssConverterManager;
	private IAggregatorManager _aggregatorManager;
	private IContentManager _contentManager;
	private RssAggregatorTestHelper _testHelper;
	private ConfigInterface _configManager;
	
}