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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import org.apache.commons.lang.RandomStringUtils;
import org.entando.entando.aps.system.DigitalExchangeConstants;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeOAuth2RestTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchangesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

@Service
public class DigitalExchangesManagerImpl implements DigitalExchangesManager {

    private static final int ID_LENGTH = 20;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConfigInterface configManager;
    private final DigitalExchangeOAuth2RestTemplateFactory restTemplateFactory;

    private final List<DigitalExchange> exchanges = new CopyOnWriteArrayList<>();
    private final Map<String, OAuth2RestTemplate> templates = new ConcurrentHashMap<>();

    @Autowired
    public DigitalExchangesManagerImpl(ConfigInterface configManager, DigitalExchangeOAuth2RestTemplateFactory restTemplateFactory) {
        this.configManager = configManager;
        this.restTemplateFactory = restTemplateFactory;
    }

    @PostConstruct
    public void init() {
        loadDigitalExchanges();
        exchanges.forEach(this::addRestTemplate);
    }

    @Override
    public void refresh() {
        exchanges.clear();
        templates.clear();
        init();
    }

    @Override
    public List<DigitalExchange> getDigitalExchanges() {
        return exchanges;
    }

    @Override
    public DigitalExchange create(DigitalExchange digitalExchange) {

        if (digitalExchange.getId() == null) {
            digitalExchange.setId(RandomStringUtils.randomAlphanumeric(ID_LENGTH));
        } else if (findById(digitalExchange.getId()).isPresent()) {
            throw new IllegalArgumentException("DigitalExchange " + digitalExchange.getId() + " already exists");
        }

        exchanges.add(digitalExchange);
        updateDigitalExchangesConfig();
        if (digitalExchange.isActive()) {
            addRestTemplate(digitalExchange);
        }
        return digitalExchange;
    }

    @Override
    public Optional<DigitalExchange> findById(String digitalExchangeId) {
        return exchanges.stream()
                .filter(de -> de.getId().equals(digitalExchangeId))
                .findFirst();
    }

    @Override
    public DigitalExchange update(DigitalExchange digitalExchange) {
        OptionalInt index = getIndex(digitalExchange.getId());
        if (index.isPresent()) {
            exchanges.replaceAll(de -> {
                if (de.getId().equals(digitalExchange.getId())) {
                    updateDigitalExchangesConfig();
                    templates.remove(digitalExchange.getId());
                    if (digitalExchange.isActive()) {
                        addRestTemplate(digitalExchange);
                    }
                    return digitalExchange;
                }
                return de;
            });
            return digitalExchange;
        }
        return null;
    }

    @Override
    public void delete(String digitalExchangeId) {
        getIndex(digitalExchangeId)
                .ifPresent(i -> {
                    exchanges.remove(i);
                    updateDigitalExchangesConfig();
                    templates.remove(digitalExchangeId);
                });
    }

    private OptionalInt getIndex(String digitalExchangeId) {
        return IntStream.range(0, exchanges.size())
                .filter(i -> exchanges.get(i).getId().equals(digitalExchangeId))
                .findFirst();
    }

    @Override
    public OAuth2RestTemplate getRestTemplate(String digitalExchangeId) {
        return templates.get(digitalExchangeId);
    }

    private void loadDigitalExchanges() {
        String digitalExchangesConfig = configManager.getConfigItem(DigitalExchangeConstants.CONFIG_ITEM_DIGITAL_EXCHANGES);

        if (digitalExchangesConfig == null) {
            logger.warn("DigitalExchanges configuration not found");
            return;
        }

        try {
            DigitalExchangesConfig config = JAXB.unmarshal(new StringReader(digitalExchangesConfig), DigitalExchangesConfig.class);
            exchanges.addAll(config.getDigitalExchanges());
        } catch (DataBindingException ex) {
            logger.error("Unable to parse DigitalExchanges XML configuration", ex);
        }
    }

    private void addRestTemplate(DigitalExchange digitalExchange) {
        OAuth2RestTemplate template = restTemplateFactory.createOAuth2RestTemplate(digitalExchange);
        if (template != null) {
            templates.put(digitalExchange.getId(), template);
        } else {
            logger.error("Error creating OAuth2RestTemplate for DigitalExchange {} ({}). This instance will be disabled.",
                    digitalExchange.getId(), digitalExchange.getName());
            digitalExchange.setActive(false);
            update(digitalExchange);
        }
    }

    private void updateDigitalExchangesConfig() {
        DigitalExchangesConfig config = new DigitalExchangesConfig();
        config.setDigitalExchanges(exchanges);
        StringWriter sw = new StringWriter();
        JAXB.marshal(config, sw);
        String configString = sw.toString();

        try {
            configManager.updateConfigItem(DigitalExchangeConstants.CONFIG_ITEM_DIGITAL_EXCHANGES, configString);
        } catch (ApsSystemException ex) {
            throw new RuntimeException("Error updating DigitalExchanges configuration", ex);
        }
    }
}
