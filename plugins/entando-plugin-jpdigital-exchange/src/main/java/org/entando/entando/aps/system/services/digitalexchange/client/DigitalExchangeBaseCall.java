/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.digitalexchange.client;

import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

public class DigitalExchangeBaseCall {

    private final HttpMethod method;
    private final String[] urlSegments;
    private HttpEntity<?> entity;

    /**
     * @param method e.g. GET, POST, ...
     * @param urlSegments path segments necessary for building the last part of
     * the endpoint URL
     */
    public DigitalExchangeBaseCall(HttpMethod method, String... urlSegments) {

        this.method = method;

        this.urlSegments = new String[urlSegments.length + 1];
        this.urlSegments[0] = "api";
        System.arraycopy(urlSegments, 0, this.urlSegments, 1, urlSegments.length);
    }

    protected String getURL(DigitalExchange digitalExchange) {
        return UriComponentsBuilder
                .fromHttpUrl(digitalExchange.getUrl())
                .pathSegment(urlSegments)
                .build(false) // disable encoding: it will be done by RestTemplate
                .toUriString();
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpEntity<?> getEntity() {
        return entity;
    }

    public void setEntity(HttpEntity<?> entity) {
        this.entity = entity;
    }
}
