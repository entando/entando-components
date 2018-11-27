package org.entando.entando.plugins.jacms.aps.system.services;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeRoleDto;
import org.entando.entando.plugins.jacms.aps.system.managers.AttributeRoleManager;
import org.entando.entando.web.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttributeRoleServiceImpl implements AttributeRoleService {

    private final AttributeRoleManager manager;

    @Autowired
    public AttributeRoleServiceImpl(AttributeRoleManager manager) {
        this.manager = manager;
    }

    @Override
    public AttributeRoleDto create(AttributeRoleDto entity) {
        return manager.create(entity);
    }

    @Override
    public AttributeRoleDto update(AttributeRoleDto entity) {
        return manager.update(entity);
    }

    @Override
    public PagedMetadata<AttributeRoleDto> findMany(RestListRequest listRequest) {
        return manager.findMany(listRequest);
    }

    @Override
    public Optional<AttributeRoleDto> findById(Long id) {
        return manager.findById(id);
    }

    @Override
    public void delete(Long id) {
        manager.delete(id);
    }
}
