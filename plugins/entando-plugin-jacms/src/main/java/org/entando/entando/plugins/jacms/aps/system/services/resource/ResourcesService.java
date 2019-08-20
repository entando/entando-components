package org.entando.entando.plugins.jacms.aps.system.services.resource;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsException;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.*;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.util.IImageDimensionReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.plugins.jacms.web.resource.model.AssetDto;
import org.entando.entando.plugins.jacms.web.resource.model.FileAssetDto;
import org.entando.entando.plugins.jacms.web.resource.model.ImageAssetDto;
import org.entando.entando.plugins.jacms.web.resource.model.ImageMetadataDto;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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

    @Value("#{'${jacms.imageResource.allowedExtensions}'.split(',')}")
    private List<String> imageAllowedExtensions;

    @Value("#{'${jacms.attachResource.allowedExtensions}'.split(',')}")
    private List<String> fileAllowedExtensions;

    public PagedMetadata<AssetDto> listAssets(String resourceType, RestListRequest requestList) {
        List<AssetDto> assets = new ArrayList<>();
        try {
            List<String> resourceIds = resourceManager.searchResourcesId(createSearchFilters(resourceType, requestList),
                    extractCategoriesFromFilters(requestList));

            for(String id : resourceIds) {
                assets.add(convertResourceToDto(resourceType, resourceManager.loadResource(id)));
            }

        } catch (ApsException e) {
            throw new RestServerError("Error listing image resources", e);
        }

        SearcherDaoPaginatedResult<AssetDto> paginatedResult = new SearcherDaoPaginatedResult<>(assets.size(), assets);
        PagedMetadata<AssetDto> pagedResults = new PagedMetadata<>(requestList, paginatedResult);
        pagedResults.setBody(assets);

        return pagedResults;
    }

    public AssetDto createAsset(String resourceType, MultipartFile file, String group, List<String> categories) {
        BaseResourceDataBean resourceFile = new BaseResourceDataBean();

        validateMimeType(resourceType, file.getContentType());

        try {
            resourceFile.setInputStream(file.getInputStream());
            resourceFile.setFileSize(file.getBytes().length / 1000);
            resourceFile.setFileName(file.getOriginalFilename());
            resourceFile.setMimeType(file.getContentType());
            resourceFile.setDescr(file.getOriginalFilename());
            resourceFile.setMainGroup(group);
            resourceFile.setResourceType(resourceType);
            resourceFile.setCategories(categories.stream()
                .map(code -> categoryManager.getCategory(code)).collect(Collectors.toList()));

            ResourceInterface resource = resourceManager.addResource(resourceFile);
            return convertResourceToDto(resourceType, resourceManager.loadResource(resource.getId()));
        } catch (ApsSystemException e) {
            throw new RestServerError("Error saving new resource", e);
        } catch (IOException e) {
            throw new RestServerError("Error reading resource stream", e);
        }
    }

    public void deleteAsset(String resourceId) {
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

    public AssetDto editAsset(String resourceId, MultipartFile file, String description, String group, List<String> categories) {
        try {
            ResourceInterface resource = resourceManager.loadResource(resourceId);
            if (resource == null) {
                throw new RestServerError(String.format("Resource %s not found", resourceId), null);
            }

            BaseResourceDataBean resourceFile = new BaseResourceDataBean();
            resourceFile.setResourceType(resource.getType());
            resourceFile.setResourceId(resourceId);
            resourceFile.setMetadata(resource.getMetadata());


            if (file != null) {
                validateMimeType(resource.getType(), file.getContentType());

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
            return convertResourceToDto(resource.getType(), resourceManager.loadResource(resourceId));
        } catch (ApsSystemException e) {
            throw new RestServerError("Error updating image resource", e);
        } catch (IOException e) {
            throw new RestServerError("Error reading image stream", e);
        }
    }

    /****** Auxiliary Methods ******/

    private void validateMimeType(String resourceType, String mimeType) {
        mimeType = Optional.ofNullable(mimeType)
                .map(t -> t.split("/")[1])
                .orElseThrow(() -> new RestServerError("Invalid mime type", null));

        List<String> allowedExtensions;
        if ("Image".equals(resourceType)){
            allowedExtensions = imageAllowedExtensions;
        } else if ("Attach".equals(resourceType)){
            allowedExtensions = fileAllowedExtensions;
        } else throw new RestServerError(String.format("Invalid resource type %s", resourceType), null);

        if (!allowedExtensions.contains(mimeType)) {
            throw new RestServerError(String.format("Invalid mime type: %s", mimeType), null);
        }
    }

    private List<ImageResourceDimension> getImageDimensions() {
        Map<Integer, ImageResourceDimension> master = imageDimensionManager.getImageDimensions();
        List<ImageResourceDimension> dimensions = new ArrayList<>(master.values());
        BeanComparator comparator = new BeanComparator("dimx");
        Collections.sort(dimensions, comparator);
        return dimensions;
    }

    private FieldSearchFilter[] createSearchFilters(String type, RestListRequest requestList) {
        List<FieldSearchFilter> filters = new ArrayList<>();
        filters.add(
            new FieldSearchFilter(IResourceManager.RESOURCE_TYPE_FILTER_KEY, type, false)
        );

        //TODO better way to convert attributes to ResourceManager properties?
        for (Filter filter : Optional.ofNullable(requestList.getFilters()).orElse(new Filter[]{})) {
            String attr;
            boolean useLikeOption = false;
            switch (filter.getAttribute()) {
                case "name":
                    attr = IResourceManager.RESOURCE_FILENAME_FILTER_KEY;
                    useLikeOption = true;
                    break;
                case "description":
                    attr = IResourceManager.RESOURCE_DESCR_FILTER_KEY;
                    useLikeOption = true;
                    break;
                case "createdAt":
                    attr = IResourceManager.RESOURCE_CREATION_DATE_FILTER_KEY;
                    break;
                case "updatedAt":
                    attr = IResourceManager.RESOURCE_MODIFY_DATE_FILTER_KEY;
                    break;
                case "group":
                    attr = IResourceManager.RESOURCE_MAIN_GROUP_FILTER_KEY;
                    break;
                default:
                    continue;
            }

            filters.add(
                new FieldSearchFilter(attr, filter.getValue(), useLikeOption)
            );
        }

        filters.add(createOrderFilter(requestList));

        return filters.stream().toArray(FieldSearchFilter[]::new);
    }

    private EntitySearchFilter createOrderFilter(RestListRequest requestList) {
        String groupBy = requestList.getSort();

        String key = IResourceManager.RESOURCE_DESCR_FILTER_KEY; //default

        //TODO better way to convert attributes to ResourceManager properties?
        if ("description".equals(groupBy)) {
            key = IResourceManager.RESOURCE_DESCR_FILTER_KEY;
        } else if ("updatedAt".equals(groupBy)) {
            key = IResourceManager.RESOURCE_MODIFY_DATE_FILTER_KEY;
        } else if ("createdAt".equals(groupBy)) {
            key = IResourceManager.RESOURCE_CREATION_DATE_FILTER_KEY;
        } else if ("name".equals(groupBy)) {
            key = IResourceManager.RESOURCE_FILENAME_FILTER_KEY;
        }

        EntitySearchFilter filter = new EntitySearchFilter(key, false);
        filter.setOrder(requestList.getDirection().equals("desc") ?
                EntitySearchFilter.DESC_ORDER : EntitySearchFilter.ASC_ORDER);

        return filter;
    }

    //TODO support array of categories
    private List<String> extractCategoriesFromFilters(RestListRequest requestList) {
        List<String> categories = new ArrayList<>();

        for (Filter filter : Optional.ofNullable(requestList.getFilters()).orElse(new Filter[]{})) {
            if (filter.getAttribute().equals("categories")) {
                categories.add(filter.getValue());
            }
        }

        return categories;
    }

    private AssetDto convertResourceToDto(String resourceType, ResourceInterface resource) {
        if ("Image".equals(resourceType)) {
            return convertImageResourceToDto((ImageResource) resource);
        } else if ("Attach".equals(resourceType)) {
            return convertFileResourceToDto((AttachResource) resource);
        } else {
            throw new RestServerError(String.format("Invalid resource type: %s", resourceType), null);
        }
    }

    //TODO better encapsulate dto conversion, perhaps create separate builder?
    private ImageAssetDto convertImageResourceToDto(ImageResource resource) {
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

    private FileAssetDto convertFileResourceToDto(AttachResource resource){
        FileAssetDto.FileAssetDtoBuilder builder = FileAssetDto.builder()
                .id(resource.getId())
                .name(resource.getMasterFileName())
                .description(resource.getDescription())
                .createdAt(resource.getCreationDate())
                .updatedAt(resource.getLastModified())
                .group(resource.getMainGroup())
                .path(resource.getAttachPath())
                .size(resource.getDefaultInstance().getFileLength());

        for (Category category : resource.getCategories()) {
            builder.category(category.getCode());
        }

        return builder.build();
    }

    public void setResourceManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}