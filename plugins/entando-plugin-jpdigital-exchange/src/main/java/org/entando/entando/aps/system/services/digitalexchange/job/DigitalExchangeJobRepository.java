package org.entando.entando.aps.system.services.digitalexchange.job;

import javax.persistence.EntityManager;
import org.entando.entando.aps.system.jpa.EntandoJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DigitalExchangeJobRepository extends EntandoJPARepository<DigitalExchangeJob, String> {

    @Autowired
    public DigitalExchangeJobRepository(EntityManager entityManager) {
        super(DigitalExchangeJob.class, entityManager, new JobFieldsConverter());
    }
}
