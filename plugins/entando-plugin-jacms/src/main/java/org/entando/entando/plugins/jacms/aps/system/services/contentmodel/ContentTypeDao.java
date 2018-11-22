package org.entando.entando.plugins.jacms.aps.system.services.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import org.entando.entando.web.common.model.PagedMetadata;

import java.util.Optional;

public interface ContentTypeDao {

    ContentTypeDto update(ContentTypeDto model);
    ContentTypeDto create(ContentTypeDto model);
    void delete(Long id);
    PagedMetadata<ContentTypeDto> list();

    Optional<ContentTypeDto> findById(Long id);
}
