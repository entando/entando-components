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

import java.util.List;
import javax.validation.Valid;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelReference;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelDto;
import java.util.Collections;
import java.util.Map;
import org.entando.entando.aps.system.services.dataobjectmodel.model.IEntityModelDictionary;
import org.entando.entando.plugins.jacms.aps.system.services.contentmodel.ContentModelService;
import org.entando.entando.plugins.jacms.web.contentmodel.validator.ContentModelValidator;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentModelResourceController implements ContentModelResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ContentModelService contentModelService;
    private final ContentModelValidator contentModelValidator;

    @Autowired
    public ContentModelResourceController(ContentModelService contentModelService) {
        this.contentModelService = contentModelService;
        this.contentModelValidator = new ContentModelValidator();
    }

    @Override
    public ResponseEntity<RestResponse<PagedMetadata<ContentModelDto>>> getContentModels(RestListRequest requestList) {
        this.contentModelValidator.validateRestListRequest(requestList, ContentModelDto.class);
        PagedMetadata<ContentModelDto> result = contentModelService.findMany(requestList);
        this.contentModelValidator.validateRestListResult(requestList, result);
        logger.debug("loading contentModel list -> {}", result);
        return ResponseEntity.ok(new RestResponse(result.getBody(), null, result));
    }

    @Override
    public ResponseEntity<RestResponse<ContentModelDto>> getContentModel(@PathVariable Long modelId) {
        logger.debug("loading contentModel {}", modelId);
        ContentModelDto contentModel = contentModelService.getContentModel(modelId);
        return ResponseEntity.ok(new RestResponse(contentModel));
    }

    @Override
    public ResponseEntity<RestResponse<ContentModelDto>> addContentModel(@Valid @RequestBody ContentModelDto contentModel, BindingResult bindingResult) {
        logger.debug("adding content model");
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        ContentModelDto dto = contentModelService.create(contentModel);
        return ResponseEntity.ok(new RestResponse(dto));
    }

    @Override
    public ResponseEntity<RestResponse<ContentModelDto>> updateContentModel(@PathVariable Long modelId, @Valid @RequestBody ContentModelDto contentModel, BindingResult bindingResult) {
        logger.debug("updating contentModel {}", modelId);

        // field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.contentModelValidator.validateBodyName(modelId, contentModel, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        ContentModelDto updatedContentModel = contentModelService.update(contentModel);
        return ResponseEntity.ok(new RestResponse(updatedContentModel));
    }

    @Override
    public ResponseEntity<RestResponse<Map<String, String>>> deleteContentModel(@PathVariable Long modelId) {
        logger.info("deleting content model {}", modelId);
        contentModelService.delete(modelId);
        Map<String, String> result = Collections.singletonMap("modelId", String.valueOf(modelId));
        return ResponseEntity.ok(new RestResponse(result));
    }

    @Override
    public ResponseEntity<RestResponse<List<ContentModelReference>>> getReferences(@PathVariable Long modelId) {
        logger.debug("loading contentModel references for model {}", modelId);
        List<ContentModelReference> references = contentModelService.getContentModelReferences(modelId);
        return ResponseEntity.ok(new RestResponse(references));
    }

    @Override
    public ResponseEntity<RestResponse<IEntityModelDictionary>> getDictionary(@RequestParam(value = "typeCode", required = false) String typeCode) {
        logger.debug("loading contentModel dictionary for type {}", typeCode);
        IEntityModelDictionary dictionary = contentModelService.getContentModelDictionary(typeCode);
        return new ResponseEntity<>(new RestResponse(dictionary), HttpStatus.OK);
    }
}
