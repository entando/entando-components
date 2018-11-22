package org.entando.entando.plugins.jacms.aps.system.services.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import org.entando.entando.web.common.model.*;

import java.util.Optional;

public interface ContentTypeManager {

    ContentTypeDto create(ContentTypeDto entity);

    ContentTypeDto update(ContentTypeDto entity);

    PagedMetadata<ContentTypeDto> findMany(RestListRequest listRequest);

    Optional<ContentTypeDto> findById(Long id);

    void delete(Long id);
}
