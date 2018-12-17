package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.*;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.entity.model.*;
import org.entando.entando.plugins.jacms.aps.system.services.ContentTypeService;
import org.entando.entando.plugins.jacms.web.contentmodel.validator.ContentTypeValidator;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.*;
import org.entando.entando.web.common.model.*;
import org.entando.entando.web.userprofile.validator.ProfileTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.*;
import java.util.*;

@RestController
public class ContentTypeResourceController implements ContentTypeResource {

    private static final String CONTENT_TYPE_CODE = "contentTypeCode";
    private static final String ATTRIBUTE_CODE = "attributeCode";
    private static final String MOVEMENT = "movement";

    private final ContentTypeService service;
    private final ContentTypeValidator validator;

    @Autowired
    public ContentTypeResourceController(ContentTypeService service, ContentTypeValidator validator) {
        this.service = service;
        this.validator = validator;
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<ContentTypeDto>> create(
            @Valid @RequestBody ContentTypeDtoRequest contentType,
            BindingResult bindingResult)
            throws URISyntaxException {

        validator.validate(contentType, bindingResult);
        if(bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        ContentTypeDto result = service.create(contentType, bindingResult);

        return ResponseEntity.created(
                new URI("/plugins/contentTypes/" + result.getCode()))
                .body(new SimpleRestResponse<>(result));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<Void> delete(@PathVariable("code") String code) {

        service.delete(code);
        return ResponseEntity.ok().build();
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<PagedRestResponse<EntityTypeShortDto>> list(RestListRequest listRequest) {
        validator.validateRestListRequest(listRequest, ContentTypeDto.class);
        PagedMetadata<EntityTypeShortDto> result = service.findMany(listRequest);
        validator.validateRestListResult(listRequest, result);
        return ResponseEntity.ok(new PagedRestResponse<>(result));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<ContentTypeDto>> get(
            @PathVariable("code") String code) {

        Optional<ContentTypeDto> maybeContentType = service.findOne(code);

        return maybeContentType.map(contentTypeDto ->
                                        ResponseEntity.ok(new SimpleRestResponse<>(contentTypeDto)))
                               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<ContentTypeDto>> update(
            @Valid @RequestBody ContentTypeDtoRequest contentType,
            BindingResult bindingResult) {

        ContentTypeDto updated = service.update(contentType, bindingResult);
        return ResponseEntity.ok(new SimpleRestResponse<>(updated));
    }


    @Override
    public ResponseEntity<PagedRestResponse<String>> getContentTypeAttributeTypes(
            RestListRequest requestList) {
        validator.validateRestListRequest(requestList, AttributeTypeDto.class);
        PagedMetadata<String> attributes = service.findManyAttributes(requestList);
        validator.validateRestListResult(requestList, attributes);
        return ResponseEntity.ok(new PagedRestResponse<>(attributes));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<AttributeTypeDto>> getContentTypeAttributeType(
            @PathVariable String attributeTypeCode) {

        AttributeTypeDto attributeTypeDto = service.getAttributeType(attributeTypeCode);

        return ResponseEntity.ok(new SimpleRestResponse<>(attributeTypeDto));
    }

    @Override
    public ResponseEntity<RestResponse<EntityTypeAttributeFullDto, Map>> getContentTypeAttribute(
            @PathVariable String contentTypeCode,
            @PathVariable String attributeCode) {

        EntityTypeAttributeFullDto dto = service.getContentTypeAttribute(contentTypeCode, attributeCode);

        Map<String, String> metadata = ImmutableMap.of(
                CONTENT_TYPE_CODE, contentTypeCode
        );

        return ResponseEntity.ok(new RestResponse<>(dto, metadata));
    }

    @Override
    public ResponseEntity<RestResponse<EntityTypeAttributeFullDto, Map>> addContentTypeAttribute(
            @PathVariable String contentTypeCode,
            @Valid @RequestBody EntityTypeAttributeFullDto bodyRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        EntityTypeAttributeFullDto dto = service.addContentTypeAttribute(contentTypeCode, bodyRequest, bindingResult);

        Map<String, String> metadata = ImmutableMap.of(
                CONTENT_TYPE_CODE, contentTypeCode
        );
        return new ResponseEntity<>(new RestResponse<>(dto, metadata), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RestResponse<EntityTypeAttributeFullDto, Map>> updateContentTypeAttribute(
            @PathVariable String contentTypeCode,
            @PathVariable String attributeCode,
            @Valid @RequestBody EntityTypeAttributeFullDto bodyRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        } else if (!StringUtils.equals(attributeCode, bodyRequest.getCode())) {
            bindingResult.rejectValue("code", ProfileTypeValidator.ERRCODE_URINAME_MISMATCH, new String[]{attributeCode, bodyRequest.getCode()}, "entityType.attribute.code.mismatch");
            throw new ValidationConflictException(bindingResult);
        }

        EntityTypeAttributeFullDto dto = service.updateContentTypeAttribute(contentTypeCode, bodyRequest, bindingResult);

        Map<String, String> metadata = ImmutableMap.of(
                CONTENT_TYPE_CODE, contentTypeCode
        );

        return new ResponseEntity<>(new RestResponse<>(dto, metadata), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SimpleRestResponse<Map>> deleteContentTypeAttribute(
            @PathVariable String contentTypeCode,
            @PathVariable String attributeCode) {

        service.deleteContentTypeAttribute(contentTypeCode, attributeCode);

        Map<String, String> metadata = ImmutableMap.of(
                CONTENT_TYPE_CODE, contentTypeCode,
                ATTRIBUTE_CODE, attributeCode
        );

        return new ResponseEntity<>(new SimpleRestResponse<>(metadata), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SimpleRestResponse<Map>> reloadReferences(@PathVariable String contentTypeCode) {

        service.reloadContentTypeReferences(contentTypeCode);

        Map<String, String> result = ImmutableMap.of(
                "status", "success",
                CONTENT_TYPE_CODE, contentTypeCode
        );

        return ResponseEntity.ok(new SimpleRestResponse<>(result));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<Map>> reloadReferences(
            @Valid @RequestBody ContentTypeRefreshRequest bodyRequest, BindingResult bindingResult) {

        Map<String, Integer> status = service.reloadProfileTypesReferences(bodyRequest.getProfileTypeCodes());

        Map<String, Object> result = ImmutableMap.of(
                "result", "success",
                "contentTypeCodes", status
        );

        return ResponseEntity.ok(new SimpleRestResponse<>(result));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<EntityTypesStatusDto>> extractStatus() {
        EntityTypesStatusDto status = service.getContentTypesRefreshStatus();
        return ResponseEntity.ok(new SimpleRestResponse<>(status));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<Map>> moveContentTypeAttributeUp(
            @PathVariable String contentTypeCode,
            @PathVariable String attributeCode) {

        service.moveContentTypeAttributeUp(contentTypeCode, attributeCode);

        Map<String, String> metadata = ImmutableMap.of(
                CONTENT_TYPE_CODE, contentTypeCode,
                ATTRIBUTE_CODE, attributeCode,
                MOVEMENT, "UP"
        );

        return ResponseEntity.ok(new SimpleRestResponse<>(metadata));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<Map>> moveContentTypeAttributeDown(
            @PathVariable String contentTypeCode,
            @PathVariable String attributeCode) {
        service.moveContentTypeAttributeDown(contentTypeCode, attributeCode);

        Map<String, String> metadata = ImmutableMap.of(
                CONTENT_TYPE_CODE, contentTypeCode,
                ATTRIBUTE_CODE, attributeCode,
                MOVEMENT, "DOWN"
        );

        return ResponseEntity.ok(new SimpleRestResponse<>(metadata));
    }
}
