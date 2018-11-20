package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import org.entando.entando.web.common.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContentTypeServiceImpl implements ContentTypeService {

    // TODO Replace with DB persistence
    private Map<Long, ContentTypeDto> tmpMap = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public ContentTypeDto create(ContentTypeDto entity) {
        tmpMap.put(nextId, entity);
        entity.setId(nextId);
        nextId++;
        return entity;
    }

    @Override
    public ContentTypeDto update(ContentTypeDto entity) {
        tmpMap.put(nextId, entity);
        entity.setId(nextId);
        nextId++;
        return entity;
    }

    @Override
    public PagedMetadata<ContentTypeDto> findMany(RestListRequest listRequest) {
        List<ContentTypeDto> result = new ArrayList<>(tmpMap.values());
        return new PagedMetadata<>(listRequest, result, result.size());
    }

    @Override
    public Optional<ContentTypeDto> findById(Long id) {
        return Optional.ofNullable(tmpMap.get(id));
    }

    @Override
    public void delete(Long id) {
        tmpMap.remove(id);
    }
}
