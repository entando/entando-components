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
package org.entando.entando.web.digitalexchange.rating;

import com.agiletec.aps.system.services.role.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.entando.entando.aps.system.services.digitalexchange.rating.DERatingsSummary;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = {"digital-exchange", "installation"})
@RequestMapping(value = "/digitalExchange/rate")
public interface DEComponentRatingResource {

    @ApiOperation(value = "Rate a component")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @PostMapping(value = "/{exchange}/{component}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SimpleRestResponse<DERatingsSummary>> rateComponent(@PathVariable("exchange") String exchangeId,
            @PathVariable("component") String componentId, @Valid @RequestBody DERatingValue rating, HttpServletRequest request);
}
