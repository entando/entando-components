package org.entando.entando.plugins.jacms.aps.system.services;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import org.entando.entando.plugins.jacms.aps.system.managers.ContentTypeManager;
import org.entando.entando.web.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContentTypeServiceImpl implements ContentTypeService {

    private final ContentTypeManager contentTypeManager;

    @Autowired
    public ContentTypeServiceImpl(ContentTypeManager contentTypeManager) {
        this.contentTypeManager = contentTypeManager;
    }

    @Override
    public ContentTypeDto create(ContentTypeDto entity) {
        return contentTypeManager.create(entity);
    }

    @Override
    public ContentTypeDto update(ContentTypeDto entity) {
        return contentTypeManager.update(entity);
    }

    @Override
    public PagedMetadata<ContentTypeDto> findMany(RestListRequest listRequest) {
        return contentTypeManager.findMany(listRequest);
    }

    @Override
    public Optional<ContentTypeDto> findById(Long id) {
        return contentTypeManager.findById(id);
    }

    @Override
    public void delete(Long id) {
        contentTypeManager.delete(id);
    }
}
