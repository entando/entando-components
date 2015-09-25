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

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.ContentReport;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentReportVO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterReport;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.Subscriber;

/**
 * @author E.Mezzano
 */
public class NewsletterDAO extends AbstractDAO implements INewsletterDAO {
	
	@Override
	public void addContentToQueue(String contentId) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_CONTENT_TO_QUEUE);
			stat.setString(1, contentId);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error putting content into queue", "addContentToQueue");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void deleteContentFromQueue(String contentId) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_CONTENT_FROM_QUEUE);
			stat.setString(1, contentId);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error removing content from queue", "deleteContentFromQueue");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public List<String> loadContentQueue() {
		List<String> queue = new ArrayList<String>();
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_CONTENT_QUEUE);
			while (res.next()) {
				queue.add(res.getString(1));
			}
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error loading content queue", "loadContentQueue");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return queue;
	}
	
	@Override
	public void cleanContentQueue(List<String> queue) {
		Connection conn = null;
		PreparedStatement stat = null;
		if (queue != null && queue.size() > 0) {
			try {
				conn = this.getConnection();
				conn.setAutoCommit(false);
				StringBuffer query = new StringBuffer(CLEAN_CONTENT_QUEUE_PREFIX);
				int size = queue.size();
				query.append(" ?");
				for (int i=1; i<size; i++) {
					query.append(", ?");
				}
				query.append(" ) ");
				stat = conn.prepareStatement(query.toString());
				int index = 1;
				for (String id : queue) {
					stat.setString(index++, id);
				}
				stat.executeUpdate();
				conn.commit();
			} catch (Throwable t) {
				this.executeRollback(conn);
				processDaoException(t, "Error cleaning content queue", "cleanContentQueue");
			} finally {
				closeDaoResources(null, stat, conn);
			}
		}
	}
	
	@Override
	public void addNewsletterReport(NewsletterReport newsletterReport) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.addNewsletterReport(newsletterReport, conn);
			this.addContentReports(newsletterReport, conn);
			this.addContentRecipients(newsletterReport, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			this.processDaoException(t, "Error adding newsletter report", "addNewsletterReport");
		} finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public NewsletterContentReportVO loadContentReport(String contentId) {
		NewsletterContentReportVO contentReport = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			contentReport = this.getContentReport(contentId, conn);
			if (contentReport != null) {
				Map<String, String> recipients = this.getContentRecipients(contentReport.getId(), conn);
				contentReport.setRecipients(recipients);
			}
		} catch (Throwable t) {
			this.processDaoException(t, "Error loading content report", "loadContentReport");
		} finally {
			closeConnection(conn);
		}
		return contentReport;
	}
	
	@Override
	public List<String> loadSentContentIds() {
		List<String> sentContentIds = new ArrayList<String>();
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_SENT_CONTENTS);
			while (res.next()) {
				sentContentIds.add(res.getString(1));
			}
		} catch (Throwable t) {
			processDaoException(t, "Error loading sent contents ids", "loadSentContentIds");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return sentContentIds;
	}
	
	@Override
	public boolean existsContentReport(String contentId) {
		boolean sent = false;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(EXISTS_CONTENT_REPORT);
			stat.setString(1, contentId);
			res = stat.executeQuery();
			if (res.next()) {
				sent = true;
			}
		} catch (Throwable t) {
			processDaoException(t, "Error verifying content report existence", "existsContentReport");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return sent;
	}
	
	private void addNewsletterReport(NewsletterReport newsletterReport, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_NEWSLETTER_REPORT);
			stat.setInt(1, newsletterReport.getId());
			stat.setTimestamp(2, new Timestamp(newsletterReport.getSendDate().getTime()));
			stat.setString(3, newsletterReport.getSubject());
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Error adding report for sent newsletter", "addNewsletterReport");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	private void addContentReports(NewsletterReport newsletterReport, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_CONTENT_REPORT);
			int reportId = newsletterReport.getId();
			for (ContentReport contentReport : newsletterReport.getContentReports().values()) {
				stat.setInt(1, contentReport.getId());
				stat.setInt(2, reportId);
				stat.setString(3, contentReport.getContentId());
				stat.setString(4, contentReport.getTextBody());
				stat.setString(5, contentReport.getHtmlBody());
				stat.addBatch();
				stat.clearParameters();
			}
			stat.executeBatch();
		} catch (BatchUpdateException e) {
			this.processDaoException(e.getNextException(), "Error adding contents for sent newsletter", 
					"addContentReports");
		} catch (Throwable t) {
			this.processDaoException(t, "Error adding contents for sent newsletter", "addContentReports");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	private void addContentRecipients(NewsletterReport newsletterReport, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_CONTENT_RECIPIENT);
			for (ContentReport contentReport : newsletterReport.getContentReports().values()) {
				int id = contentReport.getId();
				contentReport.getRecipients();
				for (Entry<String, String> recipient : contentReport.getRecipients().entrySet()) {
					stat.setInt(1, id);
					stat.setString(2, recipient.getKey());
					stat.setString(3, recipient.getValue());
					stat.addBatch();
					stat.clearParameters();
				}
			}
			stat.executeBatch();
		} catch (BatchUpdateException e) {
			this.processDaoException(e.getNextException(), "Error adding recipients for sent newsletter", 
					"addContentRecipients");
		} catch (Throwable t) {
			this.processDaoException(t, "Error adding recipients for sent newsletter", "addContentRecipients");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	private NewsletterContentReportVO getContentReport(String contentId, Connection conn) {
		NewsletterContentReportVO contentReport = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_CONTENT_REPORT);
			stat.setString(1, contentId);
			res = stat.executeQuery();
			if (res.next()) {
				contentReport = this.createContentReportFromRecord(res);
			}
		} catch (Throwable t) {
			this.processDaoException(t, "Error loading content report", "getContentReport");
		} finally {
			closeDaoResources(res, stat);
		}
		return contentReport;
	}
	
	private Map<String, String> getContentRecipients(int contentReportId, Connection conn) {

		Map<String, String> recipients = new HashMap<String, String>();

		PreparedStatement stat = null;

		ResultSet res = null;

		try {

			stat = conn.prepareStatement(LOAD_CONTENT_RECIPIENTS);

			stat.setInt(1, contentReportId);

			res = stat.executeQuery();

			while (res.next()) {

				recipients.put(res.getString(1), res.getString(2));

			}

		} catch (Throwable t) {

			this.processDaoException(t, "Error loading content recipients", "getContentRecipients");

		} finally {

			closeDaoResources(res, stat);

		}

		return recipients;

	}

	

	private NewsletterContentReportVO createContentReportFromRecord(ResultSet res) throws SQLException {

		NewsletterContentReportVO contentReport = new NewsletterContentReportVO();

		contentReport.setId(res.getInt(1));

		contentReport.setNewsletterId(res.getInt(2));

		contentReport.setContentId(res.getString(3));

		contentReport.setTextBody(res.getString(4));

		contentReport.setHtmlBody(res.getString(5));

		contentReport.setSendDate(res.getTimestamp(6));

		contentReport.setSubject(res.getString(7));

		return contentReport;

	}

	

	

	@Override

	public List<Subscriber> loadSubscribers() throws ApsSystemException {

		List<Subscriber> subscribers = new ArrayList<Subscriber>();

		Connection conn = null;

		PreparedStatement stat = null;

		ResultSet res = null;

		try {

			conn = this.getConnection();

			stat = conn.prepareStatement(LOAD_SUBSCRIBERS);

			res = stat.executeQuery();

			while(res.next()){

				Subscriber subscriber = this.loadSubscriberFromResulSet(res);

				subscribers.add(subscriber);

			}

		} catch (Throwable t) {

			this.processDaoException(t, "Errore durante il caricamento della lista degli indirizzi e-mail", "loadSubscribers");

		} finally {

			this.closeDaoResources(res,stat,conn);

		}

		return subscribers;

	}

	

	@Override

	public List<Subscriber> searchSubscribers(String mailAddress, Boolean active) throws ApsSystemException {

		List<Subscriber> subscribers = new ArrayList<Subscriber>();

		Connection conn = null;

		PreparedStatement stat = null;

		ResultSet res = null;

		try {

			conn = this.getConnection();

			stat = this.buildStatement(mailAddress, active, conn);

			res = stat.executeQuery();

			this.flowResult(subscribers, mailAddress, res);

		} catch (Throwable t) {

			this.processDaoException(t, "Errore durante il caricamento della lista degli indirizzi e-mail", "searchSubscribers");

		} finally {

			this.closeDaoResources(res,stat,conn);

		}

		return subscribers;

	}

	

	private PreparedStatement buildStatement(String mailAddress, Boolean active, Connection conn) {

		String query = this.createQueryString(mailAddress, active);

		PreparedStatement stat = null;

		try {

			stat = conn.prepareStatement(query);

			if (null != active) {

				int intActive = active.booleanValue() ? 1 : 0;

				stat.setInt(1, intActive);

			}

		} catch (Throwable t) {

			processDaoException(t, "Error while creating the statement", "buildStatement");

		}

		return stat;

	}

	

	private String createQueryString(String mailAddress, Boolean active) {

		boolean hasAppendWhereClause = false;

		StringBuffer query = new StringBuffer("SELECT * FROM jpnewsletter_subscribers ");

		if (null != active) {

			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);

			query.append(" active = ? ");

		}

		query.append(" ORDER BY jpnewsletter_subscribers.mailaddress ");

		return query.toString();

	}

	

	protected void flowResult(List<Subscriber> subscribers, String mailAddress, ResultSet res) throws SQLException {

		if (null != mailAddress && mailAddress.trim().length() > 0) {

			mailAddress = mailAddress.trim().toLowerCase();

			while (res.next()) {

				String id = res.getString("mailaddress");

				if (id.toLowerCase().indexOf(mailAddress) != -1) {

					Subscriber subscriber = this.loadSubscriberFromResulSet(res);

					subscribers.add(subscriber);

				}

			}

		} else {

			while (res.next()) {

				Subscriber subscriber = this.loadSubscriberFromResulSet(res);

				subscribers.add(subscriber);

			}

		}

	}

	

	private boolean verifyWhereClauseAppend(StringBuffer query, boolean hasAppendWhereClause) {

		if (hasAppendWhereClause) {

			query.append("AND ");

		} else {

			query.append("WHERE ");

			hasAppendWhereClause = true;

		}

		return hasAppendWhereClause;

	}

	

	@Override

	public Subscriber loadSubscriber(String mailAddress) throws ApsSystemException {

		Subscriber subscriber = null;

		Connection connection = null;

		PreparedStatement statement = null;

		ResultSet res = null;

		try {

			connection = this.getConnection();

			statement = connection.prepareStatement(LOAD_BY_MAIL);

			statement.setString(1, mailAddress);

			res = statement.executeQuery();

			if (res.next()) {

				subscriber = this.loadSubscriberFromResulSet(res);

			}

		} catch (Throwable t) {

			this.processDaoException(t, "Errore durante il caricamento della sottoscrizione", "loadSubscriber");

		} finally {

			this.closeDaoResources(res, statement, connection);

		}

		return subscriber;

	}

	

	private Subscriber loadSubscriberFromResulSet(ResultSet res) throws SQLException {

		Subscriber subscriber = new Subscriber();

		String mailAddress = res.getString("mailaddress");

		Date subscriptionDate = res.getDate("subscription_date");

		subscriber.setMailAddress(mailAddress);

		subscriber.setSubscriptionDate(subscriptionDate);

		int active = res.getInt("active");

		subscriber.setActive(active==1);

		return subscriber;

	}

	

	@Override

	public void addSubscriber(Subscriber subscriber, String token) throws ApsSystemException {

		Connection conn = null;

		try {

			conn = this.getConnection();

			conn.setAutoCommit(false);

			this.addSubscriber(subscriber, conn);

			if (token!=null) {

				this.removeTokenByAddress(subscriber.getMailAddress(), conn);

				this.addToken(subscriber, token, conn);

			}

			conn.commit();

		}  catch (Throwable t) {

			this.executeRollback(conn);

			this.processDaoException(t, "Errore durante l'aggiunta di una sottoscrizione", "addSubscriber");

		} finally {

			this.closeConnection(conn);

		}

	}
	
	private void addSubscriber(Subscriber subscriber, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_SUBSCRIBER);
			stat.setString(1, subscriber.getMailAddress());
			Date subscriptionDate = subscriber.getSubscriptionDate();
			stat.setDate(2, subscriptionDate==null ? null : new java.sql.Date(subscriptionDate.getTime()));
			boolean active = subscriber.isActive();
			stat.setInt(3, active ? 1 : 0);
			stat.executeUpdate();
		}  catch (Throwable t) {
			this.processDaoException(t, "Errore durante l'aggiunta di una sottoscrizione", "addSubscriber");
		} finally {
			this.closeDaoResources(null, stat);
		}
	}
	
	@Override
	public void updateSubscriber(Subscriber subscriber, String token) throws ApsSystemException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateSubscriber(subscriber, conn);
			if (token != null) {
				this.removeTokenByAddress(subscriber.getMailAddress(), conn);
				this.addToken(subscriber, token, conn);
			}
			conn.commit();
		}  catch (Throwable t) {
			this.executeRollback(conn);
			this.processDaoException(t, "Errore durante l'aggiornamento di una sottoscrizione", "updateSubscriber");
		} finally {
			this.closeConnection(conn);
		}
	}
	
	private void updateSubscriber(Subscriber subscriber, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_SUBSCRIBER);
			Date subscriptionDate = subscriber.getSubscriptionDate();
			stat.setDate(1, subscriptionDate==null ? null : new java.sql.Date(subscriptionDate.getTime()));
			boolean active = subscriber.isActive();
			stat.setInt(2, active ? 1 : 0);
			stat.setString(3, subscriber.getMailAddress());
			stat.executeUpdate();
		}  catch (Throwable t) {
			this.processDaoException(t, "Errore durante l'aggiornamento di una sottoscrizione", "updateSubscriber");
		} finally {
			this.closeDaoResources(null, stat);
		}
	}
	
	private void addToken(Subscriber subscriber, String token, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_TOKEN);
			stat.setString(1, subscriber.getMailAddress());
			stat.setString(2, token);
			stat.setTimestamp(3, new Timestamp(subscriber.getSubscriptionDate().getTime()));
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Error adding token for subscription activation", "addToken");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	@Override
	public void deleteSubscriber(String mailAddress) throws ApsSystemException {
		super.executeQueryWithoutResultset(DELETE_SUBSCRIBER, mailAddress);
	}
	
	@Override
	public void activateSubscriber(String mailAddress) throws ApsSystemException {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ACTIVATE_SUBSCRIBER);
			stat.setString(1, mailAddress);
			stat.executeUpdate();
			this.removeTokenByAddress(mailAddress, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			this.processDaoException(t, "Errore durante l'attivazione di una sottoscrizione", "updateSubscriber");
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public String getAddressFromToken(String token) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String username = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_ADDRESS_FROM_TOKEN);
			stat.setString(1,token);
			res = stat.executeQuery();
			if (res.next()) {
				username = res.getString(1);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error getting address from token", "getAddressFromToken");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return username;
	}
	
	@Override
	public void removeToken(String token) {
		super.executeQueryWithoutResultset(DELETE_TOKEN, token);
	}
	
	@Override
	public void cleanOldSubscribers(Date date) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.clearOldSubscribers(date, conn);
			this.clearOldTokens(date, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error in clear old subscribers", "cleanOldSubscribers");
		} finally {
			closeConnection(conn);
		}
	}
	
	private void clearOldSubscribers(Date date, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_OLD_SUBSCRIBERS);
			stat.setTimestamp(1, new Timestamp(date.getTime()));
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Error in clear old tokens", "clearOldSubscribers");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	private void clearOldTokens(Date date, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_OLD_TOKENS);
			stat.setTimestamp(1, new Timestamp(date.getTime()));
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Error in clear old tokens", "clearOldSubscribers");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	private void removeTokenByAddress(String mailAddress, Connection conn) {
		super.executeQueryWithoutResultset(conn, DELETE_ADDRESS_TOKENS, mailAddress);
	}
	
	private final String ADD_CONTENT_TO_QUEUE = "INSERT INTO jpnewsletter_contentqueue ( contentid ) VALUES ( ? ) ";
	
	private final String DELETE_CONTENT_FROM_QUEUE = "DELETE FROM jpnewsletter_contentqueue WHERE contentid = ? ";
	
	private final String LOAD_CONTENT_QUEUE = "SELECT contentid FROM jpnewsletter_contentqueue ";
	
	private final String CLEAN_CONTENT_QUEUE_PREFIX = "DELETE FROM jpnewsletter_contentqueue WHERE contentid IN ( ";
	
	private final String ADD_NEWSLETTER_REPORT = 
		"INSERT INTO jpnewsletter_newsletterreport ( id, date, subject ) VALUES ( ?, ?, ? )";
	
	private final String ADD_CONTENT_REPORT = 
		"INSERT INTO jpnewsletter_contentreport ( id, newsletterid, contentid, textbody, htmlbody ) VALUES ( ?, ?, ?, ?, ? )";
	
	private final String ADD_CONTENT_RECIPIENT = 
		"INSERT INTO jpnewsletter_recipient ( contentreportid, username, mailaddress ) VALUES ( ?, ?, ? )";
	
	private final String LOAD_CONTENT_REPORT = 
		"SELECT c.id, c.newsletterid, c.contentid, c.textbody, c.htmlbody, r.date, r.subject " +
		"FROM jpnewsletter_contentreport AS c JOIN jpnewsletter_newsletterreport r ON c.newsletterid = r.id " +
		"WHERE contentid = ? ORDER BY r.date DESC ";
	
	private final String LOAD_CONTENT_RECIPIENTS = 
		"SELECT username, mailaddress FROM jpnewsletter_recipient WHERE contentreportid = ? ";
	
	private final String LOAD_SENT_CONTENTS = "SELECT DISTINCT contentid FROM jpnewsletter_contentreport ";
	
	private final String EXISTS_CONTENT_REPORT = 
		"SELECT contentid FROM jpnewsletter_contentreport WHERE contentid = ?";
	
	private static final String ADD_SUBSCRIBER = 
		"INSERT INTO jpnewsletter_subscribers (mailaddress, subscription_date, active) VALUES (?, ?, ?)";
	
	private static final String UPDATE_SUBSCRIBER = 
		"UPDATE jpnewsletter_subscribers  SET subscription_date = ?, active = ? WHERE mailaddress = ?";
	
	private static final String DELETE_SUBSCRIBER = 
		"DELETE FROM jpnewsletter_subscribers WHERE mailaddress = ?";
	
	private static final String LOAD_SUBSCRIBERS = 
		"SELECT * FROM jpnewsletter_subscribers ORDER BY mailaddress";
	
	private static final String ACTIVATE_SUBSCRIBER = 
		"UPDATE jpnewsletter_subscribers SET active = 1 WHERE mailaddress = ?";
	
	private static final String LOAD_BY_MAIL = 
		"SELECT * FROM jpnewsletter_subscribers WHERE mailaddress = ?";
	
	private static final String ADD_TOKEN = 
		"INSERT INTO jpnewsletter_subscribertokens ( mailaddress, token, regtime ) VALUES ( ?, ?, ? )";
	
	private static final String GET_ADDRESS_FROM_TOKEN = 
		"SELECT mailaddress FROM jpnewsletter_subscribertokens WHERE token = ?";
	
	private static final String DELETE_TOKEN = 
		"DELETE FROM jpnewsletter_subscribertokens WHERE token = ?";
	
	private static final String DELETE_OLD_SUBSCRIBERS = 
		"DELETE FROM jpnewsletter_subscribers WHERE subscription_date < ? AND active = 0";
	
	private static final String DELETE_OLD_TOKENS = 
		"DELETE FROM jpnewsletter_subscribertokens WHERE regtime < ?";
	
	private static final String DELETE_ADDRESS_TOKENS = 
		"DELETE FROM jpnewsletter_subscribertokens WHERE mailaddress = ?";
	
}