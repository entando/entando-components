package org.entando.entando.aps.system.jpa.servdb;

import org.entando.entando.aps.system.services.digitalexchange.job.JobType;

import java.util.Optional;

public interface DigitalExchangeJobRepositoryCustom {

    Optional<DigitalExchangeJob> findLast(String componentId, JobType jobType);
}
