package org.entando.entando.plugins.jacms.aps.system.services.resource;

import static org.entando.entando.plugins.jacms.web.resource.ResourcesController.ERRCODE_CATEGORY_NOT_FOUND;
import static org.entando.entando.plugins.jacms.web.resource.ResourcesController.ERRCODE_GROUP_NOT_FOUND;
import static org.entando.entando.plugins.jacms.web.resource.ResourcesController.ERRCODE_INVALID_FILE_TYPE;
import static org.entando.entando.plugins.jacms.web.resource.ResourcesController.ERRCODE_INVALID_RESOURCE_TYPE;
import static org.entando.entando.plugins.jacms.web.resource.ResourcesController.ERRCODE_RESOURCE_NOT_FOUND;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.FieldSearchFilter.LikeOptionType;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsException;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AttachResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.BaseResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResourceDimension;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.util.IImageDimensionReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.plugins.jacms.web.resource.model.AssetDto;
import org.entando.entando.plugins.jacms.web.resource.model.FileAssetDto;
import org.entando.entando.plugins.jacms.web.resource.model.ImageAssetDto;
import org.entando.entando.plugins.jacms.web.resource.model.ImageMetadataDto;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Slf4j
@Service
public class ResourcesService {
    public static final String PERMISSION_MANAGE_RESOURCES = "manageResources";

    @Autowired
    private IResourceManager resourceManager;

    @Autowired
    private ICategoryManager categoryManager;

    @Autowired
    private IImageDimensionReader imageDimensionManager;

    @Autowired
    private IAuthorizationManager authorizationManager;

    @Value("#{'${jacms.imageResource.allowedExtensions}'.split(',')}")
    private List<String> imageAllowedExtensions;

    @Value("#{'${jacms.attachResource.allowedExtensions}'.split(',')}")
    private List<String> fileAllowedExtensions;

    public PagedMetadata<AssetDto> listAssets(String type, RestListRequest requestList) {
        List<AssetDto> assets = new ArrayList<>();
        try {
            List<String> resourceIds = resourceManager.searchResourcesId(createSearchFilters(type, requestList),
                    extractCategoriesFromFilters(requestList));

            for(String id : resourceIds) {
                assets.add(convertResourceToDto(resourceManager.loadResource(id)));
            }

        } catch (ApsException e) {
            throw new RestServerError("plugins.jacms.resources.resourceManager.error.list", e);
        }

        SearcherDaoPaginatedResult<AssetDto> paginatedResult = new SearcherDaoPaginatedResult<>(assets.size(), assets);
        PagedMetadata<AssetDto> pagedResults = new PagedMetadata<>(requestList, paginatedResult);
        pagedResults.setBody(requestList.getSublist(assets));

        return pagedResults;
    }

    public AssetDto createAsset(String type, MultipartFile file, String group, List<String> categories, UserDetails user) {
        BaseResourceDataBean resourceFile = new BaseResourceDataBean();

        validateMimeType(type, file.getContentType());
        validateGroup(user, group);

        try {
            resourceFile.setInputStream(file.getInputStream());
            resourceFile.setFileSize(file.getBytes().length / 1000);
            resourceFile.setFileName(file.getOriginalFilename());
            resourceFile.setMimeType(file.getContentType());
            resourceFile.setDescr(file.getOriginalFilename());
            resourceFile.setMainGroup(group);
            resourceFile.setResourceType(convertResourceType(type));
            resourceFile.setCategories(convertCategories(categories));
            resourceFile.setOwner(user.getUsername());

            ResourceInterface resource = resourceManager.addResource(resourceFile);
            return convertResourceToDto(resourceManager.loadResource(resource.getId()));
        } catch (ApsSystemException e) {
            throw new RestServerError("plugins.jacms.resources.resourceManager.error.list", e);
        } catch (IOException e) {
            log.error("Error reading file input stream", e);
            throw new RestServerError("plugins.jacms.resources.image.errorReadingStream", e);
        }
    }

