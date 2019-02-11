package org.entando.entando.aps.system.jpa.portdb;

import org.entando.entando.aps.system.jpa.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(transactionManager = "portTransactionManager")
public interface DigitalExchangePageRepository extends JpaSpecificationRepository<DigitalExchangePage,Long> {

}
