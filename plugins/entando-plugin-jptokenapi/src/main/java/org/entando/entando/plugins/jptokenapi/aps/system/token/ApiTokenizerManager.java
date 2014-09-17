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