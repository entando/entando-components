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

import org.entando.entando.aps.system.services.digitalexchange.job.DigitalExchangeJob;
import org.entando.entando.aps.system.services.digitalexchange.job.DigitalExchangeJobRepository;
import org.entando.entando.aps.system.services.digitalexchange.job.JobType;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JobResourceControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private DigitalExchangeJobRepository repo;

    @Test
    public void shoudlReturnAll() throws Exception {

        DigitalExchangeJob job1 = new DigitalExchangeJob();
        job1.setId("job1");
        repo.save(job1);

        ResultActions result = createAuthRequest(get("/digitalExchange/job")).execute();

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData.totalItems", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is("job1")));

        repo.deleteById("job1");
    }

    @Test
    public void filterTest() throws Exception {

        DigitalExchangeJob job1 = new DigitalExchangeJob();
        job1.setId("job1");
        job1.setProgress(0.4);
        job1.setJobType(JobType.INSTALL);
        repo.save(job1);

        DigitalExchangeJob job2 = new DigitalExchangeJob();
        job2.setId("job2");
        job2.setProgress(0.7);
        job2.setJobType(JobType.UNINSTALL);
        repo.save(job2);

        ResultActions result = createAuthRequest(get("/digitalExchange/job")
                .param("filters[0].attribute", "progress")
                .param("filters[0].operator", "gt")
                .param("filters[0].value", "0.5")).execute();

        result.andDo(print())
                .andExpect(jsonPath("$.metaData.totalItems", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is("job2")));

        result = createAuthRequest(get("/digitalExchange/job")
                .param("filters[0].attribute", "progress")
                .param("filters[0].operator", "lt")
                .param("filters[0].value", "0.5")).execute();

        result.andDo(print())
                .andExpect(jsonPath("$.metaData.totalItems", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is("job1")));

        result = createAuthRequest(get("/digitalExchange/job")
                .param("filters[0].attribute", "id")
                .param("filters[0].operator", "eq")
                .param("filters[0].value", "job1")).execute();

        result.andDo(print())
                .andExpect(jsonPath("$.metaData.totalItems", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is("job1")));

        result = createAuthRequest(get("/digitalExchange/job")
                .param("filters[0].attribute", "jobType")
                .param("filters[0].operator", "eq")
                .param("filters[0].value", "UNINSTALL")).execute();

        result.andDo(print())
                .andExpect(jsonPath("$.metaData.totalItems", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is("job2")));

        repo.deleteById("job1");
        repo.deleteById("job2");
    }
}
