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
package org.entando.entando.plugins.jacms.web.content;

import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentService;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.services.entity.model.EntityDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.entity.validator.EntityValidator;
import org.entando.entando.plugins.jacms.web.content.validator.ContentValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.entando.entando.web.common.model.SimpleRestResponse;

/**
 * @author E.Santoboni
 */
@RestController
@RequestMapping(value = "/plugins/cms/content")
public class ContentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String ERRCODE_REFERENCED_ONLINE_CONTENT = "2";

    @Autowired
    private IContentService contentService;

    @Autowired
    private ContentValidator contentValidator;

    public IContentService getContentService() {
        return contentService;
    }

    public void setContentService(IContentService contentService) {
        this.contentService = contentService;
    }

    public ContentValidator getContentValidator() {
        return contentValidator;
    }

    public void setContentValidator(ContentValidator contentValidator) {
        this.contentValidator = contentValidator;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<EntityDto>> getContent(@PathVariable String code) throws JsonProcessingException {
        logger.debug("Requested content -> {}", code);
        EntityDto dto;
        if (!this.getContentValidator().existContent(code)) {
            throw new RestRourceNotFoundException(EntityValidator.ERRCODE_ENTITY_DOES_NOT_EXIST, "Content", code);
        } else {
            dto = this.getContentService().getContent(code);
        }
        logger.debug("Main Response -> {}", dto);
        return new ResponseEntity<>(new SimpleRestResponse<>(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<EntityDto>> addContent(@Valid @RequestBody ContentDto bodyRequest, BindingResult bindingResult) {
        logger.debug("Add new content -> {}", bodyRequest);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getContentValidator().validate(bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        EntityDto response = this.getContentService().addContent(bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        return new ResponseEntity<>(new SimpleRestResponse<>(response), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{code}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<EntityDto>> updateContent(@PathVariable String code,
            @Valid @RequestBody ContentDto bodyRequest, BindingResult bindingResult) {
        logger.debug("Update content -> {}", bodyRequest);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getContentValidator().validateBodyName(code, bodyRequest, bindingResult);
        EntityDto response = this.getContentService().updateContent(bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        return new ResponseEntity<>(new SimpleRestResponse<>(response), HttpStatus.OK);
    }

}
