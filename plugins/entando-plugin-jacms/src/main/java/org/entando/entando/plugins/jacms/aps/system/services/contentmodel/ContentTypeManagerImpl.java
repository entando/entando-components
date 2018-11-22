package org.entando.entando.plugins.jacms.aps.system.services.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import org.entando.entando.web.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContentTypeManagerImpl implements ContentTypeManager {

    private final ContentTypeDao contentTypeDao;

    @Autowired
    public ContentTypeManagerImpl(ContentTypeDao contentTypeDao) {
        this.contentTypeDao = contentTypeDao;
    }

    @Override
    public ContentTypeDto create(ContentTypeDto entity) {
        return contentTypeDao.create(entity);
    }

    @Override
    public ContentTypeDto update(ContentTypeDto entity) {
        return contentTypeDao.update(entity);
    }

    @Override
    public PagedMetadata<ContentTypeDto> findMany(RestListRequest listRequest) {
        return contentTypeDao.list();
    }

    @Override
    public Optional<ContentTypeDto> findById(Long id) {
        return contentTypeDao.findById(id);
    }

    @Override
    public void delete(Long id) {
        contentTypeDao.delete(id);
    }
}
