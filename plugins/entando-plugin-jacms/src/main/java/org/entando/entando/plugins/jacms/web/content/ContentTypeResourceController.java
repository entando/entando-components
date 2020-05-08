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
package org.entando.entando.plugins.jacms.web.content;

import static org.entando.entando.web.entity.validator.AbstractEntityTypeValidator.ERRCODE_URINAME_MISMATCH;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDtoRequest;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeRefreshRequest;
import com.google.common.collect.ImmutableMap;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.entity.model.AttributeTypeDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeAttributeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypesStatusDto;
import org.entando.entando.plugins.jacms.aps.system.services.ContentTypeService;
import org.entando.entando.plugins.jacms.web.content.validator.ContentTypeValidator;
import org.entando.entando.plugins.jacms.web.content.validator.RestContentListRequest;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.entando.entando.web.component.ComponentUsage;
import org.entando.entando.web.component.ComponentUsageEntity;
import org.entando.entando.web.page.model.PageSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContentTypeResourceController implements ContentTypeResource {
    public static final String COMPONENT_ID = "contentType";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String CONTENT_TYPE_CODE = "contentTypeCode";
    private static final String ATTRIBUTE_CODE = "attributeCode";
    private static final String MOVEMENT = "movement";

    private final ContentTypeService service;
    private final ContentTypeValidator validator;

    @Autowired
    public ContentTypeResourceController(ContentTypeService service,
            ContentTypeValidator validator) {
        this.service = service;
        this.validator = validator;
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<ContentTypeDto>> create(
            @Valid @RequestBody ContentTypeDtoRequest contentType, BindingResult bindingResult)
            throws URISyntaxException {
        logger.debug("REST request - create content type: {}", contentType);
        validateCreate(contentType, bindingResult);
        ContentTypeDto result = service.create(contentType, bindingResult);
        return ResponseEntity.created(
                new URI("/plugins/contentTypes/" + result.getCode()))
                .body(new SimpleRestResponse<>(result));
    }

    private void validateCreate(ContentTypeDtoRequest contentType, BindingResult bindingResult) {
        validator.validate(contentType, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<Map>> delete(@PathVariable("code") String code) {
        logger.debug("REST request - delete content type {}", code);
        service.delete(code);

        Map<String, String> metadata = ImmutableMap.of(
                CONTENT_TYPE_CODE, code
        );
        return ResponseEntity.ok(new SimpleRestResponse<>(metadata));
    }

    @Override
    public ResponseEntity<PagedRestResponse<EntityTypeShortDto>> list(RestListRequest listRequest) {
        logger.debug("REST request - get all content types {}", listRequest);

        validator.validateRestListRequest(listRequest, ContentTypeDto.class);
        PagedMetadata<EntityTypeShortDto> result = service.findMany(listRequest);
        validator.validateRestListResult(listRequest, result);
        return ResponseEntity.ok(new PagedRestResponse<>(result));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<ContentTypeDto>> get(@PathVariable("code") String code) {
        logger.debug("REST request - get content type {}", code);
        Optional<ContentTypeDto> maybeContentType = service.findOne(code);
        return maybeContentType.map(contentTypeDto
                -> ResponseEntity.ok(new SimpleRestResponse<>(contentTypeDto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<SimpleRestResponse<ComponentUsage>> usage(@PathVariable("code") String code) {
        logger.trace("get {} usage by code {}", COMPONENT_ID, code);
        ComponentUsage usage = ComponentUsage.builder()
                .type(COMPONENT_ID)
                .code(code)
                .usage(service.getComponentUsage(code))
                .build();

        return new ResponseEntity<>(new SimpleRestResponse<>(usage), HttpStatus.OK);
    }


    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<PagedRestResponse<ComponentUsageEntity>> getComponentUsageDetails(@PathVariable String code, RestListRequest requestList) {

        logger.trace("get {} usage details by code {}", COMPONENT_ID, code);

        validator.validateRestListRequest(requestList, ContentDto.class);

        PagedMetadata<ComponentUsageEntity> result = this.service.getComponentUsageDetails(code, requestList);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }


    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<ContentTypeDto>> update(
            @Valid @RequestBody ContentTypeDtoRequest contentType, BindingResult bindingResult) {
        logger.debug("REST request - update content type {}", contentType);
        ContentTypeDto updated = service.update(contentType, bindingResult);
        return ResponseEntity.ok(new SimpleRestResponse<>(updated));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<PagedRestResponse<String>> getContentTypeAttributeTypes(
            RestListRequest requestList) {
        logger.debug("REST request - list all content type attributes {}", requestList);
        validator.validateRestListRequest(requestList, AttributeTypeDto.class);
        PagedMetadata<String> attributes = service.findManyAttributes(requestList);
        validator.validateRestListResult(requestList, attributes);
        return ResponseEntity.ok(new PagedRestResponse<>(attributes));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<AttributeTypeDto>> getContentTypeAttribute(
            @PathVariable String attributeCode) {
        logger.debug("REST request - get content type attributes {}", attributeCode);
        AttributeTypeDto attributeTypeDto = service.getAttributeType(attributeCode);
        return ResponseEntity.ok(new SimpleRestResponse<>(attributeTypeDto));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<RestResponse<List<EntityTypeAttributeFullDto>, Map>> getContentTypeAttributes(
            @PathVariable String contentTypeCode) {
        logger.debug("REST request - get content type {} attributes", contentTypeCode);
        List<EntityTypeAttributeFullDto> dtos = service.getContentTypeAttributes(contentTypeCode);
        Map<String, String> metadata = ImmutableMap.of(CONTENT_TYPE_CODE, contentTypeCode);
        return ResponseEntity.ok(new RestResponse<>(dtos, metadata));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<RestResponse<EntityTypeAttributeFullDto, Map>> getContentTypeAttribute(
            @PathVariable String contentTypeCode, @PathVariable String attributeCode) {
        logger.debug("REST request - get content type {} attribute {}",
                contentTypeCode, attributeCode);
        EntityTypeAttributeFullDto dto = service.getContentTypeAttribute(
                contentTypeCode, attributeCode);
        Map<String, String> metadata = ImmutableMap.of(CONTENT_TYPE_CODE, contentTypeCode);
        return ResponseEntity.ok(new RestResponse<>(dto, metadata));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<RestResponse<EntityTypeAttributeFullDto, Map>> addContentTypeAttribute(
            @PathVariable String contentTypeCode,
            @Valid @RequestBody EntityTypeAttributeFullDto attribute,
            BindingResult bindingResult) {
        logger.debug("REST request - create content type {} attribute {}",
                contentTypeCode, attribute);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        EntityTypeAttributeFullDto dto = service.addContentTypeAttribute(
                contentTypeCode, attribute, bindingResult);
        Map<String, String> metadata = ImmutableMap.of(CONTENT_TYPE_CODE, contentTypeCode);
        return new ResponseEntity<>(new RestResponse<>(dto, metadata), HttpStatus.OK);
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<RestResponse<EntityTypeAttributeFullDto, Map>> updateContentTypeAttribute(
            @PathVariable String contentTypeCode,
            @PathVariable String attributeCode,
            @Valid @RequestBody EntityTypeAttributeFullDto attribute,
            BindingResult bindingResult) {
        logger.debug("REST request - update content type {} attribute {} with {}",
                contentTypeCode, attributeCode, attribute);
        validateUpdate(attributeCode, attribute, bindingResult);
        EntityTypeAttributeFullDto dto = service.updateContentTypeAttribute(
                contentTypeCode, attribute, bindingResult);
        Map<String, String> metadata = ImmutableMap.of(
                CONTENT_TYPE_CODE, contentTypeCode
        );
        return new ResponseEntity<>(new RestResponse<>(dto, metadata), HttpStatus.OK);
    }

    private void validateUpdate(@PathVariable String attributeCode,
            @RequestBody @Valid EntityTypeAttributeFullDto attribute,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        if (!StringUtils.equals(attributeCode, attribute.getCode())) {
            bindingResult.rejectValue("code", ERRCODE_URINAME_MISMATCH,
                    new String[]{attributeCode, attribute.getCode()},
                    "entityType.attribute.code.mismatch");
            throw new ValidationConflictException(bindingResult);
        }
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<Map>> deleteContentTypeAttribute(
            @PathVariable String contentTypeCode, @PathVariable String attributeCode) {
        logger.debug("REST request - delete content type {} attribute {}",
                contentTypeCode, attributeCode);
        service.deleteContentTypeAttribute(contentTypeCode, attributeCode);
        Map<String, String> metadata = ImmutableMap.of(
                CONTENT_TYPE_CODE, contentTypeCode,
                ATTRIBUTE_CODE, attributeCode
        );
        return new ResponseEntity<>(new SimpleRestResponse<>(metadata), HttpStatus.OK);
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<Map>> reloadReferences(
            @PathVariable String contentTypeCode) {
        logger.debug("REST request - reload content type references {}", contentTypeCode);
        service.reloadContentTypeReferences(contentTypeCode);
        Map<String, String> result = ImmutableMap.of(
                "status", "success",
                CONTENT_TYPE_CODE, contentTypeCode
        );
        return ResponseEntity.ok(new SimpleRestResponse<>(result));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<Map>> reloadReferences(
            @Valid @RequestBody ContentTypeRefreshRequest refreshRequest,
            BindingResult bindingResult) {
        logger.debug("REST request - reload content type references {}", refreshRequest);
        Map<String, Integer> status = service.reloadProfileTypesReferences(
                refreshRequest.getProfileTypeCodes());
        Map<String, Object> result = ImmutableMap.of(
                "result", "success",
                "contentTypeCodes", status
        );
        return ResponseEntity.ok(new SimpleRestResponse<>(result));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<EntityTypesStatusDto>> getStatus() {
        logger.debug("REST request - get content types status");
        EntityTypesStatusDto status = service.getContentTypesRefreshStatus();
        return ResponseEntity.ok(new SimpleRestResponse<>(status));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<Map>> moveContentTypeAttributeUp(
            @PathVariable String contentTypeCode, @PathVariable String attributeCode) {
        logger.debug("REST request - move up content type {} attribute {}",
                contentTypeCode, attributeCode);
        service.moveContentTypeAttributeUp(contentTypeCode, attributeCode);
        Map<String, String> metadata = ImmutableMap.of(
                CONTENT_TYPE_CODE, contentTypeCode,
                ATTRIBUTE_CODE, attributeCode,
                MOVEMENT, "UP"
        );
        return ResponseEntity.ok(new SimpleRestResponse<>(metadata));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<Map>> moveContentTypeAttributeDown(
            @PathVariable String contentTypeCode, @PathVariable String attributeCode) {
        logger.debug("REST request - move down content type {} attribute {}",
                contentTypeCode, attributeCode);
        service.moveContentTypeAttributeDown(contentTypeCode, attributeCode);
        Map<String, String> metadata = ImmutableMap.of(
                CONTENT_TYPE_CODE, contentTypeCode,
                ATTRIBUTE_CODE, attributeCode,
                MOVEMENT, "DOWN"
        );
        return ResponseEntity.ok(new SimpleRestResponse<>(metadata));
    }
}
