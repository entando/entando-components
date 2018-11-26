package org.entando.entando.plugins.jacms.aps.system.managers;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeDto;
import org.entando.entando.plugins.jacms.aps.system.persistence.AttributeDao;
import org.entando.entando.web.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttributeManagerImpl implements AttributeManager {

    private final AttributeDao dao;

    @Autowired
    public AttributeManagerImpl(AttributeDao dao) {
        this.dao = dao;
    }

    @Override
    public AttributeDto create(AttributeDto entity) {
        return dao.create(entity);
    }

    @Override
    public AttributeDto update(AttributeDto entity) {
        return dao.update(entity);
    }

    @Override
    public PagedMetadata<AttributeDto> findMany(RestListRequest listRequest) {
        return dao.list();
    }

    @Override
    public Optional<AttributeDto> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    public void delete(Long id) {
        dao.delete(id);
    }
}
