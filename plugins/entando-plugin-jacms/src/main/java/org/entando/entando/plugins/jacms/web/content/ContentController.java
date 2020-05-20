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

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.parse.ContentDOM;
import org.entando.entando.aps.util.HttpSessionHelper;
import org.entando.entando.plugins.jacms.aps.system.services.content.IContentService;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpSession;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.*;
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

import org.entando.entando.plugins.jacms.web.content.validator.BatchContentStatusRequest;
import org.entando.entando.plugins.jacms.web.content.validator.ContentStatusRequest;
import org.entando.entando.plugins.jacms.web.content.validator.RestContentListRequest;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;

/**
 * @author E.Santoboni
 */
@RestController
@RequestMapping(value = "/plugins/cms/contents")
public class ContentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String ERRCODE_CONTENT_NOT_FOUND = "1";
    public static final String ERRCODE_REFERENCED_ONLINE_CONTENT = "2";
    public static final String ERRCODE_UNAUTHORIZED_CONTENT = "3";
    public static final String ERRCODE_DELETE_PUBLIC_PAGE = "4";
    public static final String ERRCODE_INVALID_MODEL = "5";
    public static final String ERRCODE_INVALID_LANG_CODE = "6";
    public static final String ERRCODE_CONTENT_REFERENCES = "7";

    @Autowired
    private HttpSession httpSession;

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
    public ResponseEntity<PagedRestResponse<ContentDto>> getContents(RestContentListRequest requestList) {
        logger.debug("getting contents with request {} - status {}", requestList, requestList.getStatus());
        requestList.setSort(normalizeAttributeNames(requestList.getSort()));
        Optional.ofNullable(requestList.getFilters()).map(Arrays::stream).orElseGet(Stream::empty).forEach(filter -> {
            filter.setAttribute(normalizeAttributeNames(filter.getAttribute()));
        });
        this.getPaginationValidator().validateRestListRequest(requestList, ContentDto.class);
        PagedMetadata<ContentDto> result = this.getContentService().getContents(requestList, HttpSessionHelper.extractCurrentUser(httpSession));
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    private String normalizeAttributeNames(String attributeName) {
        if (attributeName != null) {
            if ("lastmodified".equalsIgnoreCase(attributeName)) {
                return IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY;
            } else if (IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY.equalsIgnoreCase(attributeName)) {
                return IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY;
            } else if ("description".equalsIgnoreCase(attributeName)) {
                return IContentManager.CONTENT_DESCR_FILTER_KEY;
            } else if ("id".equalsIgnoreCase(attributeName) || IEntityManager.ENTITY_ID_FILTER_KEY.equalsIgnoreCase(attributeName)) {
                return IEntityManager.ENTITY_ID_FILTER_KEY;
            }
            return attributeName.toLowerCase();
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ContentDto>> getContent(@PathVariable String code,
            @RequestParam(name = "status", required = false, defaultValue = IContentService.STATUS_DRAFT) String status,
            @RequestParam(name = "lang", required = false) String lang) {
        return this.getContent(code, null, status, false, lang);
    }

    @RequestMapping(value = "/{code}/model/{modelId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ContentDto>> getContent(@PathVariable String code, @PathVariable String modelId,
            @RequestParam(name = "status", required = false, defaultValue = IContentService.STATUS_DRAFT) String status,
            @RequestParam(name = "resolveLinks", required = false, defaultValue = "false") boolean resolveLinks,
            @RequestParam(name = "lang", required = false) String lang) {
        logger.debug("Requested content -> {} - model {} - status {}", code, modelId, status);
        ContentDto dto;
        if (!this.getContentValidator().existContent(code, status)) {
            throw new ResourceNotFoundException(EntityValidator.ERRCODE_ENTITY_DOES_NOT_EXIST, "Content", code);
        } else {
            dto = this.getContentService().getContent(code, modelId, status, lang, resolveLinks, HttpSessionHelper.extractCurrentUser(httpSession));
        }
        logger.debug("Main Response -> {}", dto);
        return new ResponseEntity<>(new SimpleRestResponse<>(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<List<ContentDto>>> addContent(@Valid @RequestBody List<ContentDto> bodyRequest, BindingResult bindingResult) {
        logger.debug("Add new content -> {}", bodyRequest);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        List<ContentDto> response = bodyRequest.stream()
            .map(content -> {
                this.getContentValidator().validate(content, bindingResult);
                if (bindingResult.hasErrors()) {
                    throw new ValidationGenericException(bindingResult);
                }
                ContentDto result = this.getContentService().addContent(content, HttpSessionHelper.extractCurrentUser(httpSession), bindingResult);
                if (bindingResult.hasErrors()) {
                    throw new ValidationGenericException(bindingResult);
                }
                return result;
            })
            .collect(Collectors.toList());

        return new ResponseEntity<>(new SimpleRestResponse<>(response), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    @RequestMapping(value = "/{code}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ContentDto>> updateContent(@PathVariable String code,
                                                                        @Valid @RequestBody ContentDto bodyRequest, BindingResult bindingResult) {
        logger.debug("Update content -> {}", bodyRequest);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getContentValidator().validateBodyName(code, bodyRequest, bindingResult);
        ContentDto response = this.getContentService().updateContent(bodyRequest, HttpSessionHelper.extractCurrentUser(httpSession), bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        return new ResponseEntity<>(new SimpleRestResponse<>(response), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<List<ContentDto>>> updateContents(
            @Valid @RequestBody List<ContentDto> bodyRequest, BindingResult bindingResult) {
        logger.debug("Update content -> {}", bodyRequest);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        UserDetails userDetails = HttpSessionHelper.extractCurrentUser(httpSession);
        List<ContentDto> response = bodyRequest.stream()
            .map(content -> {
                ContentDto result = this.getContentService().updateContent(content, userDetails, bindingResult);
                if (bindingResult.hasErrors()) {
                    throw new ValidationGenericException(bindingResult);
                }
                return result;
            })
            .collect(Collectors.toList());

        return new ResponseEntity<>(new SimpleRestResponse<>(response), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    @RequestMapping(value = "/{code}/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<ContentDto, Map<String, String>>> updateContentStatus(@PathVariable String code,
            @Valid @RequestBody ContentStatusRequest contentStatusRequest, BindingResult bindingResult) {
        logger.debug("changing status for content {} with request {}", code, contentStatusRequest);
        Map<String, String> metadata = new HashMap<>();
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        ContentDto contentDto = this.getContentService().updateContentStatus(code, contentStatusRequest.getStatus(), HttpSessionHelper.extractCurrentUser(httpSession));
        metadata.put("status", contentStatusRequest.getStatus());
        return new ResponseEntity<>(new RestResponse<>(contentDto, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    @RequestMapping(value = "/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<List<ContentDto>, Map<String, String>>> updateContentsStatus(
            @Valid @RequestBody BatchContentStatusRequest batchContentStatusRequest, BindingResult bindingResult) {
        logger.debug("changing status for contents with request {}", batchContentStatusRequest);
        Map<String, String> metadata = new HashMap<>();
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        List<ContentDto> response = this.getContentService().updateContentsStatus(batchContentStatusRequest.getCodes(),
                batchContentStatusRequest.getStatus(), HttpSessionHelper.extractCurrentUser(httpSession));
        metadata.put("status", batchContentStatusRequest.getStatus());

        return new ResponseEntity<>(new RestResponse<>(response, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    @RequestMapping(value = "/{code}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<?>> deleteContent(@PathVariable String code) {
        logger.debug("Deleting content -> {}", code);
        DataBinder binder = new DataBinder(code);
        BindingResult bindingResult = binder.getBindingResult();
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getContentService().deleteContent(code, HttpSessionHelper.extractCurrentUser(httpSession));
        Map<String, String> payload = new HashMap<>();
        payload.put("code", code);
        return new ResponseEntity<>(new SimpleRestResponse<>(payload), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.CONTENT_EDITOR)
    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<?>> deleteContents(@RequestBody List<String> codes) {
        logger.debug("Deleting contents -> {}", codes);

        UserDetails userDetails = HttpSessionHelper.extractCurrentUser(httpSession);
        List<String> payload = codes.stream()
            .peek(code -> this.getContentService().deleteContent(code, userDetails))
            .collect(Collectors.toList());

        return new ResponseEntity<>(new SimpleRestResponse<>(payload), HttpStatus.OK);
    }

}
