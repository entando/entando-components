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
package com.agiletec.plugins.jpnewsletter.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.plugins.jpnewsletter.aps.system.JpnewsletterSystemConstants;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterSchedulerManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.ContentReport;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterReport;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

public class JpnewsletterTestHelper extends AbstractDAO {
	
	public JpnewsletterTestHelper(IUserManager userManager, IUserProfileManager profileManager, ConfigInterface configManager) {
		this._userManager = userManager;
		this._profileManager = profileManager;
		this._configManager = configManager;
		this._config = this._configManager.getConfigItem(JpnewsletterSystemConstants.NEWSLETTER_CONFIG_ITEM);
	}
	
	public void resetConfig() throws Exception {
		this._configManager.updateConfigItem(JpnewsletterSystemConstants.NEWSLETTER_CONFIG_ITEM, this._config);
		// TODO Verificare
//		try {
//			((AbstractService) this._profileManager).refresh();
//		} catch (Throwable t) {
//			throw new ApsSystemException("Error in resetConfig", t);
//		}
	}
	
	public void setNewsletterManagerThreadDelay(long delayMillisec, boolean active, 
			String allContentsAttributeName, INewsletterManager newsletterManager) throws Throwable {
		NewsletterConfig config = newsletterManager.getNewsletterConfig();
		config.setActive(active);
		config.setAllContentsAttributeName(allContentsAttributeName);
		Date schedulerStartTime = new Date(new Date().getTime() + delayMillisec);
		config.setStartScheduler(schedulerStartTime);
		newsletterManager.updateNewsletterConfig(config);
		((INewsletterSchedulerManager) newsletterManager).refresh();
	}
	
	public void deleteNewsletters() {
		Connection conn = null;
		try {
			conn = this.getConnection();
			this.executeQuery(DELETE_NEWSLETTER_CONTENTQUEUE, conn);
			this.executeQuery(DELETE_CONTENT_RECIPIENTS, conn);
			this.executeQuery(DELETE_CONTENT_REPORTS, conn);
			this.executeQuery(DELETE_NEWSLETTER_REPORTS, conn);
		} catch (Throwable t) {
			processDaoException(t, "Errore in rimozione newsletter", "deleteNewsletters");
		} finally {
			closeConnection(conn);
		}
	}
	
	public void addUser(String username, String nome, String cognome, String email, boolean cat1, boolean cat2) throws ApsSystemException {
		User user = new User();
		user.setUsername(username);
		user.setPassword(username);
		user.setDisabled(false);
		this._userManager.addUser(user);
		this.addProfile(username, nome, cognome, email, cat1, cat2);
	}
	
	public void addProfile(String username, String nome, String cognome, String email, boolean cat1, boolean cat2) throws ApsSystemException {
		IUserProfile profile = this.createProfile(nome, cognome, email, cat1, cat2);
		this._profileManager.addProfile(username, profile);
	}
	
	public void deleteProfile(String username) throws ApsSystemException {
		this._profileManager.deleteProfile(username);
	}
	
	public void deleteUser(String username) throws ApsSystemException {
		this.deleteProfile(username);
		this._userManager.removeUser(username);
	}
	
	public void joinWithSenderThread(String name) throws Throwable {
		Thread[] threads = new Thread[100];
		Thread.enumerate(threads);
		for (Thread thread : threads) {
			if (thread==null) {
				break;
			}
			if (thread.isAlive() && thread.getName().contains(name)) {
				thread.join();
				break;
			}
		}
	}
	
	protected IUserProfile createProfile(String nome, String cognome, String email, boolean cat1, boolean cat2) {
		IUserProfile profile = _profileManager.getDefaultProfileType();
		ITextAttribute fullnameAttr = (ITextAttribute) profile.getAttribute("fullname");
		fullnameAttr.setText(nome, null);
		ITextAttribute emailAttr = (ITextAttribute) profile.getAttribute("email");
		emailAttr.setText(email, null);
		BooleanAttribute boolean1 = (BooleanAttribute) profile.getAttribute("boolean1");
		boolean1.setBooleanValue(new Boolean(cat1));
		BooleanAttribute boolean2 = (BooleanAttribute) profile.getAttribute("boolean2");
		boolean2.setBooleanValue(new Boolean(cat2));
		return profile;
	}
	
	public NewsletterReport createNewsletterReport(int id, Date sendDate, String subject) {
		NewsletterReport newsletterReport = new NewsletterReport();
		newsletterReport.setId(id);
		newsletterReport.setSendDate(sendDate);
		newsletterReport.setSubject(subject);
		return newsletterReport;
	}
	
	public ContentReport createContentReport(int id, int newsletterId, 
			String contentId, String textBody, String htmlBody) {
		ContentReport contentReport = new ContentReport();
		contentReport.setId(id);
		contentReport.setContentId(contentId);
		contentReport.setTextBody(textBody);
		contentReport.setHtmlBody(htmlBody);
		return contentReport;
	}
	
	private void executeQuery(String query, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Errore in esecuzione query " + query, "executeQuery");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	public void cleanAddresses() throws Throwable {
		this.cleanAddresses(CLEAN_TOKEN);
		this.cleanAddresses(CLEAN_SUBSCRIBERS);
	}
	
	private void cleanAddresses(String query) throws Throwable {
		Connection conn = null;
		Statement stat = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			stat.executeUpdate(query);
		} catch (Throwable t) {
			throw t;
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	public String getToken(String mailAddress) throws Throwable {
		String token = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_TOKEN);
			stat.setString(1, mailAddress);
			res = stat.executeQuery();
			if (res.next()) {
				token = res.getString(1);
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return token;
	}
	
	private IUserManager _userManager;
	private IUserProfileManager _profileManager;
	private ConfigInterface _configManager;
	
	private String _config;
	
	private final String DELETE_NEWSLETTER_CONTENTQUEUE = 
		"DELETE FROM jpnewsletter_contentqueue";
	
	private final String DELETE_NEWSLETTER_REPORTS = 
		"DELETE FROM jpnewsletter_newsletterreport";
	
	private final String DELETE_CONTENT_REPORTS = 
		"DELETE FROM jpnewsletter_contentreport";
	
	private final String DELETE_CONTENT_RECIPIENTS = 
		"DELETE FROM jpnewsletter_recipient";
	
	private final String CLEAN_TOKEN = 
		"DELETE FROM jpnewsletter_subscribertokens ";
	
	private final String CLEAN_SUBSCRIBERS = 
		"DELETE FROM jpnewsletter_subscribers ";
	
	private final String GET_TOKEN = 
		"SELECT token FROM jpnewsletter_subscribertokens WHERE mailaddress = ?";
	
	public static final String MAIL_ADDRESS = "address@change.me";//TODO INSERIRE UN INDIRIZZO E-MAIL VALIDO -solo l'indirizzo, privo di <> e ""
	
}