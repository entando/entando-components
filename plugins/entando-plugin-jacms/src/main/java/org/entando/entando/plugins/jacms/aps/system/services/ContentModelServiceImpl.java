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

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.dictionary.ContentModelDictionaryProvider;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelDto;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.dataobjectmodel.model.IEntityModelDictionary;
import org.entando.entando.plugins.jacms.aps.system.services.contentmodel.ContentModelReferencesRequestListProcessor;
import org.entando.entando.plugins.jacms.aps.system.services.contentmodel.ContentModelRequestListProcessor;
import org.entando.entando.plugins.jacms.web.contentmodel.model.ContentModelReferenceDTO;
import org.entando.entando.plugins.jacms.web.contentmodel.validator.ContentModelValidator;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.component.ComponentUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

@Service
public class ContentModelServiceImpl implements ContentModelService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IContentManager contentManager;
    private final IContentModelManager contentModelManager;
    private final ContentModelDictionaryProvider dictionaryProvider;
    private final IDtoBuilder<ContentModel, ContentModelDto> dtoBuilder;
    private final ContentTypeService contentTypeService;

    @Autowired
    public ContentModelServiceImpl(IContentManager contentManager, IContentModelManager contentModelManager,
            ContentModelDictionaryProvider dictionaryProvider,
            ContentTypeService contentTypeService) {
        this.contentManager = contentManager;
        this.contentModelManager = contentModelManager;
        this.dictionaryProvider = dictionaryProvider;
        this.contentTypeService = contentTypeService;

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

        List<ContentModel> contentModels = new ContentModelRequestListProcessor(
                requestList, this.contentModelManager.getContentModels())
                .filterAndSort().toList();

        //page
        List<ContentModel> subList = requestList.getSublist(contentModels);
        List<ContentModelDto> dtoSlice = this.dtoBuilder.convert(subList);
        SearcherDaoPaginatedResult<ContentModelDto> paginatedResult = new SearcherDaoPaginatedResult<>(contentModels.size(), dtoSlice);
        PagedMetadata<ContentModelDto> pagedMetadata = new PagedMetadata<>(requestList, paginatedResult);
        pagedMetadata.setBody(dtoSlice);
        return pagedMetadata;
    }

    @Override
    public ContentModelDto getContentModel(Long modelId) {
        ContentModel contentModel = this.contentModelManager.getContentModel(modelId);
        if (null == contentModel) {
            logger.warn("no contentModel found with id {}", modelId);
            throw new ResourceNotFoundException(ContentModelValidator.ERRCODE_CONTENTMODEL_NOT_FOUND, "contentModel", String.valueOf(modelId));
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
                throw new ResourceNotFoundException(ContentModelValidator.ERRCODE_CONTENTMODEL_NOT_FOUND, "contentModel", String.valueOf(modelId));
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
    public ComponentUsage getComponentUsage(Long modelId) {
        final List<ContentModelReference> contentModelReferences = contentModelManager
                .getContentModelReferences(modelId);
        final long onlineCount = contentModelReferences.stream()
                .filter(f -> f.isOnline()).count();
        final long offlineCount = contentModelReferences.stream()
                .filter(f -> !f.isOnline()).count();
        final List<SmallEntityType> defaultContentTemplateUsedList = this.contentManager.getSmallEntityTypes().stream()
                .filter(
                        f -> {
                            final String defaultModel = contentManager.getDefaultModel(f.getCode());
                            final boolean defaultModelUsed = String.valueOf(modelId)
                                    .equals(defaultModel);
                            return defaultModelUsed;
                        }
                ).collect(Collectors.toList());
        final List<SmallEntityType> defaultContentListTemplateUsedList = this.contentManager.getSmallEntityTypes()
                .stream()
                .filter(
                        f -> {
                            final String listModel = contentManager.getListModel(f.getCode());
                            final boolean listModelUsed = String.valueOf(modelId)
                                    .equals(listModel);
                            return listModelUsed;
                        }
                ).collect(Collectors.toList());
        int countContentDefaultTemplateReferences = defaultContentTemplateUsedList.size();
        int countContentListDefaultTemplateReferences = defaultContentListTemplateUsedList.size();
        Integer usage = Math.toIntExact(onlineCount + offlineCount + countContentDefaultTemplateReferences
                + countContentListDefaultTemplateReferences);
        ComponentUsage componentUsage = new ComponentUsage();
        componentUsage.setType("contentTemplate");
        componentUsage.setCode(String.valueOf(modelId));
        componentUsage.setStatus("");
        componentUsage.setUsage(usage);
        return componentUsage;
    }

    @Override
    public PagedMetadata<ContentModelReferenceDTO> getContentModelReferences(Long modelId, RestListRequest requestList) {
        ContentModel contentModel = this.contentModelManager.getContentModel(modelId);
        if (null == contentModel) {
            logger.debug("contentModel {} does not exists", modelId);
            throw new ResourceNotFoundException(ContentModelValidator.ERRCODE_CONTENTMODEL_NOT_FOUND, "contentModel", String.valueOf(modelId));
        }

        final List<ContentModelReference> contentModelReferences = new ContentModelReferencesRequestListProcessor(
                requestList, this.contentModelManager
                .getContentModelReferences(modelId))
                .filterAndSort().toList();

        final List<ContentModelReference> subList = requestList.getSublist(contentModelReferences);
        final List<ContentModelReferenceDTO> contentModelReferenceDTOS = mapContentModelReferencesToDTOs(subList);
        SearcherDaoPaginatedResult<ContentModelReferenceDTO> paginatedResult = new SearcherDaoPaginatedResult<>(contentModelReferences.size(), contentModelReferenceDTOS);
        PagedMetadata<ContentModelReferenceDTO> pagedMetadata = new PagedMetadata<>(requestList, paginatedResult);
        pagedMetadata.setBody(contentModelReferenceDTOS);
        return pagedMetadata;
    }

    private List<ContentModelReferenceDTO> mapContentModelReferencesToDTOs(List<ContentModelReference> contentModelReferences) {
        final List<ContentModelReferenceDTO> contentModelReferencesDTOs = contentModelReferences.stream().map(cmr ->
                mapContentModelReferenceToDTO(cmr)
        ).collect(Collectors.toList());
        return contentModelReferencesDTOs;
    }

    private ContentModelReferenceDTO mapContentModelReferenceToDTO(ContentModelReference cmr) {
        ContentModelReferenceDTO contentModelReferenceDTO = new ContentModelReferenceDTO();
        contentModelReferenceDTO.setPageCode(cmr.getPageCode());
        contentModelReferenceDTO.setOnline(cmr.isOnline());
        contentModelReferenceDTO.setWidgetPosition(cmr.getWidgetPosition());
        return contentModelReferenceDTO;
    }

    @Override
    public IEntityModelDictionary getContentModelDictionary(String typeCode) {
        if (StringUtils.isBlank(typeCode)) {
            return this.dictionaryProvider.buildDictionary();
        }
        IApsEntity prototype = this.contentManager.getEntityPrototype(typeCode);
        if (null == prototype) {
            logger.warn("no contentModel found with id {}", typeCode);
            throw new ResourceNotFoundException(ContentModelValidator.ERRCODE_CONTENTMODEL_TYPECODE_NOT_FOUND, "contentType", typeCode);
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
        validateContentType(contentModel.getContentType(), errors);
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(ContentModel contentModel) throws ApsSystemException {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(contentModel, "contentModel");
        List<ContentModelReference> references = this.contentModelManager.getContentModelReferences(contentModel.getId());
        if (!references.isEmpty()) {
            errors.reject(ContentModelValidator.ERRCODE_CONTENTMODEL_REFERENCES, null, "contentmodel.page.references");
        }
        final List<SmallEntityType> defaultContentTemplateUsedList = this.contentManager.getSmallEntityTypes().stream()
                .filter(
                        f -> {
                            final String defaultModel = contentManager.getDefaultModel(f.getCode());
                            final boolean defaultModelUsed = String.valueOf(contentModel.getId())
                                    .equals(defaultModel);
                            final String listModel = contentManager.getListModel(f.getCode());
                            final boolean listModelUsed = String.valueOf(contentModel.getId())
                                    .equals(listModel);
                            return (defaultModelUsed || listModelUsed);
                        }
                ).collect(Collectors.toList());

        if (defaultContentTemplateUsedList.size()>0){
            ArrayList defList = new ArrayList();
            defaultContentTemplateUsedList.stream().forEach(f->defList.add(f.getCode()));
            final String defListString = String.join(", ", defList);
            ArrayList args = new ArrayList();
            args.add(defListString);
            errors.reject(ContentModelValidator.ERRCODE_CONTENTMODEL_METADATA_REFERENCES, args.toArray(), "contentmodel.defaultMetadata.references");
        }
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(ContentModelDto request, ContentModel contentModel) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(contentModel, "contentModel");
        this.validateContentType(request.getContentType(), errors);
        return errors;
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
            errors.reject(ContentModelValidator.ERRCODE_CONTENTMODEL_WRONG_UTILIZER, args, "contentmodel.id.wrongUtilizer");
        }
    }

    protected void validateContentType(String contentType, BeanPropertyBindingResult errors) {
        if (!this.contentManager.getSmallContentTypesMap().containsKey(contentType)) {
            Object[] args = {contentType};
            errors.reject(ContentModelValidator.ERRCODE_CONTENTMODEL_TYPECODE_NOT_FOUND, args, "contentmodel.contentType.notFound");
        }
    }
}
