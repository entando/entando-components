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
package com.agiletec.plugins.jacms.aps.system.services.content;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentAuthorizationHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.category.CategoryServiceUtilizer;
import org.entando.entando.aps.system.services.entity.AbstractEntityService;
import org.entando.entando.aps.system.services.group.GroupServiceUtilizer;
import static org.entando.entando.aps.system.services.page.IPageService.STATUS_DRAFT;
import static org.entando.entando.aps.system.services.page.IPageService.STATUS_ONLINE;
import org.entando.entando.aps.system.services.page.PageServiceUtilizer;
import org.entando.entando.plugins.jacms.web.content.ContentController;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

public class ContentService extends AbstractEntityService<Content>
        implements IContentService, GroupServiceUtilizer<ContentDto>, CategoryServiceUtilizer<ContentDto>, PageServiceUtilizer<ContentDto>, ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ERRCODE_CONTENT_NOT_FOUND = "1";
    private static final String ERRCODE_STATUS_INVALID = "3";
    private static final String ERRCODE_CONTENT_REFERENCES = "5";

    private IContentManager contentManager;
    private IContentAuthorizationHelper contentAuthorizationHelper;
    private ApplicationContext applicationContext;
    private IDtoBuilder<Content, ContentDto> dtoBuilder;

    protected IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    protected IContentAuthorizationHelper getContentAuthorizationHelper() {
        return contentAuthorizationHelper;
    }

    public void setContentAuthorizationHelper(IContentAuthorizationHelper contentAuthorizationHelper) {
        this.contentAuthorizationHelper = contentAuthorizationHelper;
    }

    public IDtoBuilder<Content, ContentDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<Content, ContentDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    private void setUp() {
        this.setDtoBuilder(new DtoBuilder<Content, ContentDto>() {
            @Override
            protected ContentDto toDto(Content src) {
                return new ContentDto(src);
            }
        });
    }

    @Override
    public String getManagerName() {
        return ((IManager) this.getContentManager()).getName();
    }

    @Override
    public List<ContentDto> getGroupUtilizer(String groupCode) {
        try {
            List<String> contentIds = ((GroupUtilizer<String>) this.getContentManager()).getGroupUtilizers(groupCode);
            return this.buildDtoList(contentIds);
        } catch (ApsSystemException ex) {
            logger.error("Error loading content references for group {}", groupCode, ex);
            throw new RestServerError("Error loading content references for group", ex);
        }
    }

    @Override
    public List<ContentDto> getCategoryUtilizer(String categoryCode) {
        try {
            List<String> contentIds = ((CategoryUtilizer) this.getContentManager()).getCategoryUtilizers(categoryCode);
            return this.buildDtoList(contentIds);
        } catch (ApsSystemException ex) {
            logger.error("Error loading content references for category {}", categoryCode, ex);
            throw new RestServerError("Error loading content references for category", ex);
        }
    }

    @Override
    public List<ContentDto> getPageUtilizer(String pageCode) {
        try {
            List<String> contentIds = ((PageUtilizer) this.getContentManager()).getPageUtilizers(pageCode);
            return this.buildDtoList(contentIds);
        } catch (ApsSystemException ex) {
            logger.error("Error loading content references for page {}", pageCode, ex);
            throw new RestServerError("Error loading content references for page", ex);
        }
    }

    private List<ContentDto> buildDtoList(List<String> contentIds) {
        List<ContentDto> dtoList = new ArrayList<>();
        if (null != contentIds) {
            contentIds.stream().forEach(i -> {
                try {
                    dtoList.add(this.getDtoBuilder().convert(this.getContentManager().loadContent(i, true)));
                } catch (ApsSystemException e) {
                    logger.error("error loading content {}", i, e);
                }
            });
        }
        return dtoList;
    }

    @Override
    public ContentDto getContent(String code, UserDetails user) {
        ContentDto dto = (ContentDto) super.getEntity(JacmsSystemConstants.CONTENT_MANAGER, code);
        dto.setReferences(this.getReferencesInfo(dto.getId()));
        return dto;
    }

    @Override
    public ContentDto addContent(ContentDto request, UserDetails user, BindingResult bindingResult) {
        return (ContentDto) this.addEntity(JacmsSystemConstants.CONTENT_MANAGER, request, bindingResult);
    }

    @Override
    public ContentDto updateContent(ContentDto request, UserDetails user, BindingResult bindingResult) {
        return (ContentDto) super.updateEntity(JacmsSystemConstants.CONTENT_MANAGER, request, bindingResult);
    }

    @Override
    public void deleteContent(String code, UserDetails user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ContentDto updateContentStatus(String code, String status, UserDetails user) {
        try {
            Content content = this.getContentManager().loadContent(code, false);
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(code, "content");
            if (null == content) {
                throw new RestRourceNotFoundException(ERRCODE_CONTENT_NOT_FOUND, "content", code);
            }
            if (status.equals(STATUS_DRAFT) && null == this.getContentManager().loadContent(code, true)) {
                return this.getDtoBuilder().convert(content);
            }
            Content newContent = null;
            if (status.equals(STATUS_ONLINE)) {
                //need to check referenced objects
                this.getContentManager().insertOnLineContent(content);
                newContent = this.getContentManager().loadContent(code, true);
            } else if (status.equals(STATUS_DRAFT)) {
                Map<String, ContentServiceUtilizer> beans = applicationContext.getBeansOfType(ContentServiceUtilizer.class);
                if (null != beans) {
                    Iterator<ContentServiceUtilizer> iter = beans.values().iterator();
                    while (iter.hasNext()) {
                        ContentServiceUtilizer serviceUtilizer = iter.next();
                        List utilizer = serviceUtilizer.getContentUtilizer(code);
                        if (null != utilizer && utilizer.size() > 0) {
                            bindingResult.reject(ContentController.ERRCODE_REFERENCED_ONLINE_CONTENT, new String[]{code}, "content.status.invalid.online.ref");
                            throw new ValidationGenericException(bindingResult);
                        }
                    }
                }
                this.getContentManager().removeOnLineContent(content);
                newContent = this.getContentManager().loadContent(code, false);
            }
            return this.getDtoBuilder().convert(newContent);
        } catch (ValidationGenericException | RestRourceNotFoundException e) {
            throw e;
        } catch (ApsSystemException e) {
            logger.error("Error updating content {} status", code, e);
            throw new RestServerError("error in update page content", e);
        }
    }

    @Override
    public PagedMetadata<?> getContentReferences(String code, String managerName, UserDetails user, RestListRequest requestList) {
        try {
            Content content = this.getContentManager().loadContent(code, false);
            if (null == content) {
                logger.warn("no content found with code {}", code);
                throw new RestRourceNotFoundException(ERRCODE_CONTENT_NOT_FOUND, "content", code);
            }
            ContentServiceUtilizer<?> utilizer = this.getContentServiceUtilizer(managerName);
            if (null == utilizer) {
                logger.warn("no references found for {}", managerName);
                throw new RestRourceNotFoundException(ERRCODE_CONTENT_REFERENCES, "reference", managerName);
            }
            List<?> dtoList = utilizer.getContentUtilizer(code);
            List<?> subList = requestList.getSublist(dtoList);
            SearcherDaoPaginatedResult<?> pagedResult = new SearcherDaoPaginatedResult(dtoList.size(), subList);
            PagedMetadata<Object> pagedMetadata = new PagedMetadata<>(requestList, pagedResult);
            pagedMetadata.setBody((List<Object>) subList);
            return pagedMetadata;
        } catch (ApsSystemException ex) {
            logger.error("Error extracting content references - content {} - manager {}", code, managerName, ex);
            throw new RestServerError("Error extracting content references", ex);
        }
    }

    private ContentServiceUtilizer<?> getContentServiceUtilizer(String managerName) {
        Map<String, ContentServiceUtilizer> beans = this.applicationContext.getBeansOfType(ContentServiceUtilizer.class);
        ContentServiceUtilizer defName = beans.values().stream()
                .filter(service -> service.getManagerName().equals(managerName))
                .findFirst().orElse(null);
        return defName;
    }

    @Override
    protected void addEntity(IEntityManager entityManager, Content entityToAdd) {
        this.updateEntity(entityManager, entityToAdd);
    }

    @Override
    protected void updateEntity(IEntityManager entityManager, Content entityToUpdate) {
        try {
            this.getContentManager().saveContent(entityToUpdate);
        } catch (Exception e) {
            logger.error("Error saving content", e);
            throw new RestServerError("Error saving content", e);
        }
    }

    private Map<String, Boolean> getReferencesInfo(String contentId) {
        Map<String, Boolean> references = new HashMap<>();
        try {
            String[] defNames = this.applicationContext.getBeanNamesForType(ContentUtilizer.class);
            for (String defName : defNames) {
                ContentUtilizer service = null;
                try {
                    service = this.applicationContext.getBean(defName, ContentUtilizer.class);
                } catch (Throwable t) {
                    logger.error("error in hasReferencingObjects", t);
                    service = null;
                }
                if (service != null) {
                    List<?> utilizers = service.getContentUtilizers(contentId);
                    references.put(service.getName(), (utilizers != null && !utilizers.isEmpty()));
                }
            }
        } catch (ApsSystemException ex) {
            logger.error("error loading references for content {}", contentId, ex);
            throw new RestServerError("error in getReferencesInfo ", ex);
        }
        return references;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
