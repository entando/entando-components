/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import java.util.Map;
import org.entando.entando.web.common.model.RestResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

/**
 * Base class containing the information necessary to query a specific Digital
 * Exchange endpoint, parse its response and combine multiple responses provided
 * by the same endpoint of different Digital Exchange instances.
 *
 * @param <R> the type of each DE response
 * @param <C> the type of the combined response
 */
public abstract class DigitalExchangeCall<R extends RestResponse<?, ?>, C> extends DigitalExchangeBaseCall<R> {

    private final ParameterizedTypeReference<R> parameterizedTypeReference;

    /**
     * @param method e.g. GET, POST, ...
     * @param parameterizedTypeReference The ParameterizedTypeReference class is
     * used to tell Spring how to convert the HTTP response into a Java object.
     * It is important to specify all the nested generic types, otherwise
     * unknown types will be converted into instances of Map causing
     * ClassCastException at runtime.
     * @param urlSegments path segments necessary for building the last part of
     * the endpoint URL
     */
    public DigitalExchangeCall(HttpMethod method,
            ParameterizedTypeReference<R> parameterizedTypeReference,
            String... urlSegments) {

        super(method, urlSegments);
        this.parameterizedTypeReference = parameterizedTypeReference;
    }

    public ParameterizedTypeReference<R> getParameterizedTypeReference() {
        return parameterizedTypeReference;
    }

    protected boolean isResponseParsable(R response) {
        return response != null;
    }

    protected abstract R getEmptyRestResponse();

    protected abstract C combineResults(Map<String, R> results);
}
