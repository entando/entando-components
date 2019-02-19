package org.entando.entando.aps.system.jpa.servdb.repo;

import org.entando.entando.aps.system.jpa.JpaSpecificationRepository;
import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(transactionManager = "servTransactionManager")
public interface DigitalExchangeJobRepository extends JpaSpecificationRepository<DigitalExchangeJob, String>,
        DigitalExchangeJobRepositoryCustom {

}
