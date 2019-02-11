package org.entando.entando.aps.system.jpa.portdb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(transactionManager = "portTransactionManager")
public interface DigitalExchangePageRepository extends JpaRepository<DigitalExchangePage, Long> {

}
