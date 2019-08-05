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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.entando.entando.plugins.jacms.aps.system.services.contentsettings.ContentSettingsService;
import org.entando.entando.plugins.jacms.web.contentsettings.model.ContentSettingsCropRatioRequest;
import org.entando.entando.plugins.jacms.web.contentsettings.model.ContentSettingsDto;
import org.entando.entando.plugins.jacms.web.contentsettings.model.ContentSettingsEditorRequest;
import org.entando.entando.plugins.jacms.web.contentsettings.model.ContentSettingsMetadataRequest;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class ContentSettingsController {

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
    @RestAccessControl(permission = Permission.SUPERUSER)
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
            @Valid @RequestBody ContentSettingsMetadataRequest request) {
        logger.debug("REST request - add new content settings metadata");

        return ResponseEntity.ok(new SimpleRestResponse<>(
                service.addMetadata(request.getKey().trim(), request.getMapping().trim())
        ));
    }

    @ApiOperation(value = "DELETE ContentSettingsMetadata", nickname = "deleteContentSettingsMetadata", tags = {"content-settings-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @DeleteMapping("/plugins/cms/contentSettings/metadata")
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<Map<String, List<String>>>> deleteMetadata(
            @Valid @RequestBody ContentSettingsMetadataRequest request) {
        logger.debug("REST request - delete content settings metadata");

        return ResponseEntity.ok(new SimpleRestResponse<>(
                service.removeMetadata(request.getKey().trim())
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

    @ApiOperation(value = "DELETE ContentSettingsCropRatio", nickname = "deleteContentSettingsCropRatio", tags = {"content-settings-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @DeleteMapping("/plugins/cms/contentSettings/cropRatios")
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<List<String>>> deleteCropRatio(
            @Valid @RequestBody ContentSettingsCropRatioRequest request) {
        logger.debug("REST request - delete content settings crop ratio");

        return ResponseEntity.ok(new SimpleRestResponse<>(
                service.removeCropRatio(request.getRatio().trim())
        ));
    }

    @ApiOperation(value = "PUT ContentSettingsEditor", nickname = "putContentSettingsEditor", tags = {"content-settings-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PutMapping("/plugins/cms/contentSettings/editor")
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<ContentSettingsDto.Editor>> setEditor(
            @Valid @RequestBody ContentSettingsEditorRequest request) {
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
    public ResponseEntity<SimpleRestResponse<String>> reloadIndexes() {
        logger.debug("REST request - reload indexes");
        service.reloadContentsIndex();
        return ResponseEntity.ok(new SimpleRestResponse<>("")); //TODO how to return with no result?
    }

    @ApiOperation(value = "POST Reload References", nickname = "postReloadReferences", tags = {"content-settings-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @PostMapping("/plugins/cms/contentSettings/reloadReferences")
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<SimpleRestResponse<String>> reloadReferences() {
        logger.debug("REST request - reload references");
        service.reloadContentsReference();
        return ResponseEntity.ok(new SimpleRestResponse<>("")); //TODO how to return with no result?
    }
}
