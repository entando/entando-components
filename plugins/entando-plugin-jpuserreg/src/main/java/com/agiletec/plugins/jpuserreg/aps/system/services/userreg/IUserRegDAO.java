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
package com.agiletec.plugins.jpuserreg.aps.system.services.userreg;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object for Activation Token Table.
 * 
 * @author zuanni
 * */
public interface IUserRegDAO {
	
	/**
	 * Insert a token for activation/reactivation in to database
	 * @param username the username requested
	 * @param token the token associated with the request
	 * @param date the registration date
	 * @param the type of token
	 * */
	public void addActivationToken(String username, String token, Date regtime, String type);
	
	/**
	 * Returns the username from the associated token if exist
	 * */
	public String getUsernameFromToken(String token);
	
	/**
	 * Remove from database a consumed token
	 * */
	public void removeConsumedToken(String token);
	
	/**
	 * Delete old request account, which has not been activated
	 * */
	public void clearOldTokens(Date date);
	
	/**
	 * Delete token associated with provided username
	 * */
	public void clearTokenByUsername(String username);

	/**
	 * Return usernames associated with expired tokens
	 * */
	public List<String> oldAccountsNotActivated(Date date);
	

	public final static String ACTIVATION_TOKEN_TYPE = "activation";
	
	public final static String REACTIVATION_RECOVER_TOKEN_TYPE = "reactivation_recover";

}