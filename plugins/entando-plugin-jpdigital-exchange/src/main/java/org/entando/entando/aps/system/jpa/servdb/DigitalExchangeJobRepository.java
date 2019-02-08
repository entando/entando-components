package org.entando.entando.aps.system.jpa.servdb;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.entando.entando.aps.system.jpa.EntandoJPARepository;
import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.entando.entando.aps.system.services.digitalexchange.job.JobFieldsConverter;
import org.entando.entando.aps.system.services.digitalexchange.job.JobType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DigitalExchangeJobRepository extends EntandoJPARepository<DigitalExchangeJob, String> {

    @Autowired
    public DigitalExchangeJobRepository(@Qualifier("servEntityManager") EntityManager entityManager) {
        super(DigitalExchangeJob.class, entityManager, new JobFieldsConverter());
    }

    public Optional<DigitalExchangeJob> findLast(String componentId, JobType jobType) {

        Specification<DigitalExchangeJob> specification = new Specification<DigitalExchangeJob>() {
            @Override
            public Predicate toPredicate(Root<DigitalExchangeJob> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                return cb.and(
                        cb.equal(root.get("componentId"), componentId),
                        cb.equal(root.get("jobType"), jobType)
                );
            }
        };

        Sort sort = Sort.by(Sort.Direction.DESC, "started");
        Pageable pageable = PageRequest.of(0, 1, sort);

        List<DigitalExchangeJob> all = findAll(specification, pageable).getContent();
        if (all.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(all.get(0));
    }
}
