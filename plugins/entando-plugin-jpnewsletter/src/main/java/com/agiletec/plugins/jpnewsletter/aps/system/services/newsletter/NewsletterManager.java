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
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringEscapeUtils;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.linkresolver.ILinkResolverManager;
import com.agiletec.plugins.jacms.aps.system.services.renderer.IContentRenderer;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailSendersUtilizer;
import com.agiletec.plugins.jpnewsletter.aps.system.JpnewsletterSystemConstants;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.ContentReport;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentReportVO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentType;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterReport;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterSearchBean;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.Subscriber;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.parse.NewsletterConfigDOM;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.util.ShaEncoder;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni, A.Turrini, E.Mezzano
 */
public class NewsletterManager extends AbstractService 
		implements INewsletterManager, MailSendersUtilizer, INewsletterSchedulerManager {
	
	private static final Logger _logger = LoggerFactory.getLogger(NewsletterManager.class);
	
	@Override
	public void init() throws Exception {
		try {
			this.loadConfigs();
			this.startScheduler();
			_logger.info(this.getClass().getName() + ": initialized");
		} catch (Throwable t) {
			_logger.error("{} Manager: Error on initialization", this.getClass().getName(), t);
		}
	}
	
	@Override
	public void refresh() throws Throwable {
		synchronized (this) {
			super.refresh();
		}
	}
	
	@Override
	protected void release() {
		this._scheduler.cancel();
		this._scheduler = null;
	}
	
	@Override
	public void destroy() {
		this._scheduler.cancel();
		this._scheduler = null;
	}
	
	protected void loadConfigs() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpnewsletterSystemConstants.NEWSLETTER_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + JpnewsletterSystemConstants.NEWSLETTER_CONFIG_ITEM);
			}
			NewsletterConfigDOM configDOM = new NewsletterConfigDOM();
			this.setConfig(configDOM.extractConfig(xml));
		} catch (Throwable t) {
			_logger.error("Error loading config", t);
			throw new ApsSystemException("Error loading config", t);
		}
	}
	
	@Override
	public void startScheduler() throws ApsSystemException {
		NewsletterConfig config = this.getConfig();
		Date start = config.getNextTaskTime();
		this._scheduler = new Scheduler(this, start, config.getHoursDelay());
	}
	
	@Override
	public void stopScheduler() throws ApsSystemException {
		this.release();
	}
	
	@Override
	public NewsletterConfig getNewsletterConfig() {
		return this._config.clone();
	}
	
	@Override
	public void updateNewsletterConfig(NewsletterConfig config) throws ApsSystemException {
		try {
			String xml = new NewsletterConfigDOM().createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpnewsletterSystemConstants.NEWSLETTER_CONFIG_ITEM, xml);
			this.setConfig(config);
			this.refresh();
		} catch (Throwable t) {
			_logger.error("Error updating configuration", t);
			throw new ApsSystemException("Error updating configuration", t);
		}
	}
	
	@Override
	public void addContentToQueue(String contentId) throws ApsSystemException {
		try {
			this.getNewsletterDAO().addContentToQueue(contentId);
		} catch (Throwable t) {
			_logger.error("Error adding content on queue", t);
			// Do not throws exceptions
		}
	}
	
	@Override
	public void removeContentFromQueue(String contentId) throws ApsSystemException {
		try {
			this.getNewsletterDAO().deleteContentFromQueue(contentId);
		} catch (Throwable t) {
			_logger.error("Error removing content from queue", t);
			throw new ApsSystemException("Error removing content from queue", t);
		}
	}
	
	@Override
	public List<String> getContentQueue() throws ApsSystemException {
		try {
			return this.getNewsletterDAO().loadContentQueue();
		} catch (Throwable t) {
			_logger.error("Error loading content queue", t);
			throw new ApsSystemException("Errore in caricamento coda contenuti newsLetter", t);
		}
	}
	
	@Override
	public boolean existsContentReport(String contentId) throws ApsSystemException {
		try {
			return this.getNewsletterDAO().existsContentReport(contentId);
		} catch (Throwable t) {
			_logger.error("Error on 'existsContentReport' method", t);
			throw new ApsSystemException("Error verifying content report existence", t);
		}
	}
	
	@Override
	public List<String> getSentContentIds() throws ApsSystemException {
		try {
			return this.getNewsletterDAO().loadSentContentIds();
		} catch (Throwable t) {
			_logger.error("Error on 'getSentContentIds' method", t);
			throw new ApsSystemException("Error loading sent contents ids", t);
		}
	}
	
	@Override
	public NewsletterContentReportVO getContentReport(String contentId) throws ApsSystemException {
		try {
			return this.getNewsletterDAO().loadContentReport(contentId);
		} catch (Throwable t) {
			_logger.error("Error on 'getContentReport' method", t);
			throw new ApsSystemException("Error loading content report", t);
		}
	}
	
	@Override
	public void sendNewsletter() throws ApsSystemException {
		if (!this.isSendingNewsletter() && this.getContentQueue().size()>0) {
			NewsletterSenderThread sender = new NewsletterSenderThread(this);
			sender.setName(JpnewsletterSystemConstants.NEWSLETTER_SENDER_THREAD_NAME);
			sender.start();
		}
	}
	
	protected void sendNewsletterFromThread() throws ApsSystemException {
		synchronized (this) {
			if (this.isSendingNewsletter()) {
				return;
			} else {
				this.setSendingNewsletter(true);
			}
		}
		try {
			_logger.info("Newletter: delivery process initiated"); 
			if (!this.getConfig().isActive()) return;
			List<String> contentIds = this.getContentQueue();
			if (contentIds.size() > 0) {
				List<Content> contents = new ArrayList<Content>(contentIds.size());
				for (int i = 0; i < contentIds.size(); i++) {
					String id = contentIds.get(i);
					Content content = this.getContentManager().loadContent(id, true);
					if (content != null) {
						contents.add(content);
					}
				}
				if (contents.size() > 0) {
					this.sendNewsletterToUsers(contents);
				} else {
					_logger.info("Newletter: had some contents ids selected, corresponding to no actual content: this is quite strange!");
				}
				this.getNewsletterDAO().cleanContentQueue(contentIds);
			} else {
				_logger.info("Newletter: no contents found for delivery");
			}
			_logger.info("Newletter: delivery process completed"); 
		} catch (Throwable t) {
			_logger.error("Newletter: delivery process ended abnormally", t);
		} finally {
			this.setSendingNewsletter(false);
		}
	}
	
	@Override
	public String buildMailBody(Content content, boolean html) throws ApsSystemException {
		String mailBody = null;
		try {
			NewsletterConfig config = this.getConfig();
			NewsletterContentType contentType = config.getContentType(content.getTypeCode());
			int modelId = html ? contentType.getHtmlModel() : contentType.getSimpleTextModel();
			mailBody = this.buildMailBody(content, modelId, html);
		} catch (Throwable t) {
			_logger.error("Error on 'buildMailBody' method", t);
			throw new ApsSystemException("Error building body for content " + content.getId(), t);
		}
		return mailBody;
	}
	
	protected void sendNewsletterToUsers(List<Content> contents) throws ApsSystemException {
		try {
			Map<String, List<String>> profileAttributes = this.prepareProfileAttributesForContents(contents);
			NewsletterReport newsletterReport = this.prepareNewsletterReport(contents);
			Set<String> usernames = this.extractUsernames();
			if (null != usernames && usernames.size() > 0) {
				Iterator<String> userIter = usernames.iterator();
				while (userIter.hasNext()) {
					String username = (String) userIter.next();
					UserDetails user = this.getUserManager().getUser(username);
					if (null != user && !user.isDisabled()) {
						this.sendNewsletterToUser(username, contents, profileAttributes, newsletterReport);
					}
				}
			} else {
				_logger.warn("Newsletter: no receivers to send newsletter to!");
			}
			this.sendNewsletterToSubscribers(contents, newsletterReport);
			this.addNewsletterReport(newsletterReport);
		} catch (Throwable t) {
			_logger.error("Error on 'sendNewsletterToUsers' method", t);
			throw new ApsSystemException("Error sending Newsletter To Users ", t);
		}
	}
	
	protected void addNewsletterReport(NewsletterReport newsletterReport) throws ApsSystemException {
		if (null == newsletterReport) return;
		try {
			IKeyGeneratorManager keyGeneratorManager = this.getKeyGeneratorManager();
			newsletterReport.setId(keyGeneratorManager.getUniqueKeyCurrentValue());
			for (ContentReport contentReport : newsletterReport.getContentReports().values()) {
				contentReport.setId(keyGeneratorManager.getUniqueKeyCurrentValue());
			}
			this.getNewsletterDAO().addNewsletterReport(newsletterReport);
		} catch (Throwable t) {
			_logger.error("Error adding newsletter report : id {}", newsletterReport.getId(), t);
		}
	}
	
	private Map<String, List<String>> prepareProfileAttributesForContents(List<Content> contents) {
		Map<String, List<String>> profileAttributes = new HashMap<String, List<String>>();
		Properties subscriptions = this.getConfig().getSubscriptions();
		for (int i=0; i<contents.size(); i++) {
			Content content = contents.get(i);
			List<String> contentProfileAttributes = this.extractProfileAttributesForContent(content, subscriptions);
			if (contentProfileAttributes != null && contentProfileAttributes.size() > 0) {
				profileAttributes.put(content.getId(), contentProfileAttributes);
			}
		}
		return profileAttributes;
	}
	
	protected NewsletterReport prepareNewsletterReport(List<Content> contents) {
		NewsletterConfig config = this.getConfig();
		NewsletterReport newsletterReport = new NewsletterReport();
		newsletterReport.setSubject(config.getSubject());
		newsletterReport.setSendDate(new Date());
		String defaultLang = this.getLangManager().getDefaultLang().getCode();
		boolean alsoHtml = config.isAlsoHtml();
		for (Content content : contents) {
			boolean isConfiguredWithModels = false;
			ContentReport contentReport = new ContentReport();
			contentReport.setContentId(content.getId());
			String textBodyPart = this.prepareMailBodyContentPart(content, defaultLang, false);
			if (null != textBodyPart) {
				isConfiguredWithModels = true;
				contentReport.setTextBody(textBodyPart);
			}
			if (alsoHtml) {
				String htmlBodyPart = this.prepareMailBodyContentPart(content, defaultLang, true);
				contentReport.setHtmlBody(htmlBodyPart);
			}
			if (isConfiguredWithModels) {
				newsletterReport.addContentReport(contentReport);
			} else {
				_logger.info(" Newsletter content {} not added, because has not model in config.", content.getId());
			}
		}
		return newsletterReport;
	}
	
	private void sendNewsletterToUser(String username, List<Content> contents, 
			Map<String, List<String>> profileAttributes, NewsletterReport newsletterReport) {
		NewsletterConfig config = this.getConfig();
		try {
			IUserProfile profile = this.getProfileManager().getProfile(username);
			if (profile != null) {
				String eMail = (String) profile.getValue(profile.getMailAttributeName());
				if (eMail != null && eMail.length() > 0) {
					List<Content> userContents = this.extractContentsForUser(profile, eMail, contents, profileAttributes, newsletterReport);
					if (userContents.size() > 0) {
						String[] emailAddresses = { eMail };
						String simpleText = this.prepareMailBody(userContents, newsletterReport, false);
						if (config.isAlsoHtml()) {
							String htmlText = this.prepareMailBody(userContents, newsletterReport, true);
							this.getMailManager().sendMixedMail(simpleText, htmlText, config.getSubject(), null, null, null, emailAddresses, config.getSenderCode());
						} else {
							this.getMailManager().sendMail(simpleText, config.getSubject(), null, null, emailAddresses, config.getSenderCode());
						}
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error on 'sendNewsletterToUser' method", t);
		}
	}
	
	protected String prepareMailBody(List<Content> userContents, NewsletterReport newsletterReport, boolean isHtml) {
		StringBuffer body = this.prepareMailCommonBody(userContents, newsletterReport, isHtml);
		NewsletterConfig config = this.getConfig();
		body.append(isHtml ? config.getHtmlFooter() : config.getTextFooter());
		return body.toString();
	}
	
	protected StringBuffer prepareMailCommonBody(List<Content> userContents, NewsletterReport newsletterReport, boolean isHtml) {
		NewsletterConfig config = this.getConfig();
		StringBuffer body = new StringBuffer(isHtml ? config.getHtmlHeader() : config.getTextHeader());
		String separator = isHtml ? config.getHtmlSeparator() : config.getTextSeparator();
		boolean first = true;
		for (Content content : userContents) {
			ContentReport contentReport = newsletterReport.getContentReport(content.getId());
			if (contentReport != null) {
				if (first) {
					first = false;
				} else {
					body.append(separator);
				}
				String text = isHtml ? contentReport.getHtmlBody() : contentReport.getTextBody();
				body.append(text);
			}
		}
		return body;
	}
	
	private String buildMailBody(Content content, long modelId, boolean html) throws ApsSystemException {
		NewsletterConfig config = this.getConfig();
		String header = html ? config.getHtmlHeader() : config.getTextHeader();
		String defaultLang = this.getLangManager().getDefaultLang().getCode();
		String mailContentBody = this.getContentRenderer().render(content, modelId, defaultLang, null);
		mailContentBody = this.getLinkResolver().resolveLinks(mailContentBody, null);
		String footer = html ? config.getHtmlFooter() : config.getTextFooter();
		String mailBody = header.concat(mailContentBody).concat(footer);
		if (!html) {
			return StringEscapeUtils.unescapeHtml(mailBody);
		}
		return mailBody;
	}
	
	private Set<String> extractUsernames() throws ApsSystemException {
		NewsletterConfig config = this.getConfig();
		Set<String> usernames = new HashSet<String>();
		String allContentsAttribute = config.getAllContentsAttributeName();
		if (null != allContentsAttribute && allContentsAttribute.trim().length() > 0) {
			EntitySearchFilter filter = new EntitySearchFilter(allContentsAttribute, true, Boolean.TRUE, false);
			EntitySearchFilter[] filters = {filter};
			try {
				List<String> usernamesForAllContents = ((IEntityManager) this.getProfileManager()).searchId(filters);
				usernames.addAll(usernamesForAllContents);
			} catch (Throwable t) {
				_logger.error("Error on 'extractUsernames' method", t);
				throw new ApsSystemException("Error searching for usernames on profile's attribute " + allContentsAttribute, t);
			}
		}
		Iterator<Object> subscriptionsIter = config.getSubscriptions().values().iterator();
		while (subscriptionsIter.hasNext()) {
			String boolAttributeName = (String) subscriptionsIter.next();
			if (null != boolAttributeName && boolAttributeName.trim().length() > 0) {
				EntitySearchFilter filter = new EntitySearchFilter(boolAttributeName, true, Boolean.TRUE, false);
				EntitySearchFilter[] filters = {filter};
				try {
					List<String> usernamesForCategory = ((IEntityManager) this.getProfileManager()).searchId(filters);
					usernames.addAll(usernamesForCategory);
				} catch (Throwable t) {
					_logger.error("Error on 'extractUsernames' method", t);
					throw new ApsSystemException("Error searching for usernames on profile's attribute " + boolAttributeName, t);
				}
			}
		}
		return usernames;
	}
	
	private List<Content> extractContentsForUser(IUserProfile profile, String eMail, List<Content> contents, 
			Map<String, List<String>> profileAttributes, NewsletterReport newsletterReport) throws ApsSystemException {
		NewsletterConfig config = this.getConfig();
		List<Content> userContents = new ArrayList<Content>();
		if (profile != null) {
			String username = profile.getUsername();
			String allContentsAttribute = config.getAllContentsAttributeName();
			boolean allContents = false;
			if (null != allContentsAttribute) {
				Boolean value = (Boolean) profile.getValue(allContentsAttribute);
				allContents = value != null && value;
			}
			List<String> groupNames = this.extractUserGroupNames(/*user*/username);
			boolean isGroupAdmin = groupNames.contains(Group.ADMINS_GROUP_NAME);
			for (int i=0; i<contents.size(); i++) {
				Content content = contents.get(i);
				String contentId = content.getId();
				List<String> contentProfileAttributes = profileAttributes.get(contentId);
				ContentReport contentReport = newsletterReport.getContentReport(contentId);
				if (contentReport != null && (isGroupAdmin || this.checkUserAllowedOnContent(groupNames, content))) {
					if (allContents) {
						userContents.add(content);
						contentReport.addRecipient(username, eMail);
					} else if (contentProfileAttributes!=null && contentProfileAttributes.size() > 0) {
						for (String profileAttrName : contentProfileAttributes) {
							Boolean value = (Boolean) profile.getValue(profileAttrName);
							if (value != null && value) {
								userContents.add(content);
								contentReport.addRecipient(username, eMail);
								break;
							}
						}
					}
				}
			}
		}
		return userContents;
	}
	
	private List<String> extractProfileAttributesForContent(Content content, Properties subscriptions) {
		Collection<String> categories = this.extractCategoryCodes(content);
		List<String> profileAttributes = new ArrayList<String>();
		for (String categoryCode : categories) {
			String attributeName = subscriptions.getProperty(categoryCode);
			if (attributeName != null) {
				profileAttributes.add(attributeName);
			}
		}
		return profileAttributes;
	}
	
	private String prepareMailBodyContentPart(Content content, String defaultLang, boolean isHtml) {
		NewsletterContentType contentType = this.getConfig().getContentTypes().get(content.getTypeCode());
		int modelId = isHtml ? contentType.getHtmlModel() : contentType.getSimpleTextModel();
		String mailContentBody = this.getContentRenderer().render(content, modelId, defaultLang, null);
		mailContentBody = this.getLinkResolver().resolveLinks(mailContentBody, null);
		if (!isHtml) {
			return StringEscapeUtils.unescapeHtml(mailContentBody);
		}
		return mailContentBody;
	}
	
	private List<String> extractUserGroupNames(String username) throws ApsSystemException {
		List<Authorization> authorizations = this.getAuthorizationManager().getUserAuthorizations(username);
		List<String> groupNames = new ArrayList<String>();
		if (null != authorizations) {
			for (int i = 0; i < authorizations.size(); i++) {
				Authorization authorization = authorizations.get(i);
				if (null != authorization && null != authorization.getGroup()) {
					String groupName = authorization.getGroup().getName();
					if (null != groupName && !groupNames.contains(groupName)) {
						groupNames.add(groupName);
					}
				}
			}
		}
		return groupNames;
	}
	
	private boolean checkUserAllowedOnContent(List<String> userGroups, Content content) {
		String mainGroup = content.getMainGroup();
		boolean allowed = false;
		if (Group.FREE_GROUP_NAME.equals(mainGroup) || userGroups.contains(mainGroup)) {
			allowed = true;
		} else if (!this.getConfig().isOnlyOwner()) {
			for (String current : content.getGroups()) {
				if (Group.FREE_GROUP_NAME.equals(current) || userGroups.contains(current)) {
					allowed = true;
					break;
				}
			}
		}
		return allowed;
	}
	
	private Set<String> extractCategoryCodes(Content content) {
		Set<String> categoryCodes = new HashSet<String>();
		Iterator<Category> categoryIter = content.getCategories().iterator();
		while (categoryIter.hasNext()) {
			Category category = categoryIter.next();
			this.addCategoryCode(category, categoryCodes);
		}
		return categoryCodes;
	}
	
	private void addCategoryCode(Category category, Set<String> codes) {
		codes.add(category.getCode());
		Category parentCategory = (Category) category.getParent();
		if (null != parentCategory && !parentCategory.getCode().equals(parentCategory.getParentCode())) {
			this.addCategoryCode(parentCategory, codes);
		}
	}
	
	@Override
	public List<String> loadNewsletterContentIds(EntitySearchFilter[] filters, 
			Collection<String> userGroupCodes, NewsletterSearchBean searchBean) throws ApsSystemException {
		List<String> contentsId = null;
		try {
			NewsletterConfig config = this.getConfig();
			String[] contentTypes = config.getContentTypesArray();
			if (contentTypes==null || contentTypes.length==0) {
				contentsId = new ArrayList<String>();
			} else {
				String[] categories = (null != config.getAllContentsAttributeName()) ? null : config.getCategoriesArray();
				contentsId = this.getNewsletterSearcherDAO().loadNewsletterContentsId(contentTypes, 
						categories, filters, userGroupCodes);
				Boolean inQueue = searchBean.getInQueue();
				if (inQueue != null) {
					List<String> contentQueue = this.getNewsletterDAO().loadContentQueue();
					if (inQueue) {
						contentsId = this.intersectContentIds(contentsId, contentQueue);
					} else {
						contentsId = this.exceptContentIds(contentsId, contentQueue);
					}
				}
				Boolean sent = searchBean.getSent();
				if (sent != null) {
					List<String> sentContentIds = this.getNewsletterDAO().loadSentContentIds();
					if (sent) {
						contentsId = this.intersectContentIds(contentsId, sentContentIds);
					} else {
						contentsId = this.exceptContentIds(contentsId, sentContentIds);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error sending newsletter", t);
		}
		return contentsId;
	}
	
	private List<String> exceptContentIds(List<String> contentIds, List<String> exceptedContentIds) {
		if (exceptedContentIds.size()>0 && contentIds.size()>0) {
			for (String contentId : exceptedContentIds) {
				contentIds.remove(contentId);
				if (contentIds.isEmpty()) {
					break;
				}
			}
		}
		return contentIds;
	}
	
	protected List<String> intersectContentIds(List<String> contentIds, List<String> intersectedContentIds) {
		List<String> intersection = new ArrayList<String>();
		if (intersectedContentIds.size()>0 && contentIds.size()>0) {
			for (String contentId : intersectedContentIds) {
				if (contentIds.remove(contentId)) {
					intersection.add(contentId);
				}
			}
		}
		return intersection;
	}
	
	
	@Override
	public List<Subscriber> loadSubscribers() throws ApsSystemException {
		List<Subscriber> subscribers = new ArrayList<Subscriber>();
		try {
			subscribers = this.getNewsletterDAO().loadSubscribers();
		} catch (Throwable t) {
			_logger.error("Error loading subscribers", t);
			throw new ApsSystemException("Error loading subscribers", t);
		}
		return subscribers;
	}
	
	@Override
	public List<Subscriber> searchSubscribers(String mailAddress, Boolean active) throws ApsSystemException {
		List<Subscriber> subscribers = new ArrayList<Subscriber>();
		try {
			subscribers = this.getNewsletterDAO().searchSubscribers(mailAddress, active);
		} catch (Throwable t) {
			_logger.error("Error searching subscribers", t);
			throw new ApsSystemException("Error searching subscribers", t);
		}
		return subscribers;
	}
	
	@Override
	public Subscriber loadSubscriber(String mailAddress) throws ApsSystemException {
		try {
			this.cleanOldSubscribers();
			Subscriber subscriber = this.getNewsletterDAO().loadSubscriber(mailAddress);
			return subscriber;
		} catch (Throwable t) {
			_logger.error("Error loading subscriber {}", mailAddress, t);
			throw new ApsSystemException("Error loading subscriber " + mailAddress, t);
		}
	}
	
	@Override
	public void addSubscriber(String mailAddress) throws ApsSystemException {
		try {
			Subscriber subscriber = new Subscriber();
			subscriber.setMailAddress(mailAddress);
			subscriber.setSubscriptionDate(new Date());
			subscriber.setActive(false);
			String token = this.createToken(mailAddress);
			this.getNewsletterDAO().addSubscriber(subscriber, token);
			this.sendSubscriptionMail(mailAddress, token);
		} catch (Throwable t) {
			_logger.error("Error adding subscriber {}", mailAddress, t);
			throw new ApsSystemException("Error adding subscriber", t);
		}
	}
	
	@Override
	public void resetSubscriber(String mailAddress) throws ApsSystemException {
		try {
			Subscriber subscriber = new Subscriber();
			subscriber.setMailAddress(mailAddress);
			subscriber.setSubscriptionDate(new Date());
			subscriber.setActive(false);
			String token = this.createToken(mailAddress);
			this.getNewsletterDAO().updateSubscriber(subscriber, token);
			this.sendSubscriptionMail(mailAddress, token);
		} catch (Throwable t) {
			_logger.error("Error reseting subscriber {}", mailAddress, t);
			throw new ApsSystemException("Error reseting subscriber", t);
		}
	}
	
	@Override
	public void deleteSubscriber(String mailAddress) throws ApsSystemException {
		try {
			this.getNewsletterDAO().deleteSubscriber(mailAddress);
		} catch (Throwable t) {
			_logger.error("Error deleting subscriber {}", mailAddress, t);
			throw new ApsSystemException("Error deleting subscriber", t);
		}
	}
	
	@Override
	public void activateSubscriber(String mailAddress, String token) throws ApsSystemException {
		try {
			this.cleanOldSubscribers();
			this.getNewsletterDAO().activateSubscriber(mailAddress);
		} catch (Throwable t) {
			_logger.error("Error activating subscriber {}", mailAddress, t);
			throw new ApsSystemException("Error activating subscriber", t);
		}
	}
	
	@Override
	public String getAddressFromToken(String token) throws ApsSystemException {
		try {
			String mailAddress = this.getNewsletterDAO().getAddressFromToken(token);
			return mailAddress;
		} catch (Throwable t) {
			_logger.error("Error loading address from token", t);
			throw new ApsSystemException("Error loading address from token", t);
		}
	}
	
	@Override
	public void cleanOldSubscribers() throws ApsSystemException {
		int days = this.getConfig().getSubscriptionTokenValidityDays();
		long time = new Date().getTime() - (86400000l * days);
		Date expiration = new Date(time);
		this.getNewsletterDAO().cleanOldSubscribers(expiration);
	}
	
	@Override
	public Boolean isAlreadyAnUser(String mailAddress) throws ApsSystemException {
		try {
			Collection<IApsEntity> profileTypes = this.getProfileManager().getEntityPrototypes().values();
			if (null == profileTypes || profileTypes.isEmpty()) {
				return false;
			}
			Iterator<IApsEntity> iter = profileTypes.iterator();
			while (iter.hasNext()) {
				IUserProfile type = (IUserProfile) iter.next();
				if (null != type.getMailAttributeName()) {
					EntitySearchFilter filterByEmail = new EntitySearchFilter(type.getMailAttributeName(), true, mailAddress, true);
					EntitySearchFilter[] filters = {filterByEmail};
					List<String> usernames = ((IEntityManager) this.getProfileManager()).searchId(filters);
					if (null != usernames && usernames.size() > 0) {
						return true;
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error on 'isAlreadyAnUser' method", t);
			throw new ApsSystemException("Error on 'isAlreadyAnUser' method", t);
		}
		return false;//(null != usernames && usernames.size() > 0);
	}
	
	protected String createToken(String word) throws NoSuchAlgorithmException {
		Random random = new Random();
		StringBuilder salt = new StringBuilder();
		long rndLong = random.nextLong();
		salt.append(rndLong);
		String date = DateConverter.getFormattedDate(new Date(), "SSSmmyyyy-SSS-MM:ssddmmHHmmEEE");
		salt.append(date);
		rndLong = random.nextLong();
		salt.append(rndLong);
		// genero il token in base a username e salt
		String token = ShaEncoder.encodeWord(word, salt.toString());
		return token;
	}
	
	protected void sendSubscriptionMail(String mailAddress, String token) throws ApsSystemException {
		try {
			EmailSenderThread thread = new EmailSenderThread(mailAddress, token, this);
			thread.setName(JpnewsletterSystemConstants.EMAIL_SENDER_NAME_THREAD_PREFIX + System.currentTimeMillis());
			thread.start();
		} catch (Throwable t) {
			_logger.error("Sending email", t);
			throw new ApsSystemException("Sending email", t);
		}
	}
	
	protected String createLink(String mailAddress, String token) {
		NewsletterConfig config = this.getConfig();
		String pageCode = config.getSubscriptionPageCode();
		IPage page = this.getPageManager().getPage(pageCode);
		Lang lang = this.getLangManager().getDefaultLang();
		if (null == page || null == lang) {
			if (null == page) {
				_logger.warn("null subscriprion page {}", pageCode);
			}
			return "";
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		params.put("mailAddress", mailAddress);
		return this.getUrlManager().createUrl(page, lang, params, false);
	}
	
	protected String parseText(String defaultText, Map<String, String> params) {
		String body = defaultText;
		for (Entry<String, String> pairs : params.entrySet()) {
			String regExp = "\\{" + pairs.getKey() + "\\}";
			Pattern pattern = Pattern.compile(regExp);
			Matcher codeMatcher = pattern.matcher("");
			codeMatcher.reset(body);
			if (codeMatcher.find()) {
				body = codeMatcher.replaceAll((String) pairs.getValue());
			}
		}
		return body;
	}
	
	protected void sendSubscriptionFormThread(String mailAddress, String token) throws ApsSystemException {
		try {
			NewsletterConfig config = this.getConfig();
			String senderCode = config.getSenderCode();
			String subject = config.getSubscriptionSubject();
			if (subject!=null) {
				Map<String, String> bodyParams = new HashMap<String, String>();
				String link = this.createLink(mailAddress, token);
				bodyParams.put("subscribeLink", link);
				String textBody = this.parseText(config.getSubscriptionTextBody(), bodyParams);
				String[] recipientsTo = new String[] { mailAddress };
				if (config.isAlsoHtml()) {
					String htmlBody = this.parseText(config.getSubscriptionHtmlBody(), bodyParams);
					this.getMailManager().sendMixedMail(textBody, htmlBody, subject, null, recipientsTo, null, null, senderCode);
				} else {
					this.getMailManager().sendMail(textBody, subject, recipientsTo, null, null, senderCode, IMailManager.CONTENTTYPE_TEXT_PLAIN);
				}
			} else {
				_logger.warn("Incomplete configuration for newsletter subscribers! CHECK {} item!!", JpnewsletterSystemConstants.NEWSLETTER_CONFIG_ITEM);
			}
		} catch (Throwable t) {
			_logger.error("Error on 'sendSubscriptionFormThread' method", t);
			throw new ApsSystemException("Error sending email for subscription confirmation request to address " + mailAddress, t);
		}
	}
	
	private void sendNewsletterToSubscribers(List<Content> contents, NewsletterReport newsletterReport) throws ApsSystemException {
		List<Content> contentsToSubscribers = new ArrayList<Content>();
		for (int i = 0; i < contents.size(); i++) {
			Content content = contents.get(i);
			if (this.isContentToSend(content)) {
				contentsToSubscribers.add(content);
			}
		}
		this.sendContentsToSubscribers(contentsToSubscribers, newsletterReport);
	}
	
	private boolean isContentToSend(Content content) {
		if (null == content) return false;
		String mainGroup = content.getMainGroup();
		if (null != mainGroup && Group.FREE_GROUP_NAME.equals(mainGroup)) return true;
		Set<String> groups = content.getGroups();
		if (null != groups && groups.contains(Group.FREE_GROUP_NAME)) {
			return true;
		}
		return false;
	}
	
	private void sendContentsToSubscribers(List<Content> contents, NewsletterReport newsletterReport) throws ApsSystemException {
		List<Subscriber> subscribers = this.searchSubscribers(null, Boolean.TRUE);
		NewsletterConfig config = this.getConfig();
		for (Subscriber subscriber : subscribers) {
			String mailAddress = subscriber.getMailAddress();
			String[] emailAddresses = { mailAddress };
			String simpleText = this.prepareSubscribersMailBody(contents, newsletterReport, false, mailAddress);
			if (config.isAlsoHtml()) {
				String htmlText = this.prepareSubscribersMailBody(contents, newsletterReport, true, mailAddress);
				this.getMailManager().sendMixedMail(simpleText, htmlText, config.getSubject(), null, null, null, emailAddresses, config.getSenderCode());
			} else {
				this.getMailManager().sendMail(simpleText, config.getSubject(), null, null, emailAddresses, config.getSenderCode());
			}
		}
	}
	
	private String prepareSubscribersMailBody(List<Content> userContents, NewsletterReport newsletterReport, boolean isHtml, String mailAddress) {
		NewsletterConfig config = this.getConfig();
		String unsubscriptionLink = isHtml ? config.getSubscribersHtmlFooter() : config.getSubscribersTextFooter();
		if (unsubscriptionLink!=null) {
			StringBuffer body = this.prepareMailCommonBody(userContents, newsletterReport, isHtml);
			String link = this.createUnsubscriptionLink(mailAddress);
			Map<String, String> footerParams = new HashMap<String, String>();
			footerParams.put("unsubscribeLink", link);
			String unlink = this.parseText(unsubscriptionLink, footerParams);
			body.append(unlink);
			return body.toString();
		} else {
			_logger.warn("Incomplete configuration for newsletter subscribers! CHECK {} item!!", JpnewsletterSystemConstants.NEWSLETTER_CONFIG_ITEM);
			return this.prepareMailBody(userContents, newsletterReport, isHtml);
		}
	}
	
	protected String createUnsubscriptionLink(String mailAddress) {
		NewsletterConfig config = this.getConfig();
		String pageCode = config.getUnsubscriptionPageCode();
		IPage page = this.getPageManager().getPage(pageCode);
		Lang lang = this.getLangManager().getDefaultLang();
		if (null == page || null == lang) {
			if (null == page) {
				_logger.warn("null unsubscriprion page {}", pageCode);
			}
			return "";
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("mailAddress", mailAddress);
		return this.getUrlManager().createUrl(page, lang, params, false);
	}
	
	protected boolean isSendingNewsletter() {
		return _sendingNewsletter;
	}
	protected void setSendingNewsletter(boolean sendingNewsletter) {
		this._sendingNewsletter = sendingNewsletter;
	}
	
	protected NewsletterConfig getConfig() {
		return this._config;
	}
	protected void setConfig(NewsletterConfig config) {
		this._config = config;
	}
	
	@Override
	public String[] getSenderCodes() {
		return new String[] { this.getConfig().getSenderCode() };
	}
	
	protected IMailManager getMailManager() {
		return _mailManager;
	}
	public void setMailManager(IMailManager mailManager) {
		this._mailManager = mailManager;
	}
	
	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	
	protected IURLManager getUrlManager() {
		return _urlManager;
	}
	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	protected IUserProfileManager getProfileManager() {
		return _profileManager;
	}
	public void setProfileManager(IUserProfileManager profileManager) {
		this._profileManager = profileManager;
	}
	
	protected ILinkResolverManager getLinkResolver() {
		return _linkResolver;
	}
	public void setLinkResolver(ILinkResolverManager linkResolver) {
		this._linkResolver = linkResolver;
	}
	
	protected ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
	protected IContentRenderer getContentRenderer() {
		return _contentRenderer;
	}
	public void setContentRenderer(IContentRenderer renderer) {
		this._contentRenderer = renderer;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}
	
	protected INewsletterDAO getNewsletterDAO() {
		return _newsletterDAO;
	}
	public void setNewsletterDAO(INewsletterDAO newsletterDAO) {
		this._newsletterDAO = newsletterDAO;
	}
	
	protected INewsletterSearcherDAO getNewsletterSearcherDAO() {
		return _newsletterSearcherDAO;
	}
	public void setNewsletterSearcherDAO(INewsletterSearcherDAO newsletterSearcherDAO) {
		this._newsletterSearcherDAO = newsletterSearcherDAO;
	}
	
	private NewsletterConfig _config;
	private boolean _sendingNewsletter = false;
	
	private IMailManager _mailManager;
	private IUserProfileManager _profileManager;
	private IUserManager _userManager;
	private IContentManager _contentManager;
	private IAuthorizationManager _authorizationManager;
	private IURLManager _urlManager;
	private IPageManager _pageManager;
	private ILinkResolverManager _linkResolver;
	private ILangManager _langManager;
	private IContentRenderer _contentRenderer;
	private ConfigInterface _configManager;
	private INewsletterDAO _newsletterDAO;
	private INewsletterSearcherDAO _newsletterSearcherDAO;
	private IKeyGeneratorManager _keyGeneratorManager;
	
	private TimerTask _scheduler;
	
}