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
package org.entando.entando.web.digitalexchange.install;

import org.entando.entando.aps.system.init.IInitializerManager;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeOAuth2RestTemplateFactory;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesMocker;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.entando.entando.aps.system.services.widgettype.IWidgetService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("DEinstallTest")
public class DigitalExchangeInstallResourceIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String BASE_URL = "/digitalExchange";

    @Autowired
    private IWidgetService widgetService;

    @Autowired
    private IInitializerManager initializerManager;

    @Configuration
    @Profile("DEinstallTest")
    public static class TestConfig {

        @Bean
        @Primary
        public DigitalExchangeOAuth2RestTemplateFactory getRestTemplateFactory() {
            return new DigitalExchangesMocker()
                    .addDigitalExchange(DE_1_ID, () -> {
                    })
                    .initMocks();
        }
    }

    @Test
    public void shouldStartInstallationJob() throws Exception {

        String componentCode = "test_widget";

        ResultActions result = createAuthRequest(get(BASE_URL + "/{exchange}/install/{component}", DE_1_ID, componentCode)).execute();

        result.andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.payload").isString());

        // TODO: This is temporary
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
        }

        SystemInstallationReport report = initializerManager.getCurrentReport();

        assertThat(report.getComponentReport(componentCode, false))
                .isNotNull();
                //.matches(cr -> cr.getStatus() == SystemInstallationReport.Status.OK);

        assertThat(widgetService.getWidget(componentCode))
                .isNotNull();

        //widgetService.removeWidget(componentCode);
    }
}
