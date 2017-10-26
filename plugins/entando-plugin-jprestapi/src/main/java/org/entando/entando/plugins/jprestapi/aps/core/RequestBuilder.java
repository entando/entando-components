/*
 * Copyright 2016-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jprestapi.aps.core;

import antlr.StringUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;
import org.entando.entando.plugins.jprestapi.aps.core.helper.RequestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class implements all the methods to imlement generic (REST) HTTP calls
 * <p>
 *
 *
 * @author entando
 */
public abstract class RequestBuilder implements IRequestBuilder {

    protected Endpoint _endpopoint;
    protected IAuthenticationCredentials _credentials;
    protected Map<String, String> _headers = new HashMap<String, String>();
    protected Map<String, String> _requestParams = new HashMap<String, String>();
    protected boolean _execute = true;
    protected boolean _hasRoot = false;
    protected boolean _isJson = true;
    protected boolean _debug = false;
    protected List<String> _path = new ArrayList<String>();
    protected DefaultHttpClient _httpClient;
    protected String _payload;
    protected IRestInvocation _intercept;
    private static final Logger logger = LoggerFactory.getLogger(RequestBuilder.class);
    /**
     * Common request seup. This method is invoked before every request.
     *
     * @param verb
     */
    protected abstract void setupRequest(HttpRequestBase verb) throws Throwable;

    /**
     * Common client setup. This method is invoked before every request when no
     * authentication is required
     */
    protected DefaultHttpClient setupClient() {
        return new DefaultHttpClient();
    }

    /**
     * Common autenticathed client setup. This method is invoked before every request
     * whenever the endpoint requires authentication.
     *
     * @return
     * @note For backward compatibility this method invokes the default
     * 'setuClient' method
     */
    protected DefaultHttpClient setupAuthenticationClient() {
        return setupClient();
    }

    @Override
    public RequestBuilder setEndpoint(Endpoint endpoint) {
        _endpopoint = endpoint;
        return this;
    }

    @Override
    public RequestBuilder setCredentials(IAuthenticationCredentials credentials) {
        if (null != credentials
                && credentials.valid()) {
            this._credentials = credentials;
        }
        return this;
    }

    @Override
    public RequestBuilder setHeaders(Map<String, String> headersMap) {
        if (null != headersMap
                && !headersMap.isEmpty()) {
            this._headers = headersMap;
        }
        return this;
    }

    @Override
    public RequestBuilder setRequestParams(Map<String, String> requestParams) {
        if (null != requestParams
                && !requestParams.isEmpty()) {
            this._requestParams = requestParams;
        }
        return this;
    }

    @Override
    public RequestBuilder setTestMode(boolean mode) {
        this._execute = !mode;
        return this;
    }

    @Override
    public RequestBuilder setUnmarshalOptions(boolean isJson, boolean hasRoot) {
        _isJson = isJson;
        _hasRoot = hasRoot;
        return this;
    }

    @Override
    public RequestBuilder setDebug(boolean debug) {
        _debug = debug;
        return this;
    }

    @Override
    public RequestBuilder addPath(String path) {
        if (null != path
                && !"".equals(path.trim())) {
            _path.add(path.trim());
        }
        return this;
    }

    /**
     * Setup additional path to the endpoint URL provided.
     *
     * @param path
     * @return
     */
    public RequestBuilder addPath(List<String> path) {
        if (null != path
                && !path.isEmpty()) {
            _path = path;
        }
        return this;
    }

    public RequestBuilder setPayload(String payload) {
        if (null != payload) {
            _payload = payload;
        }
        return this;
    }

    /**
     * Prepare headers before request
     *
     * @param verb
     */
    protected void injectHeaders(HttpRequestBase verb) throws Throwable {
        if (null != verb) {
            RequestHelper.addHeaders(verb, _headers);
            // perform common operations on the request
            setupRequest(verb);
            if (_debug) {
                logger.info("Header list:");
                for (Header header : verb.getAllHeaders()) {
                    logger.info("{} : {} ",header.getName(),header.getValue());
                }
            }
        }
    }

    /**
     * Prepare custom parameters
     *
     * @param verb
     * @throws Throwable
     */
    protected void injectParameters(HttpRequestBase verb) throws Throwable {
        RequestHelper.appendParameters(verb, _requestParams);
        if (_debug) {
            logger.info("after appending parameters: {}",verb.getURI());
        }
    }

    /**
     * Append additional path
     *
     * @param verb
     * @throws Throwable
     */
    protected void appendPath(HttpRequestBase verb) throws Throwable {
        if (null != verb) {
            RequestHelper.appendPath(verb, _path);

            if (_debug) {
                logger.info("after appending path: {}",verb.getURI());
            }
        }
    }

