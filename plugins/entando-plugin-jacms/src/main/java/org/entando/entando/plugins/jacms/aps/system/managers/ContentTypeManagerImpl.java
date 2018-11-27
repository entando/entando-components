package org.entando.entando.plugins.jacms.aps.system.managers;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import org.entando.entando.plugins.jacms.aps.system.persistence.ContentTypeDao;
import org.entando.entando.web.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContentTypeManagerImpl implements ContentTypeManager {

    private final ContentTypeDao dao;

    @Autowired
    public ContentTypeManagerImpl(ContentTypeDao dao) {
        this.dao = dao;
    }

    @Override
    public ContentTypeDto create(ContentTypeDto entity) {
        return dao.create(entity);
    }

    @Override
    public ContentTypeDto update(ContentTypeDto entity) {
        return dao.update(entity);
    }

    @Override
    public PagedMetadata<ContentTypeDto> findMany(RestListRequest listRequest) {
        return dao.list();
    }

    @Override
    public Optional<ContentTypeDto> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    public void delete(Long id) {
        dao.delete(id);
    }
}
