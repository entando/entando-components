/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version v3.0 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jpversioning.web.resource;

import com.agiletec.aps.system.services.role.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.entando.entando.plugins.jpversioning.web.resource.model.ResourceDTO;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags = {"resource-versioning-controller"})
@ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 409, message = "Conflict"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 500, message = "Server Error")
})
public interface IResourceVersioning {

    @ApiOperation(value = "LIST trashed resources", nickname = "listTrashedResources", tags = {
            "resource-versioning-controller"})
    @GetMapping("")
    @RestAccessControl(permission = Permission.MANAGE_RESOURCES)
    ResponseEntity<PagedRestResponse<ResourceDTO>> listTrashedResources(@RequestParam(value = "resourceTypeCode") String resourceTypeCode,
            RestListRequest requestList);

    @ApiOperation(value = "POST resource to recover", nickname = "recoverTrashedResource", tags = {
            "resource-versioning-controller"})
    @PostMapping("/{resourceId}/recover")
    @RestAccessControl(permission = Permission.MANAGE_RESOURCES)
    ResponseEntity<ResourceDTO> recoverResource(@PathVariable(value = "resourceId") String resourceId);

    @ApiOperation(value = "DELETE trashed resource", nickname = "deleteTrashedResource", tags = {
            "resource-versioning-controller"})
    @DeleteMapping("/{resourceId}")
    @RestAccessControl(permission = Permission.MANAGE_RESOURCES)
    ResponseEntity<ResourceDTO> deleteTrashedResource(@PathVariable(value = "resourceId") String resourceId);

    @ApiOperation(value = "GET trashed resource", nickname = "deleteTrashedResource", tags = {
            "resource-versioning-controller"})
    @GetMapping(value = "/{resourceId}/{size}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @RestAccessControl(permission = Permission.MANAGE_RESOURCES)
    ResponseEntity getTrashedResource(@PathVariable(value = "resourceId") String resourceId,
            @PathVariable(value = "size") Integer size);
}
