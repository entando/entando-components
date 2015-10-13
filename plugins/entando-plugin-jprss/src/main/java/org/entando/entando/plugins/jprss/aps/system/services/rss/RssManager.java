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
package org.entando.entando.plugins.jprss.aps.system.services.rss;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingEvent;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingObserver;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.util.FilterUtils;
import com.agiletec.plugins.jacms.aps.system.services.linkresolver.ILinkResolverManager;
import org.entando.entando.plugins.jprss.aps.system.services.JpRssSystemConstants;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager that handles the RSS Channels
 * @author S.Puddu - E.Santoboni
 */
public class RssManager extends AbstractService implements IRssManager, EntityTypesChangingObserver {
	
	private static final Logger _logger = LoggerFactory.getLogger(RssManager.class);
	
	@Override
	public void init() throws Exception {
		try {
			this.loadMappingConfig();
			_logger.debug("{} ready", this.getClass().getName());
		} catch (Throwable t) {
			_logger.error("{} Manager: Error on initialization", this.getClass().getName(), t);
		}
	}
	
	@Override
	public void updateFromEntityTypesChanging(EntityTypesChangingEvent event) {
		if (!event.getEntityManagerName().equals(JacmsSystemConstants.CONTENT_MANAGER)) {
			return;
		}
		try {
			this.loadMappingConfig();
		} catch (Throwable t) {
			_logger.error("error loading Rss Content Config", t);
		}
	}

