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
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jacms.aps.system.services.resource.ResourcesService;
import org.entando.entando.plugins.jacms.web.resource.model.ImageAssetDto;
import org.entando.entando.plugins.jacms.web.resource.validator.FileAssetValidator;
import org.entando.entando.plugins.jacms.web.resource.validator.ImageAssetValidator;
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

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ResourcesController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ResourcesService service;
    private final ImageAssetValidator imageValidator;
    private final FileAssetValidator fileValidator;

    @Autowired
    public ResourcesController(ResourcesService service, ImageAssetValidator imageValidator, FileAssetValidator fileValidator) {
        this.service = service;
        this.imageValidator = imageValidator;
        this.fileValidator = fileValidator;
    }

    @ApiOperation(value = "LIST ImageResources", nickname = "listImageResources", tags = {"resources-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @GetMapping("/plugins/cms/assets/images")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    public ResponseEntity<PagedRestResponse<ImageAssetDto>> listImageAssets(RestListRequest requestList) {
        logger.debug("REST request - list image resources");
        imageValidator.validateRestListRequest(requestList, ImageAssetDto.class);
        PagedMetadata<ImageAssetDto> result = service.listImageAssets(requestList);
        imageValidator.validateRestListResult(requestList, result);
        return ResponseEntity.ok(new PagedRestResponse<>(result));
    }

    @ApiOperation(value = "CREATE ImageResource", nickname = "createImageResource", tags = {"resources-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PostMapping("/plugins/cms/assets/images")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    public ResponseEntity<SimpleRestResponse<ImageAssetDto>> createImageAsset(@RequestPart("file") MultipartFile file, @RequestParam String group,
               @RequestParam String categories) {
        logger.debug("REST request - create new image resource");
        return ResponseEntity.ok(new SimpleRestResponse<>(
                service.createImageAsset(file, group, Arrays.stream(categories.split(","))
                        .map(String::trim).collect(Collectors.toList()))
        ));
    }

    @ApiOperation(value = "EDIT ImageResource", nickname = "editImageResource", tags = {"resources-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PostMapping("/plugins/cms/assets/images/{resourceId}")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    public ResponseEntity<SimpleRestResponse<ImageAssetDto>> editImageAsset(@PathVariable("resourceId") String resourceId,
            @RequestPart(value = "file", required = false) MultipartFile file, @RequestParam(required = false) String group,
            @RequestParam(required = false) String categories, @RequestParam(required = false) String description) {
        logger.debug("REST request - edit image resource with id {}", resourceId);
        return ResponseEntity.ok(new SimpleRestResponse<>(
                service.editImageAsset(resourceId, file, description, group, Optional.ofNullable(categories)
                    .map(c -> Arrays.stream(c.split(","))
                    .map(String::trim).collect(Collectors.toList()))
                    .orElse(Collections.emptyList()))
        ));
    }

    @ApiOperation(value = "DELETE ImageResource", nickname = "deleteImageResource", tags = {"resources-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @DeleteMapping("/plugins/cms/assets/images/{resourceId}")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    public ResponseEntity deleteImageAsset(@PathVariable("resourceId") String resourceId) {
        logger.debug("REST request - delete image resource with id {}", resourceId);
        service.deleteImageAsset(resourceId);
        return ResponseEntity.ok().build();
    }
}
