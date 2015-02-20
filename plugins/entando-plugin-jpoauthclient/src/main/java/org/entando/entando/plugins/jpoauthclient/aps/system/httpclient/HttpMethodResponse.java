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
package org.entando.entando.plugins.jpoauthclient.aps.system.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.oauth.OAuth;
import org.entando.entando.plugins.jpoauthclient.aps.oauth.client.ExcerptInputStream;
import org.entando.entando.plugins.jpoauthclient.aps.oauth.http.HttpMessage;
import org.entando.entando.plugins.jpoauthclient.aps.oauth.http.HttpResponseMessage;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;

/**
 * The response part of an HttpMethod, encapsulated as an HttpMessage.
 * 
 * @author John Kristian
 */
public class HttpMethodResponse extends HttpResponseMessage {

    /**
     * Construct an OAuthMessage from the HTTP response, including parameters
     * from OAuth WWW-Authenticate headers and the body. The header parameters
     * come first, followed by the ones from the response body.
     */
    public HttpMethodResponse(HttpMethod method, byte[] requestBody, String requestEncoding)
            throws IOException {
        super(method.getName(), new URL(method.getURI().toString()));
        this.method = method;
        this.requestBody = requestBody;
        this.requestEncoding = requestEncoding;
        this.headers.addAll(getHeaders());
    }
	
    private final HttpMethod method;
    private final byte[] requestBody;
    private final String requestEncoding;

    @Override
    public int getStatusCode() {
        return method.getStatusCode();
    }

    @Override
    public InputStream openBody() throws IOException {
        return method.getResponseBodyAsStream();
    }

    private List<Map.Entry<String, String>> getHeaders() {
        List<Map.Entry<String, String>> headers = new ArrayList<Map.Entry<String, String>>();
        Header[] allHeaders = method.getResponseHeaders();
        if (allHeaders != null) {
            for (Header header : allHeaders) {
                headers.add(new OAuth.Parameter(header.getName(), header.getValue()));
            }
        }
        return headers;
    }

    /** Return a complete description of the HTTP exchange. */
    @Override
    public void dump(Map<String, Object> into) throws IOException {
        super.dump(into);
        {
            StringBuilder request = new StringBuilder(method.getName());
            request.append(" ").append(method.getPath());
            String query = method.getQueryString();
            if (query != null && query.length() > 0) {
                request.append("?").append(query);
            }
            request.append(EOL);
            for (Header header : method.getRequestHeaders()) {
                request.append(header.getName()).append(": ").append(header.getValue()).append(EOL);
            }
            request.append(EOL);
            if (requestBody != null) {
                request.append(new String(requestBody, requestEncoding));
            }
            into.put(REQUEST, request.toString());
        }
        {
            StringBuilder response = new StringBuilder();
            String value = method.getStatusLine().toString();
            response.append(value).append(EOL);
            for (Header header : method.getResponseHeaders()) {
                String name = header.getName();
                value = header.getValue();
                response.append(name).append(": ").append(value).append(EOL);
            }
            response.append(EOL);
            if (body != null) {
                response.append(new String(((ExcerptInputStream) body).getExcerpt(),
                        getContentCharset()));
            }
            into.put(HttpMessage.RESPONSE, response.toString());
        }
    }

}
