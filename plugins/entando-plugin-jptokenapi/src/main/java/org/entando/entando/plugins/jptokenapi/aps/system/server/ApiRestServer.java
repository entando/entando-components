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
package org.entando.entando.plugins.jptokenapi.aps.system.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.provider.json.JSONProvider;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;

import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.entando.entando.plugins.jptokenapi.aps.system.JpTokenApiSystemConstants;
import org.entando.entando.plugins.jptokenapi.aps.system.token.IApiTokenizerManager;

/**
 * @author E.Santoboni
 */
public class ApiRestServer extends org.entando.entando.aps.system.services.api.server.ApiRestServer {
	
	@Override
    protected Object buildGetDeleteResponse(String langCode, ApiMethod.HttpMethod httpMethod, 
            String namespace, String resourceName, HttpServletRequest request, HttpServletResponse response, UriInfo ui) {
        Object responseObject = null;
        try {
            IResponseBuilder responseBuilder = (IResponseBuilder) ApsWebApplicationUtils.getBean(SystemConstants.API_RESPONSE_BUILDER, request);
            Properties properties = this.extractRequestParameters(ui);
            properties.put(SystemConstants.API_LANG_CODE_PARAMETER, langCode);
            ApiMethod apiMethod = responseBuilder.extractApiMethod(httpMethod, namespace, resourceName);
			String entandoApiToken = this.extractApiToken(request, ui);
			if (null != entandoApiToken) {
				this.extractTokenParameters(entandoApiToken, apiMethod, request, properties);
			} else {
				this.extractOAuthParameters(apiMethod, request, response, properties);
			}
            responseObject = responseBuilder.createResponse(apiMethod, properties);
        } catch (ApiException ae) {
            responseObject = this.buildErrorResponse(httpMethod, namespace, resourceName, ae);
        } catch (Throwable t) {
            responseObject = this.buildErrorResponse(httpMethod, namespace, resourceName, t);
        }
        return this.createResponse(responseObject);
    }
    
	@Override
    protected Object buildPostPutResponse(String langCode, ApiMethod.HttpMethod httpMethod, 
            String namespace, String resourceName, HttpServletRequest request, HttpServletResponse response, UriInfo ui, MediaType mediaType) {
        Object responseObject = null;
        try {
            IResponseBuilder responseBuilder = (IResponseBuilder) ApsWebApplicationUtils.getBean(SystemConstants.API_RESPONSE_BUILDER, request);
            Properties properties = this.extractRequestParameters(ui);
            properties.put(SystemConstants.API_LANG_CODE_PARAMETER, langCode);
            ApiMethod apiMethod = responseBuilder.extractApiMethod(httpMethod, namespace, resourceName);
			
			String entandoApiToken = this.extractApiToken(request, ui);
			if (null != entandoApiToken) {
				this.extractTokenParameters(entandoApiToken, apiMethod, request, properties);
			} else {
				this.extractOAuthParameters(apiMethod, request, response, properties);
			}
            Class expectedType = apiMethod.getExpectedType();
            Object bodyObject = null;
            if (MediaType.APPLICATION_JSON_TYPE.equals(mediaType)) {
                JSONProvider jsonProvider = new JSONProvider();
                bodyObject = jsonProvider.readFrom(expectedType, expectedType.getGenericSuperclass(), 
                        expectedType.getAnnotations(), mediaType, null, request.getInputStream());
            } else {
                JAXBContext context = JAXBContext.newInstance(expectedType);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                bodyObject = (Object) unmarshaller.unmarshal(request.getInputStream());
            }
            responseObject = responseBuilder.createResponse(apiMethod, bodyObject, properties);
        } catch (ApiException ae) {
            responseObject = this.buildErrorResponse(httpMethod, namespace, resourceName, ae);
        } catch (Throwable t) {
            responseObject = this.buildErrorResponse(httpMethod, namespace, resourceName, t);
        }
        return this.createResponse(responseObject);
    }
    
	private String extractApiToken(HttpServletRequest request, UriInfo ui) {
		String token = request.getHeader(API_TOKEN_PARAM_NAME);
		if (null != token && token.trim().length() > 0) {
			return token;
		}
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		if (null != queryParams) {
			Set<Entry<String, List<String>>> entries = queryParams.entrySet();
			Iterator<Entry<String, List<String>>> iter = entries.iterator();
			while (iter.hasNext()) {
				Map.Entry<String, List<String>> entry = (Entry<String, List<String>>) iter.next();
				String key = entry.getKey();
				if (API_TOKEN_PARAM_NAME.equals(key)) {
					//extract only the first value
					return entry.getValue().get(0);
				}
			}
		}
		return null;
	}
	
	protected void extractTokenParameters(String entandoApiToken, ApiMethod apiMethod, 
			HttpServletRequest request, Properties properties) throws ApiException, IOException, ServletException {
		UserDetails user = null;
        IApiTokenizerManager tokenizerManager = 
                (IApiTokenizerManager) ApsWebApplicationUtils.getBean(JpTokenApiSystemConstants.TOKENIZER_MANAGER, request);
        IAuthenticationProviderManager authenticationProvider = 
                (IAuthenticationProviderManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHENTICATION_PROVIDER_MANAGER, request);
        IAuthorizationManager authorizationManager = 
                (IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, request);
        try {
            String username = tokenizerManager.getUser(entandoApiToken);
            user = authenticationProvider.getUser(username);
            if (null != user) {
                properties.put(SystemConstants.API_USER_PARAMETER, user);
            } else if (apiMethod.getRequiredAuth()) {
				throw new ApiException(IApiErrorCodes.API_AUTHENTICATION_REQUIRED, "Invalid or missing user for token '" + entandoApiToken + "'", Response.Status.UNAUTHORIZED);
			}
        } catch (Exception e) {
            if (apiMethod.getRequiredAuth()) {
                throw new ApiException(IApiErrorCodes.API_AUTHENTICATION_REQUIRED, "Authentication Required", Response.Status.UNAUTHORIZED);
            }
        }
        if (null == user && (apiMethod.getRequiredAuth() || null != apiMethod.getRequiredPermission())) {
            throw new ApiException(IApiErrorCodes.API_AUTHENTICATION_REQUIRED, "Authentication Required", Response.Status.UNAUTHORIZED);
        } else if (null != user && null != apiMethod.getRequiredPermission() 
                && !authorizationManager.isAuthOnPermission(user, apiMethod.getRequiredPermission())) {
            throw new ApiException(IApiErrorCodes.API_AUTHORIZATION_REQUIRED, "Authorization Required", Response.Status.UNAUTHORIZED);
        }
	}
    
	public static final String API_TOKEN_PARAM_NAME = "entandoApiToken";
	
}
