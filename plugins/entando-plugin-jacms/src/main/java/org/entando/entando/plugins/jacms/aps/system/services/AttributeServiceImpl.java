package org.entando.entando.plugins.jacms.aps.system.services;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeDto;
import org.entando.entando.plugins.jacms.aps.system.managers.AttributeManager;
import org.entando.entando.web.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttributeServiceImpl implements AttributeService {

    private final AttributeManager manager;

    @Autowired
    public AttributeServiceImpl(AttributeManager manager) {
        this.manager = manager;
    }

    @Override
    public AttributeDto create(AttributeDto entity) {
        return manager.create(entity);
    }

    @Override
    public AttributeDto update(AttributeDto entity) {
        return manager.update(entity);
    }

    @Override
    public PagedMetadata<AttributeDto> findMany(RestListRequest listRequest) {
        return manager.findMany(listRequest);
    }

    @Override
    public Optional<AttributeDto> findById(Long id) {
        return manager.findById(id);
    }

    @Override
    public void delete(Long id) {
        manager.delete(id);
    }

}
