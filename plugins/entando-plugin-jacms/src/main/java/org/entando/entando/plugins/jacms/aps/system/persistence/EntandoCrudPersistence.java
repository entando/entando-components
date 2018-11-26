package org.entando.entando.plugins.jacms.aps.system.persistence;

import org.entando.entando.web.common.model.PagedMetadata;

import java.util.Optional;

public interface EntandoCrudPersistence<T, ID> {

    T update(T model);
    T create(T model);
    void delete(ID id);
    PagedMetadata<T> list();

    Optional<T> findById(ID id);
}
