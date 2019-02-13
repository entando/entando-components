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

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

public class DigitalExchangeStreamCallExecutor extends DigitalExchangeBaseCallExecutor<DigitalExchangeBaseCall, InputStream> {

    public DigitalExchangeStreamCallExecutor(MessageSource messageSource,
            DigitalExchange digitalExchange, OAuth2RestTemplate restTemplate,
            DigitalExchangeBaseCall call) {
        super(messageSource, digitalExchange, restTemplate, call);
    }

    @Override
    protected InputStream executeCall(OAuth2RestTemplate restTemplate, String url, DigitalExchangeBaseCall call) {

        ResponseEntity<Resource> responseEntity = restTemplate.exchange(
                call.getURL(getDigitalExchange()), call.getMethod(), call.getEntity(), Resource.class);

        if (responseEntity.getBody() == null) {
            throw new HttpMessageNotReadableException("Response body is null");
        }

        try {
            return responseEntity.getBody().getInputStream();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    protected InputStream buildErrorResponse(String errorCode, String errorMessage) {
        throw new HttpMessageNotReadableException(errorMessage);
    }
}
