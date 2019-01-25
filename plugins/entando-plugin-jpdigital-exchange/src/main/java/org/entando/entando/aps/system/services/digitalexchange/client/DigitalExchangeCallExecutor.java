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

import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.RestResponse;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import static org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClientImpl.*;

public class DigitalExchangeCallExecutor<R extends RestResponse<?, ?>, C> extends DigitalExchangeBaseCallExecutor<DigitalExchangeCall<R, C>, R> {

    private static final Logger logger = LoggerFactory.getLogger(DigitalExchangeCallExecutor.class);

    protected DigitalExchangeCallExecutor(MessageSource messageSource,
            DigitalExchange digitalExchange, OAuth2RestTemplate restTemplate,
            DigitalExchangeCall<R, C> call) {

        super(messageSource, digitalExchange, restTemplate, call);
    }

    @Override
    protected R executeCall(OAuth2RestTemplate restTemplate, String url, DigitalExchangeCall<R, C> call) {
        ResponseEntity<R> responseEntity = restTemplate
                .exchange(url, call.getMethod(), call.getEntity(), call.getParameterizedTypeReference());

        R response = responseEntity.getBody();

        if (call.isResponseParsable(response)) {
            return response;
        } else {
            logger.error("Error calling {}. Unable to parse response", url);
            return getErrorResponse(ERRCODE_DE_WRONG_PAYLOAD, "digitalExchange.unparsableResponse", getDigitalExchange().getName());
        }
    }

    @Override
    protected R buildErrorResponse(String errorCode, String errorMessage) {
        R errorResponse = getCall().getEmptyRestResponse();
        errorResponse.addError(new RestError(errorCode, errorMessage));
        return errorResponse;
    }
}
