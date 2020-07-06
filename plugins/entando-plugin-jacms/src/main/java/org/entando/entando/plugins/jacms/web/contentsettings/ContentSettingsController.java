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
package org.entando.entando.plugins.jacms.web.contentsettings;

import com.agiletec.aps.system.services.role.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.entando.entando.plugins.jacms.aps.system.services.contentsettings.ContentSettingsService;
import org.entando.entando.plugins.jacms.web.contentsettings.model.ContentSettingsCropRatioRequest;
import org.entando.entando.plugins.jacms.web.contentsettings.model.ContentSettingsDto;
import org.entando.entando.plugins.jacms.web.contentsettings.model.ContentSettingsEditorRequest;
import org.entando.entando.plugins.jacms.web.contentsettings.model.CreateContentSettingsMetadataRequest;
import org.entando.entando.plugins.jacms.web.contentsettings.model.EditContentSettingsMetadataRequest;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentSettingsController {
    public static final String ERRCODE_INVALID_EDITOR = "1";
    public static final String ERRCODE_INVALID_METADATA = "2";
    public static final String ERRCODE_CONFLICT_METADATA = "3";
    public static final String ERRCODE_INVALID_CROP_RATIO = "4";
    public static final String ERRCODE_CONFLICT_CROP_RATIO = "5";
    public static final String ERRCODE_NOT_FOUND_CROP_RATIO = "6";
    public static final String ERRCODE_NOT_FOUND_METADATA = "7";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ContentSettingsService service;

    @Autowired
    public ContentSettingsController(ContentSettingsService service) {
        this.service = service;
    }

    @ApiOperation(value = "GET ContentSettings", nickname = "getContentSettings", tags = {"content-settings-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @GetMapping("/plugins/cms/contentSettings")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    public ResponseEntity<SimpleRestResponse<ContentSettingsDto>> getContentSettings() {
        logger.debug("REST request - get content settings");
        return ResponseEntity.ok(new SimpleRestResponse<>(
                service.getContentSettings()
        ));
    }


    @ApiOperation(value = "CREATE ContentSettingsMetadata", nickname = "createContentSettingsMetadata", tags = {"content-settings-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PostMapping("/plugins/cms/contentSettings/metadata")
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<Map<String, List<String>>>> createMetadata(
            @Valid @RequestBody CreateContentSettingsMetadataRequest request) {
        logger.debug("REST request - add new content settings metadata");

        return ResponseEntity.ok(new SimpleRestResponse<>(
                service.addMetadata(request.getKey().trim(), request.getMapping().trim())
        ));
    }

    @ApiOperation(value = "EDIT ContentSettingsMetadata", nickname = "editContentSettingsMetadata", tags = {"content-settings-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PutMapping("/plugins/cms/contentSettings/metadata/{key}")
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<Map<String, List<String>>>> editMetadata(@PathVariable String key,
            @Valid @RequestBody EditContentSettingsMetadataRequest request) {
        logger.debug("REST request - add new content settings metadata");

        return ResponseEntity.ok(new SimpleRestResponse<>(
                service.editMetadata(key, request.getMapping().trim())
        ));
    }

    @ApiOperation(value = "DELETE ContentSettingsMetadata", nickname = "deleteContentSettingsMetadata", tags = {"content-settings-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @DeleteMapping("/plugins/cms/contentSettings/metadata/{key}")
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<Map<String, List<String>>>> deleteMetadata(@PathVariable String key) {
        logger.debug("REST request - delete content settings metadata: {}", key);

        return ResponseEntity.ok(new SimpleRestResponse<>(
                service.removeMetadata(key.trim())
        ));
    }

    @ApiOperation(value = "CREATE ContentSettingsCropRatio", nickname = "createContentSettingsCropRatio", tags = {"content-settings-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PostMapping("/plugins/cms/contentSettings/cropRatios")
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<List<String>>> createCropRatio(
            @Valid @RequestBody ContentSettingsCropRatioRequest request) {
        logger.debug("REST request - add new content settings crop ratio");

        return ResponseEntity.ok(new SimpleRestResponse<>(
                service.addCropRatio(request.getRatio().trim())
        ));
    }

    @ApiOperation(value = "EDIT ContentSettingsCropRatio", nickname = "editContentSettingsCropRatio", tags = {"content-settings-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Updated"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PutMapping("/plugins/cms/contentSettings/cropRatios/{ratio}")
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<List<String>>> editCropRatio(@PathVariable String ratio,
            @Valid @RequestBody ContentSettingsCropRatioRequest request) {
        logger.debug("REST request - edit content settings crop ratio: {}", ratio);

        return ResponseEntity.ok(new SimpleRestResponse<>(
                service.editCropRatio(ratio, request.getRatio().trim())
        ));
    }

    @ApiOperation(value = "DELETE ContentSettingsCropRatio", nickname = "deleteContentSettingsCropRatio", tags = {"content-settings-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @DeleteMapping("/plugins/cms/contentSettings/cropRatios/{ratio}")
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<Map>> deleteCropRatio(@PathVariable String ratio) {
        logger.debug("REST request - delete content settings crop ratio: {}", ratio);

        service.removeCropRatio(ratio.trim());
        return ResponseEntity.ok(new SimpleRestResponse<>(new HashMap()));
    }

    @ApiOperation(value = "PUT ContentSettingsEditor", nickname = "putContentSettingsEditor", tags = {"content-settings-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PutMapping("/plugins/cms/contentSettings/editor")
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<ContentSettingsDto.Editor>> setEditor(
            @ApiParam("Available editor codes: none, fckeditor") @Valid @RequestBody ContentSettingsEditorRequest request) {
        logger.debug("REST request - set content settings editor");

        return ResponseEntity.ok(new SimpleRestResponse<>(
                service.setEditor(request.getKey().trim())
        ));
    }

    @ApiOperation(value = "POST Reload Indexes", nickname = "postReloadIndexes", tags = {"content-settings-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PostMapping("/plugins/cms/contentSettings/reloadIndexes")
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<Map>> reloadIndexes() {
        logger.debug("REST request - reload indexes");
        service.reloadContentsIndex();
        return ResponseEntity.ok(new SimpleRestResponse<>(new HashMap()));
    }

    @ApiOperation(value = "POST Reload References", nickname = "postReloadReferences", tags = {"content-settings-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PostMapping("/plugins/cms/contentSettings/reloadReferences")
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<Map>> reloadReferences() {
        logger.debug("REST request - reload references");
        service.reloadContentsReference();
        return ResponseEntity.ok(new SimpleRestResponse<>(new HashMap()));
    }
}
