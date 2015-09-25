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
package org.entando.entando.plugins.jpoauthclient.aps.system;

/**
 * @author E.Santoboni
 */
public interface ConsumerSystemConstants {
    
    public static final String API_PROVIDER_CONNECTION_MANAGER = "jpoauthConsumerProviderConnectionManager";
    
    public static final String ENTANDO_SERVICE_PROVIDER_REQ_TOKEN_URL_PARAM_NAME = "jpoauthclient_entandoProvider_requestTokenURL";
    public static final String ENTANDO_SERVICE_PROVIDER_AUTH_URL_PARAM_NAME = "jpoauthclient_entandoProvider_userAuthorizationURL";
    public static final String ENTANDO_SERVICE_PROVIDER_ACCESS_TOKEN_URL_PARAM_NAME = "jpoauthclient_entandoProvider_accessTokenURL";
    
	public static final String CONSUMER_CALLBACK_URL_PARAM_NAME = "jpoauthclient_consumer_callbackURL";
	public static final String CONSUMER_KEY_PARAM_NAME = "jpoauthclient_consumer_consumerKey";
	public static final String CONSUMER_SECRET_PARAM_NAME = "jpoauthclient_consumer_consumerSecret";
	public static final String ENTANDO_SERVICE_PROVIDER_BASE_URL_PARAM_NAME = "jpoauthclient_entandoProvider_baseURL";
	
	public static final String DEFAULT_CONSUMER_NAME = "entandoDefaultConsumer";
    
}
