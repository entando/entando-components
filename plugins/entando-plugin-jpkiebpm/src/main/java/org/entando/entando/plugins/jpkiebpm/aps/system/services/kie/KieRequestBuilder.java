/*
* The MIT License
*
* Copyright 2017 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.protocol.HTTP;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.HEADER_VALUE_JSON;
import org.entando.entando.plugins.jprestapi.aps.core.RequestBuilder;
import org.entando.entando.plugins.jprestapi.aps.core.helper.RequestHelper;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.HEADER_KEY_ACCEPT;

/**
 *
 * @author Entando
 */
public class KieRequestBuilder extends RequestBuilder {

    public KieRequestBuilder(KieClient client) {
        this._client = client;
    }

    @Override
    protected void setupRequest(HttpRequestBase verb) throws Throwable {
        // process evaluation URL
        RequestHelper.addBaseUrl(verb, _client.getBaseUrl());
        // headers are set on single mothod basis
    }

    @Override
    protected DefaultHttpClient setupClient() {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        return httpclient;
    }

    @Override
    protected DefaultHttpClient setupAuthenticationClient() {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        // basic authentication
        String username = _client.getCredentials().getUsername();
        String password = _client.getCredentials().getPassword();
        UsernamePasswordCredentials authCredentials = new UsernamePasswordCredentials(username, password);

        // TODO restrict host and scope!
        AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT);
        httpclient.getCredentialsProvider().setCredentials(authScope, authCredentials);
        return httpclient;
    }


    private final KieClient _client;
}
