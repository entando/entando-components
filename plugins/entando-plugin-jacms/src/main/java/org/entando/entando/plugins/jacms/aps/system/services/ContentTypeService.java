package org.entando.entando.plugins.jacms.aps.system.services;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.*;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.entity.AbstractEntityTypeService;
import org.entando.entando.web.common.model.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Service
public class ContentTypeService extends AbstractEntityTypeService<IContent, ContentTypeDto> {

    private static final String CONTENT_MODEL_MANAGER = "jacmsContentManager";


    @Override
    protected IDtoBuilder<IContent, ContentTypeDto> getEntityTypeFullDtoBuilder(
            IEntityManager masterManager) {
        return new ContentTypeDtoBuilder(masterManager.getAttributeRoles());
    }

    public ContentTypeDto create(ContentTypeDtoRequest contentType, BindingResult bindingResult) {
        return addEntityType(CONTENT_MODEL_MANAGER, contentType, bindingResult);
    }

    public void delete(String code) {
        super.deleteEntityType(CONTENT_MODEL_MANAGER, code);
    }

    public PagedMetadata<org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto> findMany(RestListRequest listRequest) {
        return getShortEntityTypes(CONTENT_MODEL_MANAGER, listRequest);
    }

    public Optional<ContentTypeDto> findOne(String code) {
        return Optional.ofNullable(super.getFullEntityType(CONTENT_MODEL_MANAGER, code));
    }

    public ContentTypeDto update(ContentTypeDtoRequest contentTypeRequest, BindingResult bindingResult) {
        return updateEntityType(CONTENT_MODEL_MANAGER, contentTypeRequest, bindingResult);
    }
}
