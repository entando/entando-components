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
package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Map;
import javax.validation.Valid;
import org.entando.entando.aps.system.services.dataobjectmodel.model.IEntityModelDictionary;
import org.entando.entando.plugins.jacms.web.contentmodel.model.ContentModelReferenceDTO;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.entando.entando.web.component.ComponentUsage;
import org.entando.entando.web.component.ComponentUsageEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags={ "content-model-controller" })
@ApiResponses({
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 409, message = "Conflict"),
    @ApiResponse(code = 401, message = "Unauthorized"),
    @ApiResponse(code = 500, message = "Server Error")
})
public interface ContentModelResource {

    @ApiOperation(value = "Returns a paginated list of content models")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK")
    })
    ResponseEntity<PagedRestResponse<ContentModelDto>> getContentModels(RestListRequest requestList);
    
    @ApiOperation(value = "Retrieves a content model")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Not Found")
    })
    ResponseEntity<SimpleRestResponse<ContentModelDto>> getContentModel(@PathVariable Long modelId);
    
    @ApiOperation(value = "Adds a new content model")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK")
    })
    ResponseEntity<SimpleRestResponse<ContentModelDto>> addContentModel(@Valid @RequestBody ContentModelDto contentModel, BindingResult bindingResult);
    
    @ApiOperation(value = "Updates an existing content model")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK")
    })
    ResponseEntity<SimpleRestResponse<ContentModelDto>> updateContentModel(@PathVariable Long modelId, @Valid @RequestBody ContentModelDto contentModelRequest, BindingResult bindingResult);
    
    @ApiOperation(value = "Deletes a content model")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK")
    })
    ResponseEntity<SimpleRestResponse<Map<String, String>>> deleteContentModel(@PathVariable Long modelId);
    
    @ApiOperation(value = "Returns the references (pages and widgets) of a content model")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK")
    })
    ResponseEntity<PagedRestResponse<ContentModelReferenceDTO>> getReferences(@PathVariable Long modelId,RestListRequest requestList);

    @ApiOperation(value = "Returns usages counts for the content template")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK")
    })
    ResponseEntity<SimpleRestResponse<ComponentUsage>> getComponentUsage(@PathVariable Long modelId);

    @ApiOperation(value = "Returns usage details for the content template")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK")
    })
    ResponseEntity<PagedRestResponse<ComponentUsageEntity>> getComponentUsageDetails(@PathVariable Long modelId, RestListRequest restListRequest);

    @ApiOperation(value = "Returns dictionary containing Velocity instructions for the editor")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK")
    })
    ResponseEntity<SimpleRestResponse<IEntityModelDictionary>> getDictionary(@RequestParam(value = "typeCode", required = false) String typeCode);
}
