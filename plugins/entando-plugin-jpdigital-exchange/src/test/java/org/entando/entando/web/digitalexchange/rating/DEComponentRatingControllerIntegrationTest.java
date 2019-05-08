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
package org.entando.entando.web.digitalexchange.rating;

import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeOAuth2RestTemplateFactory;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesMocker;
import org.entando.entando.aps.system.services.digitalexchange.rating.DERatingsSummary;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils.*;
import org.entando.entando.crypt.DefaultTextEncryptor;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@ActiveProfiles("DERatingTest")
public class DEComponentRatingControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String BASE_URL = "/digitalExchange/rate";

    private static final String COMPONENT_ID = "component_id";
    private static final double RATING = 4.5;
    private static final int NUMBER_OF_RATINGS = 2;

    @Configuration
    @Profile("DERatingTest")
    public static class TestConfig {

        @Bean
        @Primary
        public DigitalExchangeOAuth2RestTemplateFactory getRestTemplateFactory() {
            return new DigitalExchangesMocker()
                    .addDigitalExchange(DE_1_ID, getFakeResponse())
                    .initMocks();
        }
        
        @Bean
        @Primary
        public TextEncryptor getDigitalExchangeTextEncryptor() {
            return new DefaultTextEncryptor("changeit");
        }
    }

    @Test
    public void shouldRateComponent() throws Exception {

        DERatingValue ratingValue = new DERatingValue(5);

        ResultActions result = createAuthRequest(
                post(BASE_URL + "/{exchangeId}/{componentId}", DE_1_ID, COMPONENT_ID))
                .setContent(ratingValue)
                .execute();

        result.andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.payload.componentId", is(COMPONENT_ID)))
                .andExpect(jsonPath("$.payload.rating", is(RATING)))
                .andExpect(jsonPath("$.payload.numberOfRatings", is(NUMBER_OF_RATINGS)));
    }

    private static SimpleRestResponse<DERatingsSummary> getFakeResponse() {
        DERatingsSummary ratingsSummary = new DERatingsSummary();
        ratingsSummary.setComponentId(COMPONENT_ID);
        ratingsSummary.setNumberOfRatings(NUMBER_OF_RATINGS);
        ratingsSummary.setRating(RATING);

        return new SimpleRestResponse<>(ratingsSummary);
    }
}
