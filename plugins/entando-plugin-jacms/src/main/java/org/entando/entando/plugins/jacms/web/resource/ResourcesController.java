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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.entando.entando.plugins.jacms.aps.system.services.resource.ResourcesService;
import org.entando.entando.plugins.jacms.web.resource.model.ImageAssetDto;
import org.entando.entando.plugins.jacms.web.resource.validator.FileAssetValidator;
import org.entando.entando.plugins.jacms.web.resource.validator.ImageAssetValidator;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    //@RestAccessControl(permission = Permission.SUPERUSER)
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
    //@RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity createImageAsset(@RequestPart("file") MultipartFile file) {
        logger.debug("REST request - create new image resource");
        try {
            service.createImageAsset(file);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok().build();
    }
}
