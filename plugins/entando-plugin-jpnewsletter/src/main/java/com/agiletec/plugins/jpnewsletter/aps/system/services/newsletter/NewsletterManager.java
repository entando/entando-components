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

import org.apache.commons.lang.StringEscapeUtils;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.authorization.authorizator.IApsAuthorityManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.system.services.lang.ILangManager;
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
import java.security.NoSuchAlgorithmException;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * Servizio gestore delle newsletter.
 * @author E.Santoboni, A.Turrini, E.Mezzano
 */
public class NewsletterManager extends AbstractService 
		implements INewsletterManager, MailSendersUtilizer, INewsletterSchedulerManager {
	
	@Override
	public void init() throws Exception {
		this.loadConfigs();
		this.startScheduler();
		ApsSystemUtils.getLogger().debug(this.getClass().getName() + ": initialized");
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
	
	/**
	 * Carica e salva la configurazione di sistema del servizio.
	 * @throws ApsSystemException
	 */
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
			ApsSystemUtils.logThrowable(t, this, "loadConfigs");
			throw new ApsSystemException("Errore in fase di inizializzazione", t);
		}
	}
	
	@Override
	public void startScheduler() throws ApsSystemException {
		NewsletterConfig config = this.getConfig();
		Date start = config.getNextTaskTime();
		this._scheduler = new Scheduler(this, start, config.getHoursDelay());
	}
	
	/**
	 * Restart the scheduler at the intended time and NOT at the next iteration. This may
	 * result in an immediate execution of the delivery process
	 * @param config
	 */
	private void restartScheduler(NewsletterConfig config) {
		Date start = config.getStartScheduler();
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
		Date originalStartDate = null;
		try {
			originalStartDate = this.getConfig().getStartScheduler();
			String xml = new NewsletterConfigDOM().createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpnewsletterSystemConstants.NEWSLETTER_CONFIG_ITEM, xml);
			this.setConfig(config);
			// restart the scheduler if necessary
			if (originalStartDate.getTime() != config.getStartScheduler().getTime()) {
				ApsSystemUtils.getLogger().info("Newsletter: scheduler restart issued");
				stopScheduler();
				restartScheduler(config);
				ApsSystemUtils.getLogger().info("Newsletter: scheduler restart completed");
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateNewsletterConfig");
			throw new ApsSystemException("Errore in fase di aggiornamento configurazione newsletter", t);
		}
	}
	
	@Override
	public void addContentToQueue(String contentId) throws ApsSystemException {
		try {
			this.getNewsletterDAO().addContentToQueue(contentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addContentToQueue");
			// Do not throws exceptions
		}
	}
	
	@Override
	public void removeContentFromQueue(String contentId) throws ApsSystemException {
		try {
			this.getNewsletterDAO().deleteContentFromQueue(contentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeContentFromQueue");
			throw new ApsSystemException("Errore in aggiunta contenuto in coda newsLetter", t);
		}
	}
	
	@Override
	public List<String> getContentQueue() throws ApsSystemException {
		try {
			return this.getNewsletterDAO().loadContentQueue();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentQueue");
			throw new ApsSystemException("Errore in caricamento coda contenuti newsLetter", t);
		}
	}
	
	@Override
	public boolean existsContentReport(String contentId) throws ApsSystemException {
		try {
			return this.getNewsletterDAO().existsContentReport(contentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentQueue");
			throw new ApsSystemException("Error verifying content report existence", t);
		}
	}
	
	@Override
	public List<String> getSentContentIds() throws ApsSystemException {
		try {
			return this.getNewsletterDAO().loadSentContentIds();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentQueue");
			throw new ApsSystemException("Error loading sent contents ids", t);
		}
	}
	
	@Override
	public NewsletterContentReportVO getContentReport(String contentId) throws ApsSystemException {
		try {
			return this.getNewsletterDAO().loadContentReport(contentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentQueue");
			throw new ApsSystemException("Error loading content report", t);
		}
	}
	
	/**
	 * Istanzia un thread che esegue l'invio della newsletter. 
	 * Verifica che in ogni istante sia in esecuzione al più un processo di invio della newsletter.
	 * @throws ApsSystemException In caso di errore.
	 */
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
			ApsSystemUtils.getLogger().info("Newletter: delivery process initiated"); 
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
					ApsSystemUtils.getLogger().info("Newletter: had some contents ids selected, corresponding to no actual content: this is quite strange!");
				}
				this.getNewsletterDAO().cleanContentQueue(contentIds);
			} else {
				ApsSystemUtils.getLogger().info("Newletter: no contents found for delivery");
			}
			ApsSystemUtils.getLogger().info("Newletter: delivery process completed"); 
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().info("Newletter: delivery process ended abnormally");
			ApsSystemUtils.logThrowable(t, this, "sendNewsletterFromThread");
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
			ApsSystemUtils.logThrowable(t, this, "buildMailBody");
			throw new ApsSystemException("Errore in generazione body contenuto " + content.getId(), t);
		}
		return mailBody;
	}
	
	/**
	 * Invia i contenuti dati agli utenti registrati alla newsletter discriminando, per ogni utente, 
	 * i contenuti a lui visibili e per i quali ha fatto implicita richiesta nel proprio profilo.
	 * @param contents La lista dei contenuti da inviare tramite newsletter.
	 * @throws ApsSystemException In caso di errore.
	 */
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
				ApsSystemUtils.getLogger().error("Newsletter: no receivers to send newsletter to!");
			}
			this.sendNewsletterToSubscribers(contents, newsletterReport);
			this.addNewsletterReport(newsletterReport);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "sendNewsletterToUsers");
			throw new ApsSystemException("Error sending Newsletter To Users ", t);
		}
	}
	
	/**
	 * Salva il report della newsletter appena inviata.
	 * @param newsletterReport Il report della newsletter.
	 * @throws ApsSystemException In caso di errore.
	 */
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
			ApsSystemUtils.logThrowable(t, this, "buildMailBody", "Error adding newsletter report : id " + newsletterReport.getId());
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
				ApsSystemUtils.getLogger().info(" Newsletter content " + content.getId() + " not added, because has not model in config.");
			}
		}
		return newsletterReport;
	}
	
	private void sendNewsletterToUser(String username, List<Content> contents, 
			Map<String, List<String>> profileAttributes, NewsletterReport newsletterReport) {
		NewsletterConfig config = this.getConfig();
		try {
			UserDetails user = this.getUserManager().getUser(username);
			IUserProfile profile = (IUserProfile) user.getProfile();
			if (profile != null) {
				String eMail = (String) profile.getValue(config.getMailAttrName());
				if (eMail != null && eMail.length() > 0) {
					List<Content> userContents = this.extractContentsForUser(user, eMail, contents, profileAttributes, newsletterReport);
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
			ApsSystemUtils.logThrowable(t, this, "sendNewsletterToUser");
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
	
	/**
	 * Prepara il body della mail in base al contenuto e al modello.
	 * @param content Il Contenuto per cui costruire il body della mail.
	 * @param modelId L'id del modello utilizzato.
	 * @param html Indica se il modello è di tipo html o testo semplice.
	 * @return Il body della mail completo di blocchi iniziale e finale.
	 * @throws ApsSystemException
	 */
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
	
	/**
	 * Estrae gli username dei destinatari della newsletter.
	 * @return Il set contenente gli username dei destinatari della newsletter.
	 * @throws ApsSystemException
	 */
	private Set<String> extractUsernames() throws ApsSystemException {
		NewsletterConfig config = this.getConfig();
		Set<String> usernames = new HashSet<String>();
		String allContentsAttribute = config.getAllContentsAttributeName();
		if (null != allContentsAttribute && allContentsAttribute.trim().length() > 0) {
			EntitySearchFilter filter = new EntitySearchFilter(allContentsAttribute, true, new Boolean(true), false);
			EntitySearchFilter[] filters = {filter};
			try {
				List<String> usernamesForAllContents = ((IEntityManager) this.getProfileManager()).searchId(filters);
				usernames.addAll(usernamesForAllContents);
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "extractUsernames");
				throw new ApsSystemException("Error searching for usernames on profile's attribute " + allContentsAttribute, t);
			}
		}
		Iterator<Object> subscriptionsIter = config.getSubscriptions().values().iterator();
		while (subscriptionsIter.hasNext()) {
			String boolAttributeName = (String) subscriptionsIter.next();
			if (null != boolAttributeName && boolAttributeName.trim().length() > 0) {
				EntitySearchFilter filter = new EntitySearchFilter(boolAttributeName, true, new Boolean(true), false);
				EntitySearchFilter[] filters = {filter};
				try {
					List<String> usernamesForCategory = ((IEntityManager) this.getProfileManager()).searchId(filters);
					usernames.addAll(usernamesForCategory);
				} catch (Throwable t) {
					ApsSystemUtils.logThrowable(t, this, "extractUsernames");
					throw new ApsSystemException("Error searching for usernames on profile's attribute " + boolAttributeName, t);
				}
			}
		}
		return usernames;
	}
	
	private List<Content> extractContentsForUser(UserDetails user, String eMail, List<Content> contents, 
			Map<String, List<String>> profileAttributes, NewsletterReport newsletterReport) throws ApsSystemException {
		NewsletterConfig config = this.getConfig();
		List<Content> userContents = new ArrayList<Content>();
		String username = user.getUsername();
		IUserProfile profile = (IUserProfile) user.getProfile();
		if (profile != null) {
			String allContentsAttribute = config.getAllContentsAttributeName();
			boolean allContents = false;
			if (null != allContentsAttribute) {
				Boolean value = (Boolean) profile.getValue(allContentsAttribute);
				allContents = value != null && value.booleanValue();
			}
			List<String> groupNames = this.extractUserGroupNames(user);
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
							if (value != null && value.booleanValue()) {
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
	
	private List<String> extractUserGroupNames(UserDetails user) throws ApsSystemException {
		List<IApsAuthority> authorities = this.getGroupManager().getAuthorizationsByUser(user);
		List<String> groupNames = new ArrayList<String>(authorities.size());
		for (IApsAuthority authority : authorities) {
			groupNames.add(authority.getAuthority());
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
	
	/**
	 * Estrae i codici delle categorie della newsletter
	 * @param content Il contenuto a cui si riferisce la newsletter.
	 * @return Il set contenente i codici delle categorie della newsletter come {@link String}.
	 */
	private Set<String> extractCategoryCodes(Content content) {
		Set<String> categoryCodes = new HashSet<String>();
		Iterator<Category> categoryIter = content.getCategories().iterator();
		while (categoryIter.hasNext()) {
			Category category = categoryIter.next();
			this.addCategoryCode(category, categoryCodes);
		}
		return categoryCodes;
	}
	
	/**
	 * Aggiunge al {@link Set} dei codici il codice della categoria data.
	 * @param category La categoria da aggiungere.
	 * @param codes Il {@link Set} dei codici delle categorie.
	 */
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
				if (inQueue!=null) {
					List<String> contentQueue = this.getNewsletterDAO().loadContentQueue();
					if (inQueue.booleanValue()) {
						contentsId = this.intersectContentIds(contentsId, contentQueue);
					} else {
						contentsId = this.exceptContentIds(contentsId, contentQueue);
					}
				}
				Boolean sent = searchBean.getSent();
				if (sent!=null) {
					List<String> sentContentIds = this.getNewsletterDAO().loadSentContentIds();
					if (sent.booleanValue()) {
						contentsId = this.intersectContentIds(contentsId, sentContentIds);
					} else {
						contentsId = this.exceptContentIds(contentsId, sentContentIds);
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadNewsletterContentsId", 
					"Errore spedizione lista contenuti newsletter ");
		}
		return contentsId;
	}
	
	private List<String> exceptContentIds(List<String> contentIds, List<String> exceptedContentIds) {
		if (exceptedContentIds.size()>0 && contentIds.size()>0) {
			for (String contentId : exceptedContentIds) {
				contentIds.remove(contentId);
				if (contentIds.size()==0) {
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
			ApsSystemUtils.logThrowable(t, this, "loadSubscribers");
			throw new ApsSystemException(
					"Errore caricamento di tutte le sottoscrizioni", t);
		}
		return subscribers;
	}
	
	@Override
	public List<Subscriber> searchSubscribers(String mailAddress, Boolean active) throws ApsSystemException {
		List<Subscriber> subscribers = new ArrayList<Subscriber>();
		try {
			subscribers = this.getNewsletterDAO().searchSubscribers(mailAddress, active);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchSubscribers");
			throw new ApsSystemException("Errore in ricerca sottoscrizioni", t);
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
			ApsSystemUtils.logThrowable(t, this, "loadSubscriber");
			throw new ApsSystemException("Errore caricamento di un sottoscrizioni " + mailAddress, t);
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
			ApsSystemUtils.logThrowable(t, this, "addSubscriber");
			throw new ApsSystemException("Errore in aggiunta sottoscrizione", t);
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
			ApsSystemUtils.logThrowable(t, this, "resetSubscriber");
			throw new ApsSystemException("Errore in reset sottoscrizione", t);
		}
	}
	
	@Override
	public void deleteSubscriber(String mailAddress) throws ApsSystemException {
		try {
			this.getNewsletterDAO().deleteSubscriber(mailAddress);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteSubscriber");
			throw new ApsSystemException("Errore cancellazione sottoscrizione", t);
		}
	}
	
	@Override
	public void activateSubscriber(String mailAddress, String token) throws ApsSystemException {
		try {
			this.cleanOldSubscribers();
			this.getNewsletterDAO().activateSubscriber(mailAddress);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "activateSubscriber");
			throw new ApsSystemException("Errore attivazione sottoscrizione", t);
		}
	}
	
	@Override
	public String getAddressFromToken(String token) throws ApsSystemException {
		try {
			String mailAddress = this.getNewsletterDAO().getAddressFromToken(token);
			return mailAddress;
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAddressFromToken");
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
		List<String> usernames = null;
		try {
			String emailAttributeName = this.getConfig().getMailAttrName();
			EntitySearchFilter filterByEmail = new EntitySearchFilter(emailAttributeName, true, mailAddress, true);
			EntitySearchFilter[] filters = {filterByEmail};
			usernames = ((IEntityManager) this.getProfileManager()).searchId(filters);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "isAlreadyAnUser");
			throw new ApsSystemException("Errore ricerca indirizzo e-mail tra gli utenti registrati", t);
		}
		return (null != usernames && usernames.size() > 0);
	}
	
	/**
	 * Generated random token 
	 */
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
			ApsSystemUtils.logThrowable(t, this, "sendMail");
			throw new ApsSystemException("errore in invio email", t);
		}
	}
	
	/**
	 * Create link for email confirmation
	 * */
	protected String createLink(String mailAddress, String token) {
		NewsletterConfig config = this.getConfig();
		String langcode = this.getLangManager().getDefaultLang().getCode();
		String pageCode = config.getSubscriptionPageCode();
		String applBaseUrl = this.getConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL);
		StringBuffer link = new StringBuffer(applBaseUrl);
		link.append(langcode);
		link.append("/");
		link.append(pageCode);
		link.append(".wp");
		link.append("?mailAddress=");
		link.append(mailAddress);
		link.append("&token=");
		link.append(token);
		return link.toString();
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
				ApsSystemUtils.getLogger().warn("Incomplete configuration for newsletter subscribers! CHECK " + JpnewsletterSystemConstants.NEWSLETTER_CONFIG_ITEM + " item!!");
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "sendMail");
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
		List<Subscriber> subscribers = this.searchSubscribers(null, new Boolean(true));
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
			ApsSystemUtils.getLogger().warn("Incomplete configuration for newsletter subscribers! CHECK " + JpnewsletterSystemConstants.NEWSLETTER_CONFIG_ITEM + " item!!");
			return this.prepareMailBody(userContents, newsletterReport, isHtml);
		}
	}
	
	/**
	 * Create link for newsletter unsubscription
	 * */
	protected String createUnsubscriptionLink(String mailAddress) {
		NewsletterConfig config = this.getConfig();
		String langcode = this.getLangManager().getDefaultLang().getCode();
		String pageCode = config.getUnsubscriptionPageCode();
		String applBaseUrl = this.getConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL);
		StringBuffer link = new StringBuffer(applBaseUrl);
		link.append(langcode);
		link.append("/");
		link.append(pageCode);
		link.append(".wp");
		link.append("?mailAddress=");
		link.append(mailAddress);
		return link.toString();
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
	
	public IApsAuthorityManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IApsAuthorityManager groupManager) {
		this._groupManager = groupManager;
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
	
	/**
	 * Returns the configuration manager.
	 * @return The Configuration manager.
	 */
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	/**
	 * Set method for Spring bean injection.<br /> Set the Configuration manager.
	 * @param configManager The Configuration manager.
	 */
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
	private IApsAuthorityManager _groupManager;
	private ILinkResolverManager _linkResolver;
	private ILangManager _langManager;
	private IContentRenderer _contentRenderer;
	private ConfigInterface _configManager;
	private INewsletterDAO _newsletterDAO;
	private INewsletterSearcherDAO _newsletterSearcherDAO;
	private IKeyGeneratorManager _keyGeneratorManager;
	
	private TimerTask _scheduler;
	
}