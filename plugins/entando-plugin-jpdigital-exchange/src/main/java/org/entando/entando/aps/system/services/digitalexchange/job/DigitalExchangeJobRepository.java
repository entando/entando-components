package org.entando.entando.aps.system.services.digitalexchange.job;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DigitalExchangeJobRepository extends SimpleJpaRepository<DigitalExchangeJob, String> {

    private final EntityManager em;

    @Autowired
    public DigitalExchangeJobRepository(EntityManager entityManager) {
        super(DigitalExchangeJob.class, entityManager);
        this.em = entityManager;
    }

    public List<DigitalExchangeJob> list() {
        CriteriaQuery<DigitalExchangeJob> cq = em.getCriteriaBuilder().createQuery(DigitalExchangeJob.class);
        Root<DigitalExchangeJob> root = cq.from(DigitalExchangeJob.class);
        cq.select(root);
        TypedQuery<DigitalExchangeJob> q = em.createQuery(cq);
        return q.getResultList();
    }

}
