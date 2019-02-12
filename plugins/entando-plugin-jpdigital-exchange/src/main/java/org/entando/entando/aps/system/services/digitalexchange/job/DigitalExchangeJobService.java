package org.entando.entando.aps.system.services.digitalexchange.job;

import org.entando.entando.aps.system.jpa.EntityFieldsConverter;
import org.entando.entando.aps.system.jpa.JpaSpecificationRepository;
import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.entando.entando.aps.system.jpa.servdb.repo.DigitalExchangeJobRepository;
import org.entando.entando.aps.system.services.EntandoBaseJpaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DigitalExchangeJobService extends EntandoBaseJpaService<DigitalExchangeJob, String> {

    private final DigitalExchangeJobRepository repository;
    private final JobFieldsConverter fieldsConverter;

    public DigitalExchangeJobService(DigitalExchangeJobRepository repository) {
       this.repository = repository;
       this.fieldsConverter = new JobFieldsConverter();
    }

    @Override
    protected EntityFieldsConverter<DigitalExchangeJob> getFieldsConverter() {
        return this.fieldsConverter;
    }

    @Override
    protected DigitalExchangeJobRepository getRepository() {
        return this.repository;
    }


    public Optional<DigitalExchangeJob> findById(String id) {
        return this.getRepository().findById(id);
    }

    public void save(DigitalExchangeJob job) {
        this.getRepository().save(job);
    }

    public Optional<DigitalExchangeJob> findLast(String componentId, JobType type) {
        return this.getRepository().findLast(componentId, type);
    }

}
