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
package org.entando.entando.plugins.jpversioning.web.contentsversioning;

import javax.servlet.http.HttpSession;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.plugins.jpversioning.services.contentsversioning.ContentVersioningService;
import org.entando.entando.plugins.jpversioning.web.contentsversioning.model.ContentVersionDTO;
import org.entando.entando.plugins.jpversioning.web.contentsversioning.validator.ContentVersioningValidator;
import org.entando.entando.plugins.jpversioning.web.contentsversioning.validator.ContentVersioningValidatorErrorCodes;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/plugins/versioning/contents")
public class ContentVersioningController implements IContentVersioning {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ContentVersioningService contentVersioningService;

    @Autowired
    private ContentVersioningValidator contentVersioningValidator;

    @Autowired
    private HttpSession httpSession;

    @Override
    public ResponseEntity<PagedRestResponse<ContentVersionDTO>> listContentVersions(String contentId,
            RestListRequest requestList) {
        logger.debug("REST request - list versions for a content with contentId: {}", contentId);

        contentVersioningValidator.validateRestListRequest(requestList, ContentVersionDTO.class);

        if (!contentVersioningValidator.contentVersioningExist(contentId)) {
            throw new ResourceNotFoundException(
                    ContentVersioningValidatorErrorCodes.ERRCODE_CONTENT_VERSIONING_DOES_NOT_EXIST.value,
                    "Content Versions", contentId);
        }

        PagedMetadata<ContentVersionDTO> result = contentVersioningService.getListContentVersions(contentId, requestList);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PagedRestResponse<ContentVersionDTO>> listLatestVersions(RestListRequest requestList) {
        logger.debug("REST request - list versions for all content with request: {}", requestList);

        contentVersioningValidator.validateRestListRequest(requestList, ContentVersionDTO.class);

        PagedMetadata<ContentVersionDTO> result = contentVersioningService.getLatestVersions(requestList);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }
}
