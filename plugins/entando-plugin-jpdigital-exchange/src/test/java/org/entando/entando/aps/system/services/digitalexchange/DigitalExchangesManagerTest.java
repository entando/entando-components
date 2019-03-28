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

import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import java.util.List;
import org.entando.entando.aps.system.DigitalExchangeConstants;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeOAuth2RestTemplateFactory;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.aps.system.services.digitalexchange.signature.SignatureUtil;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils.*;

public class DigitalExchangesManagerTest {

    @Mock
    private ConfigInterface configManager;

    @Mock
    private DigitalExchangeOAuth2RestTemplateFactory restTemplateFactory;

    @InjectMocks
    private DigitalExchangesManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockRestTemplateFactory();
        mockValidDigitalExchangesConfig();
        manager.refresh();
    }

    @Test
    public void shouldReturnDigitalExchanges() {

        List<DigitalExchange> digitalExchanges = manager.getDigitalExchanges();

        assertThat(digitalExchanges)
                .isNotNull()
                .hasSize(1)
                .element(0)
                // Testing JAXB deserialization
                .extracting(DigitalExchange::getId, DigitalExchange::getName, DigitalExchange::getUrl,
                        DigitalExchange::getClientKey, DigitalExchange::getClientSecret,
                        DigitalExchange::isActive, DigitalExchange::getTimeout)
                .containsExactly(DE_1_ID, DE_1_NAME, DE_URL, "client-id", "client-secret", true, 5000);

        assertThat(manager.getRestTemplate(DE_1_ID)).isNotNull();
    }

    @Test
    public void shouldReturnEmptyListForMissingConfiguration() {

        // missing configuration
        when(configManager.getConfigItem(DigitalExchangeConstants.CONFIG_ITEM_DIGITAL_EXCHANGES))
                .thenReturn(null);
        manager.refresh();

        assertThat(manager.getDigitalExchanges())
                .isNotNull().isEmpty();
    }

    @Test
    public void shouldReturnEmptyListForInvalidConfiguration() {

        // invalid configuration
        when(configManager.getConfigItem(DigitalExchangeConstants.CONFIG_ITEM_DIGITAL_EXCHANGES))
                .thenReturn("");
        manager.refresh();

        assertThat(manager.getDigitalExchanges())
                .isNotNull().isEmpty();
    }

    @Test
    public void shouldFindDigitalExchange() {
        assertTrue(manager.findById(DE_1_ID).isPresent());
        assertThat(manager.getRestTemplate(DE_1_ID)).isNotNull();
    }

    @Test
    public void shouldFindNothing() {
        assertFalse(manager.findById(INEXISTENT_DE_ID).isPresent());
    }

    @Test
    public void shouldAddDigitalExchange() {

        DigitalExchange digitalExchange = manager.create(getNewDE());
        
        assertThat(digitalExchange)
                .isNotNull()
                .extracting(DigitalExchange::getId, DigitalExchange::getName)
                .containsExactly(digitalExchange.getId(), NEW_DE_NAME);

        assertThat(manager.getRestTemplate(DE_1_ID)).isNotNull();
        assertThat(manager.getRestTemplate(digitalExchange.getId())).isNotNull();
    }

    @Test
    public void shouldNotAddDisabledDigitalExchangeTemplateOnCreate() {
        DigitalExchange digitalExchange = getNewDE();
        digitalExchange.setActive(false);

        digitalExchange = manager.create(digitalExchange);
        
        assertThat(digitalExchange).isNotNull();
        assertTrue(manager.findById(digitalExchange.getId()).isPresent());
        assertThat(manager.getRestTemplate(digitalExchange.getId())).isNull();
    }

    @Test
    public void shouldUpdateDigitalExchange() {
        DigitalExchange digitalExchange = getDE1();
        int timeout = 1000;
        digitalExchange.setTimeout(timeout);

        assertThat(manager.update(digitalExchange))
                .isNotNull()
                .extracting(DigitalExchange::getId, DigitalExchange::getTimeout)
                .containsExactly(DE_1_ID, timeout);

        assertThat(manager.getRestTemplate(DE_1_ID)).isNotNull();
        assertThat(manager.getRestTemplate(DE_1_ID)).isNotNull();
    }

    @Test
    public void shouldNotUpdateInexistentDigitalExchange() {
        assertThat(manager.update(getInexistentDE())).isNull();
    }

    @Test
    public void shouldNotAddDisabledDigitalExchangeTemplateOnUpdate() {
        DigitalExchange digitalExchange = getDE1();
        digitalExchange.setActive(false);

        assertThat(manager.update(digitalExchange)).isNotNull();
        assertTrue(manager.findById(DE_1_ID).isPresent());
        assertThat(manager.getRestTemplate(DE_1_ID)).isNull();
    }

    @Test
    public void shouldDeleteDigitalExchange() {
        manager.delete(DE_1_ID);
        assertFalse(manager.findById(DE_1_ID).isPresent());
        assertThat(manager.getRestTemplate(DE_1_ID)).isNull();
    }

    @Test
    public void shouldFailSafelyWhenUnableToCreateOAuth2RestTemplate() {

        when(restTemplateFactory.createOAuth2RestTemplate(any())).thenReturn(null);

        DigitalExchange newDE = manager.create(getNewDE());
        assertTrue(manager.findById(newDE.getId()).isPresent());
        assertFalse(manager.findById(newDE.getId()).get().isActive());
        assertThat(manager.getRestTemplate(newDE.getId())).isNull();
    }

    private void mockRestTemplateFactory() {
        when(restTemplateFactory.createOAuth2RestTemplate(any()))
                .thenReturn(new OAuth2RestTemplate(new ClientCredentialsResourceDetails()));
    }

    private void mockValidDigitalExchangesConfig() {

        when(configManager.getConfigItem(DigitalExchangeConstants.CONFIG_ITEM_DIGITAL_EXCHANGES))
                .thenReturn("<digitalExchanges>"
                        + "  <digitalExchange>"
                        + "    <id>" + DE_1_ID + "</id>"
                        + "    <name>" + DE_1_NAME + "</name>"
                        + "    <url>" + DE_URL + "</url>"
                        + "    <key>client-id</key>"
                        + "    <secret>client-secret</secret>"
                        + "    <timeout>5000</timeout>"
                        + "    <active>true</active>"
                        + "    <publicKey>" + getTestPublicKey() + "</publicKey>"
                        + "  </digitalExchange>"
                        + "</digitalExchanges>");
    }
}
