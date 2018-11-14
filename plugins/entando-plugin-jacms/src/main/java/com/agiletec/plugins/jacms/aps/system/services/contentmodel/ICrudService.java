package com.agiletec.plugins.jacms.aps.system.services.contentmodel;

import org.entando.entando.web.common.model.*;

public interface ICrudService<T, ID> {

    T save(T entity);

    PagedMetadata<T> findAll(RestListRequest listRequest);

    T findById(ID id);

    void delete(ID id);
}
