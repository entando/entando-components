package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.*;
import io.swagger.annotations.*;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

@RequestMapping(value = ContentTypeResourceController.BASE_URL)
public interface ContentTypeResource {

    @ApiOperation(value = "createContentType", nickname = "createContentTypeUsingPOST", response = ContentTypeDto.class, tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ContentTypeDto.class),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden") })
    @PostMapping
    ResponseEntity<ContentTypeDto> create(@ApiParam(value = "contentType", required = true) @Valid @RequestBody ContentTypeDtoRequest contentType, BindingResult bindingResult) throws URISyntaxException;

    @ApiOperation(value = "deleteContentType", nickname = "deleteContentTypeUsingDELETE", tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden") })
    @DeleteMapping(value = "/{code}")
    ResponseEntity<Void> delete(@ApiParam(value = "code", required = true) @PathVariable("code") String code);


    @ApiOperation(value = "getAllContentTypes", nickname = "getAllContentTypesUsingGET", response = ContentTypeDto.class, responseContainer = "List", tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ContentTypeDto.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden") })
    @GetMapping
    @RestAccessControl(permission = Permission.SUPERUSER)
    ResponseEntity<PagedMetadata<org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto>> list(RestListRequest listRequest);


    @ApiOperation(value = "getContentType", nickname = "getContentTypeUsingGET", response = ContentTypeDto.class, tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ContentTypeDto.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @GetMapping(value = "/{code}")
    ResponseEntity<ContentTypeDto> get(@ApiParam(value = "code", required = true) @PathVariable("code") String id);


    @ApiOperation(value = "updateContentType", nickname = "updateContentTypeUsingPUT", response = ContentTypeDto.class, tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ContentTypeDto.class),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @PutMapping
    ResponseEntity<ContentTypeDto> update(@ApiParam(value = "contentType", required = true) @Valid @RequestBody ContentTypeDtoRequest contentType, BindingResult bindingResult);
}
