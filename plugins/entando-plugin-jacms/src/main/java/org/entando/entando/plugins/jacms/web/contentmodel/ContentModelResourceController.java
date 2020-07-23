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

import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.*;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.services.dataobjectmodel.model.IEntityModelDictionary;
import org.entando.entando.plugins.jacms.aps.system.services.ContentModelService;
import org.entando.entando.plugins.jacms.web.contentmodel.validator.ContentModelValidator;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.*;
import org.entando.entando.web.component.ComponentUsage;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/plugins/cms/contentmodels")
public class ContentModelResourceController implements ContentModelResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String COMPONENT_ID = "contentTemplate";

    private final ContentModelService contentModelService;
    private final ContentModelValidator contentModelValidator;

    @Autowired
    public ContentModelResourceController(ContentModelService contentModelService) {
        this.contentModelService = contentModelService;
        this.contentModelValidator = new ContentModelValidator();
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<PagedRestResponse<ContentModelDto>> getContentModels(RestListRequest requestList) {
        this.contentModelValidator.validateRestListRequest(requestList, ContentModelDto.class);
        PagedMetadata<ContentModelDto> result = contentModelService.findMany(requestList);
        this.contentModelValidator.validateRestListResult(requestList, result);
        logger.debug("loading contentModel list -> {}", result);
        return ResponseEntity.ok(new PagedRestResponse<>(result));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{modelId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<SimpleRestResponse<ContentModelDto>> getContentModel(@PathVariable Long modelId) {
        logger.debug("loading contentModel {}", modelId);
        ContentModelDto contentModel = contentModelService.getContentModel(modelId);
        return ResponseEntity.ok(new SimpleRestResponse<>(contentModel));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<SimpleRestResponse<ContentModelDto>> addContentModel(@Valid @RequestBody ContentModelDto contentModel, BindingResult bindingResult) {
        logger.debug("adding content model");
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        ContentModelDto dto = contentModelService.create(contentModel);
        return ResponseEntity.ok(new SimpleRestResponse<>(dto));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{modelId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
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
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{modelId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
    public ResponseEntity<SimpleRestResponse<Map<String, String>>> deleteContentModel(@PathVariable Long modelId) {
        logger.info("deleting content model {}", modelId);
        contentModelService.delete(modelId);
        Map<String, String> result = Collections.singletonMap("modelId", String.valueOf(modelId));
        return ResponseEntity.ok(new SimpleRestResponse<>(result));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{modelId}/pagereferences", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<SimpleRestResponse<List<ContentModelReference>>> getReferences(@PathVariable Long modelId) {
        logger.debug("loading contentModel references for model {}", modelId);
        List<ContentModelReference> references = contentModelService.getContentModelReferences(modelId);
        return ResponseEntity.ok(new SimpleRestResponse<>(references));
    }


    // usage and usage details should be supplied better, the service should implements the relative interface,
    // currently this is only a way to make consistent entando-component-manager requests
    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{modelId}/usage", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<SimpleRestResponse<ComponentUsage>> getUsage(@PathVariable Long modelId) {

        logger.debug("loading contentModel references for model {}", modelId);

        int size;
        try {
            size = contentModelService.getContentModelReferences(modelId).size();
        } catch (ResourceNotFoundException e) {
            size = 0;
        }

        ComponentUsage usage = ComponentUsage.builder()
                .type(COMPONENT_ID)
                .code(modelId + "")
                .usage(size)
                .build();

        return new ResponseEntity<>(new SimpleRestResponse<>(usage), HttpStatus.OK);
    }


    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dictionary", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<SimpleRestResponse<IEntityModelDictionary>> getDictionary(@RequestParam(value = "typeCode", required = false) String typeCode) {
        logger.debug("loading contentModel dictionary for type {}", typeCode);
        IEntityModelDictionary dictionary = contentModelService.getContentModelDictionary(typeCode);
        return ResponseEntity.ok(new SimpleRestResponse<>(dictionary));
    }
}
