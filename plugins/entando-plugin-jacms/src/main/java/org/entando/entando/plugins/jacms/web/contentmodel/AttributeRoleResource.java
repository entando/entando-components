package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeRoleDto;
import io.swagger.annotations.*;
import org.entando.entando.web.common.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

public interface AttributeRoleResource {

    @ApiOperation(value = "createRole", nickname = "createRoleUsingPOST", response = AttributeRoleDto.class, tags={ "attribute-role-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AttributeRoleDto.class),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @PostMapping
    ResponseEntity<AttributeRoleDto> create(@ApiParam(value = "attributeRole", required = true) @Valid @RequestBody AttributeRoleDto attributeRole) throws URISyntaxException;

    @ApiOperation(value = "deleteRole", nickname = "deleteRoleUsingDELETE", tags={ "attribute-role-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden") })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> delete(@ApiParam(value = "id", required = true) @PathVariable("id") Long id);


    @ApiOperation(value = "getAllRoles", nickname = "getAllRolesUsingGET", response = AttributeRoleDto.class, responseContainer = "List", tags={ "attribute-role-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AttributeRoleDto.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @GetMapping
    ResponseEntity<PagedMetadata<AttributeRoleDto>> list(RestListRequest listRequest);

    @ApiOperation(value = "getRole", nickname = "getRoleUsingGET", response = AttributeRoleDto.class, tags={ "attribute-role-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AttributeRoleDto.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @GetMapping(value = "/{id}")
    ResponseEntity<AttributeRoleDto> get(@ApiParam(value = "id", required = true) @PathVariable("id") Long id);


    @ApiOperation(value = "updateRole", nickname = "updateRoleUsingPUT", response = AttributeRoleDto.class, tags={ "attribute-role-resource-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AttributeRoleDto.class),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @PutMapping
    ResponseEntity<AttributeRoleDto> update(@ApiParam(value = "attributeRole", required = true) @Valid @RequestBody AttributeRoleDto attributeRole);
}