    public AssetDto cloneAsset(String resourceId) {

        try {
            ResourceInterface clonedResource = loadResource(resourceId);
            clonedResource.setId(null);
            resourceManager.addResource(clonedResource);

            return convertResourceToDto(resourceManager.loadResource(clonedResource.getId()));
        } catch (ApsSystemException e) {
            throw new RestServerError("plugins.jacms.resources.resourceManager.error.list", e);
        }
    }

    public AssetDto getAsset(String resourceId) {
        try {
            ResourceInterface resource = resourceManager.loadResource(resourceId);
            if (resource == null) {
                throw new ResourceNotFoundException(ERRCODE_RESOURCE_NOT_FOUND, "asset", resourceId);
            }

            return convertResourceToDto(resource);
        } catch (ApsSystemException e) {
            throw new RestServerError("plugins.jacms.resources.resourceManager.error.get", e);
        }
    }

    public void deleteAsset(String resourceId) {
        try {
            ResourceInterface resource = resourceManager.loadResource(resourceId);
            if (resource == null) {
                throw new ResourceNotFoundException(ERRCODE_RESOURCE_NOT_FOUND, "asset", resourceId);
            }
            resourceManager.deleteResource(resource);
        } catch (ApsSystemException e) {
            throw new RestServerError("plugins.jacms.resources.resourceManager.error.delete", e);
        }
    }

    private ResourceInterface loadResource(String resourceId) {
        try {
            ResourceInterface resource = resourceManager.loadResource(resourceId);

            if (resource == null) {
                throw new ResourceNotFoundException(ERRCODE_RESOURCE_NOT_FOUND, "asset", resourceId);
            }

            return resource;
        } catch (ApsSystemException e) {
            throw new RestServerError("plugins.jacms.resources.resourceManager.error.persistence", e);
        }
    }

