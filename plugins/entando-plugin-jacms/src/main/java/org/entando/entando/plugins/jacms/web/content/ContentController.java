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
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import org.entando.entando.plugins.jacms.aps.system.services.content.IContentService;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import java.util.Arrays;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
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
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestEntityListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.validation.Errors;

/**
 * @author E.Santoboni
 */
@RestController
@SessionAttributes("user")
@RequestMapping(value = "/plugins/cms/contents")
public class ContentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String ERRCODE_REFERENCED_ONLINE_CONTENT = "2";
    public static final String ERRCODE_UNAUTHORIZED_CONTENT = "3";
    public static final String ERRCODE_DELETE_PUBLIC_PAGE = "5";
    public static final String ERRCODE_INVALID_MODEL = "6";
    public static final String ERRCODE_INVALID_LANG_CODE = "7";

    // ?status=draft|published
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

    protected AbstractPaginationValidator getPaginationValidator() {
        return new AbstractPaginationValidator() {
            @Override
            public boolean supports(Class<?> type) {
                return true;
            }

            @Override
            public void validate(Object o, Errors errors) {
                //nothing to do
            }

            @Override
            protected String getDefaultSortProperty() {
                return IContentManager.CONTENT_CREATION_DATE_FILTER_KEY;
            }

            @Override
            public boolean isValidField(String fieldName, Class<?> type) {
                if (fieldName.contains(".")) {
                    return true;
                } else {
                    return Arrays.asList(IContentManager.METADATA_FILTER_KEYS).contains(fieldName);
                }
            }

        };
    }

    public ContentValidator getContentValidator() {
        return contentValidator;
    }

    public void setContentValidator(ContentValidator contentValidator) {
        this.contentValidator = contentValidator;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<ContentDto>> getContents(RestEntityListRequest requestList, @ModelAttribute("user") UserDetails user,
            @RequestParam(value = "status", required = false, defaultValue = IContentService.STATUS_DRAFT) String status,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "lang", required = false) String lang) {
        logger.debug("getting contents with request {} - status {}", requestList, status);
        this.getPaginationValidator().validateRestListRequest(requestList, ContentDto.class);
        PagedMetadata<ContentDto> result = this.getContentService().getContents(requestList, model, status, lang, user);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    @RequestMapping(value = "/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ContentDto>> getContent(@ModelAttribute("user") UserDetails user,
            @PathVariable String code, @RequestParam(value = "status", required = false, defaultValue = IContentService.STATUS_DRAFT) String status,
            @RequestParam(value = "lang", required = false) String lang) {
        return this.getContent(user, code, null, status, lang);
    }

    @RequestMapping(value = "/{code}/model/{modelId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ContentDto>> getContent(@ModelAttribute("user") UserDetails user,
            @PathVariable String code, @PathVariable String modelId,
            @RequestParam(value = "status", required = false, defaultValue = IContentService.STATUS_DRAFT) String status,
            @RequestParam(value = "lang", required = false) String lang) {
        logger.debug("Requested content -> {} - model {} - status {}", code, modelId, status);
        ContentDto dto;
        if (!this.getContentValidator().existContent(code, status)) {
            throw new ResourceNotFoundException(EntityValidator.ERRCODE_ENTITY_DOES_NOT_EXIST, "Content", code);
        } else {
            dto = this.getContentService().getContent(code, modelId, status, lang, user);
        }
        logger.debug("Main Response -> {}", dto);
        return new ResponseEntity<>(new SimpleRestResponse<>(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ContentDto>> addContent(@ModelAttribute("user") UserDetails user,
            @Valid @RequestBody ContentDto bodyRequest, BindingResult bindingResult) {
        logger.debug("Add new content -> {}", bodyRequest);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getContentValidator().validate(bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        ContentDto response = this.getContentService().addContent(bodyRequest, user, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        return new ResponseEntity<>(new SimpleRestResponse<>(response), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    @RequestMapping(value = "/{code}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ContentDto>> updateContent(@ModelAttribute("user") UserDetails user,
            @PathVariable String code, @Valid @RequestBody ContentDto bodyRequest, BindingResult bindingResult) {
        logger.debug("Update content -> {}", bodyRequest);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getContentValidator().validateBodyName(code, bodyRequest, bindingResult);
        ContentDto response = this.getContentService().updateContent(bodyRequest, user, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        return new ResponseEntity<>(new SimpleRestResponse<>(response), HttpStatus.OK);
    }

}
