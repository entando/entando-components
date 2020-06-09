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
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.VersioningManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.entando.entando.plugins.jpversioning.web.contentsversioning.model.ContentVersionDTO;
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

    public PagedMetadata<ContentVersionDTO> getListContentVersions(String contentId, RestListRequest requestList) {
        logger.debug("LIST listContentVersions for content {} with req {}", contentId, requestList);
        List<ContentVersionDTO> contentVersions = new ArrayList<>();
        try {
            contentVersions = versioningManager.getVersions(contentId).stream().map(v -> mapContentVersionToDTO(getContentVersion(v)))
                    .collect(Collectors.toList());
        } catch (ApsSystemException e) {
            e.printStackTrace();
        }
        final List<ContentVersionDTO> sublist = requestList.getSublist(contentVersions);
        PagedMetadata<ContentVersionDTO> pagedResults = new PagedMetadata<>(requestList, contentVersions.size());
        pagedResults.setBody(sublist);
        return pagedResults;
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
        return contentVersionDTO;
    }

    private ContentVersion getContentVersion(Long version) {
        try {
            return versioningManager.getVersion(version);
        } catch (ApsSystemException e) {
            e.printStackTrace();
        }
        return null;
    }

}
