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
package org.entando.entando.aps.system.services.digitalexchange.rating;

import java.util.Optional;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.digitalexchange.client.SimpleDigitalExchangeCall;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.entando.entando.web.digitalexchange.rating.DEComponentRatingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DERatingService {

    private final DigitalExchangesClient client;

    @Autowired
    public DERatingService(DigitalExchangesClient client) {
        this.client = client;
    }

    public DEComponentRatingResult rateComponent(String exchangeId, DEComponentRatingRequest ratingRequest) {

        SimpleDigitalExchangeCall<DERatingsSummary> call = new SimpleDigitalExchangeCall<>(
                HttpMethod.POST, new ParameterizedTypeReference<SimpleRestResponse<DERatingsSummary>>() {
        }, "digitalExchange", "components", ratingRequest.getComponentId(), "rate");

        call.setEntity(new HttpEntity<>(ratingRequest));

        DEComponentRatingResult result = new DEComponentRatingResult();
        call.setErrorResponseHandler(ex -> {
            int status = ex.getRawStatusCode();
            if (status == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                result.setRatingUnsupported();
            } else if (status == HttpStatus.NOT_FOUND.value()) {
                throw new ResourceNotFoundException("component", ratingRequest.getComponentId());
            }
            return Optional.empty();
        });

        SimpleRestResponse<DERatingsSummary> response = client.getSingleResponse(exchangeId, call);

        if (result.isRatingSupported()) {
            result.setRatingsSummary(response.getPayload());
        }
        result.setErrors(response.getErrors());

        return result;
    }
}
