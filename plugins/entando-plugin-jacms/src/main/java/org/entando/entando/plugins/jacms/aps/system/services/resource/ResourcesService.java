package org.entando.entando.plugins.jacms.aps.system.services.resource;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsException;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.*;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.util.IImageDimensionReader;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.plugins.jacms.web.resource.model.FileAssetDto;
import org.entando.entando.plugins.jacms.web.resource.model.ImageAssetDto;
import org.entando.entando.plugins.jacms.web.resource.model.ImageMetadataDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ResourcesService {
    @Autowired
    private IResourceManager resourceManager;

    @Autowired
    private ICategoryManager categoryManager;

    @Autowired
    private IImageDimensionReader imageDimensionManager;

    public PagedMetadata<ImageAssetDto> listImageAssets(RestListRequest requestList) {
        List<ImageAssetDto> assets = new ArrayList<>();
        try {
            List<String> resourceIds = resourceManager.searchResourcesId(createSearchFilters("Image"), null, null);

            for(String id : resourceIds) {
                assets.add(convertImageResorceToDto((ImageResource) resourceManager.loadResource(id)));
            }

        } catch (ApsException e) {
            throw new RestServerError("Error listing image resources", e);
        }

        SearcherDaoPaginatedResult<ImageAssetDto> paginatedResult = new SearcherDaoPaginatedResult<>(assets.size(), assets);
        PagedMetadata<ImageAssetDto> pagedResults = new PagedMetadata<>(requestList, paginatedResult);
        pagedResults.setBody(assets);

        return pagedResults;
    }

    public ImageAssetDto createImageAsset(MultipartFile file, String group, List<String> categories) {
        //TODO handle possible formats from config file
        BaseResourceDataBean resourceFile = new BaseResourceDataBean();

        try {
            resourceFile.setInputStream(file.getInputStream());
            resourceFile.setFileSize(file.getBytes().length / 1000);
            resourceFile.setFileName(file.getOriginalFilename());
            resourceFile.setMimeType(file.getContentType());
            resourceFile.setDescr(file.getOriginalFilename());
            resourceFile.setMainGroup(group);
            resourceFile.setResourceType("Image");
            resourceFile.setCategories(categories.stream()
                .map(code -> categoryManager.getCategory(code)).collect(Collectors.toList()));

            ImageResource resource = (ImageResource) resourceManager.addResource(resourceFile);
            return convertImageResorceToDto(resource);
        } catch (ApsSystemException e) {
            throw new RestServerError("Error saving new image resource", e);
        } catch (IOException e) {
            throw new RestServerError("Error reading image stream", e);
        }
    }

    public void deleteImageAsset(String resourceId) {
        try {
            ResourceInterface resource = resourceManager.loadResource(resourceId);
            if (resource == null) {
                throw new RestServerError(String.format("Resource %s not found", resourceId), null);
            }
            resourceManager.deleteResource(resource);
        } catch (ApsSystemException e) {
            throw new RestServerError("Error deleting image resource", e);
        }
    }

    public ImageAssetDto editImageAsset(String resourceId, MultipartFile file, String description, String group, List<String> categories) {
        try {
            ResourceInterface resource = resourceManager.loadResource(resourceId);
            if (resource == null) {
                throw new RestServerError(String.format("Resource %s not found", resourceId), null);
            }

            BaseResourceDataBean resourceFile = new BaseResourceDataBean();
            resourceFile.setResourceType("Image");
            resourceFile.setResourceId(resourceId);
            resourceFile.setMetadata(resource.getMetadata());


            if (file != null) {
                resourceFile.setInputStream(file.getInputStream());
                resourceFile.setFileSize(file.getBytes().length / 1000);
                resourceFile.setFileName(file.getOriginalFilename());
                resourceFile.setMimeType(file.getContentType());
                resourceFile.setDescr(file.getOriginalFilename());

                if ((description == null || description.trim().length() == 0 ) && resource.getDescription().equals(resource.getMasterFileName())) {
                    description = resourceFile.getFileName();
                }
            }

            if (description != null && !description.trim().isEmpty()) {
                resourceFile.setDescr(description.trim());
            } else {
                resourceFile.setDescr(resource.getDescription());
            }

            if (group != null && !group.trim().isEmpty()) {
                resourceFile.setMainGroup(group.trim());
            } else {
                resourceFile.setMainGroup(resource.getMainGroup());
            }

            if (categories != null && categories.size() > 0) {
                resourceFile.setCategories(categories.stream()
                        .map(code -> categoryManager.getCategory(code)).collect(Collectors.toList()));
            } else {
                resourceFile.setCategories(resource.getCategories());
            }

            resourceManager.updateResource(resourceFile);
            return convertImageResorceToDto((ImageResource) resourceManager.loadResource(resourceId));
        } catch (ApsSystemException e) {
            throw new RestServerError("Error updating image resource", e);
        } catch (IOException e) {
            throw new RestServerError("Error reading image stream", e);
        }
    }

    public PagedMetadata<String> listFileAssets(RestListRequest requestList) {
        return null;
    }

    public FileAssetDto createFileAsset(MultipartFile asset) {
        return null;
    }

    /****** Auxiliary Methods ******/

    private List<ImageResourceDimension> getImageDimensions() {
        Map<Integer, ImageResourceDimension> master = imageDimensionManager.getImageDimensions();
        List<ImageResourceDimension> dimensions = new ArrayList<>(master.values());
        BeanComparator comparator = new BeanComparator("dimx");
        Collections.sort(dimensions, comparator);
        return dimensions;
    }

    private FieldSearchFilter[] createSearchFilters(String type) {
        FieldSearchFilter typeCodeFilter = new FieldSearchFilter(IResourceManager.RESOURCE_TYPE_FILTER_KEY, type, false);
        FieldSearchFilter[] filters = {typeCodeFilter};
        /*if (!StringUtils.isBlank(this.getOwnerGroupName())) {
            FieldSearchFilter groupFilter = new FieldSearchFilter(IResourceManager.RESOURCE_MAIN_GROUP_FILTER_KEY, this.getOwnerGroupName(), false);
            filters = ArrayUtils.add(filters, groupFilter);
        }
        if (!StringUtils.isBlank(this.getText())) {
            FieldSearchFilter textFilter = new FieldSearchFilter(IResourceManager.RESOURCE_DESCR_FILTER_KEY, this.getText(), true);
            filters = ArrayUtils.add(filters, textFilter);
        }
        if (!StringUtils.isBlank(this.getFileName())) {
            FieldSearchFilter filenameFilter = new FieldSearchFilter(IResourceManager.RESOURCE_FILENAME_FILTER_KEY, this.getFileName(), true);
            filters = ArrayUtils.add(filters, filenameFilter);
        }
        filters = ArrayUtils.add(filters, this.getOrderFilter());*/
        return filters;
    }

    private ImageAssetDto convertImageResorceToDto(ImageResource resource){
        ImageAssetDto.ImageAssetDtoBuilder builder = ImageAssetDto.builder()
                .id(resource.getId())
                .name(resource.getMasterFileName())
                .description(resource.getDescription())
                .createdAt(resource.getCreationDate())
                .updatedAt(resource.getLastModified())
                .group(resource.getMainGroup())
                .metadata(resource.getMetadata())
                .version(ImageMetadataDto.builder()
                        .path(resource.getImagePath("0"))
                        .size(resource.getDefaultInstance().getFileLength())
                        .build());

        for (Category category : resource.getCategories()) {
            builder.category(category.getCode());
        }

        for (ImageResourceDimension dimensions : getImageDimensions()) {
            ResourceInstance instance = resource.getInstance(dimensions.getIdDim(), null);

            if (instance == null) {
                log.warn("ResourceInstance not found for dimensions id {} and image id {}", dimensions.getIdDim(), resource.getId());
                continue;
            }

            builder.version(ImageMetadataDto.builder()
                    .path(resource.getImagePath(String.valueOf(dimensions.getIdDim())))
                    .size(instance.getFileLength())
                    .dimensions(String.format("%dx%d px", dimensions.getDimx(), dimensions.getDimy()))
                    .build());
        }

        return builder.build();
    }

    public void setResourceManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}
