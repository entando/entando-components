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
package com.agiletec.plugins.jpcasclient.aps.system.services.controller.control;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Utility for CAS ticket validation
 * 
 * @author G.Cocco
 * */
public class CasClientTicketValidationUtil {

	private static final Logger _logger =  LoggerFactory.getLogger(CasClientTicketValidationUtil.class);
	
	public CasClientTicketValidationUtil(String urlCasValidate) {
		this._urlCasValidate = urlCasValidate;
	}
	
	/**
	 * ticket validation 
	 * */
	public Assertion validateTicket(String service, String ticket_key) throws ApsSystemException {
		Assertion assertion = null;
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		_client = new HttpClient(connectionManager);
		GetMethod authget = new GetMethod();
		Map<String, String> params = new HashMap<String, String>();
		params.put("service", service);
		params.put("ticket", ticket_key);				
		authget.getParams().setCookiePolicy(CookiePolicy.DEFAULT);
		String responseBodyValidation = null;
		String responseAssertion = null;
		String responseUser = null;
		responseBodyValidation = this.CASgetMethod(authget, _client, this._urlCasValidate, params);				
		try {
			// 	controllo della risposta sulla richiesta validazione ticket
			if ( null != responseBodyValidation && responseBodyValidation.length() > 0 ) {
				InputStreamReader reader;
				reader = new InputStreamReader(authget.getResponseBodyAsStream());
				BufferedReader bufferedReader = new BufferedReader(reader);
				responseAssertion = bufferedReader.readLine();
				if (responseAssertion.equals(_positiveResponse)) {
					responseUser = bufferedReader.readLine();
				}
				ApsSystemUtils.getLogger().info("CasClientTicketValidationUtil - Assertion: " + responseAssertion + " user: " + responseUser);
			}
		} catch (Throwable t) {
			_logger.error("Error in CasClientTicketValidationUtil - validateTicket", t);
			throw new ApsSystemException("Error in CasClientTicketValidationUtil - validateTicket", t);
		}
		if (null != responseAssertion && null != responseUser && responseAssertion.equalsIgnoreCase(_positiveResponse) && responseUser.length() > 0 ) {
			assertion = new AssertionImpl(responseUser);
		}
		return assertion;
	}
	
	/**
	 * Service method for execution of get request. 
	 * */
	public String CASgetMethod(GetMethod authget, HttpClient client, String url, Map<String, String> params) {
		String body = null;
		authget.setPath(url);
		int i = 0;
		if ( null != params && params.size() > 0 ) {
			NameValuePair[] services = new NameValuePair[params.size()];
			for (Iterator iter = params.entrySet().iterator(); iter.hasNext();)	{
				Map.Entry entry = (Map.Entry)iter.next();
				String key = (String)entry.getKey();
				String value = (String)entry.getValue();
				services[i++] = new NameValuePair(key, value);
			}
			authget.setQueryString(services);
		}
		try {
			client.executeMethod(authget);
			body = authget.getResponseBodyAsString();
		} catch (Throwable t) {
			_logger.error("Error in CasClientTicketValidationUtil - CASgetMethod", t);
			throw new RuntimeException("Error in CasClientTicketValidationUtil - CASgetMethod", t);
		} finally {
			authget.releaseConnection();
		}
		return body;
	}
	
	private final static String _positiveResponse = "yes";
	private HttpClient _client;
	private String _urlCasValidate;
	
}
