/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jacms.aps.system.services;

import static org.entando.entando.plugins.jacms.web.resource.ResourcesController.ERRCODE_RESOURCE_NOT_FOUND;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDtoBuilder;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDtoRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.entity.AbstractEntityTypeService;
import org.entando.entando.aps.system.services.entity.model.AttributeTypeDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeAttributeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypesStatusDto;
import org.entando.entando.plugins.jacms.aps.system.services.content.ContentService;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.entity.model.EntityTypeDtoRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class ContentTypeService extends AbstractEntityTypeService<Content, ContentTypeDto> {

    private static final String CONTENT_MODEL_MANAGER = "jacmsContentManager";

    private final ContentService contentService;

    @Override
    protected IDtoBuilder<Content, ContentTypeDto> getEntityTypeFullDtoBuilder(
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
        String code = contentTypeRequest.getCode();
        if (findOne(code).isPresent()) {
            return updateEntityType(CONTENT_MODEL_MANAGER, contentTypeRequest, bindingResult);
        } else {
            throw new ResourceNotFoundException(ERRCODE_RESOURCE_NOT_FOUND, "contentType", code);
        }
    }

    public Integer usage(String code) {
        return contentService.countContentsByType(code);
    }

    public PagedMetadata<String> findManyAttributes(RestListRequest requestList) {
        return getAttributeTypes(CONTENT_MODEL_MANAGER, requestList);
    }

    public AttributeTypeDto getAttributeType(String attributeCode) {
        return getAttributeType(CONTENT_MODEL_MANAGER, attributeCode);
    }

    public List<EntityTypeAttributeFullDto> getContentTypeAttributes(String contentTypeCode) {
        return getEntityAttributes(CONTENT_MODEL_MANAGER, contentTypeCode);
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

    public Map<String, Integer> reloadProfileTypesReferences(List<String> profileTypeCodes) {
        return reloadEntityTypesReferences(CONTENT_MODEL_MANAGER, profileTypeCodes);
    }

    @Override
    protected Content createEntityType(IEntityManager entityManager, EntityTypeDtoRequest dto,
            BindingResult bindingResult) throws Throwable {
        ContentTypeDtoRequest request = (ContentTypeDtoRequest) dto;
        Content result = super.createEntityType(entityManager, dto, bindingResult);
        result.setViewPage(request.getViewPage());
        result.setDefaultModel(request.getDefaultContentModel());
        result.setListModel(request.getDefaultContentModelList());

        return result;
    }
}
