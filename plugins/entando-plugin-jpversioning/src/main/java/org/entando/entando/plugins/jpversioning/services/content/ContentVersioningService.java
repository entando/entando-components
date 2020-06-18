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
package org.entando.entando.plugins.jpversioning.services.content;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.resource.ResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.TrashedResourceManager;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.VersioningManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.plugins.jacms.aps.system.services.content.ContentService;
import org.entando.entando.plugins.jpversioning.web.content.model.ContentVersionDTO;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class ContentVersioningService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final static String RESOURCE = "resource";
    private final static String RESOURCE_ID = "id";

    @Autowired
    private VersioningManager versioningManager;

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentManager contentManager;

    @Autowired
    private TrashedResourceManager trashedResourceManager;

    @Autowired
    private ResourceManager resourceManager;

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
            logger.error("Error reading the list of content versions for content {}", contentId, e);
            throw new RestServerError(String.format("Error while getting content versions for content %s", contentId),
                    e);
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
            throw new RestServerError(String.format("Error while getting content versions for request %s", requestList),
                    e);
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
            return versioningManager.getVersion(versionId);
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

    public ContentDto recover(Long versionId) {
        logger.debug("recover versionId {}", versionId);
        Content content;
        String lastVersionCode;
        try {
            ContentVersion contentVersion = getContentVersion(versionId);
            List<String> trashedResources = getTrashedResources(versionId);
            if (null != trashedResources && trashedResources.size() > 0) {
                for (String resourceId : trashedResources) {
                    trashedResourceManager.restoreResource(resourceId);
                }
            }
            content = versioningManager.getContent(contentVersion);
            final String contentId = contentVersion.getContentId();
            ContentRecordVO currentRecordVo = contentManager.loadContentVO(contentId);
            if (null != currentRecordVo) {
                lastVersionCode = currentRecordVo.getVersion();

            } else {
                ContentVersion lastVersion = versioningManager.getLastVersion(contentId);
                lastVersionCode = lastVersion.getVersion();
            }

            content.setVersion(lastVersionCode);
            contentManager.saveContent(content);
        } catch (Exception e) {
            logger.error("Error recovering version " + versionId, e);
            throw new RestServerError(String.format("Error recovering version %s", versionId), e);
        }
        return contentService.getDtoBuilder().convert(content);
    }

    public List<String> getTrashedResources(Long versionId) {
        try {
            List<String> trashedResourcesId = null;
            ContentVersion contentVersion = getContentVersion(versionId);
            String contentXml = contentVersion.getXml();
            Document doc = loadContentDocumentDOM(contentXml);
            List<String> resourceIds = loadResourcesIdFromContentDocumentDOM(doc);
            List<String> archivedResources = getArchivedResourcesId(resourceIds);
            if (null != resourceIds && resourceIds.size() > 0) {
                trashedResourcesId = getTrashedResourcesId(resourceIds, archivedResources);
            }
            return trashedResourcesId;
        } catch (Exception e) {
            logger.error("Error loading trashed resources for content with version {}", versionId, e);
            throw new RestServerError(
                    String.format("Error loading trashed resources for content with version %s", versionId), e);
        }

    }

    private List<String> getArchivedResourcesId(List<String> resourceIds) {
        List<String> archivedResources = null;
        if (null != resourceIds) {
            Iterator<String> it = resourceIds.iterator();
            String id;
            while (it.hasNext()) {
                ResourceInterface resourceInterface;
                id = it.next();
                try {
                    resourceInterface = resourceManager.loadResource(id);
                    if (null != resourceInterface) {
                        if (null == archivedResources) {
                            archivedResources = new ArrayList<>();
                        }
                        archivedResources.add(id);
                    }
                } catch (Throwable t) {
                    logger.error("Error checking resource " + id, t);
                    throw new RestServerError(String.format("checking resource %s", id), t);
                }
            }
        }
        return archivedResources;
    }

    private List<String> getTrashedResourcesId(List<String> resourceIds, List<String> archivedResources) {
        List<String> trashedResources = null;
        Iterator<String> it = resourceIds.iterator();
        String id;
        while (it.hasNext()) {
            id = it.next();
            if (null == archivedResources || !archivedResources.contains(id)) {
                ResourceInterface resourceInterface;
                try {
                    resourceInterface = trashedResourceManager.loadTrashedResource(id);
                    if (null != resourceInterface) {
                        if (null == trashedResources) {
                            trashedResources = new ArrayList<>();
                        }
                        trashedResources.add(id);
                    }
                } catch (Throwable t) {
                    logger.error("Error checking resource " + id, t);
                    throw new RestServerError(String.format("Error checking resource %s", id), t);
                }
            }
        }
        return trashedResources;
    }

    private List<String> loadResourcesIdFromContentDocumentDOM(Document doc) {

        List<String> resourcesId = null;
        NodeList listaNodiAttributi = doc.getElementsByTagName(RESOURCE);
        for (int i = 0; i < listaNodiAttributi.getLength(); i++) {
            Node node = listaNodiAttributi.item(i);
            NamedNodeMap namedNodeMap = node.getAttributes();
            if (null == resourcesId) {
                resourcesId = new ArrayList<>();
            }
            resourcesId.add(namedNodeMap.getNamedItem(RESOURCE_ID).getNodeValue());
        }
        return resourcesId;
    }

    private Document loadContentDocumentDOM(String contentXml)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = fact.newDocumentBuilder();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(contentXml.getBytes("UTF-8"));
        Document doc = builder.parse(byteArrayInputStream);
        return doc;
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
