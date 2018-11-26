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
package org.entando.entando.plugins.jacms.aps.system.services;

import com.agiletec.aps.system.common.entity.model.*;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.*;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.dictionary.ContentModelDictionaryProvider;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.*;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.utils.ContentModelUtils;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.*;
import org.entando.entando.aps.system.services.*;
import org.entando.entando.aps.system.services.dataobjectmodel.model.IEntityModelDictionary;
import org.entando.entando.plugins.jacms.web.contentmodel.validator.ContentModelValidator;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.*;

@Service
public class ContentModelServiceImpl implements ContentModelService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IContentManager contentManager;
    private final IContentModelManager contentModelManager;
    private final ContentModelDictionaryProvider dictionaryProvider;
    private final IDtoBuilder<ContentModel, ContentModelDto> dtoBuilder;

    @Autowired
    public ContentModelServiceImpl(IContentManager contentManager, IContentModelManager contentModelManager, ContentModelDictionaryProvider dictionaryProvider) {
        this.contentManager = contentManager;
        this.contentModelManager = contentModelManager;
        this.dictionaryProvider = dictionaryProvider;

        this.dtoBuilder = new DtoBuilder<ContentModel, ContentModelDto>() {

            @Override
            protected ContentModelDto toDto(ContentModel src) {
                ContentModelDto dto = new ContentModelDto();
                dto.setContentShape(src.getContentShape());
                dto.setContentType(src.getContentType());
                dto.setDescr(src.getDescription());
                dto.setId(src.getId());
                dto.setStylesheet(src.getStylesheet());
                return dto;
            }
        };
    }

    @Override
    public PagedMetadata<ContentModelDto> findMany(RestListRequest requestList) {

        List<ContentModel> contentModels = this.contentModelManager.getContentModels();

        Stream<ContentModel> stream = contentModels.stream();

        //filter
        List<Predicate<ContentModel>> filters = ContentModelUtils.getPredicates(requestList);
        for (Predicate<ContentModel> predicate : filters) {
            stream = stream.filter(predicate);
        }

        //sort
        Comparator<ContentModel> comparator = ContentModelUtils.getComparator(requestList.getSort(), requestList.getDirection());
        if (null != comparator) {
            stream = stream.sorted(comparator);
        }

        contentModels = stream.collect(Collectors.toList());

        //page
        List<ContentModel> subList = requestList.getSublist(contentModels);
        List<ContentModelDto> dtoSlice = this.dtoBuilder.convert(subList);
        SearcherDaoPaginatedResult<ContentModelDto> paginatedResult = new SearcherDaoPaginatedResult(contentModels.size(), dtoSlice);
        PagedMetadata<ContentModelDto> pagedMetadata = new PagedMetadata<>(requestList, paginatedResult);
        pagedMetadata.setBody(dtoSlice);
        return pagedMetadata;
    }

    @Override
    public ContentModelDto getContentModel(Long modelId) {
        ContentModel contentModel = this.contentModelManager.getContentModel(modelId);
        if (null == contentModel) {
            logger.warn("no contentModel found with id {}", modelId);
            throw new RestRourceNotFoundException(ContentModelValidator.ERRCODE_CONTENTMODEL_NOT_FOUND, "contentModel", String.valueOf(modelId));
        }
        ContentModelDto dto = this.dtoBuilder.convert(contentModel);
        return dto;
    }

    @Override
    public Optional<ContentModelDto> findById(Long modelId) {
        ContentModelDto dto = null;
        ContentModel contentModel = this.contentModelManager.getContentModel(modelId);
        if (contentModel != null) {
            dto = this.dtoBuilder.convert(contentModel);
        }
        return Optional.ofNullable(dto);
    }

    @Override
    public ContentModelDto create(ContentModelDto entity) {
        try {
            ContentModel contentModel = this.createContentModel(entity);
            BeanPropertyBindingResult validationResult = this.validateForAdd(contentModel);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }
            this.contentModelManager.addContentModel(contentModel);
            ContentModelDto dto = this.dtoBuilder.convert(contentModel);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error saving a content model", e);
            throw new RestServerError("Error saving a content model", e);
        }
    }

    @Override
    public ContentModelDto update(ContentModelDto entity) {
        try {
            long modelId = entity.getId();
            ContentModel contentModel = this.contentModelManager.getContentModel(entity.getId());
            if (null == contentModel) {
                logger.warn("no contentModel found with id {}", modelId);
                throw new RestRourceNotFoundException(ContentModelValidator.ERRCODE_CONTENTMODEL_NOT_FOUND, "contentModel", String.valueOf(modelId));
            }

            BeanPropertyBindingResult validationResult = this.validateForUpdate(entity, contentModel);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }
            this.copyProperties(entity, contentModel);

            this.contentModelManager.updateContentModel(contentModel);
            ContentModelDto dto = this.dtoBuilder.convert(contentModel);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error saving a content model", e);
            throw new RestServerError("Error saving a content model", e);
        }
    }

    @Override
    public void delete(Long modelId) {
        try {
            ContentModel contentModel = this.contentModelManager.getContentModel(modelId);
            if (null == contentModel) {
                logger.info("contentModel {} does not exists", modelId);
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateForDelete(contentModel);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }
            this.contentModelManager.removeContentModel(contentModel);
        } catch (ApsSystemException e) {
            logger.error("Error in delete contentModel {}", modelId, e);
            throw new RestServerError("error in delete contentModel", e);
        }
    }

    @Override
    public List<ContentModelReference> getContentModelReferences(Long modelId) {
        ContentModel contentModel = this.contentModelManager.getContentModel(modelId);
        if (null == contentModel) {
            logger.info("contentModel {} does not exists", modelId);
            throw new RestRourceNotFoundException(ContentModelValidator.ERRCODE_CONTENTMODEL_NOT_FOUND, "contentModel", String.valueOf(modelId));
        }
        return this.contentModelManager.getContentModelReferences(modelId);
    }

    @Override
    public IEntityModelDictionary getContentModelDictionary(String typeCode) {
        if (StringUtils.isBlank(typeCode)) {
            return this.dictionaryProvider.buildDictionary();
        }
        IApsEntity prototype = this.contentManager.getEntityPrototype(typeCode);
        if (null == prototype) {
            logger.warn("no contentModel found with id {}", typeCode);
            throw new RestRourceNotFoundException(ContentModelValidator.ERRCODE_CONTENTMODEL_TYPECODE_NOT_FOUND, "contentType", typeCode);
        }
        return this.dictionaryProvider.buildDictionary(prototype);
    }

    protected ContentModel createContentModel(ContentModelDto src) {
        ContentModel contentModel = new ContentModel();
        this.copyProperties(src, contentModel);
        return contentModel;
    }

    protected void copyProperties(ContentModelDto src, ContentModel dest) {
        dest.setContentShape(src.getContentShape());
        dest.setContentType(src.getContentType());
        dest.setDescription(src.getDescr());
        dest.setId(src.getId());
        dest.setStylesheet(src.getStylesheet());
    }

    protected BeanPropertyBindingResult validateForAdd(ContentModel contentModel) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(contentModel, "contentModel");
        validateIdIsUnique(contentModel, errors);
        validateContentType(contentModel, errors);
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(ContentModel contentModel) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(contentModel, "contentModel");
        List<ContentModelReference> references = this.contentModelManager.getContentModelReferences(contentModel.getId());
        if (!references.isEmpty()) {
            errors.reject(ContentModelValidator.ERRCODE_CONTENTMODEL_REFERENCES, null, "contentmodel.page.references");
        }
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(ContentModelDto request, ContentModel contentModel) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(contentModel, "contentModel");
        this.validateContentTypeIsEquals(request.getContentType(), contentModel.getContentType(), errors);
        this.validateContentType(contentModel, errors);
        return errors;
    }

    protected void validateContentTypeIsEquals(String newContentType, String existingConentType, BeanPropertyBindingResult errors) {
        if (!newContentType.equals(existingConentType)) {
            Object[] args = {existingConentType, newContentType};
            errors.reject(ContentModelValidator.ERRCODE_CONTENTMODEL_CANNOT_UPDATE_CONTENT_TYPE, args, "contentmodel.contentType.locked");
        }
    }

    protected void validateIdIsUnique(ContentModel contentModel, BeanPropertyBindingResult errors) {
        long modelId = contentModel.getId();

        ContentModel dummyModel = this.contentModelManager.getContentModel(modelId);
        if (dummyModel != null) {
            Object[] args = {String.valueOf(modelId)};
            errors.reject(ContentModelValidator.ERRCODE_CONTENTMODEL_ALREADY_EXISTS, args, "contentmodel.id.already.present");
        }
        SmallEntityType utilizer = this.contentModelManager.getDefaultUtilizer(modelId);
        if (null != utilizer && !utilizer.getCode().equals(contentModel.getContentType())) {
            Object[] args = {String.valueOf(modelId), utilizer.getDescription()};
            errors.reject(ContentModelValidator.ERRCODE_CONTENTMODEL_WRONG_UTILIZER, args, "contentModel.id.wrongUtilizer");
        }
    }

    protected void validateContentType(ContentModel contentModel, BeanPropertyBindingResult errors) {
        String contentType = contentModel.getContentType();

        if (!this.contentManager.getSmallContentTypesMap().containsKey(contentType)) {
            Object[] args = {contentType};
            errors.reject(ContentModelValidator.ERRCODE_CONTENTMODEL_TYPECODE_NOT_FOUND, args, "contentModel.contentType.notFound");
        }
    }
}
