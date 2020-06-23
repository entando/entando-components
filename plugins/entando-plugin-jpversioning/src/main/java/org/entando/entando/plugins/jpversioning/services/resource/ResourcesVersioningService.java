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
package org.entando.entando.plugins.jpversioning.services.resource;

import static org.entando.entando.plugins.jacms.web.resource.ResourcesController.ERRCODE_INVALID_RESOURCE_TYPE;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.AuthorizationManager;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AttachResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResourceDimension;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.util.IImageDimensionReader;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.TrashedResourceManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.plugins.jpversioning.web.content.validator.VersioningValidatorErrorCodes;
import org.entando.entando.plugins.jpversioning.web.resource.model.FileResourceDTO;
import org.entando.entando.plugins.jpversioning.web.resource.model.ImageResourceDTO;
import org.entando.entando.plugins.jpversioning.web.resource.model.ImageVersionDTO;
import org.entando.entando.plugins.jpversioning.web.resource.model.ResourceDTO;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

@Service
public class ResourcesVersioningService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TrashedResourceManager trashedResourceManager;

    @Autowired
    private AuthorizationManager authorizationManager;

    @Autowired
    private IImageDimensionReader imageDimensionManager;

    public PagedMetadata<ResourceDTO> getTrashedResources(String resourceTypeCode, RestListRequest requestList, UserDetails user) {

        try {
            logger.debug("GET getTrashedResources type {} and req {}", resourceTypeCode, requestList);

            String description = extractAttributeValue(requestList.getFilters(), "description");

            List<String> allowedGroups = getAllowedGroups(user);

            List<String> resourcesId = trashedResourceManager
                    .searchTrashedResourceIds(resourceTypeCode, description, allowedGroups);

            List<ResourceDTO> result = new ArrayList<>();

            for (String id : resourcesId) {
                try {
                    ResourceDTO resource = convertResourceToDto(trashedResourceManager.loadTrashedResource(id));
                    result.add(resource);
                } catch (ApsSystemException e) {
                    logger.error("Error loading trashedResource with id: {}", id);
                }
            }

            final List<ResourceDTO> sublist = requestList.getSublist(result);

            PagedMetadata<ResourceDTO> pagedResults = new PagedMetadata<>(requestList, resourcesId.size());
            pagedResults.setBody(sublist);

            return pagedResults;
        } catch (ApsSystemException e) {
            logger.error("Error while getting trashed resources with request {}", requestList, e);
            throw new RestServerError(String.format("Error while getting trashed resources with request %s", requestList),
                    e);
        }
    }

    public ResourceDTO recoverResource(String resourceId) {

        try {
            logger.debug("POST recover resource {}", resourceId);

            ResourceInterface resource = trashedResourceManager.loadTrashedResource(resourceId);

            if (resource != null) {
                trashedResourceManager.restoreResource(resourceId);
                return convertResourceToDto(resource);
            } else {
                throw new ResourceNotFoundException(
                        VersioningValidatorErrorCodes.ERRCODE_TRASHED_RESOURCE_DOES_NOT_EXIST.value,
                        "Trashed resource: ", resourceId);
            }
        } catch (ApsSystemException e) {
            logger.error("Error while recovering trashed resources with id {}", resourceId, e);
            throw new RestServerError(String.format("Error while recovering trashed resources with id %s", resourceId),
                    e);
        }
    }

    private List<String> getAllowedGroups(UserDetails user) {
        List<String> result = new ArrayList<>();
        List<Group> userGroups = authorizationManager.getUserGroups(user);

        for (int i = 0; i < userGroups.size(); i++) {
            Group group = userGroups.get(i);
            if (group.getName().equals(Group.ADMINS_GROUP_NAME)) {
                result.clear();
                break;
            } else {
                result.add(group.getName());
            }
        }

        return result;
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

    public ResourceDTO convertResourceToDto(ResourceInterface resource) {
        String type = extractTypeFromResource(resource.getType());

        if (FileResourceDTO.RESOURCE_TYPE.equals(type)) {
            return convertResourceToFileResourceDto((AttachResource) resource);
        } else if (ImageResourceDTO.RESOURCE_TYPE.equals(type)) {
            return convertResourceToImageResourceDto((ImageResource) resource);
        } else {
            logger.error("Resource type not supported");
            BeanPropertyBindingResult errors = new BeanPropertyBindingResult(type, "resources.file.type");
            errors.reject(ERRCODE_INVALID_RESOURCE_TYPE, "plugins.jacms.resources.invalidResourceType");
            throw new ValidationGenericException(errors);
        }
    }

    private FileResourceDTO convertResourceToFileResourceDto(AttachResource resource){
        return new FileResourceDTO(resource.getId(), resource.getMasterFileName(), resource.getDescription(),
                resource.getCreationDate(), resource.getLastModified(), resource.getMainGroup(),
                getCategories(resource), resource.getDefaultInstance().getFileLength(), resource.getAttachPath(),
                resource.getOwner(), resource.getFolderPath());
    }

    private ImageResourceDTO convertResourceToImageResourceDto(ImageResource resource) {
        List<ImageVersionDTO> versions = new ArrayList<>();

        for (ImageResourceDimension dimensions : getImageDimensions()) {
            ResourceInstance instance = resource.getInstance(dimensions.getIdDim(), null);

            if (instance == null) {
                logger.warn("ResourceInstance not found for dimensions id {} and image id {}", dimensions.getIdDim(), resource.getId());
                continue;
            }

            String dimension = String.format("%dx%d px", dimensions.getDimx(), dimensions.getDimy());
            String path = resource.getImagePath(String.valueOf(dimensions.getIdDim()));
            String size = instance.getFileLength();

            versions.add(new ImageVersionDTO(dimension, path, size));
        }

        return new ImageResourceDTO(resource.getId(), resource.getMasterFileName(), resource.getDescription(),
                resource.getCreationDate(), resource.getLastModified(), versions, resource.getMainGroup(),
                getCategories(resource), resource.getOwner(), resource.getFolderPath());
    }

    private List<ImageResourceDimension> getImageDimensions() {
        Map<Integer, ImageResourceDimension> master = imageDimensionManager.getImageDimensions();
        List<ImageResourceDimension> dimensions = new ArrayList<>(master.values());
        BeanComparator comparator = new BeanComparator("dimx");
        Collections.sort(dimensions, comparator);
        return dimensions;
    }

    private List<String> getCategories(AbstractResource resource) {
        return resource.getCategories().stream()
                .map(Category::getCode).collect(Collectors.toList());
    }

    public String extractTypeFromResource(String resourceType) {
        if ("Attach".equals(resourceType)) {
            return FileResourceDTO.RESOURCE_TYPE;
        } else if ("Image".equals(resourceType)) {
            return ImageResourceDTO.RESOURCE_TYPE;
        } else {
            throw new RestServerError(String.format("Invalid resource type: %s", resourceType), null);
        }
    }
}
