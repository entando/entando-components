package org.entando.entando.aps.system.jpa.servdb;

import org.entando.entando.aps.system.jpa.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(transactionManager = "servTransactionManager")
public interface DigitalExchangeJobRepository extends JpaSpecificationRepository<DigitalExchangeJob, String>,
        DigitalExchangeJobRepositoryCustom {

}
