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
package org.entando.entando.plugins.jpversioning.web.configuration;

import com.agiletec.aps.system.services.role.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.entando.entando.plugins.jpversioning.web.configuration.model.VersioningConfigurationDTO;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api(tags = {"versioning-configuration-controller"})
@ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 409, message = "Conflict"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 500, message = "Server Error")
})
public interface IVersioningConfiguration {

    @ApiOperation(value = "GET versioning configuration", nickname = "getVersioningConfiguration", tags = {
            "versioning-configuration-controller"})
    @GetMapping("")
    @RestAccessControl(permission = Permission.SUPERUSER)
    ResponseEntity<VersioningConfigurationDTO> getVersioningConfiguration();

    @ApiOperation(value = "PUT versioning configuration", nickname = "putVersioningConfiguration", tags = {
            "versioning-configuration-controller"})
    @PutMapping("")
    @RestAccessControl(permission = Permission.SUPERUSER)
    ResponseEntity<VersioningConfigurationDTO> putVersioningConfiguration(@RequestBody VersioningConfigurationDTO bodyRequest);

}
