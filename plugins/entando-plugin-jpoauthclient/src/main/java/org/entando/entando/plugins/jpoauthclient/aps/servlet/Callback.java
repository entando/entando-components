/*
 * Copyright 2007 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.entando.entando.plugins.jpoauthclient.aps.servlet;

import com.agiletec.aps.util.ApsWebApplicationUtils;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.server.OAuthServlet;

import org.entando.entando.plugins.jpoauthclient.aps.oauth.client.OAuthClient;

import org.entando.entando.plugins.jpoauthclient.aps.system.ConsumerSystemConstants;
import org.entando.entando.plugins.jpoauthclient.aps.system.CookieMap;
import org.entando.entando.plugins.jpoauthclient.aps.system.RedirectException;
import org.entando.entando.plugins.jpoauthclient.aps.system.httpclient.OAuthHttpClient;
import org.entando.entando.plugins.jpoauthclient.aps.system.services.client.IProviderConnectionManager;

/**
 * An OAuth callback handler.
 * @author John Kristian
 */
public class Callback extends HttpServlet {
    
    public static final String PATH = "/OAuth/Callback";
    
    /**
     * Exchange an OAuth request token for an access token, and store the latter
     * in cookies.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        OAuthConsumer consumer = null;
        IProviderConnectionManager providerConnection = 
			(IProviderConnectionManager) ApsWebApplicationUtils.getBean(ConsumerSystemConstants.API_PROVIDER_CONNECTION_MANAGER, request);
        try {
            final OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
            requestMessage.requireParameters("consumer");
            final String consumerName = requestMessage.getParameter("consumer");
            consumer = providerConnection.getConsumer(consumerName);
            final CookieMap cookies = new CookieMap(request, response);
            final OAuthAccessor accessor = providerConnection.newAccessor(consumer, cookies);
            final String expectedToken = accessor.requestToken;
            String requestToken = requestMessage.getParameter(OAuth.OAUTH_TOKEN);
            if (requestToken == null || requestToken.length() <= 0) {
                //log.warn(request.getMethod() + " "
                //        + OAuthServlet.getRequestURL(request));
                requestToken = expectedToken;
                if (requestToken == null) {
                    OAuthProblemException problem = new OAuthProblemException(OAuth.Problems.PARAMETER_ABSENT);
                    problem.setParameter(OAuth.Problems.OAUTH_PARAMETERS_ABSENT, OAuth.OAUTH_TOKEN);
                    throw problem;
                }
            } else if (!requestToken.equals(expectedToken)) {
                OAuthProblemException problem = new OAuthProblemException("token_rejected");
                problem.setParameter("oauth_rejected_token", requestToken);
                problem.setParameter("oauth_expected_token", expectedToken);
                throw problem;
            }
            List<OAuth.Parameter> parameters = null;
            String verifier = requestMessage.getParameter(OAuth.OAUTH_VERIFIER);
            if (verifier != null) {
                parameters = OAuth.newList(OAuth.OAUTH_VERIFIER, verifier);
            }
            OAuthClient client = new OAuthClient(new OAuthHttpClient());
            OAuthMessage result = client.getAccessToken(accessor, null, parameters);
            if (accessor.accessToken != null) {
                String returnTo = requestMessage.getParameter("returnTo");
                if (returnTo == null) {
                    returnTo = request.getContextPath(); // home page
                }
                cookies.remove(consumerName + ".requestToken");
                int oneYear = 365*24*60*60;
                cookies.put(consumerName + ".accessToken", accessor.accessToken, oneYear);
                cookies.remove(consumerName + ".tokenSecret");
                cookies.put(consumerName + ".tokenSecret", accessor.tokenSecret, oneYear);
                throw new RedirectException(returnTo);
            }
            OAuthProblemException problem = new OAuthProblemException(OAuth.Problems.PARAMETER_ABSENT);
            problem.setParameter(OAuth.Problems.OAUTH_PARAMETERS_ABSENT, OAuth.OAUTH_TOKEN);
            problem.getParameters().putAll(result.getDump());
            throw problem;
        } catch (Exception e) {
            providerConnection.handleException(e, request, response, consumer);
        }
    }
    
}
