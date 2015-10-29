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

import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator.ApsAggregatorItem;
import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.impl.FeedFetcherCache;
import com.rometools.fetcher.impl.HashMapFeedInfoCache;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * This service builds {@link Content} objects from a feed.
 */
public class RssConverterManager extends AbstractService implements IRssConverterManager {

	private static final Logger _logger = LoggerFactory.getLogger(RssConverterManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadMappingMap();
		_logger.debug("{} ready ", this.getClass().getName());
	}
	
	@SuppressWarnings("unchecked")
	protected void loadMappingMap() throws ApsSystemException {
		try {
			String xml = this.getConfigInterface().getConfigItem(IRssConverterManager.CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("The config item: " + IRssConverterManager.CONFIG_ITEM + " is not present.");
			}
			Document doc = this.getDocument(xml);
			Element root = doc.getRootElement();
			List<Element> elements = root.getChildren();
			Iterator<Element> it = elements.iterator();
			while (it.hasNext()) {
				Element currentElement = it.next();
				String key = currentElement.getAttributeValue("contentType");
				this.getMappingMap().put(key, new AggregatorConfig(currentElement));
			}
		} catch (Throwable t) {
			_logger.error("An error occurred loading configuration", t);
			throw new ApsSystemException("An error occurred loading configuration", t);
		}
	}
	
	@Override
	public Set<String> getMappings() {
		return this.getMappingMap().keySet();
	}
	
	private Document getDocument(String xmlText) throws ApsSystemException {
		Document doc = null;
		try {
			SAXBuilder saxBuilder = new SAXBuilder(false);
			Reader stringReader = new StringReader(xmlText);
			doc = saxBuilder.build(stringReader);
		} catch (Throwable t) {
			_logger.error("error parsing the configuration", t);
			throw new ApsSystemException ("error parsing the configuration", t);
		}
		return doc;
	}
	
	@Override
	public AggregatorConfig getAggregatorConfig(String contentType) {
		return this.getMappingMap().get(contentType);
	}
	
	@Override
	public List<Content> getContents(ApsAggregatorItem item) throws ApsSystemException {
		List<Content> contents = new ArrayList<Content>();
		AggregatorConfig aggregatorConfig = this.getMappingMap().get(item.getContentType());
		try {
			List<SyndEntry> entries = this.getRssEntries(IRssConverterManager.RSS_2_0, item.getLink());
			if(null == entries) return contents;
			Iterator<SyndEntry> entriesIt = entries.iterator();
			String linkAttributeName = aggregatorConfig.getLinkAttributeName();// this.getLinkAttributeName(typeCode);
			while (entriesIt.hasNext()) {
				SyndEntry feedItem = entriesIt.next();
				String link = feedItem.getLink();
				Content content = this.getExistingContent(link, linkAttributeName, item.getContentType());
				if (null == content) {
					String typeCode = aggregatorConfig.getContentType();
					content = this.getContentManager().createContentType(typeCode);
				}
				this.getContentBuilder().populateContentFromMapping(content, feedItem, item, this.getMappingMap().get(aggregatorConfig.getContentType()));
				contents.add(content);
			}
		} catch (Throwable t) {
			_logger.error("error transforming feed to content list", t);
			throw new ApsSystemException("error transforming feed to content list", t);
		}
		return contents;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SyndEntry> getRssEntries(String feedType, String url) throws ApsSystemException {
		SyndFeed feed = null;
		try {
			feed = new SyndFeedImpl();
			String outputType = feedType;

			feed.setFeedType(outputType);

			feed.setTitle("Entando Aggregated Feed");
			feed.setDescription("Entando Aggregated Feed");
			feed.setAuthor("Entando");
			feed.setLink("www.entando.com");

			List<SyndEntry> entries = new ArrayList<SyndEntry>();
			feed.setEntries(entries);
			SyndFeed inFeed = this.retrieveFeed(url);
			if (null == inFeed) return null;
			entries.addAll(inFeed.getEntries());

		} catch (Throwable t) {
			throw new ApsSystemException ("error in getRssEntries",t);
		}
		return feed.getEntries();
	}
	
	private SyndFeed retrieveFeed(String url) {
		SyndFeed inFeed = null;
		try {
			FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
			FeedFetcher feedFetcher = new HttpURLFeedFetcher(feedInfoCache);
			URL inputUrl = new URL(url);
			inFeed = feedFetcher.retrieveFeed(inputUrl);
		} catch (Throwable t) {
			_logger.error("error in retrieveFeed with url {}", url, t);
		}
		return inFeed;
	}
	
	private Content getExistingContent(String link, String linkAttributeName, String contentType) throws ApsSystemException {
		Content content = null;
		try {
			EntitySearchFilter linkFilter = new EntitySearchFilter(linkAttributeName, true, link, false);
			linkFilter.setLangCode(this.getDefaultLangCode());
			EntitySearchFilter typeCodeFilter = new EntitySearchFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, contentType, false);
			EntitySearchFilter[] filters = new EntitySearchFilter[]{linkFilter, typeCodeFilter};
			List<String> searchContents  = this.getContentManager().searchId(filters);
			if (null != searchContents && searchContents.size() > 0) {
				String contentId  = (String) searchContents.get(0);
				content = this.getContentManager().loadContent(contentId, true);
			}
		} catch (Throwable t) {
			throw new ApsSystemException("error in getExistingContent", t);
		}
		return content;
	}
	
	private String getDefaultLangCode() {
		return this.getLangManager().getDefaultLang().getCode();
	}
	
	public void setMappingMap(Map<String, AggregatorConfig> mappingMap) {
		this._mappingMap = mappingMap;
	}
	public Map<String, AggregatorConfig> getMappingMap() {
		return _mappingMap;
	}

	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	protected ILangManager getLangManager() {
		return _langManager;
	}
	
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	
	public void setConfigInterface(ConfigInterface configInterface) {
		this._configInterface = configInterface;
	}
	protected ConfigInterface getConfigInterface() {
		return _configInterface;
	}

	public void setContentBuilder(ContentBuilder contentBuilder) {
		this._contentBuilder = contentBuilder;
	}
	protected ContentBuilder getContentBuilder() {
		return _contentBuilder;
	}

	private Map<String, AggregatorConfig> _mappingMap = new HashMap<String, AggregatorConfig>();
	private ConfigInterface _configInterface;
	private ILangManager _langManager;
	private IContentManager _contentManager;
	private ContentBuilder _contentBuilder;
}
