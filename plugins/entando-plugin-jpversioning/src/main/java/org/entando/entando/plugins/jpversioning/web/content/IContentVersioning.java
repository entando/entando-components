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
package org.entando.entando.plugins.jpversioning.web.content;

import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Map;
import org.entando.entando.plugins.jpversioning.web.content.model.ContentVersionDTO;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Api(tags = {"content-versioning-controller"})
@ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 409, message = "Conflict"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 500, message = "Server Error")
})
public interface IContentVersioning {

    @ApiOperation(value = "LIST content versions", nickname = "listContentVersions", tags = {
            "content-versioning-controller"})
    @GetMapping("/{contentId}")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    ResponseEntity<PagedRestResponse<ContentVersionDTO>> listContentVersions(
            @PathVariable(value = "contentId") String contentId, RestListRequest requestList);

    @ApiOperation(value = "LIST contents versions", nickname = "listLatestVersions", tags = {
            "content-versioning-controller"})
    @GetMapping("")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    ResponseEntity<PagedRestResponse<ContentVersionDTO>> listLatestVersions(RestListRequest requestList);

    @ApiOperation(value = "GET content version", nickname = "getContentVersion", tags = {
            "content-versioning-controller"})
    @GetMapping("/{contentId}/versions/{versionId}")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    ResponseEntity<ContentDto> getContentVersion(@PathVariable(value = "contentId") String contentId,
            @PathVariable(value = "versionId") Long versionId);

    @ApiOperation(value = "DELETE content version", nickname = "deleteContentVersion", tags = {
            "content-versioning-controller"})
    @DeleteMapping("/{contentId}/versions/{versionId}")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    ResponseEntity<SimpleRestResponse<Map>> delete(@PathVariable String contentId, @PathVariable Long versionId);

    @ApiOperation(value = "POST content version to Recover", nickname = "recoverContentVersion", tags = {
            "content-versioning-controller"})
    @PostMapping("/{contentId}/versions/{versionId}/recover")
    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    ResponseEntity<ContentDto> recoverContentVersion(@PathVariable(value = "contentId") String contentId,
            @PathVariable(value = "versionId") Long versionId);

}