    public AssetDto editAsset(String resourceId, MultipartFile file, String description, List<String> categories) {
        try {
            ResourceInterface resource = loadResource(resourceId);

            BaseResourceDataBean resourceFile = new BaseResourceDataBean();
            resourceFile.setResourceType(resource.getType());
            resourceFile.setResourceId(resourceId);
            resourceFile.setMetadata(resource.getMetadata());
            resourceFile.setOwner(resource.getOwner());


            if (file != null) {
                validateMimeType(unconvertResourceType(resource.getType()), file.getContentType());

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

            resourceFile.setMainGroup(resource.getMainGroup());
            resourceFile.setCategories(convertCategories(categories));

            resourceManager.updateResource(resourceFile);
            return convertResourceToDto(resourceManager.loadResource(resourceId));
        } catch (ApsSystemException e) {
            throw new RestServerError("plugins.jacms.resources.resourceManager.error.persistence", e);
        } catch (IOException e) {
            log.error("Error reading file input stream", e);
            throw new RestServerError("plugins.jacms.resources.image.errorReadingStream", e);
        }
    }

    /****** Auxiliary Methods ******/

    private List<Category> convertCategories(List<String> categories) {
        return categories.stream().map(code -> Optional.ofNullable(categoryManager.getCategory(code))
                .orElseThrow(() -> {
                    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(code, "resources.category");
                    errors.reject(ERRCODE_CATEGORY_NOT_FOUND, "plugins.jacms.category.error.notFound");
                    return new ValidationGenericException(errors);
                }))
                .collect(Collectors.toList());
    }

    public void validateGroup(UserDetails user, String group) {
        List<Group> groups = authorizationManager.getGroupsByPermission(user, PERMISSION_MANAGE_RESOURCES);

        for(Group g : groups) {
            if (g.getAuthority().equals(group)) {
                return;
            }
        }

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(group, "resources.group");
        errors.reject(ERRCODE_GROUP_NOT_FOUND, "plugins.jacms.group.error.notFound");
        throw new ValidationGenericException(errors);
    }

    public void validateMimeType(String resourceType, final String mimeType) {
        String type = Optional.ofNullable(mimeType)
                .map(t -> t.split("/")[1])
                .orElseThrow(() -> {
                    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(mimeType, "resources.file.type");
                    errors.reject(ERRCODE_INVALID_FILE_TYPE, "plugins.jacms.resources.invalidMimeType");
                    return new ValidationGenericException(errors);
                });

        List<String> allowedExtensions;
        if (ImageAssetDto.RESOURCE_TYPE.equals(resourceType)){
            allowedExtensions = imageAllowedExtensions;
        } else if (FileAssetDto.RESOURCE_TYPE.equals(resourceType)){
            allowedExtensions = fileAllowedExtensions;
        } else {
            BeanPropertyBindingResult errors = new BeanPropertyBindingResult(mimeType, "resources.file.type");
            errors.reject(ERRCODE_INVALID_RESOURCE_TYPE, "plugins.jacms.resources.invalidResourceType");
            throw new ValidationGenericException(errors);
        }

        if (!allowedExtensions.contains(type)) {
            BeanPropertyBindingResult errors = new BeanPropertyBindingResult(mimeType, "resources.file.type");
            errors.reject(ERRCODE_INVALID_FILE_TYPE, "plugins.jacms.resources.invalidMimeType");
            throw new ValidationGenericException(errors);
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

        if (type != null) {
            filters.add(
                    new FieldSearchFilter(IResourceManager.RESOURCE_TYPE_FILTER_KEY, convertResourceType(type), false)
            );
        }

        List<String> groups = new ArrayList<>();
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
                    filters.add(createEntitySearchFilter(IResourceManager.RESOURCE_CREATION_DATE_FILTER_KEY, filter));
                    continue;
                case "updatedAt":
                    filters.add(createEntitySearchFilter(IResourceManager.RESOURCE_MODIFY_DATE_FILTER_KEY, filter));
                    continue;
                case "group":
                    groups.add(filter.getValue());
                    continue;
                case "owner":
                    attr = IResourceManager.RESOURCE_OWNER_FILTER_KEY;
                    break;
                default:
                    log.warn("Invalid filter attribute: " + filter.getAttribute());
                    continue;
            }

            filters.add(
                    new FieldSearchFilter(attr, filter.getValue(), useLikeOption)
            );

        }

        if (groups.size() > 0) {
            filters.add(
                    new FieldSearchFilter(IResourceManager.RESOURCE_MAIN_GROUP_FILTER_KEY, groups, false)
            );
        }

        filters.add(createOrderFilter(requestList));

        return filters.stream().toArray(FieldSearchFilter[]::new);
    }

    private EntitySearchFilter createEntitySearchFilter(String attribute, Filter filter) {
        EntitySearchFilter result = null;

        if (FilterOperator.GREATER.getValue().equalsIgnoreCase(filter.getOperator())) {
            result = new EntitySearchFilter(attribute, false, filter.getValue(), null);
        } else if (FilterOperator.LOWER.getValue().equalsIgnoreCase(filter.getOperator())) {
            result = new EntitySearchFilter(attribute, false, null, filter.getValue());
        } else if (FilterOperator.NOT_EQUAL.getValue().equalsIgnoreCase(filter.getOperator())) {
            result = new EntitySearchFilter(attribute, false, filter.getValue(), false);
            result.setNotOption(true);
        } else {
            result = new EntitySearchFilter(attribute, false, filter.getValue(),
                    FilterOperator.LIKE.getValue().equalsIgnoreCase(filter.getOperator()),
                    LikeOptionType.COMPLETE);
        }
        result.setOrder(filter.getOrder());

        return result;
    }

