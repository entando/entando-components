package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.*;
import io.swagger.annotations.*;
import org.entando.entando.aps.system.services.entity.model.*;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Map;

public interface ContentTypeResource {


    @ApiOperation(
            value = "createContentType",
            nickname = "createContentTypeUsingPOST",
            response = ContentTypeDto.class,
            tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ContentTypeDto.class),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden") })
    @PostMapping("/plugins/cms/contentTypes")
    ResponseEntity<SimpleRestResponse<ContentTypeDto>> create(
            @ApiParam(value = "contentType", required = true) @Valid @RequestBody ContentTypeDtoRequest contentType,
            BindingResult bindingResult
    ) throws URISyntaxException;


    @ApiOperation(
            value = "deleteContentType",
            nickname = "deleteContentTypeUsingDELETE",
            tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden") })
    @DeleteMapping("/plugins/cms/contentTypes/{code}")
    ResponseEntity<Void> delete(@ApiParam(value = "code", required = true) @PathVariable("code") String code);


    @ApiOperation(
            value = "getAllContentTypes",
            nickname = "getAllContentTypesUsingGET",
            response = ContentTypeDto.class,
            responseContainer = "List",
            tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ContentTypeDto.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden") })
    @GetMapping(path = "/plugins/cms/contentTypes")
    @RestAccessControl(permission = Permission.SUPERUSER)
    ResponseEntity<PagedRestResponse<EntityTypeShortDto>> list(RestListRequest listRequest);


    @ApiOperation(
            value = "getContentType",
            nickname = "getContentTypeUsingGET",
            response = ContentTypeDto.class,
            tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ContentTypeDto.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @GetMapping("/plugins/cms/contentTypes/{code}")
    ResponseEntity<SimpleRestResponse<ContentTypeDto>> get(@ApiParam(value = "code", required = true) @PathVariable("code") String id);


    @ApiOperation(
            value = "updateContentType",
            nickname = "updateContentTypeUsingPUT",
            response = ContentTypeDto.class,
            tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ContentTypeDto.class),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @PutMapping("/plugins/cms/contentTypes")
    ResponseEntity<SimpleRestResponse<ContentTypeDto>> update(
            @ApiParam(value = "contentType", required = true) @Valid @RequestBody ContentTypeDtoRequest contentType,
            BindingResult bindingResult);


    @RestAccessControl(permission = Permission.SUPERUSER)
    @GetMapping("/plugins/cms/contentTypeAttributes")
    ResponseEntity<PagedRestResponse<String>> getContentTypeAttributeTypes(RestListRequest requestList);


    @RestAccessControl(permission = Permission.SUPERUSER)
    @GetMapping("/plugins/cms/contentTypeAttributes/{attributeTypeCode}")
    ResponseEntity<SimpleRestResponse<AttributeTypeDto>> getContentTypeAttributeType(
            @PathVariable String attributeTypeCode);


    @RestAccessControl(permission = Permission.SUPERUSER)
    @GetMapping("/plugins/cms/contentTypes/{contentTypeCode}/attributes/{attributeCode}")
    ResponseEntity<RestResponse<EntityTypeAttributeFullDto, Map>> getContentTypeAttribute(
            @PathVariable String contentTypeCode,
            @PathVariable String attributeCode);


    @RestAccessControl(permission = Permission.SUPERUSER)
    @PostMapping("/plugins/cms/contentTypes/{contentTypeCode}/attributes")
    ResponseEntity<RestResponse<EntityTypeAttributeFullDto, Map>> addContentTypeAttribute(
            @PathVariable String contentTypeCode,
            @Valid @RequestBody EntityTypeAttributeFullDto bodyRequest,
            BindingResult bindingResult);


    @RestAccessControl(permission = Permission.SUPERUSER)
    @PutMapping("/plugins/cms/contentTypes/{contentTypeCode}/attributes/{attributeCode}")
    ResponseEntity<RestResponse<EntityTypeAttributeFullDto, Map>> updateContentTypeAttribute(
            @PathVariable String contentTypeCode,
            @PathVariable String attributeCode,
            @Valid @RequestBody EntityTypeAttributeFullDto bodyRequest,
            BindingResult bindingResult);


    @RestAccessControl(permission = Permission.SUPERUSER)
    @DeleteMapping("/plugins/cms/contentTypes/{contentTypeCode}/attributes/{attributeCode}")
    ResponseEntity<SimpleRestResponse<Map>> deleteContentTypeAttribute(
            @PathVariable String contentTypeCode,
            @PathVariable String attributeCode);


    @RestAccessControl(permission = Permission.SUPERUSER)
    @PostMapping("/plugins/cms/contentTypes/refresh/{contentTypeCode}")
    ResponseEntity<SimpleRestResponse<Map>> reloadReferences(@PathVariable String contentTypeCode);

//    @RestAccessControl(permission = Permission.SUPERUSER)
//    @PostMapping("/plugins/cms/contentTypesStatus")
//    ResponseEntity<SimpleRestResponse<Map>> reloadReferences(
//            @Valid @RequestBody ContentTypeRefreshRequest bodyRequest,
//            BindingResult bindingResult);

    @RestAccessControl(permission = Permission.SUPERUSER)
    @GetMapping(value = "/plugins/cms/contentTypesStatus")
    ResponseEntity<SimpleRestResponse<EntityTypesStatusDto>> extractStatus();


    @RestAccessControl(permission = Permission.SUPERUSER)
    @PutMapping("/plugins/cms/contentTypes/{contentTypeCode}/attributes/{attributeCode}/moveUp")
    ResponseEntity<SimpleRestResponse<Map>> moveContentTypeAttributeUp(
            @PathVariable String contentTypeCode,
            @PathVariable String attributeCode);


    @RestAccessControl(permission = Permission.SUPERUSER)
    @PutMapping("/plugins/cms/contentTypes/{contentTypeCode}/attributes/{attributeCode}/moveDown")
    ResponseEntity<SimpleRestResponse<Map>> moveContentTypeAttributeDown(
            @PathVariable String contentTypeCode,
            @PathVariable String attributeCode);
}
