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
package org.entando.entando.aps.system.services.digitalexchange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeCall;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.digitalexchange.client.SimpleDigitalExchangeCall;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import static org.entando.entando.web.digitalexchange.DigitalExchangeValidator.*;

@Service
public class DigitalExchangesServiceImpl implements DigitalExchangesService {

    private final Logger logger = LoggerFactory.getLogger(DigitalExchangesServiceImpl.class);

    private static final String DIGITAL_EXCHANGE_LABEL = "digitalExchange";

    private final DigitalExchangesManager manager;
    private final DigitalExchangesClient client;

    @Autowired
    public DigitalExchangesServiceImpl(DigitalExchangesManager manager, DigitalExchangesClient client) {
        this.manager = manager;
        this.client = client;
    }

    @Override
    public List<DigitalExchange> getDigitalExchanges() {
        return manager.getDigitalExchanges();
    }

    @Override
    public DigitalExchange findById(String id) {
        return manager.findById(id).orElseThrow(()
                -> new ResourceNotFoundException(ERRCODE_DIGITAL_EXCHANGE_NOT_FOUND, DIGITAL_EXCHANGE_LABEL, id));
    }

    @Override
    public DigitalExchange create(DigitalExchange digitalExchange) {

        validateName(digitalExchange);
        validateURL(digitalExchange);

        DigitalExchange storedDigitalExchange = manager.create(digitalExchange);

        verifyHasPublicKey(storedDigitalExchange);

        return storedDigitalExchange;
    }


    @Override
    public DigitalExchange update(DigitalExchange digitalExchange) {
        DigitalExchange oldDigitalExchange = findById(digitalExchange.getId());
        if (!oldDigitalExchange.getName().equals(digitalExchange.getName())) {
            validateName(digitalExchange);
        }
        validateURL(digitalExchange);
        DigitalExchange updatedDe = manager.update(digitalExchange);

        verifyHasPublicKey(updatedDe);

        return updatedDe;
    }

    @Override
    public void delete(String digitalExchangeId) {
        findById(digitalExchangeId);
        manager.delete(digitalExchangeId);
    }

    @Override
    public List<RestError> test(String digitalExchangeId) {
        DigitalExchange digitalExchange = findById(digitalExchangeId);

        SimpleDigitalExchangeCall<Map<String, List<RestError>>> call = new SimpleDigitalExchangeCall<>(
                HttpMethod.GET, new ParameterizedTypeReference<SimpleRestResponse<Map<String, List<RestError>>>>() {
        }, "digitalExchange", "exchanges", "test");

        return client.getSingleResponse(digitalExchange, call).getErrors();
    }

    @Override
    public Map<String, List<RestError>> testAll() {
        return client.getCombinedResult(new TestExchangesCall());
    }

    private void validateName(DigitalExchange digitalExchange) {

        if (getDigitalExchanges().stream()
                .anyMatch(de -> digitalExchange.getName().equals(de.getName()))) {

            BeanPropertyBindingResult errors = new BeanPropertyBindingResult(digitalExchange, DIGITAL_EXCHANGE_LABEL);
            errors.reject(ERRCODE_DIGITAL_EXCHANGE_NAME_TAKEN, new Object[]{digitalExchange.getName()}, "digitalExchange.name.taken");
            throw new ValidationConflictException(errors);
        }
    }

    private void validateURL(DigitalExchange digitalExchange) {
        try {
            UriComponentsBuilder.fromHttpUrl(digitalExchange.getUrl());
        } catch (IllegalArgumentException ex) {
            BeanPropertyBindingResult errors = new BeanPropertyBindingResult(digitalExchange, DIGITAL_EXCHANGE_LABEL);
            errors.reject(ERRCODE_DIGITAL_EXCHANGE_INVALID_URL, null, "digitalExchange.url.invalid");
            throw new ValidationConflictException(errors);
        }
    }

    private void verifyHasPublicKey(DigitalExchange storedDigitalExchange) {
        if (storedDigitalExchange.hasNoPublicKey()) {
            tryUpdateWithRemotePublicKey(storedDigitalExchange);
        }
    }

    private void tryUpdateWithRemotePublicKey(DigitalExchange digitalExchange) {

        try {
            updateWithRemotePublicKey(digitalExchange);
        } catch (Exception ex) {
            logger.error("An error occurred while downloading public key for digital exchange " + digitalExchange.getId());
            digitalExchange.invalidate();
        }
    }

    private void updateWithRemotePublicKey(DigitalExchange digitalExchange) {
        Optional<String> publicKey = getRemotePublicKey(digitalExchange);
        digitalExchange.setPublicKey(publicKey.orElseThrow(NotExistentPublicKey::new));
    }

    private Optional<String> getRemotePublicKey(DigitalExchange digitalExchange) {
        SimpleDigitalExchangeCall<String> call = new SimpleDigitalExchangeCall<>(
                HttpMethod.GET, new ParameterizedTypeReference<SimpleRestResponse<String>>() {
        }, "digitalExchange", "publicKey");

        SimpleRestResponse<String> response = client.getSingleResponse(digitalExchange, call);
        return Optional.ofNullable(response.getPayload());
    }

    private static class NotExistentPublicKey extends RuntimeException {}

    private static class TestExchangesCall extends DigitalExchangeCall<SimpleRestResponse<Map<String, List<RestError>>>, Map<String, List<RestError>>> {

        public TestExchangesCall() {
            super(HttpMethod.GET, new ParameterizedTypeReference<SimpleRestResponse<Map<String, List<RestError>>>>() {
            }, "digitalExchange", "exchanges", "test");
        }

        @Override
        protected SimpleRestResponse<Map<String, List<RestError>>> getEmptyRestResponse() {
            return new SimpleRestResponse<>(new HashMap<>());
        }

        @Override
        protected Map<String, List<RestError>> combineResults(Map<String, SimpleRestResponse<Map<String, List<RestError>>>> results) {
            return results.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getErrors()));
        }
    }
}
