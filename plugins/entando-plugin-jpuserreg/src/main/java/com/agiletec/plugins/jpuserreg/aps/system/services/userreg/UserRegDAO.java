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
package com.agiletec.plugins.jpuserreg.aps.system.services.userreg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

/**
 * Data Access Object for managing information associated
 * with Account Requests, Suspinsion and Password Recover
 * 
 * @author zuanni
 * */
public class UserRegDAO extends AbstractDAO implements IUserRegDAO {

	private static final Logger _logger = LoggerFactory.getLogger(UserRegDAO.class);
	
	@Override
	public void addActivationToken(String username, String token, Date regtime, String type) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_TOKEN);
			stat.setString(1, username);
			stat.setString(2, token);
			stat.setTimestamp(3, new Timestamp(regtime.getTime()));
			stat.setString(4, type);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error adding token for account activation. user: {}", username,  t);
			throw new RuntimeException("Error adding token for account activation", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public String getUsernameFromToken(String token) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String username = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_USERNAME_FROM_TOKEN);
			stat.setString(1,token);
			res = stat.executeQuery();
			if (res.next()) {
				username = res.getString("username");
			}
		} catch (Throwable t) {
			_logger.error("Error getting Username from token",  t);
			throw new RuntimeException("Error getting Username from token", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return username;
	}
	
	@Override
	public void removeConsumedToken(String token) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_USED_TOKEN);
			stat.setString(1,token);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error removing consumed Token",  t);
			throw new RuntimeException("Error removing consumed Token", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void clearOldTokens(Date date) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			Timestamp timeBound = new Timestamp(date.getTime());
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_OLD_TOKENS);
			stat.setTimestamp(1, timeBound);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error in clear old tokens", t);
			throw new RuntimeException("Error in clear old tokens", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void clearTokenByUsername(String username) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_USER_TOKENS);
			stat.setString(1, username);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error in clear tokens by username {}", username,  t);
			throw new RuntimeException("Error in clear tokens by username", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public List<String> oldAccountsNotActivated(Date date) {
		List<String> usernames = new ArrayList<String>();
		Timestamp timeBound = new Timestamp(date.getTime());
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String username = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(USERNAMES_FROM_OLD_ACCOUNT_REQUESTS);
			stat.setTimestamp(1, timeBound);
			res = stat.executeQuery();
			while (res.next()) {
				username = res.getString("username");
				usernames.add(username);
			}
		} catch (Throwable t) {
			_logger.error("error extracting oldAccountsNotActivated",  t);
			throw new RuntimeException("error extracting oldAccountsNotActivated", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return usernames;
	}
	
	private static final String ADD_TOKEN = 
		"INSERT INTO jpuserreg_activationtokens(username, token, regtime, tokentype) VALUES (?, ?, ?, ?)";
	
	private static final String GET_USERNAME_FROM_TOKEN = 
		"SELECT username FROM jpuserreg_activationtokens WHERE token = ?";
	
	private static final String DELETE_USED_TOKEN = 
		"DELETE FROM jpuserreg_activationtokens WHERE token = ?";
	
	private static final String USERNAMES_FROM_OLD_ACCOUNT_REQUESTS =
		"SELECT jpuserreg_activationtokens.username FROM jpuserreg_activationtokens, authusers " +
		" WHERE regtime < ? AND tokentype = 'activation' " +
		" AND jpuserreg_activationtokens.username = authusers.username " +
		" AND authusers.active = 0 ";
	
	private static final String DELETE_OLD_TOKENS = 
		"DELETE FROM jpuserreg_activationtokens WHERE regtime < ? AND tokentype = 'reactivation_recover'";
	
	private static final String DELETE_USER_TOKENS = 
		"DELETE FROM jpuserreg_activationtokens WHERE username = ?";

}