
/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.digitalexchange.install;

import com.agiletec.aps.system.services.role.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import org.entando.entando.aps.system.services.digitalexchange.install.DigitalExchangeJob;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = {"digital-exchange", "installation"})
@RequestMapping(value = "/digitalExchange")
public interface DigitalExchangeInstallResource {

    @ApiOperation(value = "Starts component installation job")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @PostMapping(value = "{exchange}/install/{component}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SimpleRestResponse<DigitalExchangeJob>> install(@PathVariable("exchange") String digitalExchangeId,
                                                                   @PathVariable("component") String componentId, HttpServletRequest request) throws URISyntaxException ;

    @ApiOperation(value = "Starts component remove job ")
    @ApiResponses({
            @ApiResponse( code = 201, message = "Created")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @PostMapping(value = "{exchange}/uninstall/{component}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SimpleRestResponse<DigitalExchangeJob>> uninstall(@PathVariable("exchange") String digitalExchangeId,
                                                                     @PathVariable("component") String componentId, HttpServletRequest request) throws URISyntaxException ;

    @ApiOperation(value = "Checks installation job status")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @GetMapping(value = "/install/{component}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SimpleRestResponse<DigitalExchangeJob>> getLastInstallJob(@PathVariable("component") String componentId);

    @ApiOperation(value = "Checks removal job status")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @GetMapping(value = "/uninstall/{component}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SimpleRestResponse<DigitalExchangeJob>> getLastUninstallJob(@PathVariable("component") String componentId);

}
