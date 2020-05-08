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
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.entando.entando.plugins.jacms.aps.system.services.resource.ResourcesService;
import org.entando.entando.plugins.jacms.web.resource.model.AssetDto;
import org.entando.entando.plugins.jacms.web.resource.request.CreateResourceRequest;
import org.entando.entando.plugins.jacms.web.resource.request.UpdateResourceRequest;
import org.entando.entando.plugins.jacms.web.resource.util.HttpSessionHelper;
import org.entando.entando.plugins.jacms.web.resource.validator.ResourcesValidator;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class ResourcesController {
    public static final String ERRCODE_RESOURCE_NOT_FOUND = "1";
    public static final String ERRCODE_CATEGORY_NOT_FOUND = "2";
    public static final String ERRCODE_GROUP_NOT_FOUND = "3";
    public static final String ERRCODE_INVALID_FILE_TYPE = "4";
    public static final String ERRCODE_INVALID_RESOURCE_TYPE = "5";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @NonNull private final ResourcesService service;
    @NonNull private final ResourcesValidator resourceValidator;
    @NonNull private final HttpSession httpSession;

    @ApiOperation(value = "LIST Resources", nickname = "listResources", tags = {"resources-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @GetMapping("/plugins/cms/assets")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    public ResponseEntity<PagedRestResponse<AssetDto>> listAssets(@RequestParam(value = "type", required = false) String type,
            RestListRequest requestList) {
        logger.debug("REST request - list image resources");

        resourceValidator.validateRestListRequest(requestList, AssetDto.class);
        PagedMetadata<AssetDto> result = service.listAssets(type, requestList);
        resourceValidator.validateRestListResult(requestList, result);
        return ResponseEntity.ok(new PagedRestResponse<>(result));
    }

    @ApiOperation(value = "CREATE Resource", nickname = "createResource", tags = {"resources-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PostMapping(value = "/plugins/cms/assets")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    public ResponseEntity<SimpleRestResponse<AssetDto>> createAsset(
            @RequestParam(value = "metadata") String request,
            @RequestParam(value = "file") MultipartFile file) throws JsonProcessingException {
        logger.debug("REST request - create new resource");

        CreateResourceRequest resourceRequest = new ObjectMapper().readValue(request, CreateResourceRequest.class);

        List<String> categoriesList = Optional.ofNullable(resourceRequest.getCategories()).orElse(Collections.emptyList())
                .stream()
                .map(String::trim)
                .filter(c -> c.length() > 0)
                .collect(Collectors.toList());

        AssetDto result = service.createAsset(resourceRequest.getType(), file, resourceRequest.getGroup(), categoriesList, HttpSessionHelper.extractCurrentUser(httpSession));
        return ResponseEntity.ok(new SimpleRestResponse<>(result));
    }

    @ApiOperation(value = "CLONE Resource", nickname = "cloneResource", tags = {"resources-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PostMapping("/plugins/cms/assets/{resourceId}/clone")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    public ResponseEntity<SimpleRestResponse<AssetDto>> cloneAsset(@PathVariable("resourceId") String resourceId) {
        logger.debug("REST request - clone resource");
        AssetDto result = service.cloneAsset(resourceId);
        return ResponseEntity.ok(new SimpleRestResponse<>(result));
    }

    @ApiOperation(value = "EDIT Resource", nickname = "editResource", tags = {"resources-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PostMapping(value = "/plugins/cms/assets/{resourceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    public ResponseEntity<SimpleRestResponse<AssetDto>> editAsset(@PathVariable("resourceId") String resourceId,
            @RequestParam(value = "metadata") String request,
            @RequestParam(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        logger.debug("REST request - edit image resource with id {}", resourceId);

        UpdateResourceRequest resourceRequest = new ObjectMapper().readValue(request, UpdateResourceRequest.class);

        List<String> categoriesList = Optional.ofNullable(resourceRequest.getCategories()).orElse(Collections.emptyList())
                .stream()
                .map(String::trim)
                .filter(c -> c.length() > 0)
                .collect(Collectors.toList());

        AssetDto result = service.editAsset(resourceId, file, resourceRequest.getDescription(), categoriesList);
        return ResponseEntity.ok(new SimpleRestResponse<>(result));
    }

    @ApiOperation(value = "DELETE Resource", nickname = "deleteResource", tags = {"resources-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @DeleteMapping("/plugins/cms/assets/{resourceId}")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    public ResponseEntity<SimpleRestResponse<Map>> deleteAsset(@PathVariable("resourceId") String resourceId) {
        logger.debug("REST request - delete resource with id {}", resourceId);
        service.deleteAsset(resourceId);
        return ResponseEntity.ok(new SimpleRestResponse<>(new HashMap()));
    }

}
