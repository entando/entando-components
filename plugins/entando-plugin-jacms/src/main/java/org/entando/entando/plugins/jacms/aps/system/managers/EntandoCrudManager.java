package org.entando.entando.plugins.jacms.aps.system.managers;

import org.entando.entando.web.common.model.*;

import java.util.Optional;

public interface EntandoCrudManager<T, ID> {

    T create(T entity);

    T update(T entity);

    PagedMetadata<T> findMany(RestListRequest listRequest);

    Optional<T> findById(ID id);

    void delete(ID id);
}
