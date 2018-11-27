package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeDto;
import io.swagger.annotations.*;
import org.entando.entando.web.common.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

public interface AttributeResource {

    @ApiOperation(value = "createAttribute", nickname = "createAttributeUsingPOST", response = AttributeDto.class, tags={ "attribute-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AttributeDto.class),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @PostMapping
    ResponseEntity<AttributeDto> create(@ApiParam(value = "attribute", required = true) @Valid @RequestBody AttributeDto attribute) throws URISyntaxException;

    @ApiOperation(value = "deleteAttribute", nickname = "deleteAttributeUsingDELETE", tags={ "attribute-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden") })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> delete(@ApiParam(value = "id", required = true) @PathVariable("id") Long id);


    @ApiOperation(value = "getAllAttributes", nickname = "getAllAttributesUsingGET", response = AttributeDto.class, responseContainer = "List", tags={ "attribute-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AttributeDto.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @GetMapping
    ResponseEntity<PagedMetadata<AttributeDto>> list(RestListRequest listRequest);


    @ApiOperation(value = "getAttribute", nickname = "getAttributeUsingGET", response = AttributeDto.class, tags={ "attribute-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AttributeDto.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @GetMapping(value = "/{id}")
    ResponseEntity<AttributeDto> get(@ApiParam(value = "id", required = true) @PathVariable("id") Long id);


    @ApiOperation(value = "updateAttribute", nickname = "updateAttributeUsingPUT", response = AttributeDto.class, tags={ "attribute-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AttributeDto.class),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @PutMapping
    ResponseEntity<AttributeDto> update(@ApiParam(value = "attribute", required = true) @Valid @RequestBody AttributeDto attribute);
}
