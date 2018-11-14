package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface AttributeResource {

    @ApiOperation(value = "createAttribute", nickname = "createAttributeUsingPOST", notes = "", response = AttributeDto.class, tags={ "attribute-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AttributeDto.class),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @PostMapping
    ResponseEntity<AttributeDto> create(@ApiParam(value = "attribute", required = true) @Valid @RequestBody AttributeDto attribute);

    @ApiOperation(value = "deleteAttribute", nickname = "deleteAttributeUsingDELETE", notes = "", tags={ "attribute-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden") })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> delete(@ApiParam(value = "id", required = true) @PathVariable("id") Long id);


    @ApiOperation(value = "getAllAttributes", nickname = "getAllAttributesUsingGET", notes = "", response = AttributeDto.class, responseContainer = "List", tags={ "attribute-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AttributeDto.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @GetMapping
    ResponseEntity<List<AttributeDto>> list(@ApiParam(value = "") @Valid @RequestParam(value = "offset", required = false) Long offset, @ApiParam(value = "Page number of the requested page") @Valid @RequestParam(value = "page", required = false) Integer page, @ApiParam(value = "") @Valid @RequestParam(value = "pageNumber", required = false) Integer pageNumber, @ApiParam(value = "") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize, @ApiParam(value = "") @Valid @RequestParam(value = "paged", required = false) Boolean paged, @ApiParam(value = "Size of a page") @Valid @RequestParam(value = "size", required = false) Integer size, @ApiParam(value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.") @Valid @RequestParam(value = "sort", required = false) List<String> sort, @ApiParam(value = "") @Valid @RequestParam(value = "sort.sorted", required = false) Boolean sortSorted, @ApiParam(value = "") @Valid @RequestParam(value = "sort.unsorted", required = false) Boolean sortUnsorted, @ApiParam(value = "") @Valid @RequestParam(value = "unpaged", required = false) Boolean unpaged);


    @ApiOperation(value = "getAttribute", nickname = "getAttributeUsingGET", notes = "", response = AttributeDto.class, tags={ "attribute-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AttributeDto.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @GetMapping(value = "/{id}")
    ResponseEntity<AttributeDto> get(@ApiParam(value = "id", required = true) @PathVariable("id") Long id);


    @ApiOperation(value = "updateAttribute", nickname = "updateAttributeUsingPUT", notes = "", response = AttributeDto.class, tags={ "attribute-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AttributeDto.class),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @PutMapping
    ResponseEntity<AttributeDto> update(@ApiParam(value = "attribute", required = true) @Valid @RequestBody AttributeDto attribute);
}