	private void loadMappingConfig() throws ApsSystemException {
		Map<String, RssContentMapping> mappings = new HashMap<String, RssContentMapping>();
		try {
			Map<String, IApsEntity> contentTypes = this.getContentManager().getEntityPrototypes();
			Iterator<IApsEntity> contentTypeIter = contentTypes.values().iterator();
			while (contentTypeIter.hasNext()) {
				IApsEntity contentType = contentTypeIter.next();
				AttributeInterface attributeTitle = contentType.getAttributeByRole(JpRssSystemConstants.ATTRIBUTE_ROLE_RSS_CONTENT_TITLE);
				if (null != attributeTitle) {
					RssContentMapping mapping = new RssContentMapping();
					mapping.setContentType(contentType.getTypeCode());
					mapping.setTitleAttributeName(attributeTitle.getName());
					AttributeInterface attributeDescr = contentType.getAttributeByRole(JpRssSystemConstants.ATTRIBUTE_ROLE_RSS_CONTENT_DESCRIPTION);
					if (null != attributeDescr) {
						mapping.setDescriptionAttributeName(attributeDescr.getName());
					}
					mappings.put(contentType.getTypeCode(), mapping);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading rss content mapping", t);
			throw new ApsSystemException("Error loading rss content mapping", t);
		}
		this.setContentMapping(mappings);
	}

	@Override
	public void addChannel(Channel channel) throws ApsSystemException {
		try {
			int key = getKeyGeneratorManager().getUniqueKeyCurrentValue();
			channel.setId(key);
			this.getRssDAO().addChannel(channel);
		} catch (Throwable t) {
			_logger.error("Error adding a new channel", t);
			throw new ApsSystemException("Error adding a new channel", t);
		}
	}

	@Override
	public void deleteChannel(int id) throws ApsSystemException {
		try {
			this.getRssDAO().deleteChannel(id);
		} catch (Throwable t) {
			_logger.error("Error deleting the channel with code ", id, t);
			throw new ApsSystemException("Error deleting the channel with code: " + id, t);
		}
	}

	@Override
	public void updateChannel(Channel channel) throws ApsSystemException {
		try {
			this.getRssDAO().updateChannel(channel);
		} catch (Throwable t) {
			_logger.error("Error updating a channel", t);
			throw new ApsSystemException("Error updating a channel", t);
		}
	}

	@Override
	public List<Channel> getChannels(int status) throws ApsSystemException {
		List<Channel> channels = null;
		try {
			channels = this.getRssDAO().getChannels(status);
		} catch (Throwable t) {
			_logger.error("Error getting the list of the channels by status {}", status, t);
			throw new ApsSystemException("Error getting the list of the channels", t);
		}
		return channels;
	}

	@Override
	public Channel getChannel(int id) throws ApsSystemException {
		Channel channel = null;
		try {
			channel = this.getRssDAO().getChannel(id);
		} catch (Throwable t) {
			_logger.error("Error loading channel with id {}", id, t);
			throw new ApsSystemException("Error loading channel with id" + id, t);
		}
		return channel;
	}

	private EntitySearchFilter[] getEntitySearchFilter(Channel channel, String langCode) {
		String contentTypeCode = channel.getContentType();
		String widgetParam = channel.getFilters();
		EntitySearchFilter[] entitySearchFilters = null;
		if (null != widgetParam && widgetParam.trim().length() > 0) {
			IApsEntity contentType = this.getContentManager().getEntityPrototype(contentTypeCode);
			FilterUtils filterUtils = new FilterUtils();
			entitySearchFilters = filterUtils.getFilters(contentType, widgetParam, langCode);
		} else {
			entitySearchFilters = new EntitySearchFilter[0];
		}
		return entitySearchFilters;
	}
	
	@Override
	public SyndFeed getSyndFeed(Channel channel, String lang, String feedLink, HttpServletRequest req, HttpServletResponse resp) throws ApsSystemException {
		SyndFeed feed = null;
		if (null == feed) {
			feed = new SyndFeedImpl();
			feed.setFeedType(channel.getFeedType());
			feed.setTitle(channel.getTitle());
			feed.setLink(feedLink);
			feed.setDescription(channel.getDescription());
			List<String> contentsId = this.getContentsId(channel, lang);
			feed.setEntries(this.getEntries(contentsId, lang, feedLink, req, resp));
		}
		return feed;
	}
	
	private List<SyndEntry> getEntries(List<String> contentsId, String lang, String feedLink, HttpServletRequest req, HttpServletResponse resp) throws ApsSystemException {
		List<SyndEntry> entries = new ArrayList<SyndEntry>();
		Iterator<String> idIterator = contentsId.iterator();
		while (idIterator.hasNext()) {
			String id = (String) idIterator.next();
			ContentRecordVO currentContent = this.getContentManager().loadContentVO(id);
			RssContentMapping mapping = (RssContentMapping) this.getContentMapping().get(currentContent.getTypeCode());
			if (null == mapping) {

				_logger.error("Null content mapping by existed channel for content type {}", currentContent.getTypeCode());
				continue;
			}
			entries.add(this.createEntry(currentContent, lang, feedLink, req, resp));
		}
		return entries;
	}

	private SyndEntry createEntry(ContentRecordVO contentVO, String langCode, String feedLink, HttpServletRequest req, HttpServletResponse resp) throws ApsSystemException {
		SyndEntry entry = new SyndEntryImpl();
		RssContentMapping mapping = this.getContentMapping().get(contentVO.getTypeCode());
		try {
			Content content = (Content) this.getContentManager().loadContent(contentVO.getId(), true);
			ITextAttribute titleAttr = (ITextAttribute) content.getAttribute(mapping.getTitleAttributeName());
			String title = (titleAttr.getTextForLang(langCode));
			if (null == title || title.trim().length() == 0) {
				title = titleAttr.getText();
			}
			entry.setTitle(title);
			String link = this.createLink(content, feedLink);
			entry.setLink(link);
			entry.setPublishedDate(contentVO.getModify());
			ITextAttribute descrAttr = (ITextAttribute) content.getAttribute(mapping.getDescriptionAttributeName());
			if (null != descrAttr) {
				SyndContent description = new SyndContentImpl();
				description.setType(JpRssSystemConstants.SYNDCONTENT_TYPE_TEXTHTML);
				String inLang = descrAttr.getTextForLang(langCode);
				//TODO Ottimizzare!
				RequestContext requestContext = new RequestContext();
				requestContext.setRequest(req);
				requestContext.setResponse(resp);
				if (null != inLang && inLang.length() > 0) {
					String textValue = this.getLinkResolver().resolveLinks(inLang, requestContext);
					if (null != textValue && textValue.trim().length() > 0) {
						description.setValue(textValue);
					} else {
						description.setValue(descrAttr.getText());
					}
				} else {
					String textValue = this.getLinkResolver().resolveLinks(descrAttr.getText(), requestContext);
					description.setValue(textValue);
				}
				entry.setDescription(description);
			}
		} catch (Throwable t) {
			_logger.error("Error in createEntry", t);
			throw new ApsSystemException("Error in createEntry", t);
		}
		return entry;
	}

	private String createLink(Content content, String feedLink) {
		SymbolicLink symbolicLink = new SymbolicLink();
		StringBuilder destination = new StringBuilder(feedLink);
		String viewPageCode = content.getViewPage();
		if (null == viewPageCode || null == this.getPageManager().getPage(viewPageCode)) {
			viewPageCode = this.getPageManager().getRoot().getCode();
		}
		destination.append(viewPageCode).append(".page").append("?contentId=").append(content.getId());
		symbolicLink.setDestinationToUrl(destination.toString());
		return symbolicLink.getUrlDest();
	}

	@Override
	public Map<String, String> getAvailableContentTypes() {
		Map<String, String> availableContentTypes = new HashMap<String, String>();
		Iterator it = this.getContentMapping().entrySet().iterator();
		Map<String, SmallContentType> contentTypes = this.getContentManager().getSmallContentTypesMap();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String typeCode = (String) pairs.getKey();
			SmallContentType smallContentType = (SmallContentType) contentTypes.get(typeCode);
			if (null != smallContentType) {
				availableContentTypes.put(typeCode, smallContentType.getDescr());
			}
		}
		return availableContentTypes;
	}

	@Override
	public RssContentMapping getContentMapping(String typeCode) {
		RssContentMapping mapping = this.getContentMapping().get(typeCode);
		if (null != mapping) {
			return mapping.clone();
		}
		return null;
	}

	protected EntitySearchFilter[] addFilter(EntitySearchFilter[] filters, EntitySearchFilter filterToAdd) {
		int len = filters.length;
		EntitySearchFilter[] newFilters = new EntitySearchFilter[len + 1];
		for (int i = 0; i < len; i++) {
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		return newFilters;
	}

	private List<String> getContentsId(Channel channel, String langCode) throws ApsSystemException {
		RssContentMapping mapping = (RssContentMapping) this.getContentMapping().get(channel.getContentType());
		if (null == mapping) {
			_logger.error("Null content mapping by existed channel for content type {}", channel.getContentType());
			return new ArrayList<String>();
		}
		try {
			EntitySearchFilter[] searchFilters = this.getEntitySearchFilter(channel, langCode);//this.getEntitySearchFilterDOM().getFilters(channel.getContentType(), channel.getFilters());
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, channel.getContentType(), false);
			EntitySearchFilter[] entitySearchFilters = addFilter(searchFilters, filterToAdd);

			String[] categories = null;
			if (null != channel.getCategory() && channel.getCategory().trim().length() > 0) {
				categories = new String[]{channel.getCategory()};
			}
			Collection<String> userGroupCodes = new ArrayList<String>();
			userGroupCodes.add(Group.FREE_GROUP_NAME);
			List<String> contentsId = this.getContentManager().loadPublicContentsId(channel.getContentType(), categories, entitySearchFilters, userGroupCodes);

			if (channel.getMaxContentsSize() > 0 && contentsId.size() > channel.getMaxContentsSize()) {
				return contentsId.subList(0, channel.getMaxContentsSize());
			} else {
				return contentsId;
			}
		} catch (Throwable t) {
			_logger.error("Error in rss contents", t);
			throw new ApsSystemException("Error in rss contents", t);
		}
	}

	@Override
	public Map<String, String> getAvailableFeedTypes() {
		return _availableFeedTypes;
	}
	public void setAvailableFeedTypes(Map<String, String> availableFeedTypes) {
		this._availableFeedTypes = availableFeedTypes;
	}

	protected Map<String, RssContentMapping> getContentMapping() {
		return _contentMapping;
	}
	protected void setContentMapping(Map<String, RssContentMapping> contentMapping) {
		this._contentMapping = contentMapping;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	protected ILinkResolverManager getLinkResolver() {
		return _linkResolver;
	}
	public void setLinkResolver(ILinkResolverManager linkResolver) {
		this._linkResolver = linkResolver;
	}

	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	protected IRssDAO getRssDAO() {
		return _rssDAO;
	}
	public void setRssDAO(IRssDAO rssDAO) {
		this._rssDAO = rssDAO;
	}

	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	private Map<String, RssContentMapping> _contentMapping;
	private IContentManager _contentManager;
	private IPageManager _pageManager;
	private ConfigInterface _configManager;
	private IKeyGeneratorManager _keyGeneratorManager;
	private Map<String, String> _availableFeedTypes;
	private ILinkResolverManager _linkResolver;
	private IRssDAO _rssDAO;

}
