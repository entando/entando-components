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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.model.RestResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils.*;

/**
 * Helper class that can be used to fake the behavior of a set of a DE instances
 * in a consistent way.
 *
 * Example:
 * <pre>
 * new DigitalExchangesMocker()
 *        // Simulate a hello response
 *        .addDigitalExchange("DE 1", request -> {
 *            // here you can obtain information about the mocked request (HTTP
 *            // method, URL parameters and payload) from the request variable
 *            return new SimpleRestResponse<>("hello");
 *        })
 *        // Simulate an broken server
 *        .addDigitalExchange("Unreachable DE", () -> {
 *            throw new RestClientException("Connection refused");
 *        })
 *        .initMocks();
 * </pre>
 */
public class DigitalExchangesMocker {

    private final OAuth2RestTemplate restTemplate;
    private final DigitalExchangeOAuth2RestTemplateFactory restTemplateFactory;
    private final List<DigitalExchange> exchanges;
    private final Map<String, Function<DigitalExchangeMockedRequest, RestResponse<?, ?>>> responsesMap;

    public DigitalExchangesMocker() {
        this.restTemplate = mock(OAuth2RestTemplate.class);
        this.restTemplateFactory = mock(DigitalExchangeOAuth2RestTemplateFactory.class);
        this.exchanges = new ArrayList<>();
        this.responsesMap = new HashMap<>();
    }

    public List<DigitalExchange> getFakeExchanges() {
        return exchanges;
    }

    private DigitalExchange getDigitalExchange(String id) {
        DigitalExchange digitalExchange;
        switch (id) {
            case DE_1_ID:
                digitalExchange = getDE1();
                break;
            case DE_2_ID:
                digitalExchange = getDE2();
                break;
            default:
                digitalExchange = DigitalExchangeTestUtils.getDigitalExchange(id, id + " Name");
                break;
        }
        digitalExchange.setUrl(String.format("https://de%s.entando.com/", exchanges.size() + 1));
        return digitalExchange;
    }

    public DigitalExchangesMocker addDigitalExchange(String id, RestResponse<?, ?> result) {
        return addDigitalExchange(id, (request) -> result, null);
    }

    public DigitalExchangesMocker addDigitalExchange(String id, Runnable runnable) {
        return addDigitalExchange(id, runnable, null);
    }

    public DigitalExchangesMocker addDigitalExchange(String id, Runnable runnable, Consumer<DigitalExchange> deConsumer) {
        return addDigitalExchange(id, (request) -> {
            runnable.run();
            return null;
        }, deConsumer);
    }

    public DigitalExchangesMocker addDigitalExchange(String id, Function<DigitalExchangeMockedRequest, RestResponse<?, ?>> function) {
        return addDigitalExchange(id, function, null);
    }

    public DigitalExchangesMocker addDigitalExchange(String id, Function<DigitalExchangeMockedRequest, RestResponse<?, ?>> function, Consumer<DigitalExchange> deConsumer) {

        DigitalExchange digitalExchange = getDigitalExchange(id);
        if (deConsumer != null) {
            deConsumer.accept(digitalExchange);
        }
        exchanges.add(digitalExchange);

        responsesMap.put(digitalExchange.getUrl(), function);

        return this;
    }

    public DigitalExchangeOAuth2RestTemplateFactory initMocks() {

        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenAnswer(invocation -> {

                    String url = invocation.getArgument(0);
                    String baseUrl = getBaseUrl(url);
                    Map<String, String> urlParameters = getURLParameters(url);

                    DigitalExchangeMockedRequest request = new DigitalExchangeMockedRequest()
                            .setMethod(invocation.getArgument(1))
                            .setUrlParams(urlParameters)
                            .setEntity(invocation.getArgument(2));

                    return ResponseEntity.ok(responsesMap.get(baseUrl).apply(request));
                });

        when(restTemplateFactory.createOAuth2RestTemplate(any())).thenReturn(restTemplate);

        return restTemplateFactory;
    }

    private String getBaseUrl(String url) {
        return exchanges.stream().map(ex -> ex.getUrl())
                .filter(baseUrl -> url.startsWith(baseUrl)).findFirst().get();
    }

    private Map<String, String> getURLParameters(String url) {
        Map<String, String> map = new HashMap<>();
        int questionMarkPosition = url.indexOf("?");
        if (questionMarkPosition != -1) {
            String query = url.substring(questionMarkPosition + 1);
            for (String queryPart : query.split("&")) {
                String[] split = queryPart.split("=");
                map.put(split[0], split[1]);
            }
        }
        return map;
    }
}
