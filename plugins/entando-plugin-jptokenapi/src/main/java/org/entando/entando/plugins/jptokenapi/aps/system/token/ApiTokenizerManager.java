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

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
@Aspect
public class ApiTokenizerManager extends AbstractService implements IApiTokenizerManager {

	private static final Logger _logger = LoggerFactory.getLogger(ApiTokenizerManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public String getUser(String token) throws ApsSystemException {
		String username = null;
		try {
			username = this.getApiTokenDAO().getUser(token);
		} catch (Throwable t) {
			_logger.error("Error extracting user by token {}", token, t);
			throw new ApsSystemException("Error extracting user by token '" + token + "'", t);
		}
		return username;
	}
	
	@Override
	public String getToken(String username) throws ApsSystemException {
		String token = null;
		try {
			token = this.getApiTokenDAO().getToken(username);
		} catch (Throwable t) {
			_logger.error("Error extracting token by username {}", username, t);
			throw new ApsSystemException("Error extracting token by username '" + username + "'", t);
		}
		return token;
	}
	
	@Override
	public String updateToken(String username) throws ApsSystemException {
		String token = null;
		try {
			token = this.getApiTokenDAO().updateToken(username);
		} catch (Throwable t) {
			_logger.error("Error updating token by username {}", username, t);
			throw new ApsSystemException("Error updating token by username '" + username + "'", t);
		}
		return token;
	}
	
	@Before("execution(* com.agiletec.aps.system.services.user.IUserManager.changePassword(..)) && args(username, password)")
	public void changePassword(Object username, Object password) {
		try {
			this.updateToken((String) username);
		} catch (Throwable t) {
			_logger.error("Error updating token by username {}", username, t);
		}
	}
	
	protected IApiTokenDAO getApiTokenDAO() {
		return _apiTokenDAO;
	}
	public void setApiTokenDAO(IApiTokenDAO apiTokenDAO) {
		this._apiTokenDAO = apiTokenDAO;
	}
	
	private IApiTokenDAO _apiTokenDAO;
	
}