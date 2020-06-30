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
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelDto;
import java.util.Collections;
import java.util.Map;
import javax.validation.Valid;
import org.entando.entando.aps.system.services.dataobjectmodel.model.IEntityModelDictionary;
import org.entando.entando.plugins.jacms.aps.system.services.ContentModelService;
import org.entando.entando.plugins.jacms.web.contentmodel.model.ContentModelReferenceDTO;
import org.entando.entando.plugins.jacms.web.contentmodel.validator.ContentModelReferencesValidator;
import org.entando.entando.plugins.jacms.web.contentmodel.validator.ContentModelValidator;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/plugins/cms/contentmodels")
public class ContentModelResourceController implements ContentModelResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ContentModelService contentModelService;
    private final ContentModelValidator contentModelValidator;
    private final ContentModelReferencesValidator contentModelReferencesValidator;

    @Autowired
    public ContentModelResourceController(ContentModelService contentModelService,
            ContentModelReferencesValidator contentModelReferencesValidator) {
        this.contentModelService = contentModelService;
        this.contentModelReferencesValidator = contentModelReferencesValidator;
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
    public ResponseEntity<PagedRestResponse<ContentModelReferenceDTO>> getReferences(@PathVariable Long modelId,RestListRequest requestList) {
        logger.debug("loading contentModel references for model {}", modelId);
        this.contentModelReferencesValidator.validateRestListRequest(requestList, ContentModelReferenceDTO.class);
        PagedMetadata<ContentModelReferenceDTO> references = contentModelService.getContentModelReferences(modelId,requestList);
        return ResponseEntity.ok(new PagedRestResponse<>(references));
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
