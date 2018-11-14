package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import io.swagger.annotations.*;
import org.entando.entando.web.common.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

public interface ContentTypeResource {

    @ApiOperation(value = "createContentType", nickname = "createContentTypeUsingPOST", notes = "", response = ContentTypeDto.class, tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ContentTypeDto.class),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden") })
    @PostMapping
    ResponseEntity<ContentTypeDto> create(@ApiParam(value = "contentType", required = true) @Valid @RequestBody ContentTypeDto contentType) throws URISyntaxException;

    @ApiOperation(value = "deleteContentType", nickname = "deleteContentTypeUsingDELETE", notes = "", tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden") })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> delete(@ApiParam(value = "id", required = true) @PathVariable("id") Long id);


    @ApiOperation(value = "getAllContentTypes", nickname = "getAllContentTypesUsingGET", notes = "", response = ContentTypeDto.class, responseContainer = "List", tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ContentTypeDto.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden") })
    @GetMapping
    ResponseEntity<PagedMetadata<ContentTypeDto>> list(RestListRequest listRequest);


    @ApiOperation(value = "getContentType", nickname = "getContentTypeUsingGET", notes = "", response = ContentTypeDto.class, tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ContentTypeDto.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @GetMapping(value = "/{id}")
    ResponseEntity<ContentTypeDto> get(@ApiParam(value = "id", required = true) @PathVariable("id") Long id);


    @ApiOperation(value = "updateContentType", nickname = "updateContentTypeUsingPUT", notes = "", response = ContentTypeDto.class, tags={ "content-type-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ContentTypeDto.class),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @PutMapping
    ResponseEntity<ContentTypeDto> update(@ApiParam(value = "contentType", required = true) @Valid @RequestBody ContentTypeDto contentType);
}
