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
package org.entando.entando.plugins.jpversioning.web.resource;

import com.agiletec.aps.system.services.user.UserDetails;
import javax.servlet.http.HttpSession;
import org.entando.entando.aps.util.HttpSessionHelper;
import org.entando.entando.plugins.jpversioning.services.resource.ResourcesVersioningService;
import org.entando.entando.plugins.jpversioning.web.resource.model.ResourceDTO;
import org.entando.entando.plugins.jpversioning.web.resource.model.ResourceDownloadDTO;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/plugins/versioning/resources")
public class ResourceVersioningController implements IResourceVersioning {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ResourcesVersioningService resourcesVersioningService;

    @Autowired
    private HttpSession httpSession;

    @Override
    public ResponseEntity<PagedRestResponse<ResourceDTO>> listTrashedResources(String resourceTypeCode,
            RestListRequest requestList) {
        logger.debug("REST request - list trashed resources for resourceTypeCode: {} and with request: {}",
                resourceTypeCode, requestList);
        UserDetails userDetails = HttpSessionHelper.extractCurrentUser(httpSession);
        PagedMetadata<ResourceDTO> result = resourcesVersioningService
                .getTrashedResources(resourceTypeCode, requestList, userDetails);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResourceDTO> recoverResource(String resourceId) {
        logger.debug("REST request - recover resource: {}", resourceId);
        ResourceDTO result = resourcesVersioningService.recoverResource(resourceId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResourceDTO> deleteTrashedResource(String resourceId) {
        logger.debug("REST request - deleting trashed resource: {}", resourceId);
        ResourceDTO result = resourcesVersioningService.deleteTrashedResource(resourceId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getTrashedResource(String resourceId, Integer size) {
        logger.debug("REST request - get trashed resource id: {} and size: {}", resourceId, size);
        UserDetails userDetails = HttpSessionHelper.extractCurrentUser(httpSession);
        ResourceDownloadDTO result = resourcesVersioningService.getTrashedResource(resourceId, size, userDetails);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + result.getFilename());
        headers.add(HttpHeaders.CONTENT_TYPE, result.getType());
        return new ResponseEntity(result.getBytes(), headers, HttpStatus.OK);
    }
}