    /**
     * Append the payload
     *
     * @param verb
     * @throws Throwable
     */
    protected void appendPayload(HttpRequestBase verb) throws Throwable {
        if (null != verb && null != _payload) {
            StringEntity jsonEntity = new StringEntity(_payload);

            if (verb instanceof HttpPost) {
                ((HttpPost) verb).setEntity(jsonEntity);
                if (_debug) {
                    logger.info("payload:\n {}",_payload);
                }
            }
            if (verb instanceof HttpPut) {
                ((HttpPut) verb).setEntity(jsonEntity);
                if (_debug) {
                    logger.info("payload:\n {}",_payload);
                }
            }
        }
    }

    /**
     * Save both request and response object of the request for later analysis
     *
     * @param intercept
     * @return
     */
    public RequestBuilder trackRestInvocation(IRestInvocation intercept) {
        if (null != intercept) {
            _intercept = intercept;
        }
        return this;
    }

    /**
     * Track the request
     *
     * @param request
     */
    protected void trackRequest(HttpRequestBase request) {
        if (null != _intercept
                && null != request) {
            _intercept.setRequest(request);
        }
    }

    /**
     * Track response
     *
     * @param response
     */
    protected void trackResponse(HttpResponse response) {
        if (null != _intercept
                && null != response) {
            _intercept.setResponse(response);
        }
    }

    /**
     * Close the connection to the client
     *
     * @throws Throwable
     */
    protected void close() throws Throwable {
        if (null != _httpClient) {

        }
    }

    /**
     * Execute the request to the desired endpoint
     *
     * @return
     * @throws Throwable
     */
    protected HttpResponse executeRequest() throws Throwable {
        HttpRequestBase verb = null;
        HttpResponse response = null;

        _httpClient = _endpopoint.isAuthenticate() ? setupAuthenticationClient()
                : setupClient();
        if (null != _endpopoint) {
            if (null != _endpopoint.getVerb()) {
                String endpoint = _endpopoint.getUrl();

                switch (_endpopoint.getVerb()) {
                    case POST:
                        verb = new HttpPost(endpoint);
                        appendPayload(verb);
                        break;
                    case PUT:
                        verb = new HttpPut(endpoint);
                        appendPayload(verb);
                        break;
                    case DELETE:
                        verb = new HttpDelete(endpoint);
                        break;
                    case GET:
                    default:
                        verb = new HttpGet(endpoint);
                        break;
                }
            }
        }
        // headers
        injectHeaders(verb);
        // append custom path
        appendPath(verb);
        // other parameters
        injectParameters(verb);
        // finally
        if (null != verb) {
            verb.releaseConnection();
        }
        // track request
        trackRequest(verb);
        if (_debug) {
            logger.info("request line: {}",verb.getRequestLine());
        }
        if (_execute) {
            response = _httpClient.execute(verb);
            // track response
            trackResponse(response);
        }
        return response;
    }

    /**
     * Check the response result of the invocation
     *
     * @param response
     * @return
     * @throws Throwable
     */
    protected InputStream checkResponse(HttpResponse response) throws Throwable {
        InputStream ris = null;

        if (null != response
                && null != response.getEntity()) {
            int code = response.getStatusLine().getStatusCode();

            // get the body input stream
            ris = response.getEntity().getContent();
            // check if the result of the call matches the expected one
            if (code == _endpopoint.getExpected()) {
                if (_debug) {
                    logger.info("returned status: {}",response.getStatusLine().getStatusCode());
                }
            } else {
                if (_debug) {
                    String body = IOUtils.toString(ris, "UTF-8");
                    logger.info("unexpected status: {} \n Body: {}",response.getStatusLine().getStatusCode(),body);
                }
                throw new RuntimeException("Unexpected HTTP status returned: " + code);
            }
        }
        return ris;
    }

    @Override
    public Object doRequest(Class<?> expected) throws Throwable {
        Object obj = null;
        StreamSource ss;

        HttpResponse response = executeRequest();
        if (_execute) {
            InputStream ris = checkResponse(response);
            // process answer
            if (!_debug) {
                ss = new StreamSource(ris);
            } else {
                String responseBody = IOUtils.toString(ris, "UTF-8");
                logger.info("returned body:\n{}",responseBody);
                ss = new StreamSource(new java.io.StringReader(responseBody));
            }
            if (null != expected && null != ris) {
                // unmarshall the response into the desired object
                obj = JAXBHelper.unmarshall(ss, expected, _hasRoot, _isJson);
            }
            // finally
            close();
        }
        return obj;
    }

    @Override
    public String doRequest() throws Throwable {
        String responseBody = null;

        HttpResponse response = executeRequest();
        if (_execute) {
            InputStream ris = checkResponse(response);

            responseBody = IOUtils.toString(ris, "UTF-8");
            if (_debug) {
                logger.info("returned body: {}",responseBody);
            }
            // finally
            close();
        }
        return responseBody;
    }


}
