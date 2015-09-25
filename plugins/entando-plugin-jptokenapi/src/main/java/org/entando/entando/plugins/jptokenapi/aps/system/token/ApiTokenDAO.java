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
package org.entando.entando.plugins.jptokenapi.aps.system.token;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

/**
 * @author E.Santoboni
 */
public class ApiTokenDAO extends AbstractDAO implements IApiTokenDAO {

	private static final Logger _logger = LoggerFactory.getLogger(ApiTokenDAO.class);

	@Override
	public String updateToken(String username) {
		Connection conn = null;
		PreparedStatement stat = null;
		String token_data = username + System.nanoTime();
        String token = DigestUtils.md5Hex(token_data);
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeToken(username, conn);
			stat = conn.prepareStatement(INSERT_TOKEN);
			stat.setString(1, username);
			stat.setString(2, token);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating token for user {}", username, t);
			throw new RuntimeException("Error while updating token", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
		return token;
	}
	
	@Override
	public void removeToken(String username) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeToken(username, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while deleting token by username {}", username,  t);
			throw new RuntimeException("Error while deleting token by username", t);
		} finally {
			closeConnection(conn);
		}
	}
	
	private void removeToken(String username, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_TOKEN);
			stat.setString(1, username);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error while deleting a token for user {}", username,  t);
			throw new RuntimeException("Error while deleting a token", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	@Override
	public String getToken(String username) {
		String token = this.get(username, SELECT_TOKEN);
		if (null == token) {
			token = this.updateToken(username);
		}
		return token;
	}
	
	@Override
	public String getUser(String token) {
		return this.get(token, SELECT_USERNAME);
	}
	
	private String get(String field, String query) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String data = null;
		try {
			conn = this.getConnection();
            stat = conn.prepareStatement(query);
            stat.setString(1, field);
            res = stat.executeQuery();
			if (res.next()) {
				data = res.getString(1);
			}
		} catch (Throwable t) {
			_logger.error("Error while loading data",  t);
			throw new RuntimeException("Error while loading data", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return data;
	}
	
	private static final String SELECT_TOKEN = "SELECT token FROM jptokenapi_usertokens WHERE username = ?";
	
	private static final String SELECT_USERNAME = "SELECT username FROM jptokenapi_usertokens WHERE token = ?";
	
	private static final String INSERT_TOKEN = "INSERT INTO jptokenapi_usertokens(username, token) VALUES (?, ?)";
	
	private static final String DELETE_TOKEN = "DELETE FROM jptokenapi_usertokens WHERE username = ?";
	
}