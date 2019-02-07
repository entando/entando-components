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

import java.util.List;
import org.apache.commons.lang.RandomStringUtils;
import org.entando.entando.aps.system.jpa.JobEntity;
import org.entando.entando.aps.system.jpa.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/digitalExchange/job")
public class JobResourceController {

    @Autowired
    private JobRepository jobRepository;

    @GetMapping
    public List<JobEntity> list() {
        return jobRepository.list();
    }

    @PostMapping()
    public JobEntity create() {
        JobEntity job = new JobEntity();
        job.setUser(RandomStringUtils.randomAlphanumeric(20));
        jobRepository.create(job);
        return job;
    }
}
