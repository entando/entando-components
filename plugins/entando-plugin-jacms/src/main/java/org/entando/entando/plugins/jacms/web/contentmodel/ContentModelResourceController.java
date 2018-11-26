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

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.*;
import org.entando.entando.aps.system.services.dataobjectmodel.model.IEntityModelDictionary;
import org.entando.entando.plugins.jacms.aps.system.services.ContentModelService;
import org.entando.entando.plugins.jacms.web.contentmodel.validator.ContentModelValidator;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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
    public ResponseEntity<PagedRestResponse<ContentModelDto>> getContentModels(RestListRequest requestList) {
        this.contentModelValidator.validateRestListRequest(requestList, ContentModelDto.class);
        PagedMetadata<ContentModelDto> result = contentModelService.findMany(requestList);
        this.contentModelValidator.validateRestListResult(requestList, result);
        logger.debug("loading contentModel list -> {}", result);
        return ResponseEntity.ok(new PagedRestResponse<>(result));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<ContentModelDto>> getContentModel(@PathVariable Long modelId) {
        logger.debug("loading contentModel {}", modelId);
        ContentModelDto contentModel = contentModelService.getContentModel(modelId);
        return ResponseEntity.ok(new SimpleRestResponse<>(contentModel));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<ContentModelDto>> addContentModel(@Valid @RequestBody ContentModelDto contentModel, BindingResult bindingResult) {
        logger.debug("adding content model");
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        ContentModelDto dto = contentModelService.create(contentModel);
        return ResponseEntity.ok(new SimpleRestResponse<>(dto));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<ContentModelDto>> updateContentModel(@PathVariable Long modelId, @Valid @RequestBody ContentModelDto contentModel, BindingResult bindingResult) {
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
        return ResponseEntity.ok(new SimpleRestResponse<>(updatedContentModel));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<Map<String, String>>> deleteContentModel(@PathVariable Long modelId) {
        logger.info("deleting content model {}", modelId);
        contentModelService.delete(modelId);
        Map<String, String> result = Collections.singletonMap("modelId", String.valueOf(modelId));
        return ResponseEntity.ok(new SimpleRestResponse<>(result));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<List<ContentModelReference>>> getReferences(@PathVariable Long modelId) {
        logger.debug("loading contentModel references for model {}", modelId);
        List<ContentModelReference> references = contentModelService.getContentModelReferences(modelId);
        return ResponseEntity.ok(new SimpleRestResponse<>(references));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<IEntityModelDictionary>> getDictionary(@RequestParam(value = "typeCode", required = false) String typeCode) {
        logger.debug("loading contentModel dictionary for type {}", typeCode);
        IEntityModelDictionary dictionary = contentModelService.getContentModelDictionary(typeCode);
        return ResponseEntity.ok(new SimpleRestResponse<>(dictionary));
    }
}
