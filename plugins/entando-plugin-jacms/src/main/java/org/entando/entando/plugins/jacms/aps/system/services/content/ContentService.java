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
package org.entando.entando.plugins.jacms.aps.system.services.content;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentAuthorizationHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.PublicContentAuthorizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.ContentRenderizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.IContentDispenser;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.category.CategoryServiceUtilizer;
import org.entando.entando.aps.system.services.entity.AbstractEntityService;
import org.entando.entando.aps.system.services.group.GroupServiceUtilizer;
import org.entando.entando.aps.system.services.page.PageServiceUtilizer;
import org.entando.entando.plugins.jacms.web.content.ContentController;
import org.entando.entando.plugins.jacms.web.content.validator.RestContentListRequest;
import org.entando.entando.web.common.exceptions.ResourcePermissionsException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.entity.validator.EntityValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.stream.Collectors;

import static org.entando.entando.plugins.jacms.web.content.ContentController.ERRCODE_CONTENT_NOT_FOUND;
import static org.entando.entando.plugins.jacms.web.content.ContentController.ERRCODE_CONTENT_REFERENCES;

public class ContentService extends AbstractEntityService<Content, ContentDto>
        implements IContentService,
        GroupServiceUtilizer<ContentDto>, CategoryServiceUtilizer<ContentDto>,
        PageServiceUtilizer<ContentDto>, ContentServiceUtilizer<ContentDto>,
        ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ILangManager langManager;
    private IContentManager contentManager;
    private IContentModelManager contentModelManager;
    private IAuthorizationManager authorizationManager;
    private IContentAuthorizationHelper contentAuthorizationHelper;
    private IContentDispenser contentDispenser;
    private ICmsSearchEngineManager searchEngineManager;
    private ApplicationContext applicationContext;

    public ILangManager getLangManager() {
        return langManager;
    }

    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }

    protected IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    protected IContentModelManager getContentModelManager() {
        return contentModelManager;
    }

    public void setContentModelManager(IContentModelManager contentModelManager) {
        this.contentModelManager = contentModelManager;
    }

    public IAuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }

    public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    protected IContentAuthorizationHelper getContentAuthorizationHelper() {
        return contentAuthorizationHelper;
    }

    public void setContentAuthorizationHelper(IContentAuthorizationHelper contentAuthorizationHelper) {
        this.contentAuthorizationHelper = contentAuthorizationHelper;
    }

    protected IContentDispenser getContentDispenser() {
        return contentDispenser;
    }

    public void setContentDispenser(IContentDispenser contentDispenser) {
        this.contentDispenser = contentDispenser;
    }

    protected ICmsSearchEngineManager getSearchEngineManager() {
        return searchEngineManager;
    }

    public void setSearchEngineManager(ICmsSearchEngineManager searchEngineManager) {
        this.searchEngineManager = searchEngineManager;
    }

    public IDtoBuilder<Content, ContentDto> getDtoBuilder() {
        return new DtoBuilder<Content, ContentDto>() {
            @Override
            protected ContentDto toDto(Content src) {
                return new ContentDto(src);
            }
        };
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

    @Override
    public List<ContentDto> getContentUtilizer(String contentId) {
        try {
            List<String> contentIds = ((ContentUtilizer) this.getContentManager()).getContentUtilizers(contentId);
            return this.buildDtoList(contentIds);
        } catch (ApsSystemException ex) {
            logger.error("Error loading content references for content {}", contentId, ex);
            throw new RestServerError("Error loading content references for content", ex);
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
    protected ContentDto buildEntityDto(Content entity) {
        return this.getDtoBuilder().convert(entity);
    }

    @Override
    public PagedMetadata<ContentDto> getContents(RestContentListRequest requestList, UserDetails user) {
        try {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(requestList, "content");
            boolean online = (IContentService.STATUS_ONLINE.equalsIgnoreCase(requestList.getStatus()));
            List<EntitySearchFilter> filters = requestList.buildEntitySearchFilters();
            EntitySearchFilter[] filtersArr = new EntitySearchFilter[filters.size()];
            filtersArr = filters.toArray(filtersArr);
            List<String> userGroupCodes = this.getAllowedGroups(user, online);
            List<String> result = (online)
                    ? this.getContentManager().loadPublicContentsId(requestList.getCategories(), requestList.isOrClauseCategoryFilter(), filtersArr, userGroupCodes)
                    : this.getContentManager().loadWorkContentsId(requestList.getCategories(), requestList.isOrClauseCategoryFilter(), filtersArr, userGroupCodes);
            if (!StringUtils.isBlank(requestList.getText()) && online) {
                String langCode = (StringUtils.isBlank(requestList.getLang())) ? this.getLangManager().getDefaultLang().getCode() : requestList.getLang();
                List<String> fullTextResult = this.getSearchEngineManager().searchEntityId(langCode, requestList.getText(), userGroupCodes);
                result.removeIf(i -> !fullTextResult.contains(i));
            }
            List<String> sublist = requestList.getSublist(result);
            PagedMetadata<ContentDto> pagedMetadata = new PagedMetadata<>(requestList, sublist.size());
            List<ContentDto> masterList = new ArrayList<>();
            for (String contentId : sublist) {
                masterList.add(this.buildContentDto(contentId, online,
                        requestList.getModel(), requestList.getLang(), requestList.isResolveLink(), bindingResult));
            }
            pagedMetadata.setBody(masterList);
            return pagedMetadata;
        } catch (ResourceNotFoundException | ValidationGenericException e) {
            throw e;
        } catch (Exception t) {
            logger.error("error in search contents", t);
            throw new RestServerError("error in search contents", t);
        }
    }

    protected List<String> getAllowedGroups(UserDetails currentUser, boolean requiredOnlineContents) {
        List<String> groupCodes = new ArrayList<>();
        if (null == currentUser) {
            if (requiredOnlineContents) {
                groupCodes.add(Group.FREE_GROUP_NAME);
            }
            return groupCodes;
        }
        if (requiredOnlineContents) {
            List<Group> groups = this.getAuthorizationManager().getUserGroups(currentUser);
            groupCodes.addAll(groups.stream().map(Group::getName).collect(Collectors.toList()));
            groupCodes.add(Group.FREE_GROUP_NAME);
        } else {
            List<Group> groupsByPermission = this.getAuthorizationManager().getGroupsByPermission(currentUser, Permission.CONTENT_EDITOR);
            groupCodes.addAll(groupsByPermission.stream().map(Group::getName).collect(Collectors.toList()));
        }
        return groupCodes;
    }

    @Override
    public ContentDto getContent(String code, String modelId, String status, String langCode, boolean resolveLink, UserDetails user) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(code, "content");
        boolean online = (IContentService.STATUS_ONLINE.equalsIgnoreCase(status));
        this.checkContentAuthorization(user, code, online, false, bindingResult);
        ContentDto dto = this.buildContentDto(code, online, modelId, langCode, resolveLink, bindingResult);
        dto.setReferences(this.getReferencesInfo(dto.getId()));
        return dto;
    }

    protected ContentDto buildContentDto(String code, boolean onLine,
            String modelId, String langCode, boolean resolveLink, BindingResult bindingResult) {
        ContentDto dto = null;
        try {
            Content content = this.getContentManager().loadContent(code, onLine);
            if (null == content) {
                throw new ResourceNotFoundException(EntityValidator.ERRCODE_ENTITY_DOES_NOT_EXIST, "Content", code);
            }
            dto = this.buildEntityDto(content);
        } catch (ResourceNotFoundException rnf) {
            throw rnf;
        } catch (Exception e) {
            logger.error("Error extracting content", e);
            throw new RestServerError("error extracting content", e);
        }
        dto.setHtml(this.extractRenderedContent(dto, modelId, langCode, resolveLink, bindingResult));
        dto.setReferences(this.getReferencesInfo(dto.getId()));
        return dto;
    }

    protected String extractRenderedContent(ContentDto dto, String modelId,
            String langCode, boolean resolveLink, BindingResult bindingResult) {
        String render = null;
        if (null == modelId || modelId.trim().length() == 0) {
            return null;
        }
        Integer modelIdInteger = this.checkModel(dto, modelId, bindingResult);
        if (null != modelIdInteger) {
            Lang lang = this.getLangManager().getDefaultLang();
            if (!StringUtils.isBlank(langCode)) {
                lang = this.getLangManager().getLang(langCode);
                if (null == lang) {
                    bindingResult.reject(ContentController.ERRCODE_INVALID_LANG_CODE,
                            new String[]{modelId, dto.getTypeCode()}, "plugins.jacms.content.model.invalidLangCode");
                    throw new ValidationGenericException(bindingResult);
                }
            }
            ContentRenderizationInfo renderizationInfo = this.getContentDispenser().getRenderizationInfo(dto.getId(), modelIdInteger, lang.getCode(), null, true);
            if (null != renderizationInfo) {
                if (resolveLink) {
                    this.getContentDispenser().resolveLinks(renderizationInfo, null);
                    render = renderizationInfo.getRenderedContent();
                } else {
                    render = renderizationInfo.getCachedRenderedContent();
                }
            }
        }
        return render;
    }

    protected Integer checkModel(ContentDto dto, String modelId, BindingResult bindingResult) {
        Integer modelIdInteger = null;
        if (StringUtils.isBlank(modelId)) {
            return null;
        }
        if (modelId.equals("default")) {
            if (null == dto.getDefaultModel()) {
                bindingResult.reject(ContentController.ERRCODE_INVALID_MODEL, "plugins.jacms.content.model.nullDefaultModel");
                throw new ValidationGenericException(bindingResult);
            }
            modelIdInteger = Integer.parseInt(dto.getDefaultModel());
        } else if (modelId.equals("list")) {
            if (null == dto.getListModel()) {
                bindingResult.reject(ContentController.ERRCODE_INVALID_MODEL, "plugins.jacms.content.model.nullListModel");
                throw new ValidationGenericException(bindingResult);
            }
            modelIdInteger = Integer.parseInt(dto.getListModel());
        } else {
            modelIdInteger = Integer.parseInt(modelId);
        }
        ContentModel model = this.getContentModelManager().getContentModel(modelIdInteger);
        if (model == null) {
            bindingResult.reject(ContentController.ERRCODE_INVALID_MODEL, new String[]{modelId}, "plugins.jacms.content.model.nullModel");
            throw new ValidationGenericException(bindingResult);
        } else if (!dto.getTypeCode().equals(model.getContentType())) {
            bindingResult.reject(ContentController.ERRCODE_INVALID_MODEL,
                    new String[]{modelId, dto.getTypeCode()}, "plugins.jacms.content.model.invalid");
            throw new ValidationGenericException(bindingResult);
        }
        return modelIdInteger;
    }

    @Override
    public ContentDto addContent(ContentDto request, UserDetails user, BindingResult bindingResult) {
        if (!this.getAuthorizationManager().isAuthOnGroup(user, request.getMainGroup())) {
            bindingResult.reject(ContentController.ERRCODE_UNAUTHORIZED_CONTENT, new String[]{request.getMainGroup()}, "plugins.jacms.content.group.unauthorized");
            throw new ResourcePermissionsException(bindingResult);
        }
        request.setId(null);
        return this.addEntity(JacmsSystemConstants.CONTENT_MANAGER, request, bindingResult);
    }

    @Override
    public ContentDto updateContent(ContentDto request, UserDetails user, BindingResult bindingResult) {
        this.checkContentExists(request.getId());
        this.checkContentAuthorization(user, request.getId(), false, true, bindingResult);
        return super.updateEntity(JacmsSystemConstants.CONTENT_MANAGER, request, bindingResult);
    }

    @Override
    public void deleteContent(String code, UserDetails user) {
        this.checkContentAuthorization(user, code, false, true, null);
        try {
            Content content = this.getContentManager().loadContent(code, false);
            if (null == content) {
                throw new ResourceNotFoundException(ERRCODE_CONTENT_NOT_FOUND, "content", code);
            }
            Content publicContent = this.getContentManager().loadContent(code, true);
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(code, "content");
            if (null != publicContent) {
                bindingResult.reject(ContentController.ERRCODE_DELETE_PUBLIC_PAGE,
                        new String[]{code}, "plugins.jacms.content.status.published");
                throw new ValidationGenericException(bindingResult);
            }
            this.getContentManager().deleteContent(content);
        } catch (ValidationGenericException e) {
            throw e;
        } catch (ApsSystemException e) {
            logger.error("Error deleting content {}", code, e);
            throw new RestServerError("error deleting content", e);
        }
    }

    @Override
    public ContentDto updateContentStatus(String code, String status, UserDetails user) {
        try {
            Content content = this.getContentManager().loadContent(code, false);
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(code, "content");
            if (null == content) {
                throw new ResourceNotFoundException(ERRCODE_CONTENT_NOT_FOUND, "content", code);
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
                            bindingResult.reject(ContentController.ERRCODE_REFERENCED_ONLINE_CONTENT, new String[]{code}, "plugins.jacms.content.status.invalid.online.ref");
                            throw new ValidationGenericException(bindingResult);
                        }
                    }
                }
                this.getContentManager().removeOnLineContent(content);
                newContent = this.getContentManager().loadContent(code, false);
            }
            return this.getDtoBuilder().convert(newContent);
        } catch (ValidationGenericException | ResourceNotFoundException e) {
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
                throw new ResourceNotFoundException(ERRCODE_CONTENT_NOT_FOUND, "content", code);
            }
            ContentServiceUtilizer<?> utilizer = this.getContentServiceUtilizer(managerName);
            if (null == utilizer) {
                logger.warn("no references found for {}", managerName);
                throw new ResourceNotFoundException(ERRCODE_CONTENT_REFERENCES, "reference", managerName);
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
    protected Content addEntity(IEntityManager entityManager, Content entityToAdd) {
        return this.updateEntity(entityManager, entityToAdd);
    }

    @Override
    protected Content updateEntity(IEntityManager entityManager, Content entityToUpdate) {
        try {
            this.getContentManager().saveContent(entityToUpdate);
            return this.getContentManager().loadContent(entityToUpdate.getId(), false);
        } catch (Exception e) {
            logger.error("Error saving content", e);
            throw new RestServerError("Error saving content", e);
        }
    }

    private Map<String, Boolean> getReferencesInfo(String contentId) {
        Map<String, Boolean> references = new HashMap<>();
        Map<String, ContentServiceUtilizer> beans = this.applicationContext.getBeansOfType(ContentServiceUtilizer.class);
        beans.values().stream().forEach(service -> {
            List<?> utilizers = service.getContentUtilizer(contentId);
            references.put(service.getManagerName(), (utilizers != null && !utilizers.isEmpty()));
        });
        return references;
    }

    protected void checkContentAuthorization(UserDetails userDetails, String contentId, boolean publicVersion, boolean edit, BindingResult mainBindingResult) {
        try {
            PublicContentAuthorizationInfo pcai = (publicVersion) ? this.getContentAuthorizationHelper().getAuthorizationInfo(contentId) : null;
            if (publicVersion && null == pcai) {
                throw new ResourceNotFoundException(ERRCODE_CONTENT_NOT_FOUND, "content", contentId);
            }
            List<String> userGroupCodes = new ArrayList<>();
            List<Group> groups = (null != userDetails) ? this.getAuthorizationManager().getUserGroups(userDetails) : new ArrayList<>();
            userGroupCodes.addAll(groups.stream().map(Group::getName).collect(Collectors.toList()));
            userGroupCodes.add(Group.FREE_GROUP_NAME);
            if (!(publicVersion && !edit && null != pcai && pcai.isUserAllowed(userGroupCodes))
                    && !this.getContentAuthorizationHelper().isAuthToEdit(userDetails, contentId, publicVersion)) {
                BindingResult bindingResult = (null == mainBindingResult) ? new BeanPropertyBindingResult(contentId, "content") : mainBindingResult;
                bindingResult.reject(ContentController.ERRCODE_UNAUTHORIZED_CONTENT, new String[]{contentId}, "plugins.jacms.content.unauthorized.access");
                throw new ResourcePermissionsException(bindingResult);
            }
        } catch (ResourceNotFoundException | ResourcePermissionsException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("error checking auth for content {}", contentId, ex);
            throw new RestServerError("error checking auth for content", ex);
        }
    }

    protected void checkContentExists(String code) {
        try {
            if (null == getContentManager().loadContent(code, false)) {
                logger.error("Content not found: " + code);
                throw new ResourceNotFoundException(ERRCODE_CONTENT_NOT_FOUND, "content", code);
            }
        } catch (ApsSystemException ex) {
            throw new RestServerError("plugins.jacms.content.contentManager.error.read", null);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
