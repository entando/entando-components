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


/**
 * This class describes an endopint.
 * an Endpoint is defined by an invocation verb (GET, POST etc etc), a url and
 * by the expected result for that invocation
 *
 * @author entando
 */
public class Endpoint extends IEndpoint {

    public Endpoint(httpVerb verb, String url, int expected) {
        this._url = url;
        this._verb = verb;
        this._expected = expected;
        this._authenticate = true;
    }

    public Endpoint(httpVerb verb, String url, int expected, boolean authenticate) {
        this._url = url;
        this._verb = verb;
        this._expected = expected;
        this._authenticate = authenticate;
    }

    public Endpoint resolveParams(Object... args) {
        this._url = String.format(_url, args);
        return this;
    }

    public String getUrl() {
        return _url;
    }

    public void setUrl(String url) {
        this._url = url;
    }

    public httpVerb getVerb() {
        return _verb;
    }

    public void setVerb(httpVerb verb) {
        this._verb = verb;
    }

    public int getExpected() {
        return _expected;
    }

    public void setExpected(int expected) {
        this._expected = expected;
    }

    public boolean isAuthenticate() {
        return _authenticate;
    }

    public void setAuthenticate(boolean authenticate) {
        this._authenticate = authenticate;
    }

    /**
     * URL of the current endpoint
     */
    private String _url;
    /**
     * HTTP verb to use for this endpoint
     */
    private httpVerb _verb;
    /**
     * Expected HTTP result of the invocation
     */
    private int _expected;
    /**
     * Check if this endpoint must authenticate
     */
    private boolean _authenticate;

}
