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
package org.entando.entando.plugins.jpoauthclient.aps.system.services.client;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;

import org.entando.entando.plugins.jpoauthclient.aps.system.CookieMap;
import org.entando.entando.plugins.jpoauthclient.aps.system.RedirectException;

/**
 * @author E.Santoboni
 */
public interface IProviderConnectionManager {
    
	public InputStream invokeApiMethod(ApiMethodRequestBean bean, HttpServletRequest request, 
			HttpServletResponse response, boolean handleException) throws RedirectException, Exception;
	
	public InputStream invokeApiMethod(ApiMethodRequestBean bean, HttpServletRequest request, 
			HttpServletResponse response, boolean handleException, boolean redirectOAuthErrorPage) throws RedirectException, Exception;
	
	public Object extractApiMethodResult(ApiMethodRequestBean bean, Class expectedType, HttpServletRequest request, 
			HttpServletResponse response, boolean handleException) throws RedirectException, Exception;
    
	public Object extractApiMethodResult(ApiMethodRequestBean bean, Class expectedType, HttpServletRequest request, 
			HttpServletResponse response, boolean handleException, boolean redirectOAuthErrorPage) throws RedirectException, Exception;
    
	//-----------------------
	
    public OAuthConsumer getConsumer(String name) throws IOException;
    
    public OAuthAccessor newAccessor(OAuthConsumer consumer, CookieMap cookies) throws OAuthException;
    
    public void handleException(Exception e, HttpServletRequest request,
            HttpServletResponse response, OAuthConsumer consumer)
            throws IOException, ServletException;
    
    public void removeAccessors(CookieMap cookies);
    
}
