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
package org.entando.entando.plugins.jpversioning.services.contentsversioning;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.VersioningManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.plugins.jacms.aps.system.services.content.ContentService;
import org.entando.entando.plugins.jpversioning.web.contentsversioning.model.ContentVersionDTO;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentVersioningService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private VersioningManager versioningManager;

    @Autowired
    private ContentService contentService;

    public PagedMetadata<ContentVersionDTO> getListContentVersions(String contentId, RestListRequest requestList) {
        logger.debug("LIST listContentVersions for content {} with req {}", contentId, requestList);
        List<ContentVersionDTO> contentVersionDTOs = new ArrayList<>();
        List<Long> contentVersions = new ArrayList<>();
        try {
            contentVersions = versioningManager.getVersions(contentId);
            contentVersionDTOs = requestList.getSublist(contentVersions).stream()
                    .map(cv -> mapContentVersionToDTO(getContentVersion(cv)))
                    .collect(Collectors.toList());
        } catch (ApsSystemException e) {
            logger.error("Error reading the list of content versions for content {}",contentId, e);
            throw new RestServerError(String.format("Error while getting content versions for content %s", contentId), e);
        }
        PagedMetadata<ContentVersionDTO> pagedResults = new PagedMetadata<>(requestList, contentVersions.size());
        pagedResults.setBody(contentVersionDTOs);
        return pagedResults;
    }

    public PagedMetadata<ContentVersionDTO> getLatestVersions(RestListRequest requestList) {

        try {
            logger.debug("GET getLatestVersions with req {}", requestList);

            String contentType = extractAttributeValue(requestList.getFilters(), "contentType");
            String description = extractAttributeValue(requestList.getFilters(), "description");

            List<ContentVersionDTO> result = versioningManager.getLastVersions(contentType, description).stream()
                    .map(v -> mapContentVersionToDTO(getContentVersion(v))).collect(
                            Collectors.toList());

            final List<ContentVersionDTO> sublist = requestList.getSublist(result);
            PagedMetadata<ContentVersionDTO> pagedResults = new PagedMetadata<>(requestList, result.size());
            pagedResults.setBody(sublist);

            return pagedResults;
        } catch (ApsSystemException e) {
            logger.error("Error while getting latest versions for request {}", requestList, e);
            throw new RestServerError(String.format("Error while getting content versions for request %s", requestList), e);
        }
    }

    private String extractAttributeValue(Filter[] filters, String attributeName) {
        if (filters != null) {
            for (Filter filter : filters) {
                if (filter.getAttribute().equals(attributeName)) {
                    return filter.getValue();
                }
            }
        }
        return null;
    }

    public ContentVersion getContentVersion(Long versionId) {
        try {
            ContentVersion contentVersion = versioningManager.getVersion(versionId);
            return contentVersion;
        } catch (ApsSystemException e) {
            logger.error("Error reading version {} " + versionId, e);
        }
        return null;
    }

    public ContentDto getContent(Long versionId) {
        try {
            final ContentVersion contentVersion = versioningManager.getVersion(versionId);
            final Content content = versioningManager.getContent(contentVersion);
            final ContentDto contentDto = contentService.getDtoBuilder().convert(content);
            return contentDto;
        } catch (ApsSystemException e) {
            logger.error("Error reading the content from version {} ", versionId, e);
        }
        return null;
    }

    private ContentVersionDTO mapContentVersionToDTO(ContentVersion contentVersion) {
        ContentVersionDTO contentVersionDTO = new ContentVersionDTO();
        contentVersionDTO.setApproved(contentVersion.isApproved());
        contentVersionDTO.setContentType(contentVersion.getContentType());
        contentVersionDTO.setDescription(contentVersion.getDescr());
        contentVersionDTO.setId(contentVersion.getId());
        contentVersionDTO.setOnlineVersion(contentVersion.getOnlineVersion());
        contentVersionDTO.setUsername(contentVersion.getUsername());
        contentVersionDTO.setStatus(contentVersion.getStatus());
        contentVersionDTO.setVersion(contentVersion.getVersion());
        contentVersionDTO.setVersionDate(contentVersion.getVersionDate());
        contentVersionDTO.setContentId(contentVersion.getContentId());
        return contentVersionDTO;
    }

}
