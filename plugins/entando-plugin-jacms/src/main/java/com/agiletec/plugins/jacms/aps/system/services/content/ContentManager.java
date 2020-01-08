/*
* Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import com.agiletec.aps.system.*;
import com.agiletec.aps.system.common.entity.*;
import com.agiletec.aps.system.common.entity.model.*;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.model.*;
import com.agiletec.plugins.jacms.aps.system.services.resource.ResourceUtilizer;
import org.entando.entando.aps.system.services.cache.*;
import org.slf4j.*;
import org.springframework.cache.annotation.*;

import java.util.*;

/**
 * Contents manager. This implements all the methods needed to create and manage
 * the contents.
 */
public class ContentManager extends ApsEntityManager
                            implements IContentManager, GroupUtilizer<String>, PageUtilizer, ContentUtilizer, ResourceUtilizer, CategoryUtilizer {

    private static final Logger logger = LoggerFactory.getLogger(ContentManager.class);
    private static final String ERROR_WHILE_LOADING_CONTENTS = "Error while loading contents";

    private IContentDAO contentDAO;

    private IContentSearcherDAO workContentSearcherDAO;

    private IContentSearcherDAO publicContentSearcherDAO;

    private IContentUpdaterService contentUpdaterService;

    @Override
    protected String getConfigItemName() {
        return JacmsSystemConstants.CONFIG_ITEM_CONTENT_TYPES;
    }

    /**
     * Create a new instance of the requested content. The new content is forked
     * (or cloned) from the corresponding prototype, and it's returned empty.
     *
     * @param typeCode The code of the requested (proto)type, as declared in the
     * configuration.
     * @return The new content.
     */
    @Override
    public Content createContentType(String typeCode) {
        return (Content) super.getEntityPrototype(typeCode);
    }

    /**
     * Return a list of the of the content types in a 'small form'. 'Small form'
     * mans that the contents returned are purged from all unnecessary
     * information (eg. attributes).
     *
     * @return The list of the types in a (small form).
     * @deprecated From Entando 4.1.2, use getSmallEntityTypes() method
     */
    @Override
    public List<SmallContentType> getSmallContentTypes() {
        List<SmallContentType> smallContentTypes = new ArrayList<>(getSmallContentTypesMap().values());
        Collections.sort(smallContentTypes);
        return smallContentTypes;
    }

    /**
     * Return the map of the prototypes of the contents types. Return a map,
     * index by the code of the type, of the prototypes of the available content
     * types.
     *
     * @return The map of the prototypes of the content types in a
     * 'SmallContentType' objects.
     */
    @Override
    public Map<String, SmallContentType> getSmallContentTypesMap() {
        Map<String, SmallContentType> smallContentTypes = new HashMap<>();
        List<SmallEntityType> entityTypes = super.getSmallEntityTypes();
        for (SmallEntityType entityType : entityTypes) {
            SmallContentType sct = new SmallContentType();
            sct.setCode(entityType.getCode());
            sct.setDescription(entityType.getDescription());
            smallContentTypes.put(entityType.getCode(), sct);
        }
        return smallContentTypes;
    }

    /**
     * Return the code of the default page used to display the given content.
     * The default page is defined at content type level; the type is
     * extrapolated from the code built following the conventions.
     *
     * @param contentId The content ID
     * @return The page code.
     */
    @Override
    public String getViewPage(String contentId) {
        Content type = this.getTypeById(contentId);
        return type.getViewPage();
    }

    /**
     * Return the code of the default model of content.
     *
     * @param contentId The content code
     * @return Il requested model code
     */
    @Override
    public String getDefaultModel(String contentId) {
        Content type = this.getTypeById(contentId);
        return type.getDefaultModel();
    }

    /**
     * Return the code of the model to be used when the content is rendered in
     * list
     *
     * @param contentId The code of the content
     * @return The code of the model
     */
    @Override
    public String getListModel(String contentId) {
        Content type = this.getTypeById(contentId);
        return type.getListModel();
    }

    /**
     * Return a complete content given its ID; it is possible to choose to
     * return the published -unmodifiable!- content or the working copy. It also
     * returns the data in the form of XML.
     *
     * @param id The ID of the content
     * @param onLine Specifies the type of the content to return: 'true'
     * references the published content, 'false' the freely modifiable one.
     * @return The requested content.
     * @throws ApsSystemException In case of error.
     */
    @Override
    public Content loadContent(String id, boolean onLine) throws ApsSystemException {
        try {
            ContentRecordVO contentVo = this.loadContentVO(id);
            return this.createContent(contentVo, onLine);
        } catch (ApsSystemException e) {
            logger.error("Error while loading content : id {}", id, e);
            throw new ApsSystemException("Error while loading content : id " + id, e);
        }
    }

    protected Content createContent(ContentRecordVO contentVo, boolean onLine) throws ApsSystemException {
        Content content = null;
        try {
            if (contentVo != null) {
                String xmlData;
                if (onLine) {
                    xmlData = contentVo.getXmlOnLine();
                } else {
                    xmlData = contentVo.getXmlWork();
                }
                if (xmlData != null) {
                    content = (Content) this.createEntityFromXml(contentVo.getTypeCode(), xmlData);
                    content.setId(contentVo.getId());
                    content.setTypeCode(contentVo.getTypeCode());
                    content.setDescription(contentVo.getDescription());
                    content.setOnLine(contentVo.isOnLine());
                    content.setMainGroup(contentVo.getMainGroupCode());
                    content.setSync(contentVo.isSync());
                    content.setStatus(contentVo.getStatus());
                    if (null == content.getVersion()) {
                        content.setVersion(contentVo.getVersion());
                    }
                    if (null == content.getFirstEditor()) {
                        content.setFirstEditor(contentVo.getFirstEditor());
                    }
                    if (null == content.getLastEditor()) {
                        content.setLastEditor(contentVo.getLastEditor());
                    }
                    if (null == content.getRestriction()) {
                        content.setRestriction(contentVo.getRestriction());
                    }
                    if (null == content.getCreated()) {
                        content.setCreated(contentVo.getCreate());
                    }
                    if (null == content.getLastModified()) {
                        content.setLastModified(contentVo.getModify());
                    }
                    if (null == content.getPublished()) {
                        content.setPublished(contentVo.getPublish());
                    }
                }
            }
        } catch (ApsSystemException e) {
            logger.error("Error while creating content by vo", e);
            throw new ApsSystemException("Error while creating content by vo", e);
        }
        return content;
    }

    /**
     * Return a {@link ContentRecordVO} (shortly: VO) containing the all content
     * informations stored in the DB.
     *
     * @param id The id of the requested content.
     * @return The VO object corresponding to the wanted content.
     * @throws ApsSystemException in case of error.
     */
    @Override
    public ContentRecordVO loadContentVO(String id) throws ApsSystemException {
        try {
            return (ContentRecordVO) this.getContentDAO().loadEntityRecord(id);
        } catch (Throwable t) {
            logger.error("Error while loading content vo : id {}", id, t);
            throw new ApsSystemException("Error while loading content vo : id " + id, t);
        }
    }

    /**
     * Save a content in the DB.
     *
     * @param content The content to add.
     * @throws ApsSystemException in case of error.
     */
    @Override
    public void saveContent(Content content) throws ApsSystemException {
        this.addContent(content);
    }

    @Override
    public void saveContentAndContinue(Content content) throws ApsSystemException {
        this.addUpdateContent(content, false);
    }

    /**
     * Save a content in the DB. Hopefully this method has no annotation
     * attached
     */
    @Override
    public void addContent(Content content) throws ApsSystemException {
        this.addUpdateContent(content, true);
    }

    private void addUpdateContent(Content content, boolean updateDate) throws ApsSystemException {
        try {
            content.setLastModified(new Date());
            if (updateDate) {
                content.incrementVersion(false);
            }

            String status = content.getStatus();
            if (null == status || status.equals(Content.STATUS_PUBLIC)) {
                content.setStatus(Content.STATUS_DRAFT);
            } else {
                content.setStatus(status);
            }

            if (null == content.getId()) {
                IKeyGeneratorManager keyGenerator = (IKeyGeneratorManager) this.getService(SystemConstants.KEY_GENERATOR_MANAGER);
                int key = keyGenerator.getUniqueKeyCurrentValue();
                String id = content.getTypeCode() + key;
                content.setId(id);
                this.getContentDAO().addEntity(content);
            } else {
                this.getContentDAO().updateContent(content, updateDate);
            }
        } catch (Throwable t) {
            logger.error("Error while saving content", t);
            throw new ApsSystemException("Error while saving content", t);
        }
    }

    /**
     * Publish a content.
     *
     * @param content The ID associated to the content to be displayed in the
     * portal.
     * @throws ApsSystemException in case of error.
     */
    @Override
    @CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
            key = "T(com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants).CONTENT_CACHE_PREFIX.concat(#content.id)", condition = "#content.id != null")
    @CacheInfoEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
            groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentCacheGroupsToEvictCsv(#content.id, #content.typeCode)")
    public void insertOnLineContent(Content content) throws ApsSystemException {
        try {
            content.setLastModified(new Date());
            if (null == content.getId()) {
                content.setCreated(new Date());
                this.saveContent(content);
            }
            content.incrementVersion(true);
            content.setStatus(Content.STATUS_PUBLIC);
            this.getContentDAO().insertOnLineContent(content);
            int operationEventCode;
            if (content.isOnLine()) {
                operationEventCode = PublicContentChangedEvent.UPDATE_OPERATION_CODE;
            } else {
                operationEventCode = PublicContentChangedEvent.INSERT_OPERATION_CODE;
            }
            this.notifyPublicContentChanging(content, operationEventCode);
        } catch (Throwable t) {
            logger.error("Error while inserting content on line", t);
            throw new ApsSystemException("Error while inserting content on line", t);
        }
    }

    /**
     * Return the list of all the content IDs.
     *
     * @return The list of all the content IDs.
     * @throws ApsSystemException In case of error
     * @deprecated Since Entando 2.0 version 2.0.9, use
     * searchId(EntitySearchFilter[]) method
     */
    @Override
    @Deprecated
    public List<String> getAllContentsId() throws ApsSystemException {
        return super.getAllEntityId();
    }

    @Override
    public void reloadEntityReferences(String entityId) {
        try {
            ContentRecordVO contentVo = this.loadContentVO(entityId);
            Content content = this.createContent(contentVo, true);
            if (content != null) {
                this.getContentDAO().reloadPublicContentReferences(content);
            }
            Content workcontent = this.createContent(contentVo, false);
            if (workcontent != null) {
                this.getContentDAO().reloadWorkContentReferences(workcontent);
            }
            logger.debug("Reloaded content references for content {}", entityId);
        } catch (Throwable t) {
            logger.error("Error while reloading content references for content {}", entityId, t);
        }
    }

    /**
     * Unpublish a content, preventing it from being displayed in the portal.
     * Obviously the content itself is not deleted.
     *
     * @param content the content to unpublish.
     * @throws ApsSystemException in case of error
     */
    @Override
    @CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
            key = "T(com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants).CONTENT_CACHE_PREFIX.concat(#content.id)", condition = "#content.id != null")
    @CacheInfoEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
            groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentCacheGroupsToEvictCsv(#content.id, #content.typeCode)")
    public void removeOnLineContent(Content content) throws ApsSystemException {
        try {
            content.setLastModified(new Date());
            content.incrementVersion(false);
            if (null != content.getStatus() && content.getStatus().equals(Content.STATUS_PUBLIC)) {
                content.setStatus(Content.STATUS_DRAFT);
            }
            this.getContentDAO().removeOnLineContent(content);
            this.notifyPublicContentChanging(content, PublicContentChangedEvent.REMOVE_OPERATION_CODE);
        } catch (Throwable t) {
            logger.error("Error while removing onLine content", t);
            throw new ApsSystemException("Error while removing onLine content", t);
        }
    }

    /**
     * Notify the modification of a published content.
     *
     * @param content The modified content.
     * @param operationCode the operation code to notify.
     */
    private void notifyPublicContentChanging(Content content, int operationCode) {
        PublicContentChangedEvent event = new PublicContentChangedEvent();
        event.setContent(content);
        event.setOperationCode(operationCode);
        this.notifyEvent(event);
    }

    /**
     * Return the content type from the given ID code. The code is extracted
     * following the coding conventions: the first three characters are the type
     * of the code.
     *
     * @param contentId the content ID whose content type is extracted.
     * @return The content type requested
     */
    private Content getTypeById(String contentId) {
        String typeCode = contentId.substring(0, 3);
        return (Content) super.getEntityTypes().get(typeCode);
    }

    /**
     * Deletes a content from the DB.
     *
     * @param content The content to delete.
     * @throws ApsSystemException in case of error.
     */
    @Override
    @CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
            key = "T(com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants).CONTENT_CACHE_PREFIX.concat(#content.id)", condition = "#content.id != null")
    @CacheInfoEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
            groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentCacheGroupsToEvictCsv(#content.id, #content.typeCode)")
    public void deleteContent(Content content) throws ApsSystemException {
        try {
            this.getContentDAO().deleteEntity(content.getId());
        } catch (Throwable t) {
            logger.error("Error while deleting content {}", content.getId(), t);
            throw new ApsSystemException("Error while deleting content " + content.getId(), t);
        }
    }

    @Override
    public List<String> loadPublicContentsId(String contentType, String[] categories, EntitySearchFilter[] filters,
            Collection<String> userGroupCodes) throws ApsSystemException {
        return this.loadPublicContentsId(contentType, categories, false, filters, userGroupCodes);
    }

    @Override
    public List<String> loadPublicContentsId(String contentType, String[] categories, boolean orClauseCategoryFilter,
            EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
        try {
            return this.getPublicContentSearcherDAO().loadContentsId(contentType, categories, orClauseCategoryFilter, filters, userGroupCodes);
        } catch (Throwable t) {
            logger.error(ERROR_WHILE_LOADING_CONTENTS, t);
            throw new ApsSystemException(ERROR_WHILE_LOADING_CONTENTS, t);
        }
    }

    @Override
    public List<String> loadPublicContentsId(String[] categories,
            EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
        return this.loadPublicContentsId(categories, false, filters, userGroupCodes);
    }

    @Override
    public List<String> loadPublicContentsId(String[] categories, boolean orClauseCategoryFilter,
            EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
        try {
            return this.getPublicContentSearcherDAO().loadContentsId(categories, orClauseCategoryFilter, filters, userGroupCodes);
        } catch (Throwable t) {
            logger.error(ERROR_WHILE_LOADING_CONTENTS, t);
            throw new ApsSystemException(ERROR_WHILE_LOADING_CONTENTS, t);
        }
    }

    @Override
    public List<String> loadWorkContentsId(EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
        return this.loadWorkContentsId(null, false, filters, userGroupCodes);
    }

    @Override
    public List<String> loadWorkContentsId(String[] categories, EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
        return this.loadWorkContentsId(categories, false, filters, userGroupCodes);
    }

    @Override
    public List<String> loadWorkContentsId(String[] categories, boolean orClauseCategoryFilter,
            EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
        try {
            return this.getWorkContentSearcherDAO().loadContentsId(categories, orClauseCategoryFilter, filters, userGroupCodes);
        } catch (Throwable t) {
            logger.error("Error while loading work contents", t);
            throw new ApsSystemException("Error while loading work contents", t);
        }
    }

    @Override
    public SearcherDaoPaginatedResult<String> getPaginatedWorkContentsId(String[] categories, boolean orClauseCategoryFilter, EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
        return this.getPaginatedContentsId(categories, orClauseCategoryFilter, filters, userGroupCodes, this.getWorkContentSearcherDAO());
    }

    @Override
    public SearcherDaoPaginatedResult<String> getPaginatedPublicContentsId(String[] categories, boolean orClauseCategoryFilter, EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
        return this.getPaginatedContentsId(categories, orClauseCategoryFilter, filters, userGroupCodes, this.getPublicContentSearcherDAO());
    }
    
    private SearcherDaoPaginatedResult<String> getPaginatedContentsId(String[] categories, boolean orClauseCategoryFilter, 
            EntitySearchFilter[] filters, Collection<String> userGroupCodes, IContentSearcherDAO searcherDao) throws ApsSystemException {
        SearcherDaoPaginatedResult<String> pagedResult = null;
        try {
            int count = searcherDao.countContents(categories, orClauseCategoryFilter, filters, userGroupCodes);
            List<String> contentsId = searcherDao.loadContentsId(categories, orClauseCategoryFilter, filters, userGroupCodes);
            pagedResult = new SearcherDaoPaginatedResult<>(count, contentsId);
        } catch (Throwable t) {
            logger.error("Error searching paginated contents id", t);
            throw new ApsSystemException("Error searching paginated contents id", t);
        }
        return pagedResult;
    }
    
    @Override
    public List getPageUtilizers(String pageCode) throws ApsSystemException {
        try {
            return this.getContentDAO().getPageUtilizers(pageCode);
        } catch (Throwable t) {
            throw new ApsSystemException("Error while loading referenced contents : page " + pageCode, t);
        }
    }

    @Override
    public List getContentUtilizers(String contentId) throws ApsSystemException {
        try {
            return this.getContentDAO().getContentUtilizers(contentId);
        } catch (Throwable t) {
            throw new ApsSystemException("Error while loading referenced contents : content " + contentId, t);
        }
    }

    @Override
    public List<String> getGroupUtilizers(String groupName) throws ApsSystemException {
        try {
            return this.getContentDAO().getGroupUtilizers(groupName);
        } catch (Throwable t) {
            throw new ApsSystemException("Error while loading referenced contents : group " + groupName, t);
        }
    }

    @Override
    public List getResourceUtilizers(String resourceId) throws ApsSystemException {
        try {
            return this.getContentDAO().getResourceUtilizers(resourceId);
        } catch (Throwable t) {
            throw new ApsSystemException("Error while loading referenced contents : resource " + resourceId, t);
        }
    }

    @Override
    public List getCategoryUtilizers(String resourceId) throws ApsSystemException {
        try {
            return this.getContentDAO().getCategoryUtilizers(resourceId);
        } catch (Throwable t) {
            throw new ApsSystemException("Error while loading referenced contents : category " + resourceId, t);
        }
    }

    @Override
    public void reloadCategoryReferences(String categoryCode) {
        try {
            this.getContentUpdaterService().reloadCategoryReferences(categoryCode);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "reloadCategoryReferences");
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getCategoryUtilizersForReloadReferences(String categoryCode) {
        List<String> contentIdToReload = new ArrayList<>();
        try {
            Set<String> contents = this.getContentUpdaterService().getContentsId(categoryCode);
            if (null != contents) {
                contentIdToReload.addAll(contents);
            }
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getCategoryUtilizersForReloadReferences");
        }
        return contentIdToReload;
    }

    @Override
    public boolean isSearchEngineUser() {
        return true;
    }

    @Override
    public ContentsStatus getContentsStatus() {
        ContentsStatus status = null;
        try {
            status = this.getContentDAO().loadContentStatus();
        } catch (Throwable t) {
            logger.error("error in getContentsStatus", t);
        }
        return status;
    }

    /**
     * Return the DAO which handles all the operations on the contents.
     *
     * @return The DAO managing the contents.
     */
    protected IContentDAO getContentDAO() {
        return contentDAO;
    }

    /**
     * Set the DAO which handles the operations on the contents.
     *
     * @param contentDao The DAO managing the contents.
     */
    public void setContentDAO(IContentDAO contentDao) {
        this.contentDAO = contentDao;
    }

    @Override
    protected IEntitySearcherDAO getEntitySearcherDao() {
        return this.getWorkContentSearcherDAO();
    }

    @Override
    protected IEntityDAO getEntityDao() {
        return this.getContentDAO();
    }

    protected IContentSearcherDAO getWorkContentSearcherDAO() {
        return workContentSearcherDAO;
    }

    public void setWorkContentSearcherDAO(IContentSearcherDAO workContentSearcherDAO) {
        this.workContentSearcherDAO = workContentSearcherDAO;
    }

    public IContentSearcherDAO getPublicContentSearcherDAO() {
        return publicContentSearcherDAO;
    }

    public void setPublicContentSearcherDAO(IContentSearcherDAO publicContentSearcherDAO) {
        this.publicContentSearcherDAO = publicContentSearcherDAO;
    }

    protected IContentUpdaterService getContentUpdaterService() {
        return contentUpdaterService;
    }

    public void setContentUpdaterService(IContentUpdaterService contentUpdaterService) {
        this.contentUpdaterService = contentUpdaterService;
    }

    @Override
    public IApsEntity getEntity(String entityId) throws ApsSystemException {
        return this.loadContent(entityId, false);
    }

    /**
     * @deprecated From jAPS 2.0 version 2.0.9, use getStatus()
     */
    @Override
    @Deprecated
    public int getState() {
        return super.getStatus();
    }

}
