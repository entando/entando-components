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
package org.entando.entando.plugins.jacms.web.resource;

import com.agiletec.aps.system.services.role.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.plugins.jacms.aps.system.services.resource.ResourcesService;
import org.entando.entando.plugins.jacms.web.resource.model.AssetDto;
import org.entando.entando.plugins.jacms.web.resource.model.ImageAssetDto;
import org.entando.entando.plugins.jacms.web.resource.validator.ResourcesValidator;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ResourcesController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ResourcesService service;
    private final ResourcesValidator resourceValidator;

    @Autowired
    public ResourcesController(ResourcesService service, ResourcesValidator resourceValidator) {
        this.service = service;
        this.resourceValidator = resourceValidator;
    }

    @ApiOperation(value = "LIST Resources", nickname = "listResources", tags = {"resources-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @GetMapping("/plugins/cms/assets")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    public ResponseEntity<PagedRestResponse<AssetDto>> listAssets(@RequestParam("type") String type, RestListRequest requestList) {
        logger.debug("REST request - list image resources");

        resourceValidator.validateRestListRequest(requestList, AssetDto.class);
        PagedMetadata<AssetDto> result = service.listAssets(getResourceType(type), requestList);
        resourceValidator.validateRestListResult(requestList, result);
        return ResponseEntity.ok(new PagedRestResponse<>(result));
    }

    @ApiOperation(value = "CREATE Resource", nickname = "createResource", tags = {"resources-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PostMapping("/plugins/cms/assets")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    public ResponseEntity<SimpleRestResponse<AssetDto>> createAsset(@RequestParam("type") String type, @RequestPart("file") MultipartFile file,
                @RequestParam String group, @RequestParam String categories) {
        logger.debug("REST request - create new resource");
        List<String> categoriesList = Arrays.stream(categories.split(","))
                .map(String::trim).collect(Collectors.toList());

        AssetDto result = service.createAsset(getResourceType(type), file, group, categoriesList);
        return ResponseEntity.ok(new SimpleRestResponse<>(result));
    }

    @ApiOperation(value = "EDIT Resource", nickname = "editResource", tags = {"resources-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PostMapping("/plugins/cms/assets/{resourceId}")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    public ResponseEntity<SimpleRestResponse<AssetDto>> editAsset(@PathVariable("resourceId") String resourceId,
            @RequestPart(value = "file", required = false) MultipartFile file, @RequestParam(required = false) String group,
            @RequestParam(required = false) String categories, @RequestParam(required = false) String description) {
        logger.debug("REST request - edit image resource with id {}", resourceId);

        List<String> categoriesList = Optional.ofNullable(categories)
                .map(c -> Arrays.stream(c.split(","))
                        .map(String::trim).collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        AssetDto result = service.editAsset(resourceId, file, description, group, categoriesList);
        return ResponseEntity.ok(new SimpleRestResponse<>(result));
    }

    @ApiOperation(value = "DELETE Resource", nickname = "deleteResource", tags = {"resources-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @DeleteMapping("/plugins/cms/assets/{resourceId}")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    public ResponseEntity deleteAsset(@PathVariable("resourceId") String resourceId) {
        logger.debug("REST request - delete resource with id {}", resourceId);
        service.deleteAsset(resourceId);
        return ResponseEntity.ok().build();
    }

    public String getResourceType(String type) {
        if ("image".equals(type)) {
            return "Image";
        } else if ("file".equals(type)) {
            return "Attach";
        } else {
            throw new RestServerError(String.format("Invalid resource type: %s", type), null);
        }
    }
}
