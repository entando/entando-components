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
package org.entando.entando.aps.system.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JobRepository extends SimpleJpaRepository<JobEntity, Long> {

    private final EntityManager em;

    @Autowired
    public JobRepository(EntityManager entityManager) {
        super(JobEntity.class, entityManager);
        this.em = entityManager;
    }

    @Transactional
    public void create(JobEntity job) {
        em.persist(job);
    }

    public List<JobEntity> list() {
        CriteriaQuery<JobEntity> cq = em.getCriteriaBuilder().createQuery(JobEntity.class);
        Root<JobEntity> root = cq.from(JobEntity.class);
        cq.select(root);
        TypedQuery<JobEntity> q = em.createQuery(cq);
        List<JobEntity> result = q.getResultList();
        return result;
    }
}
