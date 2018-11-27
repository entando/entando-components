package org.entando.entando.plugins.jacms.aps.system.managers;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeRoleDto;
import org.entando.entando.plugins.jacms.aps.system.persistence.AttributeRoleDao;
import org.entando.entando.web.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttributeRoleManagerImpl implements AttributeRoleManager {

    private final AttributeRoleDao dao;

    @Autowired
    public AttributeRoleManagerImpl(AttributeRoleDao dao) {
        this.dao = dao;
    }

    @Override
    public AttributeRoleDto create(AttributeRoleDto entity) {
        return dao.create(entity);
    }

    @Override
    public AttributeRoleDto update(AttributeRoleDto entity) {
        return dao.update(entity);
    }

    @Override
    public PagedMetadata<AttributeRoleDto> findMany(RestListRequest listRequest) {
        return dao.list();
    }

    @Override
    public Optional<AttributeRoleDto> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    public void delete(Long id) {
        dao.delete(id);
    }
}
