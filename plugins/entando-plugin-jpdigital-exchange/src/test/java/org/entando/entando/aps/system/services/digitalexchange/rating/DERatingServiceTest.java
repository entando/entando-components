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

import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.digitalexchange.client.SimpleDigitalExchangeCall;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.entando.entando.web.digitalexchange.rating.DEComponentRatingRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DERatingServiceTest {

    private static final String COMPONENT_ID = "component_id";
    private static final double RATING = 4.5;
    private static final int NUMBER_OF_RATINGS = 2;

    @Mock
    private DigitalExchangesClient client;

    @InjectMocks
    private DERatingService ratingService;

    @Test
    public void shouldRateComponent() {

        String exchangeId = "exchangeId";
        String reviewerId = "reviewerId";
        int rating = 5;

        SimpleRestResponse<DERatingsSummary> response = getFakeResponse();
        when(client.getSingleResponse(eq(exchangeId), any())).thenReturn(response);

        DEComponentRatingRequest ratingRequest = new DEComponentRatingRequest();
        ratingRequest.setComponentId(COMPONENT_ID);
        ratingRequest.setExchangeId(exchangeId);
        ratingRequest.setReviewerId(reviewerId);
        ratingRequest.setRating(rating);

        DEComponentRatingResult expectedResult = new DEComponentRatingResult();
        expectedResult.setRatingsSummary(response.getPayload());

        assertThat(ratingService.rateComponent(ratingRequest)).isEqualTo(expectedResult);

        ArgumentCaptor<SimpleDigitalExchangeCall<?>> callCaptor
                = ArgumentCaptor.forClass(SimpleDigitalExchangeCall.class);
        verify(client, times(1)).getSingleResponse(eq(exchangeId), callCaptor.capture());
        HttpEntity<?> entity = callCaptor.getValue().getEntity();
        assertThat(entity).isNotNull();
        assertThat(entity.getBody()).isEqualTo(ratingRequest);
    }

    private static SimpleRestResponse<DERatingsSummary> getFakeResponse() {
        DERatingsSummary ratingsSummary = new DERatingsSummary();
        ratingsSummary.setComponentId(COMPONENT_ID);
        ratingsSummary.setNumberOfRatings(NUMBER_OF_RATINGS);
        ratingsSummary.setRating(RATING);

        return new SimpleRestResponse<>(ratingsSummary);
    }
}
