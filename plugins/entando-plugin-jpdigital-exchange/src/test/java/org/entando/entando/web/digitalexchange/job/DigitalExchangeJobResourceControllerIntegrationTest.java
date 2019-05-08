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
package org.entando.entando.web.digitalexchange.job;

import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.entando.entando.aps.system.jpa.servdb.repo.DigitalExchangeJobRepository;
import org.entando.entando.aps.system.services.digitalexchange.job.JobType;
import org.entando.entando.crypt.DefaultTextEncryptor;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.test.context.ActiveProfiles;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("DEJobResourceControllerTest")
public class DigitalExchangeJobResourceControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private DigitalExchangeJobRepository repo;

    @Configuration
    @Profile("DEJobResourceControllerTest")
    public static class TestConfig {

        @Bean
        @Primary
        public TextEncryptor getDigitalExchangeTextEncryptor() {
            return new DefaultTextEncryptor("changeit");
        }
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        repo.deleteAll();
    }

    @After
    public void tearDown() {
        repo.deleteAll();
    }

    @Test
    public void shoudlReturnAll() throws Exception {

        DigitalExchangeJob job1 = new DigitalExchangeJob();
        String jobId = repo.save(job1).getId();

        ResultActions result = createAuthRequest(get("/digitalExchange/jobs")).execute();

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData.totalItems", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is(jobId)));
    }

    @Test
    public void filterTest() throws Exception {

        DigitalExchangeJob job1 = new DigitalExchangeJob();
        job1.setProgress(0.4);
        job1.setJobType(JobType.INSTALL);
        String jobId1 = repo.save(job1).getId();

        DigitalExchangeJob job2 = new DigitalExchangeJob();
        job2.setProgress(0.7);
        job2.setJobType(JobType.UNINSTALL);
        String jobId2 = repo.save(job2).getId();

        ResultActions result = createAuthRequest(get("/digitalExchange/jobs")
                .param("filters[0].attribute", "progress")
                .param("filters[0].operator", "gt")
                .param("filters[0].value", "0.5")).execute();

        result.andDo(print())
                .andExpect(jsonPath("$.metaData.totalItems", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is(jobId2)));

        result = createAuthRequest(get("/digitalExchange/jobs")
                .param("filters[0].attribute", "progress")
                .param("filters[0].operator", "lt")
                .param("filters[0].value", "0.5")).execute();

        result.andDo(print())
                .andExpect(jsonPath("$.metaData.totalItems", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is(jobId1)));

        result = createAuthRequest(get("/digitalExchange/jobs")
                .param("filters[0].attribute", "id")
                .param("filters[0].operator", "eq")
                .param("filters[0].value", jobId1)).execute();

        result.andDo(print())
                .andExpect(jsonPath("$.metaData.totalItems", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is(jobId1)));

        result = createAuthRequest(get("/digitalExchange/jobs")
                .param("filters[0].attribute", "jobType")
                .param("filters[0].operator", "eq")
                .param("filters[0].value", "UNINSTALL")).execute();

        result.andDo(print())
                .andExpect(jsonPath("$.metaData.totalItems", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is(jobId2)));
    }

    @Test
    public void shouldReturnSingleJob() throws Exception {

        DigitalExchangeJob job1 = new DigitalExchangeJob();
        String jobId = repo.save(job1).getId();

        ResultActions result = createAuthRequest(get("/digitalExchange/jobs/{jobId}", jobId)).execute();

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.payload.id", is(jobId)));
    }

    @Test
    public void shouldReturnNotFound() throws Exception {

        ResultActions result = createAuthRequest(get("/digitalExchange/jobs/{jobId}", "unexisting")).execute();

        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }
}
