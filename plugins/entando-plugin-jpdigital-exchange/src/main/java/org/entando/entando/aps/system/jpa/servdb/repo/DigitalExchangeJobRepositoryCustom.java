package org.entando.entando.aps.system.jpa.servdb.repo;

import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.entando.entando.aps.system.services.digitalexchange.job.JobType;

import java.util.Optional;

public interface DigitalExchangeJobRepositoryCustom {

    Optional<DigitalExchangeJob> findLast(String componentId, JobType jobType);
}
