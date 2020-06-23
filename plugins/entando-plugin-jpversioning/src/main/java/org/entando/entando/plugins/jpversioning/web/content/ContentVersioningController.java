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
package org.entando.entando.plugins.jpversioning.web.content;

import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.plugins.jpversioning.services.content.ContentVersioningService;
import org.entando.entando.plugins.jpversioning.web.content.model.ContentVersionDTO;
import org.entando.entando.plugins.jpversioning.web.content.validator.ContentVersioningValidator;
import org.entando.entando.plugins.jpversioning.web.content.validator.ContentVersioningValidatorErrorCodes;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
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

    private static final String CONTENT_VERSION_ID = "versionId";
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

        PagedMetadata<ContentVersionDTO> result = contentVersioningService
                .getListContentVersions(contentId, requestList);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PagedRestResponse<ContentVersionDTO>> listLatestVersions(RestListRequest requestList) {
        logger.debug("REST request - list versions for all content with request: {}", requestList);

        contentVersioningValidator.validateRestListRequest(requestList, ContentVersionDTO.class);

        PagedMetadata<ContentVersionDTO> result = contentVersioningService.getLatestVersions(requestList);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ContentDto> getContentVersion(String contentId, Long versionId) {
        logger.debug("REST request - get content version for contentId: {} and versionId", contentId, versionId);

        if (!contentVersioningValidator.checkContentIdForVersion(contentId, versionId)) {
            throw new ResourceNotFoundException(
                    ContentVersioningValidatorErrorCodes.ERRCODE_CONTENT_VERSIONING_WRONG_CONTENT_ID.value,
                    "Content Version", contentId + " version " + versionId);
        }
        ContentDto result = contentVersioningService.getContent(versionId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ContentDto> recoverContentVersion(String contentId, Long versionId) {
        logger.debug("REST request - recover version content with contentId: {} and versionId", contentId, versionId);
        if (!contentVersioningValidator.checkContentIdForVersion(contentId, versionId)) {
            throw new ResourceNotFoundException(
                    ContentVersioningValidatorErrorCodes.ERRCODE_CONTENT_VERSIONING_WRONG_CONTENT_ID.value,
                    "Content Version", contentId + " version " + versionId);
        }
        ContentDto result = contentVersioningService.recover(versionId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SimpleRestResponse<Map>> delete(String contentId, Long versionId){
        logger.debug("REST request - delete content version with versionId: {}", versionId);
        if (!contentVersioningValidator.checkContentIdForVersion(contentId, versionId)) {
            throw new ResourceNotFoundException(
                    ContentVersioningValidatorErrorCodes.ERRCODE_CONTENT_VERSIONING_WRONG_CONTENT_ID.value,
                    "Content Version", contentId + " version " + versionId);
        }
        contentVersioningService.delete(versionId);
        Map<String, String> metadata = ImmutableMap.of(
                CONTENT_VERSION_ID, String.valueOf(versionId)
        );
        return ResponseEntity.ok(new SimpleRestResponse<>(metadata));
    }
}
