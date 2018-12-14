package org.entando.entando.plugins.jacms.aps.system.services;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.*;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.entity.AbstractEntityTypeService;
import org.entando.entando.aps.system.services.entity.model.*;
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

    public PagedMetadata<EntityTypeShortDto> findMany(RestListRequest listRequest) {
        return getShortEntityTypes(CONTENT_MODEL_MANAGER, listRequest);
    }

    public Optional<ContentTypeDto> findOne(String code) {
        return Optional.ofNullable(super.getFullEntityType(CONTENT_MODEL_MANAGER, code));
    }

    public ContentTypeDto update(ContentTypeDtoRequest contentTypeRequest, BindingResult bindingResult) {
        return updateEntityType(CONTENT_MODEL_MANAGER, contentTypeRequest, bindingResult);
    }

    public PagedMetadata<String> findManyAttributes(RestListRequest requestList) {
        return getAttributeTypes(CONTENT_MODEL_MANAGER, requestList);
    }

    public AttributeTypeDto getAttributeType(String attributeCode) {
        return getAttributeType(CONTENT_MODEL_MANAGER, attributeCode);
    }

    public EntityTypeAttributeFullDto getContentTypeAttribute(String contentTypeCode, String attributeCode) {
        return getEntityAttribute(CONTENT_MODEL_MANAGER, contentTypeCode, attributeCode);
    }

    public EntityTypeAttributeFullDto addContentTypeAttribute(
            String contentTypeCode,
            EntityTypeAttributeFullDto bodyRequest,
            BindingResult bindingResult) {

        return addEntityAttribute(CONTENT_MODEL_MANAGER, contentTypeCode, bodyRequest, bindingResult);
    }

    public EntityTypeAttributeFullDto updateContentTypeAttribute(
            String contentTypeCode,
            EntityTypeAttributeFullDto bodyRequest,
            BindingResult bindingResult) {

        return updateEntityAttribute(CONTENT_MODEL_MANAGER, contentTypeCode, bodyRequest, bindingResult);
    }

    public void deleteContentTypeAttribute(String contentTypeCode, String attributeCode) {
        deleteEntityAttribute(CONTENT_MODEL_MANAGER, contentTypeCode, attributeCode);
    }

    public void reloadContentTypeReferences(String contentTypeCode) {
        reloadEntityTypeReferences(CONTENT_MODEL_MANAGER, contentTypeCode);
    }

    public EntityTypesStatusDto getContentTypesRefreshStatus() {
        return getEntityTypesRefreshStatus(CONTENT_MODEL_MANAGER);
    }

    public void moveContentTypeAttributeUp(String contentTypeCode, String attributeCode) {
        moveEntityAttribute(CONTENT_MODEL_MANAGER, contentTypeCode, attributeCode, true);
    }

    public void moveContentTypeAttributeDown(String contentTypeCode, String attributeCode) {
        moveEntityAttribute(CONTENT_MODEL_MANAGER, contentTypeCode, attributeCode, false);
    }
}
