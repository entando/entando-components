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