    private EntitySearchFilter createOrderFilter(RestListRequest requestList) {
        String groupBy = requestList.getSort();

        String key = IResourceManager.RESOURCE_DESCR_FILTER_KEY; //default

        if ("description".equals(groupBy)) {
            key = IResourceManager.RESOURCE_DESCR_FILTER_KEY;
        } else if ("updatedAt".equals(groupBy)) {
            key = IResourceManager.RESOURCE_MODIFY_DATE_FILTER_KEY;
        } else if ("createdAt".equals(groupBy)) {
            key = IResourceManager.RESOURCE_CREATION_DATE_FILTER_KEY;
        } else if ("name".equals(groupBy)) {
            key = IResourceManager.RESOURCE_FILENAME_FILTER_KEY;
        } else if ("owner".equals(groupBy)) {
            key = IResourceManager.RESOURCE_OWNER_FILTER_KEY;
        } else if ("group".equals(groupBy)) {
            key = IResourceManager.RESOURCE_MAIN_GROUP_FILTER_KEY;
        }

        EntitySearchFilter filter = new EntitySearchFilter(key, true);
        filter.setOrder(requestList.getDirection().equals(EntitySearchFilter.DESC_ORDER) ?
                EntitySearchFilter.DESC_ORDER : EntitySearchFilter.ASC_ORDER);

        return filter;
    }

    private List<String> extractCategoriesFromFilters(RestListRequest requestList) {
        List<String> categories = new ArrayList<>();

        for (Filter filter : Optional.ofNullable(requestList.getFilters()).orElse(new Filter[]{})) {
            if (filter.getAttribute().equals("categories")) {
                categories.add(filter.getValue());
            }
        }

        return categories;
    }

    public AssetDto convertResourceToDto(ResourceInterface resource) {
        String type = unconvertResourceType(resource.getType());

        if (ImageAssetDto.RESOURCE_TYPE.equals(type)) {
            return convertImageResourceToDto((ImageResource) resource);
        } else if (FileAssetDto.RESOURCE_TYPE.equals(type)) {
            return convertFileResourceToDto((AttachResource) resource);
        } else {
            log.error("Resource type not allowed");
            BeanPropertyBindingResult errors = new BeanPropertyBindingResult(type, "resources.file.type");
            errors.reject(ERRCODE_INVALID_RESOURCE_TYPE, "plugins.jacms.resources.invalidResourceType");
            throw new ValidationGenericException(errors);
        }
    }

    private ImageAssetDto convertImageResourceToDto(ImageResource resource) {
        ImageAssetDto.ImageAssetDtoBuilder builder = ImageAssetDto.builder()
                .id(resource.getId())
                .name(resource.getMasterFileName())
                .description(resource.getDescription())
                .createdAt(resource.getCreationDate())
                .updatedAt(resource.getLastModified())
                .group(resource.getMainGroup())
                .categories(resource.getCategories().stream()
                        .map(Category::getCode).collect(Collectors.toList()))
                .metadata(resource.getMetadata())
                .version(ImageMetadataDto.builder()
                        .path(resource.getImagePath("0"))
                        .size(resource.getDefaultInstance().getFileLength())
                        .build())
                .owner(resource.getOwner());

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
        return FileAssetDto.builder()
                .id(resource.getId())
                .name(resource.getMasterFileName())
                .description(resource.getDescription())
                .createdAt(resource.getCreationDate())
                .updatedAt(resource.getLastModified())
                .group(resource.getMainGroup())
                .path(resource.getAttachPath())
                .size(resource.getDefaultInstance().getFileLength())
                .categories(resource.getCategories().stream()
                        .map(Category::getCode).collect(Collectors.toList()))
                .owner(resource.getOwner())
                .build();
    }

    public String convertResourceType(String type) {
        if (ImageAssetDto.RESOURCE_TYPE.equals(type)) {
            return "Image";
        } else if (FileAssetDto.RESOURCE_TYPE.equals(type)) {
            return "Attach";
        } else {
            throw new RestServerError(String.format("Invalid resource type: %s", type), null);
        }
    }

    public String unconvertResourceType(String resourceType) {
        if ("Image".equals(resourceType)) {
            return ImageAssetDto.RESOURCE_TYPE;
        } else if ("Attach".equals(resourceType)) {
            return FileAssetDto.RESOURCE_TYPE;
        } else {
            throw new RestServerError(String.format("Invalid resource type: %s", resourceType), null);
        }
    }

    public void setResourceManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}
