package org.entando.entando.plugins.jacms.aps.system.services;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeDto;
import org.entando.entando.plugins.jacms.aps.system.managers.AttributeManager;
import org.entando.entando.web.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttributeServiceImpl implements AttributeService {

    private final AttributeManager attributeManager;

    @Autowired
    public AttributeServiceImpl(AttributeManager attributeManager) {
        this.attributeManager = attributeManager;
    }

    @Override
    public AttributeDto create(AttributeDto entity) {
        return attributeManager.create(entity);
    }

    @Override
    public AttributeDto update(AttributeDto entity) {
        return attributeManager.update(entity);
    }

    @Override
    public PagedMetadata<AttributeDto> findMany(RestListRequest listRequest) {
        return attributeManager.findMany(listRequest);
    }

    @Override
    public Optional<AttributeDto> findById(Long id) {
        return attributeManager.findById(id);
    }

    @Override
    public void delete(Long id) {
        attributeManager.delete(id);
    }

}
