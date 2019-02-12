package org.entando.entando.aps.system.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JpaSpecificationRepository<T,ID> extends JpaRepository<T,ID>, JpaSpecificationExecutor<T> {
}